package vn.co.vns.runningman.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import vn.co.vns.runningman.R;
import vn.co.vns.runningman.adapter.StockBigVolumeAdapter;
import vn.co.vns.runningman.asyntask.DownloadAsyncTask;
import vn.co.vns.runningman.model.MySQLiteHelper;
import vn.co.vns.runningman.object.StockBigVolume;
import vn.co.vns.runningman.util.Constant;
import vn.co.vns.runningman.util.Downloader;
import vn.co.vns.runningman.util.SharedPreference;
import vn.co.vns.runningman.util.Singleton;
import vn.co.vns.runningman.util.Utils;

/**
 * Created by thanhnv on 11/28/16.
 */
public class FragmentBigVolume extends Fragment {


    private ProgressDialog mProgressDialog;
    private View mMainView;
    private ListView lvBigVolume;
    private Spinner spinNumberDay,spinRate,spinVolume,spinTransaction;
    public static StockBigVolumeAdapter adapterTop;
    private ArrayList<StockBigVolume> arrayStockBigVolume=new ArrayList<StockBigVolume>();
    private  Integer   startAsyntask=0,endAsynctask=0;
    private HashMap<Integer,String> itemRates = new HashMap<Integer,String>();
    private HashMap<Integer,String> itemVolumes = new HashMap<Integer,String>();
    private MySQLiteHelper dbStock=null;
    private TextView txtdateTransition;
    public static TextView txtNumberCode;

    public static FragmentBigVolume newInstance() {
        Bundle args = new Bundle();
        FragmentBigVolume fragment = new FragmentBigVolume();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mMainView = inflater.inflate(R.layout.fragment_big_volume, null);
        mMainView.setTag("FragmentHome");
        StrictMode.ThreadPolicy policy = new StrictMode.
                ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        return mMainView;
    }

    private void  initView(){
        if(getUserVisibleHint()){
            dbStock = new MySQLiteHelper(getContext());

            lvBigVolume= mMainView.findViewById(R.id.lv_big_volume);
            txtdateTransition= mMainView.findViewById(R.id.txtdateTransitionBigVolume);
            txtNumberCode= mMainView.findViewById(R.id.txtNumberBigVolume);
            spinVolume= mMainView.findViewById(R.id.spinVolume);
            spinRate= mMainView.findViewById(R.id.spinRate);
            if(Constant.dateTransition!=null) txtdateTransition.setText(Utils.convertStringToDateVN(Constant.dateTransition));
            spinRate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                    Log.d("FragmentBigVolume.class", String.valueOf(adapterView.getId()));
                    ArrayList<StockBigVolume> listStock =new ArrayList<>();
                    switch(position)
                    {
                        case 0:
                            listStock=dbStock.getListStockBigVolume(Singleton.getInstance().getVolumeBigVolume(),2);
                            Singleton.getInstance().setRateBigVolume(2);
                            break;
                        case 1:
                            listStock=dbStock.getListStockBigVolume(Singleton.getInstance().getVolumeBigVolume(),3);
                            Singleton.getInstance().setRateBigVolume(3);
                            break;
                        case 2:
                            listStock=dbStock.getListStockBigVolume(Singleton.getInstance().getVolumeBigVolume(),4);
                            Singleton.getInstance().setRateBigVolume(4);
                            break;
                        case 3:
                            listStock=dbStock.getListStockBigVolume(Singleton.getInstance().getVolumeBigVolume(),5);
                            Singleton.getInstance().setRateBigVolume(5);
                            break;
                        case 4:
                            listStock=dbStock.getListStockBigVolume(Singleton.getInstance().getVolumeBigVolume(),6);
                            Singleton.getInstance().setRateBigVolume(6);
                            break;
                        case 5:
                            listStock=dbStock.getListStockBigVolume(Singleton.getInstance().getVolumeBigVolume(),7);
                            Singleton.getInstance().setRateBigVolume(7);
                            break;
                        case 6:
                            listStock=dbStock.getListStockBigVolume(Singleton.getInstance().getVolumeBigVolume(),8);
                            Singleton.getInstance().setRateBigVolume(8);
                            break;
                        case 7:
                            listStock=dbStock.getListStockBigVolume(Singleton.getInstance().getVolumeBigVolume(),9);
                            Singleton.getInstance().setRateBigVolume(9);
                            break;
                        case 8:
                            listStock=dbStock.getListStockBigVolume(Singleton.getInstance().getVolumeBigVolume(),10);
                            Singleton.getInstance().setRateBigVolume(10);
                            break;
                    }
                    if(listStock!=null){
                        adapterTop.getDataStock(listStock);
                        txtNumberCode.setText(String.valueOf(listStock.size()));
                    }

                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

            spinVolume.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                    ArrayList<StockBigVolume> listStock =new ArrayList<>();
                    switch(position)
                    {
                        case 0:
                            listStock=dbStock.getListStockBigVolume(50000,Singleton.getInstance().getRateBigVolume());
                            Singleton.getInstance().setVolumeBigVolume(50000);
                            break;
                        case 1:
                            listStock=dbStock.getListStockBigVolume(100000,Singleton.getInstance().getRateBigVolume());
                            Singleton.getInstance().setVolumeBigVolume(100000);
                            break;
                        case 2:
                            listStock=dbStock.getListStockBigVolume(200000,Singleton.getInstance().getRateBigVolume());
                            Singleton.getInstance().setVolumeBigVolume(200000);
                            break;
                        case 3:
                            listStock=dbStock.getListStockBigVolume(300000,Singleton.getInstance().getRateBigVolume());
                            Singleton.getInstance().setVolumeBigVolume(300000);
                            break;
                        case 4:
                            listStock=dbStock.getListStockBigVolume(400000,Singleton.getInstance().getRateBigVolume());
                            Singleton.getInstance().setVolumeBigVolume(400000);
                            break;
                        case 5:
                            listStock=dbStock.getListStockBigVolume(500000,Singleton.getInstance().getRateBigVolume());
                            Singleton.getInstance().setVolumeBigVolume(500000);
                            break;

                    }
                    if(listStock!=null){
                        adapterTop.getDataStock(listStock);
                        txtNumberCode.setText(String.valueOf(listStock.size()));
                    }

                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
//            spinTransaction= (Spinner) mMainView.findViewById(R.id.spinTransaction);
            Calendar cal = Calendar.getInstance();
            int hourofday = cal.get(Calendar.HOUR_OF_DAY);
           //Download data
            String dayVN="";
            String dayJP="";
            SimpleDateFormat sdf = new SimpleDateFormat("EEE");
            SimpleDateFormat dt1 = new SimpleDateFormat("EEE, d MMM yyyy");
            Date now = new Date();
            Date currentDateTime = new Date();
            String currentDateJP=Utils.getDateJP(currentDateTime);
            dayJP = (hourofday > 14) ? Constant.nowDay : Constant.beforeDay;
            dayVN = Utils.convertDate(dayJP);
            String currentDateVN=Utils.convertDate(currentDateJP);
            Singleton.getInstance().setNumberScreen(Constant.MAIN_BIG_VOLUME);
            MySQLiteHelper db=new MySQLiteHelper(getContext());
            Constant.maxDay=db.getMaxDayUpdate();
            //
            Date before7Day=Utils.addDays(currentDateTime,-7);

            String urlDay = Constant.URI+"/" + dayJP + "/CafeF.SolieuGD." + dayVN + ".zip";
            String urlAllDay = Constant.URI+"/" + dayJP + "/CafeF.SolieuGD.Upto" + dayVN + ".zip";
            if(SharedPreference.getInstance(getContext()).getInt("updatedStock",0)!=1) {
//                SharedPreference.getInstance(getContext()).putInt("updatedStock",1);
                if (Constant.maxDay == null) { // chua co gi insert tu dau
                    if (Utils.exists(getContext(), urlDay)) { // Ton tai link cua ngay hm nay
                        new DownloadAsyncTask(getActivity(), urlAllDay, urlDay).execute();
                    } else { //khong ton tai link cua ngay hm nay thi lay ngay hm truoc lap toi khi nao lay duoc thi thoi
                        while (!Utils.exists(getContext(), urlDay)) {
                            dayJP = Utils.getDateJP(Utils.addDays(Utils.convertStringToDateString(dayJP), -1));
                            dayVN = Utils.convertDate(dayJP);
                            urlDay = Constant.URI+"/" + dayJP + "/CafeF.SolieuGD." + dayVN + ".zip";
                            urlAllDay = Constant.URI+"/" + dayJP + "/CafeF.SolieuGD.Upto" + dayVN + ".zip";
                            if (Utils.exists(getContext(), urlDay)) {
                                new DownloadAsyncTask(getActivity(), urlAllDay, urlDay).execute();
                            }
                        }

                    }
                } else {// Co du lieu roi
                    if (!Constant.maxDay.equalsIgnoreCase(currentDateJP)) {
                        if ((Utils.convertStringToDateString(Constant.maxDay).compareTo(before7Day) < 0)) {
                            //Qua 7 ngay
                            urlAllDay = Constant.URI+"/" + dayJP + "/CafeF.SolieuGD.Upto" + dayVN + ".zip";
                            if (Utils.exists(getContext(), urlDay) && Utils.exists(getContext(),urlAllDay)) { // Ton tai link cua ngay hm nay
                                db.deleteDataTable("STOCK_BIG_VOLUME");
                                new DownloadAsyncTask(getActivity(), urlAllDay, urlDay).execute();
                            }else{
                                while (!Utils.exists(getContext(), urlDay ) || !Utils.exists(getContext(),urlAllDay)) {
                                    dayJP = Utils.getDateJP(Utils.addDays(Utils.convertStringToDateString(dayJP), -1));
                                    dayVN = Utils.convertDate(dayJP);
                                    urlDay = Constant.URI+"/" + dayJP + "/CafeF.SolieuGD." + dayVN + ".zip";
                                    urlAllDay = Constant.URI+"/" + dayJP + "/CafeF.SolieuGD.Upto" + dayVN + ".zip";
                                    if (Utils.exists(getContext(), urlDay) && Utils.exists(getContext(),urlAllDay)) {
                                        new DownloadAsyncTask(getActivity(), urlAllDay, urlDay).execute();
                                    }
                                }
                            }

                        } else {
                            // duoi 7 ngay ke tu ngay hien tai Down load tung file theo ngay giao dich
                            String dateDownload = Constant.maxDay;
                            while ((Utils.convertStringToDateString(dateDownload).before(Utils.convertStringToDateString(dayJP)))) {
                                dayJP = Utils.getDateJP(Utils.addDays(Utils.convertStringToDateString(dayJP), -1));
                                //String dateDownloadJP = dateDownload; // 20170329
                                dayVN = Utils.convertDate(dayJP);
                                if (Utils.exists(getContext(), urlDay)) {
                                    db.deleteDataTable("STOCK_BIG_VOLUME");
                                    new DownloadSingleFile(getActivity(), urlDay).execute();
                                    Log.i("Date download: ", dateDownload);
                                }
                                urlDay = Constant.URI+"/" + dayJP + "/CafeF.SolieuGD." + dayVN + ".zip";
                            }
                        }
                    }
                } // Check them la file ko ton tai tuc la ng
            }
            //get Data
            if(Constant.maxDay!=null){
                MySQLiteHelper insertStock = new MySQLiteHelper(getContext());
                ArrayList<StockBigVolume> listStock = insertStock.getListStockBigVolume(Singleton.getInstance().getVolumeBigVolume(),Singleton.getInstance().getRateBigVolume());
//                if(listStock!=null) {
//                    adapterTop = new StockBigVolumeAdapter(getActivity(), listStock);
//                    lvBigVolume.setAdapter(adapterTop);
//                    txtNumberCode.setText(String.valueOf(listStock.size()));
//                }else{
                    adapterTop = new StockBigVolumeAdapter(getActivity(), arrayStockBigVolume);
                    lvBigVolume.setAdapter(adapterTop);
                    txtNumberCode.setText(String.valueOf(arrayStockBigVolume.size()));
//                }
            }else {
                adapterTop = new StockBigVolumeAdapter(getActivity(), arrayStockBigVolume);
                lvBigVolume.setAdapter(adapterTop);
                txtNumberCode.setText(String.valueOf(arrayStockBigVolume.size()));
            }
            lvBigVolume.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> breakOutAdapter, View view, int position, long l) {
                    StockBigVolume objStockBigVolume=(StockBigVolume) lvBigVolume.getAdapter().getItem(position);
//                    String chartURL="https://www.vndirect.com.vn/portal/tong-quan/"+ objStockBigVolume.getTicker() +".shtml";
                    String chartURL="http://m.cafef.vn/tra-cuu/"+ objStockBigVolume.getTicker() +".chn";
                    Intent intent= new Intent(Intent.ACTION_VIEW, Uri.parse(chartURL));
                    startActivity(intent);
                }
            });
            createDropdown();

        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            initView();
        }
        else {
        }
    }

    private void createDropdown(){
//        String[] itemsNumberDay = new String[]{"All","10 ngày", "20 ngày", "30 ngày"};
//        ArrayAdapter<String> adapterNumberDay = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, itemsNumberDay);
//        spinNumberDay.setAdapter(adapterNumberDay);
        itemRates.put(-1, "All");
        itemRates.put(2, "2 lần");
        itemRates.put(3, "3 lần");
        itemRates.put(4, "4 lần");
        itemRates.put(5, "5 lần");
        itemRates.put(6, "6 lần");
        itemRates.put(7, "7 lần");
        itemRates.put(8, "8 lần");
        itemRates.put(9, "9 lần");
        itemRates.put(10, "10 lần");
        String[] itemsRate = new String[]{"2 lần", "3 lần", "4 lần","5 lần", "6 lần", "7 lần","8 lần", "9 lần", "10 lần"};
        ArrayAdapter<String> adapterRate = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, itemsRate);
        spinRate.setAdapter(adapterRate);

        String[] itemsVolume = new String[]{"50,000","100,000", "200,000", "300,000","400,000", "500,000"};
        ArrayAdapter<String> adapterVolume = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, itemsVolume);
        spinVolume.setAdapter(adapterVolume);


//        String[] itemsTransaction = new String[10];
//        itemsTransaction[0]=Utils.converDateToStringVN(Utils.addDays(new Date(),0));
//        itemsTransaction[1]=Utils.converDateToStringVN(Utils.addDays(new Date(),-1));
//        itemsTransaction[2]=Utils.converDateToStringVN(Utils.addDays(new Date(),-2));
//        itemsTransaction[3]=Utils.converDateToStringVN(Utils.addDays(new Date(),-3));
//        itemsTransaction[4]=Utils.converDateToStringVN(Utils.addDays(new Date(),4));
//        itemsTransaction[5]=Utils.converDateToStringVN(Utils.addDays(new Date(),-5));
//        itemsTransaction[6]=Utils.converDateToStringVN(Utils.addDays(new Date(),-6));
//        itemsTransaction[7]=Utils.converDateToStringVN(Utils.addDays(new Date(),-7));
//        itemsTransaction[8]=Utils.converDateToStringVN(Utils.addDays(new Date(),-8));
//        itemsTransaction[9]=Utils.converDateToStringVN(Utils.addDays(new Date(),-9));
//
//        ArrayAdapter<String> adapterTransaction = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, itemsTransaction);
//        spinTransaction.setAdapter(adapterTransaction);
//        if (!Constant.dateTransition.equals(null)) {
//            int spinnerPosition = adapterTransaction.getPosition(Utils.convertStringToDateVN(Constant.dateTransition));
//            spinTransaction.setSelection(spinnerPosition);
//        }else {
//            spinTransaction.setSelection(0);
//        }
    }

    private class  DownloadSingleFile extends AsyncTask<Void, Integer, Boolean>{

        private Context mContext;
        private String url;


        public DownloadSingleFile(Context mContext, String url){
            this.mContext=mContext;
            this.url=url;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            int singleFile=1;
            boolean isSucess = false;
//                Log.i("Result: ", String.valueOf(isSucess));
                if(Utils.exists(mContext,url)){
                    isSucess = Downloader.downloadFile(mContext, url, singleFile);
                }
                Log.i("Result: ", String.valueOf(isSucess));
                Log.d("Path: ",url);

            return isSucess;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if(startAsyntask==0) {
                mProgressDialog = new ProgressDialog(mContext);
                mProgressDialog.setMessage("Loading...");
                mProgressDialog.setCancelable(false);
                mProgressDialog.show();
            }
            synchronized(startAsyntask) {
                startAsyntask++;
            }
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            synchronized(endAsynctask) {
                endAsynctask++;
            }
            if(result && startAsyntask==endAsynctask) {
                MySQLiteHelper insertStock = new MySQLiteHelper(mContext);
                ArrayList<StockBigVolume> listStock = insertStock.getListStockBigVolume(Singleton.getInstance().getVolumeBigVolume(),Singleton.getInstance().getRateBigVolume());
                if(adapterTop!=null) FragmentBigVolume.adapterTop.getDataStock(listStock);
                FragmentBigVolume.txtNumberCode.setText(String.valueOf(listStock.size()));
                //FragmentBigVolume.adapterTop.notifyDataSetChanged();
               // if(mProgressDialog==null) mProgressDialog = new ProgressDialog(mContext);
                mProgressDialog.dismiss();
            }
        }
    }

    @Override
    public void onDetach() {
        if (mProgressDialog != null && mProgressDialog.isShowing())
            mProgressDialog.dismiss();
        super.onDetach();
    }
}
