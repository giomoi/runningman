package vn.co.vns.runningman.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;

import vn.co.vns.runningman.activity.MainActivity;
import vn.co.vns.runningman.fragment.FragmentBigVolume;
import vn.co.vns.runningman.fragment.FragmentBreakOut;
import vn.co.vns.runningman.fragment.FragmentEPSQuarter;
import vn.co.vns.runningman.fragment.FragmentEPSYear;
import vn.co.vns.runningman.fragment.FragmentFinacialCommon;
import vn.co.vns.runningman.fragment.FragmentHomeDetail;
import vn.co.vns.runningman.fragment.FragmentPE;
import vn.co.vns.runningman.fragment.FragmentTablePriceOnline;
import vn.co.vns.runningman.fragment.PriceVolumeAgreement;

/**
 * Created by thanhnv on 11/28/16.
 */
public class FinacialListAdapter extends FragmentPagerAdapter{
    private Context mContext=null;

    public FinacialListAdapter(Context context, FragmentManager fm) {
        super(fm);
        this.mContext=context;
    }

    private int count = 4;
    public void setCount(int count) {
        this.count = count;
    }

    /**
     * Return the Fragment associated with a specified position.
     *
     * @param position
     */
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
//                return new FragmentFinacialCommon();
                return new FragmentEPSYear();
            case 1:
                return new FragmentEPSQuarter();
            case 2:
                return new FragmentPE();

            default:
                return new Fragment();
        }
    }

    /**
     * Return the number of views available.
     */
    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public String getPageTitle(int position) {
        return (FragmentHomeDetail.getTitle(mContext, position));
    }


}
