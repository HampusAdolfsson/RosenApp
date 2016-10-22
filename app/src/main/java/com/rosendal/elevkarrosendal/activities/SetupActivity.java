package com.rosendal.elevkarrosendal.activities;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.rosendal.elevkarrosendal.R;
import com.rosendal.elevkarrosendal.fragments.schedule.id_picker.IdFragment;

public class SetupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        if (Build.VERSION.SDK_INT >= 21) getWindow().setStatusBarColor(ContextCompat.getColor(SetupActivity.this, R.color.primary700));

        getSupportFragmentManager().beginTransaction().replace(R.id.container, new IdFragment()).commit();
    }

    private class SetupAdapter extends FragmentPagerAdapter {
        public SetupAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int pos) {
            return new IdFragment();
        }

        @Override
        public int getCount() {
            return 1;
        }
    }
    private class SetupPageTransformer implements ViewPager.PageTransformer {
        @Override
        public void transformPage(View view, float pos) {
            int pageWidth = view.getWidth();

            TextView tv2 = (TextView) view.findViewById(R.id.tvIdSetup);
            if (tv2 != null) tv2.setTranslationX((pos) * (pageWidth / 4));

            EditText et = (EditText) view.findViewById(R.id.etIdSetup);
            if (et != null) et.setTranslationX((pos) * (pageWidth / 2));

            Button btn = (Button) view.findViewById(R.id.btnSetupContinue);
            if (btn != null) btn.setTranslationX((pos) * (pageWidth));
        }
    }


}
