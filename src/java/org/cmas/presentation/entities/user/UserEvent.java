package org.cmas.presentation.entities.user;

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

    @ManyToOne
    private UserClient user;

    private String ip;

    private String description;

    protected UserEvent(){

    }

    public UserEvent(UserEventType userEventType, UserClient user, String ip, String description) {
        recordDate = new Date();
        this.userEventType = userEventType;
        this.user = user;
        this.ip = ip;
        this.description = description;
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

    public UserClient getUser() {
        return user;
    }

    public void setUser(UserClient user) {
        this.user = user;
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
