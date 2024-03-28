package com.trio.java.bikerentapi.util;

import com.trio.java.bikerentapi.data.Bike;
import com.trio.java.bikerentapi.data.Customer;
import com.trio.java.bikerentapi.data.Rent;

public final class RepositoryDataProvider {

    private RepositoryDataProvider() {
    }

    public static Bike getBike() {
        return ObjectMapperHelper.getInstance().converFileToObject("bike/bike.json",
                Bike.class);
    }

    public static Customer getCustomer() {
        return ObjectMapperHelper.getInstance().converFileToObject("customer/customer.json",
                Customer.class);
    }

    public static Rent getRent() {
        return ObjectMapperHelper.getInstance().converFileToObject("rent/rent.json",
                Rent.class);
    }
}
