package vn.co.vns.runningman.fragment;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;

import vn.co.vns.runningman.R;
import vn.co.vns.runningman.adapter.PriceVolumeAgrementAdapter;
import vn.co.vns.runningman.adapter.StockBigVolumeAdapter;
import vn.co.vns.runningman.object.InforBuySellStockIndex;
import vn.co.vns.runningman.object.InforStockIndex;
import vn.co.vns.runningman.object.InforVolumeValueStockIndex;
import vn.co.vns.runningman.util.SharedPreference;
import vn.co.vns.runningman.util.Singleton;

/**
 * Created by thanhnv on 11/28/16.
 */
public class PriceVolumeAgreement extends Fragment {
    public static TextView txtVolume, txtValue, txtRateVolume, txtRateValue;
    double rateValue10;
    private View mMainView;
    private TextView txtDateTransit, txtIndex;
    private ArrayList<InforBuySellStockIndex> listInforBuySellStockIndex = new ArrayList<>();
    private ArrayList<InforStockIndex> listInforStockIndex = new ArrayList<>();
    private PriceVolumeAgrementAdapter priceVolumeAgrementAdapter;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private LinearLayout lnBuySellStockIndex, lnStockIndex;
    private SwipeRefreshLayout mSwipeRefresh;
    private final Handler uiHandler = new Handler();

    private Thread resetThread = new Thread(new Runnable() {
        @Override
        public void run() {
            Document doc = null;
            try {
                doc = Jsoup.connect("https://s.cafef.vn/lich-su-giao-dich-symbol-vnindex/trang-1-0-tab-1.chn").get();
            } catch (IOException e) {
                e.printStackTrace();
            }

            Elements trTable = doc.select("table[id=GirdTable2]");
            if (trTable.size() > 0) {
                for (Element s : trTable) {
                    Elements rowTable = s.getElementsByTag("tr");
                    for (Element tr : rowTable) {

                        if (tr.toString().contains("style=\"font-family: Arial; font-weight: normal; background-color: #f2f2f2;height:30px;padding-right:5px\"")
                                || tr.toString().contains("style=\"font-family: Arial; font-size: 10px; font-weight: normal; background-color: #FFF;height:30px;padding-right:5px\"")) {
                            Elements colTable = tr.getElementsByTag("td");
                            InforStockIndex objInforStockIndex = creatStockIndexObject(colTable);
                            listInforStockIndex.add(objInforStockIndex);
                            Log.d("InforBuySell: ", colTable.toString());
                        }
                    }
                }
                Log.d("InforBuySell: ", "test");
            }
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (!SharedPreference.getInstance().getBoolean("exchange", true))
                        priceVolumeAgrementAdapter.updatePriceVolumeAgrementAdapter(getActivity(), getListInforStockIndexFull(listInforStockIndex, 10));
                }
            });
        }
    });
    //OK
    private Thread thread = new Thread(new Runnable() {

        @Override
        public void run() {
            Document doc = null;
            try {
                doc = Jsoup.connect("https://s.cafef.vn/lich-su-giao-dich-symbol-vnindex/trang-1-0-tab-2.chn").get();
            } catch (IOException e) {
                e.printStackTrace();
            }

            Elements trTable = doc.select("table.GirdTable");
            if (trTable.size() > 0) {
                for (Element s : trTable) {
                    Elements rowTable = s.getElementsByTag("tr");
                    for (Element tr : rowTable) {

                        if (tr.toString().contains("style=\"font-family: Arial; font-size: 10px; font-weight: normal; background-color: #f2f2f2\"")
                                || tr.toString().contains("style=\"font-family: Arial; font-size: 10px; font-weight: normal; background-color: #fff\"")) {
                            Elements colTable = tr.getElementsByTag("td");
                            InforBuySellStockIndex newObject = creatStockObject(colTable);
                            listInforBuySellStockIndex.add(newObject);
                            Log.d("InforBuySell: ", colTable.toString());
                        }
                    }
                }
            }
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    priceVolumeAgrementAdapter.notifyDataSetChanged();
                }
            });
            // Volume, value current
            Document doc1 = null;
            long averVolume10 = 0;
            long averVolume20 = 0;
            long averValue10 = 0;
            long averValue20 = 0;
            final InforVolumeValueStockIndex objInforVolumeValueStockIndex = new InforVolumeValueStockIndex();
            try {
                doc1 = Jsoup.connect("https://s.cafef.vn/lich-su-giao-dich-symbol-vnindex/trang-1-0-tab-1.chn").get();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Elements trTable1 = doc1.select("table[id=GirdTable2]");
            int countRow = 0;
            long volumeAver10 = 0;
            long volumeAver20 = 0;
            long valueAver10 = 0;
            long valueAver20 = 0;
            if (trTable1.size() > 0) {
                for (Element s : trTable1) {
                    Elements rowTable = s.getElementsByTag("tr");
                    for (Element tr : rowTable) {
                        if (tr.toString().contains("id=\"ContentPlaceHolder1_ctl03_rptData2_itemTR_0\"")) {
                            Elements colTable = tr.getElementsByTag("td");

                            for (int i = 0; i < colTable.size(); i++) {
                                Element item = colTable.get(i);
                                Log.d("Value:", item.select("td").toString());
                                objInforVolumeValueStockIndex.setValue(item.select("td").text(), i);
                            }
                            Log.d("VolumeValueStockIndex: ", colTable.toString());
                        } else {
                            Elements colTable = tr.getElementsByTag("td");
                            for (int i = 0; i < colTable.size(); i++) {
                                Element item = colTable.get(i);
                                Log.d("Value:", item.select("td").toString());
                                if (i == 4 && colTable.size() > 12) {
                                    countRow++;
                                    //caculate rate for volume
                                    if (countRow == 1) {
                                        Log.d("Value:", item.select("td").text() + " : " + objInforVolumeValueStockIndex.getVolumeClose());
                                        Integer prevVolume = Integer.parseInt(item.select("td").html().toString().replaceAll("&nbsp;", "").replaceAll(",", "").trim());
                                        Integer currentVolume;
                                        if (objInforVolumeValueStockIndex.getVolumeClose() != null) {
                                            currentVolume = Integer.parseInt(objInforVolumeValueStockIndex.getVolumeClose().replaceFirst("\\s++$", "").replaceAll(",", ""));
                                        } else {
                                            currentVolume = 0;
                                        }
                                        double rateVolume = (currentVolume - prevVolume) * 100.0 / currentVolume;
                                        objInforVolumeValueStockIndex.setVolumeRate(String.format("%2.02f", rateVolume) + "%");
                                    }
                                    if (countRow <= 10) {
                                        volumeAver10 = volumeAver10 + Integer.parseInt(item.select("td").html().toString().replaceAll("&nbsp;", "").replaceAll(",", "").trim());
                                        if (countRow == 10) {
                                            averVolume10 = volumeAver10 / 10;
                                            objInforVolumeValueStockIndex.setAverVolume10(averVolume10);
                                        }
                                    }
                                    if (countRow <= 20) {
                                        volumeAver20 = volumeAver20 + Integer.parseInt(item.select("td").html().toString().replaceAll("&nbsp;", "").replaceAll(",", "").trim());
                                        if (countRow == 19) {
                                            averVolume20 = volumeAver20 / 19;
                                            objInforVolumeValueStockIndex.setAverAverVolume20(averVolume20);
                                        }
                                    }
                                }
                                //Value
                                if (i == 5 && colTable.size() > 12) {
                                    if (countRow == 1) {
                                        Log.d("Value:", item.select("td").text() + " : " + objInforVolumeValueStockIndex.getVolumeClose());
                                        Long prevValue = Long.parseLong(item.select("td").html().toString().replaceAll("&nbsp;", "").replaceAll(",", "").trim());
                                        double currentValue;
                                        if (objInforVolumeValueStockIndex.getValueClose() != null) {
                                            currentValue = Long.valueOf(objInforVolumeValueStockIndex.getValueClose().replaceFirst("\\s++$", "").replaceAll(",", ""));
                                            double rateValue = (currentValue - prevValue) * 100.0 / currentValue;
                                            objInforVolumeValueStockIndex.setValueRate(String.format("%2.02f", rateValue) + "%");
                                        } else {
                                            currentValue = 0.0;
                                        }

                                    }
                                    if (countRow <= 10) {
                                        valueAver10 = valueAver10 + Long.parseLong(item.select("td").html().toString().replaceAll("&nbsp;", "").replaceAll(",", "").trim());
                                        if (countRow == 10) {
                                            averValue10 = valueAver10 / 10;
                                            objInforVolumeValueStockIndex.setAverValue10(averValue10);
                                        }
                                    }
                                    if (countRow <= 20) {
                                        valueAver20 = valueAver20 + Long.parseLong(item.select("td").html().toString().replaceAll("&nbsp;", "").replaceAll(",", "").trim());
                                        if (countRow == 19)
                                            averValue20 = valueAver20 / 19;
                                        objInforVolumeValueStockIndex.setAverValue20(averValue20);
                                    }
                                }
                            }
                        }
                        Log.d("Volume 20: ", String.valueOf(volumeAver10) + " : " + String.valueOf(volumeAver20));
                    }

                }
            }
            Singleton.getInstance().setObjInforVolumeValueStockIndex(objInforVolumeValueStockIndex);
            final Long finalAverVolume1 = averVolume10;
            final Long finalAverVolume2 = averVolume20;
            final Long finalAverValue2 = averValue20;
            final Long finalAverValue1 = averValue10;
            objInforVolumeValueStockIndex.setAverVolume10(averVolume10);
            objInforVolumeValueStockIndex.setAverValue10(averValue10);
            objInforVolumeValueStockIndex.setAverValue20(averValue20);
            objInforVolumeValueStockIndex.setAverAverVolume20(averVolume20);
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    txtDateTransit.setText(objInforVolumeValueStockIndex.getDateTransit());
                    txtIndex.setText(objInforVolumeValueStockIndex.getPriceClose() + "  " + objInforVolumeValueStockIndex.getRate());
                    if ("".equalsIgnoreCase(SharedPreference.getInstance().getString("averIndex", "")) || SharedPreference.getInstance().getString("averIndex", "").equalsIgnoreCase("10")) {
                        String number = finalAverValue1.toString().substring(0, finalAverValue1.toString().length() - 9);
                        double amount = Double.parseDouble(number);
                        String valuerAver = String.format("%,.0f", amount);
                        if (objInforVolumeValueStockIndex.getVolumeClose() != null) {
                            txtVolume.setText(objInforVolumeValueStockIndex.getVolumeClose().substring(0, objInforVolumeValueStockIndex.getVolumeClose().length() - 9) + "-TB10: " + finalAverVolume1.toString().substring(0, finalAverVolume1.toString().length() - 6)); //+ " TB 20: "+ finalAverVolume2.toString().substring(0,finalAverVolume2.toString().length()-6)
                        } else {
                            txtVolume.setText("0");
                        }
                        if (objInforVolumeValueStockIndex.getVolumeClose() != null) {
                            txtValue.setText(objInforVolumeValueStockIndex.getValueClose().substring(0, objInforVolumeValueStockIndex.getValueClose().length() - 13) + "-TB10: " + valuerAver.replace(".", ","));
                        } else {
                            txtValue.setText("0");
                        }
                    } else {
                        String number = finalAverValue2.toString().substring(0, finalAverValue2.toString().length() - 9);
                        double amount = Double.parseDouble(number);
                        String valuerAver = String.format("%,.0f", amount);
                        txtVolume.setText(objInforVolumeValueStockIndex.getVolumeClose().substring(0, objInforVolumeValueStockIndex.getVolumeClose().length() - 9) + "-TB20: " + finalAverVolume2.toString().substring(0, finalAverVolume2.toString().length() - 6)); //+ " TB 20: "+ finalAverVolume2.toString().substring(0,finalAverVolume2.toString().length()-6)
                        txtValue.setText(objInforVolumeValueStockIndex.getValueClose().substring(0, objInforVolumeValueStockIndex.getValueClose().length() - 13) + "-TB20: " + valuerAver.replace(".", ","));
                    }
                    NumberFormat f = NumberFormat.getInstance(); // Gets a NumberFormat with the default locale, you can specify a Locale as first parameter (like Locale.FRENCH)
                    double indexClose = 0;
                    double indexOpen = 0;
                    try {
                        if (objInforVolumeValueStockIndex.getPriceClose() != null) {
                            indexClose = f.parse(objInforVolumeValueStockIndex.getPriceClose().replaceAll(",", "").replace(".", ",")).doubleValue(); // myNumber now contains 20
                        } else {
                            indexClose = 0;
                        }
                        if (objInforVolumeValueStockIndex.getPriceOpen() != null) {
                            indexOpen = f.parse(objInforVolumeValueStockIndex.getPriceOpen().replaceAll(",", "").replace(".", ",")).doubleValue();
                        } else {
                            indexOpen = 0;
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    txtRateVolume.setText(objInforVolumeValueStockIndex.getVolumeRate());
                    txtRateValue.setText(objInforVolumeValueStockIndex.getValueRate());
//                    Log.d("Rate: ", objInforVolumeValueStockIndex.getRate() != null ? objInforVolumeValueStockIndex.getRate() :0);
                    if (objInforVolumeValueStockIndex.getRate() != null && objInforVolumeValueStockIndex.getRate().contains("-")) {
                        setColorIndex(Color.RED);
                    } else if (indexClose < indexOpen) {
                        setColorIndex(Color.GREEN);
                    } else {
                        setColorIndex(Color.GREEN);
//                            setColorIndex(Color.YELLOW);
                    }

                }
            });

        }
    });

    public static PriceVolumeAgreement newInstance() {
        Bundle args = new Bundle();
        PriceVolumeAgreement fragment = new PriceVolumeAgreement();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
//            InforVolumeValueStockIndex objInforVolumeValueStockIndex=new InforVolumeValueStockIndex();
//            objInforVolumeValueStockIndex=Singleton.getInstance().getObjInforVolumeValueStockIndex();
            Log.d("Object: ", "Object");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mMainView = inflater.inflate(R.layout.fragment_price_volume, null);
        mMainView.setTag("FragmentHome");
        txtDateTransit = mMainView.findViewById(R.id.txtDateTransit);
        txtIndex = mMainView.findViewById(R.id.txtIndex);
        txtValue = mMainView.findViewById(R.id.txtValue);
        txtVolume = mMainView.findViewById(R.id.txtVolume);
        txtRateVolume = mMainView.findViewById(R.id.txtRateVolume);
        txtRateValue = mMainView.findViewById(R.id.txtRateValue);
        recyclerView = mMainView.findViewById(R.id.recycleTop);
        lnStockIndex = mMainView.findViewById(R.id.lnStockIndex);
        lnBuySellStockIndex = mMainView.findViewById(R.id.lnBuySellStockIndex);
        mSwipeRefresh = mMainView.findViewById(R.id.mSwipeRefresh);
        mSwipeRefresh.setEnabled(false);
        initWebview();
        priceVolumeAgrementAdapter = new PriceVolumeAgrementAdapter(getActivity(), listInforBuySellStockIndex);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(priceVolumeAgrementAdapter);
        linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
//        if (thread.getState() == Thread.State.NEW) {
//            thread.start();
//        }
////        thread.start();
////        }else{
//        if (resetThread.getState() == Thread.State.NEW) {
//            resetThread.start();
//        }
////        resetThread.start();
////        }

        return mMainView;
    }

    private String urlWeb = "https://s.cafef.vn/lich-su-giao-dich-symbol-vnindex/trang-1-0-tab-2.chn";
    private ProgressDialog progressDialog;

    boolean loadingFinished = true;
    boolean redirect = false;

    private void initWebview() {
        progressDialog = ProgressDialog.show(getContext(), "Loading", "Please wait...", true);
        progressDialog.setCancelable(false);
        if (SharedPreference.getInstance().getBoolean("exchange", true)) { //Default
            urlWeb = "https://s.cafef.vn/lich-su-giao-dich-symbol-vnindex/trang-1-0-tab-2.chn";
            lnBuySellStockIndex.setVisibility(View.VISIBLE);
            lnStockIndex.setVisibility(View.GONE);
        } else {
            urlWeb = "https://s.cafef.vn/lich-su-giao-dich-symbol-vnindex/trang-1-0-tab-1.chn";
            lnBuySellStockIndex.setVisibility(View.GONE);
            lnStockIndex.setVisibility(View.VISIBLE);
        }
        try {
            final WebView browser = new WebView(getContext());
            browser.setVisibility(View.INVISIBLE);
            browser.setLayerType(View.LAYER_TYPE_NONE, null);
            browser.getSettings().setJavaScriptEnabled(true);
            browser.getSettings().setBlockNetworkImage(true);
            browser.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
            browser.getSettings().setDomStorageEnabled(true);
            browser.addJavascriptInterface(new JSHtmlInterface(), "JSBridge");
            browser.loadUrl(urlWeb);
            browser.setWebViewClient(new WebViewClient() {

                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                @Override
                public boolean shouldOverrideUrlLoading(
                        WebView view, WebResourceRequest request) {
                    if (!loadingFinished) {
                        redirect = true;
                    }

                    loadingFinished = false;
                    browser.loadUrl(request.getUrl().toString());
                    return true;
                }

                @Override
                public void onPageStarted(
                        WebView view, String url, Bitmap favicon) {
                    super.onPageStarted(view, url, favicon);
                    loadingFinished = false;
                    //SHOW LOADING IF IT ISNT ALREADY VISIBLE
                }

                @Override
                public void onPageFinished(WebView view, String url) {
                    if (!redirect) {
                        loadingFinished = true;
                        browser.setVisibility(View.VISIBLE);
                        browser.loadUrl("javascript:window.JSBridge.showHTML('<html>'+document.getElementsByTagName('html')[0].innerHTML+'</html>');");
                        progressDialog.dismiss();
                        //HIDE LOADING IT HAS FINISHED
                    } else {
                        redirect = false;
                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class JSHtmlInterface {
        @android.webkit.JavascriptInterface
        public void showHTML(String html) {
            final String htmlContent = html;

            uiHandler.post(
                    new Runnable() {
                        @Override
                        public void run() {
                            Document doc = Jsoup.parse(htmlContent);
                            Elements trTable = doc.select("table[class=owner-contents-table]");
                            if (trTable.size() > 0) {
                                for (Element s : trTable) {
                                    Elements rowTable = s.getElementsByTag("tr");
                                    for (Element tr : rowTable) {

                                        if (tr.toString().contains("class=\"oddOwner\"")
                                                || tr.toString().contains("class=\"evenOwner\"")) {
                                            Elements colTable = tr.getElementsByTag("td");
                                            if (SharedPreference.getInstance().getBoolean("exchange", true)) { //Default
                                                InforBuySellStockIndex newObject = creatStockObject(colTable);
                                                listInforBuySellStockIndex.add(newObject);
                                                Log.d("InforBuySell: ", colTable.toString());
                                            } else {
                                                InforStockIndex objInforStockIndex = creatStockIndexObject(colTable);
                                                listInforStockIndex.add(objInforStockIndex);
                                                Log.d("Index value daily: ", colTable.toString());
                                            }

                                            Log.d("InforBuySell: ", colTable.toString());
                                        }
                                    }
                                }
                            }
                            //Update index history.
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (SharedPreference.getInstance().getBoolean("exchange", true)) {
                                        priceVolumeAgrementAdapter.notifyDataSetChanged();
                                    } else {
                                        priceVolumeAgrementAdapter.updatePriceVolumeAgrementAdapter(getActivity(), getListInforStockIndexFull(listInforStockIndex, 10));
                                    }

                                }
                            });
                            // get value of current day transition.
                            final InforVolumeValueStockIndex objInforVolumeValueStockIndex = new InforVolumeValueStockIndex();
                            Elements pIndexClose = doc.select("p[id=VN-Index]");
                            objInforVolumeValueStockIndex.setValueIndexClose(pIndexClose.text());
                            Elements pValueIndex = doc.select("p[id=GTGD-VN-Index]");
                            objInforVolumeValueStockIndex.setValueIndex(pValueIndex.text());
                            // Volume, value current
//                            Document doc1 = null;
//                            long averVolume10 = 0;
//                            long averVolume20 = 0;
//                            long averValue10 = 0;
//                            long averValue20 = 0;
//                            final InforVolumeValueStockIndex objInforVolumeValueStockIndex = new InforVolumeValueStockIndex();
//                            try {
//                                doc1 = Jsoup.connect("http://s.cafef.vn/Lich-su-giao-dich-VNINDEX-1.chn#data").get();
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }
//                            Elements trTable1 = doc1.select("table[id=GirdTable2]");
//                            int countRow = 0;
//                            long volumeAver10 = 0;
//                            long volumeAver20 = 0;
//                            long valueAver10 = 0;
//                            long valueAver20 = 0;
//                            if (trTable1.size() > 0) {
//                                for (Element s : trTable1) {
//                                    Elements rowTable = s.getElementsByTag("tr");
//                                    for (Element tr : rowTable) {
//                                        if (tr.toString().contains("id=\"ContentPlaceHolder1_ctl03_rptData2_itemTR_0\"")) {
//                                            Elements colTable = tr.getElementsByTag("td");
//
//                                            for (int i = 0; i < colTable.size(); i++) {
//                                                Element item = colTable.get(i);
//                                                Log.d("Value:", item.select("td").toString());
//                                                objInforVolumeValueStockIndex.setValue(item.select("td").text(), i);
//                                            }
//                                            Log.d("VolumeValueStockIndex: ", colTable.toString());
//                                        } else {
//                                            Elements colTable = tr.getElementsByTag("td");
//                                            for (int i = 0; i < colTable.size(); i++) {
//                                                Element item = colTable.get(i);
//                                                Log.d("Value:", item.select("td").toString());
//                                                if (i == 4 && colTable.size() > 12) {
//                                                    countRow++;
//                                                    //caculate rate for volume
//                                                    if (countRow == 1) {
//                                                        Log.d("Value:", item.select("td").text() + " : " + objInforVolumeValueStockIndex.getVolumeClose());
//                                                        Integer prevVolume = Integer.parseInt(item.select("td").html().toString().replaceAll("&nbsp;", "").replaceAll(",", "").trim());
//                                                        Integer currentVolume;
//                                                        if (objInforVolumeValueStockIndex.getVolumeClose() != null) {
//                                                            currentVolume = Integer.parseInt(objInforVolumeValueStockIndex.getVolumeClose().replaceFirst("\\s++$", "").replaceAll(",", ""));
//                                                        } else {
//                                                            currentVolume = 0;
//                                                        }
//                                                        double rateVolume = (currentVolume - prevVolume) * 100.0 / currentVolume;
//                                                        objInforVolumeValueStockIndex.setVolumeRate(String.format("%2.02f", rateVolume) + "%");
//                                                    }
//                                                    if (countRow <= 10) {
//                                                        volumeAver10 = volumeAver10 + Integer.parseInt(item.select("td").html().toString().replaceAll("&nbsp;", "").replaceAll(",", "").trim());
//                                                        if (countRow == 10) {
//                                                            averVolume10 = volumeAver10 / 10;
//                                                            objInforVolumeValueStockIndex.setAverVolume10(averVolume10);
//                                                        }
//                                                    }
//                                                    if (countRow <= 20) {
//                                                        volumeAver20 = volumeAver20 + Integer.parseInt(item.select("td").html().toString().replaceAll("&nbsp;", "").replaceAll(",", "").trim());
//                                                        if (countRow == 19) {
//                                                            averVolume20 = volumeAver20 / 19;
//                                                            objInforVolumeValueStockIndex.setAverAverVolume20(averVolume20);
//                                                        }
//                                                    }
//                                                }
//                                                //Value
//                                                if (i == 5 && colTable.size() > 12) {
//                                                    if (countRow == 1) {
//                                                        Log.d("Value:", item.select("td").text() + " : " + objInforVolumeValueStockIndex.getVolumeClose());
//                                                        Long prevValue = Long.parseLong(item.select("td").html().toString().replaceAll("&nbsp;", "").replaceAll(",", "").trim());
//                                                        double currentValue;
//                                                        if (objInforVolumeValueStockIndex.getValueClose() != null) {
//                                                            currentValue = Long.valueOf(objInforVolumeValueStockIndex.getValueClose().replaceFirst("\\s++$", "").replaceAll(",", ""));
//                                                            double rateValue = (currentValue - prevValue) * 100.0 / currentValue;
//                                                            objInforVolumeValueStockIndex.setValueRate(String.format("%2.02f", rateValue) + "%");
//                                                        } else {
//                                                            currentValue = 0.0;
//                                                        }
//
//                                                    }
//                                                    if (countRow <= 10) {
//                                                        valueAver10 = valueAver10 + Long.parseLong(item.select("td").html().toString().replaceAll("&nbsp;", "").replaceAll(",", "").trim());
//                                                        if (countRow == 10) {
//                                                            averValue10 = valueAver10 / 10;
//                                                            objInforVolumeValueStockIndex.setAverValue10(averValue10);
//                                                        }
//                                                    }
//                                                    if (countRow <= 20) {
//                                                        valueAver20 = valueAver20 + Long.parseLong(item.select("td").html().toString().replaceAll("&nbsp;", "").replaceAll(",", "").trim());
//                                                        if (countRow == 19)
//                                                            averValue20 = valueAver20 / 19;
//                                                        objInforVolumeValueStockIndex.setAverValue20(averValue20);
//                                                    }
//                                                }
//                                            }
//                                        }
//                                        Log.d("Volume 20: ", String.valueOf(volumeAver10) + " : " + String.valueOf(volumeAver20));
//                                    }
//
//                                }
//                            }

//                            Singleton.getInstance().setObjInforVolumeValueStockIndex(objInforVolumeValueStockIndex);
//                            final Long finalAverVolume1 = averVolume10;
//                            final Long finalAverVolume2 = averVolume20;
//                            final Long finalAverValue2 = averValue20;
//                            final Long finalAverValue1 = averValue10;
//                            objInforVolumeValueStockIndex.setAverVolume10(averVolume10);
//                            objInforVolumeValueStockIndex.setAverValue10(averValue10);
//                            objInforVolumeValueStockIndex.setAverValue20(averValue20);
//                            objInforVolumeValueStockIndex.setAverAverVolume20(averVolume20);
//                            getActivity().runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    txtDateTransit.setText(objInforVolumeValueStockIndex.getDateTransit());
//                                    txtIndex.setText(objInforVolumeValueStockIndex.getPriceClose() + "  " + objInforVolumeValueStockIndex.getRate());
//                                    if ("".equalsIgnoreCase(SharedPreference.getInstance().getString("averIndex", "")) || SharedPreference.getInstance().getString("averIndex", "").equalsIgnoreCase("10")) {
//                                        String number = finalAverValue1.toString().substring(0, finalAverValue1.toString().length() - 9);
//                                        double amount = Double.parseDouble(number);
//                                        String valuerAver = String.format("%,.0f", amount);
//                                        if (objInforVolumeValueStockIndex.getVolumeClose() != null) {
//                                            txtVolume.setText(objInforVolumeValueStockIndex.getVolumeClose().substring(0, objInforVolumeValueStockIndex.getVolumeClose().length() - 9) + "-TB10: " + finalAverVolume1.toString().substring(0, finalAverVolume1.toString().length() - 6)); //+ " TB 20: "+ finalAverVolume2.toString().substring(0,finalAverVolume2.toString().length()-6)
//                                        } else {
//                                            txtVolume.setText("0");
//                                        }
//                                        if (objInforVolumeValueStockIndex.getVolumeClose() != null) {
//                                            txtValue.setText(objInforVolumeValueStockIndex.getValueClose().substring(0, objInforVolumeValueStockIndex.getValueClose().length() - 13) + "-TB10: " + valuerAver.replace(".", ","));
//                                        } else {
//                                            txtValue.setText("0");
//                                        }
//                                    } else {
//                                        String number = finalAverValue2.toString().substring(0, finalAverValue2.toString().length() - 9);
//                                        double amount = Double.parseDouble(number);
//                                        String valuerAver = String.format("%,.0f", amount);
//                                        txtVolume.setText(objInforVolumeValueStockIndex.getVolumeClose().substring(0, objInforVolumeValueStockIndex.getVolumeClose().length() - 9) + "-TB20: " + finalAverVolume2.toString().substring(0, finalAverVolume2.toString().length() - 6)); //+ " TB 20: "+ finalAverVolume2.toString().substring(0,finalAverVolume2.toString().length()-6)
//                                        txtValue.setText(objInforVolumeValueStockIndex.getValueClose().substring(0, objInforVolumeValueStockIndex.getValueClose().length() - 13) + "-TB20: " + valuerAver.replace(".", ","));
//                                    }
//                                    NumberFormat f = NumberFormat.getInstance(); // Gets a NumberFormat with the default locale, you can specify a Locale as first parameter (like Locale.FRENCH)
//                                    double indexClose = 0;
//                                    double indexOpen = 0;
//                                    try {
//                                        if (objInforVolumeValueStockIndex.getPriceClose() != null) {
//                                            indexClose = f.parse(objInforVolumeValueStockIndex.getPriceClose().replaceAll(",", "").replace(".", ",")).doubleValue(); // myNumber now contains 20
//                                        } else {
//                                            indexClose = 0;
//                                        }
//                                        if (objInforVolumeValueStockIndex.getPriceOpen() != null) {
//                                            indexOpen = f.parse(objInforVolumeValueStockIndex.getPriceOpen().replaceAll(",", "").replace(".", ",")).doubleValue();
//                                        } else {
//                                            indexOpen = 0;
//                                        }
//                                    } catch (ParseException e) {
//                                        e.printStackTrace();
//                                    }
//                                    txtRateVolume.setText(objInforVolumeValueStockIndex.getVolumeRate());
//                                    txtRateValue.setText(objInforVolumeValueStockIndex.getValueRate());
////                    Log.d("Rate: ", objInforVolumeValueStockIndex.getRate() != null ? objInforVolumeValueStockIndex.getRate() :0);
//                                    if (objInforVolumeValueStockIndex.getRate() != null && objInforVolumeValueStockIndex.getRate().contains("-")) {
//                                        setColorIndex(Color.RED);
//                                    } else if (indexClose < indexOpen) {
//                                        setColorIndex(Color.GREEN);
//                                    } else {
//                                        setColorIndex(Color.GREEN);
////                            setColorIndex(Color.YELLOW);
//                                    }
//
//                                }
//                            });
////                            adapter.notifyDataSetChanged();
                        }
                    }
            );
        }
    }


    private void setColorIndex(int color) {
        txtIndex.setTextColor(color);
        txtDateTransit.setTextColor(color);
        txtValue.setTextColor(color);
        txtVolume.setTextColor(color);
        txtRateValue.setTextColor(color);
        txtRateVolume.setTextColor(color);
    }

    private InforBuySellStockIndex creatStockObject(Elements td) {
        InforBuySellStockIndex objInforBuySellStockIndex = new InforBuySellStockIndex();
        for (int i = 0; i < td.size(); i++) {
            Element item = td.get(i);
            Log.d("Value:", item.select("td").toString());
            objInforBuySellStockIndex.setValue(item.select("td").text(), i);
        }
        return objInforBuySellStockIndex;
    }

    private InforStockIndex creatStockIndexObject(Elements td) {
        InforStockIndex objInforStockIndex = new InforStockIndex();
        for (int i = 0; i < td.size(); i++) {
            Element item = td.get(i);
            Log.d("Value:", item.select("td").toString());
            objInforStockIndex.setValue(item.select("td").text(), i);
        }
        return objInforStockIndex;
    }

    private ArrayList<InforStockIndex> getListInforStockIndexFull(ArrayList<InforStockIndex> listInforStockIndex, int numAver) {
        ArrayList<InforStockIndex> listTemp = new ArrayList<>();
        if (listInforStockIndex.size() > numAver) {
            for (int i = 0; i < listInforStockIndex.size(); i++) {
                InforStockIndex objListInforIndex = new InforStockIndex();
                objListInforIndex = getObjectIndexFull(listInforStockIndex, listInforStockIndex.get(i).getDateTransit(), i);
                listTemp.add(objListInforIndex);
            }
        }
        return listTemp;
    }

    private InforStockIndex getObjectIndexFull(ArrayList<InforStockIndex> listInforIndex, String dateTransaction, int posObj) {
        InforStockIndex tempObj = new InforStockIndex();
        tempObj = listInforIndex.get(posObj);
        String num = listInforIndex.get(posObj).getValueTransit().replaceAll("\\s", "").replaceAll(",", "");
        double number = Double.parseDouble(num);
        tempObj = listInforIndex.get(posObj);
        double totalValue10 = 0.0;
        for (int i = posObj; i < 10; i++) { //i<listInforIndex.size() && i < posObj + 9 &&
            totalValue10 = totalValue10 + Double.parseDouble(listInforIndex.get(i).getValueTransit().replaceAll("\\s", "").replaceAll(",", ""));
        }
        tempObj.setValue(String.valueOf(totalValue10), 11);
        double averValue10 = totalValue10 / 10;
        tempObj.setValue(String.valueOf((Double.parseDouble(listInforIndex.get(posObj).getValueTransit().replaceAll("\\s", "").replaceAll(",", "")) - averValue10) * 100 / averValue10), 12);
        rateValue10 = totalValue10 / 10;
        return tempObj;
    }


    public void onChangeHomeTab() {
        initWebview();
        if (SharedPreference.getInstance().getBoolean("exchange", true)) { //Default
            priceVolumeAgrementAdapter = new PriceVolumeAgrementAdapter(getActivity(), listInforBuySellStockIndex);
            lnBuySellStockIndex.setVisibility(View.VISIBLE);
            lnStockIndex.setVisibility(View.GONE);
        } else {
            lnBuySellStockIndex.setVisibility(View.GONE);
            lnStockIndex.setVisibility(View.VISIBLE);
            priceVolumeAgrementAdapter.updatePriceVolumeAgrementAdapter(getActivity(), getListInforStockIndexFull(listInforStockIndex, 10));
        }
        priceVolumeAgrementAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(priceVolumeAgrementAdapter);
    }
}
