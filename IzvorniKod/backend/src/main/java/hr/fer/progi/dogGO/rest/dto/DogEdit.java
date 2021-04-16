package hr.fer.progi.dogGO.rest.dto;

import hr.fer.progi.dogGO.domain.WalkStyle;

public class DogEdit {

    Long dogId;
    String name;
    String breed;
    String description;
    WalkStyle walkStyle;
    String picture;

    public DogEdit(Long dogId, String name, String breed, String description, WalkStyle walkStyle, String picture) {
        this.dogId = dogId;
        this.name = name;
        this.breed = breed;
        this.description = description;
        this.walkStyle = walkStyle;
        this.picture = picture;
    }

    public Long getDogId() {
        return dogId;
    }

    public void setDogId(Long dogId) {
        this.dogId = dogId;
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
