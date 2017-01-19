package com.happybot.vcoupon.util;

import android.text.format.DateFormat;
import android.text.format.DateUtils;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateTimeConverter {

    /**
     * Get remain time from now to the future date
     *
     * @param endTime the future date in timestamp type
     * @return remain time in string format "xxd, xxh, xxp"
     * OR "dd/mm/yyyy" when the future date smaller than now
     */
    public static String getRemainTime(long endTime) {
        Calendar calendar = Calendar.getInstance();
        long currentTime = calendar.getTimeInMillis() / 1000;
        long distance = endTime - currentTime;

        // The future date smaller then current date
        if (distance <= 0) {
            calendar.setTimeInMillis(endTime * 1000);
            return DateFormat.format("dd/MM/yyyy", calendar).toString();
        }

        long days = distance / (24 * 3600);
        long hours = (distance - days * (24 * 3600)) / 3600;
        long minutes = (distance - days * (24 * 3600) - hours * 3600) / 60;
        long seconds = (distance - days * (24 * 3600) - hours * 3600 - minutes * 60);
        StringBuilder result = new StringBuilder("");

        if (days > 0)
            result.append(days).append("d, ");

        if (days > 0 || hours > 0)
            result.append(hours).append("h, ");

        if (hours > 0 || minutes > 0)
            result.append(minutes).append("p, ");

        if (days <= 0)
            result.append(seconds).append("s");
        else
            result = result.deleteCharAt(result.length() - 2);

        return result.toString();
    }

    /**
     * Get current time in timestamp type
     *
     * @return long
     */
    public static long getCurrentDateInMillis() {
        Calendar calendar = Calendar.getInstance();
        return calendar.getTimeInMillis() / 1000;
    }

    public static String getDate(long milis) {
        Date date = new Date(milis * 1000);
        return new SimpleDateFormat("dd/MM/yyyy").format(date);
    }
}