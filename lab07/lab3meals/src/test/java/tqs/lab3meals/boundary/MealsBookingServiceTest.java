// File: lab3meals/src/test/java/tqs/lab3meals/boundary/MealsBookingServiceTest.java
package tqs.lab3meals.boundary;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tqs.lab3meals.data.MealsBookingRequest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class MealsBookingServiceTest {

    MealsBookingService service;

    @BeforeEach
    void setup() {
        List<int[]> intervals = Arrays.asList(new int[]{8, 10}, new int[]{12, 14});
        service = new MealsBookingService(1, intervals);
    }

    @Test
    void testBookMealSuccess() {
        LocalDateTime dt = LocalDate.now().plusDays(1).atTime(8, 30);
        MealsBookingRequest r = new MealsBookingRequest(dt, "alice");
        String token = service.bookMeal(r);
        assertNotNull(token);
        assertEquals(r.getToken(), token);
    }

    @Test
    void testBookMealFull() {
        LocalDateTime dt = LocalDate.now().plusDays(1).atTime(8, 30);
        MealsBookingRequest r1 = new MealsBookingRequest(dt, "u1");
        MealsBookingRequest r2 = new MealsBookingRequest(dt, "u2");

        assertNotNull(service.bookMeal(r1));
        assertNull(service.bookMeal(r2)); // full because maxPopulation == 1
    }

    @Test
    void testBookMealDuplicateUser() {
        LocalDateTime dt = LocalDate.now().plusDays(1).atTime(8, 30);
        MealsBookingRequest r1 = new MealsBookingRequest(dt, "same");
        MealsBookingRequest r2 = new MealsBookingRequest(dt, "same");

        assertNotNull(service.bookMeal(r1));
        assertNull(service.bookMeal(r2)); // same user can't book same interval
    }

    @Test
    void testBookPastDate() {
        LocalDateTime past = LocalDateTime.now().minusDays(1);
        MealsBookingRequest r = new MealsBookingRequest(past, "bob");
        assertNull(service.bookMeal(r));
    }

    @Test
    void testCheckInAndCancel() {
        LocalDateTime dt = LocalDate.now().plusDays(1).atTime(8, 30);
        MealsBookingRequest r = new MealsBookingRequest(dt, "c1");
        String token = service.bookMeal(r);
        assertNotNull(token);

        // check-in success
        assertTrue(service.checkIn(token));
        assertEquals(RESERVATION_STATE.USED, r.getState());

        // cancel on USED should not change to CANCELED
        service.cancelReservation(token);
        assertEquals(RESERVATION_STATE.USED, r.getState());

        // create new booking to test cancel from RESERVED
        MealsBookingRequest r2 = new MealsBookingRequest(dt.plusDays(1), "c2");
        String t2 = service.bookMeal(r2);
        assertNotNull(t2);
        service.cancelReservation(t2);
        assertEquals(RESERVATION_STATE.CANCELED, r2.getState());
    }
}
