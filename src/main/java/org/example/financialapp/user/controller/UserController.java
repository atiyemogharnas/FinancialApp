package org.example.financialapp.user.controller;

import org.example.financialapp.user.domain.SimpleUser;
import org.example.financialapp.user.domain.User;
import org.example.financialapp.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/create")
    public ResponseEntity<Object> createUser(@RequestBody User user) {
        try {
            User createdUser = userService.createUser(user);
            return ResponseEntity.ok(createdUser);
        }catch (Exception ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @PutMapping("/update/{userId}")
    public ResponseEntity<User> updateUser(@PathVariable Long userId, @RequestBody User updatedUser) {
        User user = userService.updateUser(userId, updatedUser);
        return ResponseEntity.ok(user);
    }

    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/all")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Optional<User>> getUserById(@PathVariable Long userId) {
        Optional<User> user = userService.getUserById(userId);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/credit/{userId}")
    public ResponseEntity<SimpleUser> updateCredit(@PathVariable Long userId, @RequestParam Double additionalCredit) {
        SimpleUser simpleUser = userService.updateCredit(userId, additionalCredit);
        return ResponseEntity.ok(simpleUser);
    }

    @GetMapping("/simple")
    public ResponseEntity<List<SimpleUser>> getAllSimpleUsers() {
        List<SimpleUser> simpleUsers = userService.getAllSimpleUsers();
        return ResponseEntity.ok(simpleUsers);
    }
}

