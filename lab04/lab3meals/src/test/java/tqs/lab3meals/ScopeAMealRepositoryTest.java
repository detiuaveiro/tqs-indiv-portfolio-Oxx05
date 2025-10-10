package tqs.lab3meals;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import tqs.lab3meals.data.MealRepository;
import tqs.lab3meals.data.MealsBookingRequest;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;


@DataJpaTest
public class ScopeAMealRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private MealRepository repository;

    @Test
    void whenFindRes1ByName_thenReturnRes1_Meal() {
        var req = new MealsBookingRequest(LocalDateTime.of(2026, Month.SEPTEMBER, 20, 12, 0), "user");
        MealsBookingRequest meal1 = entityManager.persistFlushFind(req);

        // test the query method of interest
        Optional<MealsBookingRequest> found = repository.findByToken(req.getToken());
        assertThat(found).isNotEmpty().get().isEqualTo(meal1);
    }


    @Test
    void whenInvalidMealToken_thenReturnNull() {
        var req = new MealsBookingRequest(LocalDateTime.of(2026, Month.SEPTEMBER, 20, 12, 0), "user");
        MealsBookingRequest meal1 = entityManager.persistFlushFind(req);

        Optional<MealsBookingRequest> fromDb = repository.findByToken("Not token");
        assertThat(fromDb).isEmpty();
    }


    @Test
    void givenSetOfEmployees_whenFindAll_thenReturnAllEmployees() {
        LocalDateTime dateTime = LocalDateTime.of(2026, Month.SEPTEMBER, 18, 12, 0);

        for (int i = 0; i < 3; i++) {
            MealsBookingRequest req = new MealsBookingRequest(dateTime, "user" + i);
            entityManager.persist(req);
        }

        entityManager.flush();


        List<MealsBookingRequest> allEmployees = repository.findAll();
        assertThat(allEmployees).hasSize(3).extracting(MealsBookingRequest::getUserId).containsOnly("user0", "user1", "user2");
    }


    @DisplayName("Should find employees whose email ends with a specific domain")
    @Test
    void testFindEmplyeedByOrganizationDomain() {
        LocalDateTime dateTime = LocalDateTime.of(2026, Month.SEPTEMBER, 18, 12, 0);

        // Given
        String token = "res_1";
        for (int i = 0; i < 3; i++) {
            MealsBookingRequest req = new MealsBookingRequest(dateTime, "user" + i);
            if (i == 1) {
                token = req.getToken();
            }
            entityManager.persist(req);
        }

        entityManager.flush();

        // When
        Optional<MealsBookingRequest> results = repository.findByToken(token);

        // Then
        assertThat(results)
                .isPresent()
                .get()
                .extracting(MealsBookingRequest::getUserId)
                .isEqualTo("user1");


    }



}
