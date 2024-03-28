package com.trio.java.bikerentapi.e2e;

import com.trio.java.bikerentapi.data.Rent;
import com.trio.java.bikerentapi.dto.RentDto;
import com.trio.java.bikerentapi.exception.BikeNotAvailableException;
import com.trio.java.bikerentapi.exception.BikeNotFoundException;
import com.trio.java.bikerentapi.exception.CustomerNotFoundException;
import com.trio.java.bikerentapi.exception.InvalidRentDatesException;
import com.trio.java.bikerentapi.repository.impl.database.MySqlRentRepository;
import com.trio.java.bikerentapi.util.DtoDataProvider;
import com.trio.java.bikerentapi.util.ObjectMapperHelper;
import java.time.LocalDate;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:test-application.properties")
@Transactional
class RentIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MySqlRentRepository mySqlRentRepository;

    @Test
    void shouldThrowCustomerNotFoundIfCustomerIsNotInDatabase() throws Exception {
        RentDto rentDto = DtoDataProvider.getRentDto();
        rentDto.getCustomer().setId(100);

        this.mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/rent")
                                .content(ObjectMapperHelper.getInstance()
                                        .convertObjectToString(rentDto))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(result -> Assertions.assertThat(result.getResolvedException())
                        .isInstanceOf(CustomerNotFoundException.class))
                .andExpect(result -> Assertions.assertThat("Customer not found")
                        .isEqualTo(result.getResponse().getErrorMessage()));
    }

    @Test
    void shouldThrowBikeNotFoundIfBikeIsNotInDatabase() throws Exception {
        RentDto rentDto = DtoDataProvider.getRentDto();
        rentDto.getBike().setId(100);

        this.mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/rent")
                                .content(ObjectMapperHelper.getInstance()
                                        .convertObjectToString(rentDto))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(result -> Assertions.assertThat(result.getResolvedException())
                                .isInstanceOf(BikeNotFoundException.class))
                .andExpect(result -> Assertions.assertThat("Bike not found")
                        .isEqualTo(result.getResponse().getErrorMessage()));
    }

    @Test
    void shouldThrowInvalidDatesIfEndDateIsBeforeStartDate() throws Exception {
        RentDto rentDto = DtoDataProvider.getRentDto();
        rentDto.setEndDate(LocalDate.now().minusDays(2));
        rentDto.setStartDate(LocalDate.now());

        this.mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/rent")
                                .content(ObjectMapperHelper.getInstance()
                                        .convertObjectToString(rentDto))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(result -> Assertions.assertThat(result.getResolvedException())
                        .isInstanceOf(InvalidRentDatesException.class))
                .andExpect(result -> Assertions.assertThat("Dates are not valid")
                        .isEqualTo(result.getResponse().getErrorMessage()));
    }

    @Test
    void shouldCreateRentIfDataIsCorrectAndBikeIsAvailable() throws Exception {
        RentDto rentDto = DtoDataProvider.getRentDto();

        MvcResult mvcResult = this.mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/rent")
                                .content(ObjectMapperHelper.getInstance()
                                        .convertObjectToString(rentDto))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id",
                        Matchers.notNullValue())).andReturn();

        RentDto rentCreated = ObjectMapperHelper.getInstance()
                .converContentToObject(mvcResult.getResponse().getContentAsString(),
                        RentDto.class);
        Rent databaseRent = getRentById(rentCreated.getId());

        Assertions.assertThat(rentCreated.getId()).isEqualTo(databaseRent.getId());
        Assertions.assertThat(rentCreated.getCustomer().getId())
                .isEqualTo(databaseRent.getCustomer().getId());
        Assertions.assertThat(rentCreated.getCustomer().getName())
                .isEqualTo(databaseRent.getCustomer().getName());
        Assertions.assertThat(rentCreated.getStartDate())
                .isEqualTo(databaseRent.getStartdate());
        Assertions.assertThat(rentCreated.getEndDate())
                .isEqualTo(databaseRent.getEnddate());
        Assertions.assertThat(rentCreated.getFee())
                .isEqualTo(databaseRent.getFee());
        Assertions.assertThat(rentCreated.getTotal())
                .isEqualTo(databaseRent.getTotal());
        Assertions.assertThat(rentCreated.getTotalDays())
                .isEqualTo(databaseRent.getDays());
        Assertions.assertThat(rentCreated.getBike().getId())
                .isEqualTo(databaseRent.getBike().getId());
    }

    @Test
    void shouldThrowBikeNotAvailableIfBikeIsRentedForSameStartAndEndDate()
            throws Exception {
        RentDto rentDto = DtoDataProvider.getRentDto();
        createRent(rentDto);

        this.mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/rent")
                                .content(ObjectMapperHelper.getInstance()
                                        .convertObjectToString(rentDto))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(result -> Assertions.assertThat(result.getResolvedException())
                                .isInstanceOf(BikeNotAvailableException.class))
                .andExpect(result -> Assertions.assertThat(
                        "Bike not available for selected dates")
                                .isEqualTo(result.getResponse().getErrorMessage()));
    }

    @Test
    void shouldThrowBikeNotAvailableIfRequestedStartDateIsBetweenBikeRentedDate()
            throws Exception {
        RentDto rentDto = DtoDataProvider.getRentDto();
        rentDto.setStartDate(LocalDate.now());
        rentDto.setEndDate(LocalDate.now().plusDays(5));
        createRent(rentDto);

        RentDto input = DtoDataProvider.getRentDto();
        input.setStartDate(LocalDate.now().plusDays(2));
        input.setEndDate(LocalDate.now().plusDays(5));

        this.mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/rent")
                                .content(ObjectMapperHelper.getInstance()
                                        .convertObjectToString(input))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(result -> Assertions.assertThat(result.getResolvedException())
                        .isInstanceOf(BikeNotAvailableException.class))
                .andExpect(result -> Assertions.assertThat(
                        "Bike not available for selected dates")
                        .isEqualTo(result.getResponse().getErrorMessage()));
    }

    @Test
    void shouldThrowBikeNotAvailableIfRequestedRequestedEndDateIsBetweenBikeRentedDate()
            throws Exception {
        RentDto rentDto = DtoDataProvider.getRentDto();
        rentDto.setStartDate(LocalDate.now());
        rentDto.setEndDate(LocalDate.now().plusDays(5));
        createRent(rentDto);

        RentDto input = DtoDataProvider.getRentDto();
        input.setStartDate(LocalDate.now().minusDays(2));
        input.setEndDate(LocalDate.now().plusDays(3));

        this.mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/rent")
                                .content(ObjectMapperHelper.getInstance()
                                        .convertObjectToString(input))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(result -> Assertions.assertThat(result.getResolvedException())
                        .isInstanceOf(BikeNotAvailableException.class))
                .andExpect(result -> Assertions.assertThat(
                        "Bike not available for selected dates")
                                .isEqualTo(result.getResponse().getErrorMessage()));
    }

    @Test
    void shouldThrowBikeNotAvailableIfBikeWithRequestedStartDateEqualToRentedEndDate()
            throws Exception {
        RentDto rentDto = DtoDataProvider.getRentDto();
        rentDto.setStartDate(LocalDate.now());
        rentDto.setEndDate(LocalDate.now().plusDays(5));
        createRent(rentDto);

        RentDto input = DtoDataProvider.getRentDto();
        input.setStartDate(LocalDate.now().plusDays(5));
        input.setEndDate(LocalDate.now().plusDays(7));

        this.mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/rent")
                                .content(ObjectMapperHelper.getInstance()
                                        .convertObjectToString(input))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(result -> Assertions.assertThat(result.getResolvedException())
                        .isInstanceOf(BikeNotAvailableException.class))
                .andExpect(result -> Assertions.assertThat(
                        "Bike not available for selected dates")
                        .isEqualTo(result.getResponse().getErrorMessage()));
    }

    private void createRent(RentDto rentDto) throws Exception {
        this.mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/rent")
                                .content(ObjectMapperHelper.getInstance()
                                        .convertObjectToString(rentDto))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id",
                        Matchers.notNullValue()));
    }

    private Rent getRentById(int id) {
        Optional<Rent> rent = mySqlRentRepository.findById(id);
        return rent.orElse(null);
    }
}
