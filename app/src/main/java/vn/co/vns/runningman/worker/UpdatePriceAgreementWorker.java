package vn.co.vns.runningman.worker;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import vn.co.vns.runningman.service.ChangeValueIndexService;

import static vn.co.vns.runningman.receiver.BaseBoardcastReceiver.intentChangeValueIndexServices;

public class UpdatePriceAgreementWorker extends Worker {
    private Context mContext;
    private String TAG = UpdatePriceAgreementWorker.class.getSimpleName();

    public UpdatePriceAgreementWorker(@NonNull Context context, @NonNull WorkerParameters params) {
        super(context, params);
        this.mContext = context;
    }

    @NonNull
    @Override
    public Result doWork() {
        updateData();
//        Toast.makeText(mContext,"Updating",Toast.LENGTH_SHORT).show();
        return Result.success();
    }

    private void updateData() {
        Log.d(TAG,"OK Da cap nhat thanh cong");
        Log.i(TAG, "Update Stock Service! Oooooooooooooppppssssss!!!!");
//        if (SharedPreference.getInstance(this.mContext).getInt("updatedStock", 0) != 1) {
            Log.i(TAG, "Start Running! Oooooooooooooppppssssss!!!!");
            intentChangeValueIndexServices = new Intent(this.mContext, ChangeValueIndexService.class);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                if (intentChangeValueIndexServices != null)
                    Log.i(TAG, "Vao day !");
                    this.mContext.startForegroundService(intentChangeValueIndexServices);
            } else {
                this.mContext.startService(intentChangeValueIndexServices);
            }
//        }
    }
}
