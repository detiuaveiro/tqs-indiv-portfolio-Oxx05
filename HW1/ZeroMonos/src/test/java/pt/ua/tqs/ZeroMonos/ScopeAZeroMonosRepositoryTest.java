package pt.ua.tqs.ZeroMonos;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import pt.ua.tqs.ZeroMonos.data.ZeroMonosRepository;
import pt.ua.tqs.ZeroMonos.data.ZeroMonosBookingRequest;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class ScopeAZeroMonosRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ZeroMonosRepository repository;

    private final LocalDateTime validDate = TestUtils.nextValidWeekday();

    @Test
    void whenFindResByName_thenReturnRes() {
        var req = new ZeroMonosBookingRequest(validDate, "Abrantes", "Frigorifico de 200kg");
        ZeroMonosBookingRequest persisted = entityManager.persistFlushFind(req);

        Optional<ZeroMonosBookingRequest> found = repository.findByToken(req.getToken());

        assertThat(found).isPresent().get().isEqualTo(persisted);
    }

    @Test
    void whenInvalidReqToken_thenReturnNull() {
        var req = new ZeroMonosBookingRequest(validDate, "Abrantes", "Frigorifico de 200kg");
        entityManager.persistFlushFind(req);

        Optional<ZeroMonosBookingRequest> fromDb = repository.findByToken("Not token");

        assertThat(fromDb).isEmpty();
    }

    @Test
    void givenSetOfRequests_whenFindAll_thenReturnAllRequest() {
        List<String> municipalities = List.of("Abrantes", "Mortágua", "Anadia");
        List<String> descriptions = List.of("Frigorifico de 200kg", "Sofá de 3 lugares", "Cama de casal");

        for (int i = 0; i < 3; i++) {
            ZeroMonosBookingRequest req = new ZeroMonosBookingRequest(
                    validDate, municipalities.get(i), descriptions.get(i)
            );
            entityManager.persist(req);
        }
        entityManager.flush();

        List<ZeroMonosBookingRequest> requests = repository.findAll();

        assertThat(requests)
                .hasSize(3)
                .extracting(ZeroMonosBookingRequest::getMunicipality)
                .containsExactlyInAnyOrder("Abrantes", "Mortágua", "Anadia");

        assertThat(requests)
                .extracting(ZeroMonosBookingRequest::getDescription)
                .containsExactlyInAnyOrder("Frigorifico de 200kg", "Sofá de 3 lugares", "Cama de casal");
    }

    @DisplayName("Should find requests by municipality")
    @Test
    void testFindRequestByMunicipality() {
        // Este teste usa uma data fixa (18 Set 2026, 12:00) — mantemos como está
        LocalDateTime fixedDate = LocalDateTime.of(2026, Month.SEPTEMBER, 18, 12, 0);

        for (int i = 0; i < 3; i++) {
            ZeroMonosBookingRequest req = new ZeroMonosBookingRequest(
                    fixedDate,
                    "Municipality" + (i % 2),
                    "Description" + i
            );
            entityManager.persist(req);
        }
        entityManager.flush();

        List<ZeroMonosBookingRequest> results = repository.findAllByMunicipality("Municipality1");

        assertThat(results)
                .hasSize(1)
                .first()
                .extracting(ZeroMonosBookingRequest::getDescription)
                .isEqualTo("Description1");
    }
}