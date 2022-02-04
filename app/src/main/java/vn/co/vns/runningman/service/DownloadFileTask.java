package vn.co.vns.runningman.service;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.util.ArrayList;

import vn.co.vns.runningman.fragment.FragmentBigVolume;
import vn.co.vns.runningman.model.MySQLiteHelper;
import vn.co.vns.runningman.object.StockBigVolume;
import vn.co.vns.runningman.util.Constant;
import vn.co.vns.runningman.util.Downloader;
import vn.co.vns.runningman.util.Singleton;

/**
 * Created by thanhnv on 10/21/16.
 */
public class DownloadFileTask extends AsyncTask<Void, Integer, Boolean> {
    private Context mContext;
    private String url;
    private ProgressDialog mProgressDialog;

    public DownloadFileTask(Context mContext,String url){
        this.mContext=mContext;
        this.url=url;
    }

    public DownloadFileTask(Context mContext){
        this.mContext=mContext;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mProgressDialog = new ProgressDialog(mContext);
        mProgressDialog.setMessage("Loading...");
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
    }

    @Override
    protected Boolean doInBackground(Void... urls) {
        int count = Constant.URL.length;
        boolean isSucess = false;
        long totalSize = 0;
        //for (int i = 0; i < Constant.URL.length; i++) {
        int singleFile=1;
        isSucess= Downloader.downloadFile(mContext, url,singleFile);
        // publishProgress((int) ((i / (float) count) * 100));
        // Escape early if cancel() is called
        //if (isCancelled()) break;
        //}
        //Log.d("Finished", "doInBackground");
        return isSucess;
    }

    @Override
    protected void onPostExecute(Boolean result) {
        super.onPostExecute(result);
        Log.d("Finished", "Downloaded " + result + " bytes");
        if(result) {
            MySQLiteHelper insertStock = new MySQLiteHelper(mContext);
            ArrayList<StockBigVolume> listStock = insertStock.getListStockBigVolume(Singleton.getInstance().getVolumeBigVolume(),Singleton.getInstance().getRateBigVolume());
            FragmentBigVolume.adapterTop.getDataStock(listStock);
            FragmentBigVolume.adapterTop.notifyDataSetChanged();
            mProgressDialog.dismiss();
        }
    }

    protected void onProgressUpdate(Integer... progress) {
        //Real time
        //setProgressPercent(progress[0]);
    }

    protected void onPostExecute(boolean result) {

    }
}
