package org.cmas.entities.sport;

import org.cmas.entities.DictionaryEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created on Nov 16, 2015
 *
 * @author Alexander Petukhov
 */
@Table
@Entity(name = "sportsman_card_types")
public class SportsmanCardType extends DictionaryEntity{
}
