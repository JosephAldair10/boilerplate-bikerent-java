package com.trio.java.bikerentapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Dates are not valid")
public class InvalidRentDatesException extends RuntimeException {
}
