# Project Overview
The `user-svc` is a Spring Boot application that provides REST APIs for managing user data. It uses various technologies such as Spring Data JPA, Spring Security, Kafka, Redis, and more to offer a robust and scalable solution for user management.

## Architecture Description
The project follows a layered architecture with controllers, services, and repositories. It uses Spring Boot for building the application, Spring Data JPA for database interactions, and Spring Security for securing the APIs.

```mermaid
graph TD;
    A[Client] -->|HTTP Requests| B[Controller];
    B -->|Service Calls| C[Service];
    C -->|Data Access| D[Repository];
    D -->|Database Operations| E[(PostgreSQL)];
    C -->|Cache Access| F[(Redis)];
    B -->|Send event| G[(Kafka)];
```

## Entity-Relationship Diagram (ERD) with Column Details

```mermaid
erDiagram
   SiteUser {
      UUID id PK
      String emailAddress
      String phoneNumber
      String firstName
      String lastName
      LocalDateTime createdAt
      String createdBy
      LocalDateTime lastModifiedAt
      String lastModifiedBy
   }

   Address {
      UUID id PK
      String unitNumber
      String streetNumber
      String addressLine1
      String addressLine2
      String city
      String region
      String postalCode
      UUID countryId FK
      LocalDateTime createdAt
      String createdBy
      LocalDateTime lastModifiedAt
      String lastModifiedBy
   }

   Country {
      UUID id PK
      String countryName
      LocalDateTime createdAt
      String createdBy
      LocalDateTime lastModifiedAt
      String lastModifiedBy
   }

   UserAddress {
      UUID userId PK, FK
      UUID addressId PK, FK
      Boolean isDefault
      LocalDateTime createdAt
      String createdBy
      LocalDateTime lastModifiedAt
      String lastModifiedBy
   }

   SiteUser ||--o{ UserAddress : has
   Address ||--o{ UserAddress : has
   Address }o--|| Country : belongs_to
```

## Common API Flow Diagrams
The API flow diagrams illustrate the interactions between different components for various API endpoints.



