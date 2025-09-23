package pt.ua.tqs;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.Month;
import java.util.List;
import java.time.LocalDateTime;

public class MealsBookingServiceTest {

    @Test
    public void repeatedReserve(){
        MealsBookingService service = new MealsBookingService(10, List.of(new int[]{12, 14}, new int[]{19, 21}));

        var req = new MealsBookingRequest(LocalDateTime.of(2024, Month.SEPTEMBER, 20, 12, 0), "user");

        service.bookMeal(req);

        assertNull(service.bookMeal(req));
    }

    @Test
    public void outOfBoundsReserve(){
        MealsBookingService service = new MealsBookingService(10, List.of(new int[]{12, 14}, new int[]{19, 21}));

        var req = new MealsBookingRequest(LocalDateTime.of(2024, Month.SEPTEMBER, 20, 16, 0), "user");

        service.bookMeal(req);

        assertNull(service.bookMeal(req));
    }

    @Test
    public void pastReserve(){
        MealsBookingService service = new MealsBookingService(10, List.of(new int[]{12, 14}, new int[]{19, 21}));

        var req = new MealsBookingRequest(LocalDateTime.of(2024, Month.SEPTEMBER, 18, 12, 0), "user");

        assertNull(service.bookMeal(req));
    }

    @Test
    public void reuseReserve(){
        MealsBookingService service = new MealsBookingService(10, List.of(new int[]{12, 14}, new int[]{19, 21}));

        var req = new MealsBookingRequest(LocalDateTime.of(2025, Month.SEPTEMBER, 18, 12, 0), "user");

        String token = service.bookMeal(req);
        service.checkIn(token);

        assertFalse(service.checkIn(token));
    }


    @Test
    public void fullRoom(){
        MealsBookingService service = new MealsBookingService(1, List.of(new int[]{12, 14}, new int[]{19, 21}));

        var req = new MealsBookingRequest(LocalDateTime.of(2025, Month.SEPTEMBER, 18, 12, 0), "user");
        var req1 = new MealsBookingRequest(LocalDateTime.of(2025, Month.SEPTEMBER, 18, 12, 0), "user1");

        service.bookMeal(req);
        assertNull(service.bookMeal(req1));
    }

    @Test
    public void canceledReserve(){
        MealsBookingService service = new MealsBookingService(10, List.of(new int[]{12, 14}, new int[]{19, 21}));

        var req = new MealsBookingRequest(LocalDateTime.of(2025, Month.SEPTEMBER, 18, 12, 0), "user");

        String token = service.bookMeal(req);
        service.cancelReservation(token);
        assertFalse(service.checkIn(token));
    }

    @Test
    public void falseReserve(){

        MealsBookingService service = new MealsBookingService(10, List.of(new int[]{12, 14}, new int[]{19, 21}));

        service.cancelReservation("123");
    }




    @Test
    public void findReserve(){
        MealsBookingService service = new MealsBookingService(10, List.of(new int[]{12, 14}, new int[]{19, 21}));

        var req = new MealsBookingRequest(LocalDateTime.of(2025, Month.SEPTEMBER, 18, 12, 0), "user");

        String token = service.bookMeal(req);
        assertTrue(service.findReservation(token).isPresent());
        assertTrue(service.findReservation("123").isEmpty());

    }


}
