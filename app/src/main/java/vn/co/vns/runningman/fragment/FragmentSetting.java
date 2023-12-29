package vn.co.vns.runningman.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Switch;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import vn.co.vns.runningman.R;
import vn.co.vns.runningman.activity.MainActivity;
import vn.co.vns.runningman.util.SharedPreference;

/**
 * Created by thanhnv on 11/25/16.
 */
public class FragmentSetting extends Fragment {

    private View mMainView;
    private Spinner spinAverIndex;
    private RadioGroup radioGroupPriceTable;
    private Switch switchNotification;

    public static FragmentSetting newInstance() {
        Bundle args = new Bundle();
        FragmentSetting fragment = new FragmentSetting();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mMainView = inflater.inflate(R.layout.fragment_setting, null);
        mMainView.setTag("FragmentSetting");
        buildView();
        return mMainView;
    }

    private void buildView(){
        spinAverIndex= mMainView.findViewById(R.id.spinAverIndex);
        radioGroupPriceTable= mMainView.findViewById(R.id.radioPricetTable);
        switchNotification=mMainView.findViewById(R.id.switchNotification);
        radioGroupPriceTable.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                View radioButton = radioGroupPriceTable.findViewById(checkedId);
                int index = radioGroupPriceTable.indexOfChild(radioButton);
                // Add logic here
                switch (index) {
                    case 0: // first button
                        SharedPreference.getInstance().putString("priceTable","cafef");
                        break;
                    case 1: // secondbutton
                        SharedPreference.getInstance().putString("priceTable","other");
                        break;
                }
            }
        });
        if(SharedPreference.getInstance().getString("priceTable","cafef").equalsIgnoreCase("cafef")){
            radioGroupPriceTable.check(R.id.radioCafef);
        }else{
            radioGroupPriceTable.check(R.id.radioOther);
        }
        if(SharedPreference.getInstance().getString("averIndex","10").equalsIgnoreCase("10")){
            spinAverIndex.setSelection(0);
        }else{
            spinAverIndex.setSelection(1);
        }
        if(SharedPreference.getInstance().getBoolean("notification",true)){
            switchNotification.setChecked(true);
        }else{
            switchNotification.setChecked(false);
        }

        switchNotification.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    SharedPreference.getInstance().putBoolean("notification",true);
                }else{
                    SharedPreference.getInstance().putBoolean("notification",false);
                }
            }
        });
        createDropdown();
    }
    public static void changeTitleView(Activity activity){
        MainActivity.appTitle.setText(activity.getString(R.string.tab_setting));
    }

    private void createDropdown() {
        String[] itemsNumberDay = new String[]{"10 ngày", "20 ngày"};
        ArrayAdapter<String> adapterNumberDay = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, itemsNumberDay);
        spinAverIndex.setAdapter(adapterNumberDay);
        spinAverIndex.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                switch(position)
                {
                    case 0:
                        SharedPreference.getInstance().putString("averIndex","10");
                        break;
                    case 1:
                        SharedPreference.getInstance().putString("averIndex","20");
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }
}
