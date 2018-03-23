package com.malevent.pro.deeplearning.Interactor.Scheduler;

import android.support.annotation.NonNull;

import io.reactivex.Scheduler;

/**
 * Created by OCCOYANC on 3/12/2018.
 */

public interface BaseSchedulerProvider {

    @NonNull
    Scheduler computation();

    @NonNull
    Scheduler io();

    @NonNull
    Scheduler ui();
}