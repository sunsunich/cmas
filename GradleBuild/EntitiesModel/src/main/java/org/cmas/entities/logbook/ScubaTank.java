package org.cmas.entities.logbook;

import com.google.myjson.annotations.Expose;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Created on Jan 07, 2017
 *
 * @author Alexander Petukhov
 */
@Entity
@Table(name = "scuba_tanks")
public class ScubaTank implements Serializable{

    private static final long serialVersionUID = 8857324810437314337L;

    @Expose
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private DiveSpec diveSpec;

    @Expose
    @Column
    private boolean isDecoTank;

    @Expose
    @Column
    private Double size;

    @Expose
    @Column
    @Enumerated(EnumType.STRING)
    private VolumeMeasureUnit volumeMeasureUnit;

    @Expose
    @Column
    private Double startPressure;

    @Expose
    @Column
    private Double endPressure;

    @Expose
    @Column
    @Enumerated(EnumType.STRING)
    private PressureMeasureUnit pressureMeasureUnit;

    @Expose
    @Column
    @Enumerated(EnumType.STRING)
    private TankSupplyType supplyType;

    // gases
    @Expose
    @Column
    private boolean isAir;

    @Expose
    @Column
    private Double oxygenPercent;

    @Expose
    @Column
    private Double heliumPercent;

    // end gases


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DiveSpec getDiveSpec() {
        return diveSpec;
    }

    public void setDiveSpec(DiveSpec diveSpec) {
        this.diveSpec = diveSpec;
    }

    public boolean getIsDecoTank() {
        return isDecoTank;
    }

    public void setIsDecoTank(boolean isDecoTank) {
        this.isDecoTank = isDecoTank;
    }

    public Double getSize() {
        return size;
    }

    public void setSize(Double size) {
        this.size = size;
    }

    public VolumeMeasureUnit getVolumeMeasureUnit() {
        return volumeMeasureUnit;
    }

    public void setVolumeMeasureUnit(VolumeMeasureUnit volumeMeasureUnit) {
        this.volumeMeasureUnit = volumeMeasureUnit;
    }

    public Double getStartPressure() {
        return startPressure;
    }

    public void setStartPressure(Double startPressure) {
        this.startPressure = startPressure;
    }

    public Double getEndPressure() {
        return endPressure;
    }

    public void setEndPressure(Double endPressure) {
        this.endPressure = endPressure;
    }

    public PressureMeasureUnit getPressureMeasureUnit() {
        return pressureMeasureUnit;
    }

    public void setPressureMeasureUnit(PressureMeasureUnit pressureMeasureUnit) {
        this.pressureMeasureUnit = pressureMeasureUnit;
    }

    public TankSupplyType getSupplyType() {
        return supplyType;
    }

    public void setSupplyType(TankSupplyType supplyType) {
        this.supplyType = supplyType;
    }

    public boolean getIsAir() {
        return isAir;
    }

    public void setIsAir(boolean isAir) {
        this.isAir = isAir;
    }

    public Double getOxygenPercent() {
        return oxygenPercent;
    }

    public void setOxygenPercent(Double oxygenPercent) {
        this.oxygenPercent = oxygenPercent;
    }

    public Double getHeliumPercent() {
        return heliumPercent;
    }

    public void setHeliumPercent(Double heliumPercent) {
        this.heliumPercent = heliumPercent;
    }
}
