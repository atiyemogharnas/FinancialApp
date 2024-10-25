package org.example.financialapp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.financialapp.user.controller.UserController;
import org.example.financialapp.user.domain.SimpleUser;
import org.example.financialapp.user.domain.User;
import org.example.financialapp.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = UserController.class)
@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void userController_createUser_ReturnCreatedUser() throws Exception {
        User user = new SimpleUser();
        user.setUsername("testuser");
        user.setPassword("password123");

        when(userService.createUser(any(User.class))).thenReturn(user);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/users/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("testuser"));
    }

    @Test
    void userController_updateUser_ReturnUpdatedUser() throws Exception {
        User user = new SimpleUser();
        user.setUsername("updateduser");
        user.setPassword("newpassword");

        when(userService.updateUser(anyLong(), any(User.class))).thenReturn(user);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/users/update/{userId}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("updateduser"));
    }

    @Test
    void userController_deleteUser_ReturnNoContent() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/users/delete/{userId}", 1))
                .andExpect(status().isNoContent());
    }

    @Test
    void userController_getAllUsers_ReturnUserList() throws Exception {
        User user1 = new SimpleUser();
        user1.setUsername("testuser1");
        User user2 = new SimpleUser();
        user2.setUsername("testuser2");

        when(userService.getAllUsers()).thenReturn(List.of(user1, user2));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/users/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].username").value("testuser1"))
                .andExpect(jsonPath("$[1].username").value("testuser2"));
    }

    @Test
    void userController_getUserById_ReturnUser() throws Exception {
        User user = new SimpleUser();
        user.setUsername("testuser");

        when(userService.getUserById(anyLong())).thenReturn(Optional.of(user));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/users/{userId}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("testuser"));
    }

    @Test
    void userController_updateCredit_ReturnUpdatedCredit() throws Exception {
        SimpleUser user = new SimpleUser();
        user.setUsername("testuser");
        user.setCredit(150.0);

        when(userService.updateCredit(anyLong(), Mockito.anyDouble())).thenReturn(user);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/users/credit/{userId}", 1)
                        .param("additionalCredit", "50.0")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.credit").value(150.0));
    }

    @Test
    void userController_getAllSimpleUsers_ReturnSimpleUserList() throws Exception {
        SimpleUser simpleUser1 = new SimpleUser();
        simpleUser1.setUsername("simpleUser1");
        SimpleUser simpleUser2 = new SimpleUser();
        simpleUser2.setUsername("simpleUser2");

        when(userService.getAllSimpleUsers()).thenReturn(List.of(simpleUser1, simpleUser2));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/users/simple"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].username").value("simpleUser1"))
                .andExpect(jsonPath("$[1].username").value("simpleUser2"));
    }
}

