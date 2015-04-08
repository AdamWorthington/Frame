package com.frame.app.Core;

import android.os.Parcel;
import android.os.Parcelable;

public class MediaContentParcelable implements Parcelable {
    private int mData;

    /* everything below here is for implementing Parcelable */

    // 99.9% of the time you can just ignore this
    public int describeContents() {
        return 0;
    }

    // write your object's data to the passed-in Parcel
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(mData);
    }

    // this is used to regenerate your object. All Parcelables must have a CREATOR that implements these two methods
    public static final Parcelable.Creator<MediaContentParcelable> CREATOR = new Parcelable.Creator<MediaContentParcelable>() 
    {
        public MediaContentParcelable createFromParcel(Parcel in) {
            return new MediaContentParcelable(in);
        }

        public MediaContentParcelable[] newArray(int size) {
            return new MediaContentParcelable[size];
        }
    };

    // example constructor that takes a Parcel and gives you an object populated with it's values
    private MediaContentParcelable(Parcel in) {
        mData = in.readInt();
    }
}