package com.rosendal.elevkarrosendal.fragments.feed.twitter;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.rosendal.elevkarrosendal.AnimationHelper;
import com.rosendal.elevkarrosendal.R;
import com.rosendal.elevkarrosendal.fragments.refreshable_fragment.RefreshTaskHandler;
import com.rosendal.elevkarrosendal.fragments.refreshable_fragment.RefreshableFragment;
import com.twitter.sdk.android.core.models.Tweet;

import java.util.List;

public class TweetFragment extends RefreshableFragment<List<Tweet>> {
    private LinearLayout layout;
    private SwipeRefreshLayout refreshLayout;

    private ProgressBar progressBar;
    public boolean isCreated;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        setRetainInstance(true);
        refreshIntervalMs = 30 * 60 * 1000;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //inflate layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tweet, container, false);

        layout = (LinearLayout) view.findViewById(R.id.llFeed);
        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refreshLayout);
        refreshLayout.setColorSchemeColors(ContextCompat.getColor(getContext(), R.color.primary400), 0xFF000000);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                forceRefresh();
            }
        });

        progressBar = (ProgressBar) view.findViewById(R.id.pbFeed);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        isCreated = true;
        setHasLoaded(false);
        refreshIfNecessary();
    }

    @Override
    protected void onRefresh() {
        new RefreshTweetsTask(new RefreshTaskHandler<>(this), animDuration + 50).execute();
    }

    @Override
    public void onRefreshTaskFinished(List<Tweet> tweets) {
        layout.removeAllViews();
        for (Tweet tweet : tweets) {
            View tweetView = new TweetCard(getActivity(), tweet, R.style.TweetStyle);
            layout.addView(tweetView);
        }
        showContent();
        if (refreshLayout.isRefreshing()) refreshLayout.setRefreshing(false);
    }

    @Override
    public void showContent() {
        super.showContent();
        AnimationHelper.fadeViewIn(refreshLayout, animDuration);
        AnimationHelper.fadeViewOut(progressBar, animDuration);
    }

    @Override
    public void showProgressBar() {
        super.showProgressBar();

        AnimationHelper.fadeViewIn(progressBar, animDuration);
        AnimationHelper.fadeViewOut(refreshLayout, animDuration);
    }
}
