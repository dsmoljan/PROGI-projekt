package hr.fer.progi.dogGO.service.impl;

import hr.fer.progi.dogGO.domain.AssociationLogin;
import hr.fer.progi.dogGO.domain.Dog;
import hr.fer.progi.dogGO.repository.*;
import hr.fer.progi.dogGO.rest.dto.AssociationDetails;
import hr.fer.progi.dogGO.rest.dto.AssociationEdit;
import hr.fer.progi.dogGO.rest.dto.AssociationLocationEdit;
import hr.fer.progi.dogGO.rest.dto.AssociationPreview;
import hr.fer.progi.dogGO.rest.dto.AssociationRegistration;
import hr.fer.progi.dogGO.rest.dto.DogDetails;
import hr.fer.progi.dogGO.rest.dto.EditPassword;
import hr.fer.progi.dogGO.service.AssociationService;
import hr.fer.progi.dogGO.domain.Association;
import hr.fer.progi.dogGO.domain.AssociationLocation;
import hr.fer.progi.dogGO.service.RequestDeniedException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.*;

import javax.transaction.Transactional;

@Service
public class AssociationServiceJpa implements AssociationService {

    @Autowired
    private AssociationRepository associationRepo;

    @Autowired
    private AssociationLoginRepository associationLoginRepo;

    @Autowired
    private WalkerLoginRepository walkerLoginRepo;
    
    @Autowired
    private AssociationLocationRepository locationRepo;

    @Autowired
    private DogRepository dogRepo;
 
    @Autowired
    private DogServiceJpa dogService;

    @Autowired
    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    /**
     * Vraca listu svih udruga, UKLJUCUJUCI obrisane.
     */
    @Override
    public List<Association> listAll() {
        return associationRepo.findAll();
    }

    /**
     * Vraca listu detalja svih NEOBRISANIH udruga. Detalji ukljucju id, ime i lokaciju udruge.
     */
    @Override
    public List<AssociationPreview> listAllAssociationsDetails() {
        List<AssociationPreview> list = new ArrayList<>();
        for(Association association : this.listAll()) {
            if(!association.isDeleted()) {
                if(association.getAssociationLocation() == null) {
                    list.add(new AssociationPreview(association.getId(), association.getName(), null));
                } else {
                    list.add(new AssociationPreview(association.getId(), association.getName(), association.getAssociationLocation().getCity()));
                }
            }
        }
        return list;
    }

    /**
     * Prima objekt tipa AssociationRegistration, provjerava jesu li predani podatci valjani
     * i ako jesu sprema novu udrugu u bazu.
     *
     */
    @Override
    public Long createAssociation(AssociationRegistration association) {

        Assert.notNull(association, "Association object must be given");
        Assert.notNull(association.getName(), "Association name must be given.");
        Assert.notNull(association.getFirstName(), "Owner's firstname must be given.");
        Assert.notNull(association.getLastName(), "Owner's lastname must be given.");
        Assert.notNull(association.getUsername(), "Association username must be given.");
        Assert.notNull(association.getPassword(), "Password must not be null");
        Assert.notNull(association.getEmail(), "Association email must be given.");

        if(!oibAvailable(association.getOib())) {
            throw new RequestDeniedException("Association with oib "+ association.getOib() + " already exists.");
        }

        if(!usernameAvailable(association.getUsername())) {
            throw new RequestDeniedException("Association with username "+ association.getUsername() + " already exists.");
        }

        if(association.getPassword().length() < 6 || association.getPassword().length() > 20) {
            throw new RequestDeniedException("Password must be between 6 and 20 characters.");
        }
        String encodedPassword = passwordEncoder.encode(association.getPassword());
        association.setPassword(encodedPassword);

        if(!emailAvailable(association.getEmail())) {
            throw new RequestDeniedException("Association with email "+ association.getUsername() + " already exists.");
        }

        Association newAssociation = new Association(association.getOib(), association.getName(), association.getFirstName(), association.getLastName(), association.getEmail(), null, null, null, null);
        AssociationLogin newLogin = new AssociationLogin(newAssociation, association.getUsername(), association.getPassword());

        Association associationSaved = associationRepo.save(newAssociation);
        associationLoginRepo.save(newLogin);
        return associationSaved.getId();
    }

    @Override
    public boolean usernameAvailable(String username) {
        return associationLoginRepo.countByUsername(username) == 0 && walkerLoginRepo.countByUsername(username) == 0 && !"admin".equals(username);
    }

    @Override
    public boolean emailAvailable(String email) {
        return associationRepo.countByEmail(email) == 0;
    }

    @Override
    public boolean oibAvailable(String oib) {
        return associationRepo.countByOib(oib) == 0;
    }

    /**
     * Prima username i password i provjerava postoji li u entitet u bazi kojem odgovaraju
     * predani username i password. Ako da vraca odgovarajucu udrugu, inace vraca null.
     *
     */
    @Override
    public Long login(String username, String password) {
        Optional<AssociationLogin> associationLogin = associationLoginRepo.findByUsername(username);
        if (associationLogin.isPresent()) {
            if (passwordEncoder.matches(password, associationLogin.get().getPassword())) {
                return associationLogin.get().getId();
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    /**
     * Za predani id trazi udrugu u bazi. Ako postoji vraca ju, inace vraca null.
     * Ovom metodom moguce je dobiti udrugu koja je obrisana.
     *
     */
    @Override
    public Association getAssociationById(Long associationId) {
        Optional<Association> association = associationRepo.findById(associationId);
        if(association.isEmpty()) {
            return null;
        } else {
            return association.get();
        }
    }

    /**
     * Vraca detalje udruge s predanim id-em. Detalji se sastoje od svih param association klase, ali dogset
     * se sastoji samo od neobrisanih pasa.
     */
    @Override
    public AssociationDetails getAssociationDetailsById(Long associationId) {
        Association association = this.getAssociationById(associationId);
        Optional<AssociationLogin> associationLogin = associationLoginRepo.findById(associationId);
        
        if(association == null){
            return null;
        } else {
            List<Dog> dogList = dogRepo.findAllAssociationDogs(associationId);
            List<DogDetails> dogDetails = new ArrayList<>(dogList.size());
            for (Dog dog : dogList) {
				dogDetails.add(new DogDetails(dog.getId(), dog.getAssociation().getId(), dog.getAssociation().getName(), dog.getName(),
						dog.getBreed(), dog.getDescription(), dog.getPreferredWalkStyle(), 
						dog.getPicture(), dog.isDeleted()));
			}
            
            String username = associationLogin.isEmpty() ? null : associationLogin.get().getUsername();
            
            return new AssociationDetails(associationId, association.getAssociationLocation(), dogDetails,
                    association.getOib(), association.getName(), association.getFirstName(),
                    association.getLastName(), username, association.getEmail(), association.getWebAddress(),
                    association.getDescription(), association.getPictureURL(),
                    association.getPhoneNumber(), association.isDeleted());
        }
    }

    /**
     * Za predani oib trazi udrugu u bazi i vraca ju ako postoji, inace vraca null.
     * Ovom metodom moguce je dobiti samo udrugu koja NIJE pobrisana.
     */
    @Override
    public Association findByOib(String oib) {
        Optional<Association> association = associationRepo.findByOib(oib);
        if(association.isEmpty()) {
            return null;
        } else {
            return association.get();
        }
    }

    @Override
    public Long getNumberOfAllAsscociations() {
        return associationRepo.countById();
    }

    @Override
    public String encodePass(String password) {
        return passwordEncoder.encode(password);
    }
    
    /**
     * Promjena osnovnih informacija profila udruge. 
     * @throws RequestDeniedException ako nema udruge s danim id ili njenog pripadnog Logina te ako je username vec zauzet
     */
	@Override
	public AssociationDetails editAssociationProfile(AssociationEdit assocEdit) {
		Assert.notNull(assocEdit, "Association object must be given");
		Optional<Association> associationOpt = associationRepo.findById(assocEdit.getId());
		Optional<AssociationLogin> associationLoginOpt = associationLoginRepo.findById(assocEdit.getId());
		
		if(associationOpt.isEmpty()) {
			throw new RequestDeniedException("Association with ID " + Long.toString(assocEdit.getId()) + " doesn't exist!");
		}
		
		if(associationLoginOpt.isEmpty()) {
			throw new RequestDeniedException("Error while fetching user with ID " + Long.toString(assocEdit.getId()));
		}
		
		Association assoc = associationOpt.get();
		AssociationLogin assocLogin = associationLoginOpt.get();
		
		Assert.notNull(assocEdit.getName(), "Name of the association must not be null");
		Assert.notNull(assocEdit.getFirstName(), "First name must not be null");
	    Assert.notNull(assocEdit.getLastName(), "Last name must not be null");
	    Assert.notNull(assocEdit.getEmail(), "Email must not be null");
	    
	    assoc.setName(assocEdit.getName());
	    assoc.setFirstName(assocEdit.getFirstName());
        assoc.setLastName(assocEdit.getLastName());
        assoc.setEmail(assocEdit.getEmail());
        assoc.setWebAddress(assocEdit.getWebAddress());
        assoc.setDescription(assocEdit.getDescription());
        assoc.setPictureURL(assocEdit.getPictureURL());
        assoc.setPhoneNumber(assocEdit.getPhoneNumber());
        
	    
	    if((!assocLogin.getUsername().equals(assocEdit.getUsername())) && (!usernameAvailable(assocEdit.getUsername()))) {
            throw new RequestDeniedException("User with username "+ assocEdit.getUsername() + " already exists.");
        }
	    
	    assocLogin.setUsername(assocEdit.getUsername());

        associationRepo.save(assoc);
        associationLoginRepo.save(assocLogin);
        
        return getAssociationDetailsById(assocEdit.getId());
	}
	
	/**
	 * Promjena lokacije udruge, ako prethodno nije postojala lokacija, stvara se nova, u suprotnom, izmjenjuje postojecu.
	 * @throws RequestDeniedException ako ne postoji udruga s danim id
	 */
	@Transactional
	@Override
	public AssociationDetails editAssociationLocation(AssociationLocationEdit locationEdit) {
		
		Assert.notNull(locationEdit, "Location object must be given");
		Assert.notNull(locationEdit.getAssociationId(), "Association ID must be given");
		
		Assert.notNull(locationEdit.getCity(), "City must not be null");
		Assert.notNull(locationEdit.getStreet(), "Street must not be null");
		Assert.notNull(locationEdit.getHouseNumber(), "House number must not be null");
		
		Optional<Association> associationOpt  = associationRepo.findById(locationEdit.getAssociationId());
		Optional<AssociationLocation> locationOpt = locationRepo.findById(locationEdit.getAssociationId());
		
		if(associationOpt.isEmpty()) {
			throw new RequestDeniedException("Association with ID" + Long.toString(locationEdit.getAssociationId()) + "doesn't exist.");
		}
		
		Association association = associationOpt.get();
		AssociationLocation location;
		
		// ako dosad jos nije postojala lokacija udruge:
		if(locationOpt.isEmpty()) {
			location = new AssociationLocation(associationOpt.get(), 
					locationEdit.getCity(), locationEdit.getStreet(), locationEdit.getHouseNumber());
			association.setAssociationLocation(location);
		
		// promjena postojece lokacije:
		} else {
			location = locationOpt.get();
			location.setCity(locationEdit.getCity());
			location.setStreet(locationEdit.getStreet());
			location.setHouseNumber(locationEdit.getHouseNumber());
		}
		
		locationRepo.save(location);
		associationRepo.save(association);
		
		return getAssociationDetailsById(locationEdit.getAssociationId());
	}

	/**
	 * Mijenja lozinku udruge ako se posalje dobra stara lozinka.
	 * @throws RequestDeniedException ako ne postoji udruga s danim id, stara lozinka nije ispravna ili nova lozinka nije prihvatjiva 
	 */
	@Override
	public AssociationDetails editAssociationPassword(EditPassword passEdit) {
		Assert.notNull(passEdit, "EditPassword object must be given.");
        Assert.notNull(passEdit.getId(), "Id must be given");
        Optional<AssociationLogin> associationLoginOpt = associationLoginRepo.findById(passEdit.getId());

        if(associationLoginOpt.isEmpty()) {
            throw new RequestDeniedException("User with ID " + Long.toString(passEdit.getId()) + " doesn't exist.");
        }
        
        AssociationLogin associationLogin = associationLoginOpt.get();

        Assert.notNull(passEdit.getOldPassword(), "Old password must be given.");
        Assert.notNull(passEdit.getNewPassword(), "New password must be given.");
        
        if(!passwordEncoder.matches(passEdit.getOldPassword(), associationLogin.getPassword())) {
            throw new RequestDeniedException("Old password is incorrect.");
        }
        if(passEdit.getNewPassword().length() < 6 || passEdit.getNewPassword().length() > 20) {
            throw new RequestDeniedException("Password must be between 6 and 20 characters.");
        }
        
        associationLogin.setPassword(this.encodePass(passEdit.getNewPassword()));
        
        associationLoginRepo.save(associationLogin);
        
        return getAssociationDetailsById(passEdit.getId());
	}

	/**
	 * Brise udrugu po id-u (odnosno oznacava deleted = true). Njen pripadni Login se pak u potpunosti brise u bazi.
	 * @throws RequestDeniedException ako ne postoji udruga s takvim id
	 */
	@Override
	public AssociationDetails deleteAssociation(Long associationId) {
		Optional<Association> associationOpt = associationRepo.findById(associationId);
		Optional<AssociationLogin> associationLogin = associationLoginRepo.findById(associationId);
		
		if(associationOpt.isEmpty()){
            throw new RequestDeniedException("Association with ID " + Long.toString(associationId) + " doesn't exsist!");
        }
		
		Association association = associationOpt.get();
		
		for(Dog dog :  association.getDogSet()) {
		    dogService.deleteDog(dog.getId());
		}
		
        association.setDeleted(true);

        associationRepo.save(association);
        associationLoginRepo.delete(associationLogin.get());

        return getAssociationDetailsById(associationId);
	}
}
