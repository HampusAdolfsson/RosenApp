package com.rosendal.elevkarrosendal.fragments.food;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rosendal.elevkarrosendal.AnimationHelper;
import com.rosendal.elevkarrosendal.R;
import com.rosendal.elevkarrosendal.fragments.refreshable_fragment.RefreshTaskHandler;
import com.rosendal.elevkarrosendal.fragments.refreshable_fragment.RefreshableFragment;

import org.androidannotations.annotations.EFragment;

import java.util.ArrayList;

@EFragment
public class FoodFragment extends RefreshableFragment<FoodData> {
    TextView[] dishViews;
    ArrayList<String[]> extras;
    SwipeRefreshLayout refreshLayout;
    LinearLayout extrasContainer;

    private LinearLayout foodContainer;
    private RelativeLayout outerContainer;

    private ProgressBar progressBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        setRetainInstance(true);
        refreshIntervalMs = 60 * 60 * 1000;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_food, container, false);
        dishViews = new TextView[5];
        dishViews[0] = (TextView) view.findViewById(R.id.dish1);
        dishViews[1] = (TextView) view.findViewById(R.id.dish2);
        dishViews[2] = (TextView) view.findViewById(R.id.dish3);
        dishViews[3] = (TextView) view.findViewById(R.id.dish4);
        dishViews[4] = (TextView) view.findViewById(R.id.dish5);

        foodContainer = (LinearLayout) view.findViewById(R.id.llFoodContainer);
        outerContainer = (RelativeLayout) view.findViewById(R.id.content);
        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refreshLayout);
        refreshLayout.setColorSchemeColors(ContextCompat.getColor(getContext(), R.color.primary400), 0xFF000000);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                forceRefresh();
            }
        });
        extrasContainer = (LinearLayout) view.findViewById(R.id.llExtras);

        progressBar = (ProgressBar) view.findViewById(R.id.pbFood);

        if (savedInstanceState != null) {
            for (int i = 0; i < dishViews.length; i++) {
                String s = savedInstanceState.getString("dish" + i);
                dishViews[i].setText(s != null ? s : "");
            }
            if (!pbShowing) {
                progressBar.setVisibility(View.GONE);
                outerContainer.setVisibility(View.VISIBLE);
            }

            int size = savedInstanceState.getInt("extrasLength", 0);
            if (size > 0) {
                extras = new ArrayList<>();
                for (int i = 0; i < size; i++) {
                    extras.add(new String[]{savedInstanceState.getString("0" + i), savedInstanceState.getString("1" + i)});
                }
                FoodHelper.updateExtras(this, extras);
            }
            setHasLoaded(true);
        }
        return view;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(true);
        if (!hidden) {
            refreshIfNecessary();
        }
    }

    @Override
    protected void onRefresh() {
        new RefreshFoodTask(new RefreshTaskHandler<>(this), animDuration + 50).execute();
    }

    @Override
    protected void onRefreshTaskFinished(FoodData result) {
        FoodHelper.setData(this, result);
    }

    @Override
    public void showContent() {
        super.showContent();

        if (Build.VERSION.SDK_INT >= 12) {
            Animation slideIn = AnimationUtils.loadAnimation(getActivity(), android.R.anim.slide_in_left);
            slideIn.setDuration(animDuration);
            LayoutAnimationController animController = new LayoutAnimationController(slideIn, 0.1f);
            foodContainer.setLayoutAnimation(animController);
            outerContainer.setAlpha(1f);
            outerContainer.setVisibility(View.VISIBLE);

            progressBar.animate()
                    .alpha(0f)
                    .setDuration(animDuration)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            progressBar.setVisibility(View.GONE);
                        }
                    });
        } else {
            progressBar.setVisibility(View.GONE);
            outerContainer.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void showProgressBar() {
        super.showProgressBar();

        AnimationHelper.fadeViewIn(progressBar, animDuration);
        AnimationHelper.fadeViewOut(outerContainer, animDuration);
        AnimationHelper.fadeViewOut(extrasContainer, animDuration);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //inflater.inflate(R.menu.food, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    /*@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_vote) {
            if (!PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext()).getBoolean("logged_in", false)) {
                startActivity(new Intent(getActivity(), LoginActivity.class));
            } else
                startActivity(new Intent(getActivity(), PollActivity_.class));
        }
        return super.onOptionsItemSelected(item);
    }*/

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        for (int i = 0; i < dishViews.length; i++) {
            String s = dishViews[i].getText().toString();
            if (!s.isEmpty()) savedInstanceState.putString("dish" + i, s);
        }
        if (extras != null) {
            for (int i = 0; i < extras.size(); i++) {
                savedInstanceState.putString("0" + i, extras.get(i)[0]);
                savedInstanceState.putString("1" + i, extras.get(i)[1]);
            }
            savedInstanceState.putInt("extrasLength", extras.size());
        } else {
            savedInstanceState.putInt("extrasLength", 0);
        }
    }
}