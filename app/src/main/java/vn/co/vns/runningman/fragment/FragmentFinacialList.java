package vn.co.vns.runningman.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.ScrollView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import butterknife.InjectView;
import vn.co.vns.runningman.R;
import vn.co.vns.runningman.adapter.FinacialListAdapter;

/**
 * Created by thanhnv on 11/25/16.
 */
public class FragmentFinacialList extends Fragment {
    private View mMainView;
    @InjectView(R.id.layoutMainView)    ScrollView layoutMainView;
    @InjectView(R.id.progressLoading)   ProgressBar progressLoading;
    private String stockID = "";

    public static FragmentFinacialList newInstance() {
        Bundle args = new Bundle();
        FragmentFinacialList fragment = new FragmentFinacialList();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mMainView = inflater.inflate(R.layout.fragment_finacial_list, null);
        mMainView.setTag("FragmentAnalyticList");

        ViewPager pager= mMainView.findViewById(R.id.pager);
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
        return(new FinacialListAdapter(getActivity(), getChildFragmentManager()));
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser ) {

        }
    }

}
