﻿# Candidate-Test


`The following technologies are used to build the Application:`
- ☕ **Java 17**
- 🌱 **Spring Boot**
- 🌱🛢️ **Spring Data JPA**
- 🌱🛡️ **Spring Security**
- 🗎 **Swagger**
- 🐬 **MySQL**
- 🐋  **Docker**
- 🌶️ **Lombok**
- ↔️ **MapStruct**

### ❓ How to use
`Before running Online Book Store, ensure you have the following installed:`
- ☕ Java
- 🐋 Docker

`Follow the steps below to install:`
1. Clone the repository from GitHub and navigate to the project directory.
2. Create a `.env` file with the necessary environment variables. (See `.env-sample` for a sample.)
3. Run the following command to build and start the Docker containers:
   `docker-compose up --build`.
4. The application should now be running at `http://localhost:8081`. 

### Auth Endpoints(all users)

| **HTTP method** | **Endpoint**        | **Function**                       |
|:----------------|:--------------------|:-----------------------------------|
| POST            | /users/add          | Register a new user.               |
| POST            | /users/authenticate | Get JWT token for authentication.  |

### Products Management Endpoints(only authenticated users)
| **HTTP method** | **Endpoint**  | **Function**                                    |
|:----------------|:--------------|:------------------------------------------------|
| POST            | /products/add | Add a new table with products from records.     |
| GET             | /products/all | Get a list of all products from products table. |
