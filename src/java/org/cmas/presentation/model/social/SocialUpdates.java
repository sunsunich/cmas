package org.cmas.presentation.model.social;

import com.google.myjson.annotations.Expose;
import org.cmas.entities.diver.Diver;
import org.cmas.entities.logbook.DiverFriendRequest;
import org.cmas.entities.logbook.LogbookBuddieRequest;

import java.util.List;

/**
 * Created on Aug 04, 2016
 *
 * @author Alexander Petukhov
 */
public class SocialUpdates {

    @Expose
    private long socialUpdatesVersion;

    @Expose
    private List<Diver> friends;

    @Expose
    private List<DiverFriendRequest> fromRequests;

    @Expose
    private List<DiverFriendRequest> toRequests;

    @Expose
    private List<LogbookBuddieRequest> buddieRequests;

    public long getSocialUpdatesVersion() {
        return socialUpdatesVersion;
    }

    public void setSocialUpdatesVersion(long socialUpdatesVersion) {
        this.socialUpdatesVersion = socialUpdatesVersion;
    }

    public List<Diver> getFriends() {
        return friends;
    }

    public void setFriends(List<Diver> friends) {
        this.friends = friends;
    }

    public List<DiverFriendRequest> getFromRequests() {
        return fromRequests;
    }

    public void setFromRequests(List<DiverFriendRequest> fromRequests) {
        this.fromRequests = fromRequests;
    }

    public List<DiverFriendRequest> getToRequests() {
        return toRequests;
    }

    public void setToRequests(List<DiverFriendRequest> toRequests) {
        this.toRequests = toRequests;
    }

    public List<LogbookBuddieRequest> getBuddieRequests() {
        return buddieRequests;
    }

    public void setBuddieRequests(List<LogbookBuddieRequest> buddieRequests) {
        this.buddieRequests = buddieRequests;
    }
}
