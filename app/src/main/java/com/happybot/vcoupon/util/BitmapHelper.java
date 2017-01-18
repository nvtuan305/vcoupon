package com.happybot.vcoupon.util;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

public class BitmapHelper {

    public static Bitmap getRoundBitmap(Bitmap bmp, int radius) {
        Bitmap rbmp;
        if (bmp.getWidth() != radius || bmp.getHeight() != radius)
            rbmp = Bitmap.createScaledBitmap(bmp, radius, radius, false);
        else
            rbmp = bmp;

        Bitmap output = Bitmap.createBitmap(rbmp.getWidth(),
                rbmp.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xffa19774;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, rbmp.getWidth(), rbmp.getHeight());

        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setDither(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(Color.parseColor("#BAB399"));

        canvas.drawCircle(rbmp.getWidth() / 2 + 0.7f, rbmp.getHeight() / 2 + 0.7f,
                rbmp.getWidth() / 2 + 0.1f, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(rbmp, rect, rect, paint);


        return output;
    }

    public static Bitmap getCircleBitmapTextInside(int width, int height, String text) {

        Paint paint = new Paint();
        Paint circlePaint = new Paint();
        Bitmap ouput = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        ouput = ouput.copy(ouput.getConfig(), true);
        Canvas canvas = new Canvas(ouput);

        paint.setColor(Color.WHITE);
        paint.setTextSize(32f);
        paint.setAntiAlias(true);
        paint.setTextAlign(Paint.Align.CENTER);

        Rect bounds = new Rect();
        paint.getTextBounds(text, 0, text.length(), bounds);

        circlePaint.setColor(Color.parseColor("#1E88E5"));
        circlePaint.setAntiAlias(true);

        canvas.drawCircle(width / 2 + 0.1f, height / 2 + 0.1f, width / 2 + 0.1f, circlePaint);
        canvas.drawText(text, width / 2 + 0.5f, height / 2 + 0.5f + 12, paint);

        return ouput;
    }

    public static Bitmap getMarkerUserLocation(Context context, int resId) {
        Bitmap.Config conf = Bitmap.Config.ARGB_8888;
        Bitmap bmp = Bitmap.createBitmap(80, 80, conf);
        Canvas canvas1 = new Canvas(bmp);

        // paint defines the text color, stroke width and size
        Paint color = new Paint();
        color.setTextSize(35);
        color.setColor(Color.BLACK);

        // modify canvas
        canvas1.drawBitmap(BitmapFactory.decodeResource(context.getResources(), resId), 0, 0, color);
        canvas1.drawText("User Name!", 30, 40, color);

        return bmp;
    }

    public static Bitmap getBitmapFromXmlLayout(Context context, int resId) {
        Drawable drawable = context.getResources().getDrawable(resId);
        Bitmap bmp = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas();
        canvas.setBitmap(bmp);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return bmp;
    }
}
