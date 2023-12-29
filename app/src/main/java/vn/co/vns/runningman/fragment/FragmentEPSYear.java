package vn.co.vns.runningman.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import vn.co.vns.runningman.object.BusinessResultsObject;
import vn.co.vns.runningman.object.StockVolumePriceAgreement;

/**
 * Created by thanhnv on 11/28/16.
 */
public class FragmentEPSYear extends Fragment {
    private View mMainView;
    private ListView lvBigVolume;
    private StockBigVolumeAdapter adapterTop;
    private String url="http://www.cophieu68.vn/incomestatement.php?id=sam&view=ist&year=-1";

    public static FragmentEPSYear newInstance() {
        Bundle args = new Bundle();
        FragmentEPSYear fragment = new FragmentEPSYear();
        fragment.setArguments(args);
        return fragment;
    }

    private ArrayList<BusinessResultsObject> listBusinessResults=new ArrayList<>();

    private Thread resetThread = new Thread(new Runnable() {
        @Override
        public void run() {
            try {
                Document doc = Jsoup.connect(url).get();
                Elements trTableBusinessResult = doc.select("table");
                if(trTableBusinessResult.size()>0) {
                    ArrayList<String> arrYear=new ArrayList<>();
                    for (Element s : trTableBusinessResult) {
                        Elements rowTableBusinessResult = s.getElementsByTag("tr");
                        int countRow=0;
                        for (Element tr : rowTableBusinessResult) {
                            if (tr.toString().contains("tr_header")) {
                                Elements colTableBusinessResult = tr.getElementsByTag("td");
                                for(int i=1;i<colTableBusinessResult.size();i++) {
                                    BusinessResultsObject objBusinessResult = new BusinessResultsObject("SAM",colTableBusinessResult.get(i).text());
                                    arrYear.add(colTableBusinessResult.get(i).text());
                                    listBusinessResults.add(objBusinessResult);
                                }
                                Log.d("HTML: ",tr.toString());
                            }else {
                                Elements colTableBusinessResult = tr.getElementsByTag("td");
                                setValue(colTableBusinessResult,countRow);
                                Log.d("HTML: ",tr.toString());
                            }
                            countRow++;
                        }
                        Log.d("Data: ", listBusinessResults.toString());
                    }
                }
                //tr_header
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    });

    private void setValue(Elements colTableBusinessResult,int i){
        switch (i){
            case 2:
//                Elements colTableBusinessResult = tr.getElementsByTag("td");
                for (int j = 1; j <colTableBusinessResult.size(); j++) {
                    listBusinessResults.get(j-1).setNetRevenue(Float.parseFloat(colTableBusinessResult.get(j).text().replace(",","").trim()));
                }
                return;
            case 28:
                for (int j = 1; j <colTableBusinessResult.size(); j++) {
                    listBusinessResults.get(j-1).setProfitAfterTax(Float.parseFloat(colTableBusinessResult.get(j).text().replace(",","").trim()));
                }
                return;
            case 30:
                for (int j = 2; j <colTableBusinessResult.size(); j++) {
                    listBusinessResults.get(j-2).setVolume(Float.parseFloat(colTableBusinessResult.get(j).text().replace(",","").trim()));
                }
                return;
            case 32:
                for (int j = 2; j <colTableBusinessResult.size(); j++) {
                    listBusinessResults.get(j-2).setPriceEnding(Float.parseFloat(colTableBusinessResult.get(j).text().replace(",","").trim()));
                }
                return;
            case 36:
                for (int j = 2; j <colTableBusinessResult.size(); j++) {
                    if(!"N/A".equalsIgnoreCase(colTableBusinessResult.get(j).text().replace(",","").trim())) {
                        listBusinessResults.get(j - 2).setBookValue(Float.parseFloat(colTableBusinessResult.get(j).text().replace(",", "").trim()));
                    }else{
                        listBusinessResults.get(j - 2).setBookValue(0);
                    }
                }
                return;
            default:
                break;
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
//            if (resetThread.getState() == Thread.State.NEW)
//            {
//                resetThread.start();
//            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mMainView = inflater.inflate(R.layout.fragment_eps_year, null);
        mMainView.setTag("FragmentHome");


        MySQLiteHelper db= new MySQLiteHelper(getContext());
        ArrayList<StockVolumePriceAgreement> arrayStockVolumePrice=db.getListStockVolumePriceAgreement();
//        adapterTop=new ListTopAdapter(getContext(),arrayStockVolumePrice);
//        lvBigVolume.setAdapter(adapterTop);

        return mMainView;
    }
}
