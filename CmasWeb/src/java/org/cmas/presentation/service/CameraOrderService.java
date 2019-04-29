package org.cmas.presentation.service;

import org.cmas.entities.diver.Diver;
import org.cmas.entities.fin.LoyaltyProgramItem;

/**
 * Created on Feb 13, 2019
 *
 * @author Alexander Petukhov
 */
public interface CameraOrderService {

    boolean createCameraOrder(Diver diver, LoyaltyProgramItem loyaltyProgramItem);

    boolean canCreateCameraOrder(Diver diver, LoyaltyProgramItem loyaltyProgramItem);
}
