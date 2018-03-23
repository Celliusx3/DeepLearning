package com.malevent.pro.deeplearning.Presentation.Activity;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Toast;

import com.malevent.pro.deeplearning.Interactor.Scheduler.BaseSchedulerProvider;
import com.malevent.pro.deeplearning.Interactor.ViewModel.ViewModel;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import javax.inject.Inject;

import butterknife.ButterKnife;
import io.reactivex.Scheduler;

/**
 * Created by OCCOYANC on 3/12/2018.
 */

public abstract class BaseActivity extends RxAppCompatActivity {

    private static final long DOUBLE_BACK_TO_EXIT_DURATION = 2000;

    @Inject
    BaseSchedulerProvider scheduler;

    private long backButtonLastPressedElapsedTime;

    protected abstract @LayoutRes int getLayoutResource();

    protected abstract ViewModel getViewModel();

    protected abstract View getRootView();

    public Scheduler getUiScheduler() {
        return scheduler.ui();
    }

    public Scheduler getIoScheduler() {
        return scheduler.io();
    }

    private ProgressDialog loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        onSetContentView();
        onGetInputData(savedInstanceState);
        onInject();
        onBindView();

        if (getViewModel() != null) {
            getViewModel().getLoading()
                    .compose(bindToLifecycle())
                    .subscribe(this::getLoading);
        }
        onBindData(getRootView(), savedInstanceState);
    }

    protected void onSetContentView() {
        if (getLayoutResource() != 0) {
            setContentView(getLayoutResource());
        }
    }

    protected void onBindView() {
        ButterKnife.bind(this);
    }

    protected void onInject() {
    }

    protected void onBindData(@Nullable View view, Bundle savedInstanceState) {
    }

    protected void onGetInputData(Bundle savedInstanceState) {
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
    }

    @Override
    protected void onResume() {
        super.onResume();

        /*
        if (getViewModels() != null) {
            for (ViewModel viewModel : getViewModels()) {
                viewModel.getInput().onAttachView();
            }
        }*/
    }

    @Override
    protected void onPause() {
        super.onPause();

        /*if (getViewModels() != null) {
            for (ViewModel viewModel : getViewModels()) {
                viewModel.getInput().onDetachView();
            }
        }*/
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void attachBaseContext(Context context) {
        super.attachBaseContext(context);
        //super.attachBaseContext(CalligraphyContextWrapper.wrap(context));
    }

    @Override
    public void startActivity(Intent intent) {
        try {
            super.startActivity(intent);
            startActivityTransitions();
        } catch (NullPointerException npe) {

        } catch (ActivityNotFoundException anfe) {
            anfe.printStackTrace();
        }
    }

    protected void startActivityTransitions() {
        //getNavigator().showPendingTransitions(this);
    }

    @Override
    public void finish() {
        super.finish();
        finishActivityTransitions();
    }

    protected void finishActivityTransitions() {
        /*if (getNavigator() != null)
            getNavigator().showPendingTransitions(this);*/
    }

    public void showToastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void promptExitOrFinish() {
        final long currentElapsedTime = SystemClock.elapsedRealtime();
        if (currentElapsedTime < DOUBLE_BACK_TO_EXIT_DURATION + this.backButtonLastPressedElapsedTime) {
            finishAffinity();
            return;
        }

        this.backButtonLastPressedElapsedTime = currentElapsedTime;

        // TODO translate this string from backend
        showToastMessage("Press BACK again to exit");
    }

    public boolean requireDoubleBackPressToFinishActivity() {
        // Default is false. Override this in child activity
        // to get different behavior
        return false;
    }

    @Override
    public void onBackPressed() {
        if (this.requireDoubleBackPressToFinishActivity()) {
            this.promptExitOrFinish();
        } else {
            super.onBackPressed();
        }
    }

    private void getLoading(boolean isLoading){
        if (isLoading) {
            if (loading == null) {
                loading = new ProgressDialog(this);
                loading.setMessage("Loading...");
                loading.setCancelable(false);
            }
            loading.show();
        } else {
            if (loading != null) {
                loading.dismiss();
            }
        }
    }
}
