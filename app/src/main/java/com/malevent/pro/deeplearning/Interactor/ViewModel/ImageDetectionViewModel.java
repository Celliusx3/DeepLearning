package com.malevent.pro.deeplearning.Interactor.ViewModel;

import android.graphics.Bitmap;

import com.malevent.pro.deeplearning.ObjectRecognition;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by OCCOYANC on 3/12/2018.
 */

public interface ImageDetectionViewModel extends ViewModel {

    Observable<List<ObjectRecognition>> getObjectRecognition(Bitmap bitmap);

}
