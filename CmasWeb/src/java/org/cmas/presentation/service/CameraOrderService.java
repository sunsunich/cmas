package org.cmas.presentation.service;

import org.cmas.presentation.entities.CameraOrder;

import java.math.BigDecimal;

/**
 * Created on Feb 13, 2019
 *
 * @author Alexander Petukhov
 */
public interface CameraOrderService {
    String getCameraName();

    String getSendToEmail();

    BigDecimal getMarketPriceEuro();

    BigDecimal getDiscountPriceEuro();

    void sendCameraOrderEmails(CameraOrder cameraOrder);

    int getAllowedOrdersPerYearCnt();
}
