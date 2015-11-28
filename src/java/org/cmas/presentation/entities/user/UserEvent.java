package org.cmas.presentation.entities.user;

import org.cmas.entities.User;
import org.cmas.entities.UserAwareEntity;
import org.hibernate.validator.NotNull;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "user_events")
public class UserEvent extends UserAwareEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "validation.emptyField")
    @Column(nullable = false)
    private Date recordDate;

    @Column(nullable = false)
	@Enumerated(EnumType.STRING)
    private UserEventType userEventType;

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

    public UserEvent(UserEventType userEventType, String ip, String description, User user) {
        this(userEventType, ip, description);
        setUser(user);
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
