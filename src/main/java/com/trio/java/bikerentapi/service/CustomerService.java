package com.trio.java.bikerentapi.service;

import com.trio.java.bikerentapi.data.Customer;
import java.util.Optional;

public interface CustomerService {

    Optional<Customer> getCustomerDetails(int id);
}
