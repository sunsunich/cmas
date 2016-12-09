package org.cmas.activities.divespot;

import android.view.View;
import android.widget.TextView;
import org.cmas.activities.SecureActivity;
import org.cmas.entities.divespot.DiveSpot;
import org.cmas.mobile.R;
import org.cmas.util.android.BaseEfficientAdapter;

import java.util.List;

public class DiveSpotAdapter extends BaseEfficientAdapter<DiveSpot, DiveSpotViewHolder> {

    public DiveSpotAdapter(List<DiveSpot> data, SecureActivity activity) {
        super(data, activity, R.layout.dive_spot_row);

    }

    @Override
    protected DiveSpotViewHolder createHolder(View view) {
        return new DiveSpotViewHolder(
                (TextView) view.findViewById(R.id.name)
        );
    }

    @Override
    protected void modifyHolder(DiveSpotViewHolder holder, DiveSpot datum) {
        holder.getName().setText(datum.getName());

    }
}
