package vn.co.vns.runningman.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import vn.co.vns.runningman.activity.LoginActivity;

/**
 * Created by hoangtuan on 2/28/17.
 */
public class LaunchViaDialReceiver extends BroadcastReceiver {

    private static final String LAUNCHER_NUMBER = "12345";


    @Override
    public void onReceive(Context context, Intent intent) {
        String phoneNubmer = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
        if (LAUNCHER_NUMBER.equals(phoneNubmer)) {
            setResultData(null);
            Intent appIntent = new Intent(context, LoginActivity.class);
            appIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
            context.startActivity(appIntent);
        }
    }


}
