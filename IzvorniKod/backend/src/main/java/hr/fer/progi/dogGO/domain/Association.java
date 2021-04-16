package hr.fer.progi.dogGO.domain;

import org.hibernate.validator.constraints.URL;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

@Entity
public class Association {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(mappedBy = "association", cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private AssociationLocation associationLocation;

    @OneToMany(mappedBy = "association")
    private Set<Dog> dogSet;

    @NotNull
    @Column(length = 11)
    private String oib;

    @Size(min = 1, max = 40)
    @NotNull
    private String name;

    @NotNull
    @Size(min=1, max = 20)
    private String firstName;

    @NotNull
    @Size(min=1, max = 40)
    private String lastName;

    @Size(min=3, max = 40)
    @Column(unique = true)
    @NotNull
    @Email
    private String email;

    @Column(unique = true)
    private String webAddress;

    private String description;

    @URL
    private String pictureURL;

    @Size(min=9, max=10)
    private String phoneNumber;

    @Column(columnDefinition = "boolean default 'false'")
    private boolean deleted;

    public Association() {
    }

    public Association(String oib, String name, String firstName, String lastName, String email, String webAddress, String description, String pictureURL, String phoneNumber) {
        this.oib = oib;
        this.name = name;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.webAddress = webAddress;
        this.description = description;
        this.pictureURL = pictureURL;
        this.phoneNumber = phoneNumber;
    }

    public Long getId() {
        return id;
    }

    public String getOib() {
        return oib;
    }

    public void setOib(String oib) {
        this.oib = oib;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getWebAddress() {
        return webAddress;
    }

    public void setWebAddress(String webAddress) {
        this.webAddress = webAddress;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPictureURL() {
        return pictureURL;
    }

    public void setPictureURL(String pictureURL) {
        this.pictureURL = pictureURL;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public AssociationLocation getAssociationLocation() {
        return associationLocation;
    }

    public void setAssociationLocation(AssociationLocation associationLocation) {
        this.associationLocation = associationLocation;
    }

    public Set<Dog> getDogSet() {
        return dogSet;
    }

    public void setDogSet(Set<Dog> dogSet) {
        this.dogSet = dogSet;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
}
