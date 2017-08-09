package com.vanpro.zitech125.bluetooth;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import java.lang.reflect.Method;
import java.util.List;
import java.util.UUID;

import de.greenrobot.event.EventBus;
import com.vanpro.zitech125.R;
import com.vanpro.zitech125.event.DeviceRemoveEvent;
import com.vanpro.zitech125.location.ZLocation;
import com.vanpro.zitech125.manage.NotificationMgr;
import com.vanpro.zitech125.manage.StatusManage;
import com.vanpro.zitech125.present.LocationMgr;
import com.vanpro.zitech125.ui.activity.MainActivity;
import com.vanpro.zitech125.util.AndroidUtils;
import com.vanpro.zitech125.util.AppDataManager;
import com.vanpro.zitech125.util.AppStateSaveUtil;
import com.vanpro.zitech125.util.LogUtil;
import com.vanpro.zitech125.util.StringUtil;
import com.vanpro.zitech125.util.UIHelper;

/**
 * Created by Jinsen on 16/4/14.
 */
public class BluetoothLeService extends Service {

    public final static String ACTION_GATT_CONNECTED =
            "com.vanpro.ziteach.bluetooth.le.ACTION_GATT_CONNECTED";
    public final static String ACTION_GATT_DISCONNECTED =
            "com.vanpro.ziteach.bluetooth.le.ACTION_GATT_DISCONNECTED";
    public final static String ACTION_GATT_SERVICES_DISCOVERED =
            "com.vanpro.ziteach.bluetooth.le.ACTION_GATT_SERVICES_DISCOVERED";
    public final static String ACTION_DATA_AVAILABLE =
            "com.vanpro.ziteach.bluetooth.le.ACTION_DATA_AVAILABLE";
    public final static String EXTRA_DATA =
            "com.vanpro.ziteach.bluetooth.le.EXTRA_DATA";


    public final static String ACTION_LOCATION_SUCCESS = "com.vanpro.ziteach.bluetooth.le.ACTION_LOCATION_SUCCESS";
    public final static String ACTION_LOCATION_FAIL = "com.vanpro.ziteach.bluetooth.le.ACTION_LOCATION_FAIL";

    public final static String SERVICES_IS_START = "com.vanpro.ziteach.bluetooth.IS_START";


    private final static String TAG = BluetoothLeService.class.getSimpleName();

    private BluetoothManager mBluetoothManager;
    private BluetoothAdapter mBluetoothAdapter;
    private String mBluetoothDeviceAddress;
    private BluetoothGatt mBluetoothGatt;
    private int mConnectionState = STATE_DISCONNECTED;

    private static final int STATE_DISCONNECTED = 0;
    private static final int STATE_CONNECTING = 1;
    private static final int STATE_CONNECTED = 2;

    public final static UUID UUID_HEART_RATE_MEASUREMENT =
            UUID.fromString(SampleGattAttributes.HEART_RATE_MEASUREMENT);

    // Implements callback methods for GATT events that the app cares about.  For example,
    // connection change and services discovered.
    private final BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            if(gatt != null && gatt.getDevice().getAddress() != mBluetoothDeviceAddress)
                return;
            String intentAction;
            mBluetoothGatt = gatt;
            LogUtil.e(TAG, "onConnectionStateChange state  " + newState);
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                intentAction = ACTION_GATT_CONNECTED;
                mConnectionState = STATE_CONNECTED;
                broadcastUpdate(intentAction);
                connectedAction();
            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                close();
                if (mConnectionState == STATE_CONNECTED) {
                    if (BleUtil.isBLEEnable(getApplicationContext()) && !AppDataManager.getInstance().getBoolean(AppDataManager.KEY.MAIN_IS_RUNING)) {
                        Intent intent = new Intent(BluetoothLeService.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }

                    AppDataManager.getInstance().setData(AppDataManager.KEY.LAST_PARK_TIME_KEY, Long.valueOf(System.currentTimeMillis()));

                    intentAction = ACTION_GATT_DISCONNECTED;

                    broadcastUpdate(intentAction);

                    disConnectedAction();

                }
                mConnectionState = STATE_DISCONNECTED;
            }
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            LogUtil.e(TAG, gatt + " " + status);
            if (status == BluetoothGatt.GATT_SUCCESS) {
                broadcastUpdate(ACTION_GATT_SERVICES_DISCOVERED);
            } else {
                Log.w(TAG, "onServicesDiscovered received: " + status);
            }
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt,
                                         BluetoothGattCharacteristic characteristic,
                                         int status) {
            LogUtil.e(TAG, "BluetoothGattCallback onCharacteristicRead" + gatt + " " + characteristic);
            gatt.setCharacteristicNotification(characteristic, true);
            characteristic.setValue("12312312312321");
            gatt.writeCharacteristic(characteristic);
            if (status == BluetoothGatt.GATT_SUCCESS) {
                broadcastUpdate(ACTION_DATA_AVAILABLE, characteristic);
            }
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt,
                                            BluetoothGattCharacteristic characteristic) {
            LogUtil.e(TAG, "BluetoothGattCallback onCharacteristicChanged" + gatt + " " + characteristic);
            broadcastUpdate(ACTION_DATA_AVAILABLE, characteristic);
        }
    };


    //连接蓝牙 把状态初始化
    private void connectedAction() {
        AppStateSaveUtil.reset();
        if (handler != null)
            handler.removeCallbacks(delaySendMessageRunnable);
    }

    Handler handler;//= new Handler(getMainLooper());
    Runnable delaySendMessageRunnable = new Runnable() {
        @Override
        public void run() {
            if(AppDataManager.getInstance().getBoolean(AppDataManager.KEY.LOCATION_STATE)){
                NotificationMgr.getInstance().alertLocationFailNotify(getResources().getString(R.string.alert_disconnected_text));
            }else{
                NotificationMgr.getInstance().alertDisconnectedNotify(getResources().getString(R.string.alert_locaion_fail_text));
            }
        }
    };

    private void disConnectedAction() {
        AppStateSaveUtil.navigationState();

        if (handler == null)
            handler = new Handler(getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {

                LocationMgr locationManager = LocationMgr.getInstance();
                ZLocation location = locationManager.getLocation();
                if (location == null || location.getAccuracy() >= 200) {
//                    NotificationMgr.getInstance().alertLocationFailNotify(getResources().getString(R.string.alert_locaion_fail_text));
                    UIHelper.toastMessage(BluetoothLeService.this, getString(R.string.location_fail_alert));
                    AppDataManager.getInstance().setData(AppDataManager.KEY.LOCATION_STATE, false);
                    broadcastUpdate(ACTION_LOCATION_FAIL);
                } else {
//                    NotificationMgr.getInstance().alertDisconnectedNotify(getResources().getString(R.string.alert_disconnected_text));
                    String loc = location.getLongitude() + "//" + location.getLatitude() + "//" + location.getLocation().getBearing();
                    UIHelper.toastMessage(BluetoothLeService.this, getString(R.string.location_succ_alert));
                    AppDataManager.getInstance().setData(AppDataManager.KEY.LOCATON_LAN_TYPE, AndroidUtils.getLanguage(getApplicationContext()));
                    AppDataManager.getInstance().setData(AppDataManager.KEY.LOCATION_KEY, loc);
                    AppDataManager.getInstance().setData(AppDataManager.KEY.LOCATION_STATE, true);
                    broadcastUpdate(ACTION_LOCATION_SUCCESS);
                }
            }
        });

        handler.postDelayed(delaySendMessageRunnable,15000);
    }

    private void broadcastUpdate(final String action) {
        final Intent intent = new Intent(action);
        sendBroadcast(intent);
    }

    private void broadcastUpdate(final String action,
                                 final BluetoothGattCharacteristic characteristic) {
        final Intent intent = new Intent(action);

        // This is special handling for the Heart Rate Measurement profile.  Data parsing is
        // carried out as per profile specifications:
        // http://developer.bluetooth.org/gatt/characteristics/Pages/CharacteristicViewer.aspx?u=org.bluetooth.characteristic.heart_rate_measurement.xml
        if (UUID_HEART_RATE_MEASUREMENT.equals(characteristic.getUuid())) {
            int flag = characteristic.getProperties();
            int format = -1;
            if ((flag & 0x01) != 0) {
                format = BluetoothGattCharacteristic.FORMAT_UINT16;
                LogUtil.e(TAG, "Heart rate format UINT16.");
            } else {
                format = BluetoothGattCharacteristic.FORMAT_UINT8;
                LogUtil.e(TAG, "Heart rate format UINT8.");
            }
            final int heartRate = characteristic.getIntValue(format, 1);
            LogUtil.e(TAG, String.format("Received heart rate: %d", heartRate));
            intent.putExtra(EXTRA_DATA, String.valueOf(heartRate));
        } else {
            // For all other profiles, writes the data formatted in HEX.
            final byte[] data = characteristic.getValue();
            if (data != null && data.length > 0) {
                final StringBuilder stringBuilder = new StringBuilder(data.length);
                for (byte byteChar : data)
                    stringBuilder.append(String.format("%02X ", byteChar));
                intent.putExtra(EXTRA_DATA, new String(data) + "\n" + stringBuilder.toString());
            }
        }
        sendBroadcast(intent);
    }

    public class LocalBinder extends Binder {
        public BluetoothLeService getService() {
            return BluetoothLeService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        // After using a given device, you should make sure that BluetoothGatt.close() is called
        // such that resources are cleaned up properly.  In this particular example, close() is
        // invoked when the UI is disconnected from the Service.
        //        close();
        return super.onUnbind(intent);
    }

    private final IBinder mBinder = new LocalBinder();

    /**
     * Initializes a reference to the local Bluetooth adapter.
     *
     * @return Return true if the initialization is successful.
     */
    public boolean initialize() {
        // For API level 18 and above, get a reference to BluetoothAdapter through
        // BluetoothManager.
        if (mBluetoothManager == null) {
            mBluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
            if (mBluetoothManager == null) {
                Log.e(TAG, "Unable to initialize BluetoothManager.");
                return false;
            }
        }

        mBluetoothAdapter = mBluetoothManager.getAdapter();
        if (mBluetoothAdapter == null) {
            Log.e(TAG, "Unable to obtain a BluetoothAdapter.");
            return false;
        }

        return true;
    }

    /**
     * Connects to the GATT server hosted on the Bluetooth LE device.
     *
     * @param address The device address of the destination device.
     * @return Return true if the connection is initiated successfully. The connection result
     * is reported asynchronously through the
     * {@code BluetoothGattCallback#onConnectionStateChange(android.bluetooth.BluetoothGatt, int, int)}
     * callback.
     */
    public boolean connect(final String address) {
        if (mBluetoothAdapter == null || address == null) {
            Log.w(TAG, "BluetoothAdapter not initialized or unspecified address.");
            return false;
        }

        // Previously connected device.  Try to reconnect.
        if (mConnectionState == STATE_CONNECTING && !address.equals(mBluetoothDeviceAddress)) {
            close();
        }

        if(mConnectionState == STATE_CONNECTING){
            return true;
        }

        final BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
        if (device == null) {
            Log.w(TAG, "Device not found.  Unable to connect.");
            return false;
        }

        try {
            // We want to directly connect to the device, so we are setting the autoConnect
            // parameter to false.
            mBluetoothGatt = device.connectGatt(this, false, mGattCallback);
            LogUtil.e(TAG, "Trying to create a new connection.");
            mBluetoothDeviceAddress = address;
            AppDataManager.getInstance().setData(AppDataManager.KEY.LAST_CONNECTED_DEVICES_ADDRESSS, mBluetoothDeviceAddress);
            mConnectionState = STATE_CONNECTING;
        } catch (Exception e) {
            mConnectionState = STATE_DISCONNECTED;
            return false;
        }
        return true;
    }

    /**
     * Disconnects an existing connection or cancel a pending connection. The disconnection result
     * is reported asynchronously through the
     * {@code BluetoothGattCallback#onConnectionStateChange(android.bluetooth.BluetoothGatt, int, int)}
     * callback.
     */
    public void disconnect() {
        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
            Log.w(TAG, "BluetoothAdapter not initialized");
            return;
        }
        mBluetoothGatt.disconnect();

        try {
            Method refresh = BluetoothGatt.class.getMethod("refresh");
            if (refresh != null) {
                boolean success = (Boolean) refresh.invoke(mBluetoothGatt);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * After using a given BLE device, the app must call this method to ensure resources are
     * released properly.
     */
    public void close() {
        if (mBluetoothGatt == null) {
            Log.w(TAG, "close mBluetoothGatt but mBluetoothGatt is null");
            return;
        }
        mBluetoothGatt.close();
        mBluetoothGatt = null;
    }

    /**
     * Request a read on a given {@code BluetoothGattCharacteristic}. The read result is reported
     * asynchronously through the {@code BluetoothGattCallback#onCharacteristicRead(android.bluetooth.BluetoothGatt, android.bluetooth.BluetoothGattCharacteristic, int)}
     * callback.
     *
     * @param characteristic The characteristic to read from.
     */
    public void readCharacteristic(BluetoothGattCharacteristic characteristic) {
        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
            Log.w(TAG, "BluetoothAdapter not initialized");
            return;
        }
        mBluetoothGatt.readCharacteristic(characteristic);
    }

    /**
     * Enables or disables notification on a give characteristic.
     *
     * @param characteristic Characteristic to act on.
     * @param enabled        If true, enable notification.  False otherwise.
     */
    public void setCharacteristicNotification(BluetoothGattCharacteristic characteristic,
                                              boolean enabled) {
        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
            Log.w(TAG, "BluetoothAdapter not initialized");
            return;
        }
        mBluetoothGatt.setCharacteristicNotification(characteristic, enabled);

        // This is specific to Heart Rate Measurement.
        if (UUID_HEART_RATE_MEASUREMENT.equals(characteristic.getUuid())) {
            BluetoothGattDescriptor descriptor = characteristic.getDescriptor(
                    UUID.fromString(SampleGattAttributes.CLIENT_CHARACTERISTIC_CONFIG));
            descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
            mBluetoothGatt.writeDescriptor(descriptor);
        }
    }

    /**
     * Retrieves a list of supported GATT services on the connected device. This should be
     * invoked only after {@code BluetoothGatt#discoverServices()} completes successfully.
     *
     * @return A {@code List} of supported services.
     */
    public List<BluetoothGattService> getSupportedGattServices() {
        if (mBluetoothGatt == null) return null;

        return mBluetoothGatt.getServices();
    }


    @Override
    public void onCreate() {
        super.onCreate();

        LocationMgr.getInstance();
        StatusManage.createInstance();
        AppDataManager.getInstance().setData(AppDataManager.KEY.BLUETOOTH_IS_CONNECTED, false);

        broadcastUpdate(SERVICES_IS_START);

        handler = new Handler(getMainLooper());
        initialize();
        String address = AppDataManager.getInstance().getString(AppDataManager.KEY.LAST_CONNECTED_DEVICES_ADDRESSS);
        connect(address);
        connectThread();

        EventBus.getDefault().register(this);
        LogUtil.e(TAG, "onCreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LogUtil.e(TAG, "onStartCommand");
        return START_STICKY;
    }

    public static final int FOREGROUND_ID = 1664;

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        mBluetoothDeviceAddress = null;
        disconnect();
        mConnectionState = STATE_DISCONNECTED;
        close();
        Log.e(TAG, "service destroy onTaskRemoved");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        AppDataManager.getInstance().setData(AppDataManager.KEY.BLUETOOTH_IS_CONNECTED, false);
        Log.w(TAG, "service destroy");
        handler = null;
        EventBus.getDefault().unregister(this);

        mBluetoothDeviceAddress = null;
        disconnect();
        mConnectionState = STATE_DISCONNECTED;
        close();
    }

    public void onEventMainThread(DeviceRemoveEvent event) {
        LogUtil.e(TAG, "Get Devices Remove Event");
        mBluetoothDeviceAddress = null;
        disconnect();
        mConnectionState = STATE_DISCONNECTED;
        close();
        AppStateSaveUtil.removeDevices();
    }

    private void connectThread() {
        new Thread() {
            public void run() {
                while (true) {
                    LogUtil.e(TAG, "I try to connect ble devices");
                    long sleepTime = DelayedConnectedTime;
                    if (mConnectionState == STATE_CONNECTING) {
                        if (mConectWaitCount < 0) {
                            mConnectionState = STATE_DISCONNECTED;
                            mConectWaitCount = 3;
                        } else
                            mConectWaitCount--;
                    } else if (!StatusManage.getInstance().isOpenBle()) {
                    } else if (mConnectionState == STATE_CONNECTED) {
                    } else if (mConnectionState != STATE_CONNECTED && StringUtil.isNotEmpty(mBluetoothDeviceAddress)) {
                        connect(mBluetoothDeviceAddress);
                    } else {
                        if (mBluetoothGatt != null) {
                            if (!mBluetoothGatt.connect()) {
                                mBluetoothGatt.close();
                            }
                        }
                        sleepTime = 5 * DelayedConnectedTime;
                    }
                    try {
                        sleep(sleepTime);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();

    }

    long DelayedConnectedTime = 2000;
    long sleepTime = DelayedConnectedTime;

    //正在连接，等待次数 最多3次 2*3=6S
    int mConectWaitCount = 3;

}
