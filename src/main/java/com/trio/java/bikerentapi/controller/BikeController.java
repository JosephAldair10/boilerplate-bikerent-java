package com.trio.java.bikerentapi.controller;

import com.trio.java.bikerentapi.dto.BikeDto;
import com.trio.java.bikerentapi.exception.BikeNotFoundException;
import com.trio.java.bikerentapi.mapper.BikeMapper;
import com.trio.java.bikerentapi.service.BikeService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping("/api/bikes")
public class BikeController {

    @Autowired
    private BikeService bikeService;

    @GetMapping
    public List<BikeDto> findAll() {
        return bikeService.getAllBikes().stream()
                .map(BikeMapper::fromBike)
                .toList();
    }

    @GetMapping(value = "/{id}")
    public BikeDto findById(@PathVariable("id") int id) {
        return BikeMapper.fromBike(bikeService.getBikeDetails(id)
                .orElseThrow(BikeNotFoundException::new));
    }
}
