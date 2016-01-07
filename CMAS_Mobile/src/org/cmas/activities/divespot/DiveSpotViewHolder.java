package org.cmas.activities.divespot;

import android.widget.TextView;

public class DiveSpotViewHolder {

    private final TextView name;
    private final TextView latitude;
    private final TextView longitude;


    public DiveSpotViewHolder(TextView name,
                              TextView latitude,
                              TextView longitude ) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;

    }

    public TextView getLongitude() {
        return longitude;
    }

    public TextView getName() {
        return name;
    }

    public TextView getLatitude() {
        return latitude;
    }

}
