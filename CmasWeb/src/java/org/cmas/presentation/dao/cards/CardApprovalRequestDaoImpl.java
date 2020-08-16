package org.cmas.presentation.dao.cards;

import org.cmas.entities.cards.CardApprovalRequest;
import org.cmas.entities.cards.CardApprovalRequestStatus;
import org.cmas.entities.diver.Diver;
import org.cmas.entities.sport.NationalFederation;
import org.cmas.presentation.model.SortPaginator;
import org.cmas.presentation.model.cards.CardApprovalRequestSearchFormObject;
import org.cmas.util.StringUtil;
import org.cmas.util.dao.HibernateDaoImpl;
import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import java.util.List;

/**
 * Created on Oct 19, 2019
 *
 * @author Alexander Petukhov
 */
public class CardApprovalRequestDaoImpl extends HibernateDaoImpl<CardApprovalRequest> implements CardApprovalRequestDao {

    @Override
    public List<CardApprovalRequest> getPendingByDiver(Diver diver) {
        //noinspection unchecked
        return createCriteria().add(Restrictions.eq("diver", diver))
                               .add(Restrictions.eq("status", CardApprovalRequestStatus.NEW))
                               .add(Restrictions.eq("deleted", false))
                               .list();
    }

    @Override
    public List<CardApprovalRequest> searchRequests(CardApprovalRequestSearchFormObject formObject, NationalFederation federation) {
        Criteria crit = makeSearchRequest(formObject, federation);
        return searchWithPaginator(formObject, crit);
    }

    protected List<CardApprovalRequest> searchWithPaginator(SortPaginator form, Criteria crit) {
        Order order = getOrder(form.getSortColumnName(), form.isDir());
        //noinspection unchecked
        return crit.addOrder(order).setFirstResult(form.getOffset())
                   .setMaxResults(form.getLimit()).setCacheable(true).list();
    }

    @Override
    public int getMaxCountSearchRequests(CardApprovalRequestSearchFormObject formObject, NationalFederation federation) {
        Criteria crit = makeSearchRequest(formObject, federation);
        return count(crit);
    }

    protected int count(Criteria crit) {
        crit.setProjection(Projections.rowCount()).setCacheable(true);
        Object result = crit.uniqueResult();
        return result == null ? 0 : ((Number) result).intValue();
    }

    protected Criteria makeSearchRequest(CardApprovalRequestSearchFormObject form, NationalFederation federation) {
        Criteria crit = createCriteria()
                .createAlias("diver", "diver")
                .add(Restrictions.eq("issuingFederation", federation));
        String email = form.getEmail();
        if (!StringUtil.isTrimmedEmpty(email)) {
            crit.add(Restrictions.like("diver.email", StringUtil.lowerCaseEmail(email),
                                       MatchMode.START));
        }
        String firstName = form.getFirstName();
        if (!StringUtil.isTrimmedEmpty(firstName)) {
            crit.add(Restrictions.like("diver.firstName", StringUtil.correctSpaceCharAndTrim(firstName),
                                       MatchMode.START));
        }
        String lastName = form.getLastName();
        if (!StringUtil.isTrimmedEmpty(lastName)) {
            crit.add(Restrictions.like("diver.lastName", StringUtil.correctSpaceCharAndTrim(lastName), MatchMode.START));
        }
        String status = form.getStatus();
        if (!StringUtil.isTrimmedEmpty(status)) {
            crit.add(Restrictions.eq("status",
                                     CardApprovalRequestStatus.valueOf(StringUtil.correctSpaceCharAndTrim(status)))
            );
        }
        return crit;
    }

    private Order getOrder(String name, boolean dir) {
        if (dir) {
            return Order.desc(name);
        }
        return Order.asc(name);
    }
}
