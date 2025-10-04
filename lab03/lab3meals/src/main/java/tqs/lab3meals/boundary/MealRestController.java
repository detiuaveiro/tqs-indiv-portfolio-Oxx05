package tqs.lab3meals.boundary;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tqs.lab3meals.data.MealsBookingRequest;
import tqs.lab3meals.service.MealService;
import java.util.List;

/**
 * REST API endpoints for managing users
 */
@RestController
@RequestMapping("/api")
public class MealRestController {


    private final MealService service;

    public MealRestController(MealService service) {
        this.service = service;
    }

    @PostMapping("/meals" )
    public ResponseEntity<MealsBookingRequest> createMeal(@RequestBody MealsBookingRequest meal) {
        HttpStatus status = HttpStatus.CREATED;
        MealsBookingRequest saved = service.saveMeal(meal);
        return new ResponseEntity<>(saved, status);
    }

    @DeleteMapping("/meals/{id}")
    public ResponseEntity<MealsBookingRequest> deleteMeal(@PathVariable String id) throws ResourceNotFoundException {
            MealsBookingRequest meal = service.getMealsBookingRequest(id)
                    .orElseThrow(() -> new ResourceNotFoundException("User not found for id: " + id));
        service.deleteMeal(id);
        return ResponseEntity.ok().body(meal);
    }

    @PutMapping("/meals")
    public ResponseEntity<MealsBookingRequest> updateMeal(@RequestBody MealsBookingRequest meal) {
        HttpStatus status = HttpStatus.CREATED;
        service.updateMeal(meal);
        return ResponseEntity.ok().body(meal);
    }


    @GetMapping("/meals/{id}")
    public ResponseEntity<MealsBookingRequest> getBooking(@PathVariable(value = "id") String token)
            throws ResourceNotFoundException {
        MealsBookingRequest user = service.getMealsBookingRequest(token)
                .orElseThrow(() -> new ResourceNotFoundException("User not found for id: " + token));
        return ResponseEntity.ok().body(user);
    }


    @GetMapping("/meals")
    public ResponseEntity<List<MealsBookingRequest>> getBookings(){
        List<MealsBookingRequest> meals = service.getMealsBookingRequests();

        return ResponseEntity.ok().body(meals);
    }


}
