package hr.fer.progi.dogGO.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import hr.fer.progi.dogGO.domain.Association;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;

public interface AssociationRepository extends JpaRepository<Association, Long> {

    /**
     * Vraca udrugu s predanim id-ijem. Udruga MOZE biti obrisana.
     */
    Optional<Association> findById(Long Id);

    /**
     * Vraca udrugu koja ima predani oib i NIJE obrisana.
     */
    @Query("SELECT a FROM Association a WHERE a.oib = :oib and a.deleted = false")
    Optional<Association> findByOib(@Param("oib") String oib);

    /**
     * Broji koliko ima NEOBRISANIH udruga.
     */
    @Query("SELECT count(a) FROM Association a WHERE a.deleted = false")
    Long countById();

    /**
     * Broji koliko ima NEOBRISANIH udruga s predanim oibom.
     */
    @Query("SELECT count(a) FROM Association a WHERE a.oib = :oib and a.deleted = false")
    int countByOib(@Param("oib") String oib);

    /**
     * Broji koliko ima NEOBRISANIH udruga s predanim mailom.
     */
    @Query("SELECT count(a) FROM Association a WHERE a.email = :email and a.deleted=false")
    int countByEmail(@Param("email") String email);

    @Transactional
    @Modifying
    @Query("UPDATE Association a set a.deleted = true where a.id = :id")
	void deleteAssociationById(@Param("id") Long associationId);
}
