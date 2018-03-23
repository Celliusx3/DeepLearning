package com.malevent.pro.deeplearning.Di.Component;

import com.malevent.pro.deeplearning.Di.Module.MainModule;
import com.malevent.pro.deeplearning.Presentation.Activity.MainActivity;

import dagger.Subcomponent;

/**
 * Created by OCCOYANC on 3/13/2018.
 */

@ActivityScope
@Subcomponent(modules = {MainModule.class})
public interface MainComponent {
    void inject(MainActivity mainActivity);
}
