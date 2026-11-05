# Simple Banking Application

A command-line banking application built with **Java, Maven, JDBC, and MySQL**. It allows users to create bank accounts, authenticate with an account number and PIN, and perform basic banking transactions. Account and transaction data are persisted in a MySQL database.

This project was developed as the final project for the **TESDA Programming (Java) NC III** course.

> **Note:** This is an educational project, not production banking software. PINs are currently stored in plaintext. Do not use real personal information, credentials, or money with this application.

## Features

- **Create an account** — Register an account with the account holder's name, a PIN, and an initial deposit. The application generates a unique account number.
- **Account login** — Authenticate using an account number and PIN.
- **Balance inquiry** — View the current account balance.
- **List accounts** — Display the accounts registered in the system.
- **Deposit** — Add a positive amount to an account and record the transaction.
- **Withdraw** — Withdraw a positive amount when the account has sufficient funds.
- **Transfer** — Transfer funds to another existing account, subject to amount and balance validation.
- **Transaction history** — View the latest 10 transactions associated with an account.
- **Find transaction by ID** — Retrieve a transaction using its transaction ID.

## Technology Stack

| Technology | Use |
|---|---|
| Java | Application logic and object-oriented design |
| Maven | Project and dependency management |
| MySQL | Persistent storage for accounts and transactions |
| JDBC | Database access through DAO implementations |
| UML / PlantUML | System and codebase design documentation |

## Application Design

The application separates its menu handling, domain models, data access, and business logic into packages.

```text
com.rvmchua.bank
├── app
│   └── Main
├── config
│   └── DatabaseConnection
├── enums
│   ├── TransactionType
│   └── TransferType
├── menu
│   ├── MainMenu
│   ├── LoginMenu
│   └── AccountDashboard
├── model
│   ├── Account
│   └── Transaction
├── dao
│   ├── AccountDAO
│   ├── TransactionDAO
│   └── impl
│       ├── AccountDAOImpl
│       └── TransactionDAOImpl
├── service
│   ├── AccountService
│   ├── AuthService
│   └── TransactionService
└── util
    └── AccountNumberMaker
```

### Main components

- **Menu package** — Handles the command-line menus, user input, and navigation between application screens.
- **Model package** — Contains the `Account` and `Transaction` domain objects.
- **DAO package** — Defines persistence operations through `AccountDAO` and `TransactionDAO`, with JDBC implementations in `dao.impl`.
- **Service package** — Contains account operations, authentication, and transaction workflows.
- **Config package** — Manages database connection configuration and loads application properties.
- **Enums and utilities** — Define transaction and transfer types and generate account numbers.

## Database Design

The database uses two primary tables: `accounts` and `transactions`.

### `accounts`

Stores account holder information and the current account balance. Its `entry_id` is the auto-incrementing primary key, while `account_number` is unique and used for account operations.

| Field | Description |
|---|---|
| `entry_id` | Auto-incrementing primary key |
| `account_number` | Unique account number |
| `first_name` | Account holder's first name |
| `last_name` | Account holder's last name |
| `pin_code` | Account PIN |
| `balance` | Current account balance |
| `created_at` | Account creation timestamp |
| `updated_at` | Last update timestamp |

### `transactions`

Stores transaction records, including the transaction type, amount, resulting balance, and timestamp.

| Field | Description |
|---|---|
| `transaction_id` | Auto-incrementing primary key |
| `account_number` | Account associated with the transaction; foreign key to `accounts.account_number` |
| `type` | `WITHDRAW`, `DEPOSIT`, or `TRANSFER` |
| `amount` | Transaction amount |
| `balance_after` | Account balance after the transaction |
| `created_at` | Transaction timestamp |

The relationship between `accounts` and `transactions` is **one-to-many**: one account can have multiple transaction records. The schema uses a foreign key and cascading updates and deletes to maintain referential integrity.

The SQL scripts are available in the [`sql`](https://github.com/rvmchua/prj-project/tree/master/sql) directory.

## Getting Started

### Requirements

- Java
- Maven
- MySQL

The project documentation does not specify the required Java or MySQL versions. Check the project's Maven configuration and database setup before choosing versions.

### Setup

1. **Clone the repository.**

   ```bash
   git clone https://github.com/rvmchua/prj-project.git
   cd prj-project
   ```

2. **Set up the MySQL database.**

   Review and execute the SQL scripts in the [`sql`](https://github.com/rvmchua/prj-project/tree/master/sql) directory to create the required tables and constraints.

3. **Configure the database connection.**

   The `DatabaseConnection` class loads the application's database settings from application properties. Review that class and the project's configuration files for the expected property names and values. The project documentation does not list the exact database name or property keys.

4. **Import and run the application.**

   Import the repository as a Maven project in your IDE, then run the `Main` class:

   ```text
   com.rvmchua.bank.app.Main
   ```

   Make sure MySQL is running and the database connection settings are configured before starting the application.

## Design Documentation

- [Class diagram](https://github.com/rvmchua/prj-project/blob/master/uml/class-diagram.png?raw=true)
- [SQL scripts](https://github.com/rvmchua/prj-project/tree/master/sql)

## Known Limitations and Future Improvements

The following improvements are identified in the project documentation:

- Replace selected input validations with custom exception classes.
- Hash account PINs with **BCrypt** instead of storing them in plaintext.
- Migrate the business logic to **Spring and Spring Boot** and expose it through a REST API.
- Add a frontend using **React** or **Angular**.

## Learning Objectives

This project provided practical experience with:

- Java object-oriented programming concepts, including encapsulation, abstraction, inheritance, and polymorphism.
- CRUD operations and relational database design.
- JDBC-based persistence and the DAO pattern.
- Separating menu handling, business logic, and database access.
- Input validation and exception handling.
- UML diagrams for documenting application structure and workflows.

## Repository

[github.com/rvmchua/prj-project](https://github.com/rvmchua/prj-project)
