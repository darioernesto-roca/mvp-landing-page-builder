## 🚧 PHASE 1: Authentication (Login/Signup with Spring Security + JWT)

### 🎯 Goal

- Allow users (doctors, salons, etc.) to create an account and log in securely
- Enable authenticated users to build and edit their landing pages

## 📋 Steps

1. Create User entity
2. Set up PostgreSQL DB (next phase below)
3. Implement JWT authentication
   - Register, Login endpoints
   - Generate JWT token
   - Add filters to validate token on protected routes
4. Roles: USER, ADMIN (for dashboard later)
5. Protect routes (e.g., /dashboard only for logged-in users)

## ✅ Tools

- Spring Security
- Spring Data JPA
- JWT (via jjwt or spring-security-oauth2-jose)
- BCrypt for password encryption

## 💾 PHASE 2: PostgreSQL Setup

### 📋 Steps

1. Create a free PostgreSQL database (locally or via Railway/Neon)
2. Update application.yml:

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

3. Create DB entities:

- User
- LandingPage
- (optional) Service, Font, Theme

## 🎨 PHASE 3: Landing Page Builder Form

### 🎯 Goal

- Let logged-in users submit:
- Business name, description
- Logo upload (→ Cloudinary)
- Services (checkbox)
- Font + colors

### 📋 Steps

1. Create LandingPageForm.html with Thymeleaf
2. Add LandingPageController
3. Create LandingPage model + repository
4. Save form to DB, associate with user

## 🌐 PHASE 4: Subdomain Routing (Local & NGINX)

### 🎯 Goal

- Let public users visit https://drjane.yourapp.com and view the customized page

### 📋 Steps

1. Use HttpServletRequest to extract subdomain:

```java
String subdomain = request.getHeader("host").split("\\.")[0];
```

2. Load page by subdomain → find landing page in DB
3. Render with Thymeleaf
4. Update application.yml to allow CORS if needed
5. Simulate subdomains locally:
   - Add to C:\Windows\System32\drivers\etc\hosts

```bash
127.0.0.1 drjane.localhost
```

6. (Later) Add NGINX wildcard config for production

## 📂 PHASE 5: Dashboard UI (Optional but Useful)

Let users:

- View their landing page
- Edit content
- Upload new logo or services
- Delete account or regenerate page

## 🚀 PHASE 6: Deployment & DNS

### 📋 Steps

1. Buy domain (Namecheap, GoDaddy, etc.)
2. Setup Cloudflare:
   - A record for @ → server IP
   - A record for \* → server IP
3. Set up NGINX:

```bash
server {
    listen 80;
    server_name *.yourapp.com;
    location / {
        proxy_pass http://localhost:8080;
    }
}
```

4. Use Certbot to generate wildcard SSL
5. Run your app via systemd or Docker

## 🔚 What to Build First?

Let’s begin with User Authentication and the PostgreSQL database setup, then move into the form for the landing page builder.
Would you like me to walk you through:

1. Creating your User entity and POST /register endpoint with password encryption, or
2. Installing and connecting to PostgreSQL first?
