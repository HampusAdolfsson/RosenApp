package com.rosendal.elevkarrosendal.fragments.feed;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rosendal.elevkarrosendal.R;
import com.rosendal.elevkarrosendal.fragments.feed.instagram.InstaFragment;
import com.rosendal.elevkarrosendal.fragments.feed.twitter.TweetFragment;

public class FeedFragment extends Fragment {
    private TweetFragment tweetFragment;
    private InstaFragment instaFragment;

    ViewPager pager;
    FeedAdapter adapter;
    SlidingTabLayout tabs;
    CharSequence[] titles = {"Twitter", "Instagram"};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //inflate layout for this fragment
        View view = inflater.inflate(R.layout.fragment_feed, container, false);
        tweetFragment = new TweetFragment();
        instaFragment = new InstaFragment();

        adapter =  new FeedAdapter(getActivity().getSupportFragmentManager(), titles, tweetFragment, instaFragment);

        pager = (ViewPager) view.findViewById(R.id.pager);
        pager.setAdapter(adapter);

        tabs = (SlidingTabLayout) view.findViewById(R.id.tabs);
        tabs.setDistributeEvenly(true);

        tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return 0xFFFFFFFF;
            }
        });

        tabs.setViewPager(pager);
        return view;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(true);
        if (!hidden) {
            updateAsNeeded();
        }
    }

    public void updateAsNeeded() {
        if (tweetFragment.isCreated) tweetFragment.refreshIfNecessary();
        if (instaFragment.isCreated) instaFragment.refreshIfNecessary();
    }

}
