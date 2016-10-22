package com.rosendal.elevkarrosendal;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.ViewTreeObserver;

import com.rosendal.elevkarrosendal.activities.SetupActivity;
import com.rosendal.elevkarrosendal.fragments.refreshable_fragment.RefreshableFragment;
import com.rosendal.elevkarrosendal.fragments.schedule.id_picker.IdFragment;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.InstanceState;
import org.androidannotations.annotations.ViewById;

import io.fabric.sdk.android.Fabric;

@EActivity(R.layout.activity_drawer)
public class DrawerActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, ErrorHandler {

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "KWD29VdlEOvg46RUYpRJuPnyH";
    private static final String TWITTER_SECRET = "glwI6cIgfOgBN4wzMH0fpCQUvfrfllMM6qxw80xpCe01gfLW4N";

    public static final String LOG_TAG = "RosenApp";

    private boolean setupLaunched = false;

    public static final int SCHEDULE = 0;
    private static final int FOOD = 1;
    private static final int FEED = 2;
    private static final int CONTACT = 3;
    private static final int SETTINGS = 4;

    private static final int numberOfFragments = SETTINGS;
    public Fragment[] fragments = new Fragment[numberOfFragments + 1]; //One extra for errorFragment, which is not in drawer

    @InstanceState
    int selectedIndex;

    public String[] mListTitles;
    private ActionBarDrawerToggle mDrawerToggle;

    @ViewById(R.id.snackbarPosition)
    public CoordinatorLayout coord;

    @ViewById(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;

    @ViewById(R.id.toolbar)
    public Toolbar toolbar;

    @ViewById(R.id.navigationView)
    NavigationView navigationView;

    @Extra(IdFragment.FROM_SETUP)
    boolean fromSetup;

    @AfterViews
    void initViews() {
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));

        setupLaunched = !PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getBoolean("setup_done", false);

        if (setupLaunched) {
            startActivity(new Intent(this, SetupActivity.class));
            finish();
        } else {
            /*GoogleApiAvailability api = GoogleApiAvailability.getInstance();
            int response = api.isGooglePlayServicesAvailable(DrawerActivity.this);
            if (response == ConnectionResult.SERVICE_MISSING || response == ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED) {
                api.getErrorDialog(DrawerActivity.this, response, 0);
            }*/

            navigationView.setNavigationItemSelectedListener(this);

            mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.empty_string, R.string.empty_string);
            mDrawerToggle.setDrawerIndicatorEnabled(true);
            mDrawerLayout.setDrawerListener(mDrawerToggle);
            mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

            FragmentManager fm = getSupportFragmentManager();
            fragments[SCHEDULE] = fm.findFragmentById(R.id.scheduleFragment);
            fragments[FOOD] = fm.findFragmentById(R.id.foodFragment);
            fragments[FEED] = fm.findFragmentById(R.id.feedFragment);
            fragments[CONTACT] = fm.findFragmentById(R.id.contactFragment);
            fragments[SETTINGS] = fm.findFragmentById(R.id.settingsFragment);

            toolbar.setTitleTextColor(ContextCompat.getColor(DrawerActivity.this, android.R.color.white));
            setSupportActionBar(toolbar);
            mDrawerLayout.setStatusBarBackgroundColor(ContextCompat.getColor(DrawerActivity.this, R.color.primary700));

            setTitle(mListTitles[selectedIndex]);
            showFragment(selectedIndex, false);
            if (fromSetup) {
                ViewTreeObserver vto = mDrawerLayout.getViewTreeObserver();
                if (vto != null) vto.addOnPreDrawListener(new ShouldShowListener(mDrawerLayout));
                getIntent().removeExtra(IdFragment.FROM_SETUP);
            }
            int menuId = R.id.dm_schedule;
            switch (selectedIndex) {
                case SCHEDULE:
                    menuId = R.id.dm_schedule;
                    break;
                case FOOD:
                    menuId = R.id.dm_menu;
                    break;
                case FEED:
                    menuId = R.id.dm_events;
                    break;
                case CONTACT:
                    menuId = R.id.dm_contact;
                    break;
                case SETTINGS:
                    menuId = R.id.dm_settings;
                    break;
            }
            navigationView.setCheckedItem(menuId);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mListTitles = new String[]{"Schema", "Meny", "Händelser", "Kontakt", "Inställningar"};
        if (savedInstanceState != null) {
            mListTitles[FOOD] = savedInstanceState.getString("foodtitle");
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        if (!setupLaunched) mDrawerToggle.syncState();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (fragments[selectedIndex] instanceof RefreshableFragment) ((RefreshableFragment)fragments[selectedIndex]).refreshIfNecessary();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return mDrawerToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        if(menuItem.isChecked()) menuItem.setChecked(false);
        else menuItem.setChecked(true);

        mDrawerLayout.closeDrawers();

        int fragmentIndex = SCHEDULE;
        switch (menuItem.getItemId()) {
            case R.id.dm_schedule:
                fragmentIndex = SCHEDULE;
                break;
            case R.id.dm_menu:
                fragmentIndex = FOOD;
                break;
            case R.id.dm_events:
                fragmentIndex = FEED;
                break;
            case R.id.dm_contact:
                fragmentIndex = CONTACT;
                break;
            case R.id.dm_settings:
                fragmentIndex = SETTINGS;
                break;
        }
        showFragment(fragmentIndex, false);

        return true;
    }


    private void showFragment(int fragmentIndex, boolean addToBackStack) {

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        for (int i = 0; i < fragments.length; i++) {
            if (i == fragmentIndex) {
                transaction.show(fragments[i]);
            } else {
                transaction.hide(fragments[i]);
            }
        }
        if (addToBackStack) {
            transaction.addToBackStack(null);
        }
        transaction.commit();
        selectedIndex = fragmentIndex;

        toolbar.setTitle(mListTitles[fragmentIndex]);
        mDrawerLayout.closeDrawer(GravityCompat.START);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putString("foodtitle", mListTitles[FOOD]);
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    public void onNoInternet() {
        Snackbar.make(coord, R.string.network_dialog_msg, Snackbar.LENGTH_SHORT).show();
    }
    public void onError() {
        Snackbar.make(coord, R.string.unknown_error_msg, Snackbar.LENGTH_SHORT).show();
    }


    private static class ShouldShowListener implements ViewTreeObserver.OnPreDrawListener {

        private final DrawerLayout drawerLayout;

        private ShouldShowListener(DrawerLayout drawerLayout) {
            this.drawerLayout = drawerLayout;
        }

        @Override
        public boolean onPreDraw() {
            if (drawerLayout != null) {
                ViewTreeObserver vto = drawerLayout.getViewTreeObserver();
                if (vto != null) {
                    vto.removeOnPreDrawListener(this);
                }
                drawerLayout.openDrawer(Gravity.LEFT);
            }

            return true;
        }
    }
}
