package hr.fer.progi.dogGO.rest.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.sql.Date;
import java.sql.Time;
import java.util.List;

public class ReservationDetails {

    private Long walkerID;

    @JsonFormat(pattern="yyyy-MM-dd", timezone="Europe/Zagreb")
    private Date date;

    private Time startTime;

    private Time returnTime;

    private List<Long> dogsIDList;

    public Long getWalkerID() {
        return walkerID;
    }

    public void setWalkerID(Long walkerID) {
        this.walkerID = walkerID;
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

    public Time getReturnTime() {
        return returnTime;
    }

    public void setReturnTime(Time returnTime) {
        this.returnTime = returnTime;
    }

    public List<Long> getDogsIDList() {
        return dogsIDList;
    }

    public void setDogsIDList(List<Long> dogsIDList) {
        this.dogsIDList = dogsIDList;
    }
}
