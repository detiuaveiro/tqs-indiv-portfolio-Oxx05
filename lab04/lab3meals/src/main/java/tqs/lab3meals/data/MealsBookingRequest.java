package tqs.lab3meals.data;

import jakarta.persistence.*;
import tqs.lab3meals.boundary.RESERVATION_STATE;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

@Entity
@Table(name = "meals")
public class MealsBookingRequest {
    private static int idCounter = 0;

    @Id
    private String token;

    @Column(nullable = false)
    @jakarta.validation.constraints.NotNull(message = "State is mandatory")
    public RESERVATION_STATE state = RESERVATION_STATE.RESERVED;

    @Column(nullable = false)
    @jakarta.validation.constraints.NotNull(message = "Date is mandatory")
    public LocalDateTime date;

    @Column(nullable = false)
    @Size(min = 1, max = 50)
    @NotBlank(message = "User is mandatory")
    public String userId;

    public MealsBookingRequest() {
        idCounter++;
        token = "res_" + idCounter;
    }

    public MealsBookingRequest(LocalDateTime dateTime, String userId) {
        this.date = dateTime;
        this.userId = userId;
        idCounter++;
        token = "res_" + idCounter;
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
        MealsBookingRequest that = (MealsBookingRequest) o;
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
