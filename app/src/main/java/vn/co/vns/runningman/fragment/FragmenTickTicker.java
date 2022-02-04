package vn.co.vns.runningman.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import vn.co.vns.runningman.R;
import vn.co.vns.runningman.activity.MainActivity;

/**
 * Created by thanhnv on 11/25/16.
 */
public class FragmenTickTicker extends Fragment{
    private View mMainView;

    public static FragmenTickTicker newInstance() {
        Bundle args = new Bundle();
        FragmenTickTicker fragment = new FragmenTickTicker();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mMainView = inflater.inflate(R.layout.fragment_analytic, null);
        mMainView.setTag("FragmentAnalytic");
//        buildView();
        return mMainView;
    }

    public static void changeTitleView(Activity activity){
        MainActivity.appTitle.setText(activity.getString(R.string.tab_analytic));
    }
}
