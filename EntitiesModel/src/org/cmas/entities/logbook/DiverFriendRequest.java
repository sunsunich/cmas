package org.cmas.entities.logbook;

import org.cmas.entities.diver.Diver;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created on Jun 12, 2016
 *
 * @author Alexander Petukhov
 */
@Entity
@Table(name = "diver_friend_requests")
public class DiverFriendRequest extends Request {

    public DiverFriendRequest() {
    }

    public DiverFriendRequest(Diver from, Diver to) {
        super(from, to);
    }
}
