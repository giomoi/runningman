package vn.co.vns.runningman.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import vn.co.vns.runningman.R;
import vn.co.vns.runningman.activity.MainActivity;
import vn.co.vns.runningman.object.StockValue;

/**
 * Created by thanhnv on 11/25/16.
 */
public class FragmentAnalytic extends Fragment {
    private View mMainView;
    private WebView mainWebview;
    public static String stockAnalyticID;

    public static FragmentAnalytic newInstance() {
        Bundle args = new Bundle();
        FragmentAnalytic fragment = new FragmentAnalytic();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mMainView = inflater.inflate(R.layout.fragment_analytic, null);
        mMainView.setTag("FragmentAnalytic");
        return mMainView;
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser) getCharterCapital("VE9");
    }

    private void getCharterCapital(String stockId) {
        stockAnalyticID = stockId;
        mainWebview = new WebView(getContext());
        mainWebview.getSettings().setJavaScriptEnabled(true);
        mainWebview.setWebChromeClient(new WebChromeClient());
        mainWebview.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                mainWebview.loadUrl("javascript:window.HTMLOUT.processHTML('<head>'+document.getElementsByTagName('html')[0].innerHTML+'</head>');");
            }
        });
        String urlStockId = "http://www.cophieu68.vn/snapshot.php?id="+stockAnalyticID;
        urlStockId="http://m.cophieu68.vn/snapshot.php?id=Ve9&s_search=Go";
        mainWebview.loadUrl(urlStockId);
        mainWebview.addJavascriptInterface(new MJSChaterCapitalInterface(), "HTMLOUT");
    }

    private class MJSChaterCapitalInterface
    {
        @JavascriptInterface
        @SuppressWarnings("unused")
        public void processHTML(final String html)
        {
            if(html.length()>0){
                final StockValue stockValue = getDataFromHtml(html);
                if(stockValue!=null){
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
//                            setValuetoView(stockValue);
                        }
                    });
                }

            }
        }
    }

    private StockValue getDataFromHtml(String fullHtml) {
        StockValue stockValue = null;
        try {
            stockValue = new StockValue();
            stockValue.setTicker(stockAnalyticID);
            Document doc = Jsoup.parse(fullHtml);
            Elements key = doc.select("div.rowa");
            key.addAll(doc.select("div.rowb"));
            Elements value = doc.select("div.rowc");
            for(int i =0;i<value.size();i++){
                stockValue.setValue(value.get(i).text(), i);
            }
            Elements stringName = doc.select("span.orange");
            stockValue.setStockName(stringName.get(0).text());
            Elements stringHtml = doc.select("div.highcharts-container");
            stockValue.setHtmlChart(stringHtml.get(0).toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stockValue;
    }

    public static void changeTitleView(Activity activity){
        MainActivity.appTitle.setText(activity.getString(R.string.tab_analytic));
    }
}
