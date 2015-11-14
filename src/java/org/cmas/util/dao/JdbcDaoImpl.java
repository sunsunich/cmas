package org.cmas.util.dao;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.jdbc.core.simple.SimpleJdbcOperations;

public abstract class JdbcDaoImpl implements JdbcDao {

    protected SimpleJdbcOperations jdbc;

    @Required
    public void setJdbc(SimpleJdbcOperations jdbc) {
        this.jdbc = jdbc;
    }

}
