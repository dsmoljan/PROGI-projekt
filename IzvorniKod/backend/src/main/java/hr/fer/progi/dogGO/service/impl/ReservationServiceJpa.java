package hr.fer.progi.dogGO.service.impl;

import hr.fer.progi.dogGO.domain.*;
import hr.fer.progi.dogGO.repository.DogRepository;
import hr.fer.progi.dogGO.repository.ReservationRepository;
import hr.fer.progi.dogGO.rest.dto.*;
import hr.fer.progi.dogGO.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.swing.text.html.Option;
import java.sql.Date;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class ReservationServiceJpa implements ReservationService {

    @Autowired
    ReservationRepository reservationRepo;

    @Autowired
    DogService dogService;

    @Autowired                //ovo je potencijalno jako jako jako loše rješenje, ali trenutačno jedino koje mi pada na pamet osim stvaranja pasa sa new, što ne dolazi u obzir, ili dodavanja još jedne metode findById u dogService samo koja vraća Dog
    DogRepository dogRepo;   //pitati!


    @Autowired
    AssociationService associationService;

    @Autowired
    WalkerService walkerService;

    @Autowired
    AvailabilityService availabilityService;

    @Override
    public ReservationDTO addReservation(ReservationDetails reservationDetails) {

        Assert.notNull(reservationDetails.getWalkerID(), "Walker ID can't be null!");
        Assert.notNull(reservationDetails.getDate(), "Date cannot be null!");
        Assert.notNull(reservationDetails.getStartTime(), "Start time cannot be null!");
        Assert.notNull(reservationDetails.getReturnTime(), "Return time cannot be null!");
        Assert.notNull(reservationDetails.getDogsIDList(), "Dog ID cannot be null!");
        Assert.isTrue(reservationDetails.getDogsIDList().size() >= 1, "At least one dog is needed to make a reservation");

        if(reservationDetails.getStartTime().compareTo(reservationDetails.getReturnTime()) >= 0) {
            throw new RequestDeniedException("The interval of the reservation is invalid.");
        }

        boolean groupWalk = false;
        if (reservationDetails.getDogsIDList().size() > 1){
            groupWalk = true;
        } else if(reservationDetails.getDogsIDList().size() == 1) {
            DogDetails dogDetails = dogService.getDogById(reservationDetails.getDogsIDList().get(0));
            if(dogDetails.getWalkStyle().equals(WalkStyle.GROUP)) {
                throw new RequestDeniedException("Cannot add dog with walk style GROUP to an individual walk.");
            }
        }

        Set<Dog> dogs = new HashSet<>();

        //provjera dostupnosti svih pasa, te ukoliko su psi u grupnoj šetnji, da svi imaju prefered walk style == GROUP

        for (Long dogId : reservationDetails.getDogsIDList()){
            DogDetails dogDetails = dogService.getDogById(dogId);
            if (dogDetails.getWalkStyle().equals(WalkStyle.INDIVIDUAL) && groupWalk == true){
                throw new RequestDeniedException("Cannot add dog with walk style INDIVIDUAL to a group walk.");
            }

            Set<DogAvailabilityDTO> dogAvailabilities = availabilityService.getDogAvailabilityByDogId(dogId);

            boolean foundTimeslot = false;

            for (DogAvailabilityDTO a : dogAvailabilities) {
                //mislim da je problem u vremsnkim zonama - reservationDetails datum je GMT + 1, a dogAvailibility datum je GMT + 0

                //problem riješen korištenjem anotacije @JsonFormat(pattern="yyyy-MM-dd", timezone="Europe/Zagreb") u klasama Reservation i DogAvailability
                //međutim, u slučaju ikakvih problema, ovo je isto ok rješenje

                // Date testDate = Date.valueOf(reservationDetails.getDate().toLocalDate().toEpochSecond());
//                Date resDate = Date.valueOf(reservationDetails.getDate().toString());
//                Date aStartDate = Date.valueOf(a.getStartDate().toString());
//                Date aEndDate = Date.valueOf(a.getEndDate().toString());
//
//                if (resDate.getTime() >= aStartDate.getTime() && resDate.getTime() <= aEndDate.getTime()){
//                    foundTimeslot = true;
//                    break;
//                }

                //možda napraviti novu pomoćnu timestamp klasu?
                System.out.println(reservationDetails.getStartTime().getTime());
                Date resDateStart = new Date(reservationDetails.getDate().getTime() + reservationDetails.getStartTime().getTime());
                Date resDateEnd = new Date(reservationDetails.getDate().getTime() + reservationDetails.getReturnTime().getTime());
                Date aDateStart = new Date(a.getStartDate().getTime() + a.getStartTime().getTime());
                Date aDateEnd = new Date(a.getEndDate().getTime() + a.getEndTime().getTime());

                if (resDateStart.getTime() >= aDateStart.getTime() && resDateEnd.getTime() <= aDateEnd.getTime()){
                    foundTimeslot = true;
                    //zauzmi timeslot
                    break;
                }

//                if ((reservationDetails.getDate().getTime() >= a.getStartDate().getTime()) &&
//                        (reservationDetails.getDate().getTime() <= a.getEndDate().getTime())) {
//                    foundTimeslot = true;
//                    break;
//                }

            }

            if (foundTimeslot == false){
                throw new RequestDeniedException("Cannot make a reservation for dog with ID " + dogId + " as that dog isn't available during specified reservation time");
            }

            Optional<Dog> dog = dogRepo.findById(dogId);
            if (dog.isEmpty()){
                throw new RequestDeniedException("Cannot find dog with id " + dogId );
            }

            dogs.add(dog.get());

       }

       Walker walker = walkerService.findById(reservationDetails.getWalkerID());
       
       //******check if walker has made a reservation at this time**************
       long start = reservationDetails.getStartTime().getTime();
 	   long end = reservationDetails.getReturnTime().getTime();
 	   Date newReservationDate = reservationDetails.getDate();
 	   
 	   var reservations = this.findByWalkerId(walker.getId());
 	   
       for (ReservationDTO r : reservations) {
    	   long preExistingStart = r.getStartTime().getTime();
    	   long preExisitingEnd = r.getReturnTime().getTime();
    	   Date preExistingReservationDate = r.getDate();
    	 
    	   if(newReservationDate.compareTo(preExistingReservationDate) == 0
    			   && 
    			(start >= preExistingStart && start <= preExisitingEnd || end >= preExistingStart && end <= preExisitingEnd)) {
    		   throw new RequestDeniedException("This walker has already made a reservation at this time.");
    	   }
    	}
        //**********************************************************************
        WalkStyle walkStyle = groupWalk == true ? WalkStyle.GROUP : WalkStyle.INDIVIDUAL;

        Calendar cal = Calendar.getInstance();
        cal.setTime(reservationDetails.getDate());
        cal.add(Calendar.HOUR, 6);

        //java.sql.Date sqlDate = new java.sql.Date(reservationDetails.getDate().getTime());
        java.sql.Date sqlDate = new java.sql.Date(cal.getTimeInMillis());
        System.out.println(reservationDetails.getDate().getTime());
        System.out.println(cal.getTimeInMillis());

        //sad kad smo provjerili da sve pse možemo rezervirati u traženom terminu, možemo zapisati i samu rezervaciju, te s njom i pse u tablicu ReservationDogList (automatski se zapisuje)
        Reservation res = reservationRepo.save(new Reservation(walker, sqlDate, reservationDetails.getStartTime(), reservationDetails.getReturnTime(), walkStyle, true, dogs));
        return TransferObjectUtil.getReservationDTO(res);
    }

    @Override
    public Set<ReservationDTO> findByDogId(Long dogId) {
    	Set<Reservation> reservations = reservationRepo.findAllByDogId(dogId);
    	Set<ReservationDTO> reservationDTOs = new HashSet<>();
    	for (Reservation reservation : reservations) {
			reservationDTOs.add(TransferObjectUtil.getReservationDTO(reservation));
		}
        return reservationDTOs;
    }

    @Override
    public ReservationDTO findById(Long reservationId) {
        Optional<Reservation> reservation = reservationRepo.findById(reservationId);

        if (reservation.isPresent()){
            return TransferObjectUtil.getReservationDTO(reservation.get());
        }else {
            throw new RequestDeniedException("Reservation with given ID doesn't exsist");
        }

    }

    @Override
    public Set<ReservationDTO> findByWalkerId(Long walkerId) {
    	Set<Reservation> reservations = reservationRepo.findAllByWalkerId(walkerId);
    	Set<ReservationDTO> reservationDTOs = new HashSet<>();
    	for (Reservation reservation : reservations) {
			reservationDTOs.add(TransferObjectUtil.getReservationDTO(reservation));
		}
        return reservationDTOs;
    }

    @Override
    public ReservationDTO cancelReservationById(Long reservationId) {
        Optional<Reservation> reservation = reservationRepo.findById(reservationId);

        if (reservation.isPresent()){
            reservationRepo.cancelReservationById(reservation.get().getReservationId());
            return TransferObjectUtil.getReservationDTO(reservation.get());
        }else{
            throw new RequestDeniedException("Cannot cancel reservation - reservation with given ID not found");

        }


    }

    @Override
    public Set<ReservationDTO> cancelReservationByDogIdAndDate(Long dogId, Date startDate, Date endDate, Time startTime, Time endTime) {

        reservationRepo.cancelReservationByDogIdAndDate(dogId, startDate, endDate, startTime, endTime);
        Set<Reservation> reservationSet = reservationRepo.findAllByDogIdAndDate(dogId, startDate, endDate, startTime, endTime);

        Set<ReservationDTO> reservationDTOSet = new LinkedHashSet<>();

        for (Reservation r : reservationSet){
            reservationDTOSet.add(TransferObjectUtil.getReservationDTO(r));
        }

        return reservationDTOSet;
    }

    @Override
	public List<ReservationDTO> listAll() {
		List<Reservation> all = reservationRepo.findAll();
		List<ReservationDTO> allDTO = new ArrayList<>();
		
		for(Reservation reservation : all) {
			allDTO.add(TransferObjectUtil.getReservationDTO(reservation));
		}
		
		return allDTO;
	}

    @Override
    public Set<ReservationDTO> findAllByAssociationId(Long associationId) {

        Assert.notNull(associationId, "Association ID cannot be null!");
        Association association = associationService.getAssociationById(associationId);
        Assert.notNull(association, "Association with given ID not found!");

        Set<Reservation> reservationSet = reservationRepo.findAllByAssociationId(associationId);

        Set<ReservationDTO> reservationDTOSet = new LinkedHashSet<>();

        for (Reservation r : reservationSet){
            reservationDTOSet.add(TransferObjectUtil.getReservationDTO(r));
        }

        return reservationDTOSet;
    }

    @Override
    public Set<ReservationDTO> findAllActiveByAssociationId(Long associationId) {
        Assert.notNull(associationId, "Association ID cannot be null!");
        Association association = associationService.getAssociationById(associationId);
        Assert.notNull(association, "Association with given ID not found!");

        Date currentDate = Date.valueOf(java.time.LocalDate.now());
        Time currentTime = Time.valueOf(java.time.LocalTime.now());
        Set<Reservation> reservationSet = reservationRepo.findAllActiveByAssociationId(associationId, currentDate, currentTime);

        Set<ReservationDTO> reservationDTOSet = new LinkedHashSet<>();

        for (Reservation r : reservationSet){
            reservationDTOSet.add(TransferObjectUtil.getReservationDTO(r));
        }

        return reservationDTOSet;
    }

    /**
     * Vraca listu pasa koji su dostupni za grupnu šetnju u predanom terminu u predanoj udruzi.
     */
    @Override
    public List<DogIdNameDTO> getDogListByAvailability(DogIdByAvailability dogIdByAvailability) {
        List<Dog> dogs = dogRepo.findAllAssociationDogs(dogIdByAvailability.getAssociationId());
        System.out.println(dogIdByAvailability.getAssociationId());
        System.out.println(dogs.toString());
        List<DogIdNameDTO> list = new ArrayList<>();

        Date dateCheck = dogIdByAvailability.getDate();
        Time startTimeCheck = dogIdByAvailability.getStartTime();
        Time endTimeCheck = dogIdByAvailability.getEndTime();

        for(Dog dog : dogs) {
            if(dog.getPreferredWalkStyle() == WalkStyle.GROUP) {
                Set<DogAvailabilityDTO> dogAvailabilityDTOSet = availabilityService.getDogAvailabilityByDogId(dog.getId());
                for(DogAvailabilityDTO a : dogAvailabilityDTOSet) {
                    if((dateCheck.compareTo(a.getStartDate()) >= 0) && (dateCheck.compareTo(a.getEndDate()) <= 0)) {
                        if((startTimeCheck.compareTo(a.getStartTime()) >= 0) && (endTimeCheck.compareTo(a.getEndTime()) <= 0)) {
                            list.add(new DogIdNameDTO(dog));
                            break;
                        }
                    }
                }
            }
        }

        return list;
    }
}
