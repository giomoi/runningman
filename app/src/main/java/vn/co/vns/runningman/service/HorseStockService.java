package vn.co.vns.runningman.service;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import vn.co.vns.runningman.object.StockObject;

/**
 * Created by hoangtuan on 5/8/17.
 */
public class HorseStockService extends IntentService {
    private String TAG=getClass().getSimpleName().toUpperCase();
    private ArrayList<String> listCK = new ArrayList<>();
    //private View mMainView;
    private WebView mainWebview;
    private boolean isRunningView = false;
    private boolean isBindData = false;
    private String urlString = "http://banggia.vietinbanksc.com.vn";

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public HorseStockService(String name) { super(name);}

    public HorseStockService() { super("HorseStockService");}

    @Override
    public void onCreate() {
        super.onCreate();
        mainWebview = new WebView(this);
        mainWebview.getSettings().setJavaScriptEnabled(true);
        mainWebview.setWebChromeClient(new WebChromeClient());
        mainWebview.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                autoGetDataWebView();
            }
        });
        mainWebview.loadUrl(urlString);
        mainWebview.addJavascriptInterface(new MyJavaScriptInterface(), "HTMLOUT");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        //autoGetDataWebView();
    }

    private void autoGetDataWebView(){
        try {
            while (!isRunningView) {
                Thread.sleep(7000);
                mainWebview.loadUrl("javascript:window.HTMLOUT.processHTML('<head>'+document.getElementsByTagName('html')[0].innerHTML+'</head>');");
                Log.i("Data", "Refreshing...");
//                if(checkOverTimeInDay()){
////                        resetThread.interrupt();
//                    isRunningView = true;
//                    break;
//                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public class MyJavaScriptInterface
    {
        @JavascriptInterface
        @SuppressWarnings("unused")
        public void processHTML(final String html)
        {
            // process the html as needed by the app
            if(html.length()>0 && html.contains("extraBuyVol") && !isRunningView) {
                //isLoadComplete = true;
                if(!isBindData) {
                    execute(html);
                }
            }
        }
    }

    private void execute(String fullHtml) {
        isBindData = true;
        //ArrayList<StockObject> result = new ArrayList<>();
        try {
            Document doc = Jsoup.parse(fullHtml);
            Elements trTable = doc.select("table#tableQuote");
            if(trTable.size()>0) {
                for (Element s : trTable) {
                    Elements rowTable = s.getElementsByTag("tr");
                    for (Element tr : rowTable) {
                        if (tr.toString().contains("style=\"height: 21px;\"")) {
                            Elements colTable = tr.getElementsByTag("td");
                            StockObject newObject = creatStockObject(colTable);
                            showNotificationCode(newObject);
                            //result.add(newObject);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private StockObject creatStockObject(Elements td) {
        StockObject object = new StockObject();
        for(int i=0;i<td.size();i++){
            Element item = td.get(i);
            object.setValue(i==0?item.select("a[href]").text():item.select("td").text(), i);
        }
        return object;
    }

    /**
     * Hiển thị notification cho các mã đạt được điều kiện.
     * @param newObject
     */
    public void showNotificationCode(final StockObject newObject){
        if(newObject.getBuyingPrice1().isEmpty() || newObject.getBuyingPrice2().isEmpty() || newObject.getBuyingPrice3().isEmpty() || newObject.getTCPrice().isEmpty()){
            return;
        }
        if(newObject.getBuyingPrice1().equals("ATO")||newObject.getBuyingPrice1().equals("ATC")||
                newObject.getBuyingPrice2().equals("ATO")||newObject.getBuyingPrice2().equals("ATC")||
                newObject.getBuyingPrice3().equals("ATO")||newObject.getBuyingPrice3().equals("ATC")){
            return;
        }
        if(newObject.getTotalWeight().isEmpty() || newObject.getBuyingWeight1().isEmpty() || newObject.getBuyingWeight2().isEmpty()||newObject.getBuyingWeight3().isEmpty()){
            return;
        }
        double checkedPrice = 0.92* Double.valueOf(newObject.getTopPrice().replaceAll(",", "."));
        double buying1 = Double.valueOf(newObject.getBuyingPrice1().replaceAll(",", "."));
        double buying2 = Double.valueOf(newObject.getBuyingPrice2().replaceAll(",", "."));
        double buying3 = Double.valueOf(newObject.getBuyingPrice3().replaceAll(",", "."));
        double tcPrice = Double.valueOf(newObject.getTCPrice().replaceAll(",", "."));

        double amountTotal = 0.92*Double.valueOf(newObject.getTotalWeight().replaceAll("\\.", ""));
        double amountSum = Double.valueOf(newObject.getBuyingWeight1().replaceAll("\\.", ""))
                + Double.valueOf(newObject.getBuyingWeight2().replaceAll("\\.", ""))
                + Double.valueOf(newObject.getBuyingWeight3().replaceAll("\\.",""));
        if((newObject.getBuyingPrice1().equals(newObject.getTopPrice())) && (buying1>checkedPrice && buying2>checkedPrice && buying3>checkedPrice) && (buying1 > tcPrice && buying2 > tcPrice && buying3 > tcPrice)
                && amountSum>=amountTotal){
            if(!listCK.contains(newObject.getCodeStock())) {
                Toast.makeText(this, "High CK: " + newObject.getCodeStock(), Toast.LENGTH_SHORT).show();
                listCK.add(newObject.getCodeStock());
            }
//            getActivity().runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    if(!listCK.contains(newObject.getCodeStock())) {
//                        //Toast.makeText(getContext(), "High CK: " + newObject.getCodeStock(), Toast.LENGTH_SHORT).show();
//                        listCK.add(newObject.getCodeStock());
//                        txtTopCK.setText("Today, Top CK: " + listCK.toString());
//                    }
//                }
//            });
        }
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
        }
        return true;
    }
}
