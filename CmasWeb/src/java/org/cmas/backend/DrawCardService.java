package org.cmas.backend;

import org.cmas.entities.cards.PersonalCard;

import java.awt.image.BufferedImage;

/**
 * Created on Apr 06, 2016
 *
 * @author Alexander Petukhov
 */
public interface DrawCardService {

    BufferedImage drawDiverCard(PersonalCard card, boolean isGold) throws Exception;

    String getFileName(PersonalCard card, boolean isGold);
}
