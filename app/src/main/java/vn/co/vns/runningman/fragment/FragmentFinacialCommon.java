package vn.co.vns.runningman.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import vn.co.vns.runningman.dialog.Dialog;
import vn.co.vns.runningman.object.Stock;
import vn.co.vns.runningman.object.StockValue;
import vn.co.vns.runningman.util.Downloader;
import vn.co.vns.runningman.util.SharedPreference;
import vn.co.vns.runningman.util.Singleton;
import vn.co.vns.runningman.util.Utils;

/**
 * Created by thanhnv on 11/25/16.
 */
public class FragmentFinacialCommon extends Fragment{
    private View mMainView;
    private String listTicker="";
    private WebView mainWebview;
    @InjectView(R.id.layoutMainView)    ScrollView layoutMainView;
    @InjectView(R.id.progressLoading)   ProgressBar progressLoading;
    private String stockID = "";

    public static FragmentFinacialCommon newInstance() {
        Bundle args = new Bundle();
        FragmentFinacialCommon fragment = new FragmentFinacialCommon();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mMainView = inflater.inflate(R.layout.fragment_finacial_common, null);
        mMainView.setTag("FragmentAnalytic");
        ButterKnife.inject(this, mMainView);
        Button btnSubmit = mMainView.findViewById(R.id.btnSubmit);
        if(SharedPreference.getInstance().getString("TICKER", "")!=null)
            listTicker = SharedPreference.getInstance().getString("TICKER", "");
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editText = mMainView.findViewById(R.id.edtStockId);
                if(Utils.isNetworkAvailable(getActivity())) {
                    if (!editText.getText().toString().trim().isEmpty()) {
                        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(mMainView.getWindowToken(), 0);
                        progressLoading.setVisibility(View.VISIBLE);
                        getCharterCapital(editText.getText().toString().trim());
                    }
                }else{
                    Dialog.showDialogCancel(getActivity(), getResources().getString(R.string.content_disconnect_internet));

                }

            }
        });
        return mMainView;
    }

    private void getCharterCapital(String stockId) {
        stockID = stockId;
        mainWebview = new WebView(getContext());
        mainWebview.getSettings().setJavaScriptEnabled(true);
        mainWebview.setWebChromeClient(new WebChromeClient());
        mainWebview.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                mainWebview.loadUrl("javascript:window.HTMLOUT.processHTML('<head>'+document.getElementsByTagName('html')[0].innerHTML+'</head>');");
            }
        });
        String urlStockId = "https://www.vndirect.com.vn/portal/tong-quan/"+stockId+".shtml";
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
                        setValuetoView(stockValue);
                    }
                });
                }

            }
        }
    }

    private void setValuetoView(StockValue stockValue){
        TextView txtView = null;
        txtView = mMainView.findViewById(R.id.txtStockId);
        txtView.setText(stockValue.getTicker());
        txtView = mMainView.findViewById(R.id.txtMin);
        txtView.setText(stockValue.getNearByMin());
        txtView = mMainView.findViewById(R.id.txtMax);
        txtView.setText(stockValue.getNearByMax());
        txtView = mMainView.findViewById(R.id.txtWeight10Days);
        txtView.setText(stockValue.getWeight10Days());
        txtView = mMainView.findViewById(R.id.txtThiGiaVon);
        txtView.setText(stockValue.getThiGiaVon());
        txtView = mMainView.findViewById(R.id.txtCPLuuHanh);
        txtView.setText(stockValue.getCPLuuHanh());
        txtView = mMainView.findViewById(R.id.txtCPNiemYet);
        txtView.setText(stockValue.getCPNiemYet());
        txtView = mMainView.findViewById(R.id.txtCoTuc_ThiGia);
        txtView.setText(stockValue.getTyLeCoTuc_ThiGia());
        txtView = mMainView.findViewById(R.id.txtEPS_4Quy);
        txtView.setText(stockValue.getEPS_4Quy());
        txtView = mMainView.findViewById(R.id.txtEPS_Nam);
        txtView.setText(stockValue.getEPS_Nam());
        txtView = mMainView.findViewById(R.id.txtROA);
        txtView.setText(stockValue.getROA());
        txtView = mMainView.findViewById(R.id.txtROE);
        txtView.setText(stockValue.getROE());
        txtView = mMainView.findViewById(R.id.txtDonBayTC);
        txtView.setText(stockValue.getDonBayTC());
        txtView = mMainView.findViewById(R.id.txtPE);
        txtView.setText(stockValue.getPE());
        txtView = mMainView.findViewById(R.id.txtPB);
        txtView.setText(stockValue.getPB());
        txtView = mMainView.findViewById(R.id.txtBeta);
        txtView.setText(stockValue.getBEta());
        txtView = mMainView.findViewById(R.id.txtStockName);
        txtView.setText(stockValue.getStockName());
        WebView webViewChart = mMainView.findViewById(R.id.webViewChart);
        webViewChart.loadData(stockValue.getHtmlChart(), "text/html; charset=UTF-8", null);
        progressLoading.setVisibility(View.GONE);
    }

    private StockValue getDataFromHtml(String fullHtml) {
        StockValue stockValue = null;
        try {
            stockValue = new StockValue();
            stockValue.setTicker(stockID);
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

    private void buildView() {
        ArrayList<Stock> listStock = Singleton.getInstance().getListStock();
        for(int i=0; i<listStock.size(); i++){
            String tickerItem = listStock.get(i).getTicker();
            if(!listTicker.contains(tickerItem)) {
                new LoginHandler().execute(tickerItem);
            }
        }
    }

    private void getDataTicker(int position){
            ArrayList<Stock> listStock = Singleton.getInstance().getListStock();
//        if(position >= listStock.size()) return;
            if (position == 1 || listStock.size()==0) return;
            String tickerItem = listStock.get(position).getTicker();
            Log.i("Ticker Data", tickerItem);
            if (!listTicker.contains(tickerItem)) {
                new LoginHandler().execute(String.valueOf(position), tickerItem);
            } else {
                getDataTicker(position + 1);
            }

    }

    private boolean loadingData = false;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && !loadingData ) {
            loadingData = true;
            getDataTicker(0);
        }
    }

    private void handleLogin(final String position, final String ticker) {
        if(RunningApp.getInstance().getCookie().isEmpty()) {
            LoginController.getInstance().POST_req(new LoginController.ILoginCallback() {
                @Override
                public void onSuccess() {
                    handleDownloadFile(ticker, position);
                }

                @Override
                public void onError() {
//                    Toast.makeText(getContext(), "Can not login", Toast.LENGTH_SHORT).show();
                    Log.d("Bug","Can not Login");
                }
            });
        }else{
            handleDownloadFile(ticker, position);
        }
    }

    private void handleDownloadFile(final String ticker, final String position) {
        (new Downloader()).downloadFile(ticker, new Downloader.IDownloadSuccessCallback() {
            @Override
            public void onSuccess(String filePath) {
                listTicker += ticker.toUpperCase() + ",";
                SharedPreference.getInstance().putString("TICKER", listTicker);
                Log.i("Data", "Success " + ticker + ", " + position);
                getDataTicker(Integer.parseInt(position) + 1);
            }

            @Override
            public void onError() {
                getDataTicker(Integer.parseInt(position) + 1);
                Log.i("Data", "Failed");
            }
        });
    }

    public static void changeTitleView(Activity activity){
        MainActivity.appTitle.setText(activity.getString(R.string.tab_finacial));
    }

    private class LoginHandler extends AsyncTask<String, String, String>{

        @Override
        protected String doInBackground(String... params) {
            String position = params[0];
            String ticker = params[1];
            handleLogin(position, ticker);
            return null;
        }
    }
}
