package com.malevent.pro.deeplearning.Interactor.ViewModel;

import com.malevent.pro.deeplearning.Interactor.Scheduler.BaseSchedulerProvider;

import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;
import io.reactivex.subjects.PublishSubject;

/**
 * Created by OCCOYANC on 3/12/2018.
 */

public abstract class BaseViewModel implements ViewModel {

    private final BaseSchedulerProvider scheduler;
    protected PublishSubject<Boolean> loading = PublishSubject.create();
    protected PublishSubject<String> showToastMessage = PublishSubject.create();

    public BaseViewModel(BaseSchedulerProvider scheduler) {
        this.scheduler = scheduler;
    }

    @Override
    public Observable<Boolean> getLoading(){
        return loading;
    }

    @Override
    public Observable<String> getShowToastMessage() {
        return showToastMessage;
    }

    protected BaseSchedulerProvider getScheduler() {
        return scheduler;
    }

    /**
     * Utility method to apply default schedulers automatically.
     * See dhttp://blog.danlew.net/2015/03/02/dont-break-the-chain/
     *
     * @param <T> type
     * @return transformer
     */
    protected <T> ObservableTransformer<T, T> applySchedulers(boolean showException) {
        return observable -> observable
                .subscribeOn(getScheduler().io())
                .observeOn(getScheduler().ui())
                .doOnError(throwable -> {
                    throwable.printStackTrace();
                });
    }

    protected <T> ObservableTransformer<T, T> applySchedulers() {
        return applySchedulers(true);
    }

    protected void showException(Throwable throwable, String message) {
        showToastMessage.onNext(message);
    }
}
