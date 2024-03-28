package com.trio.java.bikerentapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class BikeRentApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(BikeRentApiApplication.class, args);
    }

}
