package org.cmas.entities.diver;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "notifications_counter")
public class NotificationsCounter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(optional = false, fetch = FetchType.LAZY, targetEntity = Diver.class)
    private Diver diver;

    @Column
    private boolean unsubscribed;

    @Column
    private int federationInitialCnt;

    @Column
    private int mobileCnt;

    @Column
    private int policyChangeCnt;

    @Column
    private String unsubscribeToken;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Diver getDiver() {
        return diver;
    }

    public void setDiver(Diver diver) {
        this.diver = diver;
    }

    public boolean isUnsubscribed() {
        return unsubscribed;
    }

    public void setUnsubscribed(boolean unsubscribed) {
        this.unsubscribed = unsubscribed;
    }

    public int getFederationInitialCnt() {
        return federationInitialCnt;
    }

    public void setFederationInitialCnt(int federationInitialCnt) {
        this.federationInitialCnt = federationInitialCnt;
    }

    public int getMobileCnt() {
        return mobileCnt;
    }

    public void setMobileCnt(int mobileCnt) {
        this.mobileCnt = mobileCnt;
    }

    public int getPolicyChangeCnt() {
        return policyChangeCnt;
    }

    public void setPolicyChangeCnt(int policyChangeCnt) {
        this.policyChangeCnt = policyChangeCnt;
    }

    public String getUnsubscribeToken() {
        return unsubscribeToken;
    }

    public void setUnsubscribeToken(String unsubscribeToken) {
        this.unsubscribeToken = unsubscribeToken;
    }
}
