package com.trio.java.bikerentapi.controller;

import com.trio.java.bikerentapi.dto.RentDto;
import com.trio.java.bikerentapi.exception.BikeNotAvailableException;
import com.trio.java.bikerentapi.exception.BikeNotFoundException;
import com.trio.java.bikerentapi.exception.CustomerNotFoundException;
import com.trio.java.bikerentapi.exception.InvalidRentDatesException;
import com.trio.java.bikerentapi.service.RentService;
import com.trio.java.bikerentapi.util.ObjectMapperHelper;
import com.trio.java.bikerentapi.util.RepositoryDataProvider;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = RentController.class)
class RentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RentService rentService;

    @Test
    void shouldThrowInvalidRentDatesExceptionIfEndDateIsAfterStartDate() throws Exception {
        Mockito.when(rentService.rent(ArgumentMatchers.any(RentDto.class)))
                .thenThrow(InvalidRentDatesException.class);

        this.mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/rent")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(ObjectMapperHelper.getInstance()
                                        .getFileContent("rent/rentDto.json"))
                )
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(result -> Assertions.assertTrue(result.getResolvedException()
                        instanceof InvalidRentDatesException))
                .andExpect(result -> Assertions.assertEquals("Dates are not valid",
                        result.getResponse().getErrorMessage()));
    }

    @Test
    void shouldThrowCustomerNotFoundExceptionIfCustomerIsNotPresentInDatabase() throws Exception {
        Mockito.when(rentService.rent(ArgumentMatchers.any(RentDto.class)))
                .thenThrow(CustomerNotFoundException.class);

        this.mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/rent")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(ObjectMapperHelper.getInstance()
                                        .getFileContent("rent/rentDto.json"))
                )
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(result -> Assertions.assertTrue(result.getResolvedException()
                        instanceof CustomerNotFoundException))
                .andExpect(result -> Assertions.assertEquals("Customer not found",
                        result.getResponse().getErrorMessage()));
    }

    @Test
    void shouldThrowCustomerNotFoundExceptionIfBikeIsNotPresentInDatabase() throws Exception {
        Mockito.when(rentService.rent(ArgumentMatchers.any(RentDto.class)))
                .thenThrow(BikeNotFoundException.class);

        this.mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/rent")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(ObjectMapperHelper.getInstance()
                                        .getFileContent("rent/rentDto.json"))
                )
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(result -> Assertions.assertTrue(result.getResolvedException()
                        instanceof BikeNotFoundException))
                .andExpect(result -> Assertions.assertEquals("Bike not found",
                        result.getResponse().getErrorMessage()));
    }

    @Test
    void shouldThrowBikeNotAvailableExceptionIfBikeIsAlreadyRented() throws Exception {
        Mockito.when(rentService.rent(ArgumentMatchers.any(RentDto.class)))
                .thenThrow(BikeNotAvailableException.class);

        this.mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/rent")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(ObjectMapperHelper.getInstance()
                                        .getFileContent("rent/rentDto.json"))
                )
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(result -> Assertions.assertTrue(result.getResolvedException()
                        instanceof BikeNotAvailableException))
                .andExpect(result -> Assertions.assertEquals(
                        "Bike not available for selected dates",
                        result.getResponse().getErrorMessage()));
    }

    @Test
    void shouldReturnResourceCreatedIfBikeIsAvailable() throws Exception {
        Mockito.when(rentService.rent(ArgumentMatchers.any(RentDto.class)))
                .thenReturn(RepositoryDataProvider.getRent());

        this.mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/rent")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(ObjectMapperHelper.getInstance()
                                        .getFileContent("rent/rentDto.json"))
                )
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.notNullValue()));
    }
}