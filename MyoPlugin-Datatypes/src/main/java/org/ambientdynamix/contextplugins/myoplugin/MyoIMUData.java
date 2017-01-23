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

import java.util.HashSet;
import java.util.Set;

import android.content.Intent;
import android.os.BatteryManager;
import android.os.Parcel;
import android.os.Parcelable;

class MyoIMUData implements IMyoIMUData {
    /**
     * Required CREATOR field that generates instances of this Parcelable class from a Parcel.
     *
     * @see http://developer.android.com/reference/android/os/Parcelable.Creator.html
     */
    public static Parcelable.Creator<MyoIMUData> CREATOR = new Parcelable.Creator<MyoIMUData>() {
        /**
         * Create a new instance of the Parcelable class, instantiating it from the given Parcel whose data had
         * previously been written by Parcelable.writeToParcel().
         */
        public MyoIMUData createFromParcel(Parcel in) {
            return new MyoIMUData(in);
        }

        /**
         * Create a new array of the Parcelable class.
         */
        public MyoIMUData[] newArray(int size) {
            return new MyoIMUData[size];
        }
    };
    // Public static variable for our supported context type
    public static final String CONTEXT_TYPE = "org.ambientdynamix.contextplugins.myoplugin.threeAxis";
    // Private data
    private double x;
    private double y;
    private double z;
    private double w;

    @Override
    public double getx() {
        return x;
    }

    @Override
    public double getY() {
        return y;
    }

    @Override
    public double getZ() {
        return z;
    }

    @Override
    public double getW() {
        return w;
    }

    /**
     * Returns the type of the context information represented by the IContextInfo. This string must match one of the
     * supported context information type strings described by the source ContextPlugin.
     */
    @Override
    public String getContextType() {
        return CONTEXT_TYPE;
    }

    /**
     * Returns the fully qualified class-name of the class implementing the IContextInfo interface. This allows Dynamix
     * applications to dynamically cast IContextInfo objects to their original type using reflection. A Java
     * "instanceof" compare can also be used for this purpose.
     */
    @Override
    public String getImplementingClassname() {
        return this.getClass().getName();
    }

    /**
     * Returns a Set of supported string-based context representation format types or null if no representation formats
     * are supported. Examples formats could include MIME, Dublin Core, RDF, etc. See the plug-in documentation for
     * supported representation types.
     */
    @Override
    public Set<String> getStringRepresentationFormats() {
        Set<String> formats = new HashSet<String>();
        formats.add("text/plain");
        return formats;
    }

    /**
     * Returns a string-based representation of the IContextInfo for the specified format string (e.g.
     * "application/json") or null if the requested format is not supported.
     */
    @Override
    public String getStringRepresentation(String format) {
        return "";
    }

    /**
     * Create a MyoIMUData
     */
    public MyoIMUData(double x, double y, double z, double w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }


    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }


    /**
     * Used by Parcelable when sending (serializing) data over IPC.
     */
    public void writeToParcel(Parcel out, int flags) {
        out.writeDouble(x);
        out.writeDouble(y);
        out.writeDouble(z);
        out.writeDouble(w);
    }

    /**
     * Used by the Parcelable.Creator when reconstructing (deserializing) data sent over IPC.
     */
    private MyoIMUData(final Parcel in) {
        x = in.readDouble();
        y = in.readDouble();
        z = in.readDouble();
        w = in.readDouble();
    }

    /**
     * Default implementation that returns 0.
     *
     * @return 0
     */
    @Override
    public int describeContents() {
        return 0;
    }

}