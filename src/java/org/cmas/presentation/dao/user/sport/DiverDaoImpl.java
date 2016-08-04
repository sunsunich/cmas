package org.cmas.presentation.dao.user.sport;

import org.cmas.entities.diver.Diver;
import org.cmas.entities.diver.DiverType;
import org.cmas.entities.sport.NationalFederation;
import org.cmas.presentation.dao.user.UserDaoImpl;
import org.cmas.presentation.model.registration.DiverVerificationFormObject;
import org.cmas.presentation.model.social.FindDiverFormObject;
import org.cmas.presentation.model.user.UserSearchFormObject;
import org.cmas.util.StringUtil;
import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

/**
 * Created on Feb 05, 2016
 *
 * @author Alexander Petukhov
 */
@SuppressWarnings("unchecked")
public class DiverDaoImpl extends UserDaoImpl<Diver> implements DiverDao {

    @Override
    public Diver searchDiver(NationalFederation federation, String firstName, String lastName, Date dob) {
        return (Diver) createCriteria()
                .add(Restrictions.eq("dob", dob))
                .add(Restrictions.eq("firstName", firstName))
                .add(Restrictions.eq("lastName", lastName))
                .add(Restrictions.eq("federation", federation))
                .uniqueResult();
    }

    @Override
    protected Criteria makeSearchRequest(UserSearchFormObject form) {
        Criteria criteria = super.makeSearchRequest(form);
        String diverType = form.getDiverType();
        if (!StringUtil.isTrimmedEmpty(diverType)) {
            criteria.add(Restrictions.eq("diverType", DiverType.valueOf(diverType)));
        }
        String country = form.getCountryCode();
        if (!StringUtil.isTrimmedEmpty(country)) {
            criteria.createAlias("federation", "fed")
                    .createAlias("fed.country", "country")
                    .add(Restrictions.eq("country.code", StringUtil.correctSpaceCharAndTrim(country)));
        }
        return criteria;
    }

    @Override
    public int getFullyRegisteredDiverCnt() {
        Object result = createCriteria().add(Restrictions.isNotNull("primaryPersonalCard"))
                                        .setProjection(Projections.rowCount())
                                        .uniqueResult();
        return result == null ? 0 : ((Number) result).intValue();
    }

    @Override
    public Diver getDiverByCardNumber(String cardNumber) {
        String hql = "select d from org.cmas.entities.diver.Diver d"
                     + " inner join d.cards c where c.number = :number";
        return (Diver) createQuery(hql).setString("number", cardNumber).uniqueResult();
    }

    private Criteria createDiverSearchCriteria(DiverVerificationFormObject formObject) {
        return createCriteria()
                .add(Restrictions.eq("lastName", formObject.getLastName()))
                .createAlias("federation", "fed")
                .createAlias("fed.country", "country")
                .add(Restrictions.eq("country.code", StringUtil.correctSpaceCharAndTrim(formObject.getCountry())));
    }

    @Override
    public List<Diver> searchForVerification(DiverVerificationFormObject formObject) {
        return (List<Diver>) createDiverSearchCriteria(formObject)
                .list();
    }

    @Override
    public List<Diver> searchNotFriendDivers(long diverId, FindDiverFormObject formObject) {
        String nameTemplate = StringUtil.correctSpaceCharAndTrim(formObject.getName()) + '%';
        String hql = "select d from org.cmas.entities.diver.Diver d" +
                     " inner join d.federation f inner join f.country c" +
                     " left outer join d.friendOf fr" +
                     " where (d.lastName like :lastName or d.firstName like :firstName)" +
                     " and d.diverType = :diverType and c.code = :country" +
                     " and (fr.id != :diverId or fr.id is null)" +
                     " and d.id != :diverId";

        return createQuery(hql).setString("lastName", nameTemplate)
                               .setString("firstName", nameTemplate)
                               .setString("country", StringUtil.correctSpaceCharAndTrim(formObject.getCountry()))
                               .setParameter("diverType", DiverType.valueOf(formObject.getDiverType()))
                               .setLong("diverId", diverId).list();
    }

    @Override
    public List<Diver> searchDivers(FindDiverFormObject formObject) {
        String cmasCardNumber = formObject.getCmasCardNumber();
        if (!StringUtil.isTrimmedEmpty(cmasCardNumber)) {
            return createCriteria()
                    .createAlias("primaryPersonalCard", "card")
                    .add(Restrictions.eq("primaryPersonalCard.number",
                                         StringUtil.correctSpaceCharAndTrim(cmasCardNumber)))
                    .list();
        }
        String federationCardNumber = formObject.getFederationCardNumber();
        String federationCountry = formObject.getFederationCountry();
        if (!StringUtil.isTrimmedEmpty(federationCardNumber)
            && !StringUtil.isTrimmedEmpty(federationCountry)
                ) {
            return createCriteria()
                    .createAlias("cards", "card")
                    .createAlias("federation", "federation")
                    .createAlias("federation.country", "country")
                    .add(Restrictions.eq("card.number",
                                         StringUtil.correctSpaceCharAndTrim(federationCardNumber)))
                    .add(Restrictions.eq("country.code",
                                         StringUtil.correctSpaceCharAndTrim(federationCountry)))
                    .list();
        }
        String nameTemplate = StringUtil.correctSpaceCharAndTrim(formObject.getName()) + '%';
        return createCriteria()
                .createAlias("country", "country")
                .add(Restrictions.eq("country.code", StringUtil.correctSpaceCharAndTrim(formObject.getCountry())))
                .add(Restrictions.eq("dob", formObject.getDob()))
                .add(Restrictions.eq("diverType", DiverType.valueOf(formObject.getDiverType())))
                .add(Restrictions.disjunction()
                                 .add(Restrictions.like("firstName", nameTemplate))
                                 .add(Restrictions.like("lastName", nameTemplate))
                )
                .list();
    }

    @Override
    public List<Diver> getFriends(Diver diver) {
        String sql = "select diverId, friendId from diver_friends where friendId = :diverId or diverId = :diverId";
        long diverId = diver.getId();
        List<Object[]> diverIdPairs = createSQLQuery(sql).setLong("diverId", diverId).list();
        if (diverIdPairs.isEmpty()) {
            return new ArrayList<>();
        }
        Collection<Long> diverIds = new HashSet<>(diverIdPairs.size());
        for (Object[] pair : diverIdPairs) {
            long pairElem1 = ((Number) pair[0]).longValue();
            long pairElem2 = ((Number) pair[1]).longValue();
            if (pairElem1 == diverId) {
                diverIds.add(pairElem2);
            } else {
                diverIds.add(pairElem1);
            }
        }
        return createCriteria().add(Restrictions.in("id", diverIds)).list();
    }

    @Override
    public List<Diver> getDiversByIds(List<Long> diverIds) {
        return createCriteria().add(Restrictions.in("id", diverIds)).list();
    }

    @Override
    public boolean isFriend(Diver diver, Diver friend) {
        String sql = "select count(*) from diver_friends where"
                     + " friendId = :friendId and diverId = :diverId"
                     + " or friendId = :diverId and diverId = :friendId";
        Object result = createSQLQuery(sql)
                .setLong("diverId", diver.getId())
                .setLong("friendId", friend.getId())
                .uniqueResult();
        return result != null && ((Number) result).intValue() > 0;
    }

    @Override
    public void removeFriend(Diver diver, Diver friend) {
        String sql = "delete from diver_friends where"
                     + " friendId = :friendId and diverId = :diverId"
                     + " or friendId = :diverId and diverId = :friendId";
        createSQLQuery(sql)
                .setLong("diverId", diver.getId())
                .setLong("friendId", friend.getId())
                .executeUpdate();
    }
}
