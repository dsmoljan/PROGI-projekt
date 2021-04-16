package hr.fer.progi.dogGO.rest.dto;

import hr.fer.progi.dogGO.domain.DogAvailability;

import java.sql.Date;
import java.sql.Time;
import java.util.Objects;

/**
 * DTO za postavljanje DogAvalibility
 */
public class DogAvailabilityDTO implements Comparable<DogAvailabilityDTO>{

    private Long dogID;

    private Date startDate;

    private Date endDate;

    private Time startTime;

    private Time endTime;
    
    public DogAvailabilityDTO(){

    }

    public DogAvailabilityDTO(Long dogID, Date startDate, Date endDate, Time startTime, Time endTime) {
        this.dogID = dogID;
        this.startDate = startDate;
        this.endDate = endDate;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public Long getDogID() {
        return dogID;
    }

    public void setDogID(Long dogID) {
        this.dogID = dogID;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DogAvailabilityDTO that = (DogAvailabilityDTO) o;
        return Objects.equals(dogID, that.dogID) && Objects.equals(startDate, that.startDate) && Objects.equals(endDate, that.endDate) && Objects.equals(startTime, that.startTime) && Objects.equals(endTime, that.endTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dogID, startDate, endDate, startTime, endTime);
    }

    @Override
    public int compareTo(DogAvailabilityDTO a) {

        if (this.equals(a)){
            return 0;
        }

        if (this.getStartDate().getTime() < a.getStartDate().getTime()){
            return -1;
        }else if (this.getStartDate().getTime() > a.getStartDate().getTime()){
            return 1;
        }else{
            if (this.getStartTime().getTime() > a.getStartTime().getTime()){
                return 1;
            }else{
                return -1;
            }
        }

    }
}
