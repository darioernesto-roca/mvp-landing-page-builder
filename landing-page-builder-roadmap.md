
# 🚀 Landing Page Builder Roadmap (Java + Spring Boot)

## ✅ Current Progress
Spring Boot is set up and rendering a sample page at `/hello`.

---

## 🚧 PHASE 1: Authentication (Login/Signup with Spring Security + JWT)

### 🎯 Goal
- Users can register and log in securely.
- Authenticated users can manage their landing pages.

### 📋 Steps
1. Create `User` entity.
2. Set up PostgreSQL database.
3. Implement JWT authentication.
   - Register and login endpoints.
   - Token generation and validation.
   - Authentication filter.
4. Define roles: `USER`, `ADMIN`.
5. Protect routes based on roles.

### ✅ Tools
- Spring Security
- Spring Data JPA
- JWT (e.g., `jjwt`)
- BCrypt for passwords

---

## 💾 PHASE 2: PostgreSQL Setup

### 📋 Steps
1. Install PostgreSQL locally or use a cloud provider.
2. Configure `application.yml`:
   ```yaml
   spring:
     datasource:
       url: jdbc:postgresql://localhost:5432/landingbuilder
       username: youruser
       password: yourpass
     jpa:
       hibernate:
         ddl-auto: update
       show-sql: true
   ```
3. Create JPA entities: `User`, `LandingPage`, etc.

---

## 🎨 PHASE 3: Landing Page Builder Form

### 🎯 Goal
Form for business details:
- Name, description, logo upload, services, colors, fonts.

### 📋 Steps
1. Create `LandingPageForm.html` using Thymeleaf.
2. Add `LandingPageController`.
3. Create entity and repository for `LandingPage`.
4. Save data and link to authenticated user.

---

## 🌐 PHASE 4: Subdomain Routing (Local & Production)

### 🎯 Goal
Each landing page accessible via a unique subdomain.

### 📋 Steps
1. Extract subdomain from request:
   ```java
   String subdomain = request.getHeader("host").split("\.")[0];
   ```
2. Query DB by subdomain.
3. Render template with landing data.
4. Simulate locally with `/etc/hosts`:
   ```
   127.0.0.1 drjane.localhost
   ```
5. Set up NGINX for wildcard routing.

---

## 📂 PHASE 5: User Dashboard (Optional)

- Edit landing page content.
- Upload new logo or services.
- Manage user settings.

---

## 🚀 PHASE 6: Deployment & DNS

### 📋 Steps
1. Buy a domain.
2. Configure Cloudflare DNS:
   - A record for `@` and `*` to server IP.
3. Set up NGINX:
   ```nginx
   server {
       listen 80;
       server_name *.yourapp.com;
       location / {
           proxy_pass http://localhost:8080;
       }
   }
   ```
4. Secure with SSL via Certbot (Let's Encrypt).
5. Deploy app via `java -jar` or Docker.
