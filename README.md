# CarSharing API

A Spring Boot application for a car sharing service.

## Features

- User authentication with JWT
- User registration and profile management
- Role-based access control (MANAGER and CUSTOMER roles)

## API Documentation

### Authentication

- **POST /register**: Register a new user
- **POST /login**: Authenticate and get a JWT token

### User Management

- **GET /users/me**: Get the current user's profile
- **PUT /users/me**: Update the current user's profile
- **PATCH /users/me**: Partially update the current user's profile
- **PUT /users/{id}/role**: Update a user's role (requires MANAGER role)

### Car Management

- **POST /cars**: Add a new car
- **GET /cars**: Get a list of all cars
- **GET /cars/{id}**: Get detailed information about a specific car
- **PUT /cars/{id}**: Update a car's information
- **PATCH /cars/{id}**: Partially update a car's information
- **DELETE /cars/{id}**: Delete a car

### Rental Management

- **POST /rentals**: Add a new rental (decreases car inventory by 1)
- **GET /rentals/?user_id=...&is_active=...**: Get rentals by user ID and active status
- **GET /rentals/{id}**: Get detailed information about a specific rental
- **POST /rentals/{id}/return**: Return a rental (sets actual return date and increases car
  inventory by 1)

## Postman Collection

A Postman collection and environment are available in the `postman` directory to help you test the
API:

- `postman/CarSharing.postman_collection.json`: Collection of API requests
- `postman/CarSharing.postman_environment.json`: Environment variables
- `postman/README.md`: Instructions for using the Postman collection

See the [Postman README](postman/README.md) for detailed instructions on how to import and use the
collection.

## Getting Started

### Prerequisites

- Java 21
- Maven
- MySQL

### Running the Application

1. Clone the repository
2. Configure the database connection in `application-dev.properties`
3. Run the application:

```bash
mvn spring-boot:run
```

The API will be available at http://localhost:8080

## Authentication

The API uses JWT (JSON Web Token) for authentication. To access protected endpoints:

1. Register a user or login to get a token
2. Include the token in the Authorization header of subsequent requests:

```
Authorization: Bearer <your_token>
```

## Development

### Environment Profiles

- `dev`: Development environment (default)
- `prod`: Production environment

To run with a specific profile:

```bash
mvn spring-boot:run -Dspring.profiles.active=prod
```
