package com.rosendal.elevkarrosendal.fragments.schedule;

import android.graphics.Rect;

public class ScheduleImageValues {
    private int width, height;
    private Rect cropValues;
    private int timeColumnWidth;
    private int columnWidth;
    private int dayRowHeight;
    private int scheduleHeight;
    private int lineWidth;
    private int lineHeight;

    public static final ScheduleImageValues small = new ScheduleImageValues(620, 877, new Rect(0, 114, 619, 702), 43, 107, 34, 554, 106, 1);
    public static final ScheduleImageValues smallDouble = new ScheduleImageValues(620, 877, new Rect(0, 114, 596, 702), 24, 114, 41, 547, 113, 1);
    public static final ScheduleImageValues medium = new ScheduleImageValues(930, 1315, new Rect(0 ,171, 929, 1052), 65, 160, 49, 831, 159, 2);
    public static final ScheduleImageValues mediumDouble = new ScheduleImageValues(930, 1315, new Rect(0 ,171, 929, 1052), 34, 172, 61, 819, 171, 2);
    public static final ScheduleImageValues large = new ScheduleImageValues(1240, 1753, new Rect(0, 227, 1239, 1403), 85, 214, 67, 1108, 213, 3);
    public static final ScheduleImageValues largeDouble = new ScheduleImageValues(1240, 1753, new Rect(0, 227, 1192, 1403), 46, 229, 83, 1092, 228, 3);

    public ScheduleImageValues(int width, int height, Rect cropValues, int timeColumnWidth, int columnWidth, int dayRowHeight, int scheduleHeight, int lineWidth, int lineHeight) {
        this.width = width;
        this.height = height;
        this.cropValues = cropValues;
        this.timeColumnWidth = timeColumnWidth;
        this.columnWidth = columnWidth;
        this.dayRowHeight = dayRowHeight;
        this.scheduleHeight = scheduleHeight;
        this.lineWidth = lineWidth;
        this.lineHeight = lineHeight;
    }
    public int getWidth() {
        return width;
    }
    public int getHeight() {
        return height;
    }
    public Rect getCropValues() {
        return cropValues;
    }
    public int getTimeColumnWidth() {
        return timeColumnWidth;
    }
    public int getColumnWidth() {
        return columnWidth;
    }
    public int getDayRowHeight() {
        return dayRowHeight;
    }
    public int getScheduleHeight() {
        return scheduleHeight;
    }
    public int getLineWidth() {
        return lineWidth;
    }
    public int getLineHeight() {
        return lineHeight;
    }
}
