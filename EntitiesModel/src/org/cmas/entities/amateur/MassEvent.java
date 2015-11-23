package org.cmas.entities.amateur;

import com.google.myjson.annotations.Expose;
import org.cmas.entities.DictionaryEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * Created on Nov 20, 2015
 *
 * @author Alexander Petukhov
 */
@Entity
@Table(name = "mass_events")
public class MassEvent extends DictionaryEntity{
    @Expose
    @Column(nullable = false)
    private Date date;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
