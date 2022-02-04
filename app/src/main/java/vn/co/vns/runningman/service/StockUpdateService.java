package vn.co.vns.runningman.service;

import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import java.io.File;
import java.util.Calendar;
import java.util.Date;

import vn.co.vns.runningman.R;
import vn.co.vns.runningman.model.MySQLiteHelper;
import vn.co.vns.runningman.util.Constant;
import vn.co.vns.runningman.util.Downloader;
import vn.co.vns.runningman.util.SharedPreference;
import vn.co.vns.runningman.util.Singleton;
import vn.co.vns.runningman.util.Utils;


public class StockUpdateService extends Service {
    private String TAG = StockUpdateService.class.getSimpleName();
    private PowerManager.WakeLock mScreenWakeLock;
    private String dirPath = "";
    private String urlAllDay = "http://images1.cafef.vn/data/20200409/CafeF.SolieuGD.Upto09042020.zip";
    private String urlDay = "http://images1.cafef.vn/data/20200409/CafeF.SolieuGD.09042020.zip";
    //    private String url3 =
    private String fileName = "CafeF.SolieuGD.Upto09042020.zip";
    //    private String fileName= "CafeF.SolieuGD.09042020.zip";
    private String ANDROID_CHANNEL_ID = "Channel_id";
    private int NOTIFICATION_ID = 1;
    private Handler handlerServices = null;
    private boolean DEVELOPER_MODE = true;
    private boolean isRunning = false;

    @Override
    public void onCreate() {
//        if (DEVELOPER_MODE) {
//            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
//                    .detectDiskReads()
//                    .detectDiskWrites()
//                    .detectNetwork()   // or .detectAll() for all detectable problems
//                    .penaltyLog()
//                    .build());
//            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
//                    .detectLeakedSqlLiteObjects()
//                    .detectLeakedClosableObjects()
//                    .penaltyLog()
//                    .penaltyDeath()
//                    .build());
//        }
        super.onCreate();
        Log.d(TAG, "onCreate");
        isRunning = true;
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        mScreenWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "foregroundservice:mywakelock");
        mScreenWakeLock.acquire();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Notification.Builder builder = new Notification.Builder(this, ANDROID_CHANNEL_ID)
                    .setContentTitle(getString(R.string.app_name))
                    .setContentText("SmartTracker Running")
                    .setAutoCancel(true);
            Notification notification = builder.build();
            startForeground(NOTIFICATION_ID, notification);
        } else {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                    .setContentTitle(getString(R.string.app_name))
                    .setContentText("SmartTracker is Running...")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setAutoCancel(true);
            Notification notification = builder.build();
            startForeground(NOTIFICATION_ID, notification);
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand");
        if (backgroundStartDownload.getState() == Thread.State.NEW) {
            backgroundStartDownload.start();
        }
        return START_STICKY;
    }

    private Thread backgroundStartDownload = new Thread(new Runnable() {
        public void run() {
            updateStockData();
        }

    });



    private void updateStockData() {
        android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);
        Calendar cal = Calendar.getInstance();
        int hourofday = cal.get(Calendar.HOUR_OF_DAY);
        String dayVN = "";
        String dayJP = "";

        String currentDateJP = Utils.getDateJP(new Date());
        MySQLiteHelper db = new MySQLiteHelper(getApplicationContext());
        Constant.maxDay = db.getMaxDayUpdate();
        dayJP = (hourofday > 14) ? Constant.nowDay : Constant.beforeDay;
        dayVN = Utils.convertDate(dayJP);
        urlDay = "http://images1.cafef.vn/data/" + dayJP + "/CafeF.SolieuGD." + dayVN + ".zip";
        urlAllDay = "http://images1.cafef.vn/data/" + dayJP + "/CafeF.SolieuGD.Upto" + dayVN + ".zip";
        Date currentDateTime = new Date();
        Date before7Day = Utils.addDays(currentDateTime, -7);
        dirPath = getRootDirPath(getApplicationContext());
        if (Constant.maxDay == null) {
            if (Utils.exists(getApplicationContext(), urlDay)) {
                fileName = urlAllDay.substring(urlAllDay.lastIndexOf("/") + 1);
                boolean isSucess =Downloader.downloadFiles(getApplicationContext(),urlAllDay,urlDay);
                if(isSucess){
                    SharedPreference.getInstance(getApplicationContext()).putInt("updatedStock", Constant.UPDATED);
                    Intent local = new Intent();
                    local.setAction("vn.co.vns.runningman");
                    this.sendBroadcast(local);
                }
            } else {
                while (!Utils.exists(getApplicationContext(), urlAllDay)) {
                    dayJP = Utils.getDateJP(Utils.addDays(Utils.convertStringToDateString(dayJP), -1));
                    dayVN = Utils.convertDate(dayJP);
                    urlAllDay = "http://images1.cafef.vn/data/" + dayJP + "/CafeF.SolieuGD.Upto" + dayVN + ".zip";
                    urlDay = "http://images1.cafef.vn/data/" + dayJP + "/CafeF.SolieuGD." + dayVN + ".zip";
                    if (Utils.exists(getApplicationContext(), urlAllDay)) {
                        fileName = urlAllDay.substring(urlAllDay.lastIndexOf("/") + 1);
                        boolean isSucess =Downloader.downloadFiles(getApplicationContext(),urlAllDay,urlDay);
                        if(isSucess){
                            SharedPreference.getInstance(getApplicationContext()).putInt("updatedStock", Constant.UPDATED);
                            Intent local = new Intent();
                            local.setAction("vn.co.vns.runningman");
                            this.sendBroadcast(local);
                        }
                        break;
                    }
                }
            }
        } else {
//            db.deleteDataTable("STOCK_BIG_VOLUME");
//            db.deleteStock("20200415");
            if (!Constant.maxDay.equalsIgnoreCase(currentDateJP)) {
                if ((Utils.convertStringToDateString(Constant.maxDay).compareTo(before7Day) < 0)) {
                    //Qua 7 ngay chua cap nhat.
//                    DownloadStockFile(urlDay, dirPath, fileName, 0);

                } else {
                    // duoi 7 ngay ke tu ngay hien tai Down load tung file theo ngay giao dich
                    String dateDownload = Constant.maxDay;
                    boolean isDownload = false;
                    while ((Utils.convertStringToDateString(dateDownload).before(Utils.convertStringToDateString(dayJP)))) {
                        dayJP = Utils.getDateJP(Utils.addDays(Utils.convertStringToDateString(dayJP), -1));
                        dayVN = Utils.convertDate(dayJP);
                        if (Utils.exists(getApplicationContext(), urlDay)) {
                            fileName = urlDay.substring(urlDay.lastIndexOf("/") + 1);
                            db.deleteDataTable("STOCK_BIG_VOLUME");
                            Singleton.getInstance().setNumberScreen(3);
//                            downloadFile(urlDay, dirPath, fileName, 1);
                            isDownload = true;
                            boolean isSucess =Downloader.downloadFile(getApplicationContext(), urlDay, 1);
                            if(isSucess){
                                SharedPreference.getInstance(getApplicationContext()).putInt("updatedStock", 2);
                                Intent local = new Intent();
                                local.setAction("vn.co.vns.runningman");
                                this.sendBroadcast(local);
                            }
                            Log.i(TAG, "Date download: " + dateDownload);
                        }
                        urlDay = "http://images1.cafef.vn/data/" + dayJP + "/CafeF.SolieuGD." + dayVN + ".zip";
                    }
                    if (!isDownload) stopSelf();
                }
            } else {
                stopSelf();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
        isRunning = false;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private String getRootDirPath(Context context) {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            File file = ContextCompat.getExternalFilesDirs(context.getApplicationContext(),
                    null)[0];
            return file.getAbsolutePath();
        } else {
            return context.getApplicationContext().getFilesDir().getAbsolutePath();
        }
    }

}
