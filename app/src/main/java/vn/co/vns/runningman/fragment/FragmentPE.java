package vn.co.vns.runningman.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

import vn.co.vns.runningman.R;
import vn.co.vns.runningman.adapter.StockBigVolumeAdapter;
import vn.co.vns.runningman.model.MySQLiteHelper;
import vn.co.vns.runningman.object.StockVolumePriceAgreement;

/**
 * Created by thanhnv on 11/28/16.
 */
public class FragmentPE extends Fragment {
    private View mMainView;
    private ListView lvBigVolume;
    private StockBigVolumeAdapter adapterTop;

    public static FragmentPE newInstance() {
        Bundle args = new Bundle();
        FragmentPE fragment = new FragmentPE();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mMainView = inflater.inflate(R.layout.fragment_pe, null);
        mMainView.setTag("FragmentHome");


//        Thread thread = new Thread(new Runnable() {
//
//            @Override
//            public void run() {
//                Document doc = null;
//                try {
//                    doc = Jsoup.connect("http://liveboard.cafef.vn/?center=1").get();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                Elements newsHeadlines = doc.select("td .col_e .down-price");
//                Log.d("ABC: ",newsHeadlines.toString());
//            }
//        });

//        thread.start();

        MySQLiteHelper db= new MySQLiteHelper(getContext());
        ArrayList<StockVolumePriceAgreement> arrayStockVolumePrice=db.getListStockVolumePriceAgreement();
//        adapterTop=new ListTopAdapter(getContext(),arrayStockVolumePrice);
//        lvBigVolume.setAdapter(adapterTop);

        return mMainView;
    }
}
