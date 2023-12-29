package vn.co.vns.runningman.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import vn.co.vns.runningman.R;
import vn.co.vns.runningman.RunningApp;
import vn.co.vns.runningman.VolleyAPI.TickAPI;
import vn.co.vns.runningman.activity.MainActivity;
import vn.co.vns.runningman.adapter.ResultRecyclerViewAdapter;
import vn.co.vns.runningman.dialog.Dialog;
import vn.co.vns.runningman.dialog.DialogTickTicker;
import vn.co.vns.runningman.interfaces.IGetTickCallback;
import vn.co.vns.runningman.object.Tick;
import vn.co.vns.runningman.util.Constant;
import vn.co.vns.runningman.util.Utils;

/**
 * Created by thanhnv on 11/25/16.
 */
public class FragmentResult extends Fragment {
    private View mMainView;
    private RecyclerView mRecyclerView;
    private ResultRecyclerViewAdapter adapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private String TAG=FragmentResult.class.getSimpleName();
    private FloatingActionButton fabAdd;

    public static FragmentResult newInstance() {
        Bundle args = new Bundle();
        FragmentResult fragment = new FragmentResult();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mMainView = inflater.inflate(R.layout.fragment_result, null);
        mMainView.setTag("FragmentResult");
        mRecyclerView = mMainView.findViewById(R.id.recylerVuewResult);
        mSwipeRefreshLayout= mMainView.findViewById(R.id.swipeRefreshLayout);
        fabAdd=mMainView.findViewById(R.id.fab_add);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//        buildView();
        //Lam man hinh nay thi open comment 1 line nay ra
//        makeJsonObjectRequest();
//        getTickTicker();
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                makeJsonObjectRequest();
            }
        });
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DialogTickTicker(getContext(),getResources().getString(R.string.title_tick_ticker),null).show();
            }
        });
        return mMainView;
    }

    public static void changeTitleView(Activity activity){
        MainActivity.appTitle.setText(activity.getString(R.string.tab_result));
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(getContext()!=null) {
            if (Utils.isNetworkAvailable(getContext())) {
                if (isVisibleToUser) makeJsonObjectRequest();
            } else {
                Dialog.showDialogCancel(getActivity(), getResources().getString(R.string.content_disconnect_internet));
            }
        }
    }

    private void getTickTicker(){
        TickAPI.getInstance().getTickTicker(new IGetTickCallback() {
            @Override
            public void onError(ArrayList errors) {

            }

            @Override
            public void onSuccess(ArrayList<Tick> items) {

            }
        });
    }
    private void makeJsonObjectRequest(){
        mSwipeRefreshLayout.setRefreshing(true);
        String url= Constant.URL_API+Constant.TICK_GET_RESOURCE;
        JsonObjectRequest jsonObjRequest = new JsonObjectRequest(Request.Method.GET,
                url, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.d(TAG,"Json: "+ response.toString());
                            Gson json = new Gson();
                            Type listType = new TypeToken<ArrayList<Tick>>(){}.getType();

                            List<Tick> yourClassList = new Gson().fromJson(String.valueOf(response.getJSONArray("data")), listType);
                            Tick[] results=json.fromJson(String.valueOf(response.getJSONArray("data")), Tick[].class);
                            adapter = new ResultRecyclerViewAdapter(getContext(), yourClassList);
                            mRecyclerView.setAdapter(adapter);
                            mSwipeRefreshLayout.setRefreshing(false);
                            Log.i(TAG, new Gson().toJson(results));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG,  "Error:" +error.getMessage() );
            }
        }){
            /* Passing some request headers*/
            @Override
            public Map<String, String> getHeaders()throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                return headers;
            }

//            @Override
//            protected Map<String, String> getParams()throws AuthFailureError {
//                Map<String, String> params = new HashMap<String, String>();
//                params.put("name", "Androidhive");
//                params.put("email", "abc@androidhive.info");
//                params.put("pass", "password123");
//                return  params;
//            }
        } ;

        // Adding request to request queue
        RunningApp.getInstance().addToRequestQueue(jsonObjRequest, "OK");

    }
}
