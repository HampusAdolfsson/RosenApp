package com.rosendal.elevkarrosendal.fragments.feed.instagram;

import android.util.Log;

import com.rosendal.elevkarrosendal.DrawerActivity;
import com.rosendal.elevkarrosendal.fragments.refreshable_fragment.RefreshTask;
import com.rosendal.elevkarrosendal.fragments.refreshable_fragment.RefreshTaskHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;

class RefreshInstaFeedTask extends RefreshTask<ArrayList<InstaPost>> {
    private int minTime;

    public RefreshInstaFeedTask(RefreshTaskHandler<ArrayList<InstaPost>> handler, int minTimeMillis) {
        super(handler);
        minTime = minTimeMillis;
    }

    @Override
    protected ArrayList<InstaPost> doWork() {
        ArrayList<InstaPost> posts = new ArrayList<>();
        long time = System.currentTimeMillis();

        try {
            Random random = new Random();
            for (int n = 0; n < 5; n++) {
                String url = "https://www.instagram.com/elevkarenrosendal/media?count=15";
                HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();
                con.setRequestMethod("GET");
                if (con.getResponseCode() == 403 || con.getResponseCode() >= 500) {
                    Log.e(DrawerActivity.LOG_TAG, "Instagram response: " + con.getResponseCode());
                    try {
                        Thread.sleep((n << 1) * 1000 + random.nextInt(1001));
                    } catch (InterruptedException e) {}
                } else {
                    if (con.getResponseCode() != 200) {
                        Log.e(DrawerActivity.LOG_TAG, "Instagram response: " + con.getResponseCode());
                    }
                    BufferedReader in = new BufferedReader(
                            new InputStreamReader(con.getInputStream()));
                    String inputLine;
                    StringBuilder response = new StringBuilder();

                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    in.close();

                    try {
                        posts = new ArrayList<>();

                        JSONObject json = new JSONObject(response.toString());
                        JSONArray data = json.getJSONArray("items");
                        for (int i = 0; i < data.length(); i++) {
                            InstaPost post = InstaPost.createFromJson(data.getJSONObject(i));
                            if (post != null) posts.add(post);
                        }
                        break;
                    } catch (JSONException e) {
                        handler.onError();
                        Log.e(DrawerActivity.LOG_TAG, "Error parsing timeline", e);
                    }
                }
            }
        } catch (IOException e) {
            handler.onError();
            Log.e(DrawerActivity.LOG_TAG, "Failed to get instagram timeline: " + e.getMessage(), e);
        }
        long time2 = System.currentTimeMillis();
        if (time2 - time < minTime) {
            try {
                Thread.sleep(minTime - (time2 - time));
            } catch (InterruptedException e) {
                Log.e(DrawerActivity.LOG_TAG, "", e);
            }
        }
        return posts;
    }

}
