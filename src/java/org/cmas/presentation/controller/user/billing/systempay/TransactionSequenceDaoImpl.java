package org.cmas.presentation.controller.user.billing.systempay;

import org.cmas.util.dao.JdbcDaoImpl;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementCreatorFactory;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Collections;

/**
 * Created on Jun 23, 2016
 *
 * @author Alexander Petukhov
 */
public class TransactionSequenceDaoImpl extends JdbcDaoImpl implements TransactionSequenceDao {

    private static final int SYSTEMPAY_TRANSACTION_SEQUENCE_ID = 1;
    public static final int MAX_TRANSACTION_NUMBER = 900000;

    @Transactional
    @Override
    public int getCurrentTransactionNumber() {
        PreparedStatementCreatorFactory queryForUpdateFactory = new PreparedStatementCreatorFactory(
                "SELECT transactionNumber FROM transaction_settings WHERE id = ? FOR UPDATE",
                Collections.singletonList(new SqlParameter(Types.INTEGER))
        );

        // These lines are important!
        queryForUpdateFactory.setUpdatableResults(true);
        queryForUpdateFactory.setResultSetType(ResultSet.TYPE_SCROLL_SENSITIVE);
        PreparedStatementCreator psc = queryForUpdateFactory.newPreparedStatementCreator(
                        new Object[] { SYSTEMPAY_TRANSACTION_SEQUENCE_ID });

        final int[] result = new int[1];
        RowCallbackHandler rch = new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet resultSet) throws SQLException {
                int transactionNumber = resultSet.getInt("transactionNumber");
                transactionNumber = (transactionNumber + 1) % MAX_TRANSACTION_NUMBER;
                result[0] = transactionNumber;
                // Update and release the lock
                resultSet.updateInt("transactionNumber", transactionNumber);
            }
        };

        jdbc.getJdbcOperations().query(psc, rch);

//        String sql = "SELECT transactionNumber FROM transaction_settings FOR UPDATE;"
//                     + " UPDATE transaction_settings SET transactionNumber = transactionNumber + 1;";
        return result[0];
    }
}
