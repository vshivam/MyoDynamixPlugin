/*
 * Copyright (C) The Ambient Dynamix Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.ambientdynamix.contextplugins.myoplugin;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import com.thalmic.myo.*;
import com.thalmic.myo.scanner.Scanner;
import org.ambientdynamix.api.contextplugin.ContextListenerInformation;
import org.ambientdynamix.api.contextplugin.ContextPluginRuntime;
import org.ambientdynamix.api.contextplugin.ContextPluginSettings;
import org.ambientdynamix.api.contextplugin.PowerScheme;

import java.util.ArrayList;
import java.util.UUID;

/***
 * @author Shivam Verma
 */
public class MyoPluginRuntime extends ContextPluginRuntime {
    private final String TAG = "MyoPluginRuntime";
    private Context context;
    private final ArrayList<UUID> attachContextEventListeners = new ArrayList<>();
    private boolean isListenerAttached = false;
    private long lastIMUDataSent = 0;
    private XDirection mXDirection = XDirection.UNKNOWN;


    private DeviceListener mListener = new AbstractDeviceListener() {
        private Arm mArm = Arm.UNKNOWN;
        private Vector3 lastAcceleration;
        private float doubleTapThreshold = 0.9f;
        private long lastTapTime;
        private int[] tapTimeWindow = {80, 1000};

        @Override
        public void onAccelerometerData(Myo myo, long timestamp, Vector3 accel) {
            if (lastAcceleration != null) {
                double delta = Math.abs(Math.abs(lastAcceleration.y()) - Math.abs(accel.y())) + Math.abs(Math.abs(lastAcceleration.z()) - Math.abs(accel.z()));
                if (delta > doubleTapThreshold) {
                    long timeDiff = System.currentTimeMillis() - lastTapTime;
                    if (timeDiff > tapTimeWindow[0] && timeDiff < tapTimeWindow[1]) {
                        Log.d(TAG, "DoubleTap");
                        sendBroadcastContextEvent(new CollisionData());
                    }
                    lastTapTime = System.currentTimeMillis();
                }
            }
            lastAcceleration = accel;
        }

        @Override
        public void onAttach(Myo myo, long timestamp) {
            Log.i(TAG, "Myo Attached: " + myo.getName());
        }

        @Override
        public void onDetach(Myo myo, long timestamp) {
            Log.i(TAG, "Myo Detached : " + myo.getName());
            setupMyo();
        }

        // https://developer.thalmic.com/forums/topic/1870/?page=1#post-7893
        @Override
        public void onConnect(Myo myo, long timestamp) {
            Log.i(TAG, "Myo collision: " + myo.getName());
            synchronized (attachContextEventListeners) {
                for (UUID uuid : attachContextEventListeners) {
                    sendContextRequestSuccess(uuid);
                }
                attachContextEventListeners.clear();
            }
        }

        @Override
        public void onDisconnect(Myo myo, long timestamp) {
            Log.i(TAG, "Myo disconnected: " + myo.getName());

        }

        @Override
        public void onArmSync(Myo myo, long timestamp, Arm arm, XDirection xDirection) {
            mArm = arm;
            mXDirection = xDirection;
            Log.i(TAG, "Arm recognized: " + myo.getName() + " at " + arm.toString());
        }

        @Override
        public void onArmUnsync(Myo myo, long timestamp) {
            mArm = Arm.UNKNOWN;
            mXDirection = XDirection.UNKNOWN;
            Log.i(TAG, "Arm lost: " + myo.getName());
        }


        @Override
        public void onOrientationData(Myo myo, long timestamp, Quaternion rotation) {
            long currentTime = System.currentTimeMillis();
//            if (currentTime - lastIMUDataSent > 150) {
            MyoIMUData data = new MyoIMUData(rotation.x(), rotation.y(), rotation.z(), rotation.w());
            sendBroadcastContextEvent(data);
            lastIMUDataSent = currentTime;
//            }
        }

        @Override
        public void onPose(Myo myo, long timestamp, Pose pose) {
            sendBroadcastContextEvent(new MyoPoseData(pose.toString()));
            Log.i(TAG, "Pose: " + pose.toString());
        }
    };

    private Scanner.OnScanningStartedListener mScanningStartedListener = new Scanner.OnScanningStartedListener() {
        @Override
        public void onScanningStarted() {
            Log.d(TAG, "BLE Scanning Started");
        }

        @Override
        public void onScanningStopped() {
            Log.d(TAG, "BLE Scanning Stopped");
        }
    };

    /**
     * Called once when the ContextPluginRuntime is first initialized. The implementing subclass should acquire the
     * resources necessary to run. If initialization is unsuccessful, the plug-ins should throw an exception and release
     * any acquired resources.
     */
    @Override
    public void init(PowerScheme powerScheme, ContextPluginSettings settings) throws Exception {
        // Set the power scheme
        this.setPowerScheme(powerScheme);
        // Store our secure context
        this.context = this.getSecuredContext();

    }


    @Override
    public void start() {
        enableBluetooth();
        if (initHub()) {
//            setupMyo();
        }
        Log.i(TAG, "Started!");
    }

    private void enableBluetooth() {
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        boolean isEnabled = mBluetoothAdapter.isEnabled();
        if (!isEnabled) {
            mBluetoothAdapter.enable();
        }
    }

    private boolean initHub() {
        if (Hub.getInstance().init(context, context.getPackageName())) {
            Hub.getInstance().addListener(mListener);
            Hub.getInstance().getScanner().addOnScanningStartedListener(mScanningStartedListener);
            isListenerAttached = true;
            return true;
        } else {
            Log.e(TAG, "Could not init Hub");
        }
        return false;
    }

    private void setupMyo() {
        if (Hub.getInstance().getConnectedDevices().size() > 0) {
            Log.i(TAG, "Already collision to a Myo");
            if (!isListenerAttached) {
                Hub.getInstance().addListener(mListener);
            }
        } else {
            Log.i(TAG, "Not collision to a Myo right now");
            Log.i(TAG, "Sending attach request");
            Hub.getInstance().attachByMacAddress("D3:15:CE:2B:E6:2B");
//            Hub.getInstance().attachToAdjacentMyo();
        }
    }

    /**
     * Called by the Dynamix Context Manager to stop context sensing or acting operations; however, any acquired
     * resources should be maintained, since start may be called again.
     */
    @Override
    public void stop() {
        try {
            Hub.getInstance().removeListener(mListener);
            Hub.getInstance().detach("D3:15:CE:2B:E6:2B");
            Hub.getInstance().shutdown();
            isListenerAttached = false;
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.i(TAG, "Stopped!");
    }

    /**
     * Stops the runtime (if necessary) and then releases all acquired resources in preparation for garbage collection.
     * Once this method has been called, it may not be re-started and will be reclaimed by garbage collection sometime
     * in the indefinite future.
     */
    @Override
    public void destroy() {
        stop();
        Hub.getInstance().getScanner().removeOnScanningStartedListener(mScanningStartedListener);
        context = null;
        Log.i(TAG, "Destroyed!");
    }

    @Override
    public void handleContextRequest(final UUID requestId, String contextType) {
        if (contextType.equals(MyoScanResult.CONTEXT_TYPE)) {
            setupMyo();
            synchronized (attachContextEventListeners) {
                attachContextEventListeners.add(requestId);
            }
        }
    }

    @Override
    public void handleConfiguredContextRequest(UUID requestId, String contextType, Bundle config) {

    }

    @Override
    public void updateSettings(ContextPluginSettings settings) {
        // Not supported
    }

    @Override
    public void setPowerScheme(PowerScheme scheme) {
        // Not supported
    }

    private ArrayList<ContextListenerInformation> imuListeners = new ArrayList<>();
    private ArrayList<ContextListenerInformation> poseListeners = new ArrayList<>();


    @Override
    public boolean addContextlistener(ContextListenerInformation listenerInfo) {
        Log.i(TAG, "Adding new listener for type: " + listenerInfo.getContextType());
        setupMyo();
        switch (listenerInfo.getContextType()) {
            case MyoIMUData.CONTEXT_TYPE:
                imuListeners.add(listenerInfo);
                break;
            case MyoPoseData.CONTEXT_TYPE:
                poseListeners.add(listenerInfo);
                break;
        }
        return true;
    }
}