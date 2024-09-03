package vn.co.vns.runningman.receiver;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import vn.co.vns.runningman.manager.SchedulerManager;
import vn.co.vns.runningman.service.StockOnlineServices;
import vn.co.vns.runningman.worker.PriceStockeOnlineWorker;

import static vn.co.vns.runningman.util.Constant.DELAY_INTERVAL;
import static vn.co.vns.runningman.util.Constant.END_HOURS;
import static vn.co.vns.runningman.util.Constant.END_MINUTES;
import static vn.co.vns.runningman.util.Constant.START_HOURS;
import static vn.co.vns.runningman.util.Constant.START_MINUTES;

public class SensorRestarterBroadcastReceiver extends BaseBoardcastReceiver {
    private Context mContext;
    private String TAG = SensorRestarterBroadcastReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive");
        this.mContext = context;
        // Kiểm tra thời gian hiện tại
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        if ((hour > START_HOURS || (hour == START_HOURS && minute >= START_MINUTES)) && (hour < END_HOURS || (hour == END_HOURS && minute <= END_MINUTES))) {
        // Chỉ khởi động công việc trong khoảng thời gian từ 09:15 đến 14:45
            Log.d(TAG, "09:15 đến 14:45");
            Runnable task = new Runnable() {
                @Override
                public void run() {
                    // Thực hiện công việc của bạn ở đây
                    Log.d(TAG, "Task is running");
                    WorkManager.getInstance(context).enqueue(new OneTimeWorkRequest.Builder(PriceStockeOnlineWorker.class).addTag(TAG).build());
                }
            };
            SchedulerManager.getInstance().startScheduler(task, 0, DELAY_INTERVAL, TimeUnit.MINUTES);
        }

    }



}
