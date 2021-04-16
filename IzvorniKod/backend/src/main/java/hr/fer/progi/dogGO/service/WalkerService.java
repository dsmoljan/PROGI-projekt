package hr.fer.progi.dogGO.service;

import hr.fer.progi.dogGO.domain.Walker;
import hr.fer.progi.dogGO.rest.dto.WalkerDetails;
import hr.fer.progi.dogGO.rest.dto.WalkerEdit;
import hr.fer.progi.dogGO.rest.dto.WalkerRanking;
import hr.fer.progi.dogGO.rest.dto.EditPassword;
import hr.fer.progi.dogGO.rest.dto.WalkerRegistration;

import java.util.List;

import com.itextpdf.text.DocumentException;

public interface WalkerService {
    List<WalkerDetails> listAll();
    Long createWalker(WalkerRegistration walker);
    Long login(String username, String password);
    boolean usernameAvailable(String username);
    boolean emailAvailable(String email);
    String encodePass(String password);
    WalkerDetails getWalkerDetailsById(Long id);
    Long getNumberOfWalkers();
    WalkerDetails editWalkerProfile(WalkerEdit walkerEdit);
    WalkerDetails editWalkerPassword(EditPassword walkerEditPassword);
    WalkerDetails deleteWalker(Long id);
    Walker findById(Long id);
	List<WalkerRanking> listRanking();
	WalkerRanking getRankingById(Long walkerId);
	byte[] getReservationsPDF(Long walkerId) throws DocumentException;
}
