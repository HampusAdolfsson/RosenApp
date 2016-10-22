package com.rosendal.elevkarrosendal.fragments.schedule.id_picker;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.rosendal.elevkarrosendal.R;
import com.rosendal.elevkarrosendal.fragments.schedule.ScheduleFragment;
import com.rosendal.elevkarrosendal.fragments.schedule.ScheduleHelper;

import java.util.ArrayList;

public class IdDialog {
    private AlertDialog.Builder builder;
    private IdAdapter adapter;
    private ScheduleFragment fragment;
    private boolean updateLater;
    private Preference pref;

    public LinearLayout idSelect;
    public EditText text;
    public RecyclerView recyclerView;

    public int mShortAnimationDuration;

    private String oldId;

    private static final String KEY_ID = "id";
    private static final String KEY_ID_SET_SIZE = "id_set_size";

    public View view;

    public IdDialog(ScheduleFragment fr, boolean delayUpdate, Preference preference) {
        this(fr, delayUpdate);
        pref = preference;
    }

    public IdDialog(ScheduleFragment fr, boolean delayUpdate) {
        fragment = fr;
        updateLater = delayUpdate;
        mShortAnimationDuration = fragment.getResources().getInteger(android.R.integer.config_shortAnimTime);

        ArrayList<String> list = getIds(fragment.getActivity().getApplicationContext());
        list.add("");
        oldId = PreferenceManager.getDefaultSharedPreferences(fragment.getActivity().getApplicationContext()).getString("schedule_id", "");

        if (Build.VERSION.SDK_INT >= 21)  builder = new AlertDialog.Builder(fragment.getActivity(), R.style.DialogTheme);
        else builder = new AlertDialog.Builder(fragment.getActivity());

        LayoutInflater inflater = fragment.getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.id_dialog, null);

        idSelect = (LinearLayout) view.findViewById(R.id.llEnterId);
        idSelect.setVisibility(View.GONE);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerId);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(fragment.getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new IdAdapter(list, this, fragment.getActivity());
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(fragment.getActivity(), DividerItemDecoration.VERTICAL_LIST));
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).equals(oldId)) adapter.setSelected(i);
        }

        text = (EditText) view.findViewById(R.id.etId);

        builder.setTitle(R.string.id_dialog_title)
                .setView(view)
                .setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (recyclerView.getVisibility() == View.VISIBLE) {
                            String id = adapter.mData.get(adapter.selectedIndex);
                            if (!id.equals(oldId)) {
                                PreferenceManager.getDefaultSharedPreferences(fragment.getActivity().getApplicationContext()).edit().putString("schedule_id", id).commit();
                                if (updateLater) fragment.reloadOnUnHidden = true;
                                else ScheduleHelper.refresh(fragment, ScheduleHelper.UpdateType.RELOAD);
                                if (pref != null) pref.setSummary(id);
                            }
                        } else {
                            if (!text.getText().toString().isEmpty()) {
                                PreferenceManager.getDefaultSharedPreferences(fragment.getActivity().getApplicationContext()).edit().putString("schedule_id", text.getText().toString()).commit();
                                if (pref != null) pref.setSummary(text.getText().toString());
                                adapter.mData.add(adapter.mData.size() - 1, text.getText().toString());
                                if (updateLater) fragment.reloadOnUnHidden = true;
                                else ScheduleHelper.refresh(fragment, ScheduleHelper.UpdateType.RELOAD);
                            }
                        }
                        saveIds(fragment.getActivity().getApplicationContext(), adapter.mData);
                    }
                });
    }

    public static void saveIds (Context applicationContext, ArrayList<String> ids) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(applicationContext).edit();
        editor.putInt(KEY_ID_SET_SIZE, ids.size() - 1);
        for (int i = 0; i < ids.size() - 1; i++) {
            editor.putString(KEY_ID + i, ids.get(i));
        }
        editor.apply();
    }

    public static ArrayList<String> getIds(Context applicationContexts) {
        ArrayList<String> toReturn = new ArrayList<>();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(applicationContexts);
        int count = prefs.getInt(KEY_ID_SET_SIZE, 0);
        for (int i = 0; i < count; i++) {
            toReturn.add(prefs.getString(KEY_ID + i, ""));
        }
        return toReturn;
    }
    public AlertDialog dialog;

    public void show() {
        dialog = builder.create();
        dialog.show();
    }
    /*private static final String classes[] = new String[]{"jo12", "na12a", "na12b", "na12c", "na12d",
            "na13a", "na13b", "na13c", "na13d",
            "na14a", "na14b", "na14c", "na14d",
            "smip12"};
    private static final String classes2[] = new String[]{"smip13", "smip14"};
    public static String convertId(String id) {
        for (String s : classes) {
            if (s.equalsIgnoreCase(id)) {
                return String.format("%s/1, %s/2", id, id);
            }
        }
        for (String s : classes2) {
            if (s.equalsIgnoreCase(id)) return String.format("%sA, %sB", id, id);
        }
        return id;
    }*/
}
