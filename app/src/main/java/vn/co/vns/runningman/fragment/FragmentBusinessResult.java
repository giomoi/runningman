package vn.co.vns.runningman.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

import vn.co.vns.runningman.R;
import vn.co.vns.runningman.adapter.StockBigVolumeAdapter;
import vn.co.vns.runningman.model.MySQLiteHelper;
import vn.co.vns.runningman.object.StockVolumePriceAgreement;

/**
 * Created by thanhnv on 11/28/16.
 */
public class FragmentBusinessResult extends Fragment {
    private View mMainView;
    private String urlString="https://www.vndirect.com.vn/portal/bang-can-doi-ke-toan/tdh.shtml";
    private ListView lvBigVolume;
    private StockBigVolumeAdapter adapterTop;
    private WebView webViewBusinessResult;

    public static FragmentBusinessResult newInstance() {
        Bundle args = new Bundle();
        FragmentBusinessResult fragment = new FragmentBusinessResult();
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
        mMainView = inflater.inflate(R.layout.fragment_business_result, null);
        mMainView.setTag("FragmentHome");
        webViewBusinessResult= mMainView.findViewById(R.id.webViewBusinessResult);

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
//        initView();
        if (resetThread.getState() == Thread.State.NEW)
        {
            resetThread.start();
        }
                if(!resetThread.isAlive()) {
                    resetThread.start();
                }

        return mMainView;
    }

    /**
     * @return The current value of the user-visible hint on this fragment.
     * @see #setUserVisibleHint(boolean)
     */
    @Override
    public boolean getUserVisibleHint() {

        return super.getUserVisibleHint();

    }

    private Thread resetThread = new Thread(new Runnable() {
        @Override
        public void run() {
            initView();
        }
    });

    private void initView() {
//        Document doc = Jsoup.parse(fullHtml);
        Document doc = null;
        try {
            doc = Jsoup.connect(urlString).get();
            Element head = doc.head();
            Elements trTable = doc.select("table");
            head.appendElement("<body>" + trTable.get(1).toString() + "</body>");
            final String strHTML=head.toString();
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    webViewBusinessResult.getSettings().setLoadsImagesAutomatically(true);
                    webViewBusinessResult.getSettings().setJavaScriptEnabled(true);
                    webViewBusinessResult.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
                    webViewBusinessResult.loadDataWithBaseURL(null, strHTML, "text/html", "UTF-8", null);
                }
            });
            Log.d("HTML: ",trTable.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
