package vn.co.vns.runningman.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import vn.co.vns.runningman.service.HorseStockService;

/**
 * Created by hoangtuan on 5/10/17.
 */
public class MyWakeUpService extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("Data", "Go here");
        Toast.makeText(context,"Go here", Toast.LENGTH_SHORT).show();
        Intent iService = new Intent(context, HorseStockService.class);
        context.startService(iService);
    }
}
