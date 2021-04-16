package hr.fer.progi.dogGO.repository;

import hr.fer.progi.dogGO.domain.Association;
import hr.fer.progi.dogGO.domain.AssociationLogin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AssociationLoginRepository extends JpaRepository<AssociationLogin, Long> {
    int countByUsername(String username);
    Optional<AssociationLogin> findByUsername(String username);
}
