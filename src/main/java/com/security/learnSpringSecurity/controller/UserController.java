package com.security.learnSpringSecurity.controller;

import com.security.learnSpringSecurity.model.Customer;
import com.security.learnSpringSecurity.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody Customer customer) {
        try {
            String encodedPassword = passwordEncoder.encode(customer.getPwd());
            customer.setPwd(encodedPassword);
            Customer customerSaved = customerRepository.save(customer);

            if(customer.getId() > 0){
                return ResponseEntity.status(HttpStatus.CREATED).
                        body("Given user details are successfully registered");
            }
            else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User registration failed");
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User registration failed");
        }
    }
}
