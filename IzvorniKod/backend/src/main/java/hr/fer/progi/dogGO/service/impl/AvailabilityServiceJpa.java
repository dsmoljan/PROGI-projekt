package hr.fer.progi.dogGO.service.impl;

import hr.fer.progi.dogGO.domain.Dog;
import hr.fer.progi.dogGO.domain.DogAvailability;
import hr.fer.progi.dogGO.domain.Reservation;
import hr.fer.progi.dogGO.repository.DogAvailabilityRepository;
import hr.fer.progi.dogGO.repository.DogRepository;
import hr.fer.progi.dogGO.rest.dto.DogAvailabilityDTO;
import hr.fer.progi.dogGO.rest.dto.ReservationDTO;
import hr.fer.progi.dogGO.service.AvailabilityService;
import hr.fer.progi.dogGO.service.DogService;
import hr.fer.progi.dogGO.service.RequestDeniedException;
import hr.fer.progi.dogGO.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.sql.Date;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AvailabilityServiceJpa implements AvailabilityService {

    @Autowired
    private DogRepository dogRepo;

    @Autowired
    private DogService dogService;

    @Autowired
    private DogAvailabilityRepository dogAvailabilityRepo;

    @Autowired
    private ReservationService reservationService;

    //mislim da bi za ovu metodu trebalo onda dodati da uzima u obzir i rezervacije, jer će po novom u tablici DogAvailibility biti samo
    //termini u kojima je udruga odredila da se pas može preuzeti
    //a onda se te informacije moraju kombinirati s informacijama o rezervacijama da bi se doznalo kada je zaista
    //pas dostupan za šetnju, odnosno da je dostupan u udruzi I da nije rezerviranž
    //također, ovim se jako olakšava provjera je li pas dostupan i na backendu
    //kako je to najbolje napraviti?
    //ideš po entryima iz dogavailibility koje dobiješ iz baze
    //i zatim gledaš za svaki taj termin ima li u tom terminu neka rezervacija
    //ako ima, radiš 2 nova termina tako da zaobiđeš rezervaciju i njih dodaješ u listu
    //ako u tom terminu nema rezervacije, njega normalno dodaješ u listu
    /**
     * Vraca set svih vremena kada je pas slobodan - dakle kada je rasploživ za preuzimanje u udruzi te nije već rezerviran.
     */
    @Override
    public Set<DogAvailabilityDTO> getDogAvailabilityByDogId(Long id) {

        Assert.isTrue(dogRepo.findById(id).isPresent(), "Dog with given ID doesn't exsist!");
        Set<DogAvailability> dogAvailabilities = dogAvailabilityRepo.findAllByDogId(id);

        Set<ReservationDTO> reservations = reservationService.findByDogId(id);

        Set<DogAvailabilityDTO> finalSet = new LinkedHashSet<>();

//        Comparator<ReservationDTO> reservationDTOComparator = Comparator.comparing(ReservationDTO::getDate).thenComparing(ReservationDTO::getStartTime);
//        Comparator<DogAvailability> availabilityDTOComparator = Comparator.comparing(DogAvailability::getStartDate).thenComparing(DogAvailability::getStartTime);
        //provjeri postoji li provjera da se ne smije dodati novi availability u terminu postojećeg

        List<ReservationDTO> reservationList = new LinkedList<>();
        List<DogAvailabilityDTO> availabilityList = new LinkedList<>();

        reservationList.addAll(reservations);

        for (DogAvailability a : dogAvailabilities){
            availabilityList.add(new DogAvailabilityDTO(a.getDog().getId(), a.getStartDate(), a.getEndDate(), a.getStartTime(), a.getEndTime()));
        }

        if (reservationList.size() == 0){
            LinkedHashSet<DogAvailabilityDTO> tmpSet = new LinkedHashSet<>();
            tmpSet.addAll(availabilityList);
            return tmpSet;
        }

        //sortiramo rezervacije da ih možemo obići kao ljudi
        Collections.sort(reservationList);
        Collections.sort(availabilityList);

        //sad idemo po dostupnostima
        int aIndex = 0;
        int rIndex = 0;

        ReservationDTO r = reservationList.get(rIndex);

        for (DogAvailabilityDTO a : availabilityList){
            LinkedList<DogAvailability> tmpList = new LinkedList<>();
            ReservationDTO previous = new ReservationDTO();
            ReservationDTO tmpReservation = new ReservationDTO();

            tmpReservation.setDate(a.getEndDate());
            tmpReservation.setStartTime(a.getEndTime());
            tmpReservation.setReturnTime(a.getEndTime());

            reservationList.add(tmpReservation); // fiktivna

            Collections.sort(reservationList);

            previous.setDate(a.getStartDate());
            previous.setStartTime(a.getStartTime());
            //previous.setReturnTime(r.getStartTime());
            previous.setReturnTime(a.getStartTime()); //u slučaju bugova vratiti na verziju iznad
            while ((a.getEndDate().getTime() > r.getDate().getTime()) || (a.getEndDate().getTime() == r.getDate().getTime() && a.getEndTime().getTime() >= r.getStartTime().getTime())){
                // samo treba uzimati intervale od rezervacije do rezervacije, a ako nema više rezervacija, onda uzeti interval do kraja dostupnosti

                DogAvailabilityDTO beforeReservation1 = null; //termin kada je pas dostupan prije rezervacije
                DogAvailabilityDTO beforeReservation2 = null;
                DogAvailabilityDTO beforeReservation3 = null;  //termin kada je pas dostupan nakon rezervacije
                DogAvailabilityDTO afterReservation2 = null;

                //ako je sljedeća rezervacija sljedeći dan te
                //ako ti je između rezervacije i "zatvaranja skloništa" ostalo vremena
                //to vrijeme mora biti novi availability
                if (previous.getDate().getTime() < r.getDate().getTime() && previous.getReturnTime().getTime() < a.getEndTime().getTime()){
                    beforeReservation1 = new DogAvailabilityDTO();
                    beforeReservation1.setDogID(a.getDogID());
                    beforeReservation1.setStartDate(previous.getDate());
                    beforeReservation1.setEndDate(previous.getDate());
                    beforeReservation1.setStartTime(previous.getReturnTime());
                    beforeReservation1.setEndTime(a.getEndTime());

                    //dodatak još za dan rezervacije
                    if (r.getStartTime().getTime() != a.getStartTime().getTime()){
                        beforeReservation3 = new DogAvailabilityDTO();
                        beforeReservation3.setDogID(a.getDogID());
                        beforeReservation3.setStartDate(r.getDate());
                        beforeReservation3.setEndDate(r.getDate());
                        beforeReservation3.setStartTime(a.getStartTime());
                        beforeReservation3.setEndTime(r.getStartTime());
                    };

//                    previous.setDate(new Date(previous.getDate().getTime() + 86400000));
//                    previous.setStartTime(a.getStartTime());
//                    previous.setReturnTime(a.getEndTime());
                }

                //ako je ostalo slobodnih dana između prijašnje rezervacije i početka ove, to su također nove dostupnosti
                //svi PUNI dani su nove dostupnosti za ovo
                if (previous.getDate().getTime() + 86400000 < r.getDate().getTime()){
                    beforeReservation2 = new DogAvailabilityDTO();
                    beforeReservation2.setDogID(a.getDogID());
                    beforeReservation2.setStartDate(new Date(previous.getDate().getTime() + 86400000));
                    beforeReservation2.setEndDate(new Date(r.getDate().getTime() - 86400000));
                    beforeReservation2.setStartTime(a.getStartTime());
                    beforeReservation2.setEndTime(a.getEndTime());

                    //dodatak još za dan rezervacije
                    if (r.getStartTime().getTime() != a.getStartTime().getTime()){
                        beforeReservation3 = new DogAvailabilityDTO();
                        beforeReservation3.setDogID(a.getDogID());
                        beforeReservation3.setStartDate(r.getDate());
                        beforeReservation3.setEndDate(r.getDate());
                        beforeReservation3.setStartTime(a.getStartTime());
                        beforeReservation3.setEndTime(r.getStartTime());
                    }

                }

                //dostupnosti na isti dan prije rezervacije
                if (previous.getDate().getTime() == r.getDate().getTime() && previous.getReturnTime().getTime() <= r.getStartTime().getTime()){
                    beforeReservation3 = new DogAvailabilityDTO();
                    beforeReservation3.setDogID(a.getDogID());
                    beforeReservation3.setStartDate(previous.getDate());
                    beforeReservation3.setEndDate(previous.getDate());
                    beforeReservation3.setStartTime(previous.getReturnTime());
                    beforeReservation3.setEndTime(r.getStartTime());
                }

                if (beforeReservation1 != null){
                    finalSet.add(beforeReservation1);
                }

                if (beforeReservation2 != null){
                    finalSet.add(beforeReservation2);
                }

                if (beforeReservation3 != null){
                    finalSet.add(beforeReservation3);
                }

                previous = r;
                rIndex++;
                if (rIndex < reservationList.size()){
                    r = reservationList.get(rIndex);
                }else{
                    break;
                }

            }

            //sad kad smo obišli sve rezervacije, treba provjeriti ima li još slobnog vremena od zadnje rezervacije i završetka ovog availabilitya
//            DogAvailabilityDTO beforeReservation1 = null; //termin kada je pas dostupan prije rezervacije
//            DogAvailabilityDTO beforeReservation2 = null;



        }

        /*

        //rezervacija koja počinje prije (na raniji datum) je veća
        //ako imaju isti datum, rezervacija koja počinje vremenski ranije je veća

        Set<DogAvailabilityDTO> tmpSet = new LinkedHashSet<>();

        //probaj listu pa iterirati po listi
        for (DogAvailability a : dogAvailabilities){
            if (reservations.size() == 0){
            	tmpSet.add(new DogAvailabilityDTO(a.getDog().getId(), a.getStartDate(), a.getEndDate(), a.getStartTime(), a.getEndTime()));
            }
            for (ReservationDTO r : reservations){
                //prvo gledamo je li rezervacija unutar ovog termina raspoloživosti
                if ((r.getDate().getTime()>= a.getStartDate().getTime() &&
                        r.getDate().getTime() <= a.getEndDate().getTime())
                        && (r.getStartTime().getTime() > a.getStartTime().getTime() && r.getReturnTime().getTime() < a.getEndTime().getTime())) {

                    //VAŽNO
                    //ovdje dodaj još provjeru nalazi li se rezervacija i u tom vremenskom terminu (hour!)!!!!!!!!!
                    //ako to ne napraviš, dobiti ćeš beskonačnu petlju
                    //dodano

                    //ukoliko je, moramo podijeliti termin raspoloživosti na 2 dijela, tako da ne uključuje rezervaciju
                	DogAvailabilityDTO beforeReservation1 = null; //termin kada je pas dostupan prije rezervacije
                    DogAvailabilityDTO beforeReservation2 = null;
                    DogAvailabilityDTO afterReservation1 = null;  //termin kada je pas dostupan nakon rezervacije
                    DogAvailabilityDTO afterReservation2 = null;


                    //ukoliko pas treba biti dostupan i na datume prije rezervacije
                    if (r.getDate().getTime() > a.getStartDate().getTime()) {
                    	beforeReservation1 = new DogAvailabilityDTO();
                        beforeReservation1.setDogID(a.getDog().getId());
                        beforeReservation1.setStartDate(a.getStartDate());
                        beforeReservation1.setEndDate(new Date(r.getDate().getTime() - 86400000));
                        beforeReservation1.setStartTime(a.getStartTime());
                        beforeReservation1.setEndTime(a.getEndTime());
                    }

                    beforeReservation2 = new DogAvailabilityDTO();
                    beforeReservation2.setDogID(a.getDog().getId());
                    beforeReservation2.setStartDate(r.getDate());
                    beforeReservation2.setEndDate(r.getDate());
                    beforeReservation2.setStartTime(a.getStartTime());
                    beforeReservation2.setEndTime(r.getStartTime());

                    afterReservation1 = new DogAvailabilityDTO();
                    afterReservation1.setDogID(a.getDog().getId());


                    if (r.getDate().getTime() < a.getEndDate().getTime()) { //ako je pas slobodan još dana iza dana rezervacije
                        afterReservation1.setStartDate(r.getDate());
                        afterReservation1.setEndDate(r.getDate());
                        afterReservation1.setStartTime(r.getReturnTime());
                        afterReservation1.setEndTime(a.getEndTime());

                        afterReservation2 = new DogAvailabilityDTO();
                        afterReservation2.setDogID(a.getDog().getId());

                        afterReservation2.setStartDate(new Date(r.getDate().getTime() + 86400000));
                        afterReservation2.setEndDate(a.getEndDate());
                        afterReservation2.setStartTime(a.getStartTime());
                        afterReservation2.setEndTime(a.getEndTime());
                    } else {
                        afterReservation1.setStartDate(r.getDate());
                        afterReservation1.setEndDate(a.getEndDate());
                        afterReservation1.setStartTime(r.getReturnTime());
                        afterReservation1.setEndTime(a.getEndTime());
                    }

                    dogAvailabilities.remove(a);

                    Set<DogAvailabilityDTO> tmpSet2 = new LinkedHashSet<>();
                    tmpSet2.add(beforeReservation1);
                    tmpSet2.add(beforeReservation2);
                    tmpSet2.add(afterReservation1);
                    tmpSet2.add(afterReservation2);


                    for (DogAvailabilityDTO availability : tmpSet2) {
                        boolean timeAlreadyExists = false;

                        if (availability != null) {
//
//                            for (DogAvailability oldAvailability : dogAvailabilities){
//                                if ((availability.getStartDate().getTime() >= oldAvailability.getStartDate().getTime() &&
//                                    availability.getEndDate().getTime() <= oldAvailability.getEndDate().getTime()) &&
//                                    (availability.getStartTime().getTime() >= oldAvailability.getStartTime().getTime() &&
//                                    availability.getEndTime().getTime() <= oldAvailability.getEndTime().getTime())){
//                                        timeAlreadyExsists = true;
//                                        continue;
//                                }
//                            }

                            if (timeAlreadyExists == false) {
                                tmpSet.add(availability);
                            }
                        }
                    }

//                    if (beforeReservation1 != null){
//                        dogAvailabilities.add(beforeReservation1);
//                    }
//
//                    if (beforeReservation2 != null){
//                        dogAvailabilities.add(beforeReservation2);
//                    }
//
//                    if (afterReservation1 != null){
//                        dogAvailabilities.add(afterReservation1);
//                    }
//
//                    if (afterReservation2 != null){
//                        dogAvailabilities.add(afterReservation2);
//                    }

                    //također bi trebao proći i sve ostale availibilitye i provjeriti pokrivaju li oni već termin u koji pokušavam dodati novi availibility
                    //te ako pokrivaju, ne dodavati ga
                } else {
                	tmpSet.add(new DogAvailabilityDTO(a.getDog().getId(), a.getStartDate(), a.getEndDate(), a.getStartTime(), a.getEndTime()));
                }
            }

        }

        //provjeravam nalazi li se a1 unutar termina a2
        //ako se nalazi, iz mape mičem termin a1

        Set<DogAvailabilityDTO> toRemoveSet = new LinkedHashSet<>();

        for (DogAvailabilityDTO a1 : tmpSet){
            for (DogAvailabilityDTO a2 : tmpSet){
                if (toRemoveSet.contains(a2)){
                    continue;
                }
                //tldr ako imamo za isto datumsko razdoblje jedan kraći i jedan duži interval, onda mičemo onaj duži jer je on višak
                //99% da je problem ovdje
                if (a1.equals(a2) == false &&
                        ((a1.getStartDate().getTime() >= a2.getStartDate().getTime() && a1.getEndDate().getTime() <= a2.getEndDate().getTime()) &&
                                (a1.getStartTime().getTime() >= a2.getStartTime().getTime() && a1.getEndTime().getTime() <= a2.getEndTime().getTime()))){
                    toRemoveSet.add(a2);
                }
            }
        }

        //tmpSet.removeAll(toRemoveSet);
        */

        Set<DogAvailabilityDTO> toRemoveSet = new LinkedHashSet<>();

        for (DogAvailabilityDTO a : finalSet){
            if (a.getStartDate().getTime() == a.getEndDate().getTime() && a.getStartTime().getTime() == a.getEndTime().getTime()){
                toRemoveSet.add(a);
            }
        }

        finalSet.removeAll(toRemoveSet);
        return finalSet.stream().filter((dto) -> dto.getEndDate().compareTo(Date.valueOf(LocalDate.now())) >= 0).collect(Collectors.toSet()); //ovo nek bude novi DTO, ne treba ti reservation id, koji je ionako null.
    }

    @Override
    public DogAvailability setDogAvailability(DogAvailabilityDTO dogAvailabilityDTO){

        Assert.isTrue(dogRepo.findById(dogAvailabilityDTO.getDogID()).isPresent(), "Dog with given ID doesn't exsist!");

        Assert.notNull(dogAvailabilityDTO.getStartDate(), "Error - please give a starting date!");
        Assert.notNull(dogAvailabilityDTO.getEndDate(), "Error - please give an ending date!");
        Assert.notNull(dogAvailabilityDTO.getStartTime(), "Error - please give a starting time!");
        Assert.notNull(dogAvailabilityDTO.getEndTime(), "Error - please give an ending time!");

        Dog dog = dogRepo.findById(dogAvailabilityDTO.getDogID()).get();
        System.out.println(dog.getName());

        return dogAvailabilityRepo.save(new DogAvailability(dog, dogAvailabilityDTO.getStartDate(), dogAvailabilityDTO.getEndDate(),
                dogAvailabilityDTO.getStartTime(), dogAvailabilityDTO.getEndTime()));

    }



    @Override
    public DogAvailability deleteDogAvailability(Long availabilityId) {
        Optional<DogAvailability> dogAvailability = dogAvailabilityRepo.findById(availabilityId);

        //potrebno je dodati da u slučaju brisanja neke dostupnosti, da se također izbrišu i sve rezervacije tog psa koje su bile
        //u tom termimnu dostupnosti

        if (dogAvailability.isPresent()){
            DogAvailability dogAvailability1 = dogAvailability.get();
            dogAvailabilityRepo.deleteDogAvailabilityById(availabilityId);
            reservationService.cancelReservationByDogIdAndDate(dogAvailability1.getDog().getId(), dogAvailability1.getStartDate(), dogAvailability1.getEndDate(),
                    dogAvailability1.getStartTime(), dogAvailability1.getEndTime());
            return dogAvailability.get();
        }else{
            throw new RequestDeniedException("Dog availiability entry with given ID not found");
        }

    }

    @Override
    public DogAvailability getOriginalDogAvailabilityByID(Long availabilityId) {
        Optional<DogAvailability> dogAvailability = dogAvailabilityRepo.findById(availabilityId);

        if (dogAvailability.isPresent()){
            return dogAvailability.get();
        }else{
            throw new RequestDeniedException("Dog availability with given ID not found");
        }
    }

    @Override
    public Set<DogAvailability> getOriginalDogAvailabilityByDogId(Long dogId) {
        return dogAvailabilityRepo.findAllByDogId(dogId);
    }
}
