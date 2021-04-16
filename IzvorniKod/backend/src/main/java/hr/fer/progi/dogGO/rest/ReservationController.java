package hr.fer.progi.dogGO.rest;

import hr.fer.progi.dogGO.domain.Reservation;
import hr.fer.progi.dogGO.domain.Walker;
import hr.fer.progi.dogGO.rest.dto.DogIdByAvailability;
import hr.fer.progi.dogGO.rest.dto.DogIdNameDTO;
import hr.fer.progi.dogGO.rest.dto.ReservationDTO;
import hr.fer.progi.dogGO.rest.dto.ReservationDetails;
import hr.fer.progi.dogGO.service.ReservationService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/reservations")
//@CrossOrigin("http://localhost:3000")
@CrossOrigin("https://exception-doggo-frontend-dev.herokuapp.com")
public class ReservationController {

    @Autowired
    ReservationService reservationService;
    
    @GetMapping("/all")
     public List<ReservationDTO> getAll(){
    	return reservationService.listAll();
    }
    
    
    @GetMapping("/get/byid")
    public ResponseEntity<ReservationDTO> getReservationById(@RequestParam Long reservationId){ //umjesto Reservation stavi neki DTO, u kojeg onda dodaj i ID-eve svih pasa koji su u rezervaciji
        return ResponseEntity.ok(reservationService.findById(reservationId));
    }

    /**
     * Dohvaća sve rezervacije nekog psa
     * @param dogId
     * @return
     */
    @GetMapping("/get/bydog")
    public ResponseEntity<Set<ReservationDTO>> getDogReservations(@RequestParam Long dogId){
        return ResponseEntity.ok(reservationService.findByDogId(dogId));
    }

    /**
     * Dohvaća sve rezervacije nekog walkera.
     */
    @GetMapping("/get/bywalker")
    public ResponseEntity<Set<ReservationDTO>> getWalkerReservations(@RequestParam Long walkerId){
        return ResponseEntity.ok(reservationService.findByWalkerId(walkerId));
    }

    /**
     * Dohvaća SVE rezervacije neke udruge, i prošle i buduće, i cancelled, i koje nisu cancelled
     * @param associationId
     * @return
     */
    @GetMapping("/get/byassociation/all")
     public ResponseEntity<Set<ReservationDTO>> getAllAssociationReservations(@RequestParam Long associationId){
        return ResponseEntity.ok(reservationService.findAllByAssociationId(associationId));
    }

    /**
     * Dohvaća sve aktivne rezervacije neke udruge, dakle one koje trenutno traju ili slijede, te koje nisu cancelled
     * @param associationId
     * @return
     */
    @GetMapping("/get/byassociation/active")
     public ResponseEntity<Set<ReservationDTO>> getAllActiveAssociationReservations(@RequestParam Long associationId){
        return ResponseEntity.ok(reservationService.findAllActiveByAssociationId(associationId));
    }

    @PostMapping("/add")
    public ResponseEntity<ReservationDTO> addReservation(@RequestBody ReservationDetails reservationDetails){
        return ResponseEntity.ok(reservationService.addReservation(reservationDetails));
    }

    @DeleteMapping("/cancel")
    public ResponseEntity<ReservationDTO> cancelReservation(@RequestParam Long reservationID){
        return ResponseEntity.ok(reservationService.cancelReservationById(reservationID));
    }

    /**
     * Metoda vraća sve pse dostupne za grupnu šetnju u odabranom terminu u odrabanoj udruzi.
     */
    @PostMapping("/doglist-by-availability")
    public ResponseEntity<List<DogIdNameDTO>> getDogListByAvailability(@RequestBody DogIdByAvailability dogIdByAvailability) {
        return ResponseEntity.ok(reservationService.getDogListByAvailability(dogIdByAvailability));
    }
}
