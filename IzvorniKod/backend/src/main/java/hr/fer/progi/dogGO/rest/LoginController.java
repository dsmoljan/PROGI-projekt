package hr.fer.progi.dogGO.rest;

import hr.fer.progi.dogGO.domain.Association;
import hr.fer.progi.dogGO.domain.Walker;
import hr.fer.progi.dogGO.rest.dto.LoginForm;
import hr.fer.progi.dogGO.service.AdminService;
import hr.fer.progi.dogGO.service.AssociationService;
import hr.fer.progi.dogGO.service.WalkerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;


@RestController
@RequestMapping("/login")
//@CrossOrigin("http://localhost:3000")
@CrossOrigin("https://exception-doggo-frontend-dev.herokuapp.com")
public class LoginController {

    @Autowired
    private AssociationService associationService;

    @Autowired
    private WalkerService userService;

    @Autowired
    private AdminService adminService;

    @PostMapping("/association")
    public ResponseEntity<Long> loginAssociation(@RequestBody LoginForm loginForm) {
        Long associationId = associationService.login(loginForm.getUsername(), loginForm.getPassword());
        if(associationId == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        return ResponseEntity.ok()
                .body(associationId);
    }

    @PostMapping("/walker")
    public ResponseEntity<Long> loginWalker(@RequestBody LoginForm loginForm) {
        Long walkerId = userService.login(loginForm.getUsername(), loginForm.getPassword());
        if (walkerId == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        return ResponseEntity.ok()
                .body(walkerId);
    }

    @PostMapping("/admin")
    public ResponseEntity<Long> loginAdmin(@RequestBody LoginForm loginForm) {
        if (adminService.login(loginForm.getUsername(), loginForm.getPassword())) {
            return ResponseEntity.ok().body(0L);
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }

    @GetMapping("/current")
    public ResponseEntity<String> loggedIn(Authentication authentication) {
        return ResponseEntity.ok().body(authentication.getName());
    }
}
