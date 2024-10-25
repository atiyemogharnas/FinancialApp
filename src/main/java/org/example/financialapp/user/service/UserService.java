package org.example.financialapp.user.service;

import org.example.financialapp.user.domain.SimpleUser;
import org.example.financialapp.user.domain.User;
import org.example.financialapp.user.repository.SimpleUserRepository;
import org.example.financialapp.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SimpleUserRepository simpleUserRepository;

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public User updateUser(Long userId, User updatedUser) {
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

        existingUser.setUsername(updatedUser.getUsername() == null ? existingUser.getUsername() : updatedUser.getUsername());
        existingUser.setPassword(updatedUser.getPassword() == null ? existingUser.getPassword() : updatedUser.getPassword());
        existingUser.setUserType(updatedUser.getUserType() == null ? existingUser.getUserType() : updatedUser.getUserType());

        return userRepository.save(existingUser);
    }

    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Long userId) {
        return userRepository.findById(userId);
    }

    public SimpleUser updateCredit(Long userId, Double additionalCredit) {
        SimpleUser simpleUser = simpleUserRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("SimpleUser not found with ID: " + userId));

        double newCredit = simpleUser.getCredit() + additionalCredit;
        simpleUser.setCredit(newCredit);

        return simpleUserRepository.save(simpleUser);
    }

    public List<SimpleUser> getAllSimpleUsers() {
        return simpleUserRepository.findAll();
    }
}

