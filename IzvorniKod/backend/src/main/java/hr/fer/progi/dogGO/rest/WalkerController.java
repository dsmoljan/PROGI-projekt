package hr.fer.progi.dogGO.rest;

import hr.fer.progi.dogGO.rest.dto.EditPassword;
import hr.fer.progi.dogGO.rest.dto.WalkerDetails;
import hr.fer.progi.dogGO.rest.dto.WalkerEdit;
import hr.fer.progi.dogGO.rest.dto.WalkerRanking;
import hr.fer.progi.dogGO.service.WalkerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.itextpdf.text.DocumentException;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/walker")
//@CrossOrigin("http://localhost:3000")
@CrossOrigin("https://exception-doggo-frontend-dev.herokuapp.com")
public class WalkerController {

    @Autowired
    private WalkerService walkerService;

    /**
     * Vraca listu detalja svih NEOBRISANIH setaca.
     */
    @GetMapping("/all")
    public List<WalkerDetails> listRegisteredUsers(){
        return walkerService.listAll();
    }

    @GetMapping
    public ResponseEntity<WalkerDetails> getWalker(@RequestParam Long walkerId) {
        WalkerDetails walkerDetails = walkerService.getWalkerDetailsById(walkerId);
        if(walkerDetails == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        return ResponseEntity.ok(walkerDetails);
    }

    /**
     * Vraca ukupan broj NEOBRISANIH setaca.
     */
    @GetMapping("/number-of-walkers")
    public Long numberOfWalkers() {
        return walkerService.getNumberOfWalkers();
    }
    
    @PostMapping("/edit-profile")
    @PreAuthorize("hasAnyRole('ROLE_WALKER', 'ROLE_ADMIN')")
    public ResponseEntity<WalkerDetails> editWalkerProfile(@RequestBody WalkerEdit walkerEdit) {
        WalkerDetails response = walkerService.editWalkerProfile(walkerEdit);
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/edit-password")
    @PreAuthorize("hasAnyRole('ROLE_WALKER')")
    public ResponseEntity<WalkerDetails> editWalkerPassword(@RequestBody EditPassword walkerEditPassword) {
        WalkerDetails response = walkerService.editWalkerPassword(walkerEditPassword);
        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping("/delete")
    @PreAuthorize("hasAnyRole('ROLE_WALKER', 'ROLE_ADMIN')")
    public ResponseEntity<WalkerDetails> deleteWalker(@RequestParam Long walkerId) {
        return ResponseEntity.ok().body(walkerService.deleteWalker(walkerId));
    }
    
    @GetMapping("/ranking")
    public List<WalkerRanking> getRanking(){
    	return walkerService.listRanking();
    }
    
    @GetMapping("/ranking/byid")
    public ResponseEntity<WalkerRanking> getRankingId(@RequestParam Long walkerId) {
    	return ResponseEntity.ok(walkerService.getRankingById(walkerId));
    }
    
    @GetMapping("/schedule")
    @PreAuthorize("hasAnyRole('ROLE_WALKER', 'ROLE_ADMIN')")
    public void getReservationsInPDF(@RequestParam Long walkerId, HttpServletResponse response) throws IOException, DocumentException {
    	byte[] pdfdoc = walkerService.getReservationsPDF(walkerId);
    	if(pdfdoc != null) {
    		response.setContentType("application/pdf");
    		response.getOutputStream().write(pdfdoc);
    		response.getOutputStream().close();
    	} else {
    		response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    	}
    }
}
