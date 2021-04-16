package hr.fer.progi.dogGO.rest.dto;

import java.sql.Time;
import java.sql.Date;

public class DogIdByAvailability {

    Long associationId;
    Date date;
    Time startTime;
    Time endTime;

    public DogIdByAvailability(Long associationId, Date date, Time startTime, Time endTime) {
        this.associationId = associationId;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public Long getAssociationId() {
        return associationId;
    }

    public void setAssociationId(Long associationId) {
        this.associationId = associationId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Time getStartTime() {
        return startTime;
    }

    public void setStartTime(Time startTime) {
        this.startTime = startTime;
    }

    public Time getEndTime() {
        return endTime;
    }

    public void setEndTime(Time endTime) {
        this.endTime = endTime;
    }
}
