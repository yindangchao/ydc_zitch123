package com.vanpro.zitech125.ui.fragment;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.view.View;
import android.widget.TextView;

import com.vanpro.zitech125.R;
import com.vanpro.zitech125.bluetooth.BleUtil;
import com.vanpro.zitech125.bluetooth.BluetoothLeService;
import com.vanpro.zitech125.ui.extend.BaseFragment;
import com.vanpro.zitech125.ui.widget.ScanView;
import com.vanpro.zitech125.util.LogUtil;
import com.vanpro.zitech125.util.StringUtil;
import com.vanpro.zitech125.util.umengsdk.UMengShareListener;
import com.vanpro.zitech125.util.umengsdk.UMengUtils;

import java.util.List;
import java.util.Locale;

/**
 * 搜索蓝牙设备
 * <p/>
 * Created by Jinsen on 16/4/8.
 */
public class FoundDevicesFragment extends BaseFragment implements View.OnClickListener {
    ScanView mRadarView;
    TextView mTryAgainView;

    BluetoothAdapter mBluetoothAdapter;

    private boolean mScanning;
    private Handler mHandler;

    private static final int REQUEST_ENABLE_BT = 1;
    // Stops scanning after 10 seconds.
    private static final long SCAN_PERIOD = 300000;

    String UUIDSTR = "0000ffe0-0000-1000-8000-00805f9b34fb";

    public static final String ZITECH_BLUETOOTH_NAME = "Car Locator";

    @Override
    protected int getLayoutResourceId() {
        return R.layout.fragment_found_devices;
    }

    @Override
    protected void initView() {
        mRadarView = (ScanView) findViewById(R.id.found_devices_radar);
        mTryAgainView = (TextView) findViewById(R.id.found_devices_again);
    }

    @Override
    protected void initListener() {
        mTryAgainView.setOnClickListener(this);
        getView().setOnClickListener(null);
    }

    @Override
    protected void initData() {
        // selectively disable BLE-related features.
        if (!BleUtil.isBLESupported(getContext())) {
            showToast(R.string.ble_not_supported);
            getActivity().finish();
        }

        //如果使用ActivityCompat.requestPermissions，不会调用onRequestPermissionsResult()
        //请求权限
        requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 111);
//        //判断是否需要 向用户解释，为什么要申请该权限
//        shouldShowRequestPermissionRationale(Manifest.permission.READ_CONTACTS);
        mHandler = new Handler();

        // Initializes a Bluetooth adapter.  For API level 18 and above, get a reference to
        // BluetoothAdapter through BluetoothManager.
        final BluetoothManager bluetoothManager = (BluetoothManager) getContext().getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();

        // 打开蓝牙
        if (!mBluetoothAdapter.isEnabled()) {
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            //设置蓝牙可见性，最多300秒
            intent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
            startActivity(intent);
        }

        // Checks if Bluetooth is supported on the device.
        if (mBluetoothAdapter == null) {
            showToast(R.string.ble_not_supported);
            getActivity().finish();
            return;
        }

        Intent gattServiceIntent = new Intent(getActivity(), BluetoothLeService.class);
        getActivity().bindService(gattServiceIntent, mServiceConnection, Context.BIND_AUTO_CREATE);
        scanLeDevice(false);
    }

    private void scanLeDevice(final boolean enable) {
        if (enable) {
            // Stops scanning after a pre-defined scan period.
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mScanning = false;
                    mBluetoothAdapter.stopLeScan(mLeScanCallback);
                    mRadarView.stop();
                    mTryAgainView.setVisibility(View.VISIBLE);
                }
            }, SCAN_PERIOD);
            mRadarView.start();
            mScanning = true;
            mTryAgainView.setVisibility(View.GONE);
            mBluetoothAdapter.startLeScan(mLeScanCallback);
//            mBluetoothAdapter.startLeScan(new UUID[]{UUID.fromString(UUIDSTR)}, mLeScanCallback);
        } else {
            mRadarView.stop();
            mScanning = false;
            mBluetoothAdapter.stopLeScan(mLeScanCallback);
        }
    }

    BluetoothLeService mBluetoothLeService;
    // Code to manage Service lifecycle.
    private final ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            mBluetoothLeService = ((BluetoothLeService.LocalBinder) service).getService();
            if (!mBluetoothLeService.initialize()) {
                getActivity().finish();
            }
            // Automatically connects to the device upon successful start-up initialization.
            if (StringUtil.isNotEmpty(mDeviceAddress))
                mBluetoothLeService.connect(mDeviceAddress);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mBluetoothLeService = null;
        }
    };

    String devicesNames = "Try again\n";
    String mDeviceAddress = null;
    // Device scan callback.
    private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(final BluetoothDevice device, int rssi, byte[] scanRecord) {
            if (getActivity() == null)
                return;
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    String name = device.getName();
                    if (StringUtil.isNotEmpty(name))
                        name = name.trim().toUpperCase();
                    if (StringUtil.isNotEmpty(name) && name.contains(ZITECH_BLUETOOTH_NAME.toUpperCase()) && mDeviceAddress == null) {
                        mDeviceAddress = device.getAddress();
                        boolean result = mBluetoothLeService.connect(mDeviceAddress);
                        LogUtil.e("xxxxxxxx", "Connect request result=" + result);
                        if (result) {
//                            scanLeDevice(false);
                        }
                    }
                }
            });
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unbindService(mServiceConnection);
    }

    String imgUrl = "http://image.tianjimedia.com/uploadImages/2016/336/38/K7665HIU7L5F.jpg";

    @Override
    public void onClick(View v) {
        if (mScanning)
            return;
        else
            scanLeDevice(true);
    }

    private void shareUmplatforms() {
        UMengUtils.share(getActivity(), "", "", imgUrl, imgUrl, new UMengShareListener() {

            @Override
            public void onStart(UMengUtils.SHARE_PLATFORM platform) {

            }

            @Override
            public void onResult(UMengUtils.SHARE_PLATFORM platform) {

            }

            @Override
            public void onError(UMengUtils.SHARE_PLATFORM platform, Throwable throwable) {

            }

            @Override
            public void onCancel(UMengUtils.SHARE_PLATFORM platform) {

            }
        });
    }

    private void shareViber() {
        boolean found = false;
        Intent share = new Intent(android.content.Intent.ACTION_SEND);
        share.setType("text/plain");

        // gets the list of intents that can be loaded.
        List<ResolveInfo> resInfo = getContext().getPackageManager()
                .queryIntentActivities(share, 0);
        if (!resInfo.isEmpty()) {
            for (ResolveInfo info : resInfo) {
                if (info.activityInfo.packageName.toLowerCase(
                        Locale.getDefault()).contains("com.viber.voip")
                        || info.activityInfo.name.toLowerCase(
                        Locale.getDefault()).contains("com.viber.voip")) {
                    share.putExtra(Intent.EXTRA_TEXT, "Your text to share");
                    share.setPackage(info.activityInfo.packageName);
                    found = true;
                    startActivity(Intent.createChooser(share, "Select"));
                    break;
                }
            }
            if (!found) {

                Uri marketUri = Uri.parse("market://details?id="
                        + "com.viber.voip");
                Intent marketIntent = new Intent(Intent.ACTION_VIEW, marketUri);
                startActivity(marketIntent);
            }

        }
    }
}
