package com.trio.java.bikerentapi.repository.impl.database;

import com.trio.java.bikerentapi.data.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MySqlCustomerRepository extends JpaRepository<Customer, Integer> {

}
