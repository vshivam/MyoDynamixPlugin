package org.ambientdynamix.contextplugins.myoplugin;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Set;

/**
 * Created by shivam on 8/19/16.
 */
public class MyoPoseData implements IMyoPoseData {

    public static final String CONTEXT_TYPE = "org.ambientdynamix.contextplugins.myoplugin.gesture";

    public static Parcelable.Creator<MyoPoseData> CREATOR = new Parcelable.Creator<MyoPoseData>() {
        /**
         * Create a new instance of the Parcelable class, instantiating it from the given Parcel whose data had
         * previously been written by Parcelable.writeToParcel().
         */
        public MyoPoseData createFromParcel(Parcel in) {
            return new MyoPoseData(in);
        }

        /**
         * Create a new array of the Parcelable class.
         */
        public MyoPoseData[] newArray(int size) {
            return new MyoPoseData[size];
        }
    };

    String pose;

    public MyoPoseData(String pose) {
        this.pose = pose;
    }

    public MyoPoseData(Parcel in) {
        this.pose = in.readString();
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
        return pose;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(pose);
    }

    @Override
    public String getPose() {
        return pose;
    }
}
