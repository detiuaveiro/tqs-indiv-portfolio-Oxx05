package pt.ua.tqs;

import java.time.LocalDateTime;

public class MealsBookingRequest {
    public final LocalDateTime date;
    public final String userId;
    public RESERVATION_STATE state = RESERVATION_STATE.RESERVED;

    public MealsBookingRequest(LocalDateTime dateTime, String userId) {
        this.date = dateTime;
        this.userId = userId;
    }
}
