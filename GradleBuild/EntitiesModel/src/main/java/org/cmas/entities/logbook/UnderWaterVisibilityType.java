package org.cmas.entities.logbook;

/**
 * Created on Jan 07, 2017
 *
 * @author Alexander Petukhov
 */
public enum UnderWaterVisibilityType {
    LESS_ONE_M, ONE_TO_TWO_M, TOW_TO_THREE_M, THREE_TO_FIVE_M, FIVE_TO_TEN_M, TEN_TO_25_M, MORE_25_M;

    public String getName() {
        return name();
    }
}
