# Grandma's Food - Restaurant Management API

**Grandma's Food** is a Java-based API designed to manage a restaurant's clients, products, and orders. The application leverages a microservice architecture built with Spring Boot 3.1 and Java 21, adhering to best practices in software design, including a hexagonal architecture and robust testing. 

## Table of Contents
- [Features](#features)
- [Technologies Used](#technologies-used)
- [Installation](#installation)
- [API Documentation](#api-documentation)
- [Testing](#testing)
- [Code Coverage](#code-coverage)
- [Invoice Ninja Setup](#invoice-ninja-setup)

---

## Features

The API provides management capabilities for three core entities:
1. **Clients**: Manages client records and data.
2. **Products**: Manages inventory and product information.
3. **Orders**: Tracks client orders, status, and transactions.

Additional features:
- **API Versioning**: Supports versioned endpoints for backward compatibility.
- **Database Integration**: Uses MySQL 8 for data persistence.
- **Custom Error Table**: Logs custom error descriptions for easy debugging and reporting.
- **Cache Management**: Implements caching with `@Cachable` annotations for faster data retrieval.
- **PDF Generation**: Generates invoices and reports in PDF format.
- **Electronic Invoice**: Integrates with Invoice Ninja for electronic invoice generation.

---

## Technologies Used

- **Java 21**
- **Spring Boot 3.3.3**
  - Spring Data JPA
  - Spring Cache
  - Spring AOT/JIT Compilation
- **MySQL 8** 
- **Swagger** for API documentation
- **JUnit & Mockito** for unit and integration tests
- **JaCoCo** for code coverage
- **Mapstruct** for object mapping
- **Lombok** for reducing boilerplate code
- **Invoice Ninja** for electronic invoicing

## Architecture

The project follows **Hexagonal Architecture** to enable flexibility and testability, separating core business logic from external integrations. C4 diagrams are included in the project to visualize architectural layers and dependencies.

---

## Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/juan-bueno-01/java-project.git
   cd grandmas-food
   ```
2. Set up MySQL database:
   - Use the provided `docker-compose.yml` file to launch MySQL 8 or your local installation:
   ```bash
   docker-compose up -d
   ```
   - Update `src/main/resources/application.properties` with your database details:
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/grandmas_food
   spring.datasource.username=root
   spring.datasource.password=yourpassword
   ```
3. Set up MySQL database:
   ```bash
   ./mvnw spring-boot:run
   ```
4. Access Swagger Documentation:
   - Swagger documentation is available at `http://localhost:8080/swagger-ui.html`.
## Testing

To run the unit and integration tests:
```bash
mvn test
```
## Invoice Ninja Setup (for Electronic Invoicing)

To configure **Invoice Ninja** locally using Docker, follow these steps:
1. Clone the Invoice Ninja Docker Repository:
```bash
git clone https://github.com/invoiceninja/dockerfiles.git
cd dockerfiles
```
2. Configure Environment Variables:
- Open the `env` file and add/update the following variables:
```env
APP_URL=http://in.localhost:8003/
APP_KEY=<insert your generated key here>
APP_DEBUG=true
REQUIRE_HTTPS=false
IN_USER_EMAIL=admin@example.com
IN_PASSWORD=changeme!
```
- Generate APP_KEY:
```bash
docker run --rm -it invoiceninja/invoiceninja php artisan key:generate --show
```
Copy the generated key and paste it into the `env` file under `APP_KEY`.
3. Set Folder Permissions:
```bash
chmod 755 docker/app/public
sudo chown -R 1500:1500 docker/app
```
4. Update Hosts File:
- Add the host resolution to your hosts file, typically located in `/etc/hosts`:
```hots
192.168.x.x in5.test
```
Replace `192.168.x.x` with your LAN IP address and ensure the domain name ends with `.test`.
5. Start Invoice Ninja:
```bash
docker-compose up -d
```
- When performing setup, specify db as the Database host.
> **Note**:Ensure the domain name for `APP_URL` ends with `.test` to enable local PDF generation, as some DNS resolvers require it for Chromium-based PDF generation.
6. Verify Invoice Ninja: You should now be able to access Invoice Ninja at `http://in.localhost:8003/`, logging in with the credentials set in the `.env` file. If you get a `404` error try `http://192.168.x.x/` with your LAN IP address.
## Next Steps
- **Project details**: Follow the field specifications for each table and other requirements in `project.pdf`.
- **Explore API Endpoints**: Use Swagger to explore and test the API endpoints.
- **Review C4 Diagrams**: Check the C4 diagrams in the documentation to understand the architectural layout.
- **Run JaCoCo Coverage Reports**: Execute `./mvnw jacoco:report` to verify code coverage.
