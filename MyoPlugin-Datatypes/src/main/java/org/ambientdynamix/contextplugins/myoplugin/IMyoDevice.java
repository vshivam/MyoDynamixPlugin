package org.ambientdynamix.contextplugins.myoplugin;

import org.ambientdynamix.api.application.IContextInfo;

/**
 * Created by shivam on 8/22/16.
 */
public interface IMyoDevice extends IContextInfo {

    String getName();

    String getPhysicalAddress();
}
