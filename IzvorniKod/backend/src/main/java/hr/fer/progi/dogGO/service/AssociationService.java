package hr.fer.progi.dogGO.service;

import hr.fer.progi.dogGO.domain.Association;
import hr.fer.progi.dogGO.rest.dto.AssociationDetails;
import hr.fer.progi.dogGO.rest.dto.AssociationEdit;
import hr.fer.progi.dogGO.rest.dto.AssociationLocationEdit;
import hr.fer.progi.dogGO.rest.dto.AssociationPreview;
import hr.fer.progi.dogGO.rest.dto.AssociationRegistration;
import hr.fer.progi.dogGO.rest.dto.EditPassword;

import java.util.List;

public interface AssociationService {
    List<Association> listAll();
    List<AssociationPreview> listAllAssociationsDetails();
    Long createAssociation(AssociationRegistration association);
    boolean usernameAvailable(String username);
    boolean emailAvailable(String email);
    boolean oibAvailable(String oib);
    Long login(String username, String password);
    AssociationDetails getAssociationDetailsById(Long associationId);
    Association getAssociationById(Long associationId);
    Association findByOib(String oib);
    String encodePass(String password);
    Long getNumberOfAllAsscociations();
	AssociationDetails editAssociationProfile(AssociationEdit assocEdit);
	AssociationDetails editAssociationLocation(AssociationLocationEdit locationEdit);
	AssociationDetails editAssociationPassword(EditPassword passEdit);
	AssociationDetails deleteAssociation(Long associationId);
}
