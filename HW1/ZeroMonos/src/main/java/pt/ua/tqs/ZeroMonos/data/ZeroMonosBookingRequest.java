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

    @Id
    private final String token;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @jakarta.validation.constraints.NotNull(message = "State is mandatory")
    private RESERVATION_STATE state = RESERVATION_STATE.CREATED;

    @Column(nullable = false)
    @jakarta.validation.constraints.NotNull(message = "Date is mandatory")
    private LocalDateTime date;

    @Column(nullable = false)
    @Size(min = 1, max = 50)
    @NotBlank(message = "Municipality is mandatory")
    private String municipality;


    @Column(nullable = false)
    @Size(min = 1, max = 500)
    @NotBlank(message = "Description is mandatory")
    private String description;


    @ElementCollection
    @CollectionTable(
            name = "request_history",
            joinColumns = @JoinColumn(name = "request_token")
    )
    private final List<StateChange> history = new ArrayList<>();

    public ZeroMonosBookingRequest() {
        token = "res_" + java.util.UUID.randomUUID().toString();
        StateChange st = new StateChange(this.state);
        this.history.add(st);
    }

    public ZeroMonosBookingRequest(LocalDateTime dateTime, String municipality, String description) {
        this();
        this.date = dateTime;
        this.municipality = municipality;
        this.description = description;
    }

    public void cancelBooking() {
        if (this.state == RESERVATION_STATE.FINISHED || this.state == RESERVATION_STATE.CANCELLED) {
            return;
        }

        this.state = RESERVATION_STATE.CANCELLED;
        StateChange st = new StateChange(this.state);
        this.history.add(st);
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
            default:
                return this.state;
        }

        StateChange st = new StateChange(this.state);
        this.history.add(st);
        return this.state;
    }


    @Override
    public String toString() {
        return "ZonoMonosRequst{" +
                "token='" + token + '\'' +
                ", date=" + date +
                ", state=" + state +
                ", municipality='" + municipality + '\'' +
                ", description=" + description +
                '}';
    }

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

    public String getMunicipality() {
        return municipality;
    }
    public String getDescription() {
        return description;
    }
    public String getToken() {
        return token;
    }
    public RESERVATION_STATE getState() {
        return state;
    }
    public List<StateChange> getHistory() {return  history;}

    public LocalDateTime getChangeTime(RESERVATION_STATE state) {
        for (StateChange sc : history) {
            if (sc.getState() == state) {
                return sc.getChangeDate();
            }
        }
        return null;
    }


}
