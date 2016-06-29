package org.cmas.presentation.model.divespot;

/**
 * Created on Jun 27, 2016
 *
 * @author Alexander Petukhov
 */
public class LatLngBounds {
    private double swLatitude;
    private double swLongitude;

    private double neLatitude;
    private double neLongitude;

    public double getSwLatitude() {
        return swLatitude;
    }

    public void setSwLatitude(double swLatitude) {
        this.swLatitude = swLatitude;
    }

    public double getSwLongitude() {
        return swLongitude;
    }

    public void setSwLongitude(double swLongitude) {
        this.swLongitude = swLongitude;
    }

    public double getNeLatitude() {
        return neLatitude;
    }

    public void setNeLatitude(double neLatitude) {
        this.neLatitude = neLatitude;
    }

    public double getNeLongitude() {
        return neLongitude;
    }

    public void setNeLongitude(double neLongitude) {
        this.neLongitude = neLongitude;
    }
}
