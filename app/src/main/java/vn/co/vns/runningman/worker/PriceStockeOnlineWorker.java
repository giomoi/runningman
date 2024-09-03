package vn.co.vns.runningman.worker;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import vn.co.vns.runningman.R;
import vn.co.vns.runningman.activity.MainActivity;
import vn.co.vns.runningman.service.ChangeValueIndexService;
import vn.co.vns.runningman.service.StockOnlineServices;

public class PriceStockeOnlineWorker extends Worker {
    private String TAG = PriceStockeOnlineWorker.class.getSimpleName();
    private static final String CHANNEL_ID = "example_channel_id";
    private Context mContext;

    public PriceStockeOnlineWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        Log.d(TAG, "PriceStockeOnlineWorker");
        this.mContext = context;
    }

    @NonNull
    @Override
    public Result doWork() {

        Log.i(TAG, "Start Running! Oooooooooooooppppssssss!!!!");
        Intent intentStockOnlineServices = new Intent(this.mContext, StockOnlineServices.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (intentStockOnlineServices != null)
            this.mContext.startForegroundService(intentStockOnlineServices);
        } else {
            this.mContext.startService(intentStockOnlineServices);
        }
//        }
//        updateData();
        Log.d(TAG, "doWork");
        return Result.success();
    }

    private void updateData() {
        String data = "Nội dung từ web"; // Giả lập dữ liệu từ web
        sendNotification(data);
//        Tạo intent để mở MainActivity khi người dùng nhấn vào thông báo
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);

        // Tạo thông báo
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID);
        builder.setSmallIcon(R.drawable.chat_blank_icon);
        builder.setContentTitle("Thông báo đơn giản");
        builder.setContentText("Đây là nội dung của thông báo.");
        builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
        builder.setContentIntent(pendingIntent);
        builder.setAutoCancel(true);

        // Hiển thị thông báo
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());
        notificationManager.notify(1, builder.build());
    }

    private void sendNotification(String value) {
        Log.d(TAG, "sendNotification: " + value);
        // Tạo kênh thông báo cho Android 8.0 trở lên
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Example Channel";
            String description = "This is an example notification channel";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            // Đăng ký kênh với hệ thống
            NotificationManager notificationManager = this.mContext.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
