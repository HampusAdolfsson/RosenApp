package com.rosendal.elevkarrosendal.fragments.feed.twitter;

import android.util.Log;

import com.rosendal.elevkarrosendal.DrawerActivity;
import com.twitter.sdk.android.core.models.HashtagEntity;
import com.twitter.sdk.android.core.models.MediaEntity;
import com.twitter.sdk.android.core.models.MentionEntity;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.models.TweetEntities;
import com.twitter.sdk.android.core.models.UrlEntity;
import com.twitter.sdk.android.core.models.User;
import com.twitter.sdk.android.core.models.UserEntities;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

class TweetBuilder {
    public static Tweet parseJsonToTweet(JSONObject json) {
        try {
            String createdAt = null, idStr = null, inReplyToScreenName = null, inReplyToStatusIdStr = null, inReplyToUserIdStr = null, lang = null,  source = null, text = null;
            long id = 0, inReplyToStatusId = 0, inReplyToUserId = 0;
            int favoriteCount = 0, retweetCount = 0;
            boolean truncated = false;
            Tweet retweetedStatus = null;
            User user = null;
            TweetEntities entities = null;

            if (json.has("created_at")) createdAt = json.getString("created_at");
            if (json.has("retweet_count")) retweetCount = json.getInt("retweet_count");
            if (json.has("favorite_count")) favoriteCount = json.getInt("favorite_count");
            if (json.has("id")) id = json.getLong("id");
            if (json.has("id_str")) idStr = json.getString("id_str");
            if (json.has("lang")) lang = json.getString("lang");
            if (json.has("source")) source = json.getString("source");
            if (json.has("text")) text = json.getString("text");
            if (json.has("truncated")) truncated = json.getBoolean("truncated");
            if (json.has("user")) user = parseJsonToUser(json.getJSONObject("user"));
            if (json.has("retweeted_status")) retweetedStatus = parseJsonToTweet(json.getJSONObject("retweeted_status"));
            if (json.has("entities")) entities = parseJsonToEntities(json.getJSONObject("entities"));

            return new Tweet(null,
                    createdAt,
                    null, //?
                    entities,
                    favoriteCount,
                    false,
                    "medium",
                    id,
                    idStr,
                    null,
                    0,
                    null,
                    0,
                    null,
                    lang,
                    null,
                    false,
                    null, //?
                    retweetCount,
                    false, //?
                    retweetedStatus,
                    source,
                    text,
                    truncated,
                    user,
                    false,
                    null, //?
                    null //?
            );
        } catch (JSONException e) {
            Log.e(DrawerActivity.LOG_TAG, "Error parsing timeline", e);
        }
        return null;
    }
    private static User parseJsonToUser(JSONObject json) {
        try {
            String createdAt = null, description = null, idStr = null, lang = null, name = null, profileBackgroundImageUrl = null, profileBackgroundImageUrlHttps = null, profileBannerUrl = null, profileImageUrl = null, profileImageUrlHttps = null, screenName = null, timeZone = null, url = null, profileLinkColor = null, profileTextColor = null;
            boolean defaultProfile = false, defaultProfileImage = false, contributorsEnabled = false, verified = false;
            long id = 0;
            int statusesCount = 0, utcOffset = 0;
            UserEntities entities = new UserEntities(new UserEntities.UrlEntities(new ArrayList<UrlEntity>()), new UserEntities.UrlEntities(new ArrayList<UrlEntity>()));

            if (json.has("contributors_enabled")) contributorsEnabled = json.getBoolean("contributors_enabled");
            if (json.has("created_at")) createdAt = json.getString("created_at");
            if (json.has("default_profile")) defaultProfile = json.getBoolean("default_profile");
            if (json.has("default_profile_image")) defaultProfileImage = json.getBoolean("default_profile_image");
            if (json.has("description")) description = json.getString("description");
            if (json.has("id")) id = json.getLong("id");
            if (json.has("id_str")) idStr = json.getString("id_str");
            if (json.has("lang")) lang = json.getString("lang");
            if (json.has("name")) name = json.getString("name");
            if (json.has("profile_background_image_url")) profileBackgroundImageUrl = json.getString("profile_background_image_url");
            if (json.has("profile_background_image_url_https")) profileBackgroundImageUrlHttps = json.getString("profile_background_image_url_https");
            if (json.has("profile_banner_url")) profileBannerUrl = json.getString("profile_banner_url");
            if (json.has("profile_image_url")) profileImageUrl = json.getString("profile_image_url");
            if (json.has("profile_image_url_https")) profileImageUrlHttps = json.getString("profile_image_url_https");
            if (json.has("profile_link_color")) profileLinkColor = json.getString("profile_link_color");
            if (json.has("profile_text_color")) profileTextColor = json.getString("profile_text_color");
            if (json.has("screen_name")) screenName = json.getString("screen_name");
            if (json.has("statuses_count")) statusesCount = json.getInt("statuses_count");
            if (json.has("time_zone")) timeZone = json.getString("time_zone");
            if (json.has("url")) url = json.getString("url");
            if (json.has("utc_offset") && json.get("utc_offset") != null && json.get("utc_offset") instanceof Integer){
                utcOffset = json.getInt("utc_offset");
            }
            if (json.has("verified")) verified = json.getBoolean("verified");

            return new User(
                    contributorsEnabled,
                    createdAt,
                    defaultProfile,
                    defaultProfileImage,
                    description,
                    null,
                    entities,
                    0,
                    false,
                    0,
                    0,
                    false,
                    id,
                    idStr,
                    false,
                    lang,
                    4,
                    null,
                    name,
                    "0",
                    profileBackgroundImageUrl,
                    profileBackgroundImageUrlHttps,
                    false,
                    profileBannerUrl,
                    profileImageUrl,
                    profileImageUrlHttps,
                    profileLinkColor,
                    "0",
                    "0",
                    profileTextColor,
                    false,
                    false,
                    screenName,
                    false,
                    null,
                    statusesCount,
                    timeZone,
                    url,
                    utcOffset,
                    verified,
                    null,
                    null

            );
        } catch (JSONException e) {
            Log.e(DrawerActivity.LOG_TAG, "Error parsing timeline", e);
        }
        return null;
    }

    private static TweetEntities parseJsonToEntities(JSONObject json) {
        try {
            List<HashtagEntity> list1 = new ArrayList<>();
            if (json.has("hashtags")) {
                for (int i = 0; i < json.getJSONArray("hashtags").length(); i++) {
                    String text = null;
                    int start = 0, end = 0;
                    if (json.getJSONArray("hashtags").getJSONObject(i).has("text")) text = json.getJSONArray("hashtags").getJSONObject(i).getString("text");
                    if (json.getJSONArray("hashtags").getJSONObject(i).has("indices") && json.getJSONArray("hashtags").getJSONObject(i).getJSONArray("indices").length()>=2) {
                        start = json.getJSONArray("hashtags").getJSONObject(i).getJSONArray("indices").getInt(0);
                        end = json.getJSONArray("hashtags").getJSONObject(i).getJSONArray("indices").getInt(1);
                    }
                    list1.add(new HashtagEntity(text, start, end));
                }
            }
            List<MediaEntity> list2 = new ArrayList<>();
            if (json.has("media")) {
                for (int i = 0; i < json.getJSONArray("media").length(); i++) {
                    String url = null, expandedUrl = null, displayUrl = null, idStr = null, mediaUrl = null, mediaUrlHttps = null, type = null, sourceStatusIdStr = null;
                    int start = 0, end = 0;
                    long id = 0, sourceStatusId = 0;
                    MediaEntity.Sizes sizes = null;

                    if (json.getJSONArray("media").getJSONObject(i).has("id")) id = json.getJSONArray("media").getJSONObject(i).getInt("id");
                    if (json.getJSONArray("media").getJSONObject(i).has("id_str")) idStr = json.getJSONArray("media").getJSONObject(i).getString("id_str");
                    if (json.getJSONArray("media").getJSONObject(i).has("media_url")) mediaUrl = json.getJSONArray("media").getJSONObject(i).getString("media_url");
                    if (json.getJSONArray("media").getJSONObject(i).has("url")) url = json.getJSONArray("media").getJSONObject(i).getString("url");
                    if (json.getJSONArray("media").getJSONObject(i).has("display_url")) displayUrl = json.getJSONArray("media").getJSONObject(i).getString("display_url");
                    if (json.getJSONArray("media").getJSONObject(i).has("media_url_https")) mediaUrlHttps = json.getJSONArray("media").getJSONObject(i).getString("media_url_https");
                    if (json.getJSONArray("media").getJSONObject(i).has("expanded_url")) expandedUrl = json.getJSONArray("media").getJSONObject(i).getString("expanded_url");
                    if (json.getJSONArray("media").getJSONObject(i).has("type")) type = json.getJSONArray("media").getJSONObject(i).getString("type");
                    if (json.getJSONArray("media").getJSONObject(i).has("sizes")){
                        MediaEntity.Size thumb = null, small = null, medium = null, large = null;
                        if (json.getJSONArray("media").getJSONObject(i).getJSONObject("sizes").has("small")) {
                            small = parseSize(json.getJSONArray("media").getJSONObject(i).getJSONObject("sizes").getJSONObject("small"));
                        }
                        if (json.getJSONArray("media").getJSONObject(i).getJSONObject("sizes").has("medium")) {
                            medium = parseSize(json.getJSONArray("media").getJSONObject(i).getJSONObject("sizes").getJSONObject("medium"));
                        }
                        if (json.getJSONArray("media").getJSONObject(i).getJSONObject("sizes").has("large")) {
                            large = parseSize(json.getJSONArray("media").getJSONObject(i).getJSONObject("sizes").getJSONObject("large"));
                        }
                        if (json.getJSONArray("media").getJSONObject(i).getJSONObject("sizes").has("thumb")) {
                            thumb = parseSize(json.getJSONArray("media").getJSONObject(i).getJSONObject("sizes").getJSONObject("thumb"));
                        }
                        sizes = new MediaEntity.Sizes(thumb, small, medium, large);
                    }
                    if (json.getJSONArray("media").getJSONObject(i).has("indices") && json.getJSONArray("media").getJSONObject(i).getJSONArray("indices").length()>=2) {
                        start = json.getJSONArray("media").getJSONObject(i).getJSONArray("indices").getInt(0);
                        end = json.getJSONArray("media").getJSONObject(i).getJSONArray("indices").getInt(1);
                    }

                    list2.add(new MediaEntity(url ,expandedUrl, displayUrl, start, end, id, idStr, mediaUrl, mediaUrlHttps, sizes, sourceStatusId, sourceStatusIdStr, type));
                }
            }
            List<MentionEntity> list3 = new ArrayList<>();
            if (json.has("user_mentions")) {
                for (int i = 0; i < json.getJSONArray("user_mentions").length(); i++) {
                    long id = 0;
                    int start = 0, end = 0;
                    String idStr = null, name = null, screenName = null;

                    if (json.getJSONArray("user_mentions").getJSONObject(i).has("id")) id = json.getJSONArray("user_mentions").getJSONObject(i).getInt("id");
                    if (json.getJSONArray("user_mentions").getJSONObject(i).has("id_str")) idStr = json.getJSONArray("user_mentions").getJSONObject(i).getString("id_str");
                    if (json.getJSONArray("user_mentions").getJSONObject(i).has("name")) name = json.getJSONArray("user_mentions").getJSONObject(i).getString("name");
                    if (json.getJSONArray("user_mentions").getJSONObject(i).has("screen_name")) screenName = json.getJSONArray("user_mentions").getJSONObject(i).getString("screen_name");

                    if (json.getJSONArray("user_mentions").getJSONObject(i).has("indices") && json.getJSONArray("user_mentions").getJSONObject(i).getJSONArray("indices").length()>=2) {
                        start = json.getJSONArray("user_mentions").getJSONObject(i).getJSONArray("indices").getInt(0);
                        end = json.getJSONArray("user_mentions").getJSONObject(i).getJSONArray("indices").getInt(1);
                    }

                    list3.add(new MentionEntity(id, idStr, name, screenName, start, end));
                }
            }
            List<UrlEntity> list4 = new ArrayList<>();
            if (json.has("urls")) {
                for (int i = 0; i < json.getJSONArray("urls").length(); i++) {
                    String displayUrl = null, expandedUrl = null, url = null;
                    int start = 0, end = 0;

                    if (json.getJSONArray("urls").getJSONObject(i).has("display_url")) displayUrl = json.getJSONArray("urls").getJSONObject(i).getString("display_url");
                    if (json.getJSONArray("urls").getJSONObject(i).has("url")) url = json.getJSONArray("urls").getJSONObject(i).getString("url");
                    if (json.getJSONArray("urls").getJSONObject(i).has("expanded_url")) expandedUrl = json.getJSONArray("urls").getJSONObject(i).getString("expanded_url");

                    if (json.getJSONArray("urls").getJSONObject(i).has("indices") && json.getJSONArray("urls").getJSONObject(i).getJSONArray("indices").length()>=2) {
                        start = json.getJSONArray("urls").getJSONObject(i).getJSONArray("indices").getInt(0);
                        end = json.getJSONArray("urls").getJSONObject(i).getJSONArray("indices").getInt(1);
                    }
                    list4.add(new UrlEntity(url, expandedUrl, displayUrl, start, end));
                }
            }

            return new TweetEntities(list4, list3, list2, list1);
        } catch (JSONException e) {
            Log.e(DrawerActivity.LOG_TAG, "Error parsing timeline", e);
        }
        return null;
    }
    private static MediaEntity.Size parseSize(JSONObject json) {
        try {
            int w = 0, h = 0;
            String resize = null;
            if (json.has("w")) w = json.getInt("w");
            if (json.has("h")) h = json.getInt("h");
            if (json.has("resize")) resize = json.getString("resize");
            return new MediaEntity.Size(w, h, resize);
        } catch (JSONException e) {
            Log.e(DrawerActivity.LOG_TAG, "Error parsing timeline", e);
        }
        return null;
    }
}
