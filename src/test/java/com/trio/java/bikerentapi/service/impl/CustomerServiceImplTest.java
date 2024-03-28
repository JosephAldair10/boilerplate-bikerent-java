package com.trio.java.bikerentapi.service.impl;

import com.trio.java.bikerentapi.data.Customer;
import com.trio.java.bikerentapi.repository.CustomerRepository;
import com.trio.java.bikerentapi.service.CustomerService;
import com.trio.java.bikerentapi.util.RepositoryDataProvider;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CustomerServiceImplTest {

    private CustomerService customerService;

    private CustomerRepository customerRepository;

    @BeforeEach
    public void setUp() {
        customerRepository = Mockito.mock(CustomerRepository.class);
        customerService = new CustomerServiceImpl(customerRepository);
    }

    @Test
    void shouldReturnCustomerDetails() {
        Mockito.when(customerRepository.getCustomer(ArgumentMatchers.anyInt()))
                .thenReturn(Optional.of(RepositoryDataProvider.getCustomer()));

        Optional<Customer> customer = customerService.getCustomerDetails(1);

        Assertions.assertThat(customer.isPresent()).isTrue();
        Customer data = customer.get();
        Assertions.assertThat(data).isNotNull();
        Assertions.assertThat(data.getId()).isNotNull();
        Assertions.assertThat(data.getName()).isNotNull();
    }

    @Test
    void shouldReturnEmptyIfCustomerDoesNotExist() {
        Mockito.when(customerRepository.getCustomer(ArgumentMatchers.anyInt()))
                .thenReturn(Optional.empty());

        Optional<Customer> customer = customerService.getCustomerDetails(1);

        Assertions.assertThat(customer.isPresent()).isFalse();
    }
}