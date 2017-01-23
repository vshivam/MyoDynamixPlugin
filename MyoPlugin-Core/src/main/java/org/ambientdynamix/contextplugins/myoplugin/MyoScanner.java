package org.ambientdynamix.contextplugins.myoplugin;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by shivam on 8/22/16.
 */
public class MyoScanner implements BluetoothAdapter.LeScanCallback {
    private String TAG = "MyoScanner";
    private Context mContext;
    private BluetoothAdapter mBluetoothAdapter;
    private Runnable mScanRunnable;
    private ArrayList<MyoDevice> scanResults;

    public MyoScanner(Context context) {
        this.mContext = context;
        scanResults = new ArrayList<>();
    }

    public void scan(final long timeout, final MyoScanCallback callback) {
        Log.d(TAG, "Starting scan for BLE Devices");
        BluetoothManager bluetoothManager = (BluetoothManager) mContext.getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();
        if (mBluetoothAdapter == null) {
            callback.onFailure("Device does not support Bluetooth");
        }
        if (!mBluetoothAdapter.isEnabled()) {
            callback.onFailure("Please enable Bluetooth");
        }
        if (mScanRunnable != null) {
            callback.onFailure("Scan in progress. Please Wait");
        }
        mScanRunnable = new Runnable() {
            @Override
            public void run() {
                mBluetoothAdapter.startLeScan(MyoScanner.this);
                try {
                    Thread.sleep(timeout);
                    if (callback != null) {
                        callback.onSuccess(scanResults);
                    }
                    mBluetoothAdapter.stopLeScan(MyoScanner.this);
                    mScanRunnable = null;
                    scanResults.clear();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        new Thread(mScanRunnable).start();
    }

    @Override
    public void onLeScan(BluetoothDevice bluetoothDevice, int i, byte[] scanRecord) {
        MyoDevice device = new MyoDevice(bluetoothDevice.getAddress(), bluetoothDevice.getName());
        if (!scanResults.contains(device)) {
            scanResults.add(device);
        }
    }
}
