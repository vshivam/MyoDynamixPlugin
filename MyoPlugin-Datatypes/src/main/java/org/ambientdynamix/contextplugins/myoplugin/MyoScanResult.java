package org.ambientdynamix.contextplugins.myoplugin;

import android.os.Parcel;

import java.util.Set;

/**
 * Created by shivam on 9/2/16.
 */
public class MyoScanResult implements ICollisionData {


    public static final String CONTEXT_TYPE = "org.ambientdynamix.contextplugins.myoplugin.discover";

    public static Creator<MyoScanResult> CREATOR = new Creator<MyoScanResult>() {
        /**
         * Create a new instance of the Parcelable class, instantiating it from the given Parcel whose data had
         * previously been written by Parcelable.writeToParcel().
         */
        public MyoScanResult createFromParcel(Parcel in) {
            return new MyoScanResult(in);
        }

        /**
         * Create a new array of the Parcelable class.
         */
        public MyoScanResult[] newArray(int size) {
            return new MyoScanResult[size];
        }
    };

    boolean connected;

    public MyoScanResult() {
        this.connected = true;
    }

    private MyoScanResult(Parcel in) {
        connected = in.readInt() != 0;
    }

    @Override
    public boolean isTrue() {
        return false;
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
//        http://stackoverflow.com/questions/6201311/how-to-read-write-a-boolean-when-implementing-the-parcelable-interface
        parcel.writeInt(connected ? 1 : 0);
    }
}
