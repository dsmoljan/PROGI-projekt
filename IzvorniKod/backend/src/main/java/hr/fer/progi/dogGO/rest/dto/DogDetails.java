package hr.fer.progi.dogGO.rest.dto;

import hr.fer.progi.dogGO.domain.Dog;
import hr.fer.progi.dogGO.domain.WalkStyle;

/**
 * Detalji psa BEZ liste dostupnosti.
 */
public class DogDetails {
	
    private Long dogId;
    
    private Long associationId;
    
    private String associationName;
    
    private String name;
    
    private String breed;
    
    private String description;
    
    WalkStyle walkStyle;
    
    private String picture;
    
    boolean deleted;
    
    public DogDetails(Long dogId, Long associationId, String associationName, String name, String breed, String description, WalkStyle walkStyle, String picture, boolean deleted) {
        this.dogId = dogId;
        this.associationId = associationId;
        this.associationName = associationName;
        this.name = name;
        this.breed = breed;
        this.description = description;
        this.walkStyle = walkStyle;
        this.picture = picture;
        this.deleted = deleted;
    }
    
    public DogDetails(Dog dog) {
    	this.dogId = dog.getId();
        this.associationId = dog.getAssociation().getId();
        this.associationName = dog.getAssociation().getName();
        this.name = dog.getName();
        this.breed = dog.getBreed();
        this.description = dog.getDescription();
        this.walkStyle = dog.getPreferredWalkStyle();
        this.picture = dog.getPicture();
        this.deleted = dog.isDeleted();
    }

    public Long getDogId() {
        return dogId;
    }

    public void setDogId(Long dogId) {
        this.dogId = dogId;
    }

    public Long getAssociationId() {
        return associationId;
    }

    public void setAssociationId(Long associationId) {
        this.associationId = associationId;
    }

    public void setAssociationName(String associationName) { this.associationName = associationName; }

    public  String getAssociationName() { return associationName;}


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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public WalkStyle getWalkStyle() {
        return walkStyle;
    }

    public void setWalkStyle(WalkStyle walkStyle) {
        this.walkStyle = walkStyle;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
}
