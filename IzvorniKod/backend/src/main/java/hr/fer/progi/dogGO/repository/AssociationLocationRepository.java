package hr.fer.progi.dogGO.repository;

import hr.fer.progi.dogGO.domain.AssociationLocation;
import hr.fer.progi.dogGO.domain.AssociationLogin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AssociationLocationRepository extends JpaRepository<AssociationLocation, Long> {

    /**
     * Vraca lokaciju udruge s predanim id-em.
     */
    Optional<AssociationLocation> findById(Long id);
}