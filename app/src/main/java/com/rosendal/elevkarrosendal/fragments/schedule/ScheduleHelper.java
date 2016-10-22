package com.rosendal.elevkarrosendal.fragments.schedule;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.preference.PreferenceManager;
import android.util.Log;

import com.rosendal.elevkarrosendal.DrawerActivity;
import com.rosendal.elevkarrosendal.fragments.refreshable_fragment.RefreshTaskHandler;

import java.io.FileOutputStream;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

public class ScheduleHelper {

    public static void refreshIfNecessary(ScheduleFragment fragment) {
        UpdateType type = UpdateType.NO_UPDATE;

        Calendar now = getCurrentCalendar();
        if (fragment.currentWeek != now.get(Calendar.WEEK_OF_YEAR)) {
            fragment.currentWeek = now.get(Calendar.WEEK_OF_YEAR);
            fragment.weekNumber = fragment.currentWeek;
            type = UpdateType.RELOAD;
            if (fragment.mWeekView != null) fragment.updateWeeks();
        }
        if (fragment.getTimeSinceLastUpdate() != null) {
            if (fragment.reloadOnUnHidden || fragment.getTimeSinceLastUpdate() > 60 * 60 * 1000) {
                type = UpdateType.RELOAD;
            } else if ((fragment.updateOnUnHidden || fragment.getTimeSinceLastUpdate() > 2 * 60 * 1000)
                    && fragment.scheduleBmp != null) {
                type = UpdateType.REDRAW;
            }
        } else {
            type = UpdateType.RELOAD;
        }

        fragment.updateOnUnHidden = false;
        fragment.reloadOnUnHidden = false;
        refresh(fragment, type);
    }

    public static void refresh(ScheduleFragment fragment, UpdateType type) {
        if (type == UpdateType.REDRAW) {
            Log.d(DrawerActivity.LOG_TAG, "Redrawing schedule");
            redraw(fragment);
        } else if (type == UpdateType.RELOAD) {
            reload(fragment);
        }
    }

    public static void updateIndicator(ScheduleFragment fragment, ScheduleImageValues values) {
        if (!shouldDrawIndicator(fragment)) {
            fragment.imageView.setDrawIndicator(false);
            return;
        }
        fragment.imageView.setDrawIndicator(true);

        Calendar cal = getCurrentCalendar();
        cal.add(Calendar.HOUR_OF_DAY, -8);
        double timeOffset = (60 * cal.get(Calendar.HOUR_OF_DAY) + cal.get(Calendar.MINUTE)) / (60.0 * 10.0 + 30);

        int x = values.getTimeColumnWidth() + values.getColumnWidth() * (cal.get(Calendar.DAY_OF_WEEK) - 2);
        int y = values.getDayRowHeight();
        int w = values.getColumnWidth();
        int h = (int) Math.round(timeOffset * values.getScheduleHeight());

        fragment.imageView.setIndicatorRect(x, y, w, h, values.getLineHeight());
    }


    public static void saveSuccessfulUpdate(ScheduleFragment fragment, long time, ScheduleImageValues values, boolean isValid, int week, int currentWeek, Bitmap emptyBmp) {
        SharedPreferences.Editor prefs = PreferenceManager.getDefaultSharedPreferences(fragment.getActivity().getApplicationContext()).edit();
        prefs.putInt("week", week);
        prefs.putInt("currentWeek", currentWeek);
        prefs.putLong("lastUpdate", time);
        prefs.putBoolean("idIsValid", isValid);
        int[] indicatorMetrics = fragment.imageView.getIndicatorMetrics();
        for (int i = 0; i < indicatorMetrics.length; i++) {
            prefs.putInt("indMet" + i, indicatorMetrics[i]);
        }
        int i = 6;
        if (values == ScheduleImageValues.large) i = 2;
        else if (values == ScheduleImageValues.medium) i = 1;
        else if (values == ScheduleImageValues.small) i = 0;
        else if (values == ScheduleImageValues.smallDouble) i = 4;
        else if (values == ScheduleImageValues.mediumDouble) i = 3;
        else if (values == ScheduleImageValues.largeDouble) i = 5;
        if (i < 6) prefs.putInt("lastImageValues", i);

        prefs.apply();

        try {
            FileOutputStream fo = fragment.getActivity().openFileOutput("cache_empty.png", Context.MODE_PRIVATE);
            emptyBmp.compress(Bitmap.CompressFormat.PNG, 100, fo);
            if (fo != null) fo.close();

        } catch (Exception e) {
            Log.e(DrawerActivity.LOG_TAG, "", e);
        }
    }

    public static Calendar getCurrentCalendar() {
        return new GregorianCalendar(TimeZone.getDefault(), new Locale("se", "SE"));
    }

    public static boolean shouldDrawIndicator(ScheduleFragment fragment) {
        Calendar cal = getCurrentCalendar();
        return PreferenceManager.getDefaultSharedPreferences(fragment.getActivity()).getBoolean("show_time_hint", true)
                && fragment.currentWeek == fragment.weekNumber && fragment.idIsValid
                && ((cal.get(Calendar.HOUR_OF_DAY) >= 8) && (cal.get(Calendar.HOUR_OF_DAY) < 18 || (cal.get(Calendar.HOUR_OF_DAY) == 18 && cal.get(Calendar.MINUTE) < 30)))
                && (cal.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY && cal.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY);
    }

    private static void redraw(ScheduleFragment fragment) {
        updateIndicator(fragment, fragment.lastUsedValues);
        saveSuccessfulUpdate(fragment, System.currentTimeMillis(), fragment.lastUsedValues, fragment.idIsValid, fragment.weekNumber, fragment.currentWeek, fragment.scheduleBmp);
    }

    private static void reload(ScheduleFragment fragment) {
        if (!RefreshScheduleTask.running) {
            new RefreshScheduleTask(new RefreshTaskHandler<>(fragment), fragment).execute();
        }
    }

    public enum UpdateType {
        NO_UPDATE, REDRAW, RELOAD
    }

}
