package vn.co.vns.runningman.VolleyAPI;

import vn.co.vns.runningman.util.Config;

/**
 * Created by thanhnv on 7/13/17.
 */

public abstract class BaseApi<T extends BaseView> {
    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    private String baseUrl;

    public BaseApi(String baseUrl) {
        this.baseUrl = Config.BASE_URL + baseUrl;
    }

//    public String getItemApiUrl(String id) {
//        return String.format(AppConstants.API_GET_ITEM_PATTERN, this.baseUrl, id);
//    }
//
//    public String getItemsApiUrl(int page, int quantity, String searchValue, SortBy sortBy, SortDirection sortDirection) {
//        return String.format(AppConstants.API_GET_ITEMS_PATTERN, this.baseUrl, page, quantity,searchValue, sortBy, sortDirection);
//    }
}
