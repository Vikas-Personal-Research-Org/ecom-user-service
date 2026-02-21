# E-Commerce User Service

A Spring Boot microservice for user registration, JWT authentication, and profile management. Part of the e-commerce microservices ecosystem.

## Tech Stack

- Java 21
- Spring Boot 3.4.1
- Spring Security with JWT (jjwt 0.12.6)
- Spring Data JPA with H2 (in-memory)
- Spring Cloud 2024.0.0 (Eureka Client)
- SpringDoc OpenAPI (Swagger UI)
- Maven

## API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/users/register` | Register a new user |
| POST | `/api/users/login` | Login and receive JWT token |
| GET | `/api/users/{id}` | Get user by ID |
| GET | `/api/users/email/{email}` | Get user by email |
| PUT | `/api/users/{id}` | Update user profile |

## How to Run

### Prerequisites
- Java 21
- Maven 3.9+

### Build and Run

```bash
# Build the project
mvn clean package -DskipTests

# Run the application
java -jar target/ecom-user-service-0.0.1-SNAPSHOT.jar

# Or run with Maven
mvn spring-boot:run
```

### Run Tests

```bash
mvn test
```

### Docker

```bash
# Build the image
docker build -t ecom-user-service .

# Run the container
docker run -p 8084:8084 ecom-user-service
```

## Access Points

- **Application**: http://localhost:8084
- **Swagger UI**: http://localhost:8084/swagger-ui.html
- **H2 Console**: http://localhost:8084/h2-console (JDBC URL: `jdbc:h2:mem:userdb`, Username: `sa`, No password)
- **Actuator**: http://localhost:8084/actuator

## Sample Requests

### Register
```json
POST /api/users/register
{
  "email": "user@example.com",
  "password": "password123",
  "firstName": "John",
  "lastName": "Doe",
  "role": "BUYER"
}
```

### Login
```json
POST /api/users/login
{
  "email": "user@example.com",
  "password": "password123"
}
```

## Default Users

The service comes preloaded with 10 dummy users (password: `password123` for all):
- Roles: BUYER, SELLER, ADMIN
- Emails: alice.johnson@example.com, bob.smith@example.com, carol.williams@example.com, etc.
