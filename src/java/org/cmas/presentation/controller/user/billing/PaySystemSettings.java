package org.cmas.presentation.controller.user.billing;

import org.springframework.beans.factory.annotation.Required;


public class PaySystemSettings {

    private String interKassaShopId;

    private String interKassaKey;

    public String getInterKassaShopId() {
        return interKassaShopId;
    }

    @Required
    public void setInterKassaShopId(String interKassaShopId) {
        this.interKassaShopId = interKassaShopId;
    }

    public String getInterKassaKey() {
        return interKassaKey;
    }

    @Required
    public void setInterKassaKey(String interKassaKey) {
        this.interKassaKey = interKassaKey;
    }
}
