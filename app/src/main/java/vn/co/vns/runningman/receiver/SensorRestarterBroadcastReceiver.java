package vn.co.vns.runningman.receiver;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import vn.co.vns.runningman.service.StockOnlineServices;

public class SensorRestarterBroadcastReceiver extends BaseBoardcastReceiver {
    private Context mContext;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.mContext = context;
        Log.i(SensorRestarterBroadcastReceiver.class.getSimpleName(), "Start Service ! Oooooooooooooppppssssss!!!!");
        if (isMyServiceRunning(StockOnlineServices.class,context))
            context.stopService(intentStockOnlneServices);
        intentStockOnlneServices = new Intent(context, StockOnlineServices.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (intentStockOnlneServices != null)
                context.startForegroundService(intentStockOnlneServices);
        } else {
            context.startService(intentStockOnlneServices);
        }

    }



}
