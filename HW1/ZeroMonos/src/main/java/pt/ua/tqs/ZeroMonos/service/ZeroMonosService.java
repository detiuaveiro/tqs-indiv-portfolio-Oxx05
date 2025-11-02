package pt.ua.tqs.ZeroMonos.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pt.ua.tqs.ZeroMonos.boundary.InvalidDateException;
import pt.ua.tqs.ZeroMonos.boundary.InvalidMunicipalityException;
import pt.ua.tqs.ZeroMonos.boundary.MaxCapacityException;
import pt.ua.tqs.ZeroMonos.data.Municipality;
import pt.ua.tqs.ZeroMonos.data.ZeroMonosBookingRequest;
import pt.ua.tqs.ZeroMonos.data.ZeroMonosRepository;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ZeroMonosService {

    private static final Logger logger = LoggerFactory.getLogger(ZeroMonosService.class);

    private final ZeroMonosRepository repository;
    private final MunicipalityService municipalityService;

    public ZeroMonosService(ZeroMonosRepository repository, MunicipalityService municipalityService) {
        this.municipalityService = municipalityService;
        this.repository = repository;
    }

    public void cancelRequest(String token) {
        logger.info("Cancelling request with token {}", token);
        getZeroMonosRequest(token).ifPresent(req -> {
            req.cancelBooking();
            repository.save(req);
            logger.info("Request {} cancelled", token);
        });
    }

    public Optional<ZeroMonosBookingRequest> getZeroMonosRequest(String token){
        return repository.findByToken(token);
    }

    public List<ZeroMonosBookingRequest> getZeroMonosRequests(){
        return repository.findAll();
    }

    public List<ZeroMonosBookingRequest> getZeroMonosRequestsByMunicipality(String municipalityName){
        return repository.findAllByMunicipality(municipalityName);
    }

    public boolean existsRequest(String token){
        return repository.existsZeroMonosRequestByToken(token);
    }

    public ZeroMonosBookingRequest saveRequest(ZeroMonosBookingRequest request)
            throws InvalidDateException, InvalidMunicipalityException, MaxCapacityException {
        logger.info("Saving request for municipality {} at {}", request.getMunicipality(), request.getDate());
        LocalDateTime date = request.getDate();

        if (date == null || date.isBefore(LocalDateTime.now())) {
            logger.warn("Invalid date: {}", date);
            throw new InvalidDateException("Data inválida ou no passado.");
        }

        if (date.getHour() > 18 || date.getHour() < 8) {
            logger.warn("Request outside allowed hours: {}", date.getHour());
            throw new InvalidDateException("A recolha só é possível entre as 8h e as 18h.");
        }

        if (date.getDayOfWeek() == DayOfWeek.SATURDAY || date.getDayOfWeek() == DayOfWeek.SUNDAY) {
            logger.warn("Request on weekend: {}", date.getDayOfWeek());
            throw new InvalidDateException("As recolhas não são feitas ao fim de semana.");
        }

        if (request.getMunicipality() == null || !municipalityService.getMunicipalities().contains(request.getMunicipality())) {
            logger.warn("Invalid municipality: {}", request.getMunicipality());
            throw new InvalidMunicipalityException("O município não é válido.");
        }

        LocalDate dayDate = date.toLocalDate();
        LocalDateTime startOfDay = dayDate.atStartOfDay();
        LocalDateTime endOfDay = dayDate.atTime(23, 59, 59, 999_999_999);

        int count = repository.countByDayAndMunicipality(startOfDay, endOfDay, request.getMunicipality());
        if (count >= Municipality.MAX_DAILY_REQUESTS) {
            logger.warn("Max requests reached for municipality {} on {}", request.getMunicipality(), dayDate);
            throw new MaxCapacityException("Número máximo de pedidos atingido para o município neste dia.");
        }

        ZeroMonosBookingRequest saved = repository.save(request);
        logger.info("Request saved with token {}", saved.getToken());
        return saved;
    }

    public void updateRequest(ZeroMonosBookingRequest request){
        if (repository.existsZeroMonosRequestByToken(request.getToken())) {
            logger.info("Updating state for request {}", request.getToken());
            request.updateState();
            repository.save(request);
            logger.info("Request {} updated", request.getToken());
        }
    }
}
