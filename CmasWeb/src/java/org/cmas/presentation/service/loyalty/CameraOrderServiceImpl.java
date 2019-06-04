package org.cmas.presentation.service.loyalty;

import org.cmas.entities.diver.Diver;
import org.cmas.entities.loyalty.CameraOrder;
import org.cmas.entities.loyalty.LoyaltyProgramItem;
import org.cmas.presentation.dao.loyalty.CameraOrderDao;
import org.cmas.presentation.service.mail.MailService;
import org.cmas.util.random.Randomazer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;

/**
 * Created on Feb 13, 2019
 *
 * @author Alexander Petukhov
 */
public class CameraOrderServiceImpl implements CameraOrderService {

    private static final int CAMERA_ORDER_NUMBER_RAND_PART_LENGTH = 7;

    private final Object orderCreationLock = new Object();

    private String sendToEmail;

    private int allowedOrdersPerYearCnt;

    @Autowired
    private MailService mailService;

    @Autowired
    private CameraOrderDao cameraOrderDao;

    @Autowired
    private Randomazer randomazer;

    @Override
    public boolean createCameraOrder(Diver diver, LoyaltyProgramItem loyaltyProgramItem) {
        synchronized (orderCreationLock) {
            if (!canCreateCameraOrder(diver, loyaltyProgramItem)) {
                return false;
            }
        }
        CameraOrder cameraOrder = new CameraOrder();
        cameraOrder.setDiver(diver);
        cameraOrder.setCameraName(loyaltyProgramItem.getName());
        cameraOrder.setSendToEmail(sendToEmail);
        cameraOrder.setMarketPriceEuro(loyaltyProgramItem.getMarketPriceEuro());
        cameraOrder.setDiscountPriceEuro(loyaltyProgramItem.getMarketPriceEuro());
        cameraOrder.setLoyaltyProgramItem(loyaltyProgramItem);
        Long id = (Long) cameraOrderDao.save(cameraOrder);
        cameraOrder.setExternalNumber(
                randomazer.generateRandomStringByUniqueId(id, CAMERA_ORDER_NUMBER_RAND_PART_LENGTH));
        cameraOrderDao.updateModel(cameraOrder);
        mailService.sendCameraOrderMailToSubal(cameraOrder);
        mailService.sendCameraOrderMailToDiver(cameraOrder);
        return true;
    }

    @Override
    public boolean canCreateCameraOrder(Diver diver, LoyaltyProgramItem loyaltyProgramItem) {
        return cameraOrderDao.getOrderCntForYear(diver, loyaltyProgramItem) < allowedOrdersPerYearCnt;
    }

    @Required
    public void setSendToEmail(String sendToEmail) {
        this.sendToEmail = sendToEmail;
    }

    @Required
    public void setAllowedOrdersPerYearCnt(int allowedOrdersPerYearCnt) {
        this.allowedOrdersPerYearCnt = allowedOrdersPerYearCnt;
    }
}
