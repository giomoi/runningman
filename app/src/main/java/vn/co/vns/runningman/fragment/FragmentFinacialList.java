package vn.co.vns.runningman.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import vn.co.vns.runningman.LoginController;
import vn.co.vns.runningman.R;
import vn.co.vns.runningman.RunningApp;
import vn.co.vns.runningman.activity.MainActivity;
import vn.co.vns.runningman.adapter.FinacialListAdapter;
import vn.co.vns.runningman.adapter.HomeDetailAdapter;
import vn.co.vns.runningman.object.Stock;
import vn.co.vns.runningman.object.StockValue;
import vn.co.vns.runningman.util.Downloader;
import vn.co.vns.runningman.util.SharedPreference;
import vn.co.vns.runningman.util.Singleton;

/**
 * Created by thanhnv on 11/25/16.
 */
public class FragmentFinacialList extends Fragment{
    private View mMainView;
    @InjectView(R.id.layoutMainView)    ScrollView layoutMainView;
    @InjectView(R.id.progressLoading)   ProgressBar progressLoading;
    private String stockID = "";

    public static FragmentFinacialList newInstance() {
        Bundle args = new Bundle();
        FragmentFinacialList fragment = new FragmentFinacialList();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mMainView = inflater.inflate(R.layout.fragment_finacial_list, null);
        mMainView.setTag("FragmentAnalyticList");

        ViewPager pager= mMainView.findViewById(R.id.pager);
        //pager.setOffscreenPageLimit(1);
        pager.setAdapter(buildAdapter());
        pager.setOffscreenPageLimit(1);

        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        return mMainView;
    }

    private PagerAdapter buildAdapter() {
        return(new FinacialListAdapter(getActivity(), getChildFragmentManager()));
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser ) {

        }
    }

}
