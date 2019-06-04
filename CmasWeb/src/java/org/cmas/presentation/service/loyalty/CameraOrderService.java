package org.cmas.presentation.service.loyalty;

import org.cmas.entities.diver.Diver;
import org.cmas.entities.loyalty.LoyaltyProgramItem;

/**
 * Created on Feb 13, 2019
 *
 * @author Alexander Petukhov
 */
public interface CameraOrderService {

    boolean createCameraOrder(Diver diver, LoyaltyProgramItem loyaltyProgramItem);

    boolean canCreateCameraOrder(Diver diver, LoyaltyProgramItem loyaltyProgramItem);
}
