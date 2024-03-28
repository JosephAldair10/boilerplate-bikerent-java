package com.trio.java.bikerentapi.repository.impl.database;

import com.trio.java.bikerentapi.data.Rent;
import com.trio.java.bikerentapi.repository.RentRepository;
import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class DatabaseRentRepository implements RentRepository {

    @Autowired
    private MySqlRentRepository db;

    @Override
    public List<Rent> getRentsByBikeAndStartDateAndEndDate(
            Integer id, LocalDate startDate, LocalDate endDate) {
        return db.getRentsByBikeIdAndStartdateGreaterThanEqualOrEnddateLessThanEqual(
                id, startDate, endDate);
    }

    @Override
    public Rent save(Rent rent) {
        return db.save(rent);
    }
}
