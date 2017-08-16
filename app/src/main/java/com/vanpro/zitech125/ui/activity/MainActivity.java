package com.vanpro.zitech125.ui.activity;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.umeng.socialize.UMShareAPI;
import com.vanpro.zitech125.R;
import com.vanpro.zitech125.alert.AlertUtil;
import com.vanpro.zitech125.bluetooth.BleUtil;
import com.vanpro.zitech125.bluetooth.BluetoothLeService;
import com.vanpro.zitech125.event.DeviceRemoveEvent;
import com.vanpro.zitech125.event.LocationEvent;
import com.vanpro.zitech125.event.TabChangeEvent;
import com.vanpro.zitech125.event.VerifyStoragePermissionEvent;
import com.vanpro.zitech125.location.ZLocation;
import com.vanpro.zitech125.manage.NotificationMgr;
import com.vanpro.zitech125.manage.StatusManage;
import com.vanpro.zitech125.present.LocationMgr;
import com.vanpro.zitech125.service.UpgradeManager;
import com.vanpro.zitech125.ui.dialog.CommAlertDialog;
import com.vanpro.zitech125.ui.dialog.RecommonDialog;
import com.vanpro.zitech125.ui.dialog.ShareNewDialog;
import com.vanpro.zitech125.ui.extend.BaseFragment;
import com.vanpro.zitech125.ui.fragment.BindBluetoothFragment;
import com.vanpro.zitech125.ui.fragment.CompassFragment;
import com.vanpro.zitech125.ui.fragment.FoundDevicesFragment;
import com.vanpro.zitech125.ui.fragment.MapFragment;
import com.vanpro.zitech125.ui.fragment.OpenBluetoothFragment;
import com.vanpro.zitech125.ui.fragment.SettingFragment;
import com.vanpro.zitech125.util.AndroidUtils;
import com.vanpro.zitech125.util.AppDataManager;
import com.vanpro.zitech125.util.CompassSensor;
import com.vanpro.zitech125.util.LogUtil;
import com.vanpro.zitech125.util.StringUtil;

import java.util.HashMap;

import de.greenrobot.event.EventBus;

/**
 * Created by Jinsen on 16/12
 */
public class MainActivity extends FragmentActivity {
    /**
     * 首页
     */
    public static final String TAB_COMPASS = "tab_compass";
    /**
     * 分类
     */
    public static final String TAB_MAP = "tab_map";

    /**
     * 种客中心
     */
    public static final String TAB_SETTING = "tab_setting";

    boolean mConnected = false;

    boolean mIsOpenGps = false;

    /**
     * 主界面容器
     **/
    private static FrameLayout vpContainer;

    /**
     * 底部TAB导航
     **/
    private RadioGroup rgMainNav;

    /**
     * 默认TAB位置
     **/
    public static int defaultPagerItem = 0;

    /**
     * 启动app是启动的卡页id
     **/
    private int mIndexPagerPosition = 0;

    /**
     * 当前显示卡页的ID
     */
    int mCurSelectedTabId = -1;

    private int PAGER_COUNT = 3;

    //底部的3个tab
    private RadioButton mCompassRb;
    private RadioButton mMapRb;
    private RadioButton mSettingRb;

    private ImageView mSharedBtn;
    private TextView mTitle;

    private View mCompassLine, mMapLine, mSettingLine;

    private RelativeLayout mRootView;

    private final int[] TAB_ID = new int[]{R.id.tab_compass,
            R.id.tab_map, R.id.tab_setting};
    //
    private HashMap<String, BaseFragment> mTabFragment;

    private Fragment mLastFragment = null;

    private boolean hasShowOpenBtView = false;
    private OpenBluetoothFragment mOpenBtFragment = null;

    FoundDevicesFragment mFoundDevicesFragment = null;

    CompassSensor mCompassSensor = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_main);
        initView();
        setLisetener();

        StatusManage.createInstance();
        mCompassSensor = new CompassSensor(this);

        new UpgradeManager(this).update();

        EventBus.getDefault().register(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            AndroidUtils.verifyLocationPermissions(this);
        }

        LocationMgr.getInstance();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        selectPager(0);
    }

    public void initView() {
        registerReceivers();

        vpContainer = (FrameLayout) findViewById(R.id.vp_container);
        rgMainNav = (RadioGroup) findViewById(R.id.rg_main_nav);

        mCompassRb = (RadioButton) findViewById(R.id.tab_compass);
        mMapRb = (RadioButton) findViewById(R.id.tab_map);
        mSettingRb = (RadioButton) findViewById(R.id.tab_setting);
        mRootView = (RelativeLayout) findViewById(R.id.content_view);

        mCompassLine = findViewById(R.id.tab_compass_line);
        mMapLine = findViewById(R.id.tab_map_line);
        mSettingLine = findViewById(R.id.tab_setting_line);

        mSharedBtn = (ImageView) findViewById(R.id.main_title_share_btn);
        mTitle = (TextView) findViewById(R.id.main_title_textview);

        if (isOpenGps()) {
            initFragment();
        } else {
            mIsOpenGps = false;
            toOpenGps();
        }
    }

    private void registerReceivers() {
        registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
        registerReceiver(mBleReceiver, makeBleIntentFilter());
    }

    public void selectPager(int position) {
        rgMainNav.check(TAB_ID[position]);
    }

    //显示选中的卡页
    private void showTabFragment(int position) {
        if (position > -1 && position < PAGER_COUNT && mCurSelectedTabId != position) {
            if (mTabFragment == null) {
                mTabFragment = new HashMap<>();
            }
            if (vpContainer != null) {
                BaseFragment fragment = null;
                String tag = null;
                switch (position) {
                    case 0:
                        tag = TAB_COMPASS;//CompassSensor
                        fragment = mTabFragment.get(tag);
                        if (fragment == null) {
                            fragment = new CompassFragment();
//                            fragment = new OpenBluetoothFragment();
                            mTabFragment.put(tag, fragment);
                        }
                        break;
                    case 1:
                        tag = TAB_MAP;//Map
                        fragment = mTabFragment.get(tag);
                        if (fragment == null) {
                            fragment = new MapFragment();
                            mTabFragment.put(tag, fragment);
                        }
                        break;
                    case 2:
                        tag = TAB_SETTING;     //Set up
                        fragment = mTabFragment.get(tag);
                        if (fragment == null) {
                            fragment = new SettingFragment();
                            mTabFragment.put(tag, fragment);
                        }
                        break;
                }
                mCurSelectedTabId = position;
                mIndexPagerPosition = position;
                switchFragment(mLastFragment, fragment);
                if (fragment instanceof MapFragment) {
                    fragment.setUserVisibleHint(true);
                }
                setTabLineState(mCurSelectedTabId);
                mTitle.setText(fragment.getTitle());
            }
        }
    }

    /**
     * 切换页面的重载，fragment的切换
     */
    public void switchFragment(Fragment last, Fragment next) {
        if (next == null)
            return;
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();

        //next fragment是否已经加入到activity 中
        if (!next.isAdded()) {
            // 隐藏当前的fragment，add下一个到Activity中
            if (last != null)
                transaction.hide(last).add(R.id.vp_container, next);
            else//add next fragment 到activity中
                transaction.add(R.id.vp_container, next);
        } else {
            // 隐藏last fragment，显示next
            transaction.hide(last).show(next);
        }
        mLastFragment = next;
        transaction.commitAllowingStateLoss();
    }

    private void setTabLineState(int position) {
        mCompassLine.setVisibility(View.INVISIBLE);
        mMapLine.setVisibility(View.INVISIBLE);
        mSettingLine.setVisibility(View.INVISIBLE);

        switch (position) {
            case 0:
                mCompassLine.setVisibility(View.VISIBLE);
                break;

            case 1:
                mMapLine.setVisibility(View.VISIBLE);
                break;

            case 2:
                mSettingLine.setVisibility(View.VISIBLE);
                break;
        }
    }

    public void setLisetener() {
        // 导航按钮点击选择事件，RadioGroup返回RadioButton的checkedId，通过ID转换成ViewPager需要的索引
        rgMainNav.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int pos = 0;
                switch (checkedId) {
                    case R.id.tab_compass:
                        pos = 0;
                        break;
                    case R.id.tab_map:
                        pos = 1;
                        break;
                    case R.id.tab_setting:
                        pos = 2;
                        break;
                }

                if (pos == 0 || pos == 1) {
                    mSharedBtn.setVisibility(View.VISIBLE);
                } else {
                    mSharedBtn.setVisibility(View.GONE);
                }

                showTabFragment(pos);
            }
        });

        mSharedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "";
                BaseFragment baseFragment = mTabFragment.get(TAB_MAP);
                if (baseFragment != null) {
                    MapFragment mapFragment = (MapFragment) baseFragment;
                    if (mapFragment != null) {
                        ZLocation location = mapFragment.getmCurrentLocation();
                        if (location!=null){
                            int type = location.getType();
                            url = "http://maps.google.com/maps?q=Dan@" + location.getLatitude() + "," + location.getLongitude();
                        }
                        //                    if (type == ZLocation.GPS){
//                    }else if (type == ZLocation.BD){
//                        url = "http://api.map.baidu.com/marker?location="+location.getBDLocation().getLongitude()+","+location.getBDLocation().getLatitude()+"&title=MyLocation&output=html";
//                    }
                    }
                }
                new RecommonDialog(MainActivity.this,url).show();
            }
        });
    }


    private void initFragment() {
        mIsOpenGps = true;
        registerGPSChanageListener();

        if (!BleUtil.isBLEEnable(this))
            autoOpenBLE();

        startService(new Intent(this, BluetoothLeService.class));

        mIndexPagerPosition = 0;
        showTabFragment(mIndexPagerPosition);
        showContentFragment();
    }

    boolean mGpsLastState = true;
    private final ContentObserver mGpsMonitor = new ContentObserver(null) {
        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);

            boolean enabled = ((LocationManager) getSystemService(Context.LOCATION_SERVICE))
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);
            if (mGpsLastState == enabled)
                return;
            if (enabled) {
                if (dialog != null)
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            dialog.dismissDialog();
                        }
                    });

            } else {
                if (dialog == null || !dialog.isShowing()) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            toOpenGps();
                        }
                    });
                }
            }
            mGpsLastState = enabled;
        }
    };

    private void isOpenBluetooth() {
        // 打开蓝牙
        if (!BleUtil.isBLEEnable(this)) {
            showOpenBtFragment();
        } else {
            removeOpenBtFragment();
        }
    }

    private void autoOpenBLE() {
        Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        //设置蓝牙可见性，最多300秒
        intent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
        startActivity(intent);
    }

    private boolean isOpenGps() {
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    private void showContentFragment() {
        if (AppDataManager.getInstance().getBoolean(AppDataManager.KEY.IS_NAVIGATION)) {
            if (AppDataManager.getInstance().getBoolean(AppDataManager.KEY.LOCATION_STATE)) {
                if (mCompassSensor == null)
                    mCompassSensor = new CompassSensor(this);
                mCompassSensor.start();
                StatusManage.getInstance().setStatusLoctionSucc();
            } else {
                verifyStorageP();
                StatusManage.getInstance().setStatusLoctionFail();
            }
        } else if (StringUtil.isNotEmpty(AppDataManager.getInstance().getString(AppDataManager.KEY.LAST_CONNECTED_DEVICES_ADDRESSS))) {
            StatusManage.getInstance().setStatusConnecting();
        } else {
            showFoundDevicesFragment();
        }
    }

    private void showOpenBtFragment() {
        StatusManage.getInstance().setOpenBle(false);
        if (mOpenBtFragment != null)
            return;
        hasShowOpenBtView = true;
        mOpenBtFragment = new OpenBluetoothFragment();
        FragmentManager fm = this.getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.content_view, mOpenBtFragment);
        ft.commitAllowingStateLoss();
    }

    private void removeOpenBtFragment() {
        StatusManage.getInstance().setOpenBle(true);
        if (mOpenBtFragment == null) return;

        hasShowOpenBtView = false;
        FragmentManager fm = this.getSupportFragmentManager();
        fm.beginTransaction().remove(mOpenBtFragment).commitAllowingStateLoss();
        mOpenBtFragment = null;
    }

    private void showFoundDevicesFragment() {
        if (mFoundDevicesFragment != null)
            return;

        mFoundDevicesFragment = new FoundDevicesFragment();
        FragmentManager fm = this.getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.content_view, mFoundDevicesFragment);
        ft.commitAllowingStateLoss();
    }

    private void hideFoundDevicesFragment() {
        if (mFoundDevicesFragment == null) return;
        FragmentManager fm = this.getSupportFragmentManager();
        fm.beginTransaction().remove(mFoundDevicesFragment).commitAllowingStateLoss();
        mFoundDevicesFragment = null;
    }

    CommAlertDialog dialog;

    private void toOpenGps() {
        if (dialog == null) {
            dialog = new CommAlertDialog(this);
            dialog.setCancelable(false);
            dialog.setTitle(getString(R.string.alert_open_gps_title));
            dialog.setRightBtn(getString(R.string.alert_open_gps_setting_btn), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            openGps();
                        }
                    }

            );
        }
        dialog.show();
    }

    int CODE_OPEN_GPS = 0x101;

    private void openGps() {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try {
            startActivityForResult(intent, CODE_OPEN_GPS);
        } catch (ActivityNotFoundException ex) {
            intent.setAction(Settings.ACTION_SETTINGS);
            try {
                startActivityForResult(intent, CODE_OPEN_GPS);
            } catch (Exception e) {
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CODE_OPEN_GPS) {
            if (resultCode == Activity.RESULT_OK) {
                if (dialog != null)
                    dialog.dismissDialog();
                initFragment();
            }
        } else if (mTabFragment.containsKey(TAB_COMPASS)) {
            mTabFragment.get(TAB_COMPASS).onActivityResult(requestCode, resultCode, data);
        }
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    private void registerGPSChanageListener() {
        //监听GPS的开关状态
        getContentResolver()
                .registerContentObserver(
                        Settings.Secure
                                .getUriFor(Settings.System.LOCATION_PROVIDERS_ALLOWED),
                        false, mGpsMonitor);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        AppDataManager.getInstance().setData(AppDataManager.KEY.MAIN_IS_RUNING, true);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!mIsOpenGps && isOpenGps()) {
            if (dialog != null)
                dialog.dismissDialog();
            initFragment();
        }
        isOpenBluetooth();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        AppDataManager.getInstance().setData(AppDataManager.KEY.MAIN_IS_RUNING, false);
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);// 注意
        intent.addCategory(Intent.CATEGORY_HOME);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mGattUpdateReceiver);
        unregisterReceiver(mBleReceiver);
        EventBus.getDefault().unregister(this);
        if (mCompassSensor != null)
            mCompassSensor.stop();
        mCompassSensor = null;
        stopService(new Intent(this, BluetoothLeService.class));
        getContentResolver().unregisterContentObserver(mGpsMonitor);
    }

    public final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();

            if (BluetoothLeService.ACTION_GATT_CONNECTED.equals(action)) {
                mConnected = true;
                hideFoundDevicesFragment();
                StatusManage.getInstance().setStatusConected();
                NotificationMgr.getInstance().cancelAll();
                AlertUtil.clearAlertNofity(MainActivity.this);
                if (mCompassSensor != null)
                    mCompassSensor.stop();
            } else if (BluetoothLeService.ACTION_GATT_DISCONNECTED.equals(action)) {
                if (mConnected) {
                    mConnected = false;
                }
                hideFoundDevicesFragment();

            } else if (BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {

            } else if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) {
            } else if (BluetoothLeService.ACTION_LOCATION_SUCCESS.equals(action)) {
                if (mCompassSensor == null)
                    mCompassSensor = new CompassSensor(MainActivity.this);
                mCompassSensor.start();
                StatusManage.getInstance().setStatusLoctionSucc();
            } else if (BluetoothLeService.ACTION_LOCATION_FAIL.equals(action)) {
                verifyStorageP();//检查是否有读写sd卡权限，如果没有则申请权限
                StatusManage.getInstance().setStatusLoctionFail();
            }

            selectPager(0);
        }
    };

    private void connectBluetootkSucc() {
        FragmentManager fm = this.getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.content_view, new BindBluetoothFragment());
        ft.commit();
    }

    private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE);
        intentFilter.addAction(BluetoothLeService.ACTION_LOCATION_FAIL);
        intentFilter.addAction(BluetoothLeService.ACTION_LOCATION_SUCCESS);
        return intentFilter;
    }

    private static IntentFilter makeBleIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        intentFilter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);
        intentFilter.addAction(BluetoothLeService.SERVICES_IS_START);
        return intentFilter;
    }

    public final BroadcastReceiver mBleReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
                int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE,
                        BluetoothAdapter.ERROR);
                switch (state) {
                    case BluetoothAdapter.STATE_TURNING_OFF:
                        showOpenBtFragment();
                        NotificationMgr.getInstance().alertNormalNotify(getString(R.string.ble_close_alert));
                        break;
                    case BluetoothAdapter.STATE_TURNING_ON:
                        removeOpenBtFragment();
                        NotificationMgr.getInstance().cancelNormalNotify();
                        break;
                    case BluetoothAdapter.STATE_DISCONNECTED:

                        break;
                }
            } else if (action.equals(BluetoothDevice.ACTION_ACL_DISCONNECTED)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                LogUtil.e("aaa", device.getName() + " ACTION_ACL_DISCONNECTED");
            } else if (action.equals(BluetoothLeService.SERVICES_IS_START)) {
                if (mConnected) {
                    showFoundDevicesFragment();
                }
            }
        }
    };


    /**
     * 用户点击remove按钮后发出的事件
     * 显示查找蓝牙设备界面
     *
     * @param event
     */
    public void onEventMainThread(DeviceRemoveEvent event) {
        showFoundDevicesFragment();
    }

    ZLocation mCurLoction;

    public void onEventMainThread(LocationEvent event) {
        ZLocation location = event.location;
        mCurLoction = location;
        StatusManage.getInstance().setCurLocation(mCurLoction);
    }

    public void onEventMainThread(TabChangeEvent event) {
        selectPager(event.tab);
    }

    /**
     * 需要申请sd卡读写权限
     *
     * @param event
     */
    public void onEventMainThread(VerifyStoragePermissionEvent event) {
        verifyStorageP();
    }

    /**
     * 申请读写sd卡权限
     * 如果系统版本大于等于23（6.0）
     * 则申请sd卡读写权限
     */
    private void verifyStorageP() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            AndroidUtils.verifyStoragePermissions(this);
        }
    }


}