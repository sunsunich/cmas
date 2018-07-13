package org.cmas.presentation.controller.user.billing;

import org.springframework.beans.factory.annotation.Required;


public class PaySystemSettings {

    private String systempayMode;
    private String systempayCurrencyCode;
    private String systempaySiteId;
    private String systempayCertificate;

    public String getSystempayMode() {
        return systempayMode;
    }

    @Required
    public void setSystempayMode(String systempayMode) {
        this.systempayMode = systempayMode;
    }

    public String getSystempayCurrencyCode() {
        return systempayCurrencyCode;
    }

    @Required
    public void setSystempayCurrencyCode(String systempayCurrencyCode) {
        this.systempayCurrencyCode = systempayCurrencyCode;
    }

    public String getSystempaySiteId() {
        return systempaySiteId;
    }

    @Required
    public void setSystempaySiteId(String systempaySiteId) {
        this.systempaySiteId = systempaySiteId;
    }

    public String getSystempayCertificate() {
        return systempayCertificate;
    }

    @Required
    public void setSystempayCertificate(String systempayCertificate) {
        this.systempayCertificate = systempayCertificate;
    }
}
