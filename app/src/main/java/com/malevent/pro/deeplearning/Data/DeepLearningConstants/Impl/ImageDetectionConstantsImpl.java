package com.malevent.pro.deeplearning.Data.DeepLearningConstants.Impl;

import android.content.res.AssetManager;

import com.malevent.pro.deeplearning.Data.DeepLearningConstants.ImageDetectionConstants;
import com.malevent.pro.deeplearning.Data.DeepLearningConstants.TextFileNameConstants;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;

/**
 * Created by OCCOYANC on 3/13/2018.
 */

public class ImageDetectionConstantsImpl implements ImageDetectionConstants {

    private AssetManager assetManager;

    public ImageDetectionConstantsImpl(AssetManager assetManager) {
        this.assetManager = assetManager;
    }

    @Override
    public String[] getLabels() {
        try {
            InputStream input = assetManager.open(TextFileNameConstants.IMAGE_DETECTION_LABEL);
            List<String> itemsLabel = new ArrayList<>();
            DataInputStream data_input = new DataInputStream(input);
            BufferedReader buffer = new BufferedReader(new InputStreamReader(data_input));
            String str_line;

            while ((str_line = buffer.readLine()) != null) {
                str_line = str_line.trim();
                if ((str_line.length() != 0)) {
                    itemsLabel.add(str_line);
                }
            }
            return itemsLabel.toArray(new String[itemsLabel.size()]);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Double[] getAnchors() {
        try {
            InputStream input = assetManager.open(TextFileNameConstants.IMAGE_DETECTION_ANCHOR);
            List<Double> itemsLabel = new ArrayList<>();
            DataInputStream data_input = new DataInputStream(input);
            BufferedReader buffer = new BufferedReader(new InputStreamReader(data_input));
            String str_line;

            while ((str_line = buffer.readLine()) != null) {
                str_line = str_line.trim();
                if ((str_line.length() != 0)) {
                    itemsLabel.add(Double.parseDouble(str_line));
                }
            }
            return itemsLabel.toArray(new Double[itemsLabel.size()]);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public AssetManager getAssetManager() {
        return assetManager;
    }
}
