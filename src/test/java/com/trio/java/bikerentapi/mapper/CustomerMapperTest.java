package com.trio.java.bikerentapi.mapper;

import com.trio.java.bikerentapi.data.Customer;
import com.trio.java.bikerentapi.dto.CustomerDto;
import com.trio.java.bikerentapi.util.RepositoryDataProvider;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class CustomerMapperTest {

    @Test
    void shouldReturnCustomerDto() {
        Customer customer = RepositoryDataProvider.getCustomer();

        CustomerDto customerDto = CustomerMapper.fromCustomer(customer);

        Assertions.assertThat(customerDto).isNotNull();
        Assertions.assertThat(customerDto.getId()).isNotNull();
        Assertions.assertThat(customerDto.getName()).isNotNull();

        Assertions.assertThat(customerDto.getId()).isEqualTo(customer.getId());
        Assertions.assertThat(customerDto.getName()).isEqualTo(customer.getName());
    }
}