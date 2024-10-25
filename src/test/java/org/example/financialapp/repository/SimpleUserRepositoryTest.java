package org.example.financialapp.repository;

import org.assertj.core.api.Assertions;
import org.example.financialapp.user.domain.SimpleUser;
import org.example.financialapp.user.domain.User;
import org.example.financialapp.user.repository.SimpleUserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class SimpleUserRepositoryTest {

    @Autowired
    private SimpleUserRepository simpleUserRepository;

    @Test
    void simpleUserRepository_SaveUser_ReturnSavedUser() {

        SimpleUser user = new SimpleUser();
        user.setUsername("testuser");
        user.setPassword("password123");
        user.setCredit(100.0);
        user.setUserType(User.UserType.SIMPLE);

        SimpleUser savedUser = simpleUserRepository.save(user);

        Assertions.assertThat(savedUser).isNotNull();
        Assertions.assertThat(savedUser.getId()).isGreaterThan(0);
        Assertions.assertThat(savedUser.getUsername()).isEqualTo("testuser");
        Assertions.assertThat(savedUser.getCredit()).isEqualTo(100.0);
        Assertions.assertThat(savedUser.getUserType()).isEqualTo(User.UserType.SIMPLE);
    }

    @Test
    void simpleUserRepository_GetAll_ReturnMoreThanOneUser() {

        SimpleUser user1 = new SimpleUser();
        user1.setUsername("testuser1");
        user1.setPassword("password123");
        user1.setCredit(100.0);
        user1.setUserType(User.UserType.SIMPLE);

        SimpleUser user2 = new SimpleUser();
        user2.setUsername("testuser2");
        user2.setPassword("password456");
        user2.setCredit(200.0);
        user2.setUserType(User.UserType.SIMPLE);

        simpleUserRepository.saveAll(List.of(user1, user2));

        List<SimpleUser> users = simpleUserRepository.findAll();

        Assertions.assertThat(users).isNotNull();
        Assertions.assertThat(users.size()).isEqualTo(2);
    }

    @Test
    void simpleUserRepository_FindById_ReturnUser() {

        SimpleUser user = new SimpleUser();
        user.setUsername("testuser");
        user.setPassword("password123");
        user.setCredit(100.0);
        user.setUserType(User.UserType.SIMPLE);
        SimpleUser savedUser = simpleUserRepository.save(user);

        Optional<SimpleUser> foundUser = simpleUserRepository.findById(savedUser.getId());

        Assertions.assertThat(foundUser).isPresent();
        Assertions.assertThat(foundUser.get()).isEqualTo(savedUser);
        Assertions.assertThat(foundUser.get().getUserType()).isEqualTo(User.UserType.SIMPLE);
    }

    @Test
    void simpleUserRepository_Update_ReturnUpdatedUser() {

        SimpleUser user = new SimpleUser();
        user.setUsername("testuser");
        user.setPassword("password123");
        user.setCredit(100.0);
        user.setUserType(User.UserType.SIMPLE);
        SimpleUser savedUser = simpleUserRepository.save(user);

        Optional<SimpleUser> foundUser = simpleUserRepository.findById(savedUser.getId());
        foundUser.ifPresent(u -> {
            u.setCredit(150.0);
            simpleUserRepository.save(u);
        });

        Optional<SimpleUser> updatedUser = simpleUserRepository.findById(savedUser.getId());
        Assertions.assertThat(updatedUser).isPresent();
        Assertions.assertThat(updatedUser.get().getCredit()).isEqualTo(150.0);
        Assertions.assertThat(updatedUser.get().getUserType()).isEqualTo(User.UserType.SIMPLE);
    }

    @Test
    void simpleUserRepository_Delete_ReturnEmptyUser() {

        SimpleUser user = new SimpleUser();
        user.setUsername("testuser");
        user.setPassword("password123");
        user.setCredit(100.0);
        user.setUserType(User.UserType.SIMPLE);
        SimpleUser savedUser = simpleUserRepository.save(user);

        simpleUserRepository.delete(savedUser);
        Optional<SimpleUser> foundUser = simpleUserRepository.findById(savedUser.getId());

        Assertions.assertThat(foundUser).isNotPresent();
    }
}

