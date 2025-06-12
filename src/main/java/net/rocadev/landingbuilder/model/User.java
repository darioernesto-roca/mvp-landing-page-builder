package net.rocadev.landingbuilder.model;

import jakarta.persistence.*;
import java.util.*;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    private String password;

    @elementCollection(fetch = FetchType.EAGER)
    private List<String> roles = new ArrayList<>();
        
    // Getters and Setters

}