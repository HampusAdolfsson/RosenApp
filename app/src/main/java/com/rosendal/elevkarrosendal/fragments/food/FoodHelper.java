package com.rosendal.elevkarrosendal.fragments.food;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.rosendal.elevkarrosendal.AnimationHelper;
import com.rosendal.elevkarrosendal.DrawerActivity;
import com.rosendal.elevkarrosendal.R;

import java.util.ArrayList;

abstract class FoodHelper {

    static void setData(final FoodFragment fragment, FoodData data) {
        for (int i = 0; i < data.food.length; i++) {
            String s = "";
            if (data.food[i] != null) {
                for (int n = 0; n < data.food[i].size(); n++) {
                    s += data.food[i].get(n);
                    if (n < (data.food[i].size() - 1)) {
                        s += "\n";
                    }
                }
            }
            fragment.dishViews[i].setText(s);
        }
        if (data.title != null && data.title.toLowerCase().contains("vecka")) {
            try {
                DrawerActivity activity = (DrawerActivity) fragment.getActivity();
                activity.mListTitles[1] = data.title;
                activity.toolbar.setTitle(data.title);
            } catch (Exception e) {
                Log.e(DrawerActivity.LOG_TAG, "Error setting title: " + data.title, e);
            }
        }

        FoodHelper.updateExtras(fragment, data.extras);

        if (fragment.refreshLayout.isRefreshing()) fragment.refreshLayout.setRefreshing(false);
        fragment.showContent();
    }

    public static void updateExtras(final FoodFragment fragment, ArrayList<String[]> extras) {
        fragment.extras = extras;
        fragment.extrasContainer.removeAllViews();
        if (fragment.extras.size() > 0) {
            for (String[] extra : extras) {
                View v = LayoutInflater.from(fragment.getActivity()).inflate(R.layout.food_extra_item, null);
                ((TextView) v.findViewById(R.id.extraName)).setText(extra[0]);
                ((TextView) v.findViewById(R.id.extraValue)).setText(extra[1]);
                fragment.extrasContainer.addView(v);
            }
            fragment.extrasContainer.requestLayout();
            fragment.extrasContainer.invalidate();
            AnimationHelper.fadeViewIn(fragment.extrasContainer, fragment.animDuration);
        } else {
            AnimationHelper.fadeViewOut(fragment.extrasContainer, fragment.animDuration);
        }
    }
}
