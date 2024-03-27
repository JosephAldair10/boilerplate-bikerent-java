package com.trio.java.bikerentapi.mapper;

import com.trio.java.bikerentapi.data.Bike;
import com.trio.java.bikerentapi.data.BikeImage;
import com.trio.java.bikerentapi.dto.BikeDto;

public class BikeMapper {

    public static BikeDto fromBike(Bike bike) {
        return BikeDto.builder()
                .withId(bike.getId())
                .withName(bike.getName())
                .withType(bike.getType())
                .withBodySize(bike.getBodySize())
                .withMaxLoad(bike.getMaxLoad())
                .withRate(bike.getRate())
                .withDescription(bike.getDescription())
                .withRatings(bike.getRatings())
                .withImageUrls(
                        bike.getImageUrls().stream()
                                .map(BikeImage::getUrl)
                                .toList()
                )
                .build();
    }
}
