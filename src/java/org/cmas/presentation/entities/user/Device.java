package org.cmas.presentation.entities.user;


import org.cmas.entities.DeviceType;
import org.cmas.entities.HasId;
import org.cmas.entities.User;
import org.cmas.entities.UserAwareEntity;

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
 * Created with IntelliJ IDEA.
 * User: sunsunich
 * Date: 22/11/12
 * Time: 05:06
 */
@Table
@Entity(name = "device")
public class Device extends UserAwareEntity implements Serializable, HasId {

    private static final long serialVersionUID = -5141987637013019468L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    @Enumerated(EnumType.STRING)
    private DeviceType deviceType;

    @Column(unique = true, nullable = false)
    private String clientDeviceId;

    private String pushServiceRegId;

    protected Device() {
    }

    public Device(DeviceType deviceType, String clientDeviceId, String pushServiceRegId, User user) {
        this.deviceType = deviceType;
        this.clientDeviceId = clientDeviceId;
        this.pushServiceRegId = pushServiceRegId;
        setUser(user);
    }

    @Override
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public DeviceType getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(DeviceType deviceType) {
        this.deviceType = deviceType;
    }

    public String getClientDeviceId() {
        return clientDeviceId;
    }

    public void setClientDeviceId(String clientDeviceId) {
        this.clientDeviceId = clientDeviceId;
    }

    public String getPushServiceRegId() {
        return pushServiceRegId;
    }

    public void setPushServiceRegId(String pushServiceRegId) {
        this.pushServiceRegId = pushServiceRegId;
    }
}
