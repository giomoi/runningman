package vn.co.vns.runningman.asyntask;

import android.content.Context;
import android.os.AsyncTask;

import java.util.ArrayList;

import vn.co.vns.runningman.model.MySQLiteHelper;
import vn.co.vns.runningman.object.Stock;
import vn.co.vns.runningman.object.StockBigVolume;
import vn.co.vns.runningman.util.Constant;
import vn.co.vns.runningman.util.Singleton;

/**
 * Created by Thanh on 5/27/2017.
 */

public class BigVolumeAsynctask extends AsyncTask<Void, Void, ArrayList<StockBigVolume>>{
    private Context mContext;
    private ArrayList<StockBigVolume> listBigVolume;
    private MyAsyncTaskListener mListener;

    public interface MyAsyncTaskListener {
        void onPreExecuteConcluded();
        void onPostExecuteConcluded(ArrayList<StockBigVolume> listStockBreakOut);
    }

    final public void setListener(MyAsyncTaskListener listener) {
        mListener = listener;
    }

    public BigVolumeAsynctask(Context mContext) {
        this.mContext=mContext;
    }


    @Override
    protected ArrayList<StockBigVolume> doInBackground(Void... voids) {
        MySQLiteHelper dbStock=new MySQLiteHelper(mContext);
        listBigVolume=dbStock.getListStockBigVolume(Singleton.getInstance().getVolumeBigVolume(),Singleton.getInstance().getRateBigVolume());
        return listBigVolume;
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

}
