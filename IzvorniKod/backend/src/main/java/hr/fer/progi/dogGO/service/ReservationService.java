package hr.fer.progi.dogGO.service;

import hr.fer.progi.dogGO.domain.Reservation;
import hr.fer.progi.dogGO.rest.dto.DogIdByAvailability;
import hr.fer.progi.dogGO.rest.dto.DogIdNameDTO;
import hr.fer.progi.dogGO.rest.dto.ReservationDTO;
import hr.fer.progi.dogGO.rest.dto.ReservationDetails;

import java.sql.Date;
import java.sql.Time;
import java.util.List;
import java.util.Set;

public interface ReservationService {

    /**
     * Dodaje novu rezervaciju
     * @param reservationDetails
     * @return
     */
    ReservationDTO addReservation(ReservationDetails reservationDetails);

    /**
     * Dohvaća (neotkazanu) rezervaciju po ID-u
     * @param reservationId
     * @return
     */
    ReservationDTO findById(Long reservationId);

    /**
     * Dohvaća sve rezervacije nekog psa
     * @param dogId
     * @return
     */
    Set<ReservationDTO> findByDogId(Long dogId);

    /**
     * Dohvaća sve rezervacije nekog šetača
     * @param walkerId
     * @return
     */
    Set<ReservationDTO> findByWalkerId(Long walkerId);

    /**
     * Otkazuje rezervaciju preko danog ID-a rezervacije
     * @param reservationId
     * @return
     */
    ReservationDTO cancelReservationById(Long reservationId);

    /**
     * Otkazuje sve rezervacije psa sa zadanim ID-om u nekom vremenskom intervalu
     * @param dogId
     * @param startDate
     * @param endDate
     * @param startTime
     * @param endTime
     * @return
     */
    Set<ReservationDTO> cancelReservationByDogIdAndDate(Long dogId, Date startDate, Date endDate, Time startTime, Time endTime);


    
    /**
     * Vraca sve rezervacije
     * @return
     */
	List<ReservationDTO> listAll();

    /**
     * Dohvaća SVE rezervacije neke udruge, i prošle i buduće, i cancelled, i koje nisu cancelled
     * @param associationId
     * @return
     */
	Set<ReservationDTO> findAllByAssociationId(Long associationId);

    /**
     * Dohvaća sve aktivne rezervacije neke udruge, dakle one koje trenutno traju ili slijede, te koje nisu cancelled
     * @param associationId
     * @return
     */
	Set<ReservationDTO> findAllActiveByAssociationId(Long associationId);

	/**
     * Metoda vraća sve pse dostupne za grupnu šetnju u odabranom terminu u odrabanoj udruzi.
     */
    List<DogIdNameDTO> getDogListByAvailability(DogIdByAvailability dogIdByAvailability);

}
