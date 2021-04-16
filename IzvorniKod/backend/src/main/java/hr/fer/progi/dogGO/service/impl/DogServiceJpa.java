package hr.fer.progi.dogGO.service.impl;

import hr.fer.progi.dogGO.domain.Association;
import hr.fer.progi.dogGO.domain.Dog;
import hr.fer.progi.dogGO.domain.DogAvailability;
import hr.fer.progi.dogGO.domain.Reservation;
import hr.fer.progi.dogGO.repository.DogRepository;
import hr.fer.progi.dogGO.rest.dto.DogDetails;
import hr.fer.progi.dogGO.rest.dto.DogAdd;
import hr.fer.progi.dogGO.rest.dto.DogEdit;
import hr.fer.progi.dogGO.service.AssociationService;
import hr.fer.progi.dogGO.service.DogService;
import hr.fer.progi.dogGO.service.RequestDeniedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

@Service
public class DogServiceJpa implements DogService {

    @Autowired
    private DogRepository dogRepo;

    @Autowired
    private AssociationService associationService;

    /**
     * Vraca detalje psa s predanim id-em. Moguce dobiti samo NEOBRISANE pse.
     */
    @Override
    public DogDetails getDogById(Long Id) {
        Optional<Dog> dogOpt = dogRepo.findById(Id);
        if(dogOpt.isEmpty()) {
            return null;
        } else {
            return TransferObjectUtil.getDogDetails(dogOpt.get());
        }
    }

    /**
     * Vraca listu svih vremena kada je pas slobodan.
     */
    @Override
    public List<DogAvailability> getDogAvailabilityById(Long id) {
        Optional<Dog> dog = dogRepo.findById(id);
        if (dog.isEmpty()){
            throw new RequestDeniedException("Dog with ID " + Long.toString(id) + " doesn't exsist!");
        }
        return dog.get().getDogAvailabilityList();
    }

    /**
     * Vraca listu svih NEOBRISANIH pasa.
     */
    @Override
    public List<DogDetails> listAll() {
        List<Dog> dogs = dogRepo.findAll();
        List<DogDetails> dogDetailsList = new ArrayList<>();
        for(Dog dog : dogs) {
            if(!dog.isDeleted()) {
                dogDetailsList.add(TransferObjectUtil.getDogDetails(dog));
            }
        }
        return dogDetailsList;
    }

    /**
     * Vraca listu NEOBRISANIH pasa udruge s predanim id-em.
     */
    @Override
    public List<DogDetails> allAssociationDogsList(Long id) {
        List<Dog> dogs = dogRepo.findAllAssociationDogs(id);
        List<DogDetails> dogDetailsList = new ArrayList<>();
        for(Dog dog : dogs) {

            dogDetailsList.add(TransferObjectUtil.getDogDetails(dog));
        }
        return dogDetailsList;
    }


    @Override
    public DogDetails createDog(DogAdd dogDTO) {
        Assert.notNull(dogDTO, "Dog object must be given");

        Association association = associationService.getAssociationById(dogDTO.getAssociationId());
        Assert.notNull(association, "Association with given id doesn't exist");

        Dog dog = dogRepo.save(new Dog(dogDTO.getName(), association, dogDTO.getBreed(), dogDTO.getPicture(), dogDTO.getDescription(), dogDTO.getWalkStyle()));
        if(dog != null) {
            return this.getDogById(dog.getId());
        } else {
            return null;
        }
    }

    @Override
    public DogDetails editDog(DogEdit dogEdit) {

        Optional<Dog> dog = dogRepo.findById(dogEdit.getDogId());

        if (dog.isEmpty()){
            throw new RequestDeniedException("Dog with ID " + Long.toString(dogEdit.getDogId()) + " doesn't exsist!");
        }

        dog.get().setName(dogEdit.getName());
        dog.get().setBreed(dogEdit.getBreed());
        dog.get().setDescription(dogEdit.getDescription());
        dog.get().setPicture((dogEdit.getPicture()));
        dog.get().setPreferredWalkStyle(dogEdit.getWalkStyle());

        dogRepo.save(dog.get());

        return this.getDogById(dogEdit.getDogId());
    }

    @Override
    public Long getNumberOfAllDogs() {
        return dogRepo.countById();
    }

    @Override
    public long noOfDogsInAssociation(Long associationId) {
        Association association = associationService.getAssociationById(associationId);
        Assert.notNull(association, "Association with given OIB doesn't exist");
        return dogRepo.findNoOfDogsInAssociation(associationId);
    }

    @Override
    public DogDetails deleteDog(Long dogID) {
    	Optional<Dog> dog = dogRepo.findById(dogID);
        if (!dog.isPresent()){
            throw new RequestDeniedException("Dog with ID " + Long.toString(dogID) + " doesn't exsist!");
        }
  
        Set<Reservation> reservations = dog.get().getReservations();
        
        for(Reservation r : reservations) {
            if(r.getDate().compareTo(Date.valueOf(LocalDate.now())) >= 0) {
                r.setCancelled(true);
            }
        }
        
        DogDetails dogDetails = this.getDogById(dogID);
        dogRepo.deleteDogById(dogID);
        return dogDetails;
    }

}
