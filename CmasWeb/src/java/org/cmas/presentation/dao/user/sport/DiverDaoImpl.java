package org.cmas.presentation.dao.user.sport;

import org.cmas.Globals;
import org.cmas.entities.diver.Diver;
import org.cmas.entities.diver.DiverType;
import org.cmas.entities.sport.NationalFederation;
import org.cmas.presentation.dao.user.UserDaoImpl;
import org.cmas.presentation.model.registration.DiverVerificationFormObject;
import org.cmas.presentation.model.social.FindDiverFormObject;
import org.cmas.presentation.model.user.UserSearchFormObject;
import org.cmas.util.StringUtil;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Conjunction;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.jetbrains.annotations.NotNull;

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

    @NotNull
    private static List<String> filterNames(String nameValue) {
        String[] names = StringUtil.correctSpaceCharAndTrim(nameValue).split(" ");
        List<String> filteredNames = new ArrayList<>(names.length);
        for (String name : names) {
            if (!name.isEmpty()) {
                filteredNames.add(name);
            }
        }
        return filteredNames;
    }

    @Override
    public List<Diver> searchForVerification(DiverVerificationFormObject formObject) {
        List<String> filteredNames = filterNames(formObject.getName());
        if (filteredNames.isEmpty()) {
            return new ArrayList<>();
        }
        Disjunction disjunction = Restrictions.disjunction();
        if (filteredNames.size() == 2) {
            Conjunction conjunction1 = Restrictions.conjunction();
            conjunction1.add(Restrictions.eq("firstName", filteredNames.get(0)));
            conjunction1.add(Restrictions.eq("lastName", filteredNames.get(1)));
            disjunction.add(conjunction1);
            Conjunction conjunction2 = Restrictions.conjunction();
            conjunction2.add(Restrictions.eq("lastName", filteredNames.get(0)));
            conjunction2.add(Restrictions.eq("firstName", filteredNames.get(1)));
            disjunction.add(conjunction2);
        } else {
            for (String name : filteredNames) {
                if (!name.isEmpty()) {
                    disjunction.add(Restrictions.eq("firstName", name));
                    disjunction.add(Restrictions.eq("lastName", name));
                }
            }
        }
        return (List<Diver>) createCriteria()
                .add(disjunction)
                .createAlias("federation", "fed")
                .createAlias("fed.country", "country")
                .add(Restrictions.eq("country.code", StringUtil.correctSpaceCharAndTrim(formObject.getCountry())))
                .list();
    }

    @NotNull
    private static StringBuilder getNameClause(List<String> filteredNames) {
        StringBuilder nameClause = new StringBuilder();
        if (filteredNames.size() == 2) {
            nameClause.append("d.firstName like :template0 and d.lastName like :template1 or ")
                      .append("d.lastName like :template0 and d.firstName like :template1");
        } else {
            for (int i = 0; i < filteredNames.size(); i++) {
                if (nameClause.length() > 0) {
                    nameClause.append(" or ");
                }
                nameClause.append("d.lastName like :template")
                          .append(i)
                          .append(" or d.firstName like :template")
                          .append(i);

            }
        }
        return nameClause;
    }

    @Override
    public List<Diver> searchNotFriendDivers(long diverId, FindDiverFormObject formObject) {
        List<String> filteredNames = filterNames(formObject.getName());
        if (filteredNames.isEmpty()) {
            return new ArrayList<>();
        }
        String hql = "select distinct d from org.cmas.entities.diver.Diver d" +
                     " inner join d.federation f inner join f.country c" +
                     " left outer join d.friendOf fr" +
                     " where (" + getNameClause(filteredNames) + ')' +
                     " and d.diverType = :diverType and c.code = :country" +
                     " and (fr.id != :diverId or fr.id is null)" +
                     " and d.id != :diverId";

        Query query = createQuery(hql);
        for (int i = 0; i < filteredNames.size(); i++) {
            String name = filteredNames.get(i);
            query.setString("template" + i, name + '%');
        }
        return query.setString("country", StringUtil.correctSpaceCharAndTrim(formObject.getCountry()))
                    .setParameter("diverType", DiverType.valueOf(formObject.getDiverType()))
                    .setLong("diverId", diverId).setMaxResults(Globals.ADVANCED_SEARCH_MAX_RESULT).list();
    }

    @Override
    public List<Diver> searchFriendsFast(long diverId, String input) {
        List<String> filteredNames = filterNames(input);
        if (filteredNames.isEmpty()) {
            return new ArrayList<>();
        }
        StringBuilder numberClause = new StringBuilder();
        for (int i = 0; i < filteredNames.size(); i++) {
            if (numberClause.length() > 0) {
                numberClause.append(" or ");
            }
            numberClause.append("c.number like :template").append(i);
        }
        String hql = "select distinct d from org.cmas.entities.diver.Diver d" +
                     " inner join d.cards c" +
                     " left outer join d.friendOf fr" +
                     " where ((" + getNameClause(filteredNames) + ") or (" + numberClause + "))" +
                     " and (fr.id != :diverId or fr.id is null)" +
                     " and d.id != :diverId";

        Query query = createQuery(hql);
        for (int i = 0; i < filteredNames.size(); i++) {
            String name = filteredNames.get(i);
            query.setString("template" + i, name + '%');
        }
        return query.setLong("diverId", diverId).setMaxResults(Globals.FAST_SEARCH_MAX_RESULT).list();
    }

    @Override
    public List<Diver> searchDivers(FindDiverFormObject formObject) {
        String cmasCardNumber = formObject.getCmasCardNumber();
        if (!StringUtil.isTrimmedEmpty(cmasCardNumber)) {
            return createCriteria()
                    .createAlias("primaryPersonalCard", "card")
                    .add(Restrictions.eq("card.number",
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
        List<String> filteredNames = filterNames(formObject.getName());
        if (filteredNames.isEmpty()) {
            return new ArrayList<>();
        }
        Disjunction disjunction = Restrictions.disjunction();
        if (filteredNames.size() == 2) {
            Conjunction conjunction1 = Restrictions.conjunction();
            conjunction1.add(Restrictions.like("firstName", filteredNames.get(0) + '%'));
            conjunction1.add(Restrictions.like("lastName", filteredNames.get(1) + '%'));
            disjunction.add(conjunction1);
            Conjunction conjunction2 = Restrictions.conjunction();
            conjunction2.add(Restrictions.like("lastName", filteredNames.get(0) + '%'));
            conjunction2.add(Restrictions.like("firstName", filteredNames.get(1) + '%'));
            disjunction.add(conjunction2);
        } else {
            for (String name : filteredNames) {
                if (!name.isEmpty()) {
                    disjunction.add(Restrictions.like("firstName", name + '%'));
                    disjunction.add(Restrictions.like("lastName", name + '%'));
                }
            }
        }
        return createCriteria()
                .createAlias("country", "country")
                .add(Restrictions.eq("country.code", StringUtil.correctSpaceCharAndTrim(formObject.getCountry())))
                .add(Restrictions.eq("diverType", DiverType.valueOf(formObject.getDiverType())))
                .add(disjunction)
                .setMaxResults(Globals.ADVANCED_SEARCH_MAX_RESULT)
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
