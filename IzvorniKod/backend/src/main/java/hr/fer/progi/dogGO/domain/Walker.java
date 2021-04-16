package hr.fer.progi.dogGO.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

@Entity
public class Walker {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min=1, max = 20)
    private String firstName;

    @NotNull
    @Size(min=1, max = 40)
    private String lastName;

    @Size(min=3, max = 40)
    @NotNull
    @Email
    private String email;

    @Size(min=9, max=10)
    private String phoneNumber;

    @NotNull
    private boolean publicStats;

    @Column(columnDefinition = "boolean default 'false'")
    private boolean deleted;

    @OneToMany(mappedBy="walker")
    private Set<Reservation> reservations;
    
    @OneToOne(mappedBy="walker", cascade = CascadeType.ALL, optional=true)
    @PrimaryKeyJoinColumn
    private WalkerLogin login;

    public Walker() {

    }
    
    public Walker(String firstName, String lastName, String email, String phoneNumber, boolean publicStats) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.publicStats = publicStats;
    }

    public Long getId(){
        return this.id;
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

    public boolean getPublicStats() {
        return publicStats;
    }

    public void setPublicStats(boolean publicStats) {
        this.publicStats = publicStats;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
    
	public Set<Reservation> getReservations() {
		return reservations;
	}
	
	public WalkerLogin getLogin() {
		return login;
	}
	
	public void setLogin(WalkerLogin login) {
		this.login = login;
	}
    
}