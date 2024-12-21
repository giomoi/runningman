package vn.co.vns.runningman.util;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.StrictMode;
import android.preference.PreferenceGroup;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.google.gson.Gson;

import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import vn.co.vns.runningman.RunningApp;
import vn.co.vns.runningman.activity.MainActivity;
import vn.co.vns.runningman.error.ErrorView;

/**
 * Created by thanhnv on 10/21/16.
 */
public class Utils {
    private static String TAG = Utils.class.getSimpleName();

    public static ArrayList<ErrorView> parseErrorViews(String json) {
        Gson gson = new Gson();
        ArrayList<ErrorView> errorViews = new ArrayList<>();
        ErrorView[] parses = gson.fromJson(json, ErrorView[].class);
        errorViews.addAll(Arrays.asList(parses));
        return errorViews;
    }

    public static String getResourceStringByKey(String key) {
        int text_id = RunningApp.getInstance().getResources().getIdentifier(key, "string", RunningApp.getInstance().getPackageName());
        try {
            return RunningApp.getInstance().getString(text_id);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "";
    }

    final static String DATE_FORMAT_JP = "yyyy-MM-dd";
    final static String DATE_FORMAT_VN = "dd/MM/yyyy";

    public static void showDialog(Context mContext, String msg) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
        alertDialog.setTitle("Alert")
                .setMessage(msg)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
//                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        // do nothing
//                    }
//                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    /*
     * INPUT String fortmat YYYYMMDD
     * Output Date format DD/MM/YYYY
     * */
    public static String convertStringToDateVN(String date) {
        Date dateConvert = convertStringToDateString(date);
        return converDateToStringVN(dateConvert);
//        try {
//            SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
//            Date d = format.parse(date);
//            SimpleDateFormat serverFormat = new SimpleDateFormat(DATE_FORMAT_VN);
//            return serverFormat.parse(String.valueOf(d));
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
    }

    /*
     * INPUT String fortmat YYYYMMDD
     * Output String format YYYY-MM-DD
     * */
    public static String convertStringDate(String date) {
        try {
            if (!"<DTYYYYMMDD>".equalsIgnoreCase(date)) {
                SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
                Date d = format.parse(date);
                SimpleDateFormat serverFormat = new SimpleDateFormat("yyyy-MM-dd");
                return serverFormat.format(d);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /*
     * INPUT String fortmat YYYYMMDD
     * Output String format YYYY-MM-DD - 1 Year
     * */
    public static String getBeforeYear(String date) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            Date myDate = sdf.parse(date);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(myDate);
            calendar.add(Calendar.YEAR, -1);
            return (sdf.format(calendar.getTime()));
            //return serverFormat.format(d);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /*
     * INPUT String fortmat YYYYMMDD
     * Output String format DDMMYYYY
     * */
    public static String convertDate(String rootDate) {
        String year = rootDate.substring(0, 4);
        String month = rootDate.substring(4, 6);
        String day = rootDate.substring(6, 8);

        return day + month + year;
    }

    /*
     * INPUT String fortmat YYYYMMDD
     * Output String format DD.MM.YYYY
     * */
    public static String convertPointDate(String rootDate) {
        String year = rootDate.substring(0, 4);
        String month = rootDate.substring(4, 6);
        String day = rootDate.substring(6, 8);

        return day + "." + month + "." + year;
    }

    /*
     * INPUT String fortmat DDMMYYYY
     * Output String format DD.MM.YYYY
     * */
    public static String convertPointDateVN(String rootDate) {
        String year = rootDate.substring(4, 8);
        String month = rootDate.substring(2, 4);
        String day = rootDate.substring(0, 2);
        return day + "." + month + "." + year;
    }

    /*
     * INPUT Date fortmat YYYYMMDD, int months
     * Output Date Mon Nov 07 15:35:55 GMT+07:00 2016
     * */
    public static Date addMonths(Date date, int months) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MONTH, months); //minus number would decrement the days
        return cal.getTime();
    }

    /*
     * INPUT Date Mon Nov 07 15:35:55 GMT+07:00 2016, , int day
     * Output String fortmat YYYYMMDD
     * */
    public static String getDateJP(Date dateJP) {
        Format formatter = new SimpleDateFormat("yyyyMMdd");
        String s = formatter.format(dateJP);
        return s;
    }

    /*
     * INPUT Date fortmat YYYYMMDD, int months
     * Output Date Mon Nov 07 15:35:55 GMT+07:00 2016
     * */
    public static Date addDays(Date date, int day) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, day); //minus number would decrement the days
        return cal.getTime();
    }

    /*
     * INPUT Date Mon Nov 07 15:35:55 GMT+07:00 2016
     * Output String YYYY-MM-DD
     * */
    public static String converDateToString(Date date) {
        Format formatter = new SimpleDateFormat("yyyy-MM-dd");
        String s = formatter.format(date);
        return s;
    }

    /*
     * INPUT Date Mon Nov 07 15:35:55 GMT+07:00 2016
     * Output String DD/MM/YYYY
     * */
    public static String converDateToStringVN(Date date) {
        Format formatter = new SimpleDateFormat(DATE_FORMAT_VN);
        String s = formatter.format(date);
        return s;
    }

    /*
     * INPUT String fortmat YYYYMMDD
     * Output Date Mon Nov 07 15:35:55 GMT+07:00 2016
     * */
    public static Date convertStringToDateString(String date) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
            return format.parse(date);
        } catch (ParseException e) {
            return null;
        }
    }

    public static boolean isDateValid(String date) {
        try {
            DateFormat df = new SimpleDateFormat(DATE_FORMAT_JP);
            df.setLenient(false);
            df.parse(date);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }


    public static boolean exists(Context mContext, String url) {
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy =
                    new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        try {
            HttpURLConnection huc = (HttpURLConnection) new URL(url).openConnection();
            huc.setRequestMethod("GET");  //OR  huc.setRequestMethod ("HEAD");
            huc.setRequestProperty("Connection", "close");
            huc.connect();
            int code = huc.getResponseCode();
            System.out.println(code);
            System.out.println(url);
            huc.disconnect();
            return (code == 200) ? true : false;
        } catch (Exception e) {
//            Toast.makeText(mContext,"Disconnect internet",Toast.LENGTH_LONG).show();
//            Log.d(TAG,e.getMessage());
            e.printStackTrace();
            return false;
//        } finally {
//            huc.disconnect();
        }
    }

    public static String getUrlExits(Context mContext) {
        String dayJP = Constant.nowDay;
        String dayVN = convertDate(dayJP);
        String urlDay = "http://images1.cafef.vn/data/" + dayJP + "/CafeF.SolieuGD." + dayVN + ".zip";
        int i = 0;
        do {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DATE, -i);
            ++i;
            SimpleDateFormat formatDateJP = new SimpleDateFormat("yyyyMMdd");
            String dateJP = formatDateJP.format(calendar.getTime());
            String dateVN = convertDate(dateJP);
            Log.i("MyApp: ", "90 days ago:" + formatDateJP.format(calendar.getTime()));
            urlDay = "http://images1.cafef.vn/data/" + dateJP + "/CafeF.SolieuGD." + dateVN + ".zip";
            Constant.dateTransition = dateJP;
        } while (!exists(mContext, urlDay));

        return urlDay;
    }

    public static boolean isNetworkAvailable(final Context context) {
        final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }

    public static String roundFourDecimals(String d) {
        Double tmp = Double.parseDouble(d);
        DecimalFormat twoDForm = new DecimalFormat("#,###.#");
        return String.valueOf(twoDForm.format(tmp));
    }


    public static void pushNotification(Context mContext, String strContent) {

        Intent intent = new Intent(mContext, MainActivity.class);
        String CHANNEL_ID = "MYCHANNEL";
        NotificationChannel notificationChannel = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationChannel = new NotificationChannel(CHANNEL_ID, "name", NotificationManager.IMPORTANCE_HIGH);
        }
        PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 1, intent, 0);
        Notification notification = null;
        String notificationContent = "Example1\nExample2\nExample3";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notification = new NotificationCompat.Builder(mContext, CHANNEL_ID)
                    .setContentText(strContent)
                    .setStyle(new NotificationCompat.BigTextStyle()
                            .bigText(strContent))
                    .setContentTitle("Notice")
                    .setContentIntent(pendingIntent)
                    .setChannelId(CHANNEL_ID)
                    .setSmallIcon(android.R.drawable.sym_action_chat)
                    .setAutoCancel(true)
                    .build();
        }
        notification.defaults |= Notification.DEFAULT_SOUND;
        NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(notificationChannel);
        }
        notificationManager.notify((int) System.currentTimeMillis(), notification);
    }
}
