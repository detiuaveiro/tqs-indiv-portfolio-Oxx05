package pt.ua.tqs.ZeroMonos;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pt.ua.tqs.ZeroMonos.boundary.RESERVATION_STATE;
import pt.ua.tqs.ZeroMonos.data.Municipality;
import pt.ua.tqs.ZeroMonos.data.ZeroMonosBookingRequest;
import pt.ua.tqs.ZeroMonos.data.ZeroMonosRepository;
import pt.ua.tqs.ZeroMonos.service.MunicipalityService;
import pt.ua.tqs.ZeroMonos.service.ZeroMonosService;
import pt.ua.tqs.ZeroMonos.boundary.InvalidDateException;
import pt.ua.tqs.ZeroMonos.boundary.InvalidMunicipalityException;
import pt.ua.tqs.ZeroMonos.boundary.MaxCapacityException;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ScopeBZeroMonosServiceTest {

    private ZeroMonosRepository repository;
    private ZeroMonosService service;
    private final LocalDateTime validDate = TestUtils.nextValidWeekday();

    @BeforeEach
    void setup() {
        repository = mock(ZeroMonosRepository.class);
        MunicipalityService municipalityService = mock(MunicipalityService.class);
        service = new ZeroMonosService(repository, municipalityService);

        when(municipalityService.getMunicipalities())
                .thenReturn(List.of("Anadia", "Aveiro", "Águeda"));
    }

    // --- saveRequest() -------------------------------------------------------

    @Test
    void saveRequest_withPastDate_throwsInvalidDateException() {
        var req = new ZeroMonosBookingRequest(
                LocalDateTime.of(2024, Month.JANUARY, 1, 12, 0),
                "Anadia", "Desc"
        );
        assertThrows(InvalidDateException.class, () -> service.saveRequest(req));
    }

    @Test
    void saveRequest_withInvalidHour_throwsInvalidDateException() {
        var req = new ZeroMonosBookingRequest(
                LocalDateTime.of(2026, Month.SEPTEMBER, 20, 19, 0),
                "Anadia", "Desc"
        );
        assertThrows(InvalidDateException.class, () -> service.saveRequest(req));
    }

    @Test
    void saveRequest_onWeekend_throwsInvalidDateException() {
        var req = new ZeroMonosBookingRequest(
                LocalDateTime.of(2025, Month.DECEMBER, 6, 12, 0),
                "Anadia", "Desc"
        );
        assertThrows(InvalidDateException.class, () -> service.saveRequest(req));
    }

    @Test
    void saveRequest_withInvalidMunicipality_throwsInvalidMunicipalityException() {
        var req = new ZeroMonosBookingRequest(validDate, "MunicipioFalso", "Desc");
        assertThrows(InvalidMunicipalityException.class, () -> service.saveRequest(req));
    }

    @Test
    void saveRequest_withValidData_savesSuccessfully() throws Exception {
        var req = new ZeroMonosBookingRequest(validDate, "Anadia", "Desc");
        when(repository.save(any())).thenReturn(req);

        var result = service.saveRequest(req);

        assertNotNull(result);
        verify(repository, times(1)).save(req);
    }

    @Test
    void saveRequest_withMaxRequests_throwsMaxCapacityException() {
        var req = new ZeroMonosBookingRequest(validDate, "Anadia", "Desc");
        when(repository.save(any())).thenReturn(req);
        when(repository.countByDayAndMunicipality(
                any(LocalDateTime.class),
                any(LocalDateTime.class),
                eq("Anadia")
        )).thenReturn(Municipality.MAX_DAILY_REQUESTS);

        assertThrows(MaxCapacityException.class, () -> service.saveRequest(req));
        verify(repository, times(0)).save(req);
    }

    // --- updateRequest() -----------------------------------------------------

    @Test
    void updateRequest_existingToken_updatesStateAndSaves() {
        var req = new ZeroMonosBookingRequest(validDate, "Anadia", "Desc");
        when(repository.existsZeroMonosRequestByToken(req.getToken())).thenReturn(true);

        service.updateRequest(req);

        verify(repository, times(1)).save(req);
    }

    @Test
    void updateRequest_nonExistingToken_doesNothing() {
        var req = new ZeroMonosBookingRequest(validDate, "Anadia", "Desc");
        when(repository.existsZeroMonosRequestByToken(req.getToken())).thenReturn(false);

        service.updateRequest(req);

        verify(repository, never()).save(any());
    }

    // --- cancelRequest() -----------------------------------------------------

    @Test
    void cancelRequest_existingBooking_cancelsAndSaves() {
        var req = new ZeroMonosBookingRequest(validDate, "Anadia", "Desc");
        when(repository.findByToken(req.getToken())).thenReturn(Optional.of(req));

        service.cancelRequest(req.getToken());

        verify(repository, times(1)).save(req);
        assertSame(RESERVATION_STATE.CANCELLED, req.getState());
    }

    @Test
    void cancelRequest_nonExistingBooking_doesNothing() {
        when(repository.findByToken("fakeToken")).thenReturn(Optional.empty());

        service.cancelRequest("fakeToken");

        verify(repository, never()).save(any());
    }

    // --- Getters -------------------------------------------------------------

    @Test
    void getZeroMonosRequest_returnsOptional() {
        var req = new ZeroMonosBookingRequest(validDate, "Anadia", "Desc");
        when(repository.findByToken("token")).thenReturn(Optional.of(req));

        var result = service.getZeroMonosRequest("token");

        assertTrue(result.isPresent());
        assertEquals(req, result.get());
    }

    @Test
    void getZeroMonosRequests_returnsAll() {
        var mockRequest = mock(ZeroMonosBookingRequest.class);
        when(repository.findAll()).thenReturn(List.of(mockRequest));

        var list = service.getZeroMonosRequests();

        assertEquals(1, list.size());
    }

    @Test
    void getZeroMonosRequestsByMunicipality_returnsList() {
        var municipality = new Municipality("Anadia");
        var expected = List.of(mock(ZeroMonosBookingRequest.class));
        when(repository.findAllByMunicipality(municipality.getName())).thenReturn(expected);

        var list = service.getZeroMonosRequestsByMunicipality(municipality.getName());

        assertEquals(expected, list);
    }

    @Test
    void existsRequest_delegatesToRepository() {
        when(repository.existsZeroMonosRequestByToken("abc")).thenReturn(true);

        assertTrue(service.existsRequest("abc"));
    }
}
