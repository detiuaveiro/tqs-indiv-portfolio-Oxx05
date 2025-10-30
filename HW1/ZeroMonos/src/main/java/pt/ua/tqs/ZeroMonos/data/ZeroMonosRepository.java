package pt.ua.tqs.ZeroMonos.data;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ZeroMonosRepository extends JpaRepository<ZeroMonosBookingRequest, String> {

    //put e post
    public Optional<ZeroMonosBookingRequest> findByToken(String token);
    public List<ZeroMonosBookingRequest> findAll();
    public boolean existsMealsBookingRequestByToken(String token);
    public void deleteByToken(String token);

}
