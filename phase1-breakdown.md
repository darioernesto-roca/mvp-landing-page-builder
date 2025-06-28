## ✅ PHASE 1: AUTHENTICATION — Sign Up & Login with JWT

### 🎯 Goal

Allow users (doctors, beauty salons, etc.) to:

- Register with email and password
- Log in and receive a token (JWT)
- Use the token to access protected endpoints

## 🧩 Architecture Overview for Auth Module

```text
+-------------------+         +---------------------+
|  User Registers   |  --->   |  /api/auth/register |
+-------------------+         +---------------------+
                                      |
                                      v
                        Saves user to PostgreSQL (encrypted)

+-------------------+         +---------------------+
|  User Logs In     |  --->   |  /api/auth/login    |
+-------------------+         +---------------------+
                                      |
                                      v
                            Generates & returns JWT

+-------------------+         +-----------------------------+
|  Authenticated    |  --->   | Authorization Header: Bearer JWT |
|  User Accesses    |         | /api/landing-page           |
+-------------------+         +-----------------------------+
```

## 🔨 STEP-BY-STEP IMPLEMENTATION

### ✅ STEP 1: Create the User Entity

📄 User.java in model/ folder

```java
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

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> roles = new ArrayList<>();

    // Getters and setters (or use Lombok if preferred)
}
```

## ✅ STEP 2: Create the UserRepository

📄 UserRepository.java in repository/

```java
package net.rocadev.landingbuilder.repository;

import net.rocadev.landingbuilder.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
}
```

## ✅ STEP 3: Add Dependencies for JWT & Password Encryption

📄 Add to pom.xml:

```xml
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-api</artifactId>
    <version>0.11.5</version>
</dependency>
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-impl</artifactId>
    <version>0.11.5</version>
    <scope>runtime</scope>
</dependency>
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-jackson</artifactId>
    <version>0.11.5</version>
    <scope>runtime</scope>
</dependency>
<dependency>
    <groupId>org.springframework.security</groupId>
    <artifactId>spring-security-crypto</artifactId>
</dependency>
```

Run:

```bash
mvn clean install
✅ STEP 4: Create DTOs for Login and Signup
📄 RegisterRequest.java & LoginRequest.java in dto/
```

```java
public class RegisterRequest {
    public String email;
    public String password;
}

public class LoginRequest {
    public String email;
    public String password;
}
```

## ✅ STEP 5: Create JWT Utility Class

📄 JwtUtil.java in security/

```java
@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String jwtSecret;

    public String generateToken(UserDetails userDetails) {
        return Jwts.builder()
            .setSubject(userDetails.getUsername())
            .claim("roles", userDetails.getAuthorities())
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 10 hours
            .signWith(Keys.hmacShaKeyFor(jwtSecret.getBytes()))
            .compact();
    }

    public String extractUsername(String token) {
        return Jwts.parserBuilder()
            .setSigningKey(jwtSecret.getBytes())
            .build()
            .parseClaimsJws(token)
            .getBody()
            .getSubject();
    }
}
```

📄 In application.yml, define the secret key:

```yaml
jwt:
  secret: your_very_secret_key_12345678901234567890
```

## ✅ STEP 6: Create AuthController

📄 AuthController.java in controller/

```java
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired private UserRepository userRepo;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private JwtUtil jwtUtil;
    @Autowired private AuthenticationManager authManager;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        if (userRepo.existsByEmail(request.email)) {
            return ResponseEntity.badRequest().body("Email already in use");
        }

        User user = new User();
        user.setEmail(request.email);
        user.setPassword(passwordEncoder.encode(request.password));
        user.setRoles(List.of("USER"));

        userRepo.save(user);
        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        Authentication auth = authManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.email, request.password)
        );

        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        String token = jwtUtil.generateToken(userDetails);

        return ResponseEntity.ok(Map.of("token", token));
    }
}
```

## ✅ STEP 7: Add Spring Security Config

📄 SecurityConfig.java in config/

```java
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired private JwtFilter jwtFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
            .authorizeHttpRequests()
            .requestMatchers("/api/auth/**").permitAll()
            .anyRequest().authenticated()
            .and()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
```

## ✅ STEP 8: Create JWT Filter

📄 JwtFilter.java in security/

```java
@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired private JwtUtil jwtUtil;
    @Autowired private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            String username = jwtUtil.extractUsername(token);

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        filterChain.doFilter(request, response);
    }
}
```

## ✅ FINAL TESTS

- POST /api/auth/register with { "email": "test@example.com", "password": "123456" }
- POST /api/auth/login → get JWT token
- Use Authorization: Bearer <token> in future requests
