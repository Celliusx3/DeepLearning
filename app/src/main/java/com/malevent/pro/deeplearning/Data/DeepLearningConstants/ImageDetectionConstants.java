package com.malevent.pro.deeplearning.Data.DeepLearningConstants;

import android.content.res.AssetManager;

/**
 * Created by OCCOYANC on 3/13/2018.
 */

public interface ImageDetectionConstants {
    String[] getLabels();
    Double[] getAnchors();

    AssetManager getAssetManager();
}
