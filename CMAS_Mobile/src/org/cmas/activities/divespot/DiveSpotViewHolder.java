package org.cmas.activities.divespot;

import android.widget.TextView;

public class DiveSpotViewHolder {

    private final TextView name;
    
    public DiveSpotViewHolder(TextView name) {
        this.name = name;
    }

    public TextView getName() {
        return name;
    }

}
