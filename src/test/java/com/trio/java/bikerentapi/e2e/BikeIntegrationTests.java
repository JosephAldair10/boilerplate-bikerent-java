package com.trio.java.bikerentapi.e2e;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:test-application.properties")
class IntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldReturnBikes() throws Exception {
        this.mockMvc.perform(
            get("/api/bikes")
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.length()", Matchers.greaterThan(0)));
    }

    @Test
    void shouldReturnBikeDetails() throws Exception {
        int id = 1;

        this.mockMvc.perform(
            get(String.format("/api/bikes/%s", id))
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id", equalTo(id)))
        .andExpect(jsonPath("$.name", equalTo("Monahan and Sons")));
    }

    @Test
    void shouldReturnErrorForUnknownBike() throws Exception {
        int id = 404;
        this.mockMvc.perform(
            get(String.format("/api/bikes/%s", id))
        )
        .andExpect(status().isNotFound());
    }
}
