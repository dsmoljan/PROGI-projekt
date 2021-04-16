package hr.fer.progi.dogGO.repository;

import hr.fer.progi.dogGO.domain.WalkerLogin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WalkerLoginRepository extends JpaRepository<WalkerLogin, Long> {
    int countByUsername(String username);
    Optional<WalkerLogin> findByUsername(String username);
    Optional<WalkerLogin> findById(Long id);
}
