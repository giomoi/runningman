package vn.co.vns.runningman.VolleyAPI;

import android.util.Log;

import com.android.volley.Request;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import vn.co.vns.runningman.RunningApp;
import vn.co.vns.runningman.error.ErrorView;
import vn.co.vns.runningman.error.TLog;

/**
 * Created by thanhnv on 7/28/17.
 */

public class StockAPI {
    private static StockAPI sInstance;

    public static StockAPI getInstance() {
        if (sInstance == null) sInstance = new StockAPI();

        return sInstance;
    }

    public void getDateTransition(final DataTransitionCallback callback){
        TLog.d(TickAPI.class, "======>API : " + HomeApi.getInstance().urlGetDateTransition());
        CustomStringRequest baseStringRequest = new CustomStringRequest(Request.Method.GET, HomeApi.getInstance().urlGetDateTransition(), new CustomStringRequest.IResponseStringCallback() {
            @Override
            public void onSuccess(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(new JSONObject(response).getString("data"));
                    String dayJP = jsonObject.getString("dayJP").toString();
//                    String myId = jsonObject.getString("MyUserId").toString();
//                    String otherId = jsonObject.getString("OtherUserId").toString();
                        callback.onSuccess(dayJP);
//                    callback.onSuccess();

                }
                catch (JSONException e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(ArrayList<ErrorView> errors) {
                Log.d("Error:", errors.toString());
                callback.onError();
            }
        });
        RunningApp.getInstance().addToRequestQueue(baseStringRequest);
    }

    public interface DataTransitionCallback{
        void onSuccess(String date);
        void onError();
    }

}
