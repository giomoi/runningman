package vn.co.vns.runningman.manager;

import android.util.Log;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class SchedulerManager {

    private String TAG =SchedulerManager.class.getSimpleName();
    private static SchedulerManager instance;
    private ScheduledExecutorService scheduler;
    private ScheduledFuture<?> scheduledFuture;

    private SchedulerManager() {
        scheduler = Executors.newScheduledThreadPool(1);
    }

    public static synchronized SchedulerManager getInstance() {
        if (instance == null) {
            instance = new SchedulerManager();
        }
        return instance;
    }

    public void startScheduler(Runnable task, long initialDelay, long delay, TimeUnit unit) {
        Log.d(TAG, "startScheduler");
        if (scheduledFuture == null || scheduledFuture.isCancelled()) {
            scheduledFuture = scheduler.scheduleWithFixedDelay(task, initialDelay, delay, unit);
        }
    }

    public void stopScheduler() {
        Log.d(TAG, "stopScheduler");
        if (scheduledFuture != null && !scheduledFuture.isCancelled()) {
            scheduledFuture.cancel(true);
        }
    }
}
