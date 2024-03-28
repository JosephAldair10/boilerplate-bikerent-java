package com.trio.java.bikerentapi.repository.impl.database;

import com.trio.java.bikerentapi.data.Bike;
import com.trio.java.bikerentapi.repository.BikeRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

public class DatabaseBikeRepository implements BikeRepository {

    @Autowired
    private MySqlBikeRepository db;

    @Override
    public List<Bike> getAllBikes() {
        return db.findAll();
    }

    @Override
    public Optional<Bike> getBikeDetails(int id) {
        return db.findById(id);
    }
}
