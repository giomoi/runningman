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
public class FragmentEPSQuarter extends Fragment {
    private View mMainView;
    private ListView lvBigVolume;
    private StockBigVolumeAdapter adapterTop;

    public static FragmentEPSQuarter newInstance() {
        Bundle args = new Bundle();
        FragmentEPSQuarter fragment = new FragmentEPSQuarter();
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
        mMainView = inflater.inflate(R.layout.fragment_eps_year_quater, null);
        mMainView.setTag("FragmentHome");


        return mMainView;
    }
}
