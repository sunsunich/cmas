package org.cmas.entities.elearning;

import com.google.myjson.annotations.Expose;
import org.cmas.Globals;
import org.cmas.entities.HasId;
import org.cmas.entities.diver.Diver;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Version;
import java.io.Serializable;

@Entity
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Table(name = "elearing_token")
public class ElearningToken implements Serializable, HasId {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Expose
    @Column(unique = true, length = Globals.HALF_MAX_LENGTH)
    private String token;

    @OneToOne(optional = true, fetch = FetchType.LAZY, targetEntity = Diver.class)
    private Diver diver;

    @Version
    private long version;

    @Expose
    @Column
    @Enumerated(EnumType.STRING)
    private ElearningTokenStatus status;

    public ElearningToken() {
        status = ElearningTokenStatus.NOT_ASSIGNED;
    }

    @Override
    public long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Diver getDiver() {
        return diver;
    }

    public void setDiver(Diver diver) {
        this.diver = diver;
    }

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    public ElearningTokenStatus getStatus() {
        return status;
    }

    public void setStatus(ElearningTokenStatus status) {
        this.status = status;
    }
}
