package org.cmas.entities.logbook;

import com.google.myjson.annotations.Expose;
import org.cmas.Globals;
import org.cmas.entities.DictionaryEntity;
import org.cmas.entities.diver.Diver;
import org.cmas.entities.divespot.DiveSpot;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.Version;
import java.util.Date;
import java.util.Set;

/**
 * Created on Dec 27, 2015
 *
 * @author Alexander Petukhov
 */
@Entity
@Table(name = "logbook_entries")
public class LogbookEntry extends DictionaryEntity {

    private static final long serialVersionUID = -4425599195490254686L;

    @Version
    private long updateVersion;

    @Expose
    @ManyToOne
    private Diver diver;

    @Expose
    @Column
    private String digest;

    @Expose
    @Column
    @Enumerated(EnumType.STRING)
    private LogbookEntryState state;

    @Expose
    @Column(nullable = false)
    private Date dateCreation;

    @Expose
    @Column(nullable = false)
    private Date dateEdit;

    @Expose
    @Column(nullable = true)
    private Date diveDate;

    @Expose
    @Column(nullable = true)
    private Date prevDiveDate;

    @Expose
    @ManyToOne
    private DiveSpot diveSpot;

    @Expose
    @Column(nullable = true)
    private Double latitude;

    @Expose
    @Column(nullable = true)
    private Double longitude;

    @Expose
    @Column
    @Enumerated(EnumType.STRING)
    private DiveScore score;

    @Expose
    @Column(length = Globals.VERY_BIG_MAX_LENGTH)
    private String note;

    @Expose
    @ManyToOne
    private Diver instructor;

    @Lob
    @Basic(fetch = FetchType.EAGER)
    @Column(length = Globals.DB_PIC_MAX_BYTE_SIZE)
    private byte[] photo;

    @Expose
    @Transient
    private String photoBase64;

    @Expose
    @ManyToMany
    @JoinTable(name = "logbook_entry_buddies",
            joinColumns = @JoinColumn(name = "logbookEntryId"),
            inverseJoinColumns = @JoinColumn(name = "diverId")
    )
    private Set<Diver> buddies;

    @Expose
    @Column
    @Enumerated(EnumType.STRING)
    private LogbookVisibility visibility;

    @Expose
    @OneToOne
    private DiveSpec diveSpec;

    public LogbookEntry() {
        state = LogbookEntryState.NEW;
    }

    public LogbookEntryState getState() {
        return state;
    }

    public void setState(LogbookEntryState state) {
        this.state = state;
    }

    public Date getPrevDiveDate() {
        return prevDiveDate;
    }

    public void setPrevDiveDate(Date prevDiveDate) {
        this.prevDiveDate = prevDiveDate;
    }

    public DiveSpec getDiveSpec() {
        return diveSpec;
    }

    public void setDiveSpec(DiveSpec diveSpec) {
        this.diveSpec = diveSpec;
    }

    public byte[] getPhoto() {
        return photo;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

    public String getPhotoBase64() {
        return photoBase64;
    }

    public void setPhotoBase64(String photoBase64) {
        this.photoBase64 = photoBase64;
    }

    public LogbookVisibility getVisibility() {
        return visibility;
    }

    public void setVisibility(LogbookVisibility visibility) {
        this.visibility = visibility;
    }

    public Diver getDiver() {
        return diver;
    }

    public void setDiver(Diver diver) {
        this.diver = diver;
    }

    public String getDigest() {
        return digest;
    }

    public void setDigest(String digest) {
        this.digest = digest;
    }

    public Date getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(Date dateCreation) {
        this.dateCreation = dateCreation;
    }

    public Date getDateEdit() {
        return dateEdit;
    }

    public void setDateEdit(Date dateEdit) {
        this.dateEdit = dateEdit;
    }

    public Date getDiveDate() {
        return diveDate;
    }

    public void setDiveDate(Date diveDate) {
        this.diveDate = diveDate;
    }

    public DiveSpot getDiveSpot() {
        return diveSpot;
    }

    public void setDiveSpot(DiveSpot diveSpot) {
        this.diveSpot = diveSpot;
    }

    public DiveScore getScore() {
        return score;
    }

    public void setScore(DiveScore score) {
        this.score = score;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Diver getInstructor() {
        return instructor;
    }

    public void setInstructor(Diver instructor) {
        this.instructor = instructor;
    }

    public Set<Diver> getBuddies() {
        return buddies;
    }

    public void setBuddies(Set<Diver> buddies) {
        this.buddies = buddies;
    }

    public long getUpdateVersion() {
        return updateVersion;
    }

    public void setUpdateVersion(long updateVersion) {
        this.updateVersion = updateVersion;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
}
