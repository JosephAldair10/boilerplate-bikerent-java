package com.trio.java.bikerentapi.service;

import com.trio.java.bikerentapi.data.Rent;
import com.trio.java.bikerentapi.dto.RentDto;

public interface RentService {

    Rent rent(RentDto rent);
}
