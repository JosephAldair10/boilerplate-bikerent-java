package com.trio.java.bikerentapi.controller;

import com.trio.java.bikerentapi.dto.RentDto;
import com.trio.java.bikerentapi.mapper.RentMapper;
import com.trio.java.bikerentapi.service.RentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/rent")
public class RentController {

    Logger logger = LoggerFactory.getLogger(RentController.class);

    private RentService rentService;

    public RentController(RentService rentService) {
        this.rentService = rentService;
    }

    @PostMapping
    public ResponseEntity<RentDto> rent(@RequestBody RentDto rent) {
        logger.info("Request received in rent service: {}", rent);
        RentDto responseData = RentMapper.fromRent(rentService.rent(rent));
        logger.info("Sending response for rent service: {}", responseData);
        return new ResponseEntity<>(responseData, HttpStatus.CREATED);
    }
}
