package vn.co.vns.runningman.adapter;

import android.content.Context;
import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import vn.co.vns.runningman.activity.MainActivity;
import vn.co.vns.runningman.fragment.FragmentBigVolume;
import vn.co.vns.runningman.fragment.FragmentBreakOut;
import vn.co.vns.runningman.fragment.FragmentBreakOutVolume;
import vn.co.vns.runningman.fragment.FragmentHomeDetail;
import vn.co.vns.runningman.fragment.PriceVolumeAgreement;

/**
 * Created by thanhnv on 11/28/16.
 */
public class HomeDetailAdapter extends FragmentPagerAdapter {
    private Context mContext=null;

    public HomeDetailAdapter(Context context, FragmentManager fm) {
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
        MainActivity.layoutPriceboard.setVisibility(View.GONE);
        switch (position) {
            case 0:
                MainActivity.btnExchange.setVisibility(View.VISIBLE);
                return new PriceVolumeAgreement();
            case 1:
                return new FragmentBigVolume();
            case 2:
                return new FragmentBreakOut();
            case 3:
                return new FragmentBreakOutVolume();
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
