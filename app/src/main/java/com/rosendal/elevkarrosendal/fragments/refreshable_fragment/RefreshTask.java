package com.rosendal.elevkarrosendal.fragments.refreshable_fragment;

import android.os.AsyncTask;

import java.net.InetAddress;
import java.net.UnknownHostException;

public abstract class RefreshTask<ReturnType> extends AsyncTask<Void, Void, ReturnType> {
    protected RefreshTaskHandler<ReturnType> handler;

    public RefreshTask(RefreshTaskHandler<ReturnType> handler) {
        this.handler = handler;
    }

    @Override
    protected void onPostExecute(ReturnType result) {
        if (handler != null) handler.onSuccess(result);
        running = false;
    }

    @Override
    protected void onPreExecute() {
        running = true;
    }

    @Override
    protected ReturnType doInBackground(Void... params) {
        if (!isInternetAvailable()) {
            handler.onNoInternet();
            return null;
        }
        return doWork();
    }

    protected abstract ReturnType doWork();

    public static boolean running = false;

    private static boolean isInternetAvailable() {
        try {
            InetAddress address = InetAddress.getByName("google.com");
            return (!address.equals(""));
        } catch (UnknownHostException e) {
            e.printStackTrace();
            return false;
        }
    }
}
