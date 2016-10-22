package com.rosendal.elevkarrosendal;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Build;
import android.view.View;

public abstract class AnimationHelper {

    public static void fadeViewOut(final View view, final int duration) {
        if (view.getVisibility() == View.GONE) return;
        if (Build.VERSION.SDK_INT >= 12) {
            view.animate()
                    .setDuration(duration)
                    .alpha(0f)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            view.setVisibility(View.GONE);
                        }
                    });
        } else {
            view.setVisibility(View.GONE);
        }
    }

    public static void fadeViewIn(final View view, final int duration) {
        if (view.getVisibility() != View.VISIBLE ) {
            view.setVisibility(View.VISIBLE);
            if (Build.VERSION.SDK_INT >= 12) {
                view.animate()
                        .setDuration(duration)
                        .alpha(1f)
                        .setListener(null);
            }
        }
    }
}
