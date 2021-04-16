package hr.fer.progi.dogGO;

import java.sql.Date;
import java.util.HashSet;
import java.util.Set;
import java.sql.Time;

import javax.transaction.Transactional;

import hr.fer.progi.dogGO.domain.*;
import hr.fer.progi.dogGO.repository.*;
import hr.fer.progi.dogGO.service.AssociationService;
import hr.fer.progi.dogGO.service.WalkerService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


@SpringBootApplication
public class DogGoApplication implements CommandLineRunner {

	@Autowired
	private AssociationRepository assocRepo;
	
	@Autowired
	private DogRepository dogRepo;
	
	@Autowired 
	private AssociationLoginRepository assocLoginRepo;
	
	@Autowired
	private AssociationLocationRepository locationRepo;
	
	@Autowired
	private WalkerRepository walkerRepo; 
	
	@Autowired
	private WalkerLoginRepository walkerLoginRepo;

	@Autowired
	private AssociationService associationService;

	@Autowired
	private WalkerService walkerService;
	
	@Autowired
	private ReservationRepository reservationRepo;

	@Autowired
	private DogAvailabilityRepository dogAvailabilityRepository;
	
	@Bean
	public PasswordEncoder pswdEncoder(){
		return new BCryptPasswordEncoder();
	}

	public static void main(String[] args) {
		SpringApplication.run(DogGoApplication.class, args);
	}
	
	@Transactional
	@Override
	public void run(String... args) throws Exception {
		
		// Walkers:
		Walker walker1 = new Walker("Lea", "Kopar", "lea.kopar@gmail.com", "036358979", true);
		Walker walker2 = new Walker("Ferdo", "Vranac", "ferdo.vranac@gmail.com", "036583970", true);
		Walker walker3 = new Walker("Tin", "Crnec", "tin.crnec@yahoo.com", "133658970", false);
		Walker walker4 = new Walker("Niko", "Rega", "niko.rega@gmail.com", "032223970", true);
		Walker walker5 = new Walker("Karlo", "Muhar", "muhar.karlo@gmail.com", "032223971", true);
		Walker walker6 = new Walker("Josip", "Posavec", "josip.posavec@gmail.com", "032223972", true);
		Walker walker7 = new Walker("Luka", "Horvat", "luka.horvat@gmail.com", "032223924", true);
		Walker walker8 = new Walker("Ivana", "Kovačević", "ivana.kovacevic@gmail.com", "032223974", true);
		Walker walker9 = new Walker("Nikolina", "Relja", "nikolina.relja@gmail.com", "032223975", true);
		Walker walker10 = new Walker("Duga", "Petrović", "duga.petrovic@gmail.com", "032223978", true);
		Walker admin = new Walker("Admin", "Admin", "admin@gmail.com", "0000000000", false);
		
		walkerRepo.save(walker1);
		walkerRepo.save(walker2);
		walkerRepo.save(walker3);
		walkerRepo.save(walker4);
		walkerRepo.save(walker5);
		walkerRepo.save(walker6);
		walkerRepo.save(walker7);
		walkerRepo.save(walker8);
		walkerRepo.save(walker9);
		walkerRepo.save(walker10);
		walkerRepo.save(admin);
		
		WalkerLogin walkerLogin1 = new WalkerLogin(walker1, "leakopar", walkerService.encodePass("1234567"));
		WalkerLogin walkerLogin2 = new WalkerLogin(walker2, "ferdovranac", walkerService.encodePass("1234567"));
		WalkerLogin walkerLogin3 = new WalkerLogin(walker3, "tincrnec", walkerService.encodePass("1234567"));
		WalkerLogin walkerLogin4 = new WalkerLogin(walker4, "nikorega", walkerService.encodePass("1234567"));
		WalkerLogin walkerLogin5 = new WalkerLogin(walker5, "karlomuhar", walkerService.encodePass("1234567"));
		WalkerLogin walkerLogin6 = new WalkerLogin(walker6, "josipposavec", walkerService.encodePass("1234567"));
		WalkerLogin walkerLogin7 = new WalkerLogin(walker7, "lukahorvar", walkerService.encodePass("1234567"));
		WalkerLogin walkerLogin8 = new WalkerLogin(walker8, "ivanakovacevic", walkerService.encodePass("1234567"));
		WalkerLogin walkerLogin9 = new WalkerLogin(walker9, "nikolinarelja", walkerService.encodePass("1234567"));
		WalkerLogin walkerLogin10 = new WalkerLogin(walker10, "dugapetrovic", walkerService.encodePass("1234567"));
		WalkerLogin walkerLoginAdmin = new WalkerLogin(admin, "admin", walkerService.encodePass("1234567"));
		
		walkerLoginRepo.save(walkerLogin1);
		walkerLoginRepo.save(walkerLogin2);
		walkerLoginRepo.save(walkerLogin3);
		walkerLoginRepo.save(walkerLogin4);
		walkerLoginRepo.save(walkerLogin5);
		walkerLoginRepo.save(walkerLogin6);
		walkerLoginRepo.save(walkerLogin7);
		walkerLoginRepo.save(walkerLogin8);
		walkerLoginRepo.save(walkerLogin9);
		walkerLoginRepo.save(walkerLogin10);
		walkerLoginRepo.save(walkerLoginAdmin);
		
		
		//Associations:
		Association assoc1 = new Association("12345678912", "DoggoRescue", "Pero", "Perić", "pero.peric@gmail.com", "http://web.com", "Priveremeni dom za čupave prijatelje", "http://img/slika.jpg", "0123456789");
		Association assoc2 = new Association("01234567891", "PawMates", "Marija", "Horvat", "marija.horvat@hotmail.com", "http://pawmates.hr", "Ponosni spasioci pasa", "http://img/slika2.jpg", "012301230");
		Association assoc3 = new Association("11134567891", "Woof", "Ivan", "Novak", "ivan.novak@yahoo.com", "http://woof.hr", "Woof woof!", "http://img/slika3.jpg", "012301232");
		
		assocRepo.save(assoc1);
		assocRepo.save(assoc2);
		assocRepo.save(assoc3);
		
		AssociationLogin assocLogin1 = new AssociationLogin(assoc1, "peroperic", associationService.encodePass("1234567"));
		AssociationLogin assocLogin2 =  new AssociationLogin(assoc2, "marijahorvat", associationService.encodePass("1234567"));
		AssociationLogin assocLogin3 =  new AssociationLogin(assoc3, "ivannovak", associationService.encodePass("1234567"));
		
 		assocLoginRepo.save(assocLogin1);
 		assocLoginRepo.save(assocLogin2);
 		assocLoginRepo.save(assocLogin3);
 		
 		
 		AssociationLocation loc1 = new AssociationLocation(assoc1, "Zagreb", "Ilica", "78");
 		AssociationLocation loc2 = new AssociationLocation(assoc2, "Split", "Zagrebačka", "14");
 		AssociationLocation loc3 = new AssociationLocation(assoc3, "Rijeka", "Vukovarska", "8");

 		locationRepo.save(loc1);
 		locationRepo.save(loc2);
 		locationRepo.save(loc3);
 		
 		//Dogs:
		Dog dog1 = new Dog("Rex", assoc1, "maltezer", null, "funky boy", WalkStyle.INDIVIDUAL);
		Dog dog2 = new Dog("Zeus", assoc1, "chiuaua", null, "pretty boy", WalkStyle.GROUP);
		
		Dog dog3 = new Dog("Floki", assoc2, "retriver", null, "golden boy", WalkStyle.INDIVIDUAL);
		Dog dog4 = new Dog("Kron", assoc2, "beagl", null, "happy boy", WalkStyle.GROUP);
		Dog dog5 = new Dog("Lepi", assoc2, "francuski bulldog", null, "quiet boy", WalkStyle.GROUP);
		
		Dog dog6 = new Dog("Lola", assoc3, "dalmatiner", null, "elegant queen", WalkStyle.GROUP);
		Dog dog7 = new Dog("Popi", assoc3, "pudlica", null, "dominant pudl", WalkStyle.GROUP);
		Dog dog8 = new Dog("Koki", assoc3, "mops", null, "cute mops", WalkStyle.INDIVIDUAL);
		
		dogRepo.save(dog1);
		dogRepo.save(dog2);
		dogRepo.save(dog3);
		dogRepo.save(dog4);
		dogRepo.save(dog5);
		dogRepo.save(dog6);
		dogRepo.save(dog7);
		dogRepo.save(dog8);
		
		Set<Dog> dogSet1 = new HashSet<>();
		Set<Dog> dogSet2 = new HashSet<>();
		Set<Dog> dogSet3 = new HashSet<>();
		
		dogSet1.add(dog1);
		dogSet1.add(dog2);
		
		dogSet2.add(dog3);
		dogSet2.add(dog4);
		dogSet2.add(dog5);
		
		dogSet3.add(dog6);
		dogSet3.add(dog7);
		dogSet3.add(dog8);
		
		assoc1.setDogSet(dogSet1);
		assoc2.setDogSet(dogSet2);
		assoc3.setDogSet(dogSet3);
		
		
		// reservations:
		Set<Dog> rDogs1 = new HashSet<>();
		rDogs1.add(dog4);
		rDogs1.add(dog5);
		
		Set<Dog> rDogs2 = new HashSet<>();
		rDogs2.add(dog6);
		rDogs2.add(dog7);
		
		Set<Dog> rDogs3 = new HashSet<>();
		rDogs3.add(dog1);

		//prosle rezervacije
		reservationRepo.save(new Reservation(walker1, Date.valueOf("2021-01-01"), Time.valueOf("8:00:00"), Time.valueOf("10:00:00"), WalkStyle.GROUP, false, rDogs1));
		reservationRepo.save(new Reservation(walker1, Date.valueOf("2021-01-04"), Time.valueOf("8:00:00"), Time.valueOf("10:00:00"), WalkStyle.GROUP, false, rDogs2));
		reservationRepo.save(new Reservation(walker1, Date.valueOf("2021-01-05"), Time.valueOf("8:00:00"), Time.valueOf("10:00:00"), WalkStyle.GROUP, false, rDogs1));
		reservationRepo.save(new Reservation(walker2, Date.valueOf("2021-01-02"), Time.valueOf("8:00:00"), Time.valueOf("10:00:00"), WalkStyle.GROUP, false, rDogs1));
		reservationRepo.save(new Reservation(walker3, Date.valueOf("2021-01-03"), Time.valueOf("8:00:00"), Time.valueOf("17:00:00"), WalkStyle.GROUP, false, rDogs1));
		reservationRepo.save(new Reservation(walker4, Date.valueOf("2021-01-05"), Time.valueOf("10:00:00"), Time.valueOf("12:00:00"), WalkStyle.GROUP, false, rDogs1));

		//buduce rezervacije
		reservationRepo.save(new Reservation(walker1, Date.valueOf("2021-02-01"), Time.valueOf("13:00:00"), Time.valueOf("14:00:00"), WalkStyle.GROUP, false, rDogs1));
		reservationRepo.save(new Reservation(walker1, Date.valueOf("2021-02-02"), Time.valueOf("13:00:00"), Time.valueOf("14:00:00"), WalkStyle.GROUP, false, rDogs1));
		reservationRepo.save(new Reservation(walker1, Date.valueOf("2021-02-02"), Time.valueOf("15:00:00"), Time.valueOf("16:00:00"), WalkStyle.INDIVIDUAL, false, rDogs3));
		reservationRepo.save(new Reservation(walker2, Date.valueOf("2021-02-05"), Time.valueOf("13:00:00"), Time.valueOf("14:00:00"), WalkStyle.GROUP, false, rDogs1));
		reservationRepo.save(new Reservation(walker3, Date.valueOf("2021-02-10"), Time.valueOf("13:00:00"), Time.valueOf("14:00:00"), WalkStyle.GROUP, false, rDogs1));

		//dog availability
		Date startDate = Date.valueOf("2021-01-01");
		Date endDate = Date.valueOf("2021-03-01");
		Time startTime = Time.valueOf("08:00:00");
		Time endTime = Time.valueOf("20:00:00");
		dogAvailabilityRepository.save(new DogAvailability(dog1, startDate, endDate, startTime, endTime));
		dogAvailabilityRepository.save(new DogAvailability(dog2, startDate, endDate, startTime, endTime));
		dogAvailabilityRepository.save(new DogAvailability(dog3, startDate, endDate, startTime, endTime));
		dogAvailabilityRepository.save(new DogAvailability(dog4, startDate, endDate, startTime, endTime));
		dogAvailabilityRepository.save(new DogAvailability(dog5, startDate, endDate, startTime, endTime));
		dogAvailabilityRepository.save(new DogAvailability(dog6, startDate, endDate, startTime, endTime));
		dogAvailabilityRepository.save(new DogAvailability(dog7, startDate, endDate, startTime, endTime));
		dogAvailabilityRepository.save(new DogAvailability(dog8, startDate, endDate, startTime, endTime));
	}

}
