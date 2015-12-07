package org.cmas.entities;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created on Nov 16, 2015
 *
 * @author Alexander Petukhov
 */
@Table
@Entity(name = "personal_card_types")
public class PersonalCardType extends DictionaryEntity{
    private static final long serialVersionUID = -1288086184494543646L;
}
