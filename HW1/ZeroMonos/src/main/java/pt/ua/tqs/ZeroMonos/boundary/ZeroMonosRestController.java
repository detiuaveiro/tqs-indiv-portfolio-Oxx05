package pt.ua.tqs.ZeroMonos.boundary;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pt.ua.tqs.ZeroMonos.data.*;
import pt.ua.tqs.ZeroMonos.boundary.*;
import pt.ua.tqs.ZeroMonos.service.ZeroMonosService;

import java.util.List;

/**
 * REST API endpoints for managing users
 */
@RestController
@RequestMapping("/api")
public class ZeroMonosRestController {
    private final ZeroMonosService service;

    public ZeroMonosRestController(ZeroMonosService service) {
        this.service = service;
    }

    @PostMapping("/requests" )
    public ResponseEntity<ZeroMonosBookingRequest> createMeal(@RequestBody ZeroMonosBookingRequest meal) {
        HttpStatus status = HttpStatus.CREATED;
        ZeroMonosBookingRequest saved = service.saveMeal(meal);
        return new ResponseEntity<>(saved, status);
    }

    @GetMapping("/requests/{id}")
    public ResponseEntity<MealsBookingRequest> getBooking(@PathVariable(value = "id") String token)
            throws ResourceNotFoundException {
        MealsBookingRequest user = service.getMealsBookingRequest(token)
                .orElseThrow(() -> new ResourceNotFoundException("User not found for id: " + token));
        return ResponseEntity.ok().body(user);
    }

    @DeleteMapping("/requests/{token}")
    public ResponseEntity<ZeroMonosBookingRequest> deleteMeal(@PathVariable String token) throws ResourceNotFoundException {
        ZeroMonosBookingRequest meal = service.getMealsBookingRequest(token)
                    .orElseThrow(() -> new ResourceNotFoundException("User not found for id: " + token));
        service.deleteMeal(token);
        return ResponseEntity.ok().body(meal);
    }

    @PutMapping("/requests")
    public ResponseEntity<MealsBookingRequest> updateMeal(@RequestBody MealsBookingRequest meal) {
        HttpStatus status = HttpStatus.CREATED;
        service.updateMeal(meal);
        return ResponseEntity.ok().body(meal);
    }

    @GetMapping("/municipalities")
    public ResponseEntity<List<MealsBookingRequest>> getBookings(){
        List<MealsBookingRequest> meals = service.getMealsBookingRequests();

        return ResponseEntity.ok().body(meals);
    }

    @GetMapping("/staff/requests")
    public ResponseEntity<List<MealsBookingRequest>> getAllBookings(){
        List<MealsBookingRequest> meals = service.getMealsBookingRequests();

        return ResponseEntity.ok().body(meals);
    }


    @PutMapping("/staff/requests/{id}")
    public ResponseEntity<MealsBookingRequest> updateBookingStatus(@PathVariable(value = "id") String token, @RequestParam RESERVATION_STATE state)
            throws ResourceNotFoundException {
        MealsBookingRequest booking = service.getMealsBookingRequest(token)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found for id: " + token));
        booking.setState(state);
        service.updateMeal(booking);
        return ResponseEntity.ok().body(booking);
    }


}
