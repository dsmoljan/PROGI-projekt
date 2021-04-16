package hr.fer.progi.dogGO.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.sql.Time;
import java.sql.Date;
import java.util.Objects;
import java.util.Set;

@Entity
public class Reservation implements Comparable<Reservation>{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reservation_id")
    Long reservationId;

    @ManyToOne
    @JoinColumn(name = "walker_id", nullable=false)
    private Walker walker;

    private Date date;

    private Time startTime;

    private Time returnTime;

    private WalkStyle walkStyle;

    private boolean cancelled;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "reservation_dog_list",
            joinColumns = @JoinColumn(name = "reservation_id", referencedColumnName = "reservation_id"),
            inverseJoinColumns = @JoinColumn(name = "dog_id", referencedColumnName = "id"))
    @JsonIgnore
    private Set<Dog> dogs;

    public Reservation() {

    }

    public Long getReservationId() {
        return reservationId;
    }

    public Reservation(Walker walker, java.sql.Date date, Time startTime, Time returnTime, WalkStyle walkStyle,
    		boolean cancelled, Set<Dog> dogs) {
        this.walker = walker;
        this.date = date;
        this.startTime = startTime;
        this.returnTime = returnTime;
        this.walkStyle = walkStyle;
        this.cancelled = false;
        this.dogs = dogs;
    }

    public Walker getWalker() {
        return walker;
    }

    public void setWalker(Walker walker) {
        this.walker = walker;
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

    public WalkStyle getWalkStyle() {
        return walkStyle;
    }

    public void setWalkStyle(WalkStyle walkStyle) {
        this.walkStyle = walkStyle;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

	public Set<Dog> getDogs() {
		return dogs;
	}

	public void setDogs(Set<Dog> dogs) {
		this.dogs = dogs;
	}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reservation that = (Reservation) o;
        return Objects.equals(reservationId, that.reservationId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(reservationId);
    }

    @Override
    public int compareTo(Reservation r) {
        if (this.equals(r)){
            return 0;
        }
        if (this.getDate().getTime() < r.getDate().getTime()){ //ako je manja vrijednost, znači da je datum prije
            return -1;
        } else if (this.getDate().getTime() > r.getDate().getTime()){
            return 1;
        } else{                                                 //datumi su im jednaki, treba provjeriti vremena
            if (this.getStartTime().getTime() > r.getStartTime().getTime()){
                return 1;
            }else{
                return -1;
            }
        }
    }
}
