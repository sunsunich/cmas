package org.cmas.entities;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created on Nov 20, 2015
 *
 * @author Alexander Petukhov
 */
@Entity
@Table(name = "countries")
public class Country extends DictionaryEntity {
}
