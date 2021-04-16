package hr.fer.progi.dogGO.repository;

import hr.fer.progi.dogGO.domain.Walker;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface WalkerRepository extends JpaRepository<Walker, Long> {

    /**
     * Broji koliko ima NEOBRISANIH setaca s predanim mailom.
     */
    @Query("SELECT count(w) FROM Walker w WHERE w.email = :email and w.deleted=false")
    int countByEmail(@Param("email") String email);
    
    /**
     * Lista sve NEOBRISANE setace.
     */
    @Query("SELECT w FROM Walker w WHERE w.deleted = false")
    List<Walker> findAll();
    
    /**
     * Vraca sve NEOBRISANE setace s JAVNIM statistikama.
     * @return
     */
    @Query("SELECT w FROM Walker w WHERE w.deleted = false AND w.publicStats = true")
    List<Walker> findAllPublic();
    /**
     * Vraca NEOBRISANOG setaca po id-u.
     */
    @Query("SELECT w FROM Walker w WHERE w.id = :id and w.deleted = false")
    Optional<Walker> findById(@Param("id") Long id);

    /**
     * Vraca broj NEOBRISANIH setaca.
     */
    @Query("SELECT count(w) FROM Walker w where w.deleted = false")
    long countById();

    @Transactional
    @Modifying
    @Query("UPDATE Walker w set w.deleted = true where w.id = :id")
    void deleteWalkerById(@Param("id") Long walkerId);
    
}
