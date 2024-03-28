package com.trio.java.bikerentapi.mapper;

import com.trio.java.bikerentapi.data.Bike;
import com.trio.java.bikerentapi.data.BikeImage;
import com.trio.java.bikerentapi.dto.BikeDto;
import com.trio.java.bikerentapi.util.RepositoryDataProvider;
import java.util.List;
import java.util.stream.Collectors;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class BikeMapperTest {

    @Test
    void shouldReturnBikeDto() {
        Bike bike = RepositoryDataProvider.getBike();

        BikeDto bikeDto = BikeMapper.fromBike(bike);

        Assertions.assertThat(bikeDto).isNotNull();
        Assertions.assertThat(bikeDto.getId()).isNotNull();
        Assertions.assertThat(bikeDto.getName()).isNotNull();
        Assertions.assertThat(bikeDto.getType()).isNotNull();
        Assertions.assertThat(bikeDto.getBodySize()).isNotNull();
        Assertions.assertThat(bikeDto.getMaxLoad()).isNotNull();
        Assertions.assertThat(bikeDto.getRate()).isNotNull();
        Assertions.assertThat(bikeDto.getDescription()).isNotNull();
        Assertions.assertThat(bikeDto.getRatings()).isNotNull();
        Assertions.assertThat(bikeDto.getImageUrls()).isNotNull();
        Assertions.assertThat(bikeDto.getImageUrls()).isNotEmpty();
        Assertions.assertThat(bikeDto.getImageUrls()).hasSameSizeAs(bike.getImageUrls());

        Assertions.assertThat(bikeDto.getId()).isEqualTo(bike.getId());
        Assertions.assertThat(bikeDto.getName()).isEqualTo(bike.getName());
        Assertions.assertThat(bikeDto.getType()).isEqualTo(bike.getType());
        Assertions.assertThat(bikeDto.getBodySize()).isEqualTo(bike.getBodySize());
        Assertions.assertThat(bikeDto.getMaxLoad()).isEqualTo(bike.getMaxLoad());
        Assertions.assertThat(bikeDto.getRate()).isEqualTo(bike.getRate());
        Assertions.assertThat(bikeDto.getDescription()).isEqualTo(bike.getDescription());
        Assertions.assertThat(bikeDto.getRatings()).isEqualTo(bike.getRatings());
        List<String> bikeImageUrls = bike.getImageUrls().stream()
                .map(BikeImage::getUrl)
                .collect(Collectors.toList());
        Assertions.assertThat(bikeDto.getImageUrls()).hasSameElementsAs(bikeImageUrls);
    }
}