package com.example.test.controller;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.test.dto.user.UserAuthenticationRequestDto;
import com.example.test.dto.user.UserAuthenticationResponseDto;
import com.example.test.dto.user.UserRegistrationRequestDto;
import com.example.test.dto.user.UserRegistrationResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.shaded.org.apache.commons.lang3.builder.EqualsBuilder;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "classpath:database/user/delete-users.sql",
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class AuthenticationControllerTest {

    private static MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @SneakyThrows
    @BeforeAll
    static void beforeAll(
            @Autowired WebApplicationContext applicationContext
    ) {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .apply(springSecurity())
                .build();
    }

    @SneakyThrows
    @Test
    @DisplayName("Add new user with valid fields")
    void addUser_ValidCred_Success() {
        UserRegistrationRequestDto requestDto = new UserRegistrationRequestDto(
                "user1234567890",
                "password12345"
        );

        UserRegistrationResponseDto expected = new UserRegistrationResponseDto(
                1L,
                "user1234567890"
        );

        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        MvcResult result = mockMvc.perform(
                        post("/user/add")
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();

        UserRegistrationResponseDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), UserRegistrationResponseDto.class);

        assertNotNull(actual);
        assertNotNull(actual.id());
        Assertions.assertTrue(EqualsBuilder.reflectionEquals(expected, actual, "id"));
    }

    @SneakyThrows
    @Test
    @DisplayName("Add new user with invalid fields")
    void addUser_InvalidCred_Failure() {
        UserRegistrationRequestDto requestDto = new UserRegistrationRequestDto(
                "",
                ""
        );

        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        mockMvc.perform(
                        post("/user/add")
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().is4xxClientError());
    }

    @SneakyThrows
    @Test
    @DisplayName("Authenticate user and return jwt token")
    @Sql(scripts = "classpath:database/user/add-user.sql")
    void authenticateUser_ValidCred_Success() {
        UserAuthenticationRequestDto requestDto = new UserAuthenticationRequestDto(
                "user12345",
                "password12345"
        );

        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        MvcResult result = mockMvc.perform(
                        post("/user/authenticate")
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();

        UserAuthenticationResponseDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), UserAuthenticationResponseDto.class);

        assertNotNull(actual);
    }

    @SneakyThrows
    @Test
    @DisplayName("Authenticate user with invalid credentials")
    @Sql(scripts = "classpath:database/user/add-user.sql")
    void authenticateUser_InvalidCred_Failure() {
        UserAuthenticationRequestDto requestDto = new UserAuthenticationRequestDto(
                "user123456",
                "rgrgtrhrthrthrth"
        );

        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        mockMvc.perform(
                        post("/user/authenticate")
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().is4xxClientError());
    }
}
