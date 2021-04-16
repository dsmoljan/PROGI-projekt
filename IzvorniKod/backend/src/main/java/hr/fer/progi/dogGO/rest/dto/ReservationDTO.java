package hr.fer.progi.dogGO.rest.dto;

import java.sql.Date;
import java.sql.Time;
import java.util.Objects;
import java.util.Set;

import hr.fer.progi.dogGO.domain.WalkStyle;
import hr.fer.progi.dogGO.domain.Walker;

public class ReservationDTO implements Comparable<ReservationDTO>{
    
	private Long reservationId;
    
	private WalkerDetails walker;
    
	private Date date;
    
	private Time startTime;
    
	private Time returnTime;
    
	private WalkStyle walkStyle;
    
	private boolean cancelled;
    
	private Set<DogDetails> dogs;

	public ReservationDTO(){

	}

	public ReservationDTO(Long reservationId, Walker walker, Date date, Time startTime, Time returnTime,
			WalkStyle walkStyle, boolean cancelled, Set<DogDetails> dogs) {
		this.reservationId = reservationId;
		this.walker = new WalkerDetails(walker);
		this.date = date;
		this.startTime = startTime;
		this.returnTime = returnTime;
		this.walkStyle = walkStyle;
		this.cancelled = cancelled;
		this.dogs = dogs;
	}

	public Long getReservationId() {
		return reservationId;
	}

	public void setReservationId(Long reservationId) {
		this.reservationId = reservationId;
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

	public Set<DogDetails> getDogs() {
		return dogs;
	}

	public void setDogs(Set<DogDetails> dogs) {
		this.dogs = dogs;
	}

	public WalkerDetails getWalker() {
		return walker;
	}

	public void setWalker(WalkerDetails walker) {
		this.walker = walker;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		ReservationDTO that = (ReservationDTO) o;
		return Objects.equals(reservationId, that.reservationId);
	}

	@Override
	public int hashCode() {
		return Objects.hash(reservationId);
	}

	@Override
	public int compareTo(ReservationDTO r) {
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
