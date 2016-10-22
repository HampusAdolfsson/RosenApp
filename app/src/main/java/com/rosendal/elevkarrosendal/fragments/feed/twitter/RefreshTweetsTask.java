package com.rosendal.elevkarrosendal.fragments.feed.twitter;

import android.util.Log;

import com.rosendal.elevkarrosendal.DrawerActivity;
import com.rosendal.elevkarrosendal.fragments.refreshable_fragment.RefreshTask;
import com.rosendal.elevkarrosendal.fragments.refreshable_fragment.RefreshTaskHandler;
import com.twitter.sdk.android.core.models.Tweet;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.auth.OAuth2Token;
import twitter4j.conf.ConfigurationBuilder;

class RefreshTweetsTask extends RefreshTask<List<Tweet>> {

    private int minTime;

    public RefreshTweetsTask(RefreshTaskHandler<List<Tweet>> handler, int minTimeMillis) {
        super(handler);
        minTime = minTimeMillis;
    }

    @Override
    protected List<Tweet> doWork() {
        List<Tweet> list = new ArrayList<>();
        long time = System.currentTimeMillis();

        ConfigurationBuilder builder = new ConfigurationBuilder();
        builder.setApplicationOnlyAuthEnabled(true);
        try {

            Twitter twitter = new TwitterFactory(builder.build()).getInstance();
            twitter.setOAuthConsumer("fi1JeM5tPBdRz8JWwC51HZs9R", "95tVxCSgaqgA2uLdFu1PMgg7eqAVh2RrSJSFh7KjVcskhgwwKc");
            OAuth2Token token = twitter.getOAuth2Token();

            try {
                Random random = new Random();
                for (int n = 0; n < 5; n++) {
                    String url = "https://api.twitter.com/1.1/statuses/user_timeline.json?screen_name=elevkarrosendal&count=15&include_rts=true&contributor_details=true&exclude_replies=true";
                    HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();
                    con.setRequestMethod("GET");
                    con.setRequestProperty("Authorization", "Bearer " + token.getAccessToken());
                    if (con.getResponseCode() == 403 || con.getResponseCode() >= 500) {
                        Log.e(DrawerActivity.LOG_TAG, "Twitter response: " + con.getResponseCode());
                        try {
                            Thread.sleep((n << 1) * 1000 + random.nextInt(1001));
                        } catch (InterruptedException e){}
                    } else {
                        if (con.getResponseCode()!=200) {
                            Log.e(DrawerActivity.LOG_TAG, "Twitter response: " + con.getResponseCode());
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
                            final JSONArray array = new JSONArray(response.toString());
                            list = new ArrayList<>();
                            for (int i = 0; i < array.length(); i++) {
                                list.add(TweetBuilder.parseJsonToTweet(array.getJSONObject(i)));
                            }
                            try {
                                Thread.sleep(1500);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            break;
                        } catch (JSONException e) {
                            Log.e(DrawerActivity.LOG_TAG, "Error parsing timeline", e);
                        }
                    }
                }
            } catch (IOException e) {
                handler.onError();
                Log.e(DrawerActivity.LOG_TAG, "Failed to get timeline: " + e.getMessage(), e);
            }
        } catch (twitter4j.TwitterException te) {
            handler.onError();
            Log.e(DrawerActivity.LOG_TAG, "Failed to get timeline: " + te.getMessage(), te);
        }
        long time2 = System.currentTimeMillis();
        if (time2 - time < minTime) {
            try {
                Thread.sleep(minTime - (time2 - time));
            } catch (InterruptedException e) {
                Log.e(DrawerActivity.LOG_TAG, "", e);
            }
        }
        return list;
    }


}