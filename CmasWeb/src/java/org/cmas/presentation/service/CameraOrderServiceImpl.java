package org.cmas.presentation.service;

import org.cmas.presentation.entities.CameraOrder;
import org.cmas.presentation.service.mail.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;

import java.math.BigDecimal;

/**
 * Created on Feb 13, 2019
 *
 * @author Alexander Petukhov
 */
public class CameraOrderServiceImpl implements CameraOrderService {

    private String cameraName;

    private String sendToEmail;

    private BigDecimal marketPriceEuro;

    private BigDecimal discountPriceEuro;

    @Autowired
    private MailService mailService;

    @Override
    public void sendCameraOrderEmails(CameraOrder cameraOrder) {
        mailService.sendCameraOrderMailToSubal(cameraOrder);
        mailService.sendCameraOrderMailToDiver(cameraOrder);
    }

    @Override
    public String getCameraName() {
        return cameraName;
    }

    @Override
    public String getSendToEmail() {
        return sendToEmail;
    }

    @Override
    public BigDecimal getMarketPriceEuro() {
        return marketPriceEuro;
    }

    @Override
    public BigDecimal getDiscountPriceEuro() {
        return discountPriceEuro;
    }

    @Required
    public void setMarketPriceEuro(BigDecimal marketPriceEuro) {
        this.marketPriceEuro = marketPriceEuro;
    }

    @Required
    public void setDiscountPriceEuro(BigDecimal discountPriceEuro) {
        this.discountPriceEuro = discountPriceEuro;
    }

    @Required
    public void setSendToEmail(String sendToEmail) {
        this.sendToEmail = sendToEmail;
    }

    @Required
    public void setCameraName(String cameraName) {
        this.cameraName = cameraName;
    }
}
