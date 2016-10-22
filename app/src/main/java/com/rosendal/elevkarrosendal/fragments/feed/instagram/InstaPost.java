package com.rosendal.elevkarrosendal.fragments.feed.instagram;

import android.graphics.Bitmap;
import android.util.Log;

import com.rosendal.elevkarrosendal.DrawerActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


class InstaPost {
    public Bitmap low, standard, thumbnail;
    public String user, urlLow, urlStandard, urlThumb, link, caption;
    public int likes;
    public Comment[] comments;
    public long created;

    /*public InstaPost(Bitmap low, Bitmap standard, Bitmap thumbnail, String user, int likes, Comment[] comments, long created) {
        this.low = low;
        this.standard = standard;
        this.thumbnail = thumbnail;
        this.user = user;
        this.likes = likes;
        this.comments = comments;
        this.created = created;
    }

    public InstaPost(String user, String urlLow, String urlStandard, String urlThumb, int likes, Comment[] comments, long created) {
        this.user = user;
        this.likes = likes;
        this.comments = comments;
        this.created = created;
    }*/

    public InstaPost() {
    }

    public static InstaPost createFromJson(JSONObject json) {
        try {
            InstaPost post = new InstaPost();
            post.user = json.getJSONObject("user").getString("username");
            if (json.has("caption") && json.get("caption") != null) post.caption = json.getJSONObject("caption").getString("text");
            post.likes = json.getJSONObject("likes").getInt("count");
            post.created = json.getLong("created_time");
            post.link = json.getString("link");
            JSONObject images = json.getJSONObject("images");
            post.urlLow = images.getJSONObject("low_resolution").getString("url");
            post.urlStandard = images.getJSONObject("standard_resolution").getString("url");
            post.urlThumb = images.getJSONObject("thumbnail").getString("url");
            JSONArray array = json.getJSONObject("comments").getJSONArray("data");

            post.comments = new Comment[array.length()];
            for (int i = 0; i < array.length(); i++) {
                JSONObject j = array.getJSONObject(i);
                String text = j.getString("text");

                JSONObject from = j.getJSONObject("from");
                String user = from.getString("full_name");
                if (user.isEmpty()) user = from.getString("username");

                post.comments[i] = new Comment(user, text);
            }
            return post;
        } catch (JSONException e) {
            Log.e(DrawerActivity.LOG_TAG, "Error parsing timeline", e);
        }
        return null;
    }
}
