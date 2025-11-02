package pt.ua.tqs.ZeroMonos.boundary;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class InvalidMunicipalityException extends Exception {

    public InvalidMunicipalityException(String message){
        super(message);
    }
}
