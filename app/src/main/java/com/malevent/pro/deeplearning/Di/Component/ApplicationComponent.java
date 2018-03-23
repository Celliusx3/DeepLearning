package com.malevent.pro.deeplearning.Di.Component;

import android.content.Context;

import com.malevent.pro.deeplearning.Presentation.DeepLearningApplication;
import com.malevent.pro.deeplearning.Di.Module.ApplicationModule;
import com.malevent.pro.deeplearning.Di.Module.ConstantsModule;
import com.malevent.pro.deeplearning.Di.Module.MainModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by OCCOYANC on 3/12/2018.
 */

@Singleton
@Component(modules = {ApplicationModule.class, ConstantsModule.class})
public interface ApplicationComponent {
    DeepLearningApplication getApplication();

    Context getApplicationContext();

    void inject(DeepLearningApplication deepLearningApplication);

    MainComponent plus(MainModule homeModule);


}
