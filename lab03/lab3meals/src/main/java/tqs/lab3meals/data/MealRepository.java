package tqs.lab3meals.data;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MealRepository extends JpaRepository<MealsBookingRequest, String> {

    //put e post
    public Optional<MealsBookingRequest> findByToken(String token);
    public boolean existsMealsBookingRequestByToken(String token);
    public void deleteByToken(String token);

}
