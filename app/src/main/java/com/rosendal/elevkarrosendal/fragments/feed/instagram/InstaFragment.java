package com.rosendal.elevkarrosendal.fragments.feed.instagram;


import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.rosendal.elevkarrosendal.AnimationHelper;
import com.rosendal.elevkarrosendal.R;
import com.rosendal.elevkarrosendal.fragments.refreshable_fragment.RefreshTaskHandler;
import com.rosendal.elevkarrosendal.fragments.refreshable_fragment.RefreshableFragment;

import java.util.ArrayList;

public class InstaFragment extends RefreshableFragment<ArrayList<InstaPost>> {
    private ProgressBar progressBar;
    private InstaAdapter adapter;
    private SwipeRefreshLayout refreshLayout;
    public boolean isCreated;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        setRetainInstance(true);
        refreshIntervalMs = 30 * 60 * 1000;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_insta, container, false);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.insta_recycleview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new InstaAdapter(new ArrayList<InstaPost>(), getActivity());
        recyclerView.setAdapter(adapter);

        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refreshView);
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
    protected void onRefreshTaskFinished(ArrayList<InstaPost> result) {
        adapter.setData(result);
        showContent();
        if (refreshLayout.isRefreshing()) refreshLayout.setRefreshing(false);
    }

    @Override
    public void onRefresh() {
        new RefreshInstaFeedTask(new RefreshTaskHandler<>(this), animDuration + 50).execute();
    }

    @Override
    protected void showContent() {
        super.showContent();

        AnimationHelper.fadeViewIn(refreshLayout, animDuration);
        AnimationHelper.fadeViewOut(progressBar, animDuration);
    }

    public void showProgressBar() {
        super.showProgressBar();

        AnimationHelper.fadeViewIn(progressBar, animDuration);
        AnimationHelper.fadeViewOut(refreshLayout, animDuration);
    }

}
