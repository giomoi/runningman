package vn.co.vns.runningman;

import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.text.TextUtils;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.Volley;

import java.net.CookieHandler;
import java.net.CookieManager;

/**
 * Created by hoangtuan on 2/20/17.
 */
public class RunningApp extends Application {
    private static String TAG = RunningApp.class.getSimpleName();
    private static Point screenSize;
    private static RunningApp sInstance;
    private String cookie="";
    private RequestQueue mRequestQueue;
    private static final ComponentName LAUNCHER_COMPONENT_NAME = new ComponentName(
            "vn.co.vns.runningman", "vn.co.vns.runningman.Launcher");
    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        CookieManager cookieManager = new CookieManager();
        CookieHandler.setDefault(cookieManager);
    }

    public static Context getAppContext() {
        return sInstance.getApplicationContext();
    }

    public static RunningApp getInstance(){
        return sInstance;
    }

    public String getCookie() {
        return cookie==null ? "" : cookie;
    }

    public void setCookie(String cookie) {
        this.cookie = cookie;
    }

    public boolean isLauncherIconVisible() {
        int enabledSetting = getPackageManager()
                .getComponentEnabledSetting(LAUNCHER_COMPONENT_NAME);
        return enabledSetting != PackageManager.COMPONENT_ENABLED_STATE_DISABLED;
    }

    public RequestQueue getRequestQueue(){
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        return mRequestQueue;
    }

    public void addToRequestQueue(Request request, String tag){
        //Set the default tag if tag is empty
        request.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(request);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setShouldCache(false);
        req.setTag(TAG);
        // Instantiate the cache
        Cache cache = new DiskBasedCache(getCacheDir(), 1024 * 1024); // 1MB cap

        // Set up the network to use HttpURLConnection as the HTTP client.
        Network network = new BasicNetwork(new HurlStack());

        // Instantiate the RequestQueue with the cache and network.
        mRequestQueue = new RequestQueue(cache, network);

        // Start the queue
        mRequestQueue.start();

        getRequestQueue().add(req);
    }

//    public void addToRequestQueue(Request request){
//        request.setTag(TAG);
//        getRequestQueue().add(request);
//    }

    public void cancelPendingRequests(Object tag){
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }



}
