package com.rosendal.elevkarrosendal.fragments.refreshable_fragment;

import android.app.Activity;
import android.util.Log;

import com.rosendal.elevkarrosendal.DrawerActivity;
import com.rosendal.elevkarrosendal.ErrorHandler;

public class RefreshTaskHandler<T> {
    private RefreshableFragment<T> fragment;

    public RefreshTaskHandler(RefreshableFragment<T> fragment) {
        this.fragment = fragment;
    }

    public void onSuccess(final T result) {
        Log.d(DrawerActivity.LOG_TAG, "Task finished from caller: " + fragment.getClass());
        fragment.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                fragment.onRefreshTaskFinished(result);
            }
        });
    }

    public void onError() {
        fragment.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Activity activity = fragment.getActivity();
                if (activity instanceof ErrorHandler) {
                    ((ErrorHandler)activity).onError();
                }
            }
        });

    }

    public void onNoInternet() {
        fragment.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Activity activity = fragment.getActivity();
                if (activity instanceof ErrorHandler) {
                    ((ErrorHandler)activity).onNoInternet();
                }
            }
        });
    }
}
