package org.cmas.presentation.model.divespot;



import org.cmas.Globals;
import org.cmas.presentation.validator.Validatable;
import org.cmas.presentation.validator.ValidatorUtils;
import org.hibernate.validator.Length;
import org.hibernate.validator.NotEmpty;
import org.springframework.validation.Errors;

/**
 * Created on Jul 02, 2016
 *
 * @author Alexander Petukhov
 */
public class DiveSpotFormObject implements Validatable {

    @Length(max = Globals.HALF_MAX_LENGTH)
    private String name;

    @NotEmpty(message = "validation.emptyField")
    @Length(max = Globals.HALF_MAX_LENGTH)
    private String latinName;

    @NotEmpty(message = "validation.emptyField")
    @Length(max = Globals.HALF_MAX_LENGTH)
    private String latitude;

    @NotEmpty(message = "validation.emptyField")
    @Length(max = Globals.HALF_MAX_LENGTH)
    private String longitude;

    @NotEmpty(message = "validation.emptyField")
    private String countryCode;

    @NotEmpty(message = "validation.emptyField")
    @Length(max = Globals.HALF_MAX_LENGTH)
    private String toponymName;

    private boolean isAutoGeoLocation;

    @Override
    public void validate(Errors errors) {
        ValidatorUtils.validateDouble(errors, latitude, "latitude", "validation.incorrectNumber");
        ValidatorUtils.validateDouble(errors, longitude, "longitude", "validation.incorrectNumber");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLatinName() {
        return latinName;
    }

    public void setLatinName(String latinName) {
        this.latinName = latinName;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getToponymName() {
        return toponymName;
    }

    public void setToponymName(String toponymName) {
        this.toponymName = toponymName;
    }

    public boolean isAutoGeoLocation() {
        return isAutoGeoLocation;
    }

    public void setIsAutoGeoLocation(boolean isAutoGeoLocation) {
        this.isAutoGeoLocation = isAutoGeoLocation;
    }
}
