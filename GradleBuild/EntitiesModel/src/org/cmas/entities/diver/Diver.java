package org.cmas.entities.diver;

import com.google.myjson.annotations.Expose;
import org.cmas.Globals;
import org.cmas.entities.CardUser;
import org.cmas.entities.Country;
import org.cmas.entities.PersonalCard;
import org.cmas.entities.logbook.LogbookEntry;
import org.cmas.entities.logbook.LogbookVisibility;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Created on Dec 04, 2015
 *
 * @author Alexander Petukhov
 */
@Entity
@Table(name = "divers")
public class Diver extends CardUser {

    private static final long serialVersionUID = -6873304958863096818L;

    @Expose
    @Column
    @Enumerated(EnumType.STRING)
    private DiverLevel diverLevel;

    @Expose
    @Column
    @Enumerated(EnumType.STRING)
    private DiverType diverType;

    @Expose
    @OneToMany(mappedBy = "diver", fetch = FetchType.LAZY)
    private List<PersonalCard> cards;

    @ManyToOne(fetch = FetchType.LAZY)
    private Diver instructor;

    //social

    @OneToMany(mappedBy = "diver", fetch = FetchType.LAZY)
    private List<LogbookEntry> logbookEntries;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "diver_friends",
            joinColumns = @JoinColumn(name = "diverId"),
            inverseJoinColumns = @JoinColumn(name = "friendId")
    )
    private Set<Diver> friends;

    @Column
    private long socialUpdatesVersion;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "diver_friends",
            joinColumns = @JoinColumn(name = "friendId"),
            inverseJoinColumns = @JoinColumn(name = "diverId")
    )
    private Set<Diver> friendOf;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "diver_new_from_countries",
            joinColumns = @JoinColumn(name = "diverId"),
            inverseJoinColumns = @JoinColumn(name = "countryId")
    )
    private Set<Country> newsFromCountries;

    @Column
    @Enumerated(EnumType.STRING)
    private LogbookVisibility defaultVisibility;

    @Expose
    @Column
    private Date dateLicencePaymentIsDue;

    @Expose
    @Column
    @Enumerated(EnumType.STRING)
    private DiverRegistrationStatus previousRegistrationStatus;

    @Expose
    @Column
    @Enumerated(EnumType.STRING)
    private DiverRegistrationStatus diverRegistrationStatus;

    @Expose
    @Column(length = Globals.HALF_MAX_LENGTH)
    private String areaOfInterest;

    @Expose
    @Column
    private boolean isAddFriendsToLogbookEntries;

    @Expose
    @Column
    private boolean isNewsFromCurrentLocation;
    @Expose
    @Column
    private Date dateGoldStatusPaymentIsDue;

    @Expose
    @Column(unique = true)
    private String extId;

    public Diver() {
        defaultVisibility = LogbookVisibility.PRIVATE;
    }

    public Diver(long id) {
        super(id);
        defaultVisibility = LogbookVisibility.PRIVATE;
    }

    public boolean isGold() {
        Date dateGoldStatusPaymentIsDue = getDateGoldStatusPaymentIsDue();
        return dateGoldStatusPaymentIsDue != null && dateGoldStatusPaymentIsDue.after(new Date());
    }

    public Date getDateGoldStatusPaymentIsDue() {
        return dateGoldStatusPaymentIsDue;
    }

    public void setDateGoldStatusPaymentIsDue(Date dateGoldStatusPaymentIsDue) {
        this.dateGoldStatusPaymentIsDue = dateGoldStatusPaymentIsDue;
    }

    public String getExtId() {
        return extId;
    }

    public void setExtId(String extId) {
        this.extId = extId;
    }

    public String getAreaOfInterest() {
        return areaOfInterest;
    }

    public void setAreaOfInterest(String areaOfInterest) {
        this.areaOfInterest = areaOfInterest;
    }

    public DiverRegistrationStatus getPreviousRegistrationStatus() {
        return previousRegistrationStatus;
    }

    public void setPreviousRegistrationStatus(DiverRegistrationStatus previousRegistrationStatus) {
        this.previousRegistrationStatus = previousRegistrationStatus;
    }

    public Date getDateLicencePaymentIsDue() {
        return dateLicencePaymentIsDue;
    }

    public void setDateLicencePaymentIsDue(Date dateLicencePayment) {
        this.dateLicencePaymentIsDue = dateLicencePayment;
    }

    public DiverRegistrationStatus getDiverRegistrationStatus() {
        return diverRegistrationStatus;
    }

    public void setDiverRegistrationStatus(DiverRegistrationStatus diverRegistrationStatus) {
        this.diverRegistrationStatus = diverRegistrationStatus;
    }

    public long getSocialUpdatesVersion() {
        return socialUpdatesVersion;
    }

    public void setSocialUpdatesVersion(long socialRefreshVersion) {
        this.socialUpdatesVersion = socialRefreshVersion;
    }

    public boolean isAddFriendsToLogbookEntries() {
        return isAddFriendsToLogbookEntries;
    }

    public void setIsAddFriendsToLogbookEntries(boolean isAddFriendsToLogbookEntries) {
        this.isAddFriendsToLogbookEntries = isAddFriendsToLogbookEntries;
    }

    public boolean isNewsFromCurrentLocation() {
        return isNewsFromCurrentLocation;
    }

    public void setIsNewsFromCurrentLocation(boolean isNewsFromCurrentLocation) {
        this.isNewsFromCurrentLocation = isNewsFromCurrentLocation;
    }

    public LogbookVisibility getDefaultVisibility() {
        return defaultVisibility;
    }

    public void setDefaultVisibility(LogbookVisibility defaultVisibility) {
        this.defaultVisibility = defaultVisibility;
    }

    public List<LogbookEntry> getLogbookEntries() {
        return logbookEntries;
    }

    public void setLogbookEntries(List<LogbookEntry> logbookEntries) {
        this.logbookEntries = logbookEntries;
    }

    public DiverLevel getDiverLevel() {
        return diverLevel;
    }

    public void setDiverLevel(DiverLevel diverLevel) {
        this.diverLevel = diverLevel;
    }

    public DiverType getDiverType() {
        return diverType;
    }

    public void setDiverType(DiverType diverType) {
        this.diverType = diverType;
    }

    public List<PersonalCard> getCards() {
        return cards;
    }

    public void setCards(List<PersonalCard> cards) {
        this.cards = cards;
    }

    public Diver getInstructor() {
        return instructor;
    }

    public void setInstructor(Diver instructor) {
        this.instructor = instructor;
    }

    public Set<Diver> getFriends() {
        return friends;
    }

    public void setFriends(Set<Diver> friends) {
        this.friends = friends;
    }

    public Set<Diver> getFriendOf() {
        return friendOf;
    }

    public void setFriendOf(Set<Diver> friendOf) {
        this.friendOf = friendOf;
    }

    public Set<Country> getNewsFromCountries() {
        return newsFromCountries;
    }

    public void setNewsFromCountries(Set<Country> newsFromCountries) {
        this.newsFromCountries = newsFromCountries;
    }

    @SuppressWarnings("MagicCharacter")
    @Override
    public String toString() {
        StringBuilder cardStr = new StringBuilder();
        cardStr.append('[');
        if (cards != null) {
            for (PersonalCard card : cards) {
                cardStr.append(card).append(';');
            }
        }
        cardStr.append(']');
        return "Diver{" +
               "instructor=" + instructor +
               ", diverLevel=" + diverLevel +
               ", diverType=" + diverType +
               ", cards=" + cardStr +
               "} " + super.toString();
    }
}
