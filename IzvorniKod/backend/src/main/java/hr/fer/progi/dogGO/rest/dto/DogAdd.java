package hr.fer.progi.dogGO.rest.dto;

import hr.fer.progi.dogGO.domain.WalkStyle;

public class DogAdd {

    Long associationId;
    String name;
    String breed;
    String description;
    WalkStyle walkStyle;
    String picture;

    public DogAdd(Long associationId, String name, String breed, String description, WalkStyle walkStyle, String picture) {
        this.associationId = associationId;
        this.name = name;
        this.breed = breed;
        this.description = description;
        this.walkStyle = walkStyle;
        this.picture = picture;
    }

    public Long getAssociationId() {
        return associationId;
    }

    public void setAssociationId(Long associationId) {
        this.associationId = associationId;
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
}
