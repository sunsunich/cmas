package org.cmas.entities.logbook;

import com.google.myjson.annotations.Expose;
import org.cmas.entities.DictionaryEntity;
import org.cmas.entities.diver.Diver;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Date;
import java.util.List;

/**
 * Created on Dec 27, 2015
 *
 * @author Alexander Petukhov
 */
@Entity
@Table(name = "logbook_entries")
public class LogbookEntry extends DictionaryEntity {

    private static final long serialVersionUID = -4425599195490254686L;

    @ManyToOne
    private Diver diver;

    @Column
    private String digest;

    @Expose
    @Column(nullable = false)
    private Date dateCreation;

    @Expose
    @Column(nullable = true)
    private Date dateEdit;

    @ManyToOne
    private DiveSpot diveSpot;

    @Column
    private int durationSeconds;

    @Column
    private int depthMeters;

    @Column
    @Enumerated(EnumType.STRING)
    private DiveScore score;

    @Column
    private String note;

    @ManyToOne
    private Diver instructor;

    @ManyToMany
    private List<Diver> buddies;

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

    public DiveSpot getDiveSpot() {
        return diveSpot;
    }

    public void setDiveSpot(DiveSpot diveSpot) {
        this.diveSpot = diveSpot;
    }

    public int getDurationSeconds() {
        return durationSeconds;
    }

    public void setDurationSeconds(int durationSeconds) {
        this.durationSeconds = durationSeconds;
    }

    public int getDepthMeters() {
        return depthMeters;
    }

    public void setDepthMeters(int depthMeters) {
        this.depthMeters = depthMeters;
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

    public List<Diver> getBuddies() {
        return buddies;
    }

    public void setBuddies(List<Diver> buddies) {
        this.buddies = buddies;
    }
}
