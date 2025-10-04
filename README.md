# 🏨 Hotel Room Booking System (Java, Spring Boot, MySQL)

This is a complete, full-stack web application built for managing hotel room inventory and customer reservations.

The system is designed with a layered architecture (MVC/Service/Repository) and uses modern Spring Boot features for quick development and enterprise-level security.

## ✨ Features

  * **Full-Stack Implementation:** Developed using Java, Spring Boot, Spring Data JPA, and Thymeleaf/Bootstrap.
  * **User Authentication:** Secure registration and login using **Spring Security** and **BCrypt** password hashing.
  * **Role-Based Access:** Separate access for **Customer** (booking, viewing history) and **Admin** (room management).
  * **Booking Management:** Allows users to search rooms by dates and location, and confirm reservations.
  * **Database Integration:** Persistence handled by **MySQL** and **Hibernate**.
  * **Modern UI:** Attractive and responsive interface provided by **Bootstrap 5**.
  * **Location Management:** Admin panel feature to manage multiple hotel properties.

-----

## 💻 Tech Stack

| Component | Technology | Role |
| :--- | :--- | :--- |
| **Backend** | Java 17+ / Spring Boot 3 | Application Core, RESTful Endpoints |
| **Data Access** | Spring Data JPA / Hibernate | ORM (Object-Relational Mapping) |
| **Database** | MySQL | Persistent Data Storage |
| **Frontend** | Thymeleaf, HTML5, Bootstrap 5 | Dynamic Templating and UI/Styling |
| **Build Tool** | Maven | Dependency Management & Project Build |

-----

## 🚀 Setup & Installation

### Prerequisites

You must have the following installed on your machine:

  * **Java Development Kit (JDK) 17 or higher**
  * **Apache Maven**
  * **MySQL Server** running locally (default port 3306 is assumed).

### Step 1: Clone the Repository

```bash
git clone https://github.com/YOUR_GITHUB_USERNAME/hotel-booking-system-java.git
cd hotel-booking-system-java
```

### Step 2: Database Setup

1.  **Create Database:** Log into your MySQL client (Workbench or CLI) and create the database:
    ```sql
    CREATE DATABASE hotel_booking_system;
    ```
2.  **Execute Schema Script:** Load the setup script to create tables and insert initial data.
      * Find the script at: `src/main/resources/schema.sql`
      * Execute all commands in this file against the `hotel_booking_system` database.

### Step 3: Configure Credentials

Update the connection settings in the `src/main/resources/application.properties` file:

```properties
# Replace with your actual MySQL credentials
spring.datasource.username=root
spring.datasource.password=praveen@1322?
```

### Step 4: Run the Application

Execute the Maven command from the project root directory:

```bash
mvn spring-boot:run
```

### Step 5: Access the Application

Open your browser and navigate to:

$$\text{http://localhost:8080/}$$

## 🔑 Default Credentials for Testing

| Role | Username | Password | Notes |
| :--- | :--- | :--- | :--- |
| **Admin** | `admin` | `adminpass` (or the password corresponding to the hash in `schema.sql`) | Used for accessing `/admin/rooms` and `/admin/locations`. |
| **Customer** | *(Must Register)* | *(Your Choice)* | Use the `/register` link to create a new customer account. |

-----

## Project Status

The core booking, authentication, and admin inventory features are **complete and stable**.
