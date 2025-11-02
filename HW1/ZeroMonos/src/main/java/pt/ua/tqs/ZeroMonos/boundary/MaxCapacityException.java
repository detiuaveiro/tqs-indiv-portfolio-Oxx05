package pt.ua.tqs.ZeroMonos.boundary;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class MaxCapacityException extends Exception {

    public MaxCapacityException(String message){
        super(message);
    }
}
