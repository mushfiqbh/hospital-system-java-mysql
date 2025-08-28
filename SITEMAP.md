# Clinic Management System Sitemap

This document provides a comprehensive overview of the project structure for the Clinic Management System, a Java-based application for managing clinic operations.

## Project Overview
- **Technology Stack**: Java, Maven, SQLite
- **Architecture**: MVC pattern with DAO layer
- **Main Components**: Database management, User authentication, Appointment scheduling, Billing system

## Directory Structure

### Root Directory
- `clinic_management.db` - SQLite database file
- `pom.xml` - Maven project configuration
- `SITEMAP.md` - This sitemap document

### src/
- `sqlite-jdbc.jar` - SQLite JDBC driver
- `main/`
  - `java/`
    - `com/`
      - `clinic/`
        - `Main.java` - Application entry point
        - `config/`
          - `DatabaseHelper.java` - Database connection and configuration
        - `dao/` - Data Access Objects
          - `AppointmentDao.java` - Appointment data operations
          - `BillingDao.java` - Billing data operations
          - `DoctorDao.java` - Doctor data operations
          - `PatientDao.java` - Patient data operations
          - `UserDao.java` - User data operations
        - `model/` - Data models
          - `Appointment.java` - Appointment entity
          - `Bill.java` - Bill entity
          - `BillingDetails.java` - Billing details entity
          - `Doctor.java` - Doctor entity
          - `Patient.java` - Patient entity
          - `User.java` - User entity
        - `ui/` - User Interface components
          - `AccountantPanel.java` - Accountant interface
          - `AdminPanel.java` - Administrator interface
          - `DoctorPanel.java` - Doctor interface
          - `LoginFrame.java` - Login screen
          - `MainFrame.java` - Main application frame
          - `ReceptionistPanel.java` - Receptionist interface
  - `resources/`
    - `META-INF/`
      - `MANIFEST.MF` - JAR manifest file

- `test/`
  - `java/` - Unit test directory (currently empty)

### target/
- `classes/` - Compiled Java classes
  - `com/`
    - `clinic/`
      - `Main.class` - Compiled Main class
      - `config/`
        - `DatabaseHelper.class` - Compiled DatabaseHelper class
      - `dao/` - Compiled DAO classes
        - `AppointmentDao.class`
        - `BillingDao.class`
        - `DoctorDao.class`
        - `PatientDao.class`
        - `UserDao.class`
      - `model/` - Compiled model classes
        - `Appointment.class`
        - `Bill.class`
        - `BillingDetails.class`
        - `Doctor.class`
        - `Patient.class`
        - `User.class`
      - `ui/` - Compiled UI classes
        - `AccountantPanel.class`
        - `AccountantPanel$1.class` - Inner class
        - `AdminPanel.class`
        - `AdminPanel$1.class` - Inner class
        - `DoctorPanel.class`
        - `LoginFrame.class`
        - `MainFrame.class`
        - `ReceptionistPanel.class`
    - `META-INF/`
      - `MANIFEST.MF` - JAR manifest file

- `generated-sources/`
  - `annotations/` - Generated annotation sources

- `test-classes/` - Compiled test classes (currently empty)

## Key Features
- **User Management**: Role-based access (Admin, Doctor, Receptionist, Accountant)
- **Patient Management**: Patient registration and records
- **Appointment Scheduling**: Doctor-patient appointment management
- **Billing System**: Invoice generation and payment tracking
- **Database Integration**: SQLite for data persistence

## File Categories
- **Source Files**: `.java` files in `src/main/java/`
- **Configuration**: `pom.xml`, `MANIFEST.MF`
- **Database**: `clinic_management.db`, `DatabaseHelper.java`
- **UI Components**: Classes in `ui/` package
- **Data Layer**: DAO and Model classes
- **Compiled Output**: `.class` files in `target/classes/`

---
*Last updated: August 28, 2025*