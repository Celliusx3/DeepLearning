package com.malevent.pro.deeplearning.Interactor.ViewModel.Impl;

import android.graphics.Bitmap;
import android.graphics.RectF;
import android.util.Log;

import com.malevent.pro.deeplearning.Data.DeepLearningConstants.ImageDetectionConstants;
import com.malevent.pro.deeplearning.Interactor.Scheduler.BaseSchedulerProvider;
import com.malevent.pro.deeplearning.Interactor.ViewModel.BaseViewModel;
import com.malevent.pro.deeplearning.Interactor.ViewModel.ImageDetectionViewModel;
import com.malevent.pro.deeplearning.ObjectRecognition;
import com.malevent.pro.deeplearning.Util.MathUtil;

import org.tensorflow.contrib.android.TensorFlowInferenceInterface;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

import io.reactivex.Observable;

/**
 * Created by OCCOYANC on 3/12/2018.
 */

public class ImageDetectionViewModelImpl extends BaseViewModel implements ImageDetectionViewModel{

    private static final String TAG = ImageDetectionViewModelImpl.class.getSimpleName();

    private static final String YOLO_MODEL_FILE =  "file:///android_asset/tiny-yolo-voc.pb";
    private static final String YOLO_INPUT_NAME = "input";
    private static final String YOLO_OUTPUT_NAMES = "output";
    private static final int YOLO_INPUT_SIZE = 416;
    private static final int YOLO_BLOCK_SIZE = 32;

    private static final int MAX_RESULTS = 5;
    private static final int NUM_CLASSES = 20;
    private static final int NUM_BOXES_PER_BLOCK = 5;

    private static final float MINIMUM_CONFIDENCE_YOLO = 0.25f;

    private ImageDetectionConstants imageDetectionConstants;
    private TensorFlowInferenceInterface inferenceInterface;
    private Double[] anchors;
    private String[] labels;

    public ImageDetectionViewModelImpl(ImageDetectionConstants imageDetectionConstants,
                                       BaseSchedulerProvider scheduler) {
        super(scheduler);
        this.imageDetectionConstants = imageDetectionConstants;
        anchors = imageDetectionConstants.getAnchors();
        labels = imageDetectionConstants.getLabels();
        inferenceInterface = new TensorFlowInferenceInterface(imageDetectionConstants.getAssetManager(), YOLO_MODEL_FILE);
    }

    @Override
    public Observable<List<ObjectRecognition>> getObjectRecognition(Bitmap bitmap) {

        // Log this method so that it can be analyzed with systrace.
        Log.d(TAG, "Recognize image");

        Log.d(TAG,"Preprocess bitmap start");
        // Preprocess the image data from 0-255 int to normalized float based
        // on the provided parameters.
        int[] intValues = new int[bitmap.getWidth() * bitmap.getHeight()];
        float[] floatValues = new float[bitmap.getWidth() * bitmap.getHeight() * 3];
        bitmap.getPixels(intValues, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());

        for (int i = 0; i < intValues.length; ++i) {
            floatValues[i * 3 + 0] = ((intValues[i] >> 16) & 0xFF) / 255.0f;
            floatValues[i * 3 + 1] = ((intValues[i] >> 8) & 0xFF) / 255.0f;
            floatValues[i * 3 + 2] = (intValues[i] & 0xFF) / 255.0f;
        }

        Log.d(TAG, "Preprocess bitmap end");

        // Copy the input data into TensorFlow.
        Log.d(TAG, "Feed data to TensorFlow start");
        inferenceInterface.feed(YOLO_INPUT_NAME, floatValues, 1, bitmap.getWidth(), bitmap.getHeight(), 3);
        Log.d(TAG, "Feed data to TensorFlow end");

        //timer.endSplit("ready for inference");

        // Run the inference call.
        Log.d(TAG, "Run TensorFlow start");
        String[] outputNames = YOLO_OUTPUT_NAMES.split(",");
        inferenceInterface.run(outputNames);
        Log.d(TAG, "Run TensorFlow end");

        // Copy the output Tensor back into the output array.
        Log.d(TAG, "Fetch TensorFlow start");
        final int gridWidth = bitmap.getWidth() / YOLO_BLOCK_SIZE;
        final int gridHeight = bitmap.getHeight() / YOLO_BLOCK_SIZE;
        final float[] output =
                new float[gridWidth * gridHeight * (NUM_CLASSES + 5) * NUM_BOXES_PER_BLOCK];
        inferenceInterface.fetch(outputNames[0], output);
        Log.d(TAG, "Fetch TensorFlow end");

        // Find the best detections.
        final PriorityQueue<ObjectRecognition> pq =
                new PriorityQueue<ObjectRecognition>(
                        1,
                        (lhs, rhs) -> {
                            // Intentionally reversed to put high confidence at the head of the queue.
                            return Float.compare(rhs.getConfidence(), lhs.getConfidence());
                        });

        for (int y = 0; y < gridHeight; ++y) {
            for (int x = 0; x < gridWidth; ++x) {
                for (int b = 0; b < NUM_BOXES_PER_BLOCK; ++b) {
                    final int offset =
                            (gridWidth * (NUM_BOXES_PER_BLOCK * (NUM_CLASSES + 5))) * y
                                    + (NUM_BOXES_PER_BLOCK * (NUM_CLASSES + 5)) * x
                                    + (NUM_CLASSES + 5) * b;

                    final float xPos = (x + MathUtil.sigmoid(output[offset + 0])) * YOLO_BLOCK_SIZE;
                    final float yPos = (y + MathUtil.sigmoid(output[offset + 1])) * YOLO_BLOCK_SIZE;

                    final float w = (float) (Math.exp(output[offset + 2]) * anchors[2 * b + 0]) * YOLO_BLOCK_SIZE;
                    final float h = (float) (Math.exp(output[offset + 3]) * anchors[2 * b + 1]) * YOLO_BLOCK_SIZE;

                    final RectF rect =
                            new RectF(
                                    Math.max(0, xPos - w / 2),
                                    Math.max(0, yPos - h / 2),
                                    Math.min(bitmap.getWidth() - 1, xPos + w / 2),
                                    Math.min(bitmap.getHeight() - 1, yPos + h / 2));
                    final float confidence = MathUtil.sigmoid(output[offset + 4]);

                    int detectedClass = -1;
                    float maxClass = 0;

                    final float[] classes = new float[NUM_CLASSES];
                    for (int c = 0; c < NUM_CLASSES; ++c) {
                        classes[c] = output[offset + 5 + c];
                    }

                    // Might not needed
                    MathUtil.softmax(classes);

                    for (int c = 0; c < NUM_CLASSES; ++c) {
                        if (classes[c] > maxClass) {
                            detectedClass = c;
                            maxClass = classes[c];
                        }
                    }

                    final float confidenceInClass = maxClass * confidence;
                    if (confidenceInClass > 0.01) {
                        String s = String.format( "%s (%d) %f %s", labels[detectedClass], detectedClass, confidenceInClass, rect);
                        Log.d(TAG, s);
                        pq.add(new ObjectRecognition("" + offset, labels[detectedClass], confidenceInClass, rect));
                    }
                }
            }
        }

        final ArrayList<ObjectRecognition> recognitions = new ArrayList<ObjectRecognition>();
        for (int i = 0; i < Math.min(pq.size(), MAX_RESULTS); ++i) {
            ObjectRecognition object = pq.poll();
            if (object.getConfidence() > MINIMUM_CONFIDENCE_YOLO)
                recognitions.add(object);
        }
        Log.d(TAG,"Recognize Image end"); // "recognizeImage"

        return Observable.just(recognitions);


    }
}
