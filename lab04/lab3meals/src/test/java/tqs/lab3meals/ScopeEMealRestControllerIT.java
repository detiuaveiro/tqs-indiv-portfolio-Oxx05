package tqs.lab3meals;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.TestPropertySource;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import tqs.lab3meals.data.MealRepository;
import tqs.lab3meals.data.MealsBookingRequest;
import static io.restassured.RestAssured.when;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItems;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

import static io.restassured.RestAssured.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestPropertySource( locations = "/application-integrationtest.properties")
public class ScopeEMealRestControllerIT {

    // will need to use the server port for the invocation url
    @LocalServerPort
    int randomServerPort;

    // a REST client that is test-friendly
    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private MealRepository repository;

    @BeforeEach
    void setUp() {
        MealsBookingRequest meal = new MealsBookingRequest(LocalDateTime.of(2026, Month.JUNE , 20, 13, 0), "alex");
        repository.save(meal);
    }

    @BeforeEach
    void resetDb() {
        repository.deleteAll();
    }


    @Test
    void whenCreateEmployeeWithSuccess() {
        MealsBookingRequest meal = new MealsBookingRequest(LocalDateTime.of(2026, Month.JUNE , 20, 13, 0), "alice");
        restTemplate.postForEntity("/api/meals", meal, MealsBookingRequest.class);
        when().
                get("api/meals/{id}", meal.getToken()).
                then().
                statusCode(200).
                body("userId", equalTo(meal.getUserId()));

        List<MealsBookingRequest> found = repository.findAll();
        assertThat(found).extracting(MealsBookingRequest::getUserId).contains("alice");
    }

    @Test
    void givenEmployees_whenGetEmployees_thenStatus200()  {
        createTestMeal("bob", LocalDateTime.of(2026, Month.JUNE , 20, 13, 0));
        createTestMeal("alex", LocalDateTime.of(2026, Month.JUNE , 20, 13, 0));

        when().
                get("api/meals")
                .then()
                .statusCode(200)
                .body("userId", hasItems("bob", "alex"));

        ResponseEntity<List<MealsBookingRequest>> response = restTemplate
                .exchange("/api/meals", HttpMethod.GET, null, new ParameterizedTypeReference<>() {
                });

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).extracting(MealsBookingRequest::getUserId).containsExactly("bob", "alex");

    }


    private void createTestMeal(String name, LocalDateTime data) {
        MealsBookingRequest meal = new MealsBookingRequest(data, name);
        repository.saveAndFlush(meal);
    }

}
