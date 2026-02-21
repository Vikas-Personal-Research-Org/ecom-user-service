package com.ecom.user.controller;

import com.ecom.user.dto.LoginRequest;
import com.ecom.user.dto.LoginResponse;
import com.ecom.user.dto.RegisterRequest;
import com.ecom.user.dto.UserResponse;
import com.ecom.user.exception.UserAlreadyExistsException;
import com.ecom.user.exception.UserNotFoundException;
import com.ecom.user.model.Role;
import com.ecom.user.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void register_ShouldReturnCreated() throws Exception {
        RegisterRequest request = new RegisterRequest("test@example.com", "password123", "John", "Doe", Role.BUYER);
        UserResponse response = new UserResponse(1L, "test@example.com", "John", "Doe", Role.BUYER, LocalDateTime.now());

        when(userService.register(any(RegisterRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.role").value("BUYER"));
    }

    @Test
    void register_ShouldReturnConflict_WhenUserExists() throws Exception {
        RegisterRequest request = new RegisterRequest("test@example.com", "password123", "John", "Doe", Role.BUYER);

        when(userService.register(any(RegisterRequest.class)))
                .thenThrow(new UserAlreadyExistsException("User with email test@example.com already exists"));

        mockMvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict());
    }

    @Test
    void login_ShouldReturnOk() throws Exception {
        LoginRequest request = new LoginRequest("test@example.com", "password123");
        LoginResponse response = new LoginResponse("jwt-token", "test@example.com", Role.BUYER);

        when(userService.login(any(LoginRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("jwt-token"))
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.role").value("BUYER"));
    }

    @Test
    void getUserById_ShouldReturnOk() throws Exception {
        UserResponse response = new UserResponse(1L, "test@example.com", "John", "Doe", Role.BUYER, LocalDateTime.now());

        when(userService.getUserById(1L)).thenReturn(response);

        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.email").value("test@example.com"));
    }

    @Test
    void getUserById_ShouldReturnNotFound() throws Exception {
        when(userService.getUserById(99L)).thenThrow(new UserNotFoundException("User not found with id: 99"));

        mockMvc.perform(get("/api/users/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getUserByEmail_ShouldReturnOk() throws Exception {
        UserResponse response = new UserResponse(1L, "test@example.com", "John", "Doe", Role.BUYER, LocalDateTime.now());

        when(userService.getUserByEmail("test@example.com")).thenReturn(response);

        mockMvc.perform(get("/api/users/email/test@example.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("test@example.com"));
    }

    @Test
    void updateUser_ShouldReturnOk() throws Exception {
        RegisterRequest request = new RegisterRequest("updated@example.com", "newpass", "Jane", "Smith", Role.SELLER);
        UserResponse response = new UserResponse(1L, "updated@example.com", "Jane", "Smith", Role.SELLER, LocalDateTime.now());

        when(userService.updateUser(eq(1L), any(RegisterRequest.class))).thenReturn(response);

        mockMvc.perform(put("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("updated@example.com"))
                .andExpect(jsonPath("$.firstName").value("Jane"));
    }
}
