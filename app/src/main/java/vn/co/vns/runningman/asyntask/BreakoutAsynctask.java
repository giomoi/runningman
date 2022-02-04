package vn.co.vns.runningman.asyntask;

import android.content.Context;
import android.os.AsyncTask;

import java.util.ArrayList;

import vn.co.vns.runningman.model.MySQLiteHelper;
import vn.co.vns.runningman.object.Stock;
import vn.co.vns.runningman.util.Constant;

/**
 * Created by Thanh on 5/27/2017.
 */

public class BreakoutAsynctask extends AsyncTask<Void, Void, ArrayList<Stock>>{
    private String endDate;
    private Integer volume;
    private Context mContext;
    private ArrayList<Stock> arrayListStock;
    private  ArrayList<Stock> listMaxPrice;
    private ArrayList<Stock> listStockBreakOut;
    private MyAsyncTaskListener mListener;

    public interface MyAsyncTaskListener {
        void onPreExecuteConcluded();
        void onPostExecuteConcluded(ArrayList<Stock>  listStockBreakOut);
    }

    final public void setListener(MyAsyncTaskListener listener) {
        mListener = listener;
    }

    public BreakoutAsynctask(Context mContext,Integer volume, String endDate) {
        this.endDate=endDate;
        this.volume=volume;
        this.mContext=mContext;
    }


    @Override
    protected ArrayList<Stock> doInBackground(Void... voids) {
        MySQLiteHelper dbStock=new MySQLiteHelper(mContext);
        arrayListStock=dbStock.getStockDate(Constant.maxDay, volume,-1f,-1f);
//        arrayListStock=dbStock.getStockDateBigVolume(Constant.maxDay, volume,-1f,-1f);
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
    protected void onPostExecute(ArrayList<Stock> listBreakOut) {
        super.onPostExecute(listBreakOut);
        if (mListener != null)
            mListener.onPostExecuteConcluded(listBreakOut);
    }

    private ArrayList<Stock> getListStockBreakOut(ArrayList<Stock> listStockMax, ArrayList<Stock> listStockCurrent){
        ArrayList<Stock> listBreakOut=new ArrayList<>();
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
