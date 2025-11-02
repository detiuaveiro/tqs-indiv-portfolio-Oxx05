package pt.ua.tqs.ZeroMonos.data;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ZeroMonosRepository extends JpaRepository<ZeroMonosBookingRequest, String> {

    //put e post
    public Optional<ZeroMonosBookingRequest> findByToken(String token);
    public List<ZeroMonosBookingRequest> findAllByMunicipality(String municipality);
    public List<ZeroMonosBookingRequest> findAll();
    public boolean existsZeroMonosRequestByToken(String token);
    @Query("SELECT COUNT(r) FROM ZeroMonosBookingRequest r " +
            "WHERE r.date BETWEEN :startOfDay AND :endOfDay AND r.municipality = :municipality AND r.state <> 'CANCELLED'")
    int countByDayAndMunicipality(@Param("startOfDay") LocalDateTime startOfDay,
                                  @Param("endOfDay") LocalDateTime endOfDay,
                                  @Param("municipality") String municipality);


}
