package vn.co.vns.runningman.error;

import android.util.Log;

import vn.co.vns.runningman.util.Config;

/**
 * Created by thanhnv on 7/13/17.
 */

public class TLog {
    public static void e(Class<?> clazz, String msg) {
        if (Config.ISLOG)
            e(clazz.getSimpleName(), msg==null?"":msg);
    }

    public static void d(Class<?> clazz, String msg) {
        if (Config.ISLOG)
            d(clazz.getSimpleName(), msg==null?"":msg);
    }

    public static void e(String tag, String msg) {
        if (Config.ISLOG)
            Log.e(tag, msg==null?"":msg);
    }

    public static void d(String tag, String msg) {
        if (Config.ISLOG)
            Log.d(tag, msg==null?"":msg);
    }

    public static void e(String msg) {
        if (Config.ISLOG)
            d("MCApp", msg==null?"":msg);
    }

    public static void d(String msg) {
        if (Config.ISLOG)
            d("MCApp", msg==null?"":msg);
    }
}
