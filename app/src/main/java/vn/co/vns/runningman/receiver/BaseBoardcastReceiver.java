package vn.co.vns.runningman.receiver;


import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BaseBoardcastReceiver extends BroadcastReceiver {
    public static Intent intentStockOnlneServices;
    public static Intent intentChangeValueIndexServices;

    @Override
    public void onReceive(Context context, Intent intent) {

    }

    protected boolean isMyServiceRunning(Class<?> serviceClass, Context mContext) {
        ActivityManager manager = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}
