package com.trio.java.bikerentapi.repository.impl.database;

import com.trio.java.bikerentapi.data.Customer;
import com.trio.java.bikerentapi.repository.CustomerRepository;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class DatabaseCustomerRepository implements CustomerRepository {

    @Autowired
    private MySqlCustomerRepository db;

    @Override
    public Optional<Customer> getCustomer(int id) {
        return db.findById(id);
    }
}
