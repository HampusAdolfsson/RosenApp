package com.rosendal.elevkarrosendal.fragments.schedule;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.preference.PreferenceManager;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.rosendal.elevkarrosendal.DrawerActivity;
import com.rosendal.elevkarrosendal.fragments.refreshable_fragment.RefreshTask;
import com.rosendal.elevkarrosendal.fragments.refreshable_fragment.RefreshTaskHandler;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class RefreshScheduleTask extends RefreshTask<Void> {

    public static final int COLORS = 1;
    public static final int BLACK_AND_WHITE = 2;

    private static final int NO_UPDATE = 0;
    private static final int RELOAD = 1;
    private static final int REDRAW = 2;

    private ScheduleFragment fragment;

    private ScheduleImageValues values;

    public RefreshScheduleTask(RefreshTaskHandler<Void> handler, ScheduleFragment fragment) {
        super(handler);
        this.fragment = fragment;
    }

    @Override
    protected Void doWork() {
        String id = PreferenceManager.getDefaultSharedPreferences(fragment.getActivity().getApplicationContext()).getString("schedule_id", "");
        switch (Integer.parseInt(PreferenceManager.getDefaultSharedPreferences(fragment.getActivity().getApplicationContext()).getString("schedule_resolution", "1"))) {
            case 0:
                values = id.contains(",") ? ScheduleImageValues.largeDouble : ScheduleImageValues.large;
                break;
            case 1:
                values = id.contains(",") ? ScheduleImageValues.mediumDouble : ScheduleImageValues.medium;
                break;
            case 2:
                values = id.contains(",") ? ScheduleImageValues.smallDouble : ScheduleImageValues.small;
                break;
            default:
                values = ScheduleImageValues.small;
        }

        fragment.lastUsedValues = values;
        try {
            final String url = "http://www.novasoftware.se/ImgGen/schedulegenerator.aspx?format=png&schoolid=81320/sv-se&type=-1&id=" + URLEncoder.encode(id, "UTF-8") + "&period=&week=" + fragment.weekNumber + "&mode=0&printer=1&colors=" + COLORS + "&head=1&clock=1&foot=1&day=" + 0 + "&width=" + values.getWidth() + "&height=" + values.getHeight() + "&count=1&decrypt=0";
            fragment.getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Glide.with(fragment).load(url)
                            .asBitmap()
                            .into(new SimpleTarget<Bitmap>() {
                                @Override
                                public void onResourceReady(final Bitmap resource, GlideAnimation anim) {
                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            fragment.idIsValid = !(resource.getPixel(10, 10) == -525362);
                                            final Bitmap schedule = ImageEditor.cropWeekSchedule(resource, values);
                                            ScheduleHelper.updateIndicator(fragment, values);
                                            ScheduleHelper.saveSuccessfulUpdate(fragment, System.currentTimeMillis(),
                                                    fragment.lastUsedValues, fragment.idIsValid, fragment.weekNumber, fragment.currentWeek, schedule);
                                            fragment.getActivity().runOnUiThread(
                                                    new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            if (schedule != null) {
                                                                BitmapDrawable drawable = ((BitmapDrawable) fragment.imageView.getDrawable());
                                                                if (drawable != null)
                                                                    drawable.getBitmap().recycle();
                                                                fragment.imageView.setImageBitmap(schedule);
                                                                fragment.showContent();
                                                            }
                                                            if (fragment.scheduleBmp != null)
                                                                fragment.scheduleBmp.recycle();
                                                            fragment.scheduleBmp = schedule;
                                                        }
                                                    });
                                        }
                                    }).start();

                                }
                            });

                }
            });
        } catch (UnsupportedEncodingException e) {
            Log.e(DrawerActivity.LOG_TAG, "Error encoding url", e);
        }
        return null;
    }
}