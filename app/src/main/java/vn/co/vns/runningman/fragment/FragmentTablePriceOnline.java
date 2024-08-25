package vn.co.vns.runningman.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.regex.Pattern;

import vn.co.vns.runningman.R;
import vn.co.vns.runningman.activity.MainActivity;
import vn.co.vns.runningman.adapter.PriceOnlineAdapter;
import vn.co.vns.runningman.interfaces.RecyclerItemClickListener;
import vn.co.vns.runningman.object.StockIndex;
import vn.co.vns.runningman.object.StockObject;
import vn.co.vns.runningman.util.Constant;
import vn.co.vns.runningman.util.SharedPreference;

import static vn.co.vns.runningman.util.Constant.optionPriceboard;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by thanhnv on 12/9/16.
 */
public class FragmentTablePriceOnline extends Fragment {
    private RecyclerView listPriceOnline;
    private TextView txtCodeStock;
    private TextView txtTopCK;
    private TextView txtCKName;
    private TextView txtGapPrice;
    private TextView txtPointIndex, txtRateIndex, txtTotaVolume, txtTotalValue;

    private String TAG = getClass().getSimpleName().toUpperCase();
    private ArrayList<String> listCK = new ArrayList<>();
    private ProgressDialog mProgressDialog;
    private WebView mainWebview;
    private String fullHtml = "";
    private boolean isRunningView = false;
    private boolean isBindData = false;
    private PriceOnlineAdapter mainAdapter;
    private String urlString = Constant.URL_OTHER_HSX;
    private ArrayList<StockObject> listStockTransition = new ArrayList<>();
    private String strSpecial = "";

    private Thread resetThread = new Thread(new Runnable() {
        @Override
        public void run() {
            try {
                while (!isRunningView) {
                    Thread.sleep(7000);
                    if (getUserVisibleHint()) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Log.i("Data", "Refreshing..." + urlString);
                                mainWebview.getSettings().setJavaScriptEnabled(true);
                                mainWebview.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
                                mainWebview.getSettings().setDomStorageEnabled(true);
                                mainWebview.setWebViewClient(new WebViewClient());
                                mainWebview.loadUrl("javascript:window.HTMLOUT.processHTML('<head>'+document.getElementsByTagName('html')[0].innerHTML+'</head>');");
                            }
                        });
                        if (checkOverTimeInDay()) {
                            resetThread.interrupt();
                        }
                    }
                }
            } catch (Exception e) {
                resetThread.interrupt();
                e.printStackTrace();

            }
        }
    });

    private void initView(View mMainView) {
        listPriceOnline = (RecyclerView) mMainView.findViewById(R.id.listPriceOnline);
        txtCodeStock = (TextView) mMainView.findViewById(R.id.txtCodeStock);
        txtTopCK = (TextView) mMainView.findViewById(R.id.txtTopCK);
        txtCKName = (TextView) mMainView.findViewById(R.id.txtCKName);
        txtGapPrice = (TextView) mMainView.findViewById(R.id.txtGapPrice);
        txtPointIndex = (TextView) mMainView.findViewById(R.id.txtPointIndex);
        txtPointIndex = (TextView) mMainView.findViewById(R.id.txtPointIndex);
        txtTotalValue = (TextView) mMainView.findViewById(R.id.txtTotalValue);
        txtTotaVolume = (TextView) mMainView.findViewById(R.id.txtTotaVolume);
        txtRateIndex = (TextView) mMainView.findViewById(R.id.txtRateIndex);

    }

    public static FragmentTablePriceOnline newInstance(String urlString) {
        Bundle args = new Bundle();
        args.putString("StringURL", urlString);
        FragmentTablePriceOnline fragment = new FragmentTablePriceOnline();
        fragment.setArguments(args);
        return fragment;
    }

    public static void changeTitleView(Activity activity) {
        if (SharedPreference.getInstance().getBoolean("ho", true)) { //Default
            MainActivity.appTitle.setText(activity.getString(R.string.txt_title_vuong));
        } else {
            MainActivity.appTitle.setText(activity.getString(R.string.txt_title_nhat));
        }
    }

    private void executeHtml() {
        isBindData = true;
        final ArrayList<StockObject> result = new ArrayList<>();
        final ArrayList<StockIndex> resultIndex = new ArrayList<>();
        try {
            Document doc = Jsoup.parse(fullHtml);
            if (SharedPreference.getInstance().getString("priceTable", "cafef").equalsIgnoreCase("cafef")) {
                Elements trTable = doc.select("table#stock-price-table-fix");
                Elements tbody = doc.select("tbody#stock-price-table-body");
                if (tbody.size() > 0) {
                    for (Element s : tbody) {
                        Elements rowTable = s.getElementsByTag("tr");
                        for (Element tr : rowTable) {
                            if (tr.toString().contains("price-item stock")) {
                                Elements colTable = tr.getElementsByTag("td");
                                StockObject newObject = creatStockObjectCafef(colTable);
                                if (!"".equalsIgnoreCase(newObject.getCodeStock())) {
                                    showNotificationCode(newObject);
                                    //Filter only stock 3 character
                                    if (newObject.getCodeStock().length() <= 4)
                                        result.add(newObject);
                                }
                            }
                        }
                        if (listStockTransition.size() == 0) {
                            listStockTransition = result;
                        }
                    }
                }
                //Index
//                Elements trTableIndex = doc.select("table.dataTable");
                StockIndex newObjectIndex;
                if (SharedPreference.getInstance().getBoolean("ho", true)) { //Default
                    Element vnindex = doc.select("span.idx_1").first();
                    String index = vnindex.text();
                    Element rate_vn_index1 = doc.select("span.chgidx_1").first();
                    String rate_index1 = rate_vn_index1.text();
                    Element vol_vn_index1 = doc.select("span.vol_1").first();
                    String vol_index1 = vol_vn_index1.text();
                    Element val_vn_index1 = doc.select("span.val_1").first();
                    String val_index1 = val_vn_index1.text();

                    newObjectIndex = new StockIndex("VN-INDEX", index, rate_index1, vol_index1, val_index1);
                } else {
                    Element vnindex = doc.select("span.idx_2").first();
                    String index = vnindex.text();
                    Element rate_vn_index1 = doc.select("span.chgidx_2").first();
                    String rate_index1 = rate_vn_index1.text();
                    Element vol_vn_index1 = doc.select("span.vol_2").first();
                    String vol_index1 = vol_vn_index1.text();
                    Element val_vn_index1 = doc.select("span.val_2").first();
                    String val_index1 = val_vn_index1.text();

                    newObjectIndex = new StockIndex("HN-INDEX", index, rate_index1, vol_index1, val_index1);
                }
                resultIndex.add(newObjectIndex);
            } else {
                Elements trTable = doc.select("table#banggia-khop-lenh");
                if (trTable.size() > 0) {
                    for (Element s : trTable) {
                        Elements rowTable = s.getElementsByTag("tr");
                        for (Element tr : rowTable) {
//                            if (tr.toString().contains("class=\"invisible\"")) {
//                            if (tr.toString().contains("ListItem")) {
                                Elements colTable = tr.getElementsByTag("td");
                                StockObject newObject = creatStockObject(colTable);
                                if (!"".equalsIgnoreCase(newObject.getCodeStock())) {
                                    showNotificationCode(newObject);
                                    //Filter only stock 3 character
                                    if (newObject.getCodeStock().length() <= 4)
                                    result.add(newObject);
                                }
//                            }
                        }
                        if (listStockTransition.size() == 0) {
                            listStockTransition = result;
                        }
                    }
                }
                //Index
                Elements trTableIndex = doc.select("table.table-index");
                if (trTableIndex.size() > 0) {
                    for (Element s : trTableIndex) {
                        Elements rowTableIndex = s.getElementsByTag("tr");
                        for (Element tr : rowTableIndex) {
                            if ((SharedPreference.getInstance().getBoolean("ho", true) && tr.toString().contains("VNINDEX")) || (!SharedPreference.getInstance().getBoolean("ho", true) && tr.toString().contains("HNXIndex"))) {
                                Elements colTableIndex = tr.getElementsByTag("td");
                                String per_num = colTableIndex.get(2).select("span.num").text() + "(" + colTableIndex.get(2).select("span.per").text() + ")";
//                                for (Element span : colTableIndex.select("span")) {
//                                    per_num = per_num + span.text();
//                                }
                                StockIndex newObjectIndex = new StockIndex(colTableIndex.get(0).text(), colTableIndex.get(1).text(), per_num, colTableIndex.get(3).text(), colTableIndex.get(4).text());
                                resultIndex.add(newObjectIndex);
                            }
                        }
                    }
                }
            }
            Log.d("Index: ", resultIndex.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                int typIndex = (urlString != null && urlString.contains("Hnx")) ? 2 : 0;
                setIndex(resultIndex, typIndex);
                if (SharedPreference.getInstance().getInt("orderby", Constant.SORT_TIKER) == Constant.SORT_RATE) {
                    sortStockRate(result);
                    txtCodeStock.setTextColor(getResources().getColor(R.color.white));
                    txtGapPrice.setTextColor(getResources().getColor(R.color.green));
                } else if (SharedPreference.getInstance().getInt("orderby", Constant.SORT_TIKER) == Constant.SORT_PRIORITY) {
                    //Sort by priority
//                  mainAdapter.setListSortPriority(result,Constant.optionPriceboard);
                    sortStockPriority(result);
                    Log.d("Sort Priority: ", result.get(0).getCodeStock() + " : " + result.get(0).getPriorityOrder());
                } else {
                    sortStockName(result);
                    txtCodeStock.setTextColor(getResources().getColor(R.color.green));
                    txtGapPrice.setTextColor(getResources().getColor(R.color.white));
                }
//                if(optionPriceboard== optionPriceboard && !listItem.get(position).getCodeStock().equalsIgnoreCase("CTS")) {
                // Remove item CTS and VDS
//                }
                mainAdapter.setListItem(result, optionPriceboard);
                if (mProgressDialog != null) mProgressDialog.dismiss();
                isBindData = false;
            }
        });

    }

    private void setIndex(ArrayList<StockIndex> listIndex, int typeIndex) {
        if (listIndex.size() > 0) {
            txtCKName.setText(listIndex.get(typeIndex).getNameIndex());
            txtPointIndex.setText(listIndex.get(typeIndex).getPointIndex());
            txtRateIndex.setText(listIndex.get(typeIndex).getRateIndex());
            if (listIndex.get(typeIndex).getWeighIndex().length() > 8) {
                txtTotaVolume.setText("KL: " + listIndex.get(typeIndex).getWeighIndex().substring(0, listIndex.get(typeIndex).getWeighIndex().length() - 8) + " triệu");
                if (SharedPreference.getInstance().getString("priceTable", "cafef").equalsIgnoreCase("cafef")) {
                    txtTotalValue.setText("GT: " + listIndex.get(typeIndex).getValueIndex().substring(0, listIndex.get(typeIndex).getValueIndex().length() - 3) + " tỷ VND");
                } else {
                    txtTotalValue.setText("GT: " + listIndex.get(typeIndex).getValueIndex().substring(0, listIndex.get(typeIndex).getValueIndex().length() - 4) + " tỷ VND");
                }
            }
            String[] rateString = listIndex.get(typeIndex).getRateIndex().split("\\(");
            Log.d("Empty:", rateString[0]);
            Float ratePoint = Float.parseFloat((rateString[0].trim() != null && !rateString[0].trim().isEmpty() && !rateString[0].trim().equals("null")) ? rateString[0].trim() : "0");
            if (ratePoint > 0) {
                setIndexColor(getResources().getColor(R.color.green));
            } else if (ratePoint < 0) {
                setIndexColor(getResources().getColor(R.color.red));
            } else {
                setIndexColor(getResources().getColor(R.color.yellow));
            }
        }
    }

    private void setIndexColor(int color) {
        txtPointIndex.setTextColor(color);
        txtRateIndex.setTextColor(color);
    }

    private void sortStockRate(ArrayList<StockObject> listStock) {
        Collections.sort(listStock, new Comparator<StockObject>() {
            @Override
            public int compare(StockObject o1, StockObject o2) {
                String rate1 = o1.getRate().replace("%", "");
                String rate2 = o2.getRate().replace("%", "");
                double floatRate1 = 0.0;
                double floatRate2 = 0.0;
                if(!rate1.isEmpty()){
                    floatRate1 = Double.valueOf(rate1);
                }
                if(!rate2.isEmpty()){
                    floatRate2 = Double.valueOf(rate2);
                }
                return Double.compare(floatRate2, floatRate1);
            }
        });
    }

    private void sortStockPriority(ArrayList<StockObject> listStock) {
        Collections.sort(listStock, new Comparator<StockObject>() {
            @Override
            public int compare(StockObject o1, StockObject o2) {
                return Float.compare(o2.getPriorityOrder(), o1.getPriorityOrder());
            }
        });
    }

    private void sortStockName(ArrayList<StockObject> listStock) {
        Collections.sort(listStock, new CustomComparator());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View mMainView = inflater.inflate(R.layout.fragment_table_price_online, null);
        mMainView.setTag(TAG);
        initView(mMainView);
        buildView();
        MainActivity.layoutPriceboard.setVisibility(View.VISIBLE);
        if (urlString != null && urlString.contains("Hnx")) {
            optionPriceboard = "Hnx";
        } else {
            optionPriceboard = "HostcStockBoard";
        }
        if (getUserVisibleHint()) {
            if (mProgressDialog == null) {
                mProgressDialog = new ProgressDialog(getContext());
                mProgressDialog.setMessage("Loading...");
                mProgressDialog.setCancelable(false);
                mProgressDialog.show();
            }
        }

        return mMainView;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            MainActivity.layoutPriceboard.setVisibility(View.VISIBLE);
            if (urlString != null && urlString.contains("Hnx")) {
                optionPriceboard = "Hnx";
            } else {
                optionPriceboard = "HostcStockBoard";
            }
            if (mProgressDialog == null && getContext() != null) {
                mProgressDialog = new ProgressDialog(getContext());
                mProgressDialog.setMessage("Loading...");
                mProgressDialog.setCancelable(false);
                mProgressDialog.show();
            }
        }
    }

    private void buildView() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            urlString = bundle.getString("StringURL");
        }
        mainWebview = new WebView(getContext());
        mainWebview.getSettings().setJavaScriptEnabled(true);
        mainWebview.setWebChromeClient(new WebChromeClient());
        mainWebview.getSettings().setDomStorageEnabled(true);
        mainWebview.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
//                if (resetThread.getState() == Thread.State.NEW) {
//                    resetThread.start();
//                }
                if (!resetThread.isAlive()) {
                    resetThread.start();
                }
            }
        });
        mainWebview.loadUrl(urlString);
        mainWebview.addJavascriptInterface(new MyJavaScriptInterface(), "HTMLOUT");

        //
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        listPriceOnline.setLayoutManager(mLayoutManager);
        mainAdapter = new PriceOnlineAdapter(getContext());
        listPriceOnline.setAdapter(mainAdapter);

        txtCodeStock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreference.getInstance().putInt("orderby", Constant.SORT_TIKER);
                sortStockName(listStockTransition);
                mainAdapter.setListItem(listStockTransition, optionPriceboard);
                txtCodeStock.setTextColor(getResources().getColor(R.color.green));
                txtGapPrice.setTextColor(getResources().getColor(R.color.white));
            }
        });

        txtGapPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreference.getInstance().putInt("orderby", Constant.SORT_RATE);
                sortStockRate(listStockTransition);
                mainAdapter.setListItem(listStockTransition, optionPriceboard);
                txtCodeStock.setTextColor(getResources().getColor(R.color.white));
                txtGapPrice.setTextColor(getResources().getColor(R.color.green));
            }
        });

        listPriceOnline.addOnItemTouchListener(
                new RecyclerItemClickListener(getContext(), listPriceOnline, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        // do whatever
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                        // do whatever
                        resetThread.interrupt();
                        resetThread.stop();
                        Log.d("Thread: ", "Stop");
                    }
                })
        );

    }

    private boolean checkOverTimeInDay() {
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd 14:45:00");
            String dateString = format.format(new Date());
            format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            Date date = format.parse(dateString);
            return ((new Date()).getTime() > date.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }

    private StockObject creatStockObject(Elements td) {
        StockObject object = new StockObject();
        Elements span = td.select("span");
        for (int i = 0; i < span.size(); i++) {
//            Element item = td.get(i);
            object.setValue(span.get(i).text(), i);
        }
        return object;
    }

    private StockObject creatStockObjectCafef(Elements td) {
        StockObject object = new StockObject();
        Elements span = td.select("span");
        Elements div = td.select("div.price-change-pc");
        for (int i = 0; i < span.size(); i++) {
            object.setValueCafef((i != 20) ? span.get(i).text() : div.text(), i);
        }
        return object;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        resetThread.interrupt();
        mainWebview.clearCache(true);
    }

    /**
     * Hiển thị notification cho các mã đạt được điều kiện.
     *
     * @param newObject
     */
    public void showNotificationCode(final StockObject newObject) {
        if (newObject.getBuyingPrice1().isEmpty() || newObject.getBuyingPrice2().isEmpty() || newObject.getBuyingPrice3().isEmpty() || newObject.getTCPrice().isEmpty()) {
            return;
        }
        if (newObject.getBuyingPrice1().equals("ATO") || newObject.getBuyingPrice1().equals("ATC") ||
                newObject.getBuyingPrice2().equals("ATO") || newObject.getBuyingPrice2().equals("ATC") ||
                newObject.getBuyingPrice3().equals("ATO") || newObject.getBuyingPrice3().equals("ATC")) {
            return;
        }
        if (newObject.getTotalWeight().isEmpty() || newObject.getBuyingWeight1().isEmpty() || newObject.getBuyingWeight2().isEmpty() || newObject.getBuyingWeight3().isEmpty()) {
            return;
        }
        double cePrice = 0.0;
        double tcPrice = 0.0;
        double amountTotal = 0.0;
        double buying1 = Double.valueOf(newObject.getBuyingPrice1().replaceAll(",", "."));
        double buying2 = Double.valueOf(newObject.getBuyingPrice2().replaceAll(",", "."));
        double buying3 = Double.valueOf(newObject.getBuyingPrice3().replaceAll(",", "."));
        // Loại bỏ các ký tự không hợp lệ
        String tmpPrice = newObject.getTCPrice().replaceAll("[^0-9.-]", "");
        if(!tmpPrice.isEmpty())
            tcPrice = Double.valueOf(newObject.getTCPrice().replaceAll(",", "."));
        if(""!=newObject.getTopPrice())
            cePrice = Double.valueOf(newObject.getTopPrice().replaceAll(",", "."));
        double buyPrice1 = Double.valueOf(newObject.getBuyingPrice1().replaceAll(",", "."));
        if(""!=newObject.getTotalWeight())
        amountTotal = Double.valueOf(newObject.getTotalWeight().replaceAll("\\.", "").replaceAll(",", ""));
        double amountSum = Double.valueOf(newObject.getBuyingWeight1().replaceAll("\\.", "").replaceAll(",", ""))
                + Double.valueOf(newObject.getBuyingWeight2().replaceAll("\\.", "").replaceAll(",", ""))
                + Double.valueOf(newObject.getBuyingWeight3().replaceAll("\\.", "").replaceAll(",", ""));
        if ((newObject.getBuyingPrice1().equals(newObject.getTopPrice())) && (buying1 > tcPrice && buying2 > tcPrice && buying3 > tcPrice)
                && amountSum >= amountTotal && cePrice == buyPrice1) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (!listCK.contains(newObject.getCodeStock())) {
                        //Toast.makeText(getContext(), "High CK: " + newObject.getCodeStock(), Toast.LENGTH_SHORT).show();
                        listCK.add(newObject.getCodeStock());
                        if (listCK.size() > 0) {
                            String strHTML = "Top CK: ";
                            for (int i = 0; i < listCK.size(); i++) {
                                Calendar cal = Calendar.getInstance();
                                int hourofday = cal.get(Calendar.HOUR_OF_DAY);
                                if (hourofday < 10) {
                                    if (!strSpecial.contains(listCK.get(i)))
                                        strSpecial = strSpecial.equalsIgnoreCase("") ? listCK.get(i) : strSpecial + "," + listCK.get(i);
                                    if (strSpecial != SharedPreference.getInstance().getString("listSpecial", ""))
                                        SharedPreference.getInstance().putString("listSpecial", strSpecial);
                                }
                                if (SharedPreference.getInstance().getString("listSpecial", "").contains(listCK.get(i))) {
                                    strHTML = strHTML.equalsIgnoreCase("Top CK: ") ? strHTML + "<a style='text-decoration: none;text-decoration-color: green;' href='http://m.cafef.vn/tra-cuu/" + listCK.get(i) + ".chn'>[" + listCK.get(i) + "]</a>" : strHTML + ",<a style='text-decoration: none;' href='http://m.cafef.vn/tra-cuu/" + listCK.get(i) + ".chn'>[" + listCK.get(i) + "]</a>";
                                } else {
                                    strHTML = strHTML.equalsIgnoreCase("Top CK: ") ? strHTML + "<a style='text-decoration: none;' href='http://m.cafef.vn/tra-cuu/" + listCK.get(i) + ".chn'>" + listCK.get(i) + "</a>" : strHTML + ",<a style='text-decoration: none;' href='http://m.cafef.vn/tra-cuu/" + listCK.get(i) + ".chn'>" + listCK.get(i) + "</a>";
                                }
                            }
                            Log.d("Special: ", SharedPreference.getInstance().getString("listSpecial", ""));
                            setAsLink(txtTopCK, strHTML);
                            Log.d("Top CK", strHTML);
                            txtTopCK.setVisibility(View.VISIBLE);
                        } else {
                            txtTopCK.setVisibility(View.GONE);
                        }
                    }
                }
            });
        }
    }

    private void setAsLink(TextView view, String url) {
        Pattern pattern = Pattern.compile(url);
        Linkify.addLinks(view, pattern, "http://");
        view.setText(Html.fromHtml(url));
        view.setMovementMethod(LinkMovementMethod.getInstance());
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        return super.onContextItemSelected(item);
    }

    @Override
    public void onPause() {
        super.onPause();
        MainActivity.layoutPriceboard.setVisibility(View.GONE);
    }

    @Override
    public void onStop() {
        super.onStop();
        MainActivity.layoutPriceboard.setVisibility(View.GONE);
    }

    /**
     * Get raw html from webview
     */
    private class MyJavaScriptInterface {
        @JavascriptInterface
        @SuppressWarnings("unused")
        public void processHTML(final String html) {
            // process the html as needed by the app
            if (html.length() > 0 && !isRunningView) {
                //Log.i("HTML Length", html);
                fullHtml = html;
                //isLoadComplete = true;
                if (!isBindData) {
//                    new RetrieveFeedTask().execute();
                    executeHtml();
                }
            }
        }
    }

    public class CustomComparator implements Comparator<StockObject> {
        @Override
        public int compare(StockObject o1, StockObject o2) {
            return o1.getCodeStock().compareTo(o2.getCodeStock());
        }
    }

}