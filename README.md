# Employee Creator Backend

## Overview

Employee Creator Backend is a Play Framework (Scala) application that provides a RESTful API for managing employees (CRUD). It uses a MySQL database and applies evolutions automatically on startup.

## Prerequisites

- Java 21
- sbt 1.9+
- MySQL 8 (or higher) running locally 

## Configuration

1. **Copy** the sample config into a real one:
   ```bash
   cp conf/application.conf.sample conf/application.conf

2. Edit conf/application.conf and fill in your database credentials:
     ```bash
   slick.dbs.default.db.url      = "jdbc:mysql://localhost:3306/employee_db?serverTimezone=UTC"
    slick.dbs.default.db.user     = "root"
    slick.dbs.default.db.password = "your_db_password"

Evolutions will create the tables automatically.

## Database Setup

1. Ensure MySQL is running and run :
   ```bash
   CREATE DATABASE employee_db  

## Run the application

## API Endpoints

### Employees
| Method | Endpoint          | Description                 |
|--------|-------------------|-----------------------------|
| GET    | /employees        | List all employees          |
| GET    | /employees/:id    | Retrieve an employee by ID  |
| POST   | /employees        | Create a new employee       |
| PUT    | /employees/:id    | Update an existing employee |
| DELETE | /employees/:id    | Delete an employee          |

### Contracts
| Method | Endpoint                          | Description                              |
|--------|-----------------------------------|------------------------------------------|
| GET    | /employees/:id/contracts          | List all contracts for an employee        |
| POST   | /employees/:id/contracts          | Create a new contract for an employee     |
| DELETE | /employees/:id/contracts/:cid     | Delete a contract of an employee |

