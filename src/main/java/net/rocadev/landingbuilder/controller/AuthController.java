package net.rocadev.landingbuilder.controller;

import net.rocadev.landingbuilder.dto.LoginRequest;
import net.rocadev.landingbuilder.dto.RegisterRequest;
import net.rocadev.landingbuilder.model.User;
import net.rocadev.landingbuilder.repository.UserRepository;
import net.rocadev.landingbuilder.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        if (userRepository.existsByEmail(request.email)) {
            return ResponseEntity.badRequest().body("Email already exists");
        }

        // Create new user with default role
        net.rocadev.landingbuilder.model.User user = new net.rocadev.landingbuilder.model.User();
        user.setEmail(request.email);
        user.setPassword(new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder().encode(request.password));
        user.setRoles(List.of("USER"));

        userRepository.save(user);
        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            Authentication auth = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.email, request.password)
            );

            UserDetails userDetails = (UserDetails) auth.getPrincipal();
            String token = jwtUtil.generateToken(userDetails.getUsername());

            return ResponseEntity.ok(Map.of("token", token));

        } catch (BadCredentialsException e) {
            return ResponseEntity.status(401).body("Invalid credentials");
        }
    }
}
