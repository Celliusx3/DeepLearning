package com.malevent.pro.deeplearning.Di.Module;

import android.content.Context;
import android.content.res.AssetManager;

import com.malevent.pro.deeplearning.Presentation.DeepLearningApplication;
import com.malevent.pro.deeplearning.Interactor.Scheduler.BaseSchedulerProvider;
import com.malevent.pro.deeplearning.Interactor.Scheduler.SchedulerProvider;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by OCCOYANC on 3/12/2018.
 */

@Module
public class ApplicationModule {
    private final DeepLearningApplication mApplication;

    public ApplicationModule(final DeepLearningApplication deepLearningApplication) {
        mApplication = deepLearningApplication;
    }

    @Provides
    @Singleton
    DeepLearningApplication provideApplication() {
        return mApplication;
    }

    @Provides
    @Singleton
    Context provideApplicationContext(DeepLearningApplication deepLearningApplication) {
        return deepLearningApplication;
    }

    @Provides
    @Singleton
    BaseSchedulerProvider provideScheduler() {
        return SchedulerProvider.getInstance();
    }

    @Provides
    @Singleton
    AssetManager provideAssetManager() {
        return mApplication.getAssets();
    }
}
