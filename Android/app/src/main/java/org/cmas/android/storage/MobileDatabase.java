package org.cmas.android.storage;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import org.cmas.android.storage.dao.CountryDao;
import org.cmas.android.storage.dao.ErrorCodeDao;
import org.cmas.android.storage.dao.NationalFederationDao;
import org.cmas.android.storage.entities.CmasTypeConverters;
import org.cmas.android.storage.entities.Country;
import org.cmas.android.storage.entities.CountryToponymCrossRef;
import org.cmas.android.storage.entities.Toponym;
import org.cmas.android.storage.entities.UserFile;
import org.cmas.android.storage.entities.cards.PersonalCard;
import org.cmas.android.storage.entities.diver.Diver;
import org.cmas.android.storage.entities.i18n.ErrorCode;
import org.cmas.android.storage.entities.sport.NationalFederation;

@Database(entities = {
        ErrorCode.class,
        Country.class,
        Toponym.class,
        CountryToponymCrossRef.class,
        NationalFederation.class,
        PersonalCard.class,
        Diver.class,
        UserFile.class
}, version = 1)
@TypeConverters({CmasTypeConverters.class})
public abstract class MobileDatabase extends RoomDatabase {

    private static MobileDatabase INSTANCE;

    public static void initialize(Context context) {
        //TODO: whole database encryption when the time is right, uncomment the following line
        //SafeHelperFactory safeHelperFactory = new SafeHelperFactory(databaseKey);
        INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                        MobileDatabase.class, "database")
                       //TODO: whole database encryption when the time is right, uncomment the following line
                       //.openHelperFactory(safeHelperFactory)
                       //    .addMigrations(Migrations.migrations)
                       //    .allowMainThreadQueries()
                       .build();
    }

    public static MobileDatabase getInstance() {
        return INSTANCE;
    }

    public static void finalise() {
        if (INSTANCE != null) {
            INSTANCE.close();
            INSTANCE = null;
        }
    }

    public abstract ErrorCodeDao getErrorCodeDao();

    public abstract CountryDao getCountryDao();

    public abstract NationalFederationDao getNationalFederationDao();
}
