package vn.co.vns.runningman.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import vn.co.vns.runningman.R;
import vn.co.vns.runningman.activity.MainActivity;
import vn.co.vns.runningman.adapter.HomeDetailAdapter;
import vn.co.vns.runningman.util.SharedPreference;

/**
 * Created by thanhnv on 11/25/16.
 */
public class FragmentHome extends Fragment {
    private View mMainView;
    private ViewPager pager;

    public static FragmentHome newInstance() {
        Bundle args = new Bundle();
        FragmentHome fragment = new FragmentHome();
        fragment.setArguments(args);
        return fragment;
    }

    public static void changeTitleView(Activity activity) {
        MainActivity.appTitle.setText(activity.getString(R.string.tab_home));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mMainView = inflater.inflate(R.layout.fragment_home, null);
        mMainView.setTag("FragmentHome");
        pager = mMainView.findViewById(R.id.pager);
        //pager.setOffscreenPageLimit(1);
        pager.setAdapter(buildAdapter());
        pager.setOffscreenPageLimit(2);

        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                SharedPreference.getInstance(getContext()).putInt("screenHome", position);
                switch (position) {
                    case 0:
                        MainActivity.appTitle.setText(getActivity().getString(R.string.tab_home));
                        MainActivity.btnExchange.setVisibility(View.VISIBLE);
                        break;
                    case 1:
                        MainActivity.btnExchange.setVisibility(View.INVISIBLE);
                        MainActivity.appTitle.setText(getActivity().getString(R.string.tab_home_bigvolume));
                        break;
                    case 2:
                        MainActivity.appTitle.setText(getActivity().getString(R.string.tab_home_breakout));
                        break;
                    case 3:
                        MainActivity.appTitle.setText(getActivity().getString(R.string.tab_home_breakoutvolume));
                        break;
                    default:
                        MainActivity.appTitle.setText(getActivity().getString(R.string.tab_home));
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        return mMainView;
    }

    private PagerAdapter buildAdapter() {
        return (new HomeDetailAdapter(getActivity(), getChildFragmentManager()));
    }

    public void onChangeHome() {
        Fragment fragment;
        fragment = (PriceVolumeAgreement) pager
                .getAdapter()
                .instantiateItem(pager, pager.getCurrentItem());

        if (fragment != null && fragment instanceof PriceVolumeAgreement) {
            ((PriceVolumeAgreement) fragment).onChangeHomeTab();

        }
    }
}
