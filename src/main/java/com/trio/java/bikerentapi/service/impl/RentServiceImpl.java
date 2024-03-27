package com.trio.java.bikerentapi.service.impl;

import com.trio.java.bikerentapi.data.Bike;
import com.trio.java.bikerentapi.data.Customer;
import com.trio.java.bikerentapi.data.Rent;
import com.trio.java.bikerentapi.dto.RentDto;
import com.trio.java.bikerentapi.exception.BikeNotAvailableException;
import com.trio.java.bikerentapi.exception.BikeNotFoundException;
import com.trio.java.bikerentapi.exception.CustomerNotFoundException;
import com.trio.java.bikerentapi.exception.InvalidRentDatesException;
import com.trio.java.bikerentapi.repository.RentRepository;
import com.trio.java.bikerentapi.service.BikeService;
import com.trio.java.bikerentapi.service.CustomerService;
import com.trio.java.bikerentapi.service.RentService;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Service("rentService")
public class RentServiceImpl implements RentService {

    private static double RENT_FEE = 0.15;

    private RentRepository rentRepository;

    private BikeService bikeService;

    private CustomerService customerService;

    public RentServiceImpl(RentRepository rentRepository, BikeService bikeService,
                           CustomerService customerService) {
        this.rentRepository = rentRepository;
        this.bikeService = bikeService;
        this.customerService = customerService;
    }

    @Override
    public Rent rent(RentDto rentDto) {
        validRentDates(rentDto.getStartDate(), rentDto.getEndDate());
        Optional<Bike> actualBike = bikeService.getBikeDetails(rentDto.getBike().getId());
        Optional<Customer> actualCustomer = customerService.getCustomerDetails(
                rentDto.getCustomer().getId());
        if (!actualBike.isPresent()) {
            throw new BikeNotFoundException();
        }

        if (!actualCustomer.isPresent()) {
            throw new CustomerNotFoundException();
        }

        Bike bike = actualBike.get();
        Customer customer = actualCustomer.get();
        LocalDate rentStartDate = rentDto.getStartDate();
        LocalDate rentEndDate = rentDto.getEndDate();
        List<Rent> bikesRented = rentRepository.getRentsByBikeAndStartDateAndEndDate(bike.getId(),
                rentStartDate, rentEndDate);
        if (isAvailableToRent(bikesRented, rentStartDate, rentEndDate)) {
            return createRent(bike, customer, rentStartDate, rentEndDate);
        } else {
            throw new BikeNotAvailableException();
        }
    }

    private void validRentDates(LocalDate startDate, LocalDate endDate) {
        if (endDate.isBefore(startDate)) {
            throw new InvalidRentDatesException();
        }
    }

    private boolean isAvailableToRent(List<Rent> bikesRented, LocalDate rentStartDate, LocalDate rentEndDate) {
        if (CollectionUtils.isEmpty(bikesRented)) {
            return Boolean.TRUE;
        }

        return bikesRented.stream().noneMatch(rent -> rentStartDate.isEqual(rent.getStartdate()) ||
                rentStartDate.isEqual(rent.getEnddate()) ||
                (rentStartDate.isAfter(rent.getStartdate()) && rentStartDate.isBefore(rent.getEnddate())) ||
                (rentEndDate.isAfter(rent.getStartdate()) && rentEndDate.isBefore(rent.getEnddate())));
    }

    private Rent createRent(Bike bike, Customer customer, LocalDate startDate, LocalDate endDate) {
        int days = Long.valueOf(ChronoUnit.DAYS.between(startDate, endDate)).intValue();
        double total = BigDecimal.valueOf(bike.getRate() * days).setScale(2, RoundingMode.HALF_UP).doubleValue();
        double fee = BigDecimal.valueOf(total * RENT_FEE).setScale(2, RoundingMode.HALF_UP).doubleValue();
        Rent rent = new Rent();
        rent.setBike(bike);
        rent.setCustomer(customer);
        rent.setStartdate(startDate);
        rent.setEnddate(endDate);
        rent.setDays(days);
        rent.setTotal(total);
        rent.setFee(fee);
        return rentRepository.save(rent);
    }
}
