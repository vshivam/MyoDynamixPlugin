package org.ambientdynamix.contextplugins.myoplugin;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Set;

/**
 * Created by shivam on 8/22/16.
 */
public class MyoDevice implements IMyoDevice, Parcelable {
    private String physicalAddress;
    private String name;
    public static final String CONTEXT_TYPE = "org.ambientdynamix.contextplugins.myoplugin.device_operation";


    public MyoDevice(String physicalAddress, String name) {
        this.physicalAddress = physicalAddress;
        this.name = name;
    }

    public static Parcelable.Creator<MyoDevice> CREATOR = new Parcelable.Creator<MyoDevice>() {
        /**
         * Create a new instance of the Parcelable class, instantiating it from the given Parcel whose data had
         * previously been written by Parcelable.writeToParcel().
         */
        public MyoDevice createFromParcel(Parcel in) {
            return new MyoDevice(in);
        }

        /**
         * Create a new array of the Parcelable class.
         */
        public MyoDevice[] newArray(int size) {
            return new MyoDevice[size];
        }
    };

    /**
     * Used by the Parcelable.Creator when reconstructing (deserializing) data sent over IPC.
     */
    private MyoDevice(final Parcel in) {
        physicalAddress = in.readString();
        name = in.readString();
    }

    @Override
    /**
     * Used by Parcelable when sending (serializing) data over IPC.
     */
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(physicalAddress);
        out.writeString(name);
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


    public String getName() {
        return name;
    }

    public String getPhysicalAddress() {
        return physicalAddress;
    }

    @Override
    public boolean equals(Object o) {
        return (((MyoDevice) o).getPhysicalAddress().equals(this.getPhysicalAddress()));
    }
}
