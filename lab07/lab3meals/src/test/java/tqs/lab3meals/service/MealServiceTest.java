// File: lab3meals/src/test/java/tqs/lab3meals/service/MealServiceTest.java
package tqs.lab3meals.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import tqs.lab3meals.data.MealRepository;
import tqs.lab3meals.data.MealsBookingRequest;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class MealServiceTest {

    @Mock
    MealRepository repository;

    @InjectMocks
    MealService service;

    MealsBookingRequest sample;

    @BeforeEach
    void setup() {
        sample = new MealsBookingRequest(LocalDateTime.now().plusDays(1).withHour(9), "user1");
    }

    @Test
    void testSaveMeal() {
        when(repository.save(sample)).thenReturn(sample);
        MealsBookingRequest saved = service.saveMeal(sample);
        assertNotNull(saved);
        verify(repository, times(1)).save(sample);
    }

    @Test
    void testGetMealsBookingRequestFound() {
        when(repository.findByToken(sample.getToken())).thenReturn(Optional.of(sample));
        Optional<MealsBookingRequest> res = service.getMealsBookingRequest(sample.getToken());
        assertTrue(res.isPresent());
        assertEquals(sample, res.get());
    }

    @Test
    void testGetMealsBookingRequestNotFound() {
        when(repository.findByToken("missing")).thenReturn(Optional.empty());
        assertTrue(service.getMealsBookingRequest("missing").isEmpty());
    }

    @Test
    void testGetMealsBookingRequests() {
        List<MealsBookingRequest> list = Arrays.asList(sample);
        when(repository.findAll()).thenReturn(list);
        List<MealsBookingRequest> res = service.getMealsBookingRequests();
        assertEquals(1, res.size());
        verify(repository).findAll();
    }

    @Test
    void testDeleteMeal() {
        service.deleteMeal("any");
        verify(repository).deleteByToken("any");
    }

    @Test
    void testExistsMeal() {
        when(repository.existsMealsBookingRequestByToken("tok")).thenReturn(true);
        assertTrue(service.existsMeal("tok"));
        verify(repository).existsMealsBookingRequestByToken("tok");
    }

    @Test
    void testUpdateMealExists() {
        when(repository.existsMealsBookingRequestByToken(sample.getToken())).thenReturn(true);
        service.updateMeal(sample);
        verify(repository).save(sample);
    }

    @Test
    void testUpdateMealNotExists() {
        when(repository.existsMealsBookingRequestByToken(sample.getToken())).thenReturn(false);
        service.updateMeal(sample);
        verify(repository, never()).save(sample);
    }
}
