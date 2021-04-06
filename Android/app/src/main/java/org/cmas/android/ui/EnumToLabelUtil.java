package org.cmas.android.ui;

import androidx.annotation.StringRes;
import com.cmas.cmas_flutter.R;
import org.cmas.entities.diver.AreaOfInterest;

public final class EnumToLabelUtil {

    private EnumToLabelUtil() {
    }

    @StringRes
    public static int getResourceForAreaOfInterest(AreaOfInterest areaOfInterest) {
        switch (areaOfInterest) {
            case SCUBA_DIVING:
                return R.string.area_of_interest_scuba_diving;
            case FREE_DIVING:
                return R.string.area_of_interest_free_diving;
            case BLUE_HELMETS:
                return R.string.area_of_interest_blue_helmets;
            case OTHER:
                return R.string.area_of_interest_other;
        }
        throw new IllegalArgumentException("unknown areaOfInterest " + areaOfInterest);
    }
}
