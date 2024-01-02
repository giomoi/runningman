package vn.co.vns.runningman.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import vn.co.vns.runningman.object.StockBigVolume;

/**
 * Created by thanhnv on 10/4/16.
 */
public class Constant {

    public static final String URI="https://cafef1.mediacdn.vn/data/ami_data";
    public static final String[] URL = {
            URI+"/20161003/CafeF.SolieuGD.Raw.Upto03102016.zip", //Chua Dieu chinh
//                                        "http://images1.cafef.vn/data/20161003/CafeF.SolieuGD.Raw.03102016.zip", //Chua dieu chinh
            // "http://images1.cafef.vn/data/20161003/CafeF.SolieuGD.Upto03102016.zip",    //dieu chinh
//                                        "http://images1.cafef.vn/data/20161003/CafeF.SolieuGD.03102016.zip",     //Dieu chinh
//                                        "http://images1.cafef.vn/data/20161003/CafeF.Index.03102016.zip",
//                                        "http://images1.cafef.vn/data/20161003/CafeF.Index.Upto03102016.zip",
//                                        "http://images1.cafef.vn/data/20161003/CafeF.CCNN.Index.03102016.zip",
//                                        "http://images1.cafef.vn/data/20161003/CafeF.CCNN.Index.Upto03102016.zip",
//                                        "http://images1.cafef.vn/data/20161003/CafeF.CCNN.Upto03102016.zip",
//                                        "http://images1.cafef.vn/data/20161003/CafeF.CCNN.03102016.zip"
    };

    public static final String beforeDayBigVolume = "2016-11-18";

    public static final String[] URL_DAY = {
//                                "http://images1.cafef.vn/data/20161003/CafeF.SolieuGD.Raw.03102016.z
// ip", //Chua dieu chinh hang ngay
            "https://cafef1.mediacdn.vn/data/ami_data/20161003/CafeF.SolieuGD.03102016.zip",        //Da dieu chinh hang ngay
    };

    public static ArrayList<StockBigVolume> arrayStockBigVolume = new ArrayList<>();
    public static int MAX_SCREEN_HOME = 6;
    public static int MAIN_HOME = 0;
    public static int MAIN_BIG_VOLUME = 3;
    public static final String nowDay = new SimpleDateFormat("yyyyMMdd", Locale.ENGLISH).format(new Date());
    public static final String beforeDay = new SimpleDateFormat("yyyyMMdd", Locale.ENGLISH).format(System.currentTimeMillis() - (1000 * 60 * 60 * 24));
    //public static final String before30DayFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).format(System.currentTimeMillis() - (1000 * 60 * 60 * 24 * 30));
//    public  static  final Date before30DayFormat=Utils.addMonths(new Date(),-1);

    public static final Date beforeDayFormat = Utils.addDays(new Date(), -365);
    public static String maxDay = "";
    public static String dateTransition = null;

    public static Integer VOLUME0 = 0; //All
    public static Integer VOLUME50 = 50000;
    public static Integer VOLUME100 = 100000;
    public static Integer VOLUME200 = 200000;
    public static Integer VOLUME300 = 300000;
    public static Integer VOLUME400 = 400000;
    public static Integer VOLUME500 = 500000;

    public static Integer BREAK_OUT_NUMBER_DAY0 = 0; // All
    public static Integer BREAK_OUT_NUMBER_DAY30 = -30; // 1 month
    public static Integer BREAK_OUT_NUMBER_DAY90 = -90; // 3 month
    public static Integer BREAK_OUT_NUMBER_DAY180 = -180; // 6 month
    public static Integer BREAK_OUT_NUMBER_DAY270 = -270; // 9 month
    public static Integer BREAK_OUT_NUMBER_DAY365 = -365; // 1 year

    public static int SORT_TIKER = 0;
    public static int SORT_RATE = 1;
    public static int SORT_PRIORITY = 2;

    //Download
    public static int NOT_UPDATE = 0;
    public static int UPDATING = 1;
    public static int UPDATED = 2;

    public static Double valueToCompare = 0.92d;
    public static String optionPriceboard = "HastcStockBoard";

    //API URL and Resource
    public static int VOLLEY_TIMEOUT = 30000;
    public static int VOLLEY_MAX_NUM_RETRIES = 0;
    public static final String ERROR_PREFIX = "error_";
    public static final String URL_API = "http://runingman.freevar.com/api/v1/";
    public static final String TICK_GET_RESOURCE = "getTicks";
    public static final String TICK_ADD_RESOURCE = "%1$stick/add";
    public static final String TICK_UPDATE_RESOURCE = "%supdateTickTicker/%s";
    public static final String TICK_DELETE_RESOURCE = "%1$sdeleteTickTicker/%2$s";
    public static String STOCK_DATE_TRANSITION_RESOURCE = "%1$sgetDateTransition";


    public static String URL_OTHER_HSX = "https://stockboard.sbsc.com.vn/exchange/hsx";
    public static String URL_OTHER_HNX = "https://stockboard.sbsc.com.vn/exchange/hnx";
//    public static String URL_OTHER_HSX = "https://banggia.dnse.com.vn/hsx";
//    public static String URL_OTHER_HNX = "https://banggia.dnse.com.vn/hnx";
    public static String URL_DEFAULT_HSX = "https://banggia.vps.com.vn/chung-khoan/HOSE";
    public static String URL_DEFAULT_HNX = "https://banggia.vps.com.vn/chung-khoan/HNX";

}

