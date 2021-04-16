package hr.fer.progi.dogGO.repository;

import hr.fer.progi.dogGO.domain.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.sql.Time;
import java.util.Optional;
import java.util.Set;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    @Query("SELECT r FROM Reservation r JOIN FETCH r.dogs l where l.id = :id and r.cancelled = false")
    Set<Reservation> findAllByDogId(@Param("id") Long dogId);

    @Query("SELECT r FROM Reservation r where r.walker.id = :id and r.cancelled = false")
    Set<Reservation> findAllByWalkerId(@Param("id") Long walkerId);

    @Query("SELECT r FROM Reservation r JOIN FETCH r.dogs d where d.association.id = :id")
    Set<Reservation> findAllByAssociationId(@Param("id") Long associationId);

    @Query("SELECT r FROM Reservation r JOIN FETCH r.dogs d where d.association.id = :id and r.cancelled = false and r.date >= :current_date and r.startTime >= :current_time")
    Set<Reservation> findAllActiveByAssociationId(@Param("id") Long associationId, @Param("current_date") Date currentDate, @Param("current_time") Time currentTime);

    @Transactional
    @Modifying
    @Query("UPDATE Reservation r set r.cancelled = true where r.reservationId = :id")
    void cancelReservationById(@Param("id") Long reservationId);

    @Transactional
    @Modifying
    @Query("UPDATE Reservation r set r.cancelled = true where r.reservationId in (SELECT r1.reservationId FROM Reservation r1 JOIN r1.dogs d WHERE d.id = :id and r1.date >= :start_date and " +
            "r1.date <= :end_date and r1.startTime >= :start_time and r1.returnTime <= :end_time)")
    void cancelReservationByDogIdAndDate(@Param("id") Long dogId, @Param("start_date") Date startDate, @Param("end_date") Date endDate,
                                         @Param("start_time") Time startTime, @Param("end_time") Time endTime);

    @Query("SELECT r FROM Reservation r JOIN FETCH r.dogs d where d.id = :id and r.date >= :start_date and r.date <= :end_date and " +
            "r.startTime >= :start_time and r.returnTime <= :end_time")
    Set<Reservation> findAllByDogIdAndDate(@Param("id") Long dogId, @Param("start_date") Date startDate, @Param("end_date") Date endDate,
                                        @Param("start_time") Time startTime, @Param("end_time") Time endTime);

    @Query("SELECT r FROM Reservation r WHERE r.reservationId = :id and r.cancelled = false")
    Optional<Reservation> findById(@Param("id") Long reservationId);

}
