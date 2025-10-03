package tqs.lab3meals.boundary;

import tqs.lab3meals.data.MealsBookingRequest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

public class MealsBookingService {
    private final int maxPopulation;
    private final List<int[]> mealIntervals; // Cada int[]: {startHour, endHour}
    private final Map<String, MealsBookingRequest> reservationsByToken = new HashMap<>();
    private final Map<LocalDate, Map<int[], Set<String>>> bookedUsers = new HashMap<>();

    public MealsBookingService(int maxPopulation, List<int[]> mealIntervals) {
        this.maxPopulation = maxPopulation;
        this.mealIntervals = mealIntervals;
        LocalDate today = LocalDate.now();
        for (int i = 0; i < 7; i++) {
            LocalDate date = today.plusDays(i);
            bookedUsers.put(date, new HashMap<>());
            for (int[] interval : mealIntervals) {
                bookedUsers.get(date).put(interval, new HashSet<>());
            }
        }
    }

    public String bookMeal(MealsBookingRequest request) {
        LocalDateTime dateTime = request.date;
        LocalDate date = dateTime.toLocalDate();
        int hour = dateTime.getHour();

        if (dateTime.isBefore(LocalDateTime.now())) return null; // não pode reservar no passado
        if (!bookedUsers.containsKey(date)) return null; // fora da janela inicializada

        for (int[] interval : mealIntervals) {
            if (hour >= interval[0] && hour < interval[1]) { // verifica se hora cai no turno
                Set<String> users = bookedUsers.get(date).get(interval);

                if (users.contains(request.userId)) return null; // já tem reserva nesse turno
                if (users.size() >= maxPopulation) return null; // sala cheia

                reservationsByToken.put(request.getToken(), request);
                users.add(request.userId);
                return request.getToken();
            }
        }
        return null; // não pertence a nenhum turno válido
    }

    public Optional<MealsBookingRequest> findReservation(String token) {
        return Optional.ofNullable(reservationsByToken.get(token));
    }

    public void cancelReservation(String token) {
        MealsBookingRequest reservation = reservationsByToken.get(token);
        if (reservation != null && reservation.state == RESERVATION_STATE.RESERVED) {
            reservation.state = RESERVATION_STATE.CANCELED;

            LocalDateTime dateTime = reservation.date;
            LocalDate date = dateTime.toLocalDate();
            int hour = dateTime.getHour();

            for (int[] interval : mealIntervals) {
                if (hour >= interval[0] && hour < interval[1]) {
                    bookedUsers.get(date).get(interval).remove(reservation.userId);
                    break;
                }
            }
        }
    }

    public boolean checkIn(String token) {
        MealsBookingRequest reservation = reservationsByToken.get(token);
        if (reservation != null && reservation.state == RESERVATION_STATE.RESERVED) {
            reservation.state = RESERVATION_STATE.USED;
            return true;
        }
        return false;
    }
}
