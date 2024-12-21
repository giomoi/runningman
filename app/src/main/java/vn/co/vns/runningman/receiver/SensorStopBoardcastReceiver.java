package vn.co.vns.runningman.receiver;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import vn.co.vns.runningman.manager.SchedulerManager;
import vn.co.vns.runningman.worker.PriceStockeOnlineWorker;

public class SensorStopBoardcastReceiver extends BaseBoardcastReceiver {
    private String TAG = SensorStopBoardcastReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive " + PriceStockeOnlineWorker.intentStockOnlineServices);
        SchedulerManager.getInstance().stopScheduler();

        Log.i(SensorStopBoardcastReceiver.class.getSimpleName(), "Stops Service! Oooooooooooooppppssssss!!!!");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if(PriceStockeOnlineWorker.intentStockOnlineServices != null)
            context.stopService(PriceStockeOnlineWorker.intentStockOnlineServices);
        } else {
            context.stopService(PriceStockeOnlineWorker.intentStockOnlineServices);
        }
    }
}
