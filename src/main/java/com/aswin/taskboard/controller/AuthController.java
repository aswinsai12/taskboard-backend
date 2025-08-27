package com.aswin.taskboard.controller;

import com.aswin.taskboard.model.User;
import com.aswin.taskboard.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private BCryptPasswordEncoder passwordEncoder;

  @PostMapping("/register")
  public ResponseEntity<String> register(@RequestBody User user) {
    if (userRepository.findByUsername(user.getUsername()) != null) {
      return ResponseEntity.badRequest().body("Username already taken");
    }
    user.setPassword(passwordEncoder.encode(user.getPassword()));
    userRepository.save(user);
    return ResponseEntity.ok("User registered successfully");
  }

  @PostMapping("/login")
  public ResponseEntity<String> login(@RequestBody User login) {
    User user = userRepository.findByUsername(login.getUsername());
    if (user == null || !passwordEncoder.matches(login.getPassword(), user.getPassword())) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
    }
    return ResponseEntity.ok("Login successful");
  }
}
