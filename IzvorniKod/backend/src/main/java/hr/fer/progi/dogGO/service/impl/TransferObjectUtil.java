package hr.fer.progi.dogGO.service.impl;

import java.util.HashSet;
import java.util.Set;

import hr.fer.progi.dogGO.domain.Dog;
import hr.fer.progi.dogGO.domain.Reservation;
import hr.fer.progi.dogGO.domain.Walker;
import hr.fer.progi.dogGO.rest.dto.DogDetails;
import hr.fer.progi.dogGO.rest.dto.ReservationDTO;
import hr.fer.progi.dogGO.rest.dto.WalkerDetails;

/**
 * Klasa koja nudi mapiranje izmedu entiteta i njihovih DTO.
 * @author Fani
 *
 */
public class TransferObjectUtil {
	
	/**
     * Za danog psa vraca njegov details DTO.
     * @param d dani pas
     * @return DogDetails instanca za danog psa.
     */
    public static DogDetails getDogDetails(Dog dog) {
    	return new DogDetails(dog.getId(), dog.getAssociation().getId(), dog.getAssociation().getName(),
    			dog.getName(), dog.getBreed(), dog.getDescription(), dog.getPreferredWalkStyle(), dog.getPicture(), dog.isDeleted());
    }
    
    /**
     * Za danu rezervaciju vraca njen DTO
     * @param r dana rezervacija
     * @return ReservationDTO za danu rezervaciju
     */
    public static ReservationDTO getReservationDTO(Reservation reservation) {
    	Set<Dog> dogs = reservation.getDogs();
    	Set<DogDetails> dogDTOs = new HashSet<>();
    	for (Dog dog : dogs) {
			dogDTOs.add(getDogDetails(dog));
		}
    	return new ReservationDTO(reservation.getReservationId(), reservation.getWalker(),
    			reservation.getDate(), reservation.getStartTime(), reservation.getReturnTime(), reservation.getWalkStyle(),
    			reservation.isCancelled(), dogDTOs);
    }
    /**
     * Za danog setaca vraca detalje ukljucujuci i username.
     * @param walker dani setac
     * @param walkerLogin login danog setaca
     * @return WalkerDetails za danog setaca
     */
    public static WalkerDetails getWalkerDetails(Walker walker) {
    	return new WalkerDetails(walker.getId(), walker.getFirstName(), walker.getLastName(),
    			walker.getLogin().getUsername(), walker.getEmail(), walker.getPhoneNumber(), walker.getPublicStats());
    }
    
}
