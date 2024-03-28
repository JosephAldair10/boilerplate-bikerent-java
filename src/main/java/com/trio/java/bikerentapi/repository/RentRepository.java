package com.trio.java.bikerentapi.repository;

import com.trio.java.bikerentapi.data.Rent;
import java.time.LocalDate;
import java.util.List;

public interface RentRepository {

    List<Rent> getRentsByBikeAndStartDateAndEndDate(
            Integer id, LocalDate startDate, LocalDate endDate);

    Rent save(Rent rent);
}
