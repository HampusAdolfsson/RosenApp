package com.rosendal.elevkarrosendal.fragments.schedule;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.rosendal.elevkarrosendal.DrawerActivity;
import com.rosendal.elevkarrosendal.R;
import com.rosendal.elevkarrosendal.fragments.refreshable_fragment.RefreshableFragment;
import com.rosendal.elevkarrosendal.fragments.schedule.ScheduleHelper.UpdateType;
import com.rosendal.elevkarrosendal.fragments.schedule.id_picker.IdDialog;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Calendar;

public class ScheduleFragment extends RefreshableFragment<Void> {

    public TouchImageView imageView;
    public Bitmap scheduleBmp;
    public ScheduleImageValues lastUsedValues;
    public boolean idIsValid;
    public TextView mWeekView;
    private ProgressBar progressBar;
    public int weekNumber;
    public int currentWeek;
    public int[] weekArray;

    public boolean reloadOnUnHidden = false;
    public boolean updateOnUnHidden = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        setRetainInstance(true);
        refreshIntervalMs = 2 * 60 * 1000;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        imageView.setImageDrawable(null);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view =  inflater.inflate(R.layout.fragment_schedule, container, false);
        imageView = (TouchImageView) view.findViewById(R.id.iwSchedule);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

        weekNumber = ScheduleHelper.getCurrentCalendar().get(Calendar.WEEK_OF_YEAR);
        currentWeek = weekNumber;

        progressBar = (ProgressBar) view.findViewById(R.id.pbSchedule);

        if (!loadUpdate()){
            reloadOnUnHidden = true;
            showProgressBar();
        } else {
            setHasLoaded(true);
            imageView.setDrawIndicator(ScheduleHelper.shouldDrawIndicator(this));
        }
        return view;

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.schedule, menu);
        super.onCreateOptionsMenu(menu, inflater);
        MenuItem actionItem = menu.findItem(R.id.action_select_week);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.GINGERBREAD_MR1) {
            LinearLayout layout = (LinearLayout) actionItem.getActionView();
            mWeekView = (TextView) layout.findViewById(R.id.tvWeek);
            mWeekView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showWeekPopup(true);
                }
            });
            ImageButton btnNext = (ImageButton) layout.findViewById(R.id.btnNextWeek);
            btnNext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (int i : weekArray) {
                        if (i == weekNumber+1) {
                            weekNumber += 1;
                            updateWeekView();
                            RefreshScheduleTask.running = false;
                            ScheduleHelper.refresh(ScheduleFragment.this, UpdateType.RELOAD);
                            break;
                        }
                    }
                }
            });
            ImageButton btnPrev = (ImageButton) layout.findViewById(R.id.btnPrevWeek);
            btnPrev.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (int i : weekArray) {
                        if (i == weekNumber - 1) {
                            weekNumber -= 1;
                            updateWeekView();
                            RefreshScheduleTask.running = false;
                            ScheduleHelper.refresh(ScheduleFragment.this, UpdateType.RELOAD);
                            break;
                        }
                    }
                }
            });
        }
        updateWeeks();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(true);
        if (!hidden) {
            refreshIfNecessary();
        }
    }

    @Override
    public void refreshIfNecessary() {
        ScheduleHelper.refreshIfNecessary(this);
    }

    @Override
    protected void onRefresh() {}

    @Override
    protected void onRefreshTaskFinished(Void result) { }

    public void updateWeekView() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.GINGERBREAD_MR1) {
            mWeekView.setText("v." + weekNumber);
            if (weekNumber == currentWeek) {
                mWeekView.setTextColor(ContextCompat.getColor(getContext(), android.R.color.primary_text_light));
            } else {
                mWeekView.setTextColor(0xDE444444);
            }
        }
    }
    public void updateWeeks() {
        Calendar now = ScheduleHelper.getCurrentCalendar();
        int maxWeeks = now.getActualMaximum(Calendar.WEEK_OF_YEAR);
        ArrayList<Integer> list = new ArrayList<>();
        list.add(currentWeek);
        for (int i = 1; i < 5; i++) {
            if (currentWeek+i<=maxWeeks) list.add(currentWeek+i);
            if (currentWeek-i>=1) list.add(0, currentWeek-i);
        }
        weekArray = new int[list.size()];
        for (int i = 0; i < weekArray.length; i++) {
            weekArray[i] = list.get(i);
        }
        updateWeekView();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.action_update) {
            ScheduleHelper.refresh(this, UpdateType.RELOAD);
        } else if (menuItem.getItemId() == R.id.action_change_id) {
            new IdDialog(this, false).show();
        } else if (Build.VERSION.SDK_INT == Build.VERSION_CODES.GINGERBREAD_MR1 && menuItem.getItemId() == R.id.action_select_week) {
            showWeekPopup(false);
        }
        return true;
    }

    // show a dropdown to select week
    public void showWeekPopup(Boolean showAsDropDown) {
        final ListView listView = new ListView(getActivity());

        int width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16*2 + 36, getResources().getDisplayMetrics());
        final PopupWindow popup = new PopupWindow(listView, width, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popup.setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(getContext(), android.R.color.background_dark)));

        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, getResources().getDisplayMetrics());
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) popup.setElevation(px);

        int selected = 0;
        for (int i = 0; i < weekArray.length; i++) {
            if (weekNumber == weekArray[i]){
                selected = i;
                break;
            }
        }

        listView.setAdapter(getAdapter(selected));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (weekNumber != weekArray[position]) {
                    listView.setSelection(position);
                    weekNumber = weekArray[position];
                    updateWeekView();
                    ScheduleHelper.refresh(ScheduleFragment.this, UpdateType.RELOAD);
                }
                popup.dismiss(

                );
            }
        });
        listView.setSelection(selected);
        listView.setDividerHeight(0);
        listView.setLayoutParams(new ViewGroup.LayoutParams(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT));

        popup.setContentView(listView);
        if (showAsDropDown) popup.showAsDropDown(mWeekView, - (width - mWeekView.getWidth())/2, 0);
        else popup.showAtLocation(imageView, Gravity.CENTER, 0, 0);
    }
    private ArrayAdapter<String> getAdapter(final int selected) {
        String[] array = new String[weekArray.length];
        for (int i = 0; i < weekArray.length; i++) {
            array[i] = "v." + weekArray[i];
        }
        return new ArrayAdapter<String>(
                getActivity(), R.layout.week_item_row, array) {
            @Override
            public View getView(int pos, View convertView, ViewGroup parent) {
                View v = convertView;

                if(convertView == null) {
                    LayoutInflater vi = LayoutInflater.from(getContext());
                    v = vi.inflate(R.layout.week_item_row, null);
                }

                String s = getItem(pos);

                if (s != null) {
                    TextView tv1 = (TextView) v.findViewById(R.id.text1);
                    if (selected == pos) {
                        tv1.setTextColor(ContextCompat.getColor(getContext(), R.color.primary300));
                    } else if (weekArray[pos] == currentWeek) {
                        tv1.setTextColor(0xB3FFFFFF);
                    } else {
                        tv1.setTextColor(ContextCompat.getColor(getContext(), android.R.color.primary_text_dark));
                    }
                    tv1.setText(s);
                }
                return v;
            }
        };
    }
    private boolean loadUpdate() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());

        long timeInMillis = prefs.getLong("lastUpdate", 0);
        if (timeInMillis != 0) {
            try {
                Bitmap bmp = BitmapFactory.decodeStream(getActivity().openFileInput("cache_empty.png"));
                if (bmp != null && imageView != null) imageView.setImageBitmap(bmp);
                else return false;
            } catch (FileNotFoundException e) {
                Log.e(DrawerActivity.LOG_TAG, "Error loading cache", e);
                return false;
            }
        } else return false;
        weekNumber = prefs.getInt("week", 54);
        currentWeek = prefs.getInt("currentWeek", 54);
        idIsValid = prefs.getBoolean("idIsValid", false);

        int x = prefs.getInt("indMet" + TouchImageView.X, 0);
        int y = prefs.getInt("indMet" + TouchImageView.Y, 0);
        int w = prefs.getInt("indMet" + TouchImageView.WIDTH, 0);
        int h = prefs.getInt("indMet" + TouchImageView.HEIGHT, 0);
        int l = prefs.getInt("indMet" + TouchImageView.LINE_HEIGHT, 0);

        imageView.setIndicatorRect(x, y, w, h, l);

        int i = prefs.getInt("lastImageValues", 0);
        if (i == 2) lastUsedValues = ScheduleImageValues.large;
        else if (i == 1) lastUsedValues = ScheduleImageValues.medium;
        else if (i == 3) lastUsedValues = ScheduleImageValues.mediumDouble;
        else if (i == 4) lastUsedValues = ScheduleImageValues.smallDouble;
        else if (i == 5) lastUsedValues = ScheduleImageValues.largeDouble;
        else lastUsedValues = ScheduleImageValues.small;

        return true;
    }

    @Override
    protected void showContent() {
        super.showContent();
        progressBar.setVisibility(View.GONE);
        imageView.setVisibility(View.VISIBLE);
    }
    public void showProgressBar() {
        super.showProgressBar();
        progressBar.setVisibility(View.VISIBLE);
        imageView.setVisibility(View.GONE);

    }


}