package hr.fer.progi.dogGO.rest;

import hr.fer.progi.dogGO.rest.dto.AssociationDetails;
import hr.fer.progi.dogGO.rest.dto.AssociationEdit;
import hr.fer.progi.dogGO.rest.dto.AssociationLocationEdit;
import hr.fer.progi.dogGO.rest.dto.AssociationPreview;
import hr.fer.progi.dogGO.rest.dto.EditPassword;
import hr.fer.progi.dogGO.service.AssociationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/association")
@CrossOrigin("https://exception-doggo-frontend-dev.herokuapp.com")
//@CrossOrigin("http://localhost:3000")
public class AssociationController {

    @Autowired
    private AssociationService associationService;

    /**
     * Vraca listu previewa svih neobrisanih udruga (preview su id, ime i lokacija).
     */
    @GetMapping("/all")
    public List<AssociationPreview> listAssociations() {
        return associationService.listAllAssociationsDetails();
    }

    /**
     * Vraca detalje udruge (koji sadrze samo neobrisane pse) koja odgovara predanom id-u. Ovom metodom moze se dobiti i objekt
     * obrisane udruge.
     */
    @GetMapping
    public ResponseEntity<AssociationDetails> getAssociation(@RequestParam Long associationId) {
        AssociationDetails associationDetails = associationService.getAssociationDetailsById(associationId);
        if(associationDetails == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        return ResponseEntity.ok(associationDetails);
    }

    @GetMapping("/number-of-all-associations")
    public Long getNumberOfAllAssociations(){
        return associationService.getNumberOfAllAsscociations();
    }
    
    @PostMapping("/edit-profile")
    @PreAuthorize("hasAnyRole('ROLE_ASSOCIATION', 'ROLE_ADMIN')")
    public ResponseEntity<AssociationDetails> editAssociationProfile(@RequestBody AssociationEdit assocEdit) {
    	AssociationDetails response = associationService.editAssociationProfile(assocEdit);
    	return ResponseEntity.ok(response);
    }
    
    @PostMapping("/edit-location")
    @PreAuthorize("hasAnyRole('ROLE_ASSOCIATION', 'ROLE_ADMIN')")
    public ResponseEntity<AssociationDetails> editAssociationLocation(@RequestBody AssociationLocationEdit locationEdit) {
    	AssociationDetails response = associationService.editAssociationLocation(locationEdit);
    	return ResponseEntity.ok(response);
    }
    
    @PostMapping("/edit-password")
    @PreAuthorize("hasAnyRole('ROLE_ASSOCIATION', 'ROLE_ADMIN')")
    public ResponseEntity<AssociationDetails> editAssociationPassword(@RequestBody EditPassword passEdit) {
    	AssociationDetails response = associationService.editAssociationPassword(passEdit);
    	return ResponseEntity.ok(response);
    }
    
    @DeleteMapping("/delete")
    @PreAuthorize("hasAnyRole('ROLE_ASSOCIATION', 'ROLE_ADMIN')")
    public ResponseEntity<AssociationDetails> deleteAssociation(@RequestParam Long associationId) {
    	AssociationDetails response = associationService.deleteAssociation(associationId);
    	return ResponseEntity.ok(response);
    }
    
}