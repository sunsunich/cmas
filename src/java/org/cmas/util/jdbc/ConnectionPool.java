package org.cmas.util.jdbc;

import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Required;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.*;
import java.sql.SQLFeatureNotSupportedException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.sql.SQLException;
import java.sql.Connection;
import java.io.PrintWriter;

@ManagedResource
@SuppressWarnings({"ClassWithTooManyMethods"})
public class ConnectionPool implements InitializingBean, DisposableBean, DataSource {
    private BlockingQueue<PooledConnection> pool;
    private ConnectionPoolDataSource ds;
    //private long validationIntervalMillis;
    private int poolCapacity;
    private final AtomicInteger taken = new AtomicInteger(0);

    private final AtomicInteger created = new AtomicInteger(0);
    private final AtomicInteger dropped = new AtomicInteger(0);
    private final AtomicInteger invalidated = new AtomicInteger(0);
    private final AtomicInteger returned = new AtomicInteger(0);
    private final AtomicInteger destroyed = new AtomicInteger(0);
    private final Logger log = LoggerFactory.getLogger(getClass());
    private final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(ConnectionPool.class.getName());
    private final PoolConnectionEventListener listener = new PoolConnectionEventListener();
    private static final int CONN_VALIDATION_TIMEOUT = 20;

    /*
    public void setValidationIntervalSeconds(int validationIntervalSeconds) {
        validationIntervalMillis = validationIntervalSeconds * 1000L;
    } */

    @Override
    public java.util.logging.Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return logger;
    }


    private class PoolConnectionEventListener implements ConnectionEventListener {
        @Override
        public void connectionClosed(ConnectionEvent event) {
            PooledConnection pconn = (PooledConnection) event.getSource();
            returnToPool(pconn);
        }

        @Override
        public void connectionErrorOccurred(ConnectionEvent event) {
            log.warn("Connection error detected", event.getSQLException());
            PooledConnection pconn = (PooledConnection) event.getSource();
            destroyConnection(pconn);
        }
    }

    public void setPoolCapacity(int poolCapacity) {
        this.poolCapacity = poolCapacity;
    }

    @Required
    public void setDs(ConnectionPoolDataSource ds) {
        this.ds = ds;
    }

    private PooledConnection createConnection() throws SQLException {
        PooledConnection result = ds.getPooledConnection();
        created.incrementAndGet();
        result.addConnectionEventListener(listener);
        return result;
    }

    private void closeConnection(PooledConnection connection) {
        connection.removeConnectionEventListener(listener);
        try {
            connection.close();
        } catch (SQLException e) {
            log.warn("Error closing connection", e);
        }
    }

    private boolean validateConnection(PooledConnection connection) {
        boolean result = false;
        try {
            result = connection.getConnection().isValid(CONN_VALIDATION_TIMEOUT);
        } catch (SQLException e) {
            log.error("Error validating connection", e);
        }
        if (!result) {
            // Проверка не прошла!
            log.debug("Pooled connection is not valid");
            // Закрываем умерший коннект
            closeConnection(connection);
            invalidated.incrementAndGet();
        }
        return result;
    }

    public PooledConnection takeFromPool() throws SQLException {
        log.debug("Taking connection from the pool");
        // Пытаемся взять из пула
        PooledConnection result = pool.poll();
        if (result == null) {
            // В пуле ничего нет
            log.debug("Empty pool, will create new connection");
            result = createConnection();
        }
        while (!validateConnection(result)) {
            // Открываем коннект заново
            result = createConnection();
        }
        taken.incrementAndGet();
        return result;
    }

    public void destroyConnection(PooledConnection cn) {
        log.debug("Destroying connection {}", cn);
        destroyed.incrementAndGet();
        closeConnection(cn);
    }

    public void returnToPool(PooledConnection cn) {
        returned.incrementAndGet();
        log.debug("Returning connection to the pool");
        // Пытаемся скормить коннект пулу
        boolean pooled = pool.offer(cn);
        if (!pooled) {
            // Пул не схавал, уничтожаем коннект
            log.info("Excess connection will be destroyed");
            closeConnection(cn);
            dropped.incrementAndGet();
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        pool = new LinkedBlockingQueue<PooledConnection>(poolCapacity);
    }

    @Override
    public void destroy() throws Exception {
        for (PooledConnection cn : pool) {
            closeConnection(cn);
        }
    }


    @ManagedAttribute(description = "Сколько коннектов взято из пула")
    public int getTakenCount() {
        return taken.get();
    }

    @ManagedAttribute(description = "Сколько коннектов вовращено в пул")
    public int getReturnedCount() {
        return returned.get();
    }

    @ManagedAttribute(description = "Сколько коннектов принудительно закрыто")
    public int getDestroyedCount() {
        return destroyed.get();
    }

    @ManagedAttribute(description = "Сколько избыточных коннектов закрыто")
    public int getDropped() {
        return dropped.get();
    }

    @ManagedAttribute(description = "Сколько коннектов не прошло проверку")
    public int getInvalidated() {
        return invalidated.get();
    }

    @ManagedAttribute(description = "Сколько коннектов создано")
    public int getCreated() {
        return created.get();
    }


    @ManagedAttribute(description = "Сколько коннектов сейчас в пуле")
    public int getInPool() {
        return pool.size();
    }

    @Override
    public Connection getConnection() throws SQLException {
        return takeFromPool().getConnection();
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        throw new UnsupportedOperationException(
            "Cannot specify username/password for connections");
    }

    @Override
    public PrintWriter getLogWriter() throws SQLException {
        return ds.getLogWriter();
    }

    @Override
    public void setLogWriter(PrintWriter out) throws SQLException {
        ds.setLogWriter(out);
    }

    @Override
    public void setLoginTimeout(int seconds) throws SQLException {
        ds.setLoginTimeout(seconds);
    }

    @Override
    public int getLoginTimeout() throws SQLException {
        return ds.getLoginTimeout();
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        try {
            return iface.cast(this);
        } catch (ClassCastException e) {
            throw new SQLException("Unable to unwrap " + getClass() + " to " + iface.toString(), e);
        }

    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return iface.isInstance(this);
    }

}
