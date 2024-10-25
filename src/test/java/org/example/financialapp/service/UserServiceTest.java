package org.example.financialapp.service;

import org.assertj.core.api.Assertions;
import org.example.financialapp.user.domain.SimpleUser;
import org.example.financialapp.user.domain.User;
import org.example.financialapp.user.repository.SimpleUserRepository;
import org.example.financialapp.user.repository.UserRepository;
import org.example.financialapp.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private SimpleUserRepository simpleUserRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void userService_createUser_ReturnSavedUser() {
        
        SimpleUser user = new SimpleUser();
        user.setUsername("testuser");
        user.setPassword("password123");
        user.setCredit(100.0);
        user.setUserType(User.UserType.SIMPLE);

        when(userRepository.save(any(User.class))).thenReturn(user);

        
        User createdUser = userService.createUser(user);

        
        Assertions.assertThat(createdUser).isNotNull();
        Assertions.assertThat(createdUser.getUsername()).isEqualTo("testuser");
    }

    @Test
    void userService_updateUser_ReturnUpdatedUser() {
        
        SimpleUser existingUser = new SimpleUser();
        existingUser.setId(1L);
        existingUser.setUsername("testuser");
        existingUser.setPassword("password123");
        existingUser.setUserType(User.UserType.SIMPLE);

        SimpleUser updatedUser = new SimpleUser();
        updatedUser.setUsername("updateduser");
        updatedUser.setPassword("newpassword");

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenReturn(existingUser);

        
        User result = userService.updateUser(1L, updatedUser);

        
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getUsername()).isEqualTo("updateduser");
        Assertions.assertThat(result.getPassword()).isEqualTo("newpassword");
    }

    @Test
    void userService_deleteUser_ShouldNotThrowException() {
        
        Long userId = 1L;

        
        Assertions.assertThatCode(() -> userService.deleteUser(userId)).doesNotThrowAnyException();
    }

    @Test
    void userService_getAllUsers_ReturnListOfUsers() {
        
        SimpleUser user1 = new SimpleUser();
        user1.setUsername("testuser1");
        SimpleUser user2 = new SimpleUser();
        user2.setUsername("testuser2");

        when(userRepository.findAll()).thenReturn(List.of(user1, user2));

        
        List<User> users = userService.getAllUsers();

        
        Assertions.assertThat(users).isNotNull();
        Assertions.assertThat(users.size()).isEqualTo(2);
    }

    @Test
    void userService_getUserById_ReturnUser() {
        
        SimpleUser user = new SimpleUser();
        user.setId(1L);
        user.setUsername("testuser");

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        
        Optional<User> foundUser = userService.getUserById(1L);

        
        Assertions.assertThat(foundUser).isPresent();
        Assertions.assertThat(foundUser.get().getUsername()).isEqualTo("testuser");
    }

    @Test
    void userService_updateCredit_ReturnUpdatedCredit() {
        
        SimpleUser user = new SimpleUser();
        user.setId(1L);
        user.setUsername("testuser");
        user.setCredit(100.0);
        user.setUserType(User.UserType.SIMPLE);

        when(simpleUserRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(simpleUserRepository.save(any(SimpleUser.class))).thenReturn(user);

        
        SimpleUser updatedUser = userService.updateCredit(1L, 50.0);

        
        Assertions.assertThat(updatedUser).isNotNull();
        Assertions.assertThat(updatedUser.getCredit()).isEqualTo(150.0);
    }

    @Test
    void userService_updateCredit_ShouldThrowExceptionForNegativeCredit() {
        
        SimpleUser user = new SimpleUser();
        user.setId(1L);
        user.setCredit(100.0);

        when(simpleUserRepository.findById(anyLong())).thenReturn(Optional.of(user));

        
        Assertions.assertThatThrownBy(() -> userService.updateCredit(1L, -50.0))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("credit can not be negative");
    }
}

