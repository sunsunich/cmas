package org.cmas.activities.divespot;

import android.view.View;
import android.widget.TextView;
import org.cmas.R;
import org.cmas.activities.SecureActivity;
import org.cmas.entities.divespot.DiveSpot;
import org.cmas.util.android.BaseEfficientAdapter;

import java.util.List;

public class DiveSpotAdapter extends BaseEfficientAdapter<DiveSpot, DiveSpotViewHolder> {

    public DiveSpotAdapter(List<DiveSpot> data, SecureActivity activity) {
        super(data, activity, R.layout.dive_spot_row);

    }

    @Override
    protected DiveSpotViewHolder createHolder(View view) {
        return new DiveSpotViewHolder(
                (TextView) view.findViewById(R.id.name),
                (TextView) view.findViewById(R.id.latitude),
                (TextView) view.findViewById(R.id.longitude)
        );
    }

    @Override
    protected void modifyHolder(DiveSpotViewHolder holder, DiveSpot datum) {
        holder.getName().setText(datum.getName());
        holder.getLatitude().setText(activity.getString(R.string.latitude_label) + datum.getLatitude());
        holder.getLongitude().setText(activity.getString(R.string.longitude_label) + datum.getLongitude());
    }
}
