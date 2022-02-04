package vn.co.vns.runningman.VolleyAPI;

import vn.co.vns.runningman.object.Tick;
import vn.co.vns.runningman.util.Constant;

/**
 * Created by thanhnv on 7/13/17.
 */


    public class HomeApi extends BaseApi<Tick> {
    private static HomeApi sInstance;

    public HomeApi(String baseUrl) {
        super(baseUrl);
    }

    public HomeApi() {
        super("");
    }

    public static HomeApi getInstance() {
        if (sInstance == null) sInstance = new HomeApi();
        return sInstance;
    }

//    public HomeApi() {
//        super("");
//    }

    public String urlAddTick() {
        return String.format(Constant.TICK_ADD_RESOURCE, this.getBaseUrl());
    }

    public String urlUpdateTick(String ticker) {
        return String.format(Constant.TICK_UPDATE_RESOURCE, this.getBaseUrl(),ticker);
    }

    public String urlDeleteTick(String ticker) {
        return String.format(Constant.TICK_DELETE_RESOURCE, this.getBaseUrl(),ticker);
    }

    public String urlGetTick() {
        return String.format(Constant.TICK_GET_RESOURCE, this.getBaseUrl());
    }

    public String urlGetDateTransition() {
        return String.format(Constant.STOCK_DATE_TRANSITION_RESOURCE, this.getBaseUrl());
    }
}
