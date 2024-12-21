package vn.co.vns.runningman.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

import vn.co.vns.runningman.R;
import vn.co.vns.runningman.adapter.StockBreakOutVolumeAdapter;
import vn.co.vns.runningman.asyntask.BreakoutVolumeAsynctask;
import vn.co.vns.runningman.model.MySQLiteHelper;
import vn.co.vns.runningman.object.Stock;
import vn.co.vns.runningman.object.StockBigVolume;
import vn.co.vns.runningman.util.Constant;
import vn.co.vns.runningman.util.Singleton;
import vn.co.vns.runningman.util.Utils;

/**
 * Created by thanhnv on 11/28/16.
 */
public class FragmentBreakOutVolume extends Fragment {
    private ProgressDialog mProgressDialog;
    private View mMainView;
    private Spinner spinNumberDay,spinVolume;
    private ArrayList<StockBigVolume> listStockBreakOut=new ArrayList<>();
    private ListView lvBreakOut;
    private StockBreakOutVolumeAdapter breakOutVolumeAdapter=null;
    public static TextView txtNumberBreakOut;
    private TextView txtDateBrekOut;


    public static FragmentBreakOutVolume newInstance() {
        Bundle args = new Bundle();
        FragmentBreakOutVolume fragment = new FragmentBreakOutVolume();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mMainView = inflater.inflate(R.layout.fragment_break_out_volume, null);
        mMainView.setTag("FragmentHome");
        lvBreakOut= mMainView.findViewById(R.id.lv_break_out);
        spinNumberDay= mMainView.findViewById(R.id.spinNumberDay);
        spinVolume= mMainView.findViewById(R.id.spinVolume);
        txtDateBrekOut= mMainView.findViewById(R.id.txtDateBrekOut);
        txtNumberBreakOut= mMainView.findViewById(R.id.txtNumberBreakOut);
        if(Constant.dateTransition!=null) txtDateBrekOut.setText(Utils.convertStringToDateVN(Constant.dateTransition));
        createDropdown();
        return mMainView;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if(Constant.maxDay==null) {
                MySQLiteHelper db = new MySQLiteHelper(getContext());
                Constant.maxDay = db.getMaxDayUpdate();
            }
                initView();
            }
        }

    private void executeBreakOutTask(){
        BreakoutVolumeAsynctask taskBreakout=new BreakoutVolumeAsynctask(getContext(),Singleton.getInstance().getVolumeBreakOut(),Singleton.getInstance().getEndDateBreakOut());
        taskBreakout.setListener(new BreakoutVolumeAsynctask.MyAsyncTaskListener() {
            @Override
            public void onPreExecuteConcluded() {
                mProgressDialog = new ProgressDialog(getContext());
                mProgressDialog.setMessage("Loading...");
                mProgressDialog.setCancelable(false);
                mProgressDialog.show();
            }


            @Override
            public void onPostExecuteConcluded(ArrayList<StockBigVolume> listStockBreakOut) {
                breakOutVolumeAdapter.getDataStock(listStockBreakOut);
                txtNumberBreakOut.setText(String.valueOf(listStockBreakOut.size()));
                mProgressDialog.dismiss();
            }
        });
        if(listStockBreakOut.size()==0) taskBreakout.execute();
    }
    private void initView(){

        breakOutVolumeAdapter=new StockBreakOutVolumeAdapter(getContext(),listStockBreakOut);
        lvBreakOut.setAdapter(breakOutVolumeAdapter);

        executeBreakOutTask();

        spinNumberDay.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                Log.d("FragmentBigVolume.class", String.valueOf(adapterView.getId()));
                String endDate=null;
                switch(position)
                {
                    case 0:
                        endDate=null;
                        break;
                    case 1:
                        endDate=Utils.getDateJP(Utils.addDays(Utils.convertStringToDateString(Constant.maxDay),Constant.BREAK_OUT_NUMBER_DAY30));
                        break;
                    case 2:
                        endDate=Utils.getDateJP(Utils.addDays(Utils.convertStringToDateString(Constant.maxDay),Constant.BREAK_OUT_NUMBER_DAY90));
                        break;
                    case 3:
                        endDate=Utils.getDateJP(Utils.addDays(Utils.convertStringToDateString(Constant.maxDay),Constant.BREAK_OUT_NUMBER_DAY180));
                        break;
                    case 4:
                        endDate=Utils.getDateJP(Utils.addDays(Utils.convertStringToDateString(Constant.maxDay),Constant.BREAK_OUT_NUMBER_DAY270));
                        break;
                    case 5:
                        endDate=Utils.getDateJP(Utils.addDays(Utils.convertStringToDateString(Constant.maxDay),Constant.BREAK_OUT_NUMBER_DAY365));
                        break;
                }
                Singleton.getInstance().setEndDateBreakOut(endDate);
                executeBreakOutTask();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinVolume.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                Log.d("FragmenBreakOut.class", String.valueOf(adapterView.getId()));
                ArrayList<Stock> listStock =new ArrayList<>();
                switch(position)
                {
                    case 0:
                        Singleton.getInstance().setVolumeBreakOut(Constant.VOLUME0);
                        break;
                    case 1:
                        Singleton.getInstance().setVolumeBreakOut(Constant.VOLUME50);
                        break;
                    case 2:
                        Singleton.getInstance().setVolumeBreakOut(Constant.VOLUME100);
                        break;
                    case 3:
                        Singleton.getInstance().setRateBigVolume(Constant.VOLUME200);
                        break;
                    case 4:
                        Singleton.getInstance().setRateBigVolume(Constant.VOLUME300);
                        break;
                    case 5:
                        Singleton.getInstance().setRateBigVolume(Constant.VOLUME400);
                        break;
                    case 6:
                        Singleton.getInstance().setRateBigVolume(Constant.VOLUME500);
                        break;
                }
                executeBreakOutTask();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        lvBreakOut.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> breakOutAdapter, View view, int position, long l) {
                Stock objStock=(Stock) lvBreakOut.getAdapter().getItem(position);
//                String chartURL="https://www.vndirect.com.vn/portal/bieu-do-ky-thuat/"+ objStock.getTicker() +".shtml";
                String chartURL="http://m.cafef.vn/tra-cuu/"+ objStock.getTicker() +".chn";
                Intent intent= new Intent(Intent.ACTION_VIEW, Uri.parse(chartURL));
                startActivity(intent);
            }
        });
    }

    private void createDropdown() {
        String[] itemsNumberDay = new String[]{"All","1 tháng", "3 tháng","6 tháng", "9 tháng", "12 tháng"}; //,"36 năm", "60 tháng", "120 tháng"
        ArrayAdapter<String> adapterNumberDay = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, itemsNumberDay);
        spinNumberDay.setAdapter(adapterNumberDay);

        String[] itemsVolume = new String[]{"All","50,000","100,000", "200,000", "300,000","400,000", "500,000"};
        ArrayAdapter<String> adapterVolume = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, itemsVolume);
        spinVolume.setAdapter(adapterVolume);
    }

}
