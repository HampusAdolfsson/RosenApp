package com.rosendal.elevkarrosendal.fragments.settings;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.support.v4.preference.PreferenceFragment;

import com.rosendal.elevkarrosendal.DrawerActivity;
import com.rosendal.elevkarrosendal.R;
import com.rosendal.elevkarrosendal.fragments.schedule.ScheduleFragment;
import com.rosendal.elevkarrosendal.fragments.schedule.id_picker.IdDialog;

public class SettingsFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);

        Preference preference = findPreference("schedule_resolution");
        preference.setSummary(getResources().getStringArray(R.array.res_entries)[Integer.parseInt(getPreferenceScreen().getSharedPreferences().getString("schedule_resolution", "1"))]);
        final Preference idPreference = findPreference("schedule_id");
        idPreference.setSummary(getPreferenceScreen().getSharedPreferences().getString("schedule_id", ""));
        idPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                new IdDialog((ScheduleFragment)((DrawerActivity)getActivity()).fragments[DrawerActivity.SCHEDULE], true, idPreference).show();
                return true;
            }
        });
        setRetainInstance(true);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals("show_time_hint")) {
            setReloadOnUnHidden(true);
        } else if (key.equals("schedule_resolution")) {
            setReloadOnUnHidden(false);
            Preference preference = findPreference("schedule_resolution");
            preference.setSummary(getResources().getStringArray(R.array.res_entries)[Integer.parseInt(sharedPreferences.getString("schedule_resolution", "0"))]);
        } else if (key.equals("schedule_id")) {
            setReloadOnUnHidden(false);
            Preference idPreference = findPreference("schedule_id");
            idPreference.setSummary(sharedPreferences.getString("schedule_id", ""));
        }
    }

    public void setReloadOnUnHidden(boolean onlyUpdate) {
        DrawerActivity drawerActivity = (DrawerActivity) getActivity();
        if (drawerActivity != null) {
            ScheduleFragment fragment = (ScheduleFragment) drawerActivity.fragments[DrawerActivity.SCHEDULE];
            if (fragment != null) {
                if (onlyUpdate) fragment.updateOnUnHidden = true;
                else fragment.reloadOnUnHidden = true;
            }
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            findPreference("schedule_id").setSummary(getPreferenceScreen().getSharedPreferences().getString("schedule_id", ""));
            getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
        } else {
            getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
        }
    }
}
