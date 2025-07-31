# E-Wallet Backend

A distributed E-Wallet system built with Spring Boot, Kafka, and MySQL. The project is organized as a multi-module Maven workspace with the following microservices:

- **userService**: Manages user registration and details.
- **walletService**: Handles wallet creation, balance updates, and wallet transactions.
- **transactionService**: Manages transaction initiation and status tracking.
- **notificationService**: Sends email notifications for user and wallet events.
- **common-code**: Shared DTOs, Kafka configs, and utility classes.

## Architecture

- **Spring Boot** for REST APIs and service orchestration.
- **Apache Kafka** for asynchronous inter-service communication.
- **MySQL** as the persistent data store for each service.
- **Lombok** for boilerplate code reduction.
- **Spring Data JPA** for ORM.

## Modules

- `userService`: User registration, validation, and Kafka publishing on user creation.
- `walletService`: Wallet CRUD, balance management, and Kafka integration for wallet updates and transaction completion.
- `transactionService`: Transaction initiation, status tracking, and Kafka event handling.
- `notificationService`: Listens to Kafka topics and sends email notifications.
- `common-code`: Contains shared DTOs like `TxnInitPayload`, `TxnCompletePayload`, `UserCreatedPayload`, and Kafka configuration.

## Getting Started

### Prerequisites

- Java 17+
- Maven 3.9+
- MySQL running with databases: `userdb`, `walletdb`, `transactiondb`, `notificationdb`
- Kafka running on `localhost:9092,localhost:9093`

### Build

From the root directory, run:

```sh
./mvnw clean install
```

### Run Services

Each service can be started independently:

```sh
cd userService && ../mvnw spring-boot:run
cd walletService && ../mvnw spring-boot:run
cd transactionService && ../mvnw spring-boot:run
cd notificationService && ../mvnw spring-boot:run
```

### Configuration

Each service has its own `src/main/resources/application.properties` for DB and Kafka configuration.

## Kafka Topics

- `USER_CREATED`: Published when a user is created.
- `TXN_INIT`: Published when a transaction is initiated.
- `TXN_COMPLETED`: Published when a transaction is completed.
- `WALLET_UPDATED`: Published when a wallet is updated.

## Endpoints

- **User Service**:  
  `POST /user-service/user` — Create a new user

- **Transaction Service**:  
  `POST /transaction/txn` — Initiate a transaction  
  `GET /transaction/status/{transactionId}` — Get transaction status

## Email Notifications

The notification service listens to user and wallet events and sends emails using the configured SMTP server.

---

**Note:**  
- Update database credentials and SMTP settings in the respective `application.properties` files before running.

---
