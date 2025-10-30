package pt.ua.tqs.ZeroMonos.data;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import pt.ua.tqs.ZeroMonos.boundary.RESERVATION_STATE;

import java.time.LocalDateTime;

@Embeddable
public class StateChange {
    @Enumerated(EnumType.STRING)
    private RESERVATION_STATE state;

    private LocalDateTime changeDate;
}