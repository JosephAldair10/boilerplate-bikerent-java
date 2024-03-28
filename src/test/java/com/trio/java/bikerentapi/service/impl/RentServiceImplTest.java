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
import com.trio.java.bikerentapi.util.DtoDataProvider;
import com.trio.java.bikerentapi.util.RepositoryDataProvider;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RentServiceImplTest {

    private RentService rentService;

    private RentRepository rentRepository;

    private BikeService bikeService;

    private CustomerService customerService;

    @BeforeEach
    public void setUp() {
        rentRepository = Mockito.mock(RentRepository.class);
        bikeService = Mockito.mock(BikeService.class);
        customerService = Mockito.mock(CustomerService.class);
        rentService = new RentServiceImpl(rentRepository, bikeService, customerService);
    }

    @Test
    void shouldThrowAnErrorIfEndDateIsAfterStartDate() {
        RentDto rentDto = DtoDataProvider.getRentDto();
        rentDto.setStartDate(LocalDate.now());
        rentDto.setEndDate(LocalDate.now().minusDays(2));

        Assertions.assertThatThrownBy(() -> rentService.rent(rentDto))
                .isInstanceOf(InvalidRentDatesException.class);
    }

    @Test
    void shouldThrowAnErrorIfBikeIsNotFound() {
        RentDto rentDto = DtoDataProvider.getRentDto();
        Customer customer = RepositoryDataProvider.getCustomer();

        Mockito.when(bikeService.getBikeDetails(rentDto.getBike().getId()))
                .thenReturn(Optional.empty());
        Mockito.when(customerService.getCustomerDetails(rentDto.getCustomer().getId()))
                .thenReturn(Optional.of(customer));

        Assertions.assertThatThrownBy(() -> rentService.rent(rentDto))
                .isInstanceOf(BikeNotFoundException.class);
    }

    @Test
    void shouldThrowAnErrorIfCustomerIsNotFound() {
        RentDto rentDto = DtoDataProvider.getRentDto();
        Bike bike = RepositoryDataProvider.getBike();

        Mockito.when(bikeService.getBikeDetails(rentDto.getBike().getId()))
                .thenReturn(Optional.of(bike));
        Mockito.when(customerService.getCustomerDetails(rentDto.getCustomer().getId()))
                .thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> rentService.rent(rentDto))
                .isInstanceOf(CustomerNotFoundException.class);
    }

    @Test
    void shouldSaveRentIfThereAreNoRentsForThatBikeInSpecifiedPeriod() {
        RentDto rentDto = DtoDataProvider.getRentDto();
        rentDto.setStartDate(LocalDate.now());
        rentDto.setEndDate(LocalDate.now().plusDays(2));

        Bike bike = RepositoryDataProvider.getBike();
        Customer customer = RepositoryDataProvider.getCustomer();

        Mockito.when(bikeService.getBikeDetails(rentDto.getBike().getId()))
                .thenReturn(Optional.of(bike));
        Mockito.when(customerService.getCustomerDetails(rentDto.getCustomer().getId()))
                .thenReturn(Optional.of(customer));
        Mockito.when(rentRepository.getRentsByBikeAndStartDateAndEndDate(
                rentDto.getBike().getId(), rentDto.getStartDate(), rentDto.getEndDate()))
                .thenReturn(Lists.newArrayList());

        rentService.rent(rentDto);

        ArgumentCaptor<Rent> rentCaptor = ArgumentCaptor.forClass(Rent.class);
        Mockito.verify(rentRepository, Mockito.atMostOnce())
                .save(rentCaptor.capture());

        Rent rent = rentCaptor.getValue();
        Assertions.assertThat(rent).isNotNull();
        Assertions.assertThat(rent.getStartdate()).isNotNull();
        Assertions.assertThat(rent.getEnddate()).isNotNull();
        Assertions.assertThat(rent.getBike()).isNotNull();
        Assertions.assertThat(rent.getCustomer()).isNotNull();
        Assertions.assertThat(rent.getDays()).isNotNull();
        Assertions.assertThat(rent.getTotal()).isNotNull();
        Assertions.assertThat(rent.getFee()).isNotNull();

        Assertions.assertThat(rent.getStartdate()).isEqualTo(rentDto.getStartDate());
        Assertions.assertThat(rent.getEnddate()).isEqualTo(rentDto.getEndDate());
        Assertions.assertThat(rent.getBike().getId()).isEqualTo(bike.getId());
        Assertions.assertThat(rent.getCustomer().getId()).isEqualTo(customer.getId());
        Assertions.assertThat(rent.getDays()).isEqualTo(3);
        double total = bike.getRate() * 3;
        Assertions.assertThat(rent.getTotal()).isEqualTo(total);
        double fee = total * 0.15;
        Assertions.assertThat(rent.getFee()).isEqualTo(fee);
    }

    @Test
    void shouldThrowAnErrorIfThereIsRentForThatBikeWithSameStartDate() {
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = LocalDate.now().plusDays(5);

        RentDto input = DtoDataProvider.getRentDto();
        input.setStartDate(startDate);
        input.setEndDate(endDate);

        Bike bike = RepositoryDataProvider.getBike();
        Rent rent = RepositoryDataProvider.getRent();
        rent.setStartdate(startDate);
        rent.setEnddate(endDate.plusDays(4));
        Customer customer = RepositoryDataProvider.getCustomer();

        Mockito.when(bikeService.getBikeDetails(input.getBike().getId()))
                .thenReturn(Optional.of(bike));
        Mockito.when(customerService.getCustomerDetails(input.getCustomer().getId()))
                .thenReturn(Optional.of(customer));
        Mockito.when(rentRepository.getRentsByBikeAndStartDateAndEndDate(
                input.getBike().getId(), input.getStartDate(), input.getEndDate()))
                .thenReturn(Lists.newArrayList(rent));

        Assertions.assertThatThrownBy(() -> rentService.rent(input))
                .isInstanceOf(BikeNotAvailableException.class);
    }

    @Test
    void shouldThrowAnErrorIfThereIsRentForThatBikeWithRequestedStartDateIsBetweenRentDate() {
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = LocalDate.now().plusDays(5);

        RentDto input = DtoDataProvider.getRentDto();
        input.setStartDate(startDate.plusDays(2));
        input.setEndDate(endDate);

        Bike bike = RepositoryDataProvider.getBike();
        Rent rent = RepositoryDataProvider.getRent();
        rent.setStartdate(startDate);
        rent.setEnddate(endDate);
        Customer customer = RepositoryDataProvider.getCustomer();

        Mockito.when(bikeService.getBikeDetails(input.getBike().getId()))
                .thenReturn(Optional.of(bike));
        Mockito.when(customerService.getCustomerDetails(input.getCustomer().getId()))
                .thenReturn(Optional.of(customer));
        Mockito.when(rentRepository.getRentsByBikeAndStartDateAndEndDate(
                input.getBike().getId(), input.getStartDate(), input.getEndDate()))
                .thenReturn(Lists.newArrayList(rent));

        Assertions.assertThatThrownBy(() -> rentService.rent(input))
                .isInstanceOf(BikeNotAvailableException.class);
    }

    @Test
    void shouldThrowAnErrorIfThereIsRentForThatBikeWithRequestedEndDateIsBetweenRentDate() {
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = LocalDate.now().plusDays(5);

        RentDto input = DtoDataProvider.getRentDto();
        input.setStartDate(startDate.minusDays(2));
        input.setEndDate(endDate.minusDays(2));

        Bike bike = RepositoryDataProvider.getBike();
        Rent rent = RepositoryDataProvider.getRent();
        rent.setStartdate(startDate);
        rent.setEnddate(endDate);
        Customer customer = RepositoryDataProvider.getCustomer();

        Mockito.when(bikeService.getBikeDetails(input.getBike().getId()))
                .thenReturn(Optional.of(bike));
        Mockito.when(customerService.getCustomerDetails(input.getCustomer().getId()))
                .thenReturn(Optional.of(customer));
        Mockito.when(rentRepository.getRentsByBikeAndStartDateAndEndDate(
                input.getBike().getId(), input.getStartDate(), input.getEndDate()))
                .thenReturn(Lists.newArrayList(rent));

        Assertions.assertThatThrownBy(() -> rentService.rent(input))
                .isInstanceOf(BikeNotAvailableException.class);
    }

    @Test
    void shouldThrowAnErrorIfThereIsRentForThatBikeWithRequestedStartDateIsEqualToRentEndDate() {
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = LocalDate.now().plusDays(5);

        RentDto input = DtoDataProvider.getRentDto();
        input.setStartDate(endDate);
        input.setEndDate(endDate.plusDays(2));

        Bike bike = RepositoryDataProvider.getBike();
        Rent rent = RepositoryDataProvider.getRent();
        rent.setStartdate(startDate);
        rent.setEnddate(endDate);

        Customer customer = RepositoryDataProvider.getCustomer();
        Mockito.when(bikeService.getBikeDetails(input.getBike().getId()))
                .thenReturn(Optional.of(bike));
        Mockito.when(customerService.getCustomerDetails(input.getCustomer().getId()))
                .thenReturn(Optional.of(customer));
        Mockito.when(rentRepository.getRentsByBikeAndStartDateAndEndDate(
                input.getBike().getId(), input.getStartDate(), input.getEndDate()))
                .thenReturn(Lists.newArrayList(rent));

        Assertions.assertThatThrownBy(() -> rentService.rent(input))
                .isInstanceOf(BikeNotAvailableException.class);
    }
}