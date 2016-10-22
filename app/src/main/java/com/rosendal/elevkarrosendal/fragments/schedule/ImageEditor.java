package com.rosendal.elevkarrosendal.fragments.schedule;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import java.util.Calendar;

public class ImageEditor {
    public static Bitmap cropWeekSchedule(Bitmap bmp, ScheduleImageValues values) {
        if (bmp == null) return null;
        Rect cropValuesRect = values.getCropValues();
        return Bitmap.createBitmap(bmp, cropValuesRect.left, cropValuesRect.top, cropValuesRect.right - cropValuesRect.left, cropValuesRect.bottom - cropValuesRect.top);
    }


    private static double getTimeOffset(Calendar cal) {
        cal.add(Calendar.HOUR_OF_DAY, -8);
        return (60 * cal.get(Calendar.HOUR_OF_DAY) + cal.get(Calendar.MINUTE)) / (60.0 * 10.0 + 30);
    }

    public static Bitmap fillColor(Bitmap bmp, int color) {
        Bitmap bitmap = Bitmap.createBitmap(bmp.getWidth(), bmp.getHeight(), Bitmap.Config.ARGB_4444);
        for (int x = 0; x < bmp.getWidth(); x++) {
            for (int y = 0; y < bmp.getHeight(); y++) {
                int alpha = Color.alpha(bmp.getPixel(x, y));
                if ((alpha) != 0x00) {
                    bitmap.setPixel(x, y, Color.argb(alpha, Color.red(color), Color.green(color), Color.blue(color)));
                }
            }
        }
        return bitmap;
    }

    public static Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }

        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }

    public static Bitmap getRoundedBitmap(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap
                .getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = bitmap.getWidth() / 2;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }
}
