package com.malevent.pro.deeplearning.Di.Module;

import android.content.Context;

import com.malevent.pro.deeplearning.Data.DeepLearningConstants.ImageDetectionConstants;
import com.malevent.pro.deeplearning.Interactor.Scheduler.BaseSchedulerProvider;
import com.malevent.pro.deeplearning.Interactor.ViewModel.ImageDetectionViewModel;
import com.malevent.pro.deeplearning.Interactor.ViewModel.Impl.ImageDetectionViewModelImpl;

import dagger.Module;
import dagger.Provides;

/**
 * Created by OCCOYANC on 3/13/2018.
 */

@Module
public class MainModule {

    private Context mContext;

    public MainModule(Context context) {
        mContext = context;
    }

    @Provides
    ImageDetectionViewModel provideImageDetectionViewModel(ImageDetectionConstants imageDetectionConstants,
                                                           BaseSchedulerProvider provider) {
        return new ImageDetectionViewModelImpl(imageDetectionConstants, provider);
    }

}
