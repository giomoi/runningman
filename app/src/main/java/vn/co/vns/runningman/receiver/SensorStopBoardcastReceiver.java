package vn.co.vns.runningman.receiver;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

public class SensorStopBoardcastReceiver extends BaseBoardcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(SensorStopBoardcastReceiver.class.getSimpleName(), "Stops Service! Oooooooooooooppppssssss!!!!");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if(intentStockOnlneServices!=null)
            context.stopService(intentStockOnlneServices);
        } else {
            context.stopService(intentStockOnlneServices);
        }
    }
}
