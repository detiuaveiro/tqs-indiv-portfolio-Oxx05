package pt.ua.tqs.ZeroMonos.data;

import jakarta.persistence.*;
import pt.ua.tqs.ZeroMonos.boundary.RESERVATION_STATE;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "requests")
public class ZeroMonosBookingRequest {
    private static int idCounter = 0;

    @Id
    private final String token;

    @Column(nullable = false)
    @jakarta.validation.constraints.NotNull(message = "State is mandatory")
    private RESERVATION_STATE state = RESERVATION_STATE.CREATED;

    @Column(nullable = false)
    @jakarta.validation.constraints.NotNull(message = "Date is mandatory")
    public LocalDateTime date;

    @Column(nullable = false)
    @Size(min = 1, max = 50)
    @NotBlank(message = "Municipality is mandatory")
    public String municipality;


    @Column(nullable = false)
    @Size(min = 1, max = 200)
    @NotBlank(message = "Description is mandatory")
    public String description;


    @ElementCollection
    @CollectionTable(
            name = "request_history",
            joinColumns = @JoinColumn(name = "request_token")
    )
    private List<StateChange> history = new ArrayList<>();

    public ZeroMonosBookingRequest() {
        idCounter++;
        token = "res_" + idCounter;
    }

    public ZeroMonosBookingRequest(LocalDateTime dateTime, String municipality, String description) {
        this.date = dateTime;
        this.municipality = municipality;
        this.description = description;
        idCounter++;
        token = "res_" + idCounter;
    }

    public void cancelBooking() {
        this.state = RESERVATION_STATE.CANCELLED;
    }

    public RESERVATION_STATE updateState() {
        switch (this.state) {
            case CREATED:
                this.state = RESERVATION_STATE.RECEIVED;
                break;
            case RECEIVED:
                this.state = RESERVATION_STATE.ASSIGNED;
                break;
            case ASSIGNED:
                this.state = RESERVATION_STATE.IN_PROGRESS;
                break;
            case IN_PROGRESS:
                this.state = RESERVATION_STATE.FINISHED;
                break;
        }

        return this.state;
    }

    @Override
    public String toString() {
        return "MealsBookingRequest{" +
                "date=" + date +
                ", userId='" + userId + '\'' +
                ", token='" + token + '\'' +
                ", state=" + state +
                '}';
    }

    //setters and getters e hashcode and equals

    public void setState(RESERVATION_STATE state) {
        this.state = state;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ZeroMonosBookingRequest that = (ZeroMonosBookingRequest) o;
        return token.equals(that.token);
    }

    @Override
    public int hashCode() {
        return token.hashCode();
    }

    public LocalDateTime getDate() {
        return date;
    }

    public String getUserId() {
        return userId;
    }
    public String getToken() {
        return token;
    }
    public RESERVATION_STATE getState() {
        return state;
    }


}
