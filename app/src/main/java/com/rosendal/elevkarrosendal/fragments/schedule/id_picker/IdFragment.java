package com.rosendal.elevkarrosendal.fragments.schedule.id_picker;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.rosendal.elevkarrosendal.DrawerActivity_;
import com.rosendal.elevkarrosendal.R;

import java.util.ArrayList;

public class IdFragment extends Fragment {
    public static final String FROM_SETUP = "from_setup";

    private EditText editText;
    private Button contBtn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.id_setup_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        editText = (EditText) getActivity().findViewById(R.id.etIdSetup);
        editText.requestFocus();
        contBtn = (Button) getActivity().findViewById(R.id.btnSetupContinue);
        contBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext()).edit();
                editor.putBoolean("setup_done", true);
                if (!editText.getText().toString().isEmpty())
                    editor.putString("schedule_id", editText.getText().toString());
                editor.commit();
                if (!editText.getText().toString().isEmpty()) {
                    ArrayList<String> list = new ArrayList<String>();
                    list.add(editText.getText().toString());
                    list.add("");
                    IdDialog.saveIds(getActivity().getApplicationContext(), list);
                }
                Intent intent = new Intent(getActivity(), DrawerActivity_.class);
                intent.putExtra(FROM_SETUP, true);
                startActivity(intent);
                getActivity().finish();
            }
        });
        ViewCompat.setElevation(contBtn, TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources().getDisplayMetrics()));
    }
}
