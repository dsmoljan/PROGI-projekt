package hr.fer.progi.dogGO.repository;

import hr.fer.progi.dogGO.domain.DogAvailability;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;

public interface DogAvailabilityRepository extends JpaRepository<DogAvailability, Long> {

    @Query("SELECT a FROM DogAvailability a WHERE a.dog.id = :id and a.deleted = false")
    Set<DogAvailability> findAllByDogId(@Param("id") Long dogID);

    @Query("SELECT a FROM DogAvailability a WHERE a.id = :id and a.deleted = false")
    Optional<DogAvailability> findById(@Param("id") Long availabilityId);

    @Transactional
    @Modifying
    @Query("UPDATE DogAvailability a set a.deleted = true where a.id = :id")
    void deleteDogAvailabilityById(@Param("id") Long availabilityId);

}
