package hr.fer.progi.dogGO.service;

import hr.fer.progi.dogGO.domain.DogAvailability;
import hr.fer.progi.dogGO.rest.dto.DogAvailabilityDTO;
import hr.fer.progi.dogGO.rest.dto.DogIdByAvailability;
import hr.fer.progi.dogGO.rest.dto.DogIdNameDTO;

import java.util.List;
import java.util.Set;

public interface AvailabilityService {

    /**
     * Vraca set svih vremena kada je pas slobodan - dakle kada je rasploživ za preuzimanje u udruzi te nije već rezerviran.
     */
	public Set<DogAvailabilityDTO> getDogAvailabilityByDogId(Long id);


    /**
     * Postavlja novu raspoloživost psa u udruzi
     * @param dogAvailabilityDTO
     * @return
     */
    public DogAvailability setDogAvailability(DogAvailabilityDTO dogAvailabilityDTO);

    /**
     * Briše raspoloživost psa u udruzi
     * @param availabilityId
     * @return
     */
    public DogAvailability deleteDogAvailability(Long availabilityId);

    /**
     * Dohvaća originalnu raspoloživost psa, tj. kada je pas dostupan u skloništu, ne uzimajući u obzir rezervacije, preko id-a raspoloživosti
     * @param availabilityId
     * @return
     */
    public DogAvailability getOriginalDogAvailabilityByID(Long availabilityId);

    /**
     * Dohvaća sve originalne raspoloživosti psa, tj. kada je pas dostupan u skloništu, ne uzimajući u obzir rezervacije
     * @param dogId
     * @return
     */
    public Set<DogAvailability> getOriginalDogAvailabilityByDogId(Long dogId);
}
