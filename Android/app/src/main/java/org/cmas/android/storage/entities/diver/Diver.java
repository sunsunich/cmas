package org.cmas.android.storage.entities.diver;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import org.cmas.android.storage.entities.User;
import com.google.myjson.annotations.Expose;
import org.cmas.entities.diver.DiverLevel;
import org.cmas.entities.diver.DiverRegistrationStatus;
import org.cmas.entities.diver.DiverType;

import java.util.Date;

/**
 * Created on Dec 04, 2015
 *
 * @author Alexander Petukhov
 */
@Entity(tableName = "DIVER")
public class Diver extends User {

    private static final long serialVersionUID = -6873304958863096818L;

    @Expose
    @ColumnInfo(name = "DIVER_LEVEL")
    public DiverLevel diverLevel;

    @Expose
    @ColumnInfo(name = "DIVER_TYPE")
    public DiverType diverType;

    @ColumnInfo(name = "SOCIAL_UPDATES_VERSION")
    public long socialUpdatesVersion;

    @Expose
    @ColumnInfo(name = "PREVIOUS_REGISTRATION_STATUS")
    public DiverRegistrationStatus previousRegistrationStatus;

    @Expose
    @ColumnInfo(name = "DIVER_REGISTRATION_STATUS")
    public DiverRegistrationStatus diverRegistrationStatus;

    @Expose
    @ColumnInfo(name = "AREA_OF_INTEREST")
    public String areaOfInterest;

    @Expose
    @ColumnInfo(name = "DATE_GOLD_STATUS_PAYMENT_IS_DUE")
    public Date dateGoldStatusPaymentIsDue;

    @Expose
    @ColumnInfo(name = "EXT_ID")
    public String extId;

    @ColumnInfo(name = "INSTRUCTOR_ID")
    public long instructorId;

    @ColumnInfo(name = "NATIONAL_FEDERATION_ID")
    public long nationalFederationId;

    public boolean isGold() {
        return dateGoldStatusPaymentIsDue != null && dateGoldStatusPaymentIsDue.after(new Date());
    }
}
