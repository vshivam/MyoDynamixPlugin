package org.ambientdynamix.contextplugins.myoplugin;

import org.ambientdynamix.api.application.IContextInfo;

import java.util.List;

/**
 * Created by shivam on 8/22/16.
 */
public interface IMyoDeviceList extends IContextInfo {
    public List<MyoDevice> getDevices();
}
