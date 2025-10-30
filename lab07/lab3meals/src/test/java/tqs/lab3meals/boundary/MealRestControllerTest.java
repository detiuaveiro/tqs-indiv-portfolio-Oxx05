// File: lab3meals/src/test/java/tqs/lab3meals/boundary/MealRestControllerTest.java
package tqs.lab3meals.boundary;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import tqs.lab3meals.data.MealsBookingRequest;
import tqs.lab3meals.service.MealService;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class MealRestControllerTest {

    @Mock
    MealService service;

    @InjectMocks
    MealRestController controller;

    MockMvc mockMvc;
    ObjectMapper mapper;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        mapper = new ObjectMapper().registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @Test
    void testCreateMeal() throws Exception {
        MealsBookingRequest req = new MealsBookingRequest(LocalDateTime.now().plusDays(1).withHour(9), "u1");
        when(service.saveMeal(any())).thenReturn(req);

        mockMvc.perform(post("/api/meals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.token").value(req.getToken()));

        verify(service).saveMeal(any());
    }

    @Test
    void testGetBookingFound() throws Exception {
        MealsBookingRequest req = new MealsBookingRequest(LocalDateTime.now().plusDays(1).withHour(9), "u2");
        when(service.getMealsBookingRequest(req.getToken())).thenReturn(Optional.of(req));

        mockMvc.perform(get("/api/meals/" + req.getToken()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value(req.getToken()));
    }

    @Test
    void testGetBookingNotFound() throws Exception {
        when(service.getMealsBookingRequest("missing")).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/meals/missing"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteMealFound() throws Exception {
        MealsBookingRequest req = new MealsBookingRequest(LocalDateTime.now().plusDays(1).withHour(9), "u3");
        when(service.getMealsBookingRequest(req.getToken())).thenReturn(Optional.of(req));

        mockMvc.perform(delete("/api/meals/" + req.getToken()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value(req.getToken()));

        verify(service).deleteMeal(req.getToken());
    }

    @Test
    void testDeleteMealNotFound() throws Exception {
        when(service.getMealsBookingRequest("no")).thenReturn(Optional.empty());

        mockMvc.perform(delete("/api/meals/no"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testUpdateMeal() throws Exception {
        MealsBookingRequest req = new MealsBookingRequest(LocalDateTime.now().plusDays(2).withHour(9), "u4");

        mockMvc.perform(put("/api/meals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value(req.getToken()));

        verify(service).updateMeal(any());
    }
}
