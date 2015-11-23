package org.cmas.presentation.entities.user;

import org.cmas.entities.amateur.Amateur;
import org.cmas.entities.sport.Sportsman;
import org.hibernate.validator.NotNull;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "user_events")
public class UserEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "validation.emptyField")
    @Column(nullable = false)
    private Date recordDate;

    @Column(nullable = false)
	@Enumerated(EnumType.STRING)
    private UserEventType userEventType;

    @ManyToOne(optional = true, fetch = FetchType.LAZY, targetEntity = Sportsman.class)
    private Sportsman sportsman;

    @ManyToOne(optional = true, fetch = FetchType.LAZY, targetEntity = Amateur.class)
    private Amateur amateur;

    private String ip;

    private String description;

    protected UserEvent(){
    }

    private UserEvent(UserEventType userEventType, String ip, String description) {
        recordDate = new Date();
        this.userEventType = userEventType;
        this.ip = ip;
        this.description = description;
    }

    public UserEvent(UserEventType userEventType, String ip, String description, Sportsman sportsman) {
        this(userEventType, ip, description);
        this.sportsman = sportsman;
    }

    public UserEvent(UserEventType userEventType, String ip, String description, Amateur amateur) {
        this(userEventType, ip, description);
        this.amateur = amateur;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getRecordDate() {
        return recordDate;
    }

    public void setRecordDate(Date recordDate) {
        this.recordDate = recordDate;
    }

    public UserEventType getUserEventType() {
        return userEventType;
    }

    public void setUserEventType(UserEventType userEventType) {
        this.userEventType = userEventType;
    }

    public Sportsman getSportsman() {
        return sportsman;
    }

    public void setSportsman(Sportsman sportsman) {
        this.sportsman = sportsman;
    }

    public Amateur getAmateur() {
        return amateur;
    }

    public void setAmateur(Amateur amateur) {
        this.amateur = amateur;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
