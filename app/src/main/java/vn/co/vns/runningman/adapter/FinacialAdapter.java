package vn.co.vns.runningman.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import vn.co.vns.runningman.fragment.FragmentAccountingBalanceSheet;
import vn.co.vns.runningman.fragment.FragmentBusinessResult;
import vn.co.vns.runningman.fragment.FragmentCashFlow;
import vn.co.vns.runningman.fragment.FragmentEPSQuarter;
import vn.co.vns.runningman.fragment.FragmentEPSYear;
import vn.co.vns.runningman.fragment.FragmentFinacialCommon;
import vn.co.vns.runningman.fragment.FragmentHomeDetail;
import vn.co.vns.runningman.fragment.FragmentPE;

/**
 * Created by thanhnv on 11/28/16.
 */
public class FinacialAdapter extends FragmentPagerAdapter{
    private Context mContext=null;

    public FinacialAdapter(Context context, FragmentManager fm) {
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
                return new FragmentFinacialCommon();
            case 1:
                return new FragmentAccountingBalanceSheet();
            case 2:
                return new FragmentBusinessResult();
            case 3:
                return new FragmentCashFlow();
            default:
                return new Fragment();
        }
    }

    /**
     * Return the number of views available.
     */
    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public String getPageTitle(int position) {
        return (FragmentHomeDetail.getTitle(mContext, position));
    }


}
