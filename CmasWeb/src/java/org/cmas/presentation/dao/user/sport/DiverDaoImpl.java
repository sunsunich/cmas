package org.cmas.presentation.dao.user.sport;

import org.cmas.Globals;
import org.cmas.entities.Country;
import org.cmas.entities.Role;
import org.cmas.entities.cards.PersonalCardType;
import org.cmas.entities.diver.Diver;
import org.cmas.entities.diver.DiverRegistrationStatus;
import org.cmas.entities.diver.DiverType;
import org.cmas.entities.sport.NationalFederation;
import org.cmas.presentation.dao.user.UserDaoImpl;
import org.cmas.presentation.model.registration.DiverVerificationFormObject;
import org.cmas.presentation.model.social.FindDiverFormObject;
import org.cmas.presentation.model.user.AddingToFederationFormObject;
import org.cmas.presentation.model.user.UserSearchFormObject;
import org.cmas.util.StringUtil;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Conjunction;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Restrictions;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created on Feb 05, 2016
 *
 * @author Alexander Petukhov
 */
@SuppressWarnings("unchecked")
public class DiverDaoImpl extends UserDaoImpl<Diver> implements DiverDao {

    @Override
    public List<Diver> searchDiversToAddToFederation(AddingToFederationFormObject formObject) {
        return searchWithPaginator(formObject, getAddToFederationCriteria(formObject));
    }

    @Override
    public int getMaxCountDiversToAddToFederation(AddingToFederationFormObject formObject) {
        return count(getAddToFederationCriteria(formObject));
    }

    @NotNull
    private Criteria getAddToFederationCriteria(AddingToFederationFormObject formObject) {
        Criteria criteria = createCriteria().add(Restrictions.eq("enabled", true));
        criteria.add(Restrictions.eq("firstName", StringUtil.correctSpaceCharAndTrim(formObject.getFirstName())));
        criteria.add(Restrictions.eq("lastName", StringUtil.correctSpaceCharAndTrim(formObject.getLastName())));
        try {
            criteria.add(Restrictions.eq("dob", Globals.getDTF().parse(formObject.getDob())));
        } catch (ParseException ignored) {
        }
        criteria.add(Restrictions.eq("role", Role.ROLE_DIVER));
        criteria.add(Restrictions.isNull("federation"))
                .add(Restrictions.in("diverRegistrationStatus",
                                     new DiverRegistrationStatus[]{
                                             DiverRegistrationStatus.INACTIVE,
                                             DiverRegistrationStatus.DEMO,
                                             DiverRegistrationStatus.GUEST}
                     )
                )
                .add(Restrictions.eq("bot", false));
        return criteria;
    }

    @Override
    public List<Diver> searchDivers(List<NationalFederation> federations, String firstName, String lastName, Date dob,
                                    DiverRegistrationStatus registrationStatus) {
        if (federations.isEmpty()) {
            return new ArrayList<>();
        }
        Criteria criteria = createCriteria()
                .add(Restrictions.eq("enabled", true))
                .createAlias("federation", "fed")
                .createAlias("fed.country", "country")
                // todo fix dob for iranian federation
                .add(Restrictions.disjunction()
                                 .add(Restrictions.eq("country.code", Country.IRAN_COUNTRY_CODE))
                                 .add(Restrictions.eq("dob", dob))
                )
                .add(Restrictions.eq("firstName", firstName))
                .add(Restrictions.eq("lastName", lastName))
                .add(Restrictions.in("federation", federations));
        if (registrationStatus != null) {
            criteria.add(Restrictions.eq("previousRegistrationStatus", registrationStatus));
        }
        return criteria.list();
    }

    @Override
    public List<Diver> getDiversByCardNumber(String cardNumber, DiverRegistrationStatus registrationStatus) {
        String hql = "select d from org.cmas.entities.diver.Diver d"
                     + " inner join d.cards c where d.enabled = :enabled and c.number = :number";
        if (registrationStatus != null) {
            hql += " and d.previousRegistrationStatus = :status";
        }
        Query query = createQuery(hql);
        if (registrationStatus != null) {
            query.setParameter("status", registrationStatus);
        }
        return query.setBoolean("enabled", true).setString("number", cardNumber).list();
    }

    @Override
    public Diver getDiverByCardNumber(NationalFederation federation, String cardNumber) {
        String hql = "select d from org.cmas.entities.diver.Diver d"
                     + " inner join d.cards c"
                     + " where d.enabled = :enabled and c.number = :number and d.federation = :federation";
        return (Diver) createQuery(hql).setBoolean("enabled", true)
                                       .setString("number", cardNumber)
                                       .setEntity("federation", federation)
                                       .uniqueResult();
    }

    @Override
    public Diver getDiverByToken(String token) {
        return (Diver) createCriteria().add(Restrictions.eq("mobileAuthToken", token)).uniqueResult();
    }

    @Override
    public Diver getByPrimaryCardNumber(String cardNumber) {
        String hql = "select d from org.cmas.entities.diver.Diver d"
                     + " inner join d.cards c"
                     + " where d.enabled = :enabled and c.number = :number and c.cardType = :cardType";
        return (Diver) createQuery(hql).setBoolean("enabled", true)
                                       .setString("number", cardNumber)
                                       .setParameter("cardType", PersonalCardType.PRIMARY)
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
        String federationId = form.getFederationId();
        if (!StringUtil.isTrimmedEmpty(federationId)) {
            criteria.add(Restrictions.eq("federation.id",
                                         Long.parseLong(StringUtil.correctSpaceCharAndTrim(federationId))));
        }
        return criteria;
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
        Date dob = null;
        try {
            dob = Globals.getDTF().parse(formObject.getDob());
        } catch (ParseException ignored) {
        }
        return (List<Diver>) createCriteria()
                .add(Restrictions.eq("enabled", true))
                .add(Restrictions.eq("dob", dob))
                .add(disjunction)
                .createAlias("federation", "fed")
                .createAlias("fed.country", "country")
                .add(Restrictions.eq("country.code", StringUtil.correctSpaceCharAndTrim(formObject.getCountry())))
                .add(Restrictions.in("diverRegistrationStatus",
                                     new DiverRegistrationStatus[]{
                                             DiverRegistrationStatus.CMAS_FULL,
                                             DiverRegistrationStatus.CMAS_BASIC,
                                             }
                     )
                )
                //remove bots
                .add(Restrictions.eq("bot", false))
                .list();
    }

    @NotNull
    private static StringBuilder getNameClause(Collection<String> filteredNames) {
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
        Set<Long> friendsIds = getFriendsIds(diverId);
        String hql = "select distinct d from org.cmas.entities.diver.Diver d" +
                     " inner join d.federation f inner join f.country c" +
                     " where (" + getNameClause(filteredNames) + ')' +
                     " and d.enabled = :enabled and d.diverType = :diverType and c.code = :country" +
                     " and d.id != :diverId";
        if (friendsIds != null && !friendsIds.isEmpty()) {
            hql += " and d.id not in (:friendIds)";
        }
        Query query = createQuery(hql);
        for (int i = 0; i < filteredNames.size(); i++) {
            String name = filteredNames.get(i);
            query.setString("template" + i, name + '%');
        }
        if (friendsIds != null && !friendsIds.isEmpty()) {
            query.setParameterList("friendIds", friendsIds);
        }
        return query.setBoolean("enabled", true)
                    .setString("country", StringUtil.correctSpaceCharAndTrim(formObject.getCountry()))
                    .setParameter("diverType", DiverType.valueOf(formObject.getDiverType()))
                    .setLong("diverId", diverId).setMaxResults(Globals.ADVANCED_SEARCH_MAX_RESULT).list();
    }

    private enum SearchMode {
        ALL, IN_FRIENDS, NEW_FRIENDS
    }

    @Override
    public List<Diver> searchDiversFast(long diverId, String input, DiverType diverType) {
        return searchFastNotSelf(diverId, input, SearchMode.ALL, diverType);
    }

    @Override
    public List<Diver> searchFriendsFast(long diverId, String input) {
        return searchFastNotSelf(diverId, input, SearchMode.NEW_FRIENDS, null);
    }

    @Override
    public List<Diver> searchInFriendsFast(long diverId, String input) {
        return searchFastNotSelf(diverId, input, SearchMode.IN_FRIENDS, null);
    }

    private List<Diver> searchFastNotSelf(long diverId, String input, SearchMode searchMode, DiverType diverType) {
        List<String> filteredNames = filterNames(input);
        if (filteredNames.isEmpty()) {
            return new ArrayList<>();
        }
        Set<Long> friendsIds = searchMode == SearchMode.ALL ? null : getFriendsIds(diverId);
        if (searchMode == SearchMode.IN_FRIENDS && (friendsIds == null || friendsIds.isEmpty())) {
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
                     " inner join d.cards c";
        hql += " where d.enabled = :enabled and ((" + getNameClause(filteredNames) + ") or (" + numberClause + "))";

        switch (searchMode) {
            case ALL:
                break;
            case IN_FRIENDS:
                hql += " and d.id in (:friendIds)";
                break;
            case NEW_FRIENDS:
                if (friendsIds != null && !friendsIds.isEmpty()) {
                    hql += " and d.id not in (:friendIds)";
                }
                break;
        }
        hql += " and d.id != :diverId";
        if (diverType != null) {
            hql += " and d.diverType = :diverType";
        }
        Query query = createQuery(hql);
        for (int i = 0; i < filteredNames.size(); i++) {
            String name = filteredNames.get(i);
            query.setString("template" + i, name + '%');
        }
        if (diverType != null) {
            query.setParameter("diverType", diverType);
        }
        if (friendsIds != null && !friendsIds.isEmpty()) {
            query.setParameterList("friendIds", friendsIds);
        }
        return query.setBoolean("enabled", true)
                    .setLong("diverId", diverId)
                    .setMaxResults(Globals.FAST_SEARCH_MAX_RESULT)
                    .list();
    }

    @Override
    public List<Diver> searchDivers(FindDiverFormObject formObject) {
        String cmasCardNumber = formObject.getCmasCardNumber();
        if (!StringUtil.isTrimmedEmpty(cmasCardNumber)) {
            return createCriteria()
                    .createAlias("primaryPersonalCard", "card")
                    .add(Restrictions.eq("enabled", true))
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
                    .add(Restrictions.eq("enabled", true))
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
                .add(Restrictions.eq("enabled", true))
                .add(Restrictions.eq("country.code", StringUtil.correctSpaceCharAndTrim(formObject.getCountry())))
                .add(Restrictions.eq("diverType", DiverType.valueOf(formObject.getDiverType())))
                .add(disjunction)
                .setMaxResults(Globals.ADVANCED_SEARCH_MAX_RESULT)
                .list();
    }

    private Set<Long> getFriendsIds(long diverId) {
        String sql = "select diverId, friendId from diver_friends where friendId = :diverId or diverId = :diverId";
        List<Object[]> diverIdPairs = createSQLQuery(sql).setLong("diverId", diverId).list();
        if (diverIdPairs.isEmpty()) {
            return Collections.emptySet();
        }
        Set<Long> diverIds = new HashSet<>(diverIdPairs.size());
        for (Object[] pair : diverIdPairs) {
            long pairElem1 = ((Number) pair[0]).longValue();
            long pairElem2 = ((Number) pair[1]).longValue();
            if (pairElem1 == diverId) {
                diverIds.add(pairElem2);
            } else {
                diverIds.add(pairElem1);
            }
        }
        return diverIds;
    }

    @Override
    public List<Diver> getFriends(Diver diver) {
        Set<Long> diverIds = getFriendsIds(diver.getId());
        if (diverIds.isEmpty()) {
            return new ArrayList<>();
        }
        return createCriteria().add(Restrictions.eq("enabled", true))
                               .add(Restrictions.in("id", diverIds)).list();
    }

    @Override
    public List<Diver> getDiversByIds(List<Long> diverIds) {
        return createCriteria().add(Restrictions.eq("enabled", true))
                               .add(Restrictions.in("id", diverIds)).list();
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

    @Transactional
    @Override
    public void updateDiverRegistrationStatusOnPaymentDueDate() {
        String hql = "update org.cmas.entities.diver.Diver d "
                     + "set d.previousRegistrationStatus = d.diverRegistrationStatus, d.diverRegistrationStatus = :newStatus "
                     + "where d.diverRegistrationStatus in (:statusList) and d.dateLicencePaymentIsDue <= :date";
//        createQuery(hql).setParameterList("statusList", Collections.singletonList(DiverRegistrationStatus.CMAS_FULL))
//                        .setParameter("newStatus", DiverRegistrationStatus.CMAS_BASIC)
//                        .setDate("date", new Date()).executeUpdate();
        createQuery(hql).setParameterList("statusList",
                                          Collections.singletonList(DiverRegistrationStatus.DEMO))
//        Arrays.asList(DiverRegistrationStatus.GUEST, DiverRegistrationStatus.DEMO))
                        .setParameter("newStatus", DiverRegistrationStatus.INACTIVE)
                        .setDate("date", new Date()).executeUpdate();
    }

    @Nullable
    @Override
    public Diver getByFirstNameLastNameCountry(@NotNull String firstName, @NotNull String lastName, @NotNull String countryCode) {
        String hql = "select d from org.cmas.entities.diver.Diver d"
                     + " inner join d.country c"
                     + " where d.enabled = :enabled and c.code = :code and concat(d.firstName, ' ', d.lastName) =:fullName";
        List<Diver> divers = createQuery(hql).setBoolean("enabled", true)
                                             .setString("code", StringUtil.correctSpaceCharAndTrim(countryCode))
                                             .setString("fullName", firstName + ' ' + lastName)
                                             .setCacheable(true)
                                             .list();
        return divers.isEmpty() ? null : divers.get(0);
    }
}
