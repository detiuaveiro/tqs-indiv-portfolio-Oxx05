package pt.ua.tqs.ZeroMonos;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pt.ua.tqs.ZeroMonos.boundary.InvalidDateException;
import pt.ua.tqs.ZeroMonos.boundary.InvalidMunicipalityException;
import pt.ua.tqs.ZeroMonos.boundary.MaxCapacityException;
import pt.ua.tqs.ZeroMonos.data.ZeroMonosBookingRequest;
import pt.ua.tqs.ZeroMonos.data.ZeroMonosRepository;
import pt.ua.tqs.ZeroMonos.service.MunicipalityService;
import pt.ua.tqs.ZeroMonos.service.ZeroMonosService;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ServicePerformanceTest {

    private ZeroMonosRepository repository;
    private MunicipalityService municipalityService;
    private ZeroMonosService service;

    @BeforeEach
    void setup() {
        repository = mock(ZeroMonosRepository.class);
        municipalityService = mock(MunicipalityService.class);
        service = new ZeroMonosService(repository, municipalityService);

        // sempre retorna 0 → ignora limite diário
        when(repository.countByDayAndMunicipality(any(), any(), eq("Anadia"))).thenReturn(0);
        when(repository.countByDayAndMunicipality(any(), any(), eq("Aveiro"))).thenReturn(0);
        when(repository.countByDayAndMunicipality(any(), any(), eq("Águeda"))).thenReturn(0);

        // retorna o próprio pedido quando salva
        when(repository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        // retorna sempre a lista de municípios válidos
        when(municipalityService.getMunicipalities())
                .thenReturn(java.util.List.of("Anadia", "Aveiro", "Águeda"));
    }

    @Test
    void performance_saveRequest_1000Requests() throws InvalidDateException, InvalidMunicipalityException, MaxCapacityException {
        LocalDateTime baseDate = LocalDateTime.of(2030, 1, 2, 10, 0); // Sexta-feira, 10h

        long start = System.currentTimeMillis();

        for (int i = 0; i < 1000; i++) {
            ZeroMonosBookingRequest req = new ZeroMonosBookingRequest(
                    baseDate.plusMinutes(i),
                    "Anadia",
                    "desc " + i
            );
            try {
                service.saveRequest(req);
            } catch (InvalidDateException | InvalidMunicipalityException | MaxCapacityException e) {
                // apenas regista ou ignora, não falha o teste
            }
        }

        long duration = System.currentTimeMillis() - start;
        System.out.println("Saved 1000 requests in " + duration + " ms");

        assertTrue(duration < 5000, "Performance degraded for 1000 requests!");
    }

    @Test
    void performance_saveRequest_withSomeInvalidRequests()
            throws InvalidDateException, InvalidMunicipalityException, MaxCapacityException {

        LocalDateTime baseDate = LocalDateTime.of(2030, 1, 2, 10, 0); // Sexta-feira, 10h
        int totalRequests = 1000;

        long start = System.currentTimeMillis();

        for (int i = 0; i < totalRequests; i++) {
            ZeroMonosBookingRequest req = new ZeroMonosBookingRequest(
                    baseDate.plusMinutes(i),
                    "Anadia",
                    "desc " + i
            );

            try {
                if (i % 100 == 0) { // simula 1 pedido inválido a cada 100
                    throw new InvalidDateException("Data inválida simulada");
                }
                service.saveRequest(req);
            } catch (InvalidDateException | InvalidMunicipalityException | MaxCapacityException e) {
                // apenas regista, não falha o teste
            }
        }

        long duration = System.currentTimeMillis() - start;
        System.out.println("Processed 1000 requests (with some invalid) in " + duration + " ms");
        assertTrue(duration < 7000, "Performance degraded with some invalid requests!");
    }

    @Test
    void performance_saveRequest_multipleMunicipalities_1000Requests(){

        LocalDateTime baseDate = LocalDateTime.of(2030, 1, 2, 10, 0); // Sexta-feira, 10h
        String[] municipalities = {"Anadia", "Aveiro", "Águeda"};

        long start = System.currentTimeMillis();

        for (int i = 0; i < 1000; i++) {
            ZeroMonosBookingRequest req = new ZeroMonosBookingRequest(
                    baseDate.plusMinutes(i),
                    municipalities[i % municipalities.length], // alterna municípios
                    "desc " + i
            );
            try {
                service.saveRequest(req);
            } catch (InvalidDateException | InvalidMunicipalityException | MaxCapacityException e) {
                // apenas regista ou ignora, não falha o teste
            }        }

        long duration = System.currentTimeMillis() - start;
        System.out.println("Saved 1000 requests across multiple municipalities in " + duration + " ms");
        assertTrue(duration < 6000, "Performance degraded for multiple municipalities!");
    }
}
