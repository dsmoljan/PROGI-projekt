package hr.fer.progi.dogGO.rest.dto;


public class WalkerRanking {
	private Long walkerId;
	private String fullName;
	private int numOfWalks;
	private int numOfDogs;
	private double totalDuration;
	
	public WalkerRanking(Long walkerId, String firstName, String lastName, int numOfWalks, int numOfDogs, double totalDuration) {
		super();
		this.walkerId = walkerId;
		this.fullName = firstName + " " + lastName;
		this.numOfWalks = numOfWalks;
		this.numOfDogs = numOfDogs;
		this.totalDuration = totalDuration;
	}
	public Long getWalkerId() {
		return walkerId;
	}
	public void setWalkerId(Long walkerId) {
		this.walkerId = walkerId;
	}
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	public int getNumOfWalks() {
		return numOfWalks;
	}
	public void setNumOfWalks(int numOfWalks) {
		this.numOfWalks = numOfWalks;
	}
	public int getNumOfDogs() {
		return numOfDogs;
	}
	public void setNumOfDogs(int numOfDogs) {
		this.numOfDogs = numOfDogs;
	}
	public double getTotalDuration() {
		return totalDuration;
	}
	public void setTotalDuration(double totalDuration) {
		this.totalDuration = totalDuration;
	}
}
