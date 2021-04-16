package hr.fer.progi.dogGO.domain;

import javax.persistence.*;

@Entity
public class AssociationLocation {

    @Id
    private Long id;

    @OneToOne
    @MapsId
    private Association association;

    private String city;

    private String street;

    private String houseNumber;

    public AssociationLocation() {

    }

    public AssociationLocation(Association association, String city, String street, String houseNumber) {
        this.association = association;
        this.city = city;
        this.street = street;
        this.houseNumber = houseNumber;
    }

    public Long getId() {
        return id;
    }

    public Association getAssociation() {
        return association;
    }

    public void setAssociation(Association association) {
        this.association = association;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(String houseNumber) {
        this.houseNumber = houseNumber;
    }
}
