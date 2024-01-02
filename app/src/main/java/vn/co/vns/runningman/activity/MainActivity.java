package vn.co.vns.runningman.activity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import java.util.Calendar;

import vn.co.vns.runningman.R;
import vn.co.vns.runningman.adapter.MainActivityAdapter;
import vn.co.vns.runningman.customerview.ViewPagerNoTouch;
import vn.co.vns.runningman.fragment.FragmentFinacial;
import vn.co.vns.runningman.fragment.FragmentHome;
import vn.co.vns.runningman.fragment.FragmentResult;
import vn.co.vns.runningman.fragment.FragmentSetting;
import vn.co.vns.runningman.fragment.FragmentTablePriceOnline;
import vn.co.vns.runningman.fragment.PriceVolumeAgreement;
import vn.co.vns.runningman.interfaces.ICompleteDownloadTicker;
import vn.co.vns.runningman.model.MySQLiteHelper;
import vn.co.vns.runningman.object.InforVolumeValueStockIndex;
import vn.co.vns.runningman.receiver.SensorChangeValueIndexBoardcastReceiver;
import vn.co.vns.runningman.receiver.SensorStockUpdateBoardcastReceiver;
import vn.co.vns.runningman.util.Constant;
import vn.co.vns.runningman.receiver.SensorRestarterBroadcastReceiver;
import vn.co.vns.runningman.receiver.SensorStopBoardcastReceiver;
import vn.co.vns.runningman.util.SharedPreference;
import vn.co.vns.runningman.util.Singleton;
import vn.co.vns.runningman.util.Utils;

/**
 * Created by thanhnv on 11/25/16.
 */
public class MainActivity extends AppCompatActivity implements TabHost.OnTabChangeListener {
    public static TextView appTitle;
    public static Button btnExchange;
    public static LinearLayout layoutPriceboard;
    public static RelativeLayout layoutTitle;
    private static Button btnSearch;
    private static Button btnListStock;
    public static Button btnUnit;
    private TabHost tabHost;
    private ViewPagerNoTouch mViewPager;
    private MainActivityAdapter mainActivityAdapter;
    private String dayVN = "";
    private String dayJP = "";
    private PriceVolumeAgreement fragment;
    private FrameLayout frmBaseBody;
    private BroadcastReceiver updateUIReciver;
    private String TAG = MainActivity.class.getSimpleName();
//    public static Intent intentStockOnlineServices;

    private ICompleteDownloadTicker handleCompleteTicker = new ICompleteDownloadTicker() {
        @Override
        public void onSuccess() {
//            setupTab();
        }

        @Override
        public void onFailed() {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        setTheme(R.style.Theme_AppCompat);
        super.onCreate(savedInstanceState);
        //this.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        SharedPreference.getInstance(getApplicationContext());
        SharedPreference.getInstance(getApplicationContext()).putString("list_priority", "");
        //createCalendarOnlinePrice();
//        if (this instanceof PriceAgreementCallback) {
//            callback = (PriceAgreementCallback) this;
//        }
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        testData();
        installView();
        setupTab();
        //Show notification stock Start 09:15
        notificationPeriodicalStockCeBigVolumn();
        //Show notification when change break out about value 40%
        if (!SharedPreference.getInstance(this).getBoolean("isChangeValue", false)) {
            periodicalChangeValueIndex();
        }
        //Notification 16h:00 the value of increase or decrease of 40% compared to 10-day average
        //Update bigvolumn everyday
        periodicalUpdateStock();

        updateDataSucess();
    }

    private void updateDataSucess() {
        IntentFilter filter = new IntentFilter();
        filter.addAction("vn.co.vns.runningman");
        updateUIReciver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                //UI update here
                btnUnit.setText(context.getString(R.string.txt_updated));
            }
        };
        registerReceiver(updateUIReciver, filter);
    }

    private void periodicalChangeValueIndex() {
        //        Start
        //Create alarm manager
        AlarmManager alarmRecerverChangeValueIndex = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        //Create pending intent & register it to your alarm notifier class
        Intent intentChangeValueIndex = new Intent(this, SensorChangeValueIndexBoardcastReceiver.class);
        PendingIntent pendingReceiverStart = PendingIntent.getBroadcast(this, 0, intentChangeValueIndex, PendingIntent.FLAG_UPDATE_CURRENT);
        //set timer you want alarm to work (here I have set it to 7.20pm)
        Calendar timeStart = Calendar.getInstance();
        timeStart.setTimeInMillis(System.currentTimeMillis());
        timeStart.set(Calendar.HOUR_OF_DAY, 16);
        timeStart.set(Calendar.MINUTE, 30);
        timeStart.set(Calendar.SECOND, 0);
        //set that timer as a RTC Wakeup to alarm manager object
        alarmRecerverChangeValueIndex.setRepeating(AlarmManager.RTC_WAKEUP, timeStart.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingReceiverStart);
//        alarmRecerverChangeValueIndex.setInexactRepeating(AlarmManager.RTC_WAKEUP, timeStart.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingReceiverStart);
        SharedPreference.getInstance(this).putBoolean("isChangeValue", true);
    }

    private void periodicalUpdateStock() {
        //Start
        //Create alarm manager
        AlarmManager alarmRecerverUpdateStock = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        //Create pending intent & register it to your alarm notifier class
        Intent intenUpdateStock = new Intent(this, SensorStockUpdateBoardcastReceiver.class);
        PendingIntent pendingReceiverStart = PendingIntent.getBroadcast(this, 0, intenUpdateStock, PendingIntent.FLAG_UPDATE_CURRENT);
        //set timer you want alarm to work (here I have set it to 7.20pm)
        Calendar timeStart = Calendar.getInstance();
        timeStart.set(Calendar.HOUR_OF_DAY, 16);
        timeStart.set(Calendar.MINUTE, 45);
        timeStart.set(Calendar.SECOND, 0);
        //set that timer as a RTC Wakeup to alarm manager object
        alarmRecerverUpdateStock.setRepeating(AlarmManager.RTC_WAKEUP, timeStart.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingReceiverStart);
    }

    private void notificationPeriodicalStockCeBigVolumn() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, 1);
        }
//        Start
        //Create alarm manager
        AlarmManager alarmRecerverStartNotification = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        //Create pending intent & register it to your alarm notifier class
        Intent intent0 = new Intent(this, SensorRestarterBroadcastReceiver.class);
        PendingIntent pendingReceiverStart = PendingIntent.getBroadcast(this, 0, intent0, PendingIntent.FLAG_UPDATE_CURRENT);
        //set timer you want alarm to work (here I have set it to 7.20pm)
        Calendar timeStart = Calendar.getInstance();
        timeStart.set(Calendar.HOUR_OF_DAY, 9);
        timeStart.set(Calendar.MINUTE, 15);
        timeStart.set(Calendar.SECOND, 0);
        //set that timer as a RTC Wakeup to alarm manager object
        alarmRecerverStartNotification.setRepeating(AlarmManager.RTC_WAKEUP, timeStart.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingReceiverStart);

        //Stop
        //Create alarm manager
        AlarmManager alarmReceiverStopNotification = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        //Create pending intent & register it to your alarm notifier class
        Intent intentStopReceiver = new Intent(this, SensorStopBoardcastReceiver.class);
        PendingIntent pendingReceiverStop = PendingIntent.getBroadcast(this, 0, intentStopReceiver, PendingIntent.FLAG_UPDATE_CURRENT);
        //set timer you want alarm to work (here I have set it to 7.20pm)
        Calendar timeStop = Calendar.getInstance();
        timeStop.set(Calendar.HOUR_OF_DAY, 11);
        timeStop.set(Calendar.MINUTE, 30);
        timeStop.set(Calendar.SECOND, 0);
        alarmReceiverStopNotification.setInexactRepeating(AlarmManager.RTC_WAKEUP, timeStop.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingReceiverStop);
    }

    private void testData() {
        MySQLiteHelper db = new MySQLiteHelper(getApplicationContext());
        Constant.maxDay = db.getMaxDayUpdate();
    }

    private void installView() {
        appTitle = findViewById(R.id.txtMainTitle);
        btnUnit = findViewById(R.id.btnUnit);
        layoutTitle = findViewById(R.id.layoutTitleMain);
        layoutTitle.setVisibility(View.VISIBLE);
        btnSearch = findViewById(R.id.btnSearch);
        btnListStock = findViewById(R.id.btnListStock);
        btnExchange = findViewById(R.id.btnExchange);
        layoutPriceboard = findViewById(R.id.layout_bottom_priceboard);
        frmBaseBody = findViewById(R.id.base_body_layout);

        btnListStock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mViewPager.setCurrentItem(5);
                btnListStock.setVisibility(View.GONE);
                btnSearch.setVisibility(View.VISIBLE);
            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mViewPager.setCurrentItem(2);
                btnListStock.setVisibility(View.VISIBLE);
                btnSearch.setVisibility(View.GONE);
            }
        });

        btnExchange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tabHost.getCurrentTab() == 0) {
                    if (SharedPreference.getInstance().getBoolean("exchange", true)) { //Default
                        SharedPreference.getInstance().putBoolean("exchange", false);
                        btnExchange.setText("-");
                    } else {
                        SharedPreference.getInstance().putBoolean("exchange", true);
                        btnExchange.setText("+");
                    }
                    FragmentHome fragmentHome = (FragmentHome) mainActivityAdapter.getItem(0);
                    fragmentHome.onChangeHome();
                } else {
                    FragmentTablePriceOnline fg;
                    if (SharedPreference.getInstance().getBoolean("ho", true)) { //Default
                        SharedPreference.getInstance().putBoolean("ho", false);
                        btnExchange.setText("V");
                        appTitle.setText(getResources().getString(R.string.txt_title_nhat));
                        if (SharedPreference.getInstance().getString("priceTable", "cafef").equalsIgnoreCase("cafef")) {
                            fg = new FragmentTablePriceOnline().newInstance(Constant.URL_DEFAULT_HNX);
                        } else {
                            fg = new FragmentTablePriceOnline().newInstance("https://iboard.ssi.com.vn/bang-gia/hnx");
                        }
                    } else {
                        SharedPreference.getInstance().putBoolean("ho", true);
                        btnExchange.setText("N");
                        appTitle.setText(getResources().getString(R.string.txt_title_vuong));
                        if (SharedPreference.getInstance().getString("priceTable", "cafef").equalsIgnoreCase("cafef")) {
                            fg = new FragmentTablePriceOnline().newInstance(Constant.URL_DEFAULT_HSX);
                        } else {
                            fg = new FragmentTablePriceOnline().newInstance(Constant.URL_OTHER_HSX);
                        }
                    }
                    if (SharedPreference.getInstance().getString("priceTable", "cafef").equalsIgnoreCase("cafef")) {
                        btnUnit.setText(getString(R.string.txt_unit_100));
                    } else {
                        btnUnit.setText(getString(R.string.txt_unit_10));
                    }

                    getSupportFragmentManager()  // or getSupportFragmentManager() if your fragment is part of support library
                            .beginTransaction()
                            .replace(R.id.base_body_layout, fg)
                            .commit();
                }
            }
        });
        //Check status update big volumn data
        MySQLiteHelper db = new MySQLiteHelper(getApplicationContext());
        Constant.maxDay = db.getMaxDayUpdate();
//        db.deleteDataTable("STOCK_BIG_VOLUME");
//        db.deleteStock("20200417");
        Calendar cal = Calendar.getInstance();
        int hourofday = cal.get(Calendar.HOUR_OF_DAY);
        dayJP = (hourofday > 14) ? Constant.nowDay : Constant.beforeDay;
        dayVN = Utils.convertDate(dayJP);
        String urlDay = Constant.URI + "/" + dayJP + "/CafeF.SolieuGD." + dayVN + ".zip";
        String dateDownload = Constant.maxDay;
        Constant.dateTransition = Constant.maxDay;
        if (dateDownload != null) {
            SharedPreference.getInstance(getApplicationContext()).putInt("updatedStock", 2);
            while ((dateDownload != null && Utils.convertStringToDateString(dateDownload).before(Utils.convertStringToDateString(dayJP)))) {
                if (Utils.exists(getApplicationContext(), urlDay)) {
                    Constant.dateTransition = dayJP;
                    if (!"".equalsIgnoreCase(Constant.maxDay) && Constant.maxDay.equalsIgnoreCase(dayJP)) {
                        SharedPreference.getInstance(getApplicationContext()).putInt("updatedStock", 1);
                    } else {
                        SharedPreference.getInstance(getApplicationContext()).putInt("updatedStock", 0);
                    }
                    break;
                }
                dayJP = Utils.getDateJP(Utils.addDays(Utils.convertStringToDateString(dayJP), -1));
                dayVN = Utils.convertDate(dayJP);
                urlDay = Constant.URI + "/" + dayJP + "/CafeF.SolieuGD." + dayVN + ".zip";
            }
            if (SharedPreference.getInstance().getInt("updatedStock", 0) == 1) {
                btnUnit.setText(getString(R.string.txt_updating));
            } else if (SharedPreference.getInstance().getInt("updatedStock", 0) == 2) {
                btnUnit.setText(getString(R.string.txt_updated));
            } else {
                btnUnit.setText(getString(R.string.txt_not_ok));
            }
        } else {
//            btnUnit.setText(getString(R.string.txt_not_ok));
            btnUnit.setText(getString(R.string.txt_updating));
        }
    }

    @Override
    public void onTabChanged(String tabId) {
        if (mViewPager != null && tabHost != null) {
            mViewPager.setCurrentItem(tabHost.getCurrentTab(), false);
        }
    }

    private void setupTab() {
        tabHost = findViewById(R.id.base_tabhost);
        tabHost.setup();
        View navHome = LayoutInflater.from(getApplicationContext()).inflate(R.layout.tab_home, null);
        TabHost.TabSpec tabHome = tabHost.newTabSpec("Home");
        tabHome.setIndicator(navHome);
        tabHome.setContent(new DummyTabFactory(this));
        tabHost.addTab(tabHome);

        View navTraining = LayoutInflater.from(getApplicationContext()).inflate(R.layout.tab_analytic, null);
        TabHost.TabSpec tabTraining = tabHost.newTabSpec("Analytic");
        tabTraining.setIndicator(navTraining);
        tabTraining.setContent(new DummyTabFactory(this));
        tabHost.addTab(tabTraining);

        View navTesting = LayoutInflater.from(getApplicationContext()).inflate(R.layout.tab_finacial, null);
        TabHost.TabSpec tabTesting = tabHost.newTabSpec("Finacial");
        tabTesting.setIndicator(navTesting);
        tabTesting.setContent(new DummyTabFactory(this));
        tabHost.addTab(tabTesting);

        View navListening = LayoutInflater.from(getApplicationContext()).inflate(R.layout.tab_result, null);
        TabHost.TabSpec tabListening = tabHost.newTabSpec("Result");
        tabListening.setIndicator(navListening);
        tabListening.setContent(new DummyTabFactory(this));
        tabHost.addTab(tabListening);

        View navSetting = LayoutInflater.from(getApplicationContext()).inflate(R.layout.tab_setting, null);
        TabHost.TabSpec tabSetting = tabHost.newTabSpec("Setting");
        tabSetting.setIndicator(navSetting);
        tabSetting.setContent(new DummyTabFactory(this));
        tabHost.addTab(tabSetting);

        tabHost.getTabWidget().setDividerDrawable(null);
        tabHost.setOnTabChangedListener(this);
        mViewPager = findViewById(R.id.mainViewPager);
        mViewPager.setSwipingView(false);
        mainActivityAdapter = new MainActivityAdapter(getSupportFragmentManager());
        mViewPager.setOffscreenPageLimit(Constant.MAX_SCREEN_HOME);
        mViewPager.setAdapter(mainActivityAdapter);
        mViewPager.addOnPageChangeListener(new ViewPagerChange());
        if (tabHost.getCurrentTab() == 0) {
            if (SharedPreference.getInstance().getBoolean("exchange", true) || SharedPreference.getInstance().getBoolean("ho", true)) { //Default
                btnExchange.setText("+");
            } else {
                btnExchange.setText("-");
            }
        }
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
//            if(tabHost.getCurrentTab()==1){
//                if(FragmentAnalytic.onFragmnet!=0){
//                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
////                    fragmentTransaction.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right);
//                    fragmentTransaction.replace(R.id.base_content, FragmentTraining.newInstance());
//                    fragmentTransaction.addToBackStack("Training");
//                    fragmentTransaction.commit();
//                }else{
//                    this.finish();
//                }
//            } else if (tabHost.getCurrentTab() == 4){
//                if(FragmentSetting.onFragment!=0){
//                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
////                    fragmentTransaction.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right);
//                    fragmentTransaction.replace(R.id.setting_content, FragmentSetting.newInstance());
//                    fragmentTransaction.addToBackStack("Setting");
//                    fragmentTransaction.commit();
//                    FragmentSetting.onFragment = 0;
//                    FragmentSetting.changeTitleView();
//                }else{
//                    this.finish();
//                }
//            }
//            else {
//                this.finish();
//            }
    }

    private void changeTitlePager(int position) {
        frmBaseBody.setVisibility(View.GONE);
        switch (position) {
            case 0:
                btnListStock.setVisibility(View.GONE);
                btnExchange.setVisibility(View.VISIBLE);
                btnUnit.setVisibility(View.VISIBLE);
                if (SharedPreference.getInstance(getApplicationContext()).getInt("updatedStock", 0) == 1) {
                    btnUnit.setText(getString(R.string.txt_updating));
                } else if (SharedPreference.getInstance(getApplicationContext()).getInt("updatedStock", 0) == 2) {
                    btnUnit.setText(getString(R.string.txt_updated));
                } else {
                    btnUnit.setText(getString(R.string.txt_not_ok));
                }
                if (SharedPreference.getInstance().getBoolean("exchange", true)) { //Default
                    btnExchange.setText(getString(R.string.txt_plus));
                } else {
                    btnExchange.setText(getString(R.string.txt_muinus));
                }
                MainActivity.layoutPriceboard.setVisibility(View.GONE);
                SharedPreference.getInstance(getApplicationContext()).putString("positionScreen", "Splash");
                FragmentHome.changeTitleView(MainActivity.this);
                FragmentHome fragmentHome = (FragmentHome) mainActivityAdapter.getItem(position);
//                fragmentHome.updateView();
                InforVolumeValueStockIndex objInforVolumeValueStockIndex = new InforVolumeValueStockIndex();
                objInforVolumeValueStockIndex = Singleton.getInstance().getObjInforVolumeValueStockIndex();
                if (objInforVolumeValueStockIndex != null) {
                    if ("".equalsIgnoreCase(SharedPreference.getInstance().getString("averIndex", "")) || SharedPreference.getInstance().getString("averIndex", "").equalsIgnoreCase("10")) {
                        String number = objInforVolumeValueStockIndex.getAverValue10().toString().substring(0, objInforVolumeValueStockIndex.getAverValue10().toString().length() - 9);
                        double amount = Double.parseDouble(number);
                        String valuerAver = String.format("%,.0f", amount);
                        PriceVolumeAgreement.txtVolume.setText(objInforVolumeValueStockIndex.getVolumeClose().substring(0, objInforVolumeValueStockIndex.getVolumeClose().length() - 9) + "-TB10: " + objInforVolumeValueStockIndex.getAverVolume10().toString().substring(0, objInforVolumeValueStockIndex.getAverVolume10().toString().length() - 6)); //+ " TB 20: "+ finalAverVolume2.toString().substring(0,finalAverVolume2.toString().length()-6)
                        PriceVolumeAgreement.txtValue.setText(objInforVolumeValueStockIndex.getValueClose().substring(0, objInforVolumeValueStockIndex.getValueClose().length() - 13) + "-TB10: " + valuerAver);
                    } else {
                        String number = objInforVolumeValueStockIndex.getAverValue20().toString().substring(0, objInforVolumeValueStockIndex.getAverValue20().toString().length() - 9);
                        double amount = Double.parseDouble(number);
                        String valuerAver = String.format("%,.0f", amount);
                        PriceVolumeAgreement.txtVolume.setText(objInforVolumeValueStockIndex.getVolumeClose().substring(0, objInforVolumeValueStockIndex.getVolumeClose().length() - 9) + "-TB20: " + objInforVolumeValueStockIndex.getAverAverVolume20().toString().substring(0, objInforVolumeValueStockIndex.getAverAverVolume20().toString().length() - 6)); //+ " TB 20: "+ finalAverVolume2.toString().substring(0,finalAverVolume2.toString().length()-6)
                        PriceVolumeAgreement.txtValue.setText(objInforVolumeValueStockIndex.getValueClose().substring(0, objInforVolumeValueStockIndex.getValueClose().length() - 13) + "-TB20: " + valuerAver);
                    }
                }
                break;
            case 1:
                frmBaseBody.setVisibility(View.VISIBLE);
                layoutPriceboard.setVisibility(View.VISIBLE);
                btnListStock.setVisibility(View.GONE);
                btnExchange.setVisibility(View.VISIBLE);
                btnUnit.setVisibility((View.VISIBLE));
                if (SharedPreference.getInstance().getBoolean("ho", true)) { //Default
                    btnExchange.setText(getString(R.string.txt_unit_n));
                } else {
                    btnExchange.setText(getString(R.string.txt_unit_v));
                }
                if (SharedPreference.getInstance().getString("priceTable", "cafef").equalsIgnoreCase("cafef")) {
                    btnUnit.setText(getString(R.string.txt_unit_100));
                } else {
                    btnUnit.setText(getString(R.string.txt_unit_10));
                }
                SharedPreference.getInstance(getApplicationContext()).putString("positionScreen", "Training");
                FragmentTablePriceOnline fragmentTablePriceOnline = (FragmentTablePriceOnline) mainActivityAdapter.getItem(position);
                FragmentTablePriceOnline.changeTitleView(MainActivity.this);
//                FragmentTablePriceOnline fragmentAnalytic = (FragmentTablePriceOnline) mainActivityAdapter.getItem(position);
//                fragmentAnalytic.updateAdapter(this);
//                if(ConstantValues.positionStartOnFragment!=-1){
//                    fragmentTraining.goToFragmnet(ConstantValues.positionStartOnFragment, "SelectListen");
//                }
                break;
            case 2:
                layoutPriceboard.setVisibility(View.GONE);
                btnListStock.setVisibility(View.VISIBLE);
                btnExchange.setVisibility(View.GONE);
                btnUnit.setVisibility(View.GONE);
                SharedPreference.getInstance(getApplicationContext()).putString("positionScreen", "Testing");
                FragmentFinacial.changeTitleView(MainActivity.this);
                FragmentFinacial fragmentFinacial = (FragmentFinacial) mainActivityAdapter.getItem(position);
                break;
            case 3:
                layoutPriceboard.setVisibility(View.GONE);
                btnListStock.setVisibility(View.GONE);
                btnExchange.setVisibility(View.GONE);
                btnUnit.setVisibility(View.GONE);
                SharedPreference.getInstance(getApplicationContext()).putString("positionScreen", "Listening");
                FragmentResult.changeTitleView(MainActivity.this);
                FragmentResult fragmentListening = (FragmentResult) mainActivityAdapter.getItem(position);
                break;
            case 4:
                layoutPriceboard.setVisibility(View.GONE);
                btnListStock.setVisibility(View.GONE);
                btnExchange.setVisibility(View.GONE);
                btnUnit.setVisibility(View.GONE);
                FragmentSetting fragmentSetting = (FragmentSetting) mainActivityAdapter.getItem(position);
                FragmentSetting.changeTitleView(MainActivity.this);
                break;
            default:
                break;
        }
    }

    private static class DummyTabFactory implements TabHost.TabContentFactory {

        private final Context mContext;

        private DummyTabFactory(Context context) {
            mContext = context;
        }

        @Override
        public View createTabContent(String tag) {
            return new View(mContext);
        }
    }

    private class ViewPagerChange implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            changeTitlePager(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (updateUIReciver != null)
            unregisterReceiver(updateUIReciver);
    }
}
