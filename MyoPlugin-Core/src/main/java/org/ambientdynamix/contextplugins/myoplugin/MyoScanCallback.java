package org.ambientdynamix.contextplugins.myoplugin;

import java.util.ArrayList;

/**
 * Created by shivam on 8/22/16.
 */
public interface MyoScanCallback {
    void onSuccess(ArrayList<MyoDevice> myos);

    void onFailure(String errorMessage);
}
