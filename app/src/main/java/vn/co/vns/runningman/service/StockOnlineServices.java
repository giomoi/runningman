package vn.co.vns.runningman.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.PowerManager;
import android.util.Log;
import android.view.Gravity;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import vn.co.vns.runningman.R;
import vn.co.vns.runningman.activity.MainActivity;
import vn.co.vns.runningman.object.StockIndex;
import vn.co.vns.runningman.object.StockObject;
import vn.co.vns.runningman.util.Constant;
import vn.co.vns.runningman.util.SharedPreference;

public class StockOnlineServices extends Service {
    private String fullHtml = "";
    private WebView wv = null;
    private ArrayList<String> listStock = new ArrayList<>();
    private PowerManager.WakeLock mScreenWakeLock;
    private int transitUrl = 3;
    private boolean isRunningView = false;
    private WindowManager windowManager;
    private String TAG = StockOnlineServices.class.getSimpleName();
    private Handler mServiceHandler;
    private int MAX_SECONDES = 1 * 60 * 1000; // 10 minutes
    private int TIME_LOAD_DELAY = 10 * 1000; //secons
    private Handler mHandlerWebView;
    private boolean isClear = false;
    private String urlString = "";

    //    private WindowManager.LayoutParams params;
    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate");
        mServiceHandler = new Handler();
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        mScreenWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "foregroundservice:mywakelock");
        mScreenWakeLock.acquire();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startMyOwnForeground();
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

    private void startMyOwnForeground(){
        String NOTIFICATION_CHANNEL_ID = "com.example.simpleapp";
        String channelName = "My Background Service";
        NotificationChannel chan = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_NONE);
            chan.setLightColor(Color.BLUE);
            chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            assert manager != null;
            manager.createNotificationChannel(chan);
        }
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        Notification notification = notificationBuilder.setOngoing(true)
//                .setSmallIcon(R.drawable.icon_1)
                .setContentTitle("App is running in background")
                .setPriority(NotificationManager.IMPORTANCE_MIN)
                .setCategory(Notification.CATEGORY_SERVICE)
                .build();
        startForeground(NOTIFICATION_ID, notification);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand");
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE", Locale.UK);
        Date d = new Date();
        String dayOfTheWeek = sdf.format(d);
        if ("Saturday".equalsIgnoreCase(dayOfTheWeek) || "Sunday".equalsIgnoreCase(dayOfTheWeek)) {
            stopSelf();
        } else {
            isClear = false;
            if (SharedPreference.getInstance(getApplicationContext()).getBoolean("notification", true))
                repeatPushNotification();
        }
        return START_STICKY;
    }

    private void repeatPushNotification() {
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
                android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
                Build.VERSION.SDK_INT < Build.VERSION_CODES.O ?
                        WindowManager.LayoutParams.TYPE_PHONE :
                        WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                        | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                PixelFormat.TRANSLUCENT
        );

        params.gravity = Gravity.TOP | Gravity.START;
        params.x = 0;
        params.y = 0;
        params.width = 10;
        params.height = 10;

        wv = new WebView(this);
        wv.getSettings().setJavaScriptEnabled(true);
        wv.setWebChromeClient(new WebChromeClient());
        wv.getSettings().setDomStorageEnabled(true);
        wv.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                Log.d(TAG, "onPageFinished");
                mHandlerWebView = new Handler();
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        Log.d(TAG, "Toi day");
                        wv.loadUrl("javascript:window.HTMLOUT.processHTML('<head>'+document.getElementsByTagName('html')[0].innerHTML+'</head>');");
                    }
                };
                if (!isClear) {
                    mHandlerWebView.postDelayed(runnable, TIME_LOAD_DELAY);
                } else {
                    mHandlerWebView.removeCallbacks(runnable);
                }
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                Log.d("Error", "loading web view: request: " + request + " error: " + error);
            }

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
                if (request.getUrl().toString().contains("/endProcess")) {
                    windowManager.removeView(wv);
                    wv.post(new Runnable() {
                        @Override
                        public void run() {
                            wv.destroy();
                        }
                    });
                    stopSelf();
                    return new WebResourceResponse("bgsType", "someEncoding", null);
                } else {
                    return null;
                }
            }
        });

        listStock = new ArrayList<>();
        if ((transitUrl % 2) == 0) {
            Log.d(TAG, "chan" + transitUrl);
            wv.loadUrl(Constant.URL_DEFAULT_HSX);
            urlString = Constant.URL_DEFAULT_HSX;
        } else {
            Log.d(TAG, "le" + transitUrl);
            wv.loadUrl(Constant.URL_DEFAULT_HNX);
            urlString = Constant.URL_DEFAULT_HNX;
        }
        transitUrl = transitUrl + 1;
        wv.addJavascriptInterface(new MyJavaScriptInterface(), "HTMLOUT");
        windowManager.getDefaultDisplay();
        windowManager.addView(wv, params);

        mServiceHandler.postDelayed(runnableServices, MAX_SECONDES);
    }

    private static final String ANDROID_CHANNEL_ID = "com.xxxx.Location.Channel";
    private static final int NOTIFICATION_ID = 555;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private Runnable runnableServices = new Runnable() {
        @Override
        public void run() {
            repeatPushNotification();
        }
    };

    private Thread resetThread2 = new Thread() {
        public void run() {
            Looper.prepare();//Call looper.prepare()
            try {
//                while (!isRunningView) {
                Thread.sleep(30000);
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        Log.i(TAG, "Refreshing..." + urlString);
                        // this will run in the main thread
                        wv.getSettings().setJavaScriptEnabled(true);
                        wv.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
                        wv.getSettings().setDomStorageEnabled(true);
                        wv.setWebViewClient(new WebViewClient());
                        wv.loadUrl("javascript:window.HTMLOUT.processHTML('<head>'+document.getElementsByTagName('html')[0].innerHTML+'</head>');");
//                            pushNotification("OK");
                    }
                });
//                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                Thread.currentThread().interrupt();
            }
            Looper.loop();
        }
    };

    /**
     * Get raw html from webview
     */
    private class MyJavaScriptInterface {
        @JavascriptInterface
        @SuppressWarnings("unused")
        public void processHTML(final String html) {
            // process the html as needed by the app
//            if(html.length()>0 && html.contains("extraBuyVol") && !isRunningView) {
            if (html.length() > 0) {
                //Log.i("HTML Length", html);
                fullHtml = html;
                //isLoadComplete = true;
//                if (!isBindData) {
////                    new RetrieveFeedTask().execute();
                executeHtml();
//                }
            }
        }
    }

    private void executeHtml() {
        final ArrayList<StockObject> result = new ArrayList<>();
        final ArrayList<StockIndex> resultIndex = new ArrayList<>();
        try {
            Document doc = Jsoup.parse(fullHtml);
            if (SharedPreference.getInstance(getApplicationContext()).getString("priceTable", "cafef").equalsIgnoreCase("cafef")) {
                Elements trTable = doc.select("table#stock-price-table");
                Elements tbody = doc.select("tbody#stock-price-table-body");
                if (trTable.size() > 0) {
                    for (Element s : tbody) {
                        Elements rowTable = s.getElementsByTag("tr");
                        for (Element tr : rowTable) {
                            if (tr.toString().contains("price-item stock")) {
                                Elements colTable = tr.getElementsByTag("td");
                                StockObject newObject = new StockObject();
                                if(urlString.contains("HNX")){
                                    newObject = creatStockObjectHNXOption1(colTable);
                                } else {
                                    newObject = creatStockObjectHSXOption1(colTable);
                                }
                                Log.d(TAG, newObject.getCodeStock());
                                if (!"".equalsIgnoreCase(newObject.getCodeStock())) {
                                    showNotificationCode(newObject);
                                    //Filter only stock 3 character
                                    if (newObject.getCodeStock().length() <= 4)
                                        result.add(newObject);
                                }
                            }
                        }
                    }
                    Log.d(TAG, listStock.toString() + " ABC");
                    if (listStock.size() > 0) {
                        String strSpecial = "";
                        for (int i = 0; i < listStock.size(); i++) {
                            strSpecial = strSpecial.equalsIgnoreCase("") ? listStock.get(i) : strSpecial + "," + listStock.get(i);
                        }
                        pushNotification(strSpecial);
//                    } else {
//                        pushNotification("Ok");
                    }
                }

            } else {
                Elements trTable = doc.select("table#table-table-scroll");
                if (trTable.size() > 0) {
                    for (Element s : trTable) {
                        Elements rowTable = s.getElementsByTag("tr");
//                        for (Element tr : rowTable) {
//                            if (tr.toString().contains("class=\"invisible\"")) {
//                                Elements colTable = tr.getElementsByTag("td");
//                                StockObject newObject = creatStockObject(colTable);
//                                if (!"".equalsIgnoreCase(newObject.getTopPrice())) {
//                                    showNotificationCode(newObject);
//                                    result.add(newObject);
//                                }
//                            }
//                        }
//                        if (listStockTransition.size() == 0) {
//                            listStockTransition = result;
//                        }
                    }
                }

            }
            Log.d("Index: ", resultIndex.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private StockObject creatStockObjectHNXOption1(Elements td){
        StockObject object = new StockObject();
        for (int i = 0; i < td.size(); i++) {
            Element item = td.get(i);
            object.setValueHNX1(i == 0 ? item.select("span.tool-tip").text() : item.select("td").text(), i);
        }
        return object;
    }

    private StockObject creatStockObjectHSXOption1(Elements td) {
        StockObject object = new StockObject();
        for (int i = 0; i < td.size(); i++) {
            Element item = td.get(i);
            object.setValueHSX1(i == 0 ? item.select("label").text() : item.select("td").text(), i);
        }
        return object;
    }

    /**
     * Hiển thị notification cho các mã đạt được điều kiện.
     *
     * @param newObject
     */
    public void showNotificationCode(final StockObject newObject) {
        Log.d(TAG, newObject.toString());
        if (newObject.getBuyingPrice1().isEmpty() || newObject.getBuyingPrice2().isEmpty() || newObject.getBuyingPrice3().isEmpty() || newObject.getTCPrice().isEmpty()) {
            return;
        }
        if (newObject.getBuyingPrice1().equals("ATO") || newObject.getBuyingPrice1().equals("ATC") ||
                newObject.getBuyingPrice2().equals("ATO") || newObject.getBuyingPrice2().equals("ATC") ||
                newObject.getBuyingPrice3().equals("ATO") || newObject.getBuyingPrice3().equals("ATC")) {
            return;
        }
        if (newObject.getTotalWeight().isEmpty() || newObject.getBuyingWeight1().isEmpty() || newObject.getBuyingWeight2().isEmpty() || newObject.getBuyingWeight3().isEmpty()) {
            return;
        }
        double checkedPrice = Constant.valueToCompare * Double.valueOf(newObject.getTopPrice().replaceAll(",", "."));
        double buying1 = Double.valueOf(newObject.getBuyingPrice1().replaceAll(",", "."));
        double buying2 = Double.valueOf(newObject.getBuyingPrice2().replaceAll(",", "."));
        double buying3 = Double.valueOf(newObject.getBuyingPrice3().replaceAll(",", "."));
        double tcPrice = Double.valueOf(newObject.getTCPrice().replaceAll(",", "."));
        double cePrice = Double.valueOf(newObject.getTopPrice().replaceAll(",", "."));
        double buyPrice1 = Double.valueOf(newObject.getBuyingPrice1().replaceAll(",", "."));

//        double amountTotal = Constant.valueToCompare*Double.valueOf(newObject.getTotalWeight().replaceAll("\\.", "").replaceAll(",", ""));
        double amountTotal = Double.valueOf(newObject.getTotalWeight().replaceAll("\\.", "").replaceAll(",", ""));
        double amountSum = Double.valueOf(newObject.getBuyingWeight1().replaceAll("\\.", "").replaceAll(",", ""))
                + Double.valueOf(newObject.getBuyingWeight2().replaceAll("\\.", "").replaceAll(",", ""))
                + Double.valueOf(newObject.getBuyingWeight3().replaceAll("\\.", "").replaceAll(",", ""));
//        double amountSum=Double.valueOf(newObject.getBuyingWeight1().replaceAll("\\.", "").replaceAll(",", ""));
//        if((newObject.getBuyingPrice1().equals(newObject.getTopPrice())) && (buying1>checkedPrice && buying2>checkedPrice && buying3>checkedPrice) && (buying1 > tcPrice && buying2 > tcPrice && buying3 > tcPrice)
        //Log.d(newObject.getCodeStock(),newObject.getBuyingPrice1()+":"+newObject.getTopPrice()+":"+buying1+":"+buying2+":"+buying3+":"+tcPrice);
        if ((newObject.getBuyingPrice1().equals(newObject.getTopPrice())) && (buying1 > tcPrice && buying2 > tcPrice && buying3 > tcPrice)
                && amountSum >= amountTotal && cePrice == buyPrice1) {
            if (!listStock.contains(newObject.getCodeStock())) {
                listStock.add(newObject.getCodeStock());
                Log.d(TAG, listStock.toString());
            }
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy!");
        if (windowManager != null) windowManager.removeView(wv);
//        isClear = true;
        if (mHandlerWebView != null) mHandlerWebView.removeCallbacks(null);
        if (mServiceHandler != null) mServiceHandler.removeCallbacks(runnableServices);
//        Intent broadcastIntent = new Intent(this, SensorRestarterBroadcastReceiver.class);
//        sendBroadcast(BaseBoardcastReceiver.intentStockOnlneServices);
    }

    public void pushNotification(String strContent) {

        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        String CHANNEL_ID = "MYCHANNEL";
        NotificationChannel notificationChannel = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationChannel = new NotificationChannel(CHANNEL_ID, "name", NotificationManager.IMPORTANCE_HIGH);
        }
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 1, intent, 0);
        Notification notification = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notification = new Notification.Builder(getApplicationContext(), CHANNEL_ID)
                    .setContentText(strContent)
                    .setContentTitle("Notice")
                    .setContentIntent(pendingIntent)
                    .setChannelId(CHANNEL_ID)
                    .setSmallIcon(android.R.drawable.sym_action_chat)
                    .setAutoCancel(true)
                    .build();
        }
        notification.defaults |= Notification.DEFAULT_SOUND;
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(notificationChannel);
        }
        notificationManager.notify((int) System.currentTimeMillis(), notification);
    }

}
