package me.leefeng.rxjava.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by limxing on 2016/11/20.
 */

public class TimeString {
    public static String getTimefromLong(long l) {
        long current = System.currentTimeMillis();
        if ((current - l) > 1000 * 60 * 60 * 24) {
            if ((current - l) > 1000 * 60 * 60 * 48) {
                SimpleDateFormat format = new SimpleDateFormat("MM-dd");
                return format.format(new Date(l));
            } else {
                return "昨天";
            }

        } else {
            SimpleDateFormat format = new SimpleDateFormat("HH:mm");
            return format.format(new Date(l));
        }
    }
}
