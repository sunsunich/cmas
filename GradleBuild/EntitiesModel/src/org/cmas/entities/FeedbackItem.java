package org.cmas.entities;

import com.google.myjson.annotations.Expose;
import org.cmas.Globals;
import org.cmas.entities.diver.Diver;
import org.cmas.entities.divespot.DiveSpot;
import org.cmas.entities.logbook.LogbookEntry;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.List;

/**
 * Created on Sep 07, 2019
 *
 * @author Alexander Petukhov
 */
@Entity
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Table(name = "feedback_items")
public class FeedbackItem implements Serializable, HasId {

    private static final long serialVersionUID = -8308397577540494864L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Expose
    private long id;

    @Column
    @Enumerated(EnumType.STRING)
    private FeedbackItemStatus feedbackItemStatus;

    @ManyToOne
    private Diver creator;

    @ManyToOne
    private DiveSpot diveSpot;

    @ManyToOne
    private LogbookEntry logbookEntry;

    @Column(length = Globals.VERY_BIG_MAX_LENGTH)
    private String text;

    @OneToMany
    private List<UserFile> files;

    public FeedbackItem() {
        feedbackItemStatus = FeedbackItemStatus.NEW;
    }

    @Override
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public FeedbackItemStatus getFeedbackItemStatus() {
        return feedbackItemStatus;
    }

    public void setFeedbackItemStatus(FeedbackItemStatus feedbackItemStatus) {
        this.feedbackItemStatus = feedbackItemStatus;
    }

    public Diver getCreator() {
        return creator;
    }

    public void setCreator(Diver creator) {
        this.creator = creator;
    }

    public DiveSpot getDiveSpot() {
        return diveSpot;
    }

    public void setDiveSpot(DiveSpot diveSpot) {
        this.diveSpot = diveSpot;
    }

    public LogbookEntry getLogbookEntry() {
        return logbookEntry;
    }

    public void setLogbookEntry(LogbookEntry logbookEntry) {
        this.logbookEntry = logbookEntry;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<UserFile> getFiles() {
        return files;
    }

    public void setFiles(List<UserFile> files) {
        this.files = files;
    }
}
