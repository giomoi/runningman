package vn.co.vns.runningman.asyntask;

import android.content.Context;
import android.os.AsyncTask;

import java.util.ArrayList;

import vn.co.vns.runningman.model.MySQLiteHelper;
import vn.co.vns.runningman.object.Stock;
import vn.co.vns.runningman.object.StockBigVolume;
import vn.co.vns.runningman.util.Constant;

/**
 * Created by Thanh on 5/27/2017.
 */

public class BreakoutVolumeAsynctask extends AsyncTask<Void, Void, ArrayList<StockBigVolume>>{
    private String endDate;
    private Integer volume;
    private Context mContext;
    private ArrayList<StockBigVolume> arrayListStock;
    private  ArrayList<Stock> listMaxPrice;
    private ArrayList<StockBigVolume> listStockBreakOut;
    private MyAsyncTaskListener mListener;

    public interface MyAsyncTaskListener {
        void onPreExecuteConcluded();
        void onPostExecuteConcluded(ArrayList<StockBigVolume> listStockBreakOut);
    }

    final public void setListener(MyAsyncTaskListener listener) {
        mListener = listener;
    }

    public BreakoutVolumeAsynctask(Context mContext, Integer volume, String endDate) {
        this.endDate=endDate;
        this.volume=volume;
        this.mContext=mContext;
    }


    @Override
    protected ArrayList<StockBigVolume> doInBackground(Void... voids) {
        MySQLiteHelper dbStock=new MySQLiteHelper(mContext);
        arrayListStock=dbStock.getStockDateBigVolume(Constant.maxDay, volume,-1f,-1f);
        listMaxPrice=dbStock.getListMMaxPriceStock(Constant.maxDay,endDate);
        //Check voi cai nay arrayListStock ra danh sach break out
        listStockBreakOut=getListStockBreakOut(listMaxPrice,arrayListStock);
        return listStockBreakOut;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (mListener != null)
            mListener.onPreExecuteConcluded();
    }

    @Override
    protected void onPostExecute(ArrayList<StockBigVolume> listBreakOut) {
        super.onPostExecute(listBreakOut);
        if (mListener != null)
            mListener.onPostExecuteConcluded(listBreakOut);
    }

    private ArrayList<StockBigVolume> getListStockBreakOut(ArrayList<Stock> listStockMax, ArrayList<StockBigVolume> listStockCurrent){
        ArrayList<StockBigVolume> listBreakOut=new ArrayList<>();
        for(int i=0;i<listStockCurrent.size();i++){

            for(int j=0;j<listStockMax.size();j++){
                if(listStockCurrent.get(i).getTicker().equalsIgnoreCase(listStockMax.get(j).getTicker()) && listStockCurrent.get(i).getClose()>=listStockMax.get(j).getClose()  ){

                    listBreakOut.add(listStockCurrent.get(i));
                }
            }
        }
        return listBreakOut;
    }
}
