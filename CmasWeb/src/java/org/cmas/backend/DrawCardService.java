package org.cmas.backend;

import com.google.firebase.database.utilities.Pair;
import org.cmas.entities.cards.PersonalCard;

import java.awt.image.BufferedImage;

/**
 * Created on Apr 06, 2016
 *
 * @author Alexander Petukhov
 */
public interface DrawCardService {

    Pair<BufferedImage,BufferedImage> drawDiverCard(PersonalCard card) throws Exception;

    String getFileName(PersonalCard card);
}
