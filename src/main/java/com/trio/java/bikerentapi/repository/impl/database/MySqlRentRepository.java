package com.trio.java.bikerentapi.repository.impl.database;

import com.trio.java.bikerentapi.data.Rent;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MySqlRentRepository extends JpaRepository<Rent, Integer> {

    List<Rent> getRentsByBikeIdAndStartdateGreaterThanEqualOrEnddateLessThanEqual(Integer id, LocalDate startDate, LocalDate endDate);
}
