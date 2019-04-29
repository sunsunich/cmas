package org.cmas.entities.fin;

import com.google.myjson.annotations.Expose;
import org.cmas.Globals;
import org.cmas.entities.DictionaryEntity;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;

/**
 * Created on Apr 28, 2019
 *
 * @author Alexander Petukhov
 */
@Entity
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Table(name = "loyalty_program_item")
public class LoyaltyProgramItem extends DictionaryEntity {
    private static final long serialVersionUID = -8742785133909179351L;

    @Expose
    @Column(nullable = false, precision = Globals.PRECISION, scale = Globals.SCALE)
    private BigDecimal marketPriceEuro;

    @Expose
    @Column(nullable = false, precision = Globals.PRECISION, scale = Globals.SCALE)
    private BigDecimal discountPriceEuro;

    @Expose
    private int featuresCnt;

    public int getFeaturesCnt() {
        return featuresCnt;
    }

    public void setFeaturesCnt(int featuresCnt) {
        this.featuresCnt = featuresCnt;
    }

    public BigDecimal getMarketPriceEuro() {
        return marketPriceEuro;
    }

    public void setMarketPriceEuro(BigDecimal marketPriceEuro) {
        this.marketPriceEuro = marketPriceEuro;
    }

    public BigDecimal getDiscountPriceEuro() {
        return discountPriceEuro;
    }

    public void setDiscountPriceEuro(BigDecimal discountPriceEuro) {
        this.discountPriceEuro = discountPriceEuro;
    }
}
