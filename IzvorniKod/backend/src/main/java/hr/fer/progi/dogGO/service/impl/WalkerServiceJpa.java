package hr.fer.progi.dogGO.service.impl;

import hr.fer.progi.dogGO.domain.Dog;
import hr.fer.progi.dogGO.domain.Reservation;
import hr.fer.progi.dogGO.domain.Walker;
import hr.fer.progi.dogGO.domain.WalkerLogin;
import hr.fer.progi.dogGO.repository.AssociationLoginRepository;
import hr.fer.progi.dogGO.repository.WalkerLoginRepository;
import hr.fer.progi.dogGO.repository.WalkerRepository;
import hr.fer.progi.dogGO.rest.dto.*;
import hr.fer.progi.dogGO.service.WalkerService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import hr.fer.progi.dogGO.service.RequestDeniedException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class WalkerServiceJpa implements WalkerService {
    @Autowired
    private WalkerRepository walkerRepo;

    @Autowired
    private WalkerLoginRepository walkerLoginRepo;

    @Autowired
    private AssociationLoginRepository associationLoginRepo;

    @Autowired
    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public List<WalkerDetails> listAll() {
        List<WalkerDetails> list = new ArrayList<>();
        for(Walker walker : walkerRepo.findAll()) {
        	list.add(new WalkerDetails(walker));
        }
        return list;
    }

    @Override
    public Long createWalker(WalkerRegistration walker) {
        Assert.notNull(walker, "User object must be given");
        Assert.notNull(walker.getUsername(), "Username must not be null");
        Assert.notNull(walker.getFirstName(), "First name must not be null");
        Assert.notNull(walker.getLastName(), "Last name must not be null");
        Assert.notNull(walker.getEmail(), "Email must not be null");

        if(!usernameAvailable(walker.getUsername())) {
            throw new RequestDeniedException("User with username "+ walker.getUsername() + " already exists.");
        }
        Assert.notNull(walker.getPassword(), "Password must not be null");
        if(walker.getPassword().length() < 6 || walker.getPassword().length() > 20) {
            throw new RequestDeniedException("Password must be between 6 and 20 characters.");
        }
        String encodedPassword = passwordEncoder.encode(walker.getPassword());
        walker.setPassword(encodedPassword);

        if(!emailAvailable(walker.getEmail())) {
            throw new RequestDeniedException("User with email "+ walker.getEmail()+ " already exists.");
        }

        Walker newWalker = new Walker(walker.getFirstName(), walker.getLastName(), walker.getEmail(), null, false);
        WalkerLogin walkerLogin = new WalkerLogin(newWalker, walker.getUsername(), walker.getPassword());

        Walker savedWalker = walkerRepo.save(newWalker);
        walkerLoginRepo.save(walkerLogin);

        return savedWalker.getId();
    }

    @Override
    public boolean usernameAvailable(String username) {
        return walkerLoginRepo.countByUsername(username) == 0 && associationLoginRepo.countByUsername(username) == 0 && !"admin".equals(username);
    }

    @Override
    public boolean emailAvailable(String email) {
        if(walkerRepo.countByEmail(email) > 0) {
            return false;
        }
        return true;
    }

    @Override
    public Long login(String username, String password) {
        Optional<WalkerLogin> walker = walkerLoginRepo.findByUsername(username);
        if(walker.isPresent()) {
            if(passwordEncoder.matches(password, walker.get().getPassword())) {
                return walker.get().getId();
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    @Override
    public String encodePass(String password) {
        return passwordEncoder.encode(password);
    }

    @Override
    public WalkerDetails getWalkerDetailsById(Long id) {
        Optional<Walker> walker = walkerRepo.findById(id);
        
        if(walker.isEmpty()) {
            return null;
        } else {
            return new WalkerDetails(walker.get());
        }
        
    }

    /**
     * Vraca ukupan broj NEOBRISANIH setaca.
     */
    @Override
    public Long getNumberOfWalkers() {
        return walkerRepo.countById();
    }
    
    /**
     * Promjena osnovnih informacija profila setaca. 
     * @throws RequestDeniedException ako nema setaca s danim id ili njegovog pripadnog Logina te ako je username vec zauzet
     */
    @Override
    public WalkerDetails editWalkerProfile(WalkerEdit walkerEdit) {
        Assert.notNull(walkerEdit, "Walker object must be given");
        Optional<Walker> walkerOpt = walkerRepo.findById(walkerEdit.getId());
        Optional<WalkerLogin> walkerLoginOpt = walkerLoginRepo.findById(walkerEdit.getId());

        if (walkerOpt.isEmpty()){
            throw new RequestDeniedException("Walker with ID " + Long.toString(walkerEdit.getId()) + " doesn't exist!");
        }
        if(walkerLoginOpt.isEmpty()) {
            throw new RequestDeniedException("Error while fetching user with ID " + Long.toString(walkerEdit.getId()));
        }

        Walker walker = walkerOpt.get();
        WalkerLogin walkerLogin = walkerLoginOpt.get();

        Assert.notNull(walkerEdit.getFirstName(), "First name must not be null");
        Assert.notNull(walkerEdit.getLastName(), "Last name must not be null");
        Assert.notNull(walkerEdit.getEmail(), "Email must not be null");

        if((!walker.getEmail().equals(walkerEdit.getEmail())) && (!emailAvailable(walkerEdit.getEmail()))) {
            throw new RequestDeniedException("User with email "+ walkerEdit.getEmail() + " already exists.");
        }

        walker.setFirstName(walkerEdit.getFirstName());
        walker.setLastName(walkerEdit.getLastName());
        walker.setEmail(walkerEdit.getEmail());
        walker.setPhoneNumber(walkerEdit.getPhoneNumber());
        walker.setPublicStats(walkerEdit.isPublicStats());

        if((!walkerLogin.getUsername().equals(walkerEdit.getUsername())) && (!usernameAvailable(walkerEdit.getUsername()))) {
             throw new RequestDeniedException("User with username "+ walkerEdit.getUsername() + " already exists.");
        }

        walkerLogin.setUsername(walkerEdit.getUsername());

        walkerRepo.save(walker);
        walkerLoginRepo.save(walkerLogin);

        return this.getWalkerDetailsById(walkerEdit.getId());
    }
    
    /**
	 * Mijenja lozinku setaca ako se posalje dobra stara lozinka.
	 * @throws RequestDeniedException ako ne postoji setac s danim id, stara lozinka nije ispravna ili nova lozinka nije prihvatjiva 
	 */
    @Override
    public WalkerDetails editWalkerPassword(EditPassword walkerEditPassword) {
        Assert.notNull(walkerEditPassword, "EditPassword object must be given.");
        Assert.notNull(walkerEditPassword.getId(), "Id must be given");
        Optional<WalkerLogin> walkerLoginOpt = walkerLoginRepo.findById(walkerEditPassword.getId());

        if(walkerLoginOpt.isEmpty()) {
            throw new RequestDeniedException("User with ID " + Long.toString(walkerEditPassword.getId()) + " doesn't exist.");
        }

        WalkerLogin walkerLogin = walkerLoginOpt.get();

        Assert.notNull(walkerEditPassword.getOldPassword(), "Old password must be given.");
        if(!passwordEncoder.matches(walkerEditPassword.getOldPassword(), walkerLogin.getPassword())) {
            throw new RequestDeniedException("Old password is incorrect.");
        }

        Assert.notNull(walkerEditPassword.getNewPassword(), "New password must be given.");
        if(walkerEditPassword.getNewPassword().length() < 6 || walkerEditPassword.getNewPassword().length() > 20) {
            throw new RequestDeniedException("Password must be between 6 and 20 characters.");
        }
        walkerLogin.setPassword(this.encodePass(walkerEditPassword.getNewPassword()));
        walkerLoginRepo.save(walkerLogin);
        return new WalkerDetails(walkerLogin.getWalker());
    }
    
    /**
	 * Brise setaca po id-u (odnosno oznacava deleted = true). Njegov pripadni Login se pak u potpunosti brise u bazi.
	 * @throws RequestDeniedException ako ne postoji setac s takvim id
	 */
    @Override
    public WalkerDetails deleteWalker(Long id) {
    	Optional<Walker> walker = walkerRepo.findById(id);
        if(!walker.isPresent()){
            throw new RequestDeniedException("Walker with ID " + Long.toString(id) + " doesn't exsist!");
        }
        
        Set<Reservation> reservations = walker.get().getReservations();
        for (Reservation r : reservations) {
            if(r.getDate().compareTo(Date.valueOf(LocalDate.now())) >= 0) {
                r.setCancelled(true);
            }
		}

        WalkerDetails walkerDetails = this.getWalkerDetailsById(id);
        Optional<WalkerLogin> walkerLogin = walkerLoginRepo.findById(id);

        walkerRepo.deleteWalkerById(id);
        walkerLoginRepo.delete(walkerLogin.get());

        return walkerDetails;
    }

    @Override
    public Walker findById(Long id) {

        return walkerRepo.findById(id).get();
    }

    /**
     * Ranking po idu setaca, bez obzira je li obrisan
     */
	@Override
	public WalkerRanking getRankingById(Long walkerId) {
		Optional<Walker> walkerOpt = walkerRepo.findById(walkerId);
		if(walkerOpt.isEmpty()) {
			return null;
		} else {
			Walker walker = walkerOpt.get();
			
			//neotkazane setnje:
			var reservations = walker.getReservations().stream().filter(
					(r) -> !r.isCancelled() && r.getDate().before( new Date(System.currentTimeMillis())))
				.collect(Collectors.toSet());
			
			List<Time> durations = new ArrayList<>();
			Set<Dog> dogs = new HashSet<>();
			
			for(Reservation r : reservations) {
				Set<Dog> walkedDogs = r.getDogs();
				dogs.addAll(walkedDogs);
				
				var start = r.getStartTime().getTime();
				var end = r.getReturnTime().getTime();
				Time duration = new Time(end - start);
				durations.add(duration);
			}
			
			Long totalTimeMili = (long) 0;
			for(Time duration : durations) {
				totalTimeMili += duration.getTime();
			}
			
			double hours = (double) totalTimeMili / 1000 / 60 / 60;
			
			return new WalkerRanking(walker.getId(), walker.getFirstName(), 
					walker.getLastName(), reservations.size(), dogs.size(), hours);
		}
	}
	
	/**
	 * Ranking svih NEOBRISANIH setaca
	 */
	@Override
	public List<WalkerRanking> listRanking() {
		List<Walker> all = walkerRepo.findAllPublic();
		List<WalkerRanking> allRank = new ArrayList<>();
		
		for(Walker walker : all) {
			allRank.add(getRankingById(walker.getId()));
		}
		
		return allRank;
	}

	@Override
	public byte[] getReservationsPDF(Long walkerId) throws DocumentException {
		Optional<Walker> walkerOpt = walkerRepo.findById(walkerId);
		
		if(!walkerOpt.isPresent()) {
			return null;
		}
		
		Walker walker = walkerOpt.get();
		Set<Reservation> reservations = walker.getReservations();
		
		Comparator<Reservation> byDate = (r1, r2) -> r1.getDate().compareTo(r2.getDate());
		Supplier<TreeSet<Reservation>> res = () -> new TreeSet<Reservation>(byDate);
		reservations = reservations.stream()
				.filter(r -> r.getDate().after(new Date(System.currentTimeMillis())))
				.collect(Collectors.toCollection(res));
		
		//create pdf document:
		Document document = new Document();
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		PdfWriter.getInstance(document, bytes);
		
		document.addSubject("Raspored korisnika " + walker.getLogin().getUsername()); 
		document.addAuthor("Exception");
		
		document.open();
		
		//create table:
		PdfPTable table = new PdfPTable(1);
		Stream.of("Moj raspored  -  " + walker.getFirstName() + " " + walker.getLastName())
	      .forEach(columnTitle -> {
	        PdfPCell header = new PdfPCell();
	        header.setBackgroundColor(BaseColor.WHITE);
	        header.setBorderWidth(0);
	        header.setBorderWidthBottom(1);
	        header.setPadding(15);
	        header.setPhrase(new Phrase(columnTitle));
	        table.addCell(header);
	    });

		for(Reservation reservation : reservations) {
            var dogs = reservation.getDogs();
            String assocName = ((Dog) dogs.toArray()[0]).getAssociation().getName();

			String cellContent = reservation.getDate() + "  " +
								 reservation.getStartTime() + 
								 " - " + reservation.getReturnTime()
                                + " | " + assocName + " |";
			

			for (Dog dog : dogs) {
				cellContent += "  " + dog.getName();
			}
			
			//create cell:
			PdfPCell cell = new PdfPCell();
			cell.setBorderWidth(0);
			cell.disableBorderSide(PdfPCell.LEFT);
			cell.disableBorderSide(PdfPCell.RIGHT);
			cell.disableBorderSide(PdfPCell.BOTTOM);
			cell.disableBorderSide(PdfPCell.TOP);
			cell.setPadding(10);
			cell.setPhrase(new Phrase(cellContent));
			table.addCell(cell);
		}
		
		document.add(table);
		document.close();
		
		return bytes.toByteArray();
	}
	
}
