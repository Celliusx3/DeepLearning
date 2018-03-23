package com.malevent.pro.deeplearning.Presentation;

import android.app.Application;

import com.malevent.pro.deeplearning.Di.Component.ApplicationComponent;
import com.malevent.pro.deeplearning.Di.Component.DaggerApplicationComponent;
import com.malevent.pro.deeplearning.Di.Module.ApplicationModule;

/**
 * Created by OCCOYANC on 3/12/2018.
 */

public class DeepLearningApplication extends Application {
    private ApplicationComponent mApplicationComponent;
    private final Object mLock = new Object();

    // Singleton Instance
    private static DeepLearningApplication singleton;

    public static DeepLearningApplication getInstance() {
        return singleton;
    }

    @Override
    public void onCreate() {
        //ActivityLifecycleCallback.register(this);
        super.onCreate();

        synchronized (mLock) {
            singleton = this;
            mApplicationComponent = DaggerApplicationComponent.builder()
                    .applicationModule(new ApplicationModule(this))
                    .build();
            mApplicationComponent.inject(this);
        }
    }

    public ApplicationComponent getApplicationComponent() {
        return mApplicationComponent;
    }
}
