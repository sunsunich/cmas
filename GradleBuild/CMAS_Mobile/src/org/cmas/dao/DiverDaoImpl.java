package org.cmas.dao;

import android.content.ContentValues;
import android.database.Cursor;
import org.cmas.entities.PersonalCard;
import org.cmas.entities.diver.Diver;
import org.cmas.entities.diver.DiverLevel;
import org.cmas.entities.diver.DiverType;
import org.cmas.util.StringUtil;

public class DiverDaoImpl extends UserDaoImpl<Diver> implements DiverDao {

    public static final String DIVER_TABLE = "divers";

    private static final String COLUMN_TYPE = "type";
    private static final String COLUMN_LEVEL = "level";
    public static final String COLUMN_CARD_NUMBER = "card_number";
    public static final String COLUMN_HAS_PAYED = "has_payed";


    private static final String CREATE_TABLE_ENDING_QUERY =
            ", " + COLUMN_TYPE + " text, "
            + COLUMN_LEVEL + " text, "
            + COLUMN_CARD_NUMBER + " text,"
            + COLUMN_HAS_PAYED + " integer not null default 0"
            + ");";

    private static final String[] ADDITIONAL_COLUMNS = {
            COLUMN_TYPE,
            COLUMN_LEVEL,
            COLUMN_CARD_NUMBER,
            COLUMN_HAS_PAYED
    };


    @Override
    protected String getTableCreateQueryEnding() {
        return CREATE_TABLE_ENDING_QUERY;
    }

    @Override
    public String getTableName() {
        return DIVER_TABLE;
    }

    @Override
    protected Diver constructEntity() {
        return new Diver();
    }

    @Override
    public String[] getAllColumns() {
        return StringUtil.concatArrays(super.getAllColumns(), ADDITIONAL_COLUMNS);
    }

    @Override
    public Diver cursorToEntity(Cursor cursor, int index) {
        Diver entity = super.cursorToEntity(cursor, index);
        int i = index + super.getAllColumns().length;
        entity.setDiverType(DiverType.valueOf(cursor.getString(i)));
        i++;
        entity.setDiverLevel(DiverLevel.valueOf(cursor.getString(i)));
        i++;

        PersonalCard primaryPersonalCard = new PersonalCard();
        primaryPersonalCard.setNumber(cursor.getString(i));
        entity.setPrimaryPersonalCard(
                primaryPersonalCard
        );
        i++;
        entity.setHasPayed(cursor.getInt(i) != 0);
        return entity;
    }

    @Override
    protected ContentValues entityToContentValues(Diver entity) {
        ContentValues values = super.entityToContentValues(entity);
        values.put(COLUMN_TYPE, entity.getDiverType().name());
        values.put(COLUMN_LEVEL, entity.getDiverLevel().name());
        values.put(COLUMN_CARD_NUMBER, entity.getPrimaryPersonalCard().getNumber());
        values.put(COLUMN_HAS_PAYED, entity.isHasPayed());
        return values;
    }

}
