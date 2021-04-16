package hr.fer.progi.dogGO.repository;

import hr.fer.progi.dogGO.domain.Dog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface DogRepository extends JpaRepository<Dog, Long> {

    /**
     * Vraca psa koji odgovara predanom id-u i NIJE obrisan.
     */
    @Query("SELECT d FROM Dog d WHERE d.id = :id and d.deleted = false")
    Optional<Dog> findById(@Param("id") Long id);

    /**@Query("SELECT d FROM Dog d WHERE d.association.oib = :oib and d.name = :name and d.deleted = false")
    Optional<Dog> findByNameAndAssociationOIB(@Param("name") String name, @Param("oib") String associationOIB);**/

    /**
     * Vraca listu NEOBRISANIH pasa koji pripada udruzi s predanim id-em.
     */
    @Query("SELECT d FROM Dog d WHERE d.association.id = :id and d.deleted = false")
    List<Dog> findAllAssociationDogs(@Param("id") Long associationId);

    /**
     * Vraca broj NEOBRISANIH pasa.
     */
    @Query("SELECT count(d) FROM Dog d where d.deleted = false")
    Long countById();

    /**
     * Vraca broj NEOBRISANIH pasa koji pripada udruzi s predanim id-em.
     */
    @Query("SELECT count(d) FROM Dog d where d.association.id = :id and d.deleted = false")
    long findNoOfDogsInAssociation(@Param("id") Long associationId);

    @Transactional
    @Modifying
    @Query("UPDATE Dog d set d.deleted = true where d.id = :id")
    void deleteDogById(@Param("id") Long dogId);
}
