# Clinic Management System

A comprehensive Java-based application for managing clinic operations, built with MVC architecture and SQLite database integration.

## 📋 Table of Contents
- [Features](#features)
- [Technology Stack](#technology-stack)
- [Prerequisites](#prerequisites)
- [Installation](#installation)
- [Usage](#usage)
- [Project Structure](#project-structure)
- [Database Setup](#database-setup)
- [Contributing](#contributing)
- [License](#license)

## ✨ Features

### 👥 User Management
- **Role-based Access Control**: Admin, Doctor, Receptionist, and Accountant roles
- **Secure Authentication**: Login system with role-based permissions
- **User Profile Management**: Create, update, and manage user accounts

### 🏥 Patient Management
- **Patient Registration**: Complete patient information management
- **Medical Records**: Store and retrieve patient history
- **Patient Search**: Quick patient lookup functionality

### 📅 Appointment Scheduling
- **Appointment Booking**: Schedule appointments with doctors
- **Appointment Management**: View, update, and cancel appointments
- **Doctor Availability**: Check doctor schedules and availability

### 💰 Billing System
- **Invoice Generation**: Automatic bill creation for services
- **Payment Tracking**: Monitor payment status and history
- **Billing Reports**: Generate financial reports

### 🗄️ Database Integration
- **SQLite Database**: Lightweight, file-based database
- **Data Persistence**: Secure data storage and retrieval
- **Backup Support**: Database backup and restore functionality

## 🛠️ Technology Stack

- **Language**: Java 8+
- **Build Tool**: Maven
- **Database**: SQLite
- **Architecture**: MVC (Model-View-Controller)
- **UI Framework**: Swing (Java GUI)
- **Database Driver**: SQLite JDBC

## 📋 Prerequisites

Before running this application, make sure you have the following installed:

- **Java Development Kit (JDK)**: Version 8 or higher
- **Maven**: Version 3.6 or higher
- **Git**: For cloning the repository (optional)

### Verifying Installation

```bash
# Check Java version
java -version

# Check Maven version
mvn -version
```

## 🚀 Installation

1. **Clone the Repository**
   ```bash
   git clone https://github.com/mushfiqbh/hospital-system-java-mysql.git
   cd hospital-system-java-mysql
   ```

2. **Navigate to Project Directory**
   ```bash
   cd Clinic-Mangement-System
   ```

3. **Install Dependencies**
   ```bash
   mvn clean install
   ```

4. **Compile the Project**
   ```bash
   mvn compile
   ```

## 🎯 Usage

### Running the Application

1. **Using Maven**
   ```bash
   mvn exec:java -Dexec.mainClass="com.clinic.main.Main"
   ```

2. **Using Java directly**
   ```bash
   java -cp target/classes com.clinic.main.Main
   ```

### First Time Setup

1. **Database Initialization**: The application will automatically create the SQLite database on first run
2. **Default Admin Account**: Create an admin account during initial setup
3. **Configuration**: Update database settings in `DatabaseHelper.java` if needed

### User Roles and Access

- **Admin**: Full system access, user management, system configuration
- **Doctor**: Patient management, appointment scheduling, medical records
- **Receptionist**: Appointment booking, patient registration, basic data entry
- **Accountant**: Billing management, financial reports, payment processing

## 📁 Project Structure

```
Clinic-Mangement-System/
├── clinic_management.db          # SQLite database file
├── pom.xml                       # Maven project configuration
├── SITEMAP.md                    # Project structure documentation
└── src/
    ├── main/
    │   ├── java/
    │   │   └── com/
    │   │       └── clinic/
    │   │           ├── Main.java              # Application entry point
    │   │           ├── config/
    │   │           │   └── DatabaseHelper.java # Database configuration
    │   │           ├── dao/                   # Data Access Objects
    │   │           │   ├── AppointmentDao.java
    │   │           │   ├── BillingDao.java
    │   │           │   ├── DoctorDao.java
    │   │           │   ├── PatientDao.java
    │   │           │   └── UserDao.java
    │   │           ├── model/                 # Data Models
    │   │           │   ├── Appointment.java
    │   │           │   ├── Bill.java
    │   │           │   ├── BillingDetails.java
    │   │           │   ├── Doctor.java
    │   │           │   ├── Patient.java
    │   │           │   └── User.java
    │   │           └── ui/                    # User Interface
    │   │               ├── AccountantPanel.java
    │   │               ├── AdminPanel.java
    │   │               ├── DoctorPanel.java
    │   │               ├── LoginFrame.java
    │   │               ├── MainFrame.java
    │   │               └── ReceptionistPanel.java
    │   └── resources/
    │       └── META-INF/
    │           └── MANIFEST.MF
    └── test/
        └── java/                    # Unit tests (expandable)
```

## 🗄️ Database Setup

The application uses SQLite as the database system. The database file `clinic_management.db` is automatically created in the project root directory.

### Database Tables

- **users**: User accounts and authentication
- **patients**: Patient information and records
- **doctors**: Doctor profiles and specializations
- **appointments**: Appointment scheduling and management
- **bills**: Billing and payment information
- **billing_details**: Detailed billing records

### Database Configuration

Database settings can be modified in `src/main/java/com/clinic/config/DatabaseHelper.java`:

```java
// Default database configuration
private static final String DB_URL = "jdbc:sqlite:clinic_management.db";
```

## 🤝 Contributing

We welcome contributions to improve the Clinic Management System!

### Development Setup

1. Fork the repository
2. Create a feature branch: `git checkout -b feature-name`
3. Make your changes
4. Run tests: `mvn test`
5. Commit your changes: `git commit -am 'Add new feature'`
6. Push to the branch: `git push origin feature-name`
7. Submit a pull request

### Code Style

- Follow Java naming conventions
- Use meaningful variable and method names
- Add comments for complex logic
- Maintain MVC architecture patterns

### Testing

```bash
# Run unit tests
mvn test

# Run with test coverage
mvn test jacoco:report
```

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 📞 Support

If you encounter any issues or have questions:

1. Check the [Issues](https://github.com/mushfiqbh/hospital-system-java-mysql/issues) page
2. Create a new issue with detailed description
3. Contact the maintainers

## 🔄 Version History

- **v1.0.0**: Initial release with core clinic management features
  - User authentication and role management
  - Patient registration and management
  - Appointment scheduling system
  - Billing and payment tracking

---

**Last Updated**: August 28, 2025
**Repository**: [hospital-system-java-mysql](https://github.com/mushfiqbh/hospital-system-java-mysql)