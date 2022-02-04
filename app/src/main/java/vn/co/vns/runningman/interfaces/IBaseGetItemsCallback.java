package vn.co.vns.runningman.interfaces;

import java.util.ArrayList;

import vn.co.vns.runningman.VolleyAPI.BaseView;
import vn.co.vns.runningman.error.ErrorView;

/**
 * Created by thanhnv on 7/13/17.
 */


public interface IBaseGetItemsCallback<T extends BaseView> {
        void onSuccess(ArrayList<T> items, String s);
        void onError(ArrayList<ErrorView> errors);

}
