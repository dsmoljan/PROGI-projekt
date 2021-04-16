package hr.fer.progi.dogGO.rest;

import hr.fer.progi.dogGO.rest.dto.DogAdd;
import hr.fer.progi.dogGO.rest.dto.DogDetails;
import hr.fer.progi.dogGO.rest.dto.DogEdit;
import hr.fer.progi.dogGO.service.DogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/dog")
//@CrossOrigin("http://localhost:3000")
@CrossOrigin("https://exception-doggo-frontend-dev.herokuapp.com")
public class DogManagementController {

    @Autowired
    private DogService dogService;

    @PostMapping("/add")
    @PreAuthorize("hasAnyRole('ROLE_ASSOCIATION', 'ROLE_ADMIN')")
    public ResponseEntity<DogDetails> createDog(@RequestBody DogAdd dogDTO) {
        DogDetails dogDetails = dogService.createDog(dogDTO);
        if(dogDetails == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } else {
            return ResponseEntity.ok(dogDetails);
        }
    }

    @PostMapping("/edit")
    @PreAuthorize("hasAnyRole('ROLE_ASSOCIATION', 'ROLE_ADMIN')")
    public ResponseEntity<DogDetails> editDog(@RequestBody DogEdit dogEdit) {
        DogDetails response = dogService.editDog(dogEdit);
        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping("/delete")
    @PreAuthorize("hasAnyRole('ROLE_ASSOCIATION', 'ROLE_ADMIN')")
    public ResponseEntity<DogDetails> deleteDog(@RequestParam Long dogID) {
        return ResponseEntity.ok().body(dogService.deleteDog(dogID));
    }
}
