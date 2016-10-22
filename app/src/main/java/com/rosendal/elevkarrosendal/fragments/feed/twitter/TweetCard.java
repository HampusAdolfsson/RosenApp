package com.rosendal.elevkarrosendal.fragments.feed.twitter;

import android.content.Context;
import android.util.AttributeSet;

import com.rosendal.elevkarrosendal.R;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.tweetui.CompactTweetView;

class TweetCard extends CompactTweetView {
    public TweetCard(Context context, Tweet tweet) {
        super(context, tweet);
    }

    public TweetCard(Context context, Tweet tweet, int styleResId) {
        super(context, tweet, styleResId);
    }

    public TweetCard(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TweetCard(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    protected int getLayout() {
        return R.layout.tweet_item;
    }
}
