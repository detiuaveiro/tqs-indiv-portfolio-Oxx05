// src/main/java/pt/ua/tqs/ZeroMonos/boundary/ZeroMonosRestController.java
package pt.ua.tqs.ZeroMonos.boundary;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pt.ua.tqs.ZeroMonos.data.ZeroMonosBookingRequest;
import pt.ua.tqs.ZeroMonos.service.MunicipalityService;
import pt.ua.tqs.ZeroMonos.service.ZeroMonosService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ZeroMonosRestController {

    private static final Logger logger = LoggerFactory.getLogger(ZeroMonosRestController.class);

    private static final String ERROR_KEY = "error";

    private final ZeroMonosService service;
    private final MunicipalityService municipalityService;

    @ExceptionHandler({InvalidDateException.class, InvalidMunicipalityException.class, MaxCapacityException.class})
    public ResponseEntity<Map<String, String>> handleBadRequestExceptions(RuntimeException ex) {
        return ResponseEntity.badRequest().body(Map.of(ERROR_KEY, ex.getMessage()));
    }

    public ZeroMonosRestController(ZeroMonosService service, MunicipalityService municipalityService) {
        this.municipalityService = municipalityService;
        this.service = service;
    }

    @PostMapping("/requests")
    public ResponseEntity<Map<String, String>> createRequest(@RequestBody ZeroMonosBookingRequest req) {
        try {
            logger.info("Creating request for municipality {} at {}", req.getMunicipality(), req.getDate());
            ZeroMonosBookingRequest booking = service.saveRequest(req);
            logger.info("Request created with token {}", booking.getToken());
            return ResponseEntity.ok(Map.of("token", booking.getToken()));
        } catch (InvalidDateException e) {
            logger.warn("Request creation failed due to invalid date: {}", e.getMessage());
            return ResponseEntity.badRequest().body(Map.of(ERROR_KEY, e.getMessage()));
        } catch (InvalidMunicipalityException e) {
            logger.warn("Request creation failed due to invalid municipality: {}", e.getMessage());
            return ResponseEntity.badRequest().body(Map.of(ERROR_KEY, e.getMessage()));
        } catch (MaxCapacityException e) {
            logger.warn("Request creation failed due to max capacity reached: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                    .body(Map.of(ERROR_KEY, e.getMessage()));
        } catch (Exception e) {
            logger.error("Unexpected error creating request", e);
            return ResponseEntity.internalServerError()
                    .body(Map.of(ERROR_KEY, "Erro inesperado ao criar pedido."));
        }
    }


    @GetMapping("/requests/{id}")
    public ResponseEntity<ZeroMonosBookingRequest> getBooking(@PathVariable(value = "id") String token)
            throws ResourceNotFoundException {
        logger.info("Fetching request with token {}", token);
        ZeroMonosBookingRequest user = service.getZeroMonosRequest(token)
                .orElseThrow(() -> new ResourceNotFoundException("User not found for id: " + token));
        return ResponseEntity.ok().body(user);
    }

    @DeleteMapping("/requests/{token}")
    public ResponseEntity<ZeroMonosBookingRequest> deleteMeal(@PathVariable String token) throws ResourceNotFoundException {
        logger.info("Cancelling request with token {}", token);
        ZeroMonosBookingRequest request = service.getZeroMonosRequest(token)
                .orElseThrow(() -> new ResourceNotFoundException("User not found for id: " + token));
        service.cancelRequest(token);
        logger.info("Request {} cancelled", token);
        return ResponseEntity.ok().body(request);
    }

    @PutMapping("/requests")
    public ResponseEntity<ZeroMonosBookingRequest> updateMeal(@RequestBody ZeroMonosBookingRequest request) {
        logger.info("Updating request {}", request.getToken());
        service.updateRequest(request);
        logger.info("Request {} updated", request.getToken());
        return ResponseEntity.ok().body(request);
    }

    @GetMapping("/municipalities")
    public ResponseEntity<List<String>> getMunicipalities() {
        List<String> municipalities = municipalityService.getMunicipalities();
        logger.info("Fetched {} municipalities", municipalities.size());
        return ResponseEntity.ok().body(municipalities);
    }

    @PostMapping("/municipalities/update")
    public ResponseEntity<String> updateMunicipalities() {
        logger.info("Manually updating municipalities");
        municipalityService.fetchMunicipalities();
        logger.info("Municipalities updated");
        return ResponseEntity.ok("Municipalities list updated manually.");
    }

    @GetMapping("/staff/requests")
    public ResponseEntity<List<ZeroMonosBookingRequest>> getAllBookings(){
        logger.info("Fetching all booking requests");
        List<ZeroMonosBookingRequest> requests = service.getZeroMonosRequests();
        return ResponseEntity.ok().body(requests);
    }

    @GetMapping("/staff/requests/{municipality}")
    public ResponseEntity<List<ZeroMonosBookingRequest>> getAllBookingsByMunicipality(@PathVariable String municipality){
        logger.info("Fetching booking requests for municipality {}", municipality);
        List<ZeroMonosBookingRequest> requests = service.getZeroMonosRequestsByMunicipality(municipality);
        return ResponseEntity.ok().body(requests);
    }

    @PutMapping("/staff/requests/{id}")
    public ResponseEntity<ZeroMonosBookingRequest> updateBookingStatus(@PathVariable(value = "id") String token)
            throws ResourceNotFoundException {
        logger.info("Updating booking status for token {}", token);
        ZeroMonosBookingRequest booking = service.getZeroMonosRequest(token)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found for id: " + token));
        service.updateRequest(booking);
        logger.info("Booking status updated for token {}", token);
        return ResponseEntity.ok().body(booking);
    }
}