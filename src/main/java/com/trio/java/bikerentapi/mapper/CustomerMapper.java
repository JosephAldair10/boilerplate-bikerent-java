package com.trio.java.bikerentapi.mapper;

import com.trio.java.bikerentapi.data.Customer;
import com.trio.java.bikerentapi.dto.CustomerDto;

public class CustomerMapper {

    public static CustomerDto fromCustomer(Customer customer) {
        return CustomerDto.builder()
                .withId(customer.getId())
                .withName(customer.getName())
                .build();
    }
}
