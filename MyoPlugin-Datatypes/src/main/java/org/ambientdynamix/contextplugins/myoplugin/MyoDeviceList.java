package org.ambientdynamix.contextplugins.myoplugin;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by shivam on 8/22/16.
 */
public class MyoDeviceList implements IMyoDeviceList {
    private ArrayList<MyoDevice> myoDevices = new ArrayList<>();
    public static final String CONTEXT_TYPE = "org.ambientdynamix.contextplugins.myoplugin.device_list";
    public static Parcelable.Creator<MyoDeviceList> CREATOR = new Parcelable.Creator<MyoDeviceList>() {
        /**
         * Create a new instance of the Parcelable class, instantiating it from the given Parcel whose data had
         * previously been written by Parcelable.writeToParcel().
         */
        public MyoDeviceList createFromParcel(Parcel in) {
            return new MyoDeviceList(in);
        }

        /**
         * Create a new array of the Parcelable class.
         */
        public MyoDeviceList[] newArray(int size) {
            return new MyoDeviceList[size];
        }
    };

    public MyoDeviceList(Parcel in) {
        in.readList(myoDevices, MyoDevice.class.getClassLoader());
    }

    public MyoDeviceList(ArrayList<MyoDevice> myoDevices) {
        this.myoDevices = new ArrayList<>(myoDevices);
    }

    @Override
    public List<MyoDevice> getDevices() {
        return myoDevices;
    }

    @Override
    public String getContextType() {
        return CONTEXT_TYPE;
    }

    @Override
    public String getImplementingClassname() {
        return this.getClass().getName();
    }

    @Override
    public Set<String> getStringRepresentationFormats() {
        return null;
    }

    @Override
    public String getStringRepresentation(String s) {
        return null;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeList(myoDevices);
    }
}
