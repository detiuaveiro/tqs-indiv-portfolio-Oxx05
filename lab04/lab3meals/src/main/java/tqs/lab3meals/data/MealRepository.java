package tqs.lab3meals.data;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface MealRepository extends JpaRepository<MealsBookingRequest, String> {

    //put e post
    public Optional<MealsBookingRequest> findByToken(String token);
    public List<MealsBookingRequest> findAll();
    public boolean existsMealsBookingRequestByToken(String token);
    public void deleteByToken(String token);

    @Query("SELECT COUNT(m) FROM MealsBookingRequest m WHERE DATE(m.date) = :date AND HOUR(m.date) >= :startHour AND HOUR(m.date) < :endHour")
    int countByDateAndHourInterval(@Param("date") java.time.LocalDate date, @Param("startHour") int startHour, @Param("endHour") int endHour);


}
