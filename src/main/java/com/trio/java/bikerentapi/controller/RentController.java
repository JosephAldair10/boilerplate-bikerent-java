package com.trio.java.bikerentapi.controller;

import com.trio.java.bikerentapi.dto.BikeDto;
import com.trio.java.bikerentapi.dto.RentDto;
import com.trio.java.bikerentapi.mapper.BikeMapper;
import com.trio.java.bikerentapi.mapper.RentMapper;
import com.trio.java.bikerentapi.service.BikeService;
import com.trio.java.bikerentapi.service.RentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/rent")
public class RentController {

    private RentService rentService;

    public RentController(RentService rentService) {
        this.rentService = rentService;
    }

    @PostMapping
    public ResponseEntity<RentDto> rent(@RequestBody RentDto rent) {
        RentDto responseData = RentMapper.fromRent(rentService.rent(rent));
        return new ResponseEntity<>(responseData, HttpStatus.CREATED);
    }
}
