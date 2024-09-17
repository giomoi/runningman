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

    public static PriceVolumeAgreement newInstance() {
        Bundle args = new Bundle();
        PriceVolumeAgreement fragment = new PriceVolumeAgreement();
        fragment.setArguments(args);
        return fragment;
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
        priceVolumeAgrementAdapter = new PriceVolumeAgrementAdapter(getActivity(), listInforBuySellStockIndex);
        recyclerView.setAdapter(priceVolumeAgrementAdapter);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
//        linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        initWebview();
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
                            txtValue.setText(pValueIndex.text());
                            txtIndex.setText(pIndexClose.text());
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
