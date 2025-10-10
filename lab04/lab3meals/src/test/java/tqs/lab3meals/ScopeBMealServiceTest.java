// src/test/java/tqs/lab3meals/ScopeBMealServiceTest.java
package tqs.lab3meals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import tqs.lab3meals.data.MealRepository;
import tqs.lab3meals.data.MealsBookingRequest;
import tqs.lab3meals.service.MealService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import java.util.Optional;

import java.time.Month;
import java.time.LocalDateTime;

public class ScopeBMealServiceTest {
    MealRepository mealRepository;
    MealService service;

    @BeforeEach
    void setup() {
        mealRepository = Mockito.mock(MealRepository.class);
        service = new MealService(mealRepository);
    }

    @Test
    public void repeatedReserve() {
        var req = new MealsBookingRequest(LocalDateTime.of(2026, Month.SEPTEMBER, 20, 12, 0), "user");

        // Primeira vez: não existe -> guarda
        Mockito.when(mealRepository.existsMealsBookingRequestByToken(req.getToken()))
                .thenReturn(false);
        Mockito.when(mealRepository.save(any(MealsBookingRequest.class)))
                .thenReturn(req);

        service.saveMeal(req);

        // Segunda vez: já existe
        Mockito.when(mealRepository.existsMealsBookingRequestByToken(req.getToken()))
                .thenReturn(true);

        assertNull(service.saveMeal(req));
    }

    @Test
    public void outOfBoundsReserve() {
        var req = new MealsBookingRequest(LocalDateTime.of(2026, Month.SEPTEMBER, 20, 16, 0), "user");

        Mockito.when(mealRepository.existsMealsBookingRequestByToken(req.getToken()))
                .thenReturn(false);

        assertNull(service.saveMeal(req)); // hora inválida
    }

    @Test
    public void pastReserve() {
        var req = new MealsBookingRequest(LocalDateTime.of(2024, Month.SEPTEMBER, 18, 12, 0), "user");

        Mockito.when(mealRepository.existsMealsBookingRequestByToken(req.getToken()))
                .thenReturn(false);

        assertNull(service.saveMeal(req)); // data passada
    }

    @Test
    public void reuseReserve() {
        var req = new MealsBookingRequest(LocalDateTime.of(2026, Month.SEPTEMBER, 18, 12, 0), "user");

        Mockito.when(mealRepository.existsMealsBookingRequestByToken(req.getToken()))
                .thenReturn(false);
        Mockito.when(mealRepository.countByDateAndHourInterval(any(), anyInt(), anyInt()))
                .thenReturn(0);
        Mockito.when(mealRepository.save(any(MealsBookingRequest.class)))
                .thenReturn(req);

        MealsBookingRequest meal = service.saveMeal(req);
        assertNotNull(meal);

        // Primeira vez: deve dar checkIn = true
        Mockito.when(mealRepository.existsMealsBookingRequestByToken(meal.getToken()))
                .thenReturn(true);
        assertTrue(service.checkIn(meal));

        // Agora já está USED → segunda vez deve falhar
        Mockito.when(mealRepository.existsMealsBookingRequestByToken(meal.getToken()))
                .thenReturn(true);
        assertFalse(service.checkIn(meal));
    }

    @Test
    public void fullRoom() {
        LocalDateTime dateTime = LocalDateTime.of(2026, Month.SEPTEMBER, 18, 12, 0);

        for (int i = 0; i < 10; i++) {
            var req = new MealsBookingRequest(dateTime, "user" + i);
            Mockito.when(mealRepository.existsMealsBookingRequestByToken(req.getToken()))
                    .thenReturn(false);
            Mockito.when(mealRepository.countByDateAndHourInterval(any(), anyInt(), anyInt()))
                    .thenReturn(i);
            Mockito.when(mealRepository.save(any(MealsBookingRequest.class)))
                    .thenReturn(req);
            assertNotNull(service.saveMeal(req));
        }

        // 11º pedido: count = 11 → excede limite
        var reqFull = new MealsBookingRequest(dateTime, "userFull");
        Mockito.when(mealRepository.existsMealsBookingRequestByToken(reqFull.getToken()))
                .thenReturn(false);
        Mockito.when(mealRepository.countByDateAndHourInterval(any(), anyInt(), anyInt()))
                .thenReturn(11);

        assertNull(service.saveMeal(reqFull));
    }

    @Test
    public void canceledReserve() {
        var req = new MealsBookingRequest(LocalDateTime.of(2026, Month.SEPTEMBER, 18, 12, 0), "user");

        Mockito.when(mealRepository.existsMealsBookingRequestByToken(req.getToken()))
                .thenReturn(false);
        Mockito.when(mealRepository.countByDateAndHourInterval(any(), anyInt(), anyInt()))
                .thenReturn(0);
        Mockito.when(mealRepository.save(any(MealsBookingRequest.class)))
                .thenReturn(req);

        MealsBookingRequest meal = service.saveMeal(req);
        assertNotNull(meal);

        // simular que a reserva foi apagada
        Mockito.when(mealRepository.existsMealsBookingRequestByToken(meal.getToken()))
                .thenReturn(false);

        assertFalse(service.checkIn(meal)); // não deve conseguir fazer check-in
    }

    @Test
    public void findReserve() {
        var req = new MealsBookingRequest(LocalDateTime.of(2026, Month.SEPTEMBER, 18, 12, 0), "user");

        Mockito.when(mealRepository.existsMealsBookingRequestByToken(req.getToken()))
                .thenReturn(false);
        Mockito.when(mealRepository.countByDateAndHourInterval(any(), anyInt(), anyInt()))
                .thenReturn(0);
        Mockito.when(mealRepository.save(any(MealsBookingRequest.class)))
                .thenReturn(req);
        Mockito.when(mealRepository.findByToken(req.getToken()))
                .thenReturn(Optional.of(req));
        Mockito.when(mealRepository.findByToken("123"))
                .thenReturn(Optional.empty());

        MealsBookingRequest meal = service.saveMeal(req);
        assertNotNull(meal);

        assertTrue(service.getMealsBookingRequest(meal.getToken()).isPresent());
        assertTrue(service.getMealsBookingRequest("123").isEmpty());
    }
}