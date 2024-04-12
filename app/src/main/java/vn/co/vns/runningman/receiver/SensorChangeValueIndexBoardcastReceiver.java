package vn.co.vns.runningman.receiver;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import vn.co.vns.runningman.service.ChangeValueIndexService;
import vn.co.vns.runningman.util.SharedPreference;

public class SensorChangeValueIndexBoardcastReceiver extends BaseBoardcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        Log.i(SensorChangeValueIndexBoardcastReceiver.class.getSimpleName(), "Change value index Service! Oooooooooooooppppssssss!!!!");
        if (SharedPreference.getInstance(context).getInt("updatedStock", 0) != 1) {
            Log.i(SensorChangeValueIndexBoardcastReceiver.class.getSimpleName(), "Stop Running! Oooooooooooooppppssssss!!!!");
            intentChangeValueIndexServices = new Intent(context, ChangeValueIndexService.class);
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                if (intentChangeValueIndexServices != null)
//                    context.startForegroundService(intentChangeValueIndexServices);
//            } else {
//                context.startService(intentChangeValueIndexServices);
//            }
        }
    }
}
