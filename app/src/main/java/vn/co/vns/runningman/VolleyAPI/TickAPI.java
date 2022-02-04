package vn.co.vns.runningman.VolleyAPI;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import vn.co.vns.runningman.RunningApp;
import vn.co.vns.runningman.error.ErrorView;
import vn.co.vns.runningman.error.TLog;
import vn.co.vns.runningman.interfaces.IGetTickCallback;
import vn.co.vns.runningman.interfaces.SimpeBaseCallback;
import vn.co.vns.runningman.object.Tick;

/**
 * Created by thanhnv on 7/13/17.
 */

public class TickAPI {

    private static TickAPI sInstance;

//    public TickAPI() {
//        super(HomeApi.getInstance(), UserView.class, UserView[].class);
//    }


    public static TickAPI getInstance() {
        if (sInstance == null) sInstance = new TickAPI();

        return sInstance;
    }

    public void addTickTicker(final Tick tick,final SimpeBaseCallback callback) {
        TLog.d(TickAPI.class, "======>API : " +  HomeApi.getInstance().urlAddTick());
        CustomStringRequest baseStringRequest = new CustomStringRequest(Request.Method.POST,
                HomeApi.getInstance().urlAddTick(), new CustomStringRequest.IResponseStringCallback() {
            @Override
            public void onSuccess(String response) {
//                try {
                    callback.onSuccess();
//                    JSONObject objResult= new JSONObject(response);
//                    String result=objResult.getString("status");
//                    if(ErrorCode.STATUS_SUCCESS.equalsIgnoreCase(result)){
//                        callback.onSuccess();
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//                Gson gson = new Gson();
//                try {
//                    JSONObject jsonObject = new JSONObject(new JSONObject(response).getString("data"));
//                    TLog.d(TickAPI.class,jsonObject.getString("UserList").toString());
//                    ArrayList<UserView> userViews = new Gson().fromJson(jsonObject.getString("UserList").toString(), new TypeToken<List<UserView>>(){}.getType());
//                    String totalUser = jsonObject.getString("TotalUser").toString();
//
//                    try {
//                        callback.onSuccess(userViews, totalUser);
//                        userViews.clear();
//                    } catch (Exception ex) {
//                        ex.printStackTrace();
//                        StringWriter trace = new StringWriter();
//                        ex.printStackTrace(new PrintWriter(trace));
//                        callback.onSuccess(null, null);
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                    callback.onSuccess(null, null);
//                }
//                callback.onSuccess();
            }

            @Override
            public void onError(ArrayList<ErrorView> errors) {
                TLog.d(TickAPI.class, "======>Error: " + errors);
                callback.onError(errors.toString());
            }
        }){

            @Override
            public HashMap<String,String> getParams(){
                HashMap<String,String> params = new HashMap<>();
                params.put("Ticker",tick.getTicker());
                params.put("PriceCurrent",tick.getPriceCurrent().toString());
                params.put("PriceTarget",tick.getPriceTarget().toString());
                params.put("OptionTarget",tick.getOptionTarget().toString());
                params.put("Content",tick.getContent());
                params.put("Owe",tick.getOwe());
                params.put("EPSYear",tick.getEPSYear());
                params.put("EPSQuaterYear",tick.getEPSQuaterYear());
                params.put("BookValue",tick.getBookValue());
                params.put("Organization",tick.getOrganization());
                params.put("Room",tick.getRoom());
                TLog.d(TickAPI.class, "======>Params: " + params);
                return params;
            }

        };
        RunningApp.getInstance().addToRequestQueue(baseStringRequest);
    }

    public void updateTickTicker(final Tick tick, final CustomStringRequest.IResponseStringCallback callback){
        TLog.d(TickAPI.class, "leaveApp ======>API : " +  HomeApi.getInstance().urlUpdateTick(tick.getTicker()));

        CustomStringRequest baseStringRequest = new CustomStringRequest(Request.Method.PUT,
                HomeApi.getInstance().urlUpdateTick(tick.getTicker()), new CustomStringRequest.IResponseStringCallback() {
            @Override
            public void onSuccess(String response) {
                TLog.d(TickAPI.class, "leaveApp response : " +  response);

                callback.onSuccess(response);
            }

            @Override
            public void onError(ArrayList<ErrorView> errors) {
                TLog.d(TickAPI.class, "leaveApp errors : " +  errors);
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("PriceCurrent",tick.getPriceCurrent().toString());
                params.put("PriceTarget",tick.getPriceTarget().toString());
                params.put("OptionTarget",tick.getOptionTarget().toString());
                params.put("Content",tick.getContent());
                params.put("Owe",tick.getOwe());
                params.put("EPSYear",tick.getEPSYear());
                params.put("EPSQuaterYear",tick.getEPSQuaterYear());
                params.put("BookValue",tick.getBookValue());
                params.put("Organization",tick.getOrganization());
                params.put("Room",tick.getRoom());
                return params;
            }
        };
        RunningApp.getInstance().addToRequestQueue(baseStringRequest);
    }

    public void deleteTickTicker(final String ticker, final SimpeBaseCallback callback){
        TLog.d(TickAPI.class, "leaveApp ======>API : " +  HomeApi.getInstance().urlDeleteTick(ticker));

        CustomStringRequest baseStringRequest = new CustomStringRequest(Request.Method.GET,
                HomeApi.getInstance().urlDeleteTick(ticker), new CustomStringRequest.IResponseStringCallback() {
            @Override
            public void onSuccess(String response) {
                TLog.d(TickAPI.class, "leaveApp response : " +  response);

                callback.onSuccess();
            }

            @Override
            public void onError(ArrayList<ErrorView> errors) {
                TLog.d(TickAPI.class, "leaveApp errors : " +  errors);
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();

                return params;
            }
        };
        RunningApp.getInstance().addToRequestQueue(baseStringRequest);
    }

    public void getTickTicker( IGetTickCallback callback){
        TLog.d(TickAPI.class, "======>API : " + HomeApi.getInstance().urlGetTick());
        CustomStringRequest baseStringRequest = new CustomStringRequest(Request.Method.GET, HomeApi.getInstance().urlGetTick(), new CustomStringRequest.IResponseStringCallback() {
            @Override
            public void onSuccess(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(new JSONObject(response).getString("data"));
                    String isBlocked = jsonObject.getString("IsBlocked").toString();
                    String myId = jsonObject.getString("MyUserId").toString();
                    String otherId = jsonObject.getString("OtherUserId").toString();

//                    callback.onSuccess();

                }
                catch (JSONException e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(ArrayList<ErrorView> errors) {
                Log.d("Error:", errors.toString());
//                callback.onError();
            }
        });
        RunningApp.getInstance().addToRequestQueue(baseStringRequest);
    }
}
