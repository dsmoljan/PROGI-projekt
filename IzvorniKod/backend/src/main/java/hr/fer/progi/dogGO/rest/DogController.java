package hr.fer.progi.dogGO.rest;

import hr.fer.progi.dogGO.domain.DogAvailability;
import hr.fer.progi.dogGO.rest.dto.DogDetails;
import hr.fer.progi.dogGO.service.DogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/dog")
//@CrossOrigin("http://localhost:3000")
@CrossOrigin("https://exception-doggo-frontend-dev.herokuapp.com")
public class DogController {

    @Autowired
    private DogService dogService;

    @GetMapping
    public ResponseEntity<DogDetails> getDog(@RequestParam Long dogId) {
        DogDetails dog = dogService.getDogById(dogId);
        if (dog == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        return ResponseEntity.ok(dog);
    }

    /**
     * Vraca listu svih NEOBRISANIH pasa.
     */
    @GetMapping("/all")
    public List<DogDetails> listDogs() {

        return dogService.listAll();
    }


    @GetMapping("/dogs-by-association")
    public List<DogDetails> allAssociationDogsSet(@RequestParam(value = "id") Long id) { //e sad, pošto svaki pas ima blob sliku u sebi, možda bi bilo pametno da za neke metode stvoriš DTO u kojem su svi podaci o psu osim slike

        //sad, ovo proslijeđivanje oib-a bi se moglo napraviti na 2 načina
        //1. je da se samo frontend brine o ulogiranom korisniku - frontend npr. zna da je udruga DogPower! ulogirana i
        //kad korisnik koji je ulogiran kao ta uloga otvori stranicu u kojoj se trebaju prikazati svi psi udruge, frontend proslijeđuje ispravan OIB
        //2. način je da se OIB proslijedi preko onog AuthenticationPrincipal, kao što je u onom crozovom primjeru
        //treba dodati provjeru postoji li taj oib/je li ok
        return dogService.allAssociationDogsList(id);
    }

    @GetMapping("/number-of-all-dogs")
    public Long numberOfAllDogs() {

        return dogService.getNumberOfAllDogs();
    }

    @GetMapping("/number-of-dogs")
    public long numberOfDogsInAssociation(@RequestParam(value = "id") Long associationId) {
        return dogService.noOfDogsInAssociation(associationId);
    }

    /**
     * Metoda vraca set vremena kada je pas s predanim id-em slobodan za setnju.
     */
    @GetMapping("/availability")
    public ResponseEntity<List<DogAvailability>> getDogAvailability(@RequestParam Long dogId) {
        return ResponseEntity.ok(dogService.getDogAvailabilityById(dogId));
    }

}

