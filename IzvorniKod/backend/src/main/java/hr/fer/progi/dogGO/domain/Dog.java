package hr.fer.progi.dogGO.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.validator.constraints.URL;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Entity
public class Dog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Association association;

    @OneToMany(mappedBy = "dog")
    private List<DogAvailability> dogAvailabilityList;

    @NotNull
    private String name;

    @Size(min = 1, max = 40)
    private String breed;

    @URL
    private String picture;

    // na ovom linku https://stackoverflow.com/questions/31469136/spring-data-jpa-storing-blob je detaljan opis rada sa blobovima u springu
    //ali pošto mi samo proslijeđujemo podatke između baze i frontenda, ne obrađujemo blob, možda je ovo dovoljno
    //@Lob
    //@Column(columnDefinition = "blob")
    //private Blob picture;


    @NotNull
    private WalkStyle preferredWalkStyle;

    @Size(min = 1, max = 300)
    private String description;

    @NotNull
    @Column(columnDefinition = "boolean default 'false'")
    private boolean deleted;

    @ManyToMany(mappedBy = "dogs")
    @JsonIgnore
    private Set<Reservation> reservations;

    public Dog(){
        
    }

    public Dog(String name, Association association, String breed, String picture, String description,  WalkStyle preferredWalkStyle) {
        this.name = name;
        this.association = association;
        this.breed = breed;
        this.picture = picture;
        this.preferredWalkStyle = preferredWalkStyle;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public WalkStyle getPreferredWalkStyle() {
        return preferredWalkStyle;
    }

    public void setPreferredWalkStyle(WalkStyle preferredWalkStyle) {
        this.preferredWalkStyle = preferredWalkStyle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public Association getAssociation() {
        return association;
    }

    public void setAssociation(Association association) {
        this.association = association;
    }

    public List<DogAvailability> getDogAvailabilityList() {
        return dogAvailabilityList;
    }

    public void setDogAvailabilityList(List<DogAvailability> dogAvailabilityList) {
        this.dogAvailabilityList = dogAvailabilityList;
    }

    public Set<Reservation> getReservations() {
		return reservations;
	}


	@Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Dog dog = (Dog) o;
        return deleted == dog.deleted && Objects.equals(id, dog.id) && Objects.equals(association, dog.association) && Objects.equals(name, dog.name) && Objects.equals(breed, dog.breed) && Objects.equals(picture, dog.picture) && preferredWalkStyle == dog.preferredWalkStyle && Objects.equals(description, dog.description) && Objects.equals(reservations, dog.reservations);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, association, name, breed, picture, preferredWalkStyle, description, deleted, reservations);
    }
}
