package org.cmas.entities.amateur;

import org.cmas.entities.User;
import org.cmas.entities.UserBalance;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.List;

/**
 * Created on Nov 15, 2015
 *
 * @author Alexander Petukhov
 */
@Entity
@Table(name = "amateurs")
public class Amateur extends User {

    @OneToOne
    private UserBalance userBalance;

    public Amateur() {
    }

    public Amateur(long id) {
        super(id);
    }

    @OneToMany(mappedBy = "amateur")
    private List<MassEventRequest> massEventRequests;

    public List<MassEventRequest> getMassEventRequests() {
        return massEventRequests;
    }

    public void setMassEventRequests(List<MassEventRequest> massEventRequests) {
        this.massEventRequests = massEventRequests;
    }

    @Override
    public UserBalance getUserBalance() {
        return userBalance;
    }

    @Override
    public void setUserBalance(UserBalance userBalance) {
        this.userBalance = userBalance;
    }
}
