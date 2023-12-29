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
import vn.co.vns.runningman.adapter.FinacialAdapter;

/**
 * Created by thanhnv on 11/25/16.
 */
public class FragmentFinacial extends Fragment {
    private View mMainView;
    private ViewPager pager;

    public static FragmentFinacial newInstance() {
        Bundle args = new Bundle();
        FragmentFinacial fragment = new FragmentFinacial();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mMainView = inflater.inflate(R.layout.fragment_finacial, null);
        mMainView.setTag("FragmentFinacial");
        pager= mMainView.findViewById(R.id.pager);
        //pager.setOffscreenPageLimit(1);
        pager.setAdapter(buildAdapter());
        pager.setOffscreenPageLimit(1);

        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        return mMainView;
    }

    private PagerAdapter buildAdapter() {
        return(new FinacialAdapter(getActivity(), getChildFragmentManager()));
    }
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser  ) {

        }
    }

    public static void changeTitleView(Activity activity){
        MainActivity.appTitle.setText(activity.getString(R.string.tab_finacial));
    }

}
