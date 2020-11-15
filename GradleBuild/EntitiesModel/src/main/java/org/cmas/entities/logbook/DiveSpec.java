package org.cmas.entities.logbook;

import com.google.myjson.annotations.Expose;
import org.cmas.Globals;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.List;

/**
 * Created on Dec 31, 2016
 *
 * @author Alexander Petukhov
 */
@Entity
@Table(name = "dive_specs")
public class DiveSpec implements Serializable {

    private static final long serialVersionUID = -2393206458923815842L;

    @Expose
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Expose
    @Column(nullable = true)
    private Integer durationMinutes;

    @Expose
    @Column(nullable = true)
    private Integer maxDepthMeters;

    @Expose
    @Column(nullable = true)
    private Integer avgDepthMeters;

    // weather

    @Expose
    @Column
    @Enumerated(EnumType.STRING)
    private WeatherType weather;

    @Expose
    @Column
    @Enumerated(EnumType.STRING)
    private SurfaceType surface;

    @Expose
    @Column
    @Enumerated(EnumType.STRING)
    private CurrentType current;

    @Expose
    @Column
    @Enumerated(EnumType.STRING)
    private UnderWaterVisibilityType underWaterVisibility;

    @Expose
    @Column
    @Enumerated(EnumType.STRING)
    private WaterType waterType;

    @Expose
    @Column
    private Double airTemp;

    @Expose
    @Column
    private Double waterTemp;

    @Expose
    @Column
    @Enumerated(EnumType.STRING)
    private TemperatureMeasureUnit temperatureMeasureUnit;

// end weather

    @Expose
    @Column
    @Enumerated(EnumType.STRING)
    private DivePurposeType divePurpose;

    @Expose
    @Column
    @Enumerated(EnumType.STRING)
    private EntryType entryType;

    // outfit
    @Expose
    @Column
    private Double additionalWeightKg;

    @Expose
    @Column
    @Enumerated(EnumType.STRING)
    private DiveSuitType diveSuit;
    // end outfit

    // tanks
    @Expose
    @Column
    private boolean isApnea;
    @Expose
    @OneToMany(mappedBy = "diveSpec")
    private List<ScubaTank> scubaTanks;
    // tanks end

    // decompression
    //will be used later
    @Expose
    @OneToMany(mappedBy = "diveSpec")
    private List<DecoStop> decoStops;

    @Expose
    @Column(length = Globals.VERY_BIG_MAX_LENGTH)
    private String decoStepsComments;

    @Expose
    @Column
    private boolean hasSafetyStop;

    @Expose
    @Column
    private String cnsToxicity;

    // end decompression


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getDurationMinutes() {
        return durationMinutes;
    }

    public void setDurationMinutes(Integer durationMinutes) {
        this.durationMinutes = durationMinutes;
    }

    public Integer getMaxDepthMeters() {
        return maxDepthMeters;
    }

    public void setMaxDepthMeters(Integer maxDepthMeters) {
        this.maxDepthMeters = maxDepthMeters;
    }

    public Integer getAvgDepthMeters() {
        return avgDepthMeters;
    }

    public void setAvgDepthMeters(Integer avgDepthMeters) {
        this.avgDepthMeters = avgDepthMeters;
    }

    public WeatherType getWeather() {
        return weather;
    }

    public void setWeather(WeatherType weather) {
        this.weather = weather;
    }

    public SurfaceType getSurface() {
        return surface;
    }

    public void setSurface(SurfaceType surface) {
        this.surface = surface;
    }

    public CurrentType getCurrent() {
        return current;
    }

    public void setCurrent(CurrentType current) {
        this.current = current;
    }

    public UnderWaterVisibilityType getUnderWaterVisibility() {
        return underWaterVisibility;
    }

    public void setUnderWaterVisibility(UnderWaterVisibilityType underWaterVisibility) {
        this.underWaterVisibility = underWaterVisibility;
    }

    public WaterType getWaterType() {
        return waterType;
    }

    public void setWaterType(WaterType waterType) {
        this.waterType = waterType;
    }

    public Double getAirTemp() {
        return airTemp;
    }

    public void setAirTemp(Double airTemp) {
        this.airTemp = airTemp;
    }

    public Double getWaterTemp() {
        return waterTemp;
    }

    public void setWaterTemp(Double waterTemp) {
        this.waterTemp = waterTemp;
    }

    public TemperatureMeasureUnit getTemperatureMeasureUnit() {
        return temperatureMeasureUnit;
    }

    public void setTemperatureMeasureUnit(TemperatureMeasureUnit temperatureMeasureUnit) {
        this.temperatureMeasureUnit = temperatureMeasureUnit;
    }

    public DivePurposeType getDivePurpose() {
        return divePurpose;
    }

    public void setDivePurpose(DivePurposeType divePurpose) {
        this.divePurpose = divePurpose;
    }

    public EntryType getEntryType() {
        return entryType;
    }

    public void setEntryType(EntryType entryType) {
        this.entryType = entryType;
    }

    public Double getAdditionalWeightKg() {
        return additionalWeightKg;
    }

    public void setAdditionalWeightKg(Double additionalWeightKg) {
        this.additionalWeightKg = additionalWeightKg;
    }

    public DiveSuitType getDiveSuit() {
        return diveSuit;
    }

    public void setDiveSuit(DiveSuitType diveSuit) {
        this.diveSuit = diveSuit;
    }

    public boolean getIsApnea() {
        return isApnea;
    }

    public void setIsApnea(boolean isApnea) {
        this.isApnea = isApnea;
    }

    public List<ScubaTank> getScubaTanks() {
        return scubaTanks;
    }

    public void setScubaTanks(List<ScubaTank> scubaTanks) {
        this.scubaTanks = scubaTanks;
    }

    public List<DecoStop> getDecoStops() {
        return decoStops;
    }

    public void setDecoStops(List<DecoStop> decoStops) {
        this.decoStops = decoStops;
    }

    public String getDecoStepsComments() {
        return decoStepsComments;
    }

    public void setDecoStepsComments(String decoStepsComments) {
        this.decoStepsComments = decoStepsComments;
    }

    public boolean isHasSafetyStop() {
        return hasSafetyStop;
    }

    public void setHasSafetyStop(boolean hasSafetyStop) {
        this.hasSafetyStop = hasSafetyStop;
    }

    public String getCnsToxicity() {
        return cnsToxicity;
    }

    public void setCnsToxicity(String cnsToxicity) {
        this.cnsToxicity = cnsToxicity;
    }
}


