package com.trio.java.bikerentapi.service.impl;

import com.trio.java.bikerentapi.data.Customer;
import com.trio.java.bikerentapi.repository.CustomerRepository;
import com.trio.java.bikerentapi.service.CustomerService;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service("customerService")
public class CustomerServiceImpl implements CustomerService {

    private CustomerRepository customerRepository;

    public CustomerServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public Optional<Customer> getCustomerDetails(int id) {
        return customerRepository.getCustomer(id);
    }
}
