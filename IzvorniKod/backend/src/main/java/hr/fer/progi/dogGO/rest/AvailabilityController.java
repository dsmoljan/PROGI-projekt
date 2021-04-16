package hr.fer.progi.dogGO.rest;

import hr.fer.progi.dogGO.domain.DogAvailability;
import hr.fer.progi.dogGO.rest.dto.DogAvailabilityDTO;
import hr.fer.progi.dogGO.service.AvailabilityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/dog/availability")
//@CrossOrigin("http://localhost:3000")
@CrossOrigin("https://exception-doggo-frontend-dev.herokuapp.com")
public class AvailabilityController {

    @Autowired
    private AvailabilityService availabilityService;

    /**
     * Dohvaća dostupnost psa preko id-a dostupnosti uzimajući u obzir SAMO dostupnost u udruzi, ne i rezervacije
     * @param availabilityId
     * @return
     */
    @GetMapping("/get/original/byid")
    public ResponseEntity<DogAvailability> getOriginalDogAvailabilitybyId(@RequestParam Long availabilityId){
        return ResponseEntity.ok(availabilityService.getOriginalDogAvailabilityByID(availabilityId));
    }

    /**
     * Dohvaća sve originalne dostupnosti psa uzimajući u obzir SAMO dostupnost u udruzi, ne i rezervacije
     * @param dogId
     * @return
     */
    @GetMapping("/get/original/all")
    public ResponseEntity<Set<DogAvailability>> getOriginalDogAvailabilityByDogId(@RequestParam Long dogId){
        return ResponseEntity.ok(availabilityService.getOriginalDogAvailabilityByDogId(dogId));
    }

    /**
     * Metoda vraca set vremena kada je pas s predanim id-em slobodan za setnju, uzimajući u obzir i rezervacije i dostupnost u udruzi
     */
    @GetMapping("/get")
    public ResponseEntity<Set<DogAvailabilityDTO>> getDogAvailability(@RequestParam Long dogId) {
        return ResponseEntity.ok(availabilityService.getDogAvailabilityByDogId(dogId));
    }

    @PostMapping("/set")
    public ResponseEntity<DogAvailability> setDogAvailability(@RequestBody DogAvailabilityDTO dogAvailabilityDTO){
        return ResponseEntity.ok(availabilityService.setDogAvailability(dogAvailabilityDTO));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<DogAvailability> deleteDogAvailability(@RequestParam Long availabilityId){
        return ResponseEntity.ok(availabilityService.deleteDogAvailability(availabilityId));
    }
}
