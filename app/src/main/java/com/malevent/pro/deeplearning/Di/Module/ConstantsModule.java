package com.malevent.pro.deeplearning.Di.Module;

import android.content.res.AssetManager;

import com.malevent.pro.deeplearning.Data.DeepLearningConstants.ImageDetectionConstants;
import com.malevent.pro.deeplearning.Data.DeepLearningConstants.Impl.ImageDetectionConstantsImpl;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by OCCOYANC on 3/13/2018.
 */

@Module
public class ConstantsModule {

    @Singleton
    @Provides
    ImageDetectionConstants provideImageDetectionConstants(AssetManager assetManager) {
        return new ImageDetectionConstantsImpl(assetManager);
    }
}
