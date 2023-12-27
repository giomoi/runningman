package vn.co.vns.runningman.service;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

import vn.co.vns.runningman.R;
import vn.co.vns.runningman.object.InforBuySellStockIndex;
import vn.co.vns.runningman.object.InforStockIndex;
import vn.co.vns.runningman.object.InforVolumeValueStockIndex;
import vn.co.vns.runningman.util.SharedPreference;
import vn.co.vns.runningman.util.Singleton;
import vn.co.vns.runningman.util.Utils;

public class ChangeValueIndexService extends Service {
    private String TAG=ChangeValueIndexService.class.getSimpleName();
    private ArrayList<InforStockIndex> listInforStockIndex = new ArrayList<>();
    private static final String ANDROID_CHANNEL_ID = "com.xxxx.Location.Channel";
    private static final int NOTIFICATION_ID = 555;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG,"onCreate");
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
        Log.d(TAG,"onStartCommand");
        if (resetThread.getState() == Thread.State.NEW)
        {
            resetThread.start();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG,"onDestroy");
        if(resetThread!=null) resetThread.interrupt();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    private Thread resetThread = new Thread(new Runnable() {
        @Override
        public void run() {
            // Volume, value current
            Document doc1 = null;
            long averVolume10 = 0;
            long averVolume20 = 0;
            long averValue10 = 0;
            long averValue20 = 0;
            final InforVolumeValueStockIndex objInforVolumeValueStockIndex = new InforVolumeValueStockIndex();
            try {
                doc1 = Jsoup.connect("https://s.cafef.vn/lich-su-giao-dich-symbol-vnindex/trang-1-0-tab-1.chn").get();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Elements trTable1 = doc1.select("table[id=GirdTable2]");
            int countRow = 0;
            long volumeAver10 = 0;
            long volumeAver20 = 0;
            long valueAver10 = 0;
            long valueAver20 = 0;
            if (trTable1.size() > 0) {
                for (Element s : trTable1) {
                    Elements rowTable = s.getElementsByTag("tr");
                    for (Element tr : rowTable) {
                        if (tr.toString().contains("id=\"ctl00_ContentPlaceHolder1_ctl03_rptData2_ctl01_itemTR\"")) {
                            Elements colTable = tr.getElementsByTag("td");

                            for (int i = 0; i < colTable.size(); i++) {
                                Element item = colTable.get(i);
                                Log.d("Value:", item.select("td").toString());
                                objInforVolumeValueStockIndex.setValue(item.select("td").text(), i);
                            }
                            Log.d("VolumeValueStockIndex: ", colTable.toString());
                        } else {
                            Elements colTable = tr.getElementsByTag("td");
                            for (int i = 0; i < colTable.size(); i++) {
                                Element item = colTable.get(i);
                                Log.d("Value:", item.select("td").toString());
                                if (i == 4 && colTable.size() > 12) {
                                    countRow++;
                                    //caculate rate for volume
                                    if (countRow == 1) {
                                        Log.d("Value:", item.select("td").text() + " : " + objInforVolumeValueStockIndex.getVolumeClose());
                                        Integer prevVolume = Integer.parseInt(item.select("td").html().toString().replaceAll("&nbsp;", "").replaceAll(",", "").trim());
                                        Integer currentVolume = Integer.parseInt(objInforVolumeValueStockIndex.getVolumeClose().replaceFirst("\\s++$", "").replaceAll(",", ""));
                                        double rateVolume = (currentVolume - prevVolume) * 100.0 / currentVolume;
                                        objInforVolumeValueStockIndex.setVolumeRate(String.format("%2.02f", rateVolume) + "%");
                                    }
                                    if (countRow <= 10) {
                                        volumeAver10 = volumeAver10 + Integer.parseInt(item.select("td").html().toString().replaceAll("&nbsp;", "").replaceAll(",", "").trim());
                                        if (countRow == 10) {
                                            averVolume10 = volumeAver10 / 10;
                                            objInforVolumeValueStockIndex.setAverVolume10(averVolume10);
                                        }
                                    }
                                    if (countRow <= 20) {
                                        volumeAver20 = volumeAver20 + Integer.parseInt(item.select("td").html().toString().replaceAll("&nbsp;", "").replaceAll(",", "").trim());
                                        if (countRow == 19) {
                                            averVolume20 = volumeAver20 / 19;
                                            objInforVolumeValueStockIndex.setAverAverVolume20(averVolume20);
                                        }
                                    }
                                }
                                //Value
                                if (i == 5 && colTable.size() > 12) {
                                    if (countRow == 1) {
                                        Log.d("Value:", item.select("td").text() + " : " + objInforVolumeValueStockIndex.getVolumeClose());
                                        Long prevValue = Long.parseLong(item.select("td").html().toString().replaceAll("&nbsp;", "").replaceAll(",", "").trim());
                                        Long currentValue = Long.valueOf(objInforVolumeValueStockIndex.getValueClose().replaceFirst("\\s++$", "").replaceAll(",", ""));
                                        double rateValue = (currentValue - prevValue) * 100.0 / currentValue;
                                        objInforVolumeValueStockIndex.setValueRate(String.format("%2.02f", rateValue) + "%");
                                    }
                                    if (countRow <= 10) {
                                        valueAver10 = valueAver10 + Long.parseLong(item.select("td").html().toString().replaceAll("&nbsp;", "").replaceAll(",", "").trim());
                                        if (countRow == 10) {
                                            averValue10 = valueAver10 / 10;
                                            objInforVolumeValueStockIndex.setAverValue10(averValue10);
                                        }
                                    }
                                    if (countRow <= 20) {
                                        valueAver20 = valueAver20 + Long.parseLong(item.select("td").html().toString().replaceAll("&nbsp;", "").replaceAll(",", "").trim());
                                        if (countRow == 19)
                                            averValue20 = valueAver20 / 19;
                                        objInforVolumeValueStockIndex.setAverValue20(averValue20);
                                    }
                                }
                            }
                        }
                        Log.d("Volume 20: ", String.valueOf(volumeAver10) + " : " + String.valueOf(volumeAver20));
                    }

                }
            }
            Singleton.getInstance().setObjInforVolumeValueStockIndex(objInforVolumeValueStockIndex);
            final Long finalAverValue1 = averValue10;
            objInforVolumeValueStockIndex.setAverVolume10(averVolume10);
            objInforVolumeValueStockIndex.setAverValue10(averValue10);
            objInforVolumeValueStockIndex.setAverValue20(averValue20);
            objInforVolumeValueStockIndex.setAverAverVolume20(averVolume20);

            String number = finalAverValue1.toString().substring(0, finalAverValue1.toString().length() - 9);
            double amount = Double.parseDouble(number);
            String valuerAver = String.format("%,.0f", amount);

            String content= objInforVolumeValueStockIndex.getDateTransit() +": "
                    +objInforVolumeValueStockIndex.getValueClose().substring(0, objInforVolumeValueStockIndex.getValueClose().length() - 13)
                    + " - TB10: "+ valuerAver.replace(".", ",")
                    + " :Rate: " + objInforVolumeValueStockIndex.getValueRate();
            Utils.pushNotification(getApplicationContext(),content);
            stopSelf();
        }
    });

    private InforStockIndex creatStockIndexObject(Elements td) {
        InforStockIndex objInforStockIndex = new InforStockIndex();
        for (int i = 0; i < td.size(); i++) {
            Element item = td.get(i);
            Log.d("Value:", item.select("td").toString());
            objInforStockIndex.setValue(item.select("td").text(), i);
        }
        return objInforStockIndex;
    }
}
