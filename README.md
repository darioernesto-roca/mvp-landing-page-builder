## 🧱 Project Overview: "Landing Page Builder for Professionals"

### 🎯 Goal

- Users (doctors, beauty salons, etc.) can:
- Sign up and create their own landing page.
- Customize it with text, logo, colors, and services.
- Access it via their own subdomain: https://drjane.yourapp.com.

## 🧩 Architecture Overview

```text
                     ┌──────────────┐
                     │   Internet   │
                     └──────┬───────┘
                            ↓
                    ┌──────────────┐
                    │  NGINX Proxy │  ← handles wildcard subdomains
                    └──────┬───────┘
                           ↓
         ┌──────────────────────────────────┐
         │        Java Spring Boot App      │
         └────────────────┬─────────────────┘
                          ↓
                ┌──────────────────┐
                │ PostgreSQL DB    │ ← user data, subdomains, page config
                └──────────────────┘
```

## ⚙️ Tools & Tech Stack

| Task                       | Tool                       |
| -------------------------- | -------------------------- |
| Language                   | Java (latest version)      |
| Framework                  | Spring Boot                |
| View Rendering             | Thymeleaf (or React later) |
| Authentication             | Spring Security (JWT)      |
| Database                   | PostgreSQL                 |
| IDE                        | **VSCode** or Eclipse      |
| Build Tool                 | Maven or Gradle            |
| Web Server / Reverse Proxy | NGINX                      |
| Deployment                 | DigitalOcean / Render      |
| DNS & SSL                  | Cloudflare + Let’s Encrypt |
| File Uploads (optional)    | Cloudinary / S3            |

## 🔧 Step 1: Install Latest Java (JDK 21+)

### ✅ For Ubuntu/Debian:

```bash
sudo apt update
sudo apt install openjdk-21-jdk
java -version
```

## ✅ For Windows/Mac:

1. Download from https://adoptium.net
2. Install and set JAVA_HOME
3. Verify:

```bash
java -version
javac -version
```

## 🛠️ Step 2: Create Spring Boot Project

Use https://start.spring.io:

- Project: Maven
- Language: Java
- Spring Boot: Latest stable
- Dependencies: - Spring Web - Spring Security - Spring Data JPA - PostgreSQL Driver - Thymeleaf - Validation
  Then unzip and open in VSCode.

## 📁 Step 3: Project Structure (MVP)

```text
src/
├── main/
│   ├── java/com/landing-app/
│   │   ├── controller/
│   │   ├── service/
│   │   ├── repository/
│   │   ├── model/
│   │   └── config/
│   └── resources/
│       ├── templates/
│       ├── static/
│       └── application.yml

```

## ✅ Step 4: Plan the MVP Features

1. Auth Module

- Sign up/login with JWT
- Role: USER / ADMIN

2. Landing Page Form

- Business Name, Description
- Logo Upload
- Services (multiselect)
- Color and Font selection

3. Dynamic Page Renderer

- Detect subdomain (drjane.landing-app.com)
- Fetch data from DB
- Render landing.html with Thymeleaf

4. Admin Panel (optional)

- View users, ban pages, manage spam

## 🌍 Step 5: Local Dev + Subdomain Simulation

- In application.yml, set host: localhost
- Use /etc/hosts file to simulate subdomains:

```bash
127.0.0.1 drjane.localhost
127.0.0.1 drsmith.localhost
```

- Use NGINX locally to simulate production routing

## 🚀 Step 6: Deployment Plan

- Buy domain (e.g., Namecheap)
- Set up wildcard DNS with Cloudflare:

```sql
*.yourapp.com → your server IP
```

- Deploy on DigitalOcean VPS or Render
- Configure NGINX for:

```bash
server_name *.landing-app.com;
```
