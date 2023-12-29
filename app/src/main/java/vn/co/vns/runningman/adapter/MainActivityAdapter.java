package vn.co.vns.runningman.adapter;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

import vn.co.vns.runningman.fragment.FragmentFinacial;
import vn.co.vns.runningman.fragment.FragmentFinacialList;
import vn.co.vns.runningman.fragment.FragmentHome;
import vn.co.vns.runningman.fragment.FragmentResult;
import vn.co.vns.runningman.fragment.FragmentSetting;
import vn.co.vns.runningman.fragment.FragmentTablePriceOnline;
import vn.co.vns.runningman.util.Constant;
import vn.co.vns.runningman.util.SharedPreference;

/**
 * Created by thanhnv on 11/25/16.
 */
public class MainActivityAdapter extends FragmentStatePagerAdapter {

    private ArrayList<Fragment> items = new ArrayList<>();

    public MainActivityAdapter(FragmentManager fm) {
        super(fm);
        items.add(FragmentHome.newInstance());
        if (SharedPreference.getInstance().getBoolean("ho", true)) { //Default
            if (SharedPreference.getInstance().getString("priceTable", "cafef").equalsIgnoreCase("cafef")) {
                items.add(FragmentTablePriceOnline.newInstance(Constant.URL_CAFFEF_HSX));
            } else {
                items.add(FragmentTablePriceOnline.newInstance(Constant.URL_SSI_HSX));
            }
        } else {
            if (SharedPreference.getInstance().getString("priceTable", "cafef").equalsIgnoreCase("cafef")) {
                items.add(FragmentTablePriceOnline.newInstance(Constant.URL_CAFFEF_HNX));
            } else {
                items.add(FragmentTablePriceOnline.newInstance(Constant.URL_SSI_HNX));
            }
        }
//        items.add(FragmentAnalytic.newInstance());
        items.add(FragmentFinacial.newInstance());
        items.add(FragmentResult.newInstance());
        items.add(FragmentSetting.newInstance());
        items.add(FragmentFinacialList.newInstance());

    }

    @Override
    public Fragment getItem(int position) {
        if (position >= Constant.MAX_SCREEN_HOME) {
            return items.get(0);
        } else if (position < 0) {
            return new Fragment();
        }
        return items.get(position);
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public int getCount() {
        return Constant.MAX_SCREEN_HOME;
    }


}
