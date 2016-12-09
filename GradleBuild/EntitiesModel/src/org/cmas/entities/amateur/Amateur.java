package org.cmas.entities.amateur;

import org.cmas.entities.User;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
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

    private static final long serialVersionUID = 3334151160628763359L;

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
}
