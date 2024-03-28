package com.trio.java.bikerentapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Bike not available for selected dates")
public class BikeNotAvailableException extends RuntimeException {
}
