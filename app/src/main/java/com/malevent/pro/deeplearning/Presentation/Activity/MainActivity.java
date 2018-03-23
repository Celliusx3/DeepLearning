package com.malevent.pro.deeplearning.Presentation.Activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.malevent.pro.deeplearning.Presentation.DeepLearningApplication;
import com.malevent.pro.deeplearning.Di.Module.MainModule;
import com.malevent.pro.deeplearning.Interactor.ViewModel.ImageDetectionViewModel;
import com.malevent.pro.deeplearning.Interactor.ViewModel.ViewModel;
import com.malevent.pro.deeplearning.ObjectRecognition;
import com.malevent.pro.deeplearning.R;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import io.reactivex.annotations.Nullable;

public class MainActivity extends BaseActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    @Inject
    ImageDetectionViewModel imageDetectionViewModel;

    @BindView(R.id.activity_main)
    RelativeLayout rlMain;

    @BindView(R.id.iv_main_image)
    ImageView ivImage;

    public static Intent getCallingIntent(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        return intent;
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_main;
    }

    @Override
    protected ViewModel getViewModel() {
        return imageDetectionViewModel;
    }

    @Override
    protected View getRootView() {
        return rlMain;
    }

    @Override
    protected void onInject() {
        super.onInject();
        Log.d(TAG, Boolean.toString(DeepLearningApplication.getInstance() == null));
        DeepLearningApplication.getInstance()
                .getApplicationComponent()
                .plus(new MainModule(this))
                .inject(this);
    }

    @Override
    protected void onBindData(@Nullable View view, Bundle savedInstanceState) {
        super.onBindData(view, savedInstanceState);

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.waterbottle);
        bitmap = Bitmap.createScaledBitmap(bitmap, 416, 416, true);

        Bitmap finalBitmap = bitmap;
        imageDetectionViewModel.getObjectRecognition(bitmap)
                .observeOn(getUiScheduler())
                .subscribeOn(getIoScheduler())
                .subscribe(objectRecognitions -> {
                    Canvas canvas = new Canvas(finalBitmap);
                    drawRecognizedObject(objectRecognitions, canvas, finalBitmap);
                    for (ObjectRecognition object: objectRecognitions
                            ) {
                        Log.d(TAG, object.getId());
                        Log.d(TAG, object.getTitle());
                        Log.d(TAG, Float.toString(object.getConfidence()));
                    }
                }, throwable -> throwable.printStackTrace());

    }

    private void drawRecognizedObject(List<ObjectRecognition> objectRecognitions, Canvas canvas, Bitmap bitmap){
        for (ObjectRecognition object: objectRecognitions){
            Paint p = new Paint();
            p.setColor(Color.RED);
            p.setStrokeWidth(2.0f);
            p.setStyle(Paint.Style.STROKE);
            canvas.drawRoundRect(object.getLocation(), 2, 2, p);
        }

        //Attach the canvas to the ImageView
        ivImage.setImageBitmap(bitmap);
    }
}
