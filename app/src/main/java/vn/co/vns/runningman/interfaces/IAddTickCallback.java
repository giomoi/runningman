package vn.co.vns.runningman.interfaces;

import java.util.ArrayList;

import vn.co.vns.runningman.object.Tick;

/**
 * Created by thanhnv on 7/13/17.
 */


    public interface IAddTickCallback extends IBaseGetItemsCallback<Tick> {

        @Override
        void onError(ArrayList errors);


        @Override
        void onSuccess(ArrayList<Tick> items, String totalUser);
    }
