package hr.fer.progi.dogGO.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.sql.Time;
import java.sql.Date;
import java.util.Objects;

@Entity
public class DogAvailability implements Comparable<DogAvailability>{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="dog")
    @JsonIgnore
    private Dog dog;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Europe/Zagreb")
    private Date startDate;

    @JsonFormat(pattern="yyyy-MM-dd", timezone="Europe/Zagreb")
    private Date endDate;
    //format hh:mm:ss
    private Time startTime;

    private Time endTime;

    boolean deleted;


    public DogAvailability() {

    }

    public DogAvailability(Dog dog, Date startDate, Date endDate, Time startTime, Time endTime) {
        this.dog = dog;
        this.startDate = startDate;
        this.endDate = endDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.deleted = false;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Dog getDog() {
        return dog;
    }

    public void setDog(Dog dog) {
        this.dog = dog;
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

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DogAvailability that = (DogAvailability) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }


    @Override
    public int compareTo(DogAvailability a) {

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
