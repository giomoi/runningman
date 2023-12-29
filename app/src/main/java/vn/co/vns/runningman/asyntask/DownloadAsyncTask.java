package vn.co.vns.runningman.asyntask;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import java.util.ArrayList;

import vn.co.vns.runningman.R;
import vn.co.vns.runningman.activity.MainActivity;
import vn.co.vns.runningman.fragment.FragmentBigVolume;
import vn.co.vns.runningman.model.MySQLiteHelper;
import vn.co.vns.runningman.object.StockBigVolume;
import vn.co.vns.runningman.util.Downloader;
import vn.co.vns.runningman.util.Singleton;
import vn.co.vns.runningman.util.Utils;

/**
 * Created by thanhnv on 11/15/16.
 */
public class DownloadAsyncTask extends AsyncTaskExecutorService<String, Boolean, Boolean> {
    private Context mContext;
    private Activity activity;
    private String urlAllDay;
    private String urlDay;
    private ProgressDialog mProgressDialog;

    public DownloadAsyncTask(Context mContext, String urlAllDay, String urlDay) {
        this.mContext = mContext;
        this.urlAllDay = urlAllDay;
        this.urlDay = urlDay;
        this.activity = (Activity) mContext;
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
    protected Boolean doInBackground(String s) {
        boolean isSucess = false;
        if (Utils.exists(mContext, urlAllDay)) {
            isSucess = Downloader.downloadFiles(mContext, urlAllDay, urlDay);
        }
        return isSucess;
    }

    @Override
    protected void onPostExecute(Boolean o) {
        if (o) {
            MySQLiteHelper insertStock = new MySQLiteHelper(mContext);
            ArrayList<StockBigVolume> listStock = insertStock.getListStockBigVolume(Singleton.getInstance().getVolumeBigVolume(), Singleton.getInstance().getRateBigVolume());
            if (listStock != null) {
                FragmentBigVolume.adapterTop.getDataStock(listStock);
                FragmentBigVolume.adapterTop.notifyDataSetChanged();
                FragmentBigVolume.txtNumberCode.setText(String.valueOf(listStock.size()));
            }
            if (mProgressDialog.isShowing()) {
                MainActivity.btnUnit.setText(mContext.getString(R.string.txt_updated));
                mProgressDialog.dismiss();
            }
        } else {
            Utils.showDialog(mContext, mContext.getString(R.string.dialog_disconect_download_error));
        }
    }
}

