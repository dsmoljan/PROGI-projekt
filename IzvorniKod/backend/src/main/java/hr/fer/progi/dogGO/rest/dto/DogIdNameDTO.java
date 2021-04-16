package hr.fer.progi.dogGO.rest.dto;

import hr.fer.progi.dogGO.domain.Dog;

public class DogIdNameDTO {
    Long dogId;
    String name;

    public DogIdNameDTO(Dog dog) {
        this.dogId = dog.getId();
        this.name = dog.getName();
    }

    public DogIdNameDTO(Long dogId, String name) {
        this.dogId = dogId;
        this.name = name;
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
}
