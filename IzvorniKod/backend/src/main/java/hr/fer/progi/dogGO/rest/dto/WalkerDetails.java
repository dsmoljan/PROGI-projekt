package hr.fer.progi.dogGO.rest.dto;

import hr.fer.progi.dogGO.domain.Walker;


public class WalkerDetails {
    
	private Long id;
    
	private String firstName;
    
	private String lastName;   
    
	private String username;
    
	private String email;
    
	private String phoneNumber;
    
	private boolean publicStats;

    public WalkerDetails(Long id, String firstName, String lastName, String username, String email, String phoneNumber, boolean publicStats) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.publicStats = publicStats;
    }
    
    public WalkerDetails(Walker walker) {
        this.id = walker.getId();
        this.firstName = walker.getFirstName();
        this.lastName = walker.getLastName();
        this.username = walker.getLogin().getUsername();
        this.email = walker.getEmail();
        this.phoneNumber = walker.getPhoneNumber();
        this.publicStats = walker.getPublicStats();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    
    public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public boolean isPublicStats() {
        return publicStats;
    }

    public void setPublicStats(boolean publicStats) {
        this.publicStats = publicStats;
    }
}
