package org.cmas.dao;

import org.cmas.entities.diver.Diver;

public class DiverDaoImpl extends UserDaoImpl<Diver> implements UserDao<Diver> {

    static final String DIVER_TABLE = "divers";

    // Database creation SQL statement
    private static final String CREATE_TABLE_ENDING_QUERY = ");";

    @Override
    public String getTableName() {
        return DIVER_TABLE;
    }

    protected Diver constructEntity(){
        return new Diver();
    }

    @Override
    protected String getTableCreateQueryEnding() {
        return CREATE_TABLE_ENDING_QUERY;
    }
}
