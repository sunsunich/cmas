package org.cmas.entities;

import com.google.myjson.annotations.Expose;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Created on Sep 07, 2019
 *
 * @author Alexander Petukhov
 */
@Entity
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Table(name="error_reports")
public class ErrorReport implements Serializable, HasId {

    private static final long serialVersionUID = -8308397577540494864L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Expose
    private long id;

    @Column
    @Enumerated(EnumType.STRING)
    private ErrorReportStatus errorReportStatus;

    @Override
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
