package com.malevent.pro.deeplearning.Interactor.ViewModel;

import io.reactivex.Observable;

/**
 * Created by OCCOYANC on 3/12/2018.
 */

public interface ViewModel {
    Observable<Boolean> getLoading();
    Observable<String> getShowToastMessage();
}
