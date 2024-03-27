package com.trio.java.bikerentapi.mapper;

import com.trio.java.bikerentapi.data.Rent;
import com.trio.java.bikerentapi.dto.RentDto;

public class RentMapper {

    public static RentDto fromRent(Rent rent) {
        return RentDto.builder()
                .withId(rent.getId())
                .withBike(BikeMapper.fromBike(rent.getBike()))
                .withCustomer(CustomerMapper.fromCustomer(rent.getCustomer()))
                .withStartDate(rent.getStartdate())
                .withEndDate(rent.getEnddate())
                .withFee(rent.getFee())
                .withTotalDays(rent.getDays())
                .withTotal(rent.getTotal())
                .build();
    }
}
