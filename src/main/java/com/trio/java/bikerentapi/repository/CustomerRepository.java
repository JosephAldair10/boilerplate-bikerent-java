package com.trio.java.bikerentapi.repository;

import com.trio.java.bikerentapi.data.Customer;
import java.util.Optional;

public interface CustomerRepository {

    Optional<Customer> getCustomer(int id);
}
