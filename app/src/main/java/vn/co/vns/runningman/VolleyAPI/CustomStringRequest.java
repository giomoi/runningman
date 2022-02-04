package vn.co.vns.runningman.VolleyAPI;

import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.ArrayList;
import java.util.Arrays;

import vn.co.vns.runningman.error.ErrorView;
import vn.co.vns.runningman.error.MCErrorCode;
import vn.co.vns.runningman.error.TLog;
import vn.co.vns.runningman.util.Constant;
import vn.co.vns.runningman.util.Utils;

/**
 * Created by thanhnv on 7/13/17.
 */

public class CustomStringRequest extends StringRequest{

    public CustomStringRequest(int method, String url,
                               final IResponseStringCallback callback) {

        super(method, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                callback.onSuccess(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String responseData = error.getMessage();
                TLog.e(CustomStringRequest.class, responseData);
                try {
                    ArrayList<ErrorView> errorViews = Utils.parseErrorViews(responseData);
                    if (errorViews == null || errorViews.size() == 0) {
                        Log.d("Error ","string request 1 ==>" +errorViews.toString());
                        callback.onError(new ArrayList<>(Arrays.asList(new ErrorView("", MCErrorCode.ConnectionError.getValue()))));
                    } else {
                        Log.d("Error ","string request 2 ==>" +errorViews.toString());
                        callback.onError(errorViews);
                    }
                } catch (Exception ex) {
                    Log.d("Error ","string request 3 ==>" +ex.toString());
                    callback.onError(new ArrayList<>(Arrays.asList(new ErrorView("", MCErrorCode.ConnectionError.getValue()))));
                }
            }
        });
        setRetryPolicy(new DefaultRetryPolicy(Constant.VOLLEY_TIMEOUT, Constant.VOLLEY_MAX_NUM_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    @Override
    protected VolleyError parseNetworkError(VolleyError volleyError) {
        Log.d("Error ","string request 4 ==>" +volleyError.toString());
        if (volleyError.networkResponse != null && volleyError.networkResponse.data != null) {
            volleyError = new VolleyError(new String(volleyError.networkResponse.data));
        }
        return volleyError;
    }

    public interface IResponseStringCallback {
        void onSuccess(String response);
        void onError(ArrayList<ErrorView> errors);
    }
}
