package org.cmas.presentation.entities.billing;

import com.google.myjson.annotations.Expose;
import org.cmas.entities.diver.Diver;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Set;

/**
 * Created on Mar 31, 2019
 *
 * @author Alexander Petukhov
 */
@Entity
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Table(name="divers_payment_list")
public class DiverPaymentList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY, targetEntity = Diver.class)
    private Diver listCreator;

    @Expose
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "divers_payment_list_to_divers",
            joinColumns = @JoinColumn(name = "listId"),
            inverseJoinColumns = @JoinColumn(name = "diverId")
    )
    private Set<Diver> divers;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Diver getListCreator() {
        return listCreator;
    }

    public void setListCreator(Diver listCreator) {
        this.listCreator = listCreator;
    }

    public Set<Diver> getDivers() {
        return divers;
    }

    public void setDivers(Set<Diver> divers) {
        this.divers = divers;
    }
}
