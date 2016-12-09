package org.cmas.util.dao;

import org.hibernate.FlushMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.orm.hibernate3.SessionHolder;
import org.springframework.transaction.support.TransactionSynchronizationManager;

public abstract class RunInHibernate implements Runnable {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private final SessionFactory sessionFactory;

    protected RunInHibernate(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void run() {
      //  log.error("RunInHibernate started");
        try {
            Session session = SessionFactoryUtils.getSession(sessionFactory, true);
        //    log.error("RunInHibernate got session");
            session.setFlushMode(FlushMode.COMMIT);
            TransactionSynchronizationManager.bindResource(sessionFactory, new SessionHolder(session));
          //  log.error("RunInHibernate TransactionSynchronizationManager binded");
            runTaskInHibernate();

        } catch (Exception e) {
            log.error("error while performing task in Hibernate", e);
        }
        finally {
            SessionHolder sessionHolder = (SessionHolder)
                    TransactionSynchronizationManager.unbindResource(sessionFactory);
            SessionFactoryUtils.closeSession(sessionHolder.getSession());
        }
    }

    protected abstract void runTaskInHibernate();
}
