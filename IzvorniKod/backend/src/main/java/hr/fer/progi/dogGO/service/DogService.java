package hr.fer.progi.dogGO.service;

import hr.fer.progi.dogGO.domain.Dog;
import hr.fer.progi.dogGO.domain.DogAvailability;
import hr.fer.progi.dogGO.rest.dto.DogDetails;
import hr.fer.progi.dogGO.rest.dto.DogAdd;
import hr.fer.progi.dogGO.rest.dto.DogEdit;

import java.util.List;
import java.util.Set;

public interface DogService {

    /**
     * Vraća listu svih pasa koji sudjeluju u programu, neovisno o udruzi
     *
     * @return
     */
    List<DogDetails> listAll();

    /**
     * Metoda prima ID udruge i vraća set svih pasa te udruge koji sudjeluju u programu
     */
    List<DogDetails> allAssociationDogsList(Long id);

    /**
     * Vraca psa preko njegovog id-a.
     *
     */
    DogDetails getDogById(Long Id);

    /**
     * Vraca listu vremena dosputnosti psa prema predanom id-u.
     */
    List<DogAvailability> getDogAvailabilityById(Long id);

    /**
     * Dodaje novog psa u bazu
     *
     * @return
     */
    DogDetails createDog(DogAdd dog);

    /**
     * Mijenja podatke o psu u bazi na osnovu dobivenog objekta klase Dog. Ukoliko su neki elementi predanog objekta null, to znači
     * da postojeće vrijednosti u bazi ostaju iste.
     *
     * @param
     * @return novi objekt tipa Dog, sa izmijenjenim članskim varijablama
     */
    DogDetails editDog(DogEdit dogEdit);

    /**
     * Vraca broj svih NEOBRISANIH pasa koji sudjeluju u programu.
     */
    Long getNumberOfAllDogs();

    /**
     * Vraća broj pasa o kojima se neka udruga trenutno skrbi
     *
     * @param associationId Id udruge
     * @return
     */
    long noOfDogsInAssociation(Long associationId);

    /**
     * Brise psa sa zadanim ID-om iz baze, na način da vrijednost atributa deleted postavi na true
     *
     * @param dogID
     * @return
     */
    DogDetails deleteDog(Long dogID);


}
