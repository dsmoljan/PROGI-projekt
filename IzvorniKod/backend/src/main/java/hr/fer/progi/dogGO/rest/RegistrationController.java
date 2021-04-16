package hr.fer.progi.dogGO.rest;

import hr.fer.progi.dogGO.domain.Association;
import hr.fer.progi.dogGO.domain.Walker;
import hr.fer.progi.dogGO.rest.dto.AssociationRegistration;
import hr.fer.progi.dogGO.rest.dto.WalkerRegistration;
import hr.fer.progi.dogGO.service.AssociationService;
import hr.fer.progi.dogGO.service.WalkerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/registration")
//@CrossOrigin("http://localhost:3000")
@CrossOrigin("https://exception-doggo-frontend-dev.herokuapp.com")
public class RegistrationController {
    @Autowired
    private AssociationService associationService;

    @Autowired
    private WalkerService userService;

    @PostMapping("/association")
    public Long createAssociation(@RequestBody AssociationRegistration association) {
        return associationService.createAssociation(association);
    }

    @PostMapping("/association/username-available")
    public ResponseEntity associationUsernameAvailable(@RequestParam String username) {
        if (associationService.usernameAvailable(username)) {
            return ResponseEntity.ok(null);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }

    @PostMapping("/association/email-available")
    public ResponseEntity associationEmailAvailable(@RequestParam String email) {
        if (associationService.emailAvailable(email)) {
            return ResponseEntity.ok(null);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }

    @PostMapping("/association/oib-available")
    public ResponseEntity associationOIBAvailable(@RequestParam String oib) {
        if (associationService.oibAvailable(oib)) {
            return ResponseEntity.ok(null);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }

    @PostMapping("/walker")
    public Long createUser(@RequestBody WalkerRegistration walker){
        return userService.createWalker(walker);
    }

    @PostMapping("/walker/username-available")
    public ResponseEntity walkerUsernameAvailable(@RequestParam String username) {
        if (userService.usernameAvailable(username)) {
            return ResponseEntity.ok(null);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }

    @PostMapping("/walker/email-available")
    public ResponseEntity walkerEmailAvailable(@RequestParam String email) {
        if (userService.emailAvailable(email)) {
            return ResponseEntity.ok(null);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }
}
