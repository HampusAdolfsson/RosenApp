package com.rosendal.elevkarrosendal.fragments.feed;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.rosendal.elevkarrosendal.fragments.feed.instagram.InstaFragment;
import com.rosendal.elevkarrosendal.fragments.feed.twitter.TweetFragment;

class FeedAdapter extends FragmentStatePagerAdapter {

    CharSequence[] titles;
    TweetFragment tweetFragment;
    InstaFragment instaFragment;

    public FeedAdapter(FragmentManager fm, CharSequence[] titles, TweetFragment f1, InstaFragment f2) {
        super(fm);

        this.titles = titles;
        tweetFragment = f1;
        instaFragment = f2;
    }

    @Override
    public Fragment getItem(int position) {
        return position == 0 ? tweetFragment : instaFragment;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }

    @Override
    public int getCount() {
        return 2;
    }
}
