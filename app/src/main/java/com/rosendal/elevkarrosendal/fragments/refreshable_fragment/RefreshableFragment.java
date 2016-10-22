package com.rosendal.elevkarrosendal.fragments.refreshable_fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Calendar;

public abstract class RefreshableFragment<T> extends Fragment {

    public int animDuration;
    protected boolean pbShowing = true;

    private Long lastUpdate;
    private boolean hasLoaded = false;

    protected int refreshIntervalMs = 1000 * 60 * 5;

    protected abstract void onRefreshTaskFinished(T result);
    protected abstract void onRefresh();

    protected void showProgressBar() {
        if (pbShowing) return;
        pbShowing = true;
    }

    protected void showContent() {
        if (!pbShowing) return;
        pbShowing = false;
    }

    protected void setHasLoaded(boolean loaded) {
        hasLoaded = loaded;
    }


    public void forceRefresh() {
        hasLoaded = true;
        onRefresh();
        lastUpdate = Calendar.getInstance().getTimeInMillis();
    }

    public void refreshIfNecessary() {
        if (!hasLoaded || lastUpdate == null || Calendar.getInstance().getTimeInMillis() - lastUpdate > refreshIntervalMs) {
            hasLoaded = true;
            onRefresh();
        }
        lastUpdate = Calendar.getInstance().getTimeInMillis();
    }


    private static final String STATE_LAST_REFRESH = "lastRefresh";

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (lastUpdate != null) {
            outState.putLong(STATE_LAST_REFRESH + getClass().getName(), lastUpdate);

        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        animDuration = getResources().getInteger(android.R.integer.config_shortAnimTime);
        if (savedInstanceState != null) {
            lastUpdate = savedInstanceState.getLong(STATE_LAST_REFRESH + getClass().getName());
        }
        return null;
    }

    public Long getTimeSinceLastUpdate() {
        if (lastUpdate != null) return System.currentTimeMillis() - lastUpdate;
        return null;
    }
}
