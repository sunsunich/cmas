package org.cmas.entities.diver;

import org.cmas.entities.CardUser;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created on Dec 04, 2015
 *
 * @author Alexander Petukhov
 */
@Entity
@Table(name = "divers")
public class Diver extends CardUser {

    private static final long serialVersionUID = -6873304958863096818L;

    public Diver() {
    }

    public Diver(long id) {
        super(id);
    }
}
