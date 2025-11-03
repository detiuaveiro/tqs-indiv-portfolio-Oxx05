package pt.ua.tqs.ZeroMonos;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import pt.ua.tqs.ZeroMonos.boundary.InvalidDateException;
import pt.ua.tqs.ZeroMonos.boundary.InvalidMunicipalityException;
import pt.ua.tqs.ZeroMonos.boundary.MaxCapacityException;
import pt.ua.tqs.ZeroMonos.boundary.ZeroMonosRestController;
import pt.ua.tqs.ZeroMonos.data.Municipality;
import pt.ua.tqs.ZeroMonos.data.ZeroMonosBookingRequest;
import pt.ua.tqs.ZeroMonos.service.MunicipalityService;
import pt.ua.tqs.ZeroMonos.service.ZeroMonosService;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ZeroMonosRestController.class)
@Import(ZeroMonosApplication.class)
public class ScopeCRequestControllerWithMockServiceTest {

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private ZeroMonosService service;

    @MockitoBean
    private MunicipalityService municipalityService;

    // Data válida única para todos os testes
    private final LocalDateTime validDate = TestUtils.nextValidWeekday();

    @BeforeEach
    void setupMocks() {
        when(municipalityService.getMunicipalities())
                .thenReturn(List.of("Aveiro", "Anadia", "Águeda"));
    }



    // ---- POST /api/requests ----
    @Test
    void whenValidRequest_thenCreateSuccessfully() throws Exception {
        var req = new ZeroMonosBookingRequest(validDate, "Aveiro", "desc");
        when(service.saveRequest(any())).thenReturn(req);

        mvc.perform(post("/api/requests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtils.toJson(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token", is(req.getToken())));

        verify(service, times(1)).saveRequest(any());
    }

    @Test
    void whenInvalidMunicipality_thenReturnBadRequest() throws Exception {
        var req = new ZeroMonosBookingRequest(validDate, "Unknown", "desc");
        when(service.saveRequest(any())).thenThrow(new InvalidMunicipalityException("Municipio inválido"));

        mvc.perform(post("/api/requests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtils.toJson(req)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("Municipio inválido")));

        verify(service, times(1)).saveRequest(any());
    }

    @Test
    void whenMaxCapacity_thenReturnTooManyRequests() throws Exception {
        var req = new ZeroMonosBookingRequest(validDate, "Aveiro", "desc");
        when(service.saveRequest(any())).thenThrow(new MaxCapacityException("Capacidade máxima atingida"));

        mvc.perform(post("/api/requests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtils.toJson(req)))
                .andExpect(status().isTooManyRequests())
                .andExpect(jsonPath("$.error", is("Capacidade máxima atingida")));

        verify(service, times(1)).saveRequest(any());
    }

    @Test
    void whenInvalidRequest_thenReturnBadRequest() throws Exception {
        var req = new ZeroMonosBookingRequest(validDate, "Aveiro", "desc");
        when(service.saveRequest(any())).thenThrow(new InvalidDateException("Data inválida"));

        mvc.perform(post("/api/requests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtils.toJson(req)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("Data inválida")));
    }

    // ---- GET /api/requests/{id} ----
    @Test
    void whenValidToken_thenReturnRequest() throws Exception {
        var req = new ZeroMonosBookingRequest(validDate, "Aveiro", "desc");
        when(service.getZeroMonosRequest(req.getToken())).thenReturn(Optional.of(req));

        mvc.perform(get("/api/requests/" + req.getToken()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.municipality", is(req.getMunicipality())));
    }

    @Test
    void whenInvalidToken_thenThrowNotFound() throws Exception {
        when(service.getZeroMonosRequest("fake")).thenReturn(Optional.empty());

        mvc.perform(get("/api/requests/fake"))
                .andExpect(status().isNotFound());
    }

    // ---- DELETE /api/requests/{token} ----
    @Test
    void whenDeleteExistingRequest_thenOk() throws Exception {
        var req = new ZeroMonosBookingRequest(validDate, "Aveiro", "desc");
        when(service.getZeroMonosRequest(req.getToken())).thenReturn(Optional.of(req));

        mvc.perform(delete("/api/requests/" + req.getToken()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.municipality", is("Aveiro")));

        verify(service, times(1)).cancelRequest(req.getToken());
    }

    // ---- PUT /api/requests ----
    @Test
    void whenUpdateRequest_thenOk() throws Exception {
        var req = new ZeroMonosBookingRequest(validDate, "Aveiro", "desc");

        mvc.perform(put("/api/requests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtils.toJson(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.municipality", is("Aveiro")));

        verify(service, times(1)).updateRequest(any());
    }

    // ---- GET /api/municipalities ----
    @Test
    void whenGetMunicipalities_thenReturnList() throws Exception {
        List<String> municipalities = List.of("Aveiro", "Anadia", "Águeda");
        when(municipalityService.getMunicipalities()).thenReturn(municipalities);

        mvc.perform(get("/api/municipalities"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0]", is("Aveiro")));
    }

    // ---- POST /api/municipalities/update ----
    @Test
    void whenForceMunicipalitiesUpdate_thenReturnOk() throws Exception {
        mvc.perform(post("/api/municipalities/update"))
                .andExpect(status().isOk())
                .andExpect(content().string("Municipalities list updated manually."));

        verify(municipalityService, times(1)).fetchMunicipalities();
    }

    // ---- GET /api/staff/requests ----
    @Test
    void whenStaffRequests_thenReturnAll() throws Exception {
        var req1 = new ZeroMonosBookingRequest(validDate, "Aveiro", "desc1");
        var req2 = new ZeroMonosBookingRequest(validDate.plusDays(1), "Anadia", "desc2");

        when(service.getZeroMonosRequests()).thenReturn(Arrays.asList(req1, req2));

        mvc.perform(get("/api/staff/requests"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    // ---- GET /api/staff/requests/{municipality} ----
    @Test
    void whenStaffRequestsByMunicipality_thenReturnFiltered() throws Exception {
        Municipality m = new Municipality("Aveiro");
        var req = new ZeroMonosBookingRequest(validDate, "Aveiro", "desc");

        when(service.getZeroMonosRequestsByMunicipality(m.getName())).thenReturn(List.of(req));

        mvc.perform(get("/api/staff/requests/Aveiro"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].municipality", is("Aveiro")));
    }

    // ---- PUT /api/staff/requests/{id} ----
    @Test
    void whenUpdateBookingStatus_thenOk() throws Exception {
        var req = new ZeroMonosBookingRequest(validDate, "Aveiro", "desc");
        when(service.getZeroMonosRequest(req.getToken())).thenReturn(Optional.of(req));

        mvc.perform(put("/api/staff/requests/" + req.getToken()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.municipality", is("Aveiro")));

        verify(service, times(1)).updateRequest(req);
    }

    //---- Performance Tests ----
    @Test
    void performance_saveRequest_1000Requests() throws Exception {
        LocalDateTime baseDate = LocalDateTime.of(2030, 1, 2, 10, 0);

        when(service.saveRequest(any())).thenAnswer(invocation -> invocation.getArgument(0));

        long start = System.currentTimeMillis();
        for (int i = 0; i < 1000; i++) {
            ZeroMonosBookingRequest req = new ZeroMonosBookingRequest(
                    baseDate.plusMinutes(i),
                    "Anadia",
                    "desc " + i
            );
            mvc.perform(post("/api/requests")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(JsonUtils.toJson(req)))
                    .andExpect(status().isOk());
        }
        long duration = System.currentTimeMillis() - start;
        System.out.println("Saved 1000 requests in " + duration + " ms");
        assert (duration < 5000);
    }


    @Test
    void performance_saveRequest_withSomeInvalidRequests() throws Exception {
        LocalDateTime baseDate = LocalDateTime.of(2030, 1, 2, 10, 0);

        when(service.saveRequest(any()))
                .thenAnswer(invocation -> {
                    ZeroMonosBookingRequest req = invocation.getArgument(0);
                    if (req.getDescription().endsWith("0")) {
                        throw new InvalidDateException("Data inválida simulada");
                    }
                    return req;
                });

        long start = System.currentTimeMillis();
        for (int i = 0; i < 1000; i++) {
            ZeroMonosBookingRequest req = new ZeroMonosBookingRequest(
                    baseDate.plusMinutes(i),
                    "Anadia",
                    "desc " + i
            );

            var resultActions = mvc.perform(post("/api/requests")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(JsonUtils.toJson(req)));

            if (req.getDescription().endsWith("0")) {
                resultActions.andExpect(status().isBadRequest());
            } else {
                resultActions.andExpect(status().isOk());
            }
        }
        long duration = System.currentTimeMillis() - start;
        System.out.println("Processed 1000 requests (with some invalid) in " + duration + " ms");
        assert (duration < 7000);
    }
}