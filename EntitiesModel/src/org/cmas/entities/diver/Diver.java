package org.cmas.entities.diver;

import org.cmas.entities.CardUser;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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

    @Column
    @Enumerated(EnumType.STRING)
    private DiverLevel diverLevel;

    @Column
    @Enumerated(EnumType.STRING)
    private DiverType diverType;

    public Diver() {
    }

    public Diver(long id) {
        super(id);
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
}
