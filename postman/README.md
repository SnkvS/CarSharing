# CarSharing API Postman Collection

This directory contains Postman collection and environment files for testing the CarSharing API.

## Files

- `CarSharing.postman_collection.json`: Postman collection with requests for all API endpoints
- `CarSharing.postman_environment.json`: Postman environment with variables for the API

## Importing into Postman

1. Open Postman
2. Click on "Import" in the top left corner
3. Drag and drop both files or click "Upload Files" and select both files
4. Click "Import"

## Setting Up the Environment

1. In Postman, click on the environment dropdown in the top right corner
2. Select "CarSharing API Environment"
3. The `baseUrl` variable is set to `http://localhost:8080` by default. Update this if your API is
   running on a different URL.

## Using the Collection

The collection is organized into folders based on functionality:

### Authentication

- **Register User**: Create a new user account
    - Method: POST
    - Endpoint: {{baseUrl}}/register
    - Body: JSON with firstName, lastName, email, password, and repeatedPassword

- **Login User**: Authenticate and get a JWT token
    - Method: POST
    - Endpoint: {{baseUrl}}/login
    - Body: JSON with email and password
    - Note: This request automatically saves the token to the `authToken` environment variable

### User Management

- **Get Current User Profile**: Get the profile of the currently authenticated user
    - Method: GET
    - Endpoint: {{baseUrl}}/users/me
    - Authorization: Bearer Token (automatically uses the `authToken` variable)

- **Update User Profile**: Update the profile of the currently authenticated user
    - Method: PUT
    - Endpoint: {{baseUrl}}/users/me
    - Body: JSON with firstName, lastName, and email
    - Authorization: Bearer Token

- **Partially Update User Profile**: Partially update the profile of the currently authenticated
  user
    - Method: PATCH
    - Endpoint: {{baseUrl}}/users/me
    - Body: JSON with firstName, lastName, and email
    - Authorization: Bearer Token

- **Update User Role**: Update the role of a user (requires MANAGER role)
    - Method: PUT
    - Endpoint: {{baseUrl}}/users/{id}/role
    - Path Parameter: id - The ID of the user to update
    - Body: JSON with roleName (either "MANAGER" or "CUSTOMER")
    - Authorization: Bearer Token

### Car Management

- **Create Car**: Add a new car to the system
    - Method: POST
    - Endpoint: {{baseUrl}}/cars
    - Body: JSON with model, brand, type, inventory, and dailyFee
    - Authorization: Bearer Token

- **Get All Cars**: Get a list of all available cars
    - Method: GET
    - Endpoint: {{baseUrl}}/cars

- **Get Car by ID**: Get detailed information about a specific car
    - Method: GET
    - Endpoint: {{baseUrl}}/cars/{id}
    - Path Parameter: id - The ID of the car to retrieve

- **Update Car**: Update all information for a specific car
    - Method: PUT
    - Endpoint: {{baseUrl}}/cars/{id}
    - Path Parameter: id - The ID of the car to update
    - Body: JSON with model, brand, type, inventory, and dailyFee
    - Authorization: Bearer Token

- **Partially Update Car**: Partially update information for a specific car
    - Method: PATCH
    - Endpoint: {{baseUrl}}/cars/{id}
    - Path Parameter: id - The ID of the car to update
    - Body: JSON with any of model, brand, type, inventory, or dailyFee
    - Authorization: Bearer Token

- **Delete Car**: Delete a specific car
    - Method: DELETE
    - Endpoint: {{baseUrl}}/cars/{id}
    - Path Parameter: id - The ID of the car to delete
    - Authorization: Bearer Token

### Rental Management

- **Create Rental**: Add a new rental (decreases car inventory by 1)
    - Method: POST
    - Endpoint: {{baseUrl}}/rentals
    - Body: JSON with carId, rentalDate, and returnDate
    - Authorization: Bearer Token

- **Get Rentals**: Get rentals by user ID and active status
    - Method: GET
    - Endpoint: {{baseUrl}}/rentals
    - Query Parameters:
        - user_id (optional): The ID of the user whose rentals to retrieve
        - is_active (optional, default: false): Whether to retrieve active (true) or completed (
          false) rentals
    - Authorization: Bearer Token

- **Get Rental by ID**: Get detailed information about a specific rental
    - Method: GET
    - Endpoint: {{baseUrl}}/rentals/{id}
    - Path Parameter: id - The ID of the rental to retrieve
    - Authorization: Bearer Token

- **Return Rental**: Return a rental (sets actual return date and increases car inventory by 1)
    - Method: POST
    - Endpoint: {{baseUrl}}/rentals/{id}/return
    - Path Parameter: id - The ID of the rental to return
    - Body: JSON with actualReturnDate
    - Authorization: Bearer Token

## Authentication Flow

1. Register a new user using the "Register User" request
2. Login with the registered user's credentials using the "Login User" request
    - This will automatically save the JWT token to the `authToken` environment variable
3. Use the authenticated endpoints with the token

## Notes

- All requests that require authentication automatically include the Bearer token from the
  environment variable
- The "Update User Role" endpoint requires the user to have the MANAGER role
