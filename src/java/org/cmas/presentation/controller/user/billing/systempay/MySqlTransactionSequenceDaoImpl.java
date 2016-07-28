package org.cmas.presentation.controller.user.billing.systempay;

import org.cmas.util.dao.JdbcDaoImpl;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created on Jun 23, 2016
 *
 * @author Alexander Petukhov
 */
public class MySqlTransactionSequenceDaoImpl extends JdbcDaoImpl implements TransactionSequenceDao {

    private static final int SYSTEMPAY_TRANSACTION_SEQUENCE_ID = 1;
    public static final int MAX_TRANSACTION_NUMBER = 900000;

    @Transactional
    @Override
    public int getCurrentTransactionNumber() {
        jdbc.getJdbcOperations().update(
                "update transaction_settings set transactionNumber=last_insert_id(transactionNumber+1) where id=?",
                new Object[] { SYSTEMPAY_TRANSACTION_SEQUENCE_ID }
        );
        return jdbc.getJdbcOperations().queryForInt( "select last_insert_id()" ) % MAX_TRANSACTION_NUMBER;
    }
}
