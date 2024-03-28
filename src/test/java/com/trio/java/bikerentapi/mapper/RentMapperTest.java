package com.trio.java.bikerentapi.mapper;

import com.trio.java.bikerentapi.data.Rent;
import com.trio.java.bikerentapi.dto.RentDto;
import com.trio.java.bikerentapi.util.RepositoryDataProvider;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class RentMapperTest {

    @Test
    void shouldReturnRentDto() {
        Rent rent = RepositoryDataProvider.getRent();

        RentDto rentDto = RentMapper.fromRent(rent);

        Assertions.assertThat(rentDto).isNotNull();
        Assertions.assertThat(rentDto.getId()).isNotNull();
        Assertions.assertThat(rentDto.getCustomer()).isNotNull();
        Assertions.assertThat(rentDto.getBike()).isNotNull();
        Assertions.assertThat(rentDto.getEndDate()).isNotNull();
        Assertions.assertThat(rentDto.getStartDate()).isNotNull();

        Assertions.assertThat(rentDto.getId()).isEqualTo(rent.getId());
        Assertions.assertThat(rentDto.getStartDate()).isEqualTo(rent.getStartdate());
        Assertions.assertThat(rentDto.getEndDate()).isEqualTo(rent.getEnddate());
        Assertions.assertThat(rentDto.getTotalDays()).isEqualTo(rent.getDays());
        Assertions.assertThat(rentDto.getFee()).isEqualTo(rent.getFee());
        Assertions.assertThat(rentDto.getTotal()).isEqualTo(rent.getTotal());
    }
}