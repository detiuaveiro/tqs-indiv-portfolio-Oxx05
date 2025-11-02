package pt.ua.tqs.ZeroMonos;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import pt.ua.tqs.ZeroMonos.data.ZeroMonosBookingRequest;
import pt.ua.tqs.ZeroMonos.data.ZeroMonosRepository;

import java.time.LocalDateTime;
import java.util.List;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "/application-integrationtest.properties")
public class ScopeEZeroMonosRestControllerIT {

    @LocalServerPort
    int randomServerPort;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ZeroMonosRepository repository;

    // Data válida única para todos os testes
    private final LocalDateTime validDate = TestUtils.nextValidWeekday();

    @BeforeEach
    void setUp() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = randomServerPort;
        repository.deleteAll();
    }

    @Test
    void whenCreateRequest_thenItIsPersistedAndRetrievable() {
        var req = new ZeroMonosBookingRequest(validDate, "Anadia", "Frigorífico de 200kg");

        // Cria via POST
        restTemplate.postForEntity("/api/requests", req, ZeroMonosBookingRequest.class);

        // Verifica via GET com RestAssured
        when()
                .get("/api/requests/{token}", req.getToken())
                .then()
                .statusCode(200)
                .body("municipality", equalTo("Anadia"))
                .body("description", equalTo("Frigorífico de 200kg"));

        // Verifica no repositório
        List<ZeroMonosBookingRequest> found = repository.findAll();
        assertThat(found)
                .extracting(ZeroMonosBookingRequest::getMunicipality)
                .contains("Anadia");
    }

    @Test
    void givenRequests_whenGetAll_thenStatus200AndListReturned() {
        createTestRequest("Anadia", "Máquina de lavar");
        createTestRequest("Mortágua", "Sofá antigo");

        // Verifica via RestAssured
        when()
                .get("/api/staff/requests")
                .then()
                .statusCode(200)
                .body("municipality", hasItems("Anadia", "Mortágua"));

        // Verifica via TestRestTemplate
        ResponseEntity<List<ZeroMonosBookingRequest>> response = restTemplate.exchange(
                "/api/staff/requests",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {}
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody())
                .extracting(ZeroMonosBookingRequest::getMunicipality)
                .containsExactlyInAnyOrder("Anadia", "Mortágua");
    }

    private void createTestRequest(String municipality, String description) {
        var req = new ZeroMonosBookingRequest(validDate, municipality, description);
        repository.saveAndFlush(req);
    }
}