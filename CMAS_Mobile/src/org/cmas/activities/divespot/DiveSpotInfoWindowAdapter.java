package org.cmas.activities.divespot;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import org.cmas.R;

/**
 * User: ABadretdinov
 * Date: 21.03.14
 * Time: 13:40
 */
public class DiveSpotInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {
    private View view;

    public DiveSpotInfoWindowAdapter(Context context) {
        LayoutInflater inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view=inflater.inflate(R.layout.dive_spot_balloon,null,false);
    }

    @Override
    public View getInfoWindow(Marker marker) {
        TextView title= (TextView) view.findViewById(R.id.spot_name);
        title.setText(marker.getTitle());
        return view;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }
}
