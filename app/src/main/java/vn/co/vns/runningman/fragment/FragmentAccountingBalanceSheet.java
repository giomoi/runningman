package vn.co.vns.runningman.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
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
public class FragmentAccountingBalanceSheet extends Fragment {
    private View mMainView;
    private WebView webAccountBalanceSheet;

    public static FragmentAccountingBalanceSheet newInstance() {
        Bundle args = new Bundle();
        FragmentAccountingBalanceSheet fragment = new FragmentAccountingBalanceSheet();
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
        mMainView = inflater.inflate(R.layout.fragment_account_balance_sheet, null);
        mMainView.setTag("FragmentAccountBalanceSheet");
        webAccountBalanceSheet= mMainView.findViewById(R.id.webAccountBalanceSheet);
        webAccountBalanceSheet.getSettings().setLoadsImagesAutomatically(true);
        webAccountBalanceSheet.getSettings().setJavaScriptEnabled(true);
        webAccountBalanceSheet.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
//        webAccountBalanceSheet.loadUrl("https://www.vndirect.com.vn/portal/bang-can-doi-ke-toan/acb.shtml");

        return mMainView;
    }
}
