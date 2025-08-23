Scrum Documentation – Hospital Management System

Sprint 0
Backlog 1: Database Migration
Task 1
Create a new file and class DatabaseMigration
Create method runMigrations() to run full database migration
Connect to DB, fetch reset SQL from ResetQueries.java and execute it
Add error handling with UI messages
Commit: Add runMigrations method to reset and initialize database

Backlog 2: Database Exception
Task 1
DatabaseException.missingDatabase()
Check if DB is missing
Ask user confirmation for auto-creation
Run migration if DB created successfully
Commit: Add missingDatabase handler with auto-create and migration support
Task 2
DatabaseException.promptForPort()
Prompt user for custom MySQL port
Return new DB connection string
Fallback to default 3306 if user cancels
Commit: Add promptForPort method for dynamic MySQL connection URL
Task 3
DatabaseException.createDatabase()
Connect to MySQL root
Run SQL query to create new database
Show success or failure message
Commit: Implement createDatabase with SQL execution and error handling
Backlog 3: Database Connection
Task 1
DatabaseConnector.getConnection()
Connect to DB using credentials
Retry until successful connection
Delegate exception handling
Commit: Implement getConnection with retry loop and exception delegation
Task 2
DatabaseConnector.handleSQLException()
Handle database not found → call missingDatabase()
Handle port/connection failure → call promptForPort()
Handle access denied → notify user and exit
Handle other unexpected errors
Commit: Add handleSQLException for DB-specific error handling

Sprint 1
Backlog 1: Main Application
Task 1
HospitalManagementSystem.HospitalManagementSystem()
Create main JFrame window
Set title, size, and close behavior
Add JTabbedPane with tabs for Doctors, Patients, Appointments, and Billings
Commit: Add HospitalManagementSystem JFrame with tabbed panels
Task 2
HospitalManagementSystem.main()
Set modern system look & feel
Customize Swing component fonts and sizes for consistency
Handle initialization errors gracefully
Launch UI in Event Dispatch Thread
Commit: configure UIManager styles and launch main application

Backlog 2: Doctor Item
Task 1
DoctorItem.DoctorItem(int id, String name)
Constructor to store doctor ID and name
Support data binding for combo box items
DoctorItem.toString()
Override toString() to return doctor’s name
Ensures combo box displays readable text
Commit: Add DoctorItem model with id-name pair and override toString

Backlog 3: Patient Item
Task 1
PatientItem.PatientItem(int id, String name)
Constructor to store patient ID and name
Support data binding for combo box items
PatientItem.toString()
Override toString() to return patient’s name
Ensures combo box displays readable text
Commit: Add PatientItem model with id-name pair and override toString

Sprint 2
Backlog 1: Abstract CRUD Panel
Task 1
CrudPanel.CrudPanel()
Implement reusable layout with:
formPanel (GridBagLayout) for form fields
Buttons: Add, Update, Delete, Clear
JTable + DefaultTableModel for records
Disable Update/Delete until row is selected
Commit: Add abstract CrudPanel with form, table, and common CRUD controls
Task 2
Abstract methods for subclasses
Define setupForm(), loadData(), addRecord(), updateRecord(), deleteRecord(), populateFormFromSelectedRow(), clearForm()
Force subclasses to implement their own logic
Commit: Define abstract methods in CrudPanel for subclass-specific CRUD logic
Backlog 2: Doctor Management Panel
Task 1
DoctorPanel.DoctorPanel()
Extend CrudPanel
Implement doctor form fields (Name, Specialization, etc.)
Load doctor data into table
Commit: Implement DoctorPanel extending CrudPanel with doctor form
Task 2
Doctor CRUD integration
Implement addRecord(), updateRecord(), deleteRecord()
Refresh table after DB operations
Commit: Connect DoctorPanel with DB for CRUD operations

Backlog 3: Patient Management Panel
Task 1
PatientPanel.PatientPanel()
Extend CrudPanel
Implement patient form fields (Name, Age, Gender, etc.)
Load patient data into table
Commit: Implement PatientPanel extending CrudPanel with patient form
Task 2
Patient CRUD integration
Implement addRecord(), updateRecord(), deleteRecord()
Refresh table dynamically
Commit: Connect PatientPanel with DB for CRUD operations

Backlog 4: Appointment Management Panel
Task 1
AppointmentPanel.AppointmentPanel()
Extend CrudPanel
Implement form with JComboBox<DoctorItem> & JComboBox<PatientItem>
Add date/time fields, Load appointment list
Commit: Implement AppointmentPanel with doctor/patient combo and CRUD form
Task 2
Appointment CRUD integration
Insert, update, delete appointments in DB
Refresh appointment table dynamically
Commit: Connect AppointmentPanel with DB for CRUD operations

Backlog 5: Billing Management Panel
Task 1
BillingPanel.BillingPanel()
Extend CrudPanel
Implement billing form fields (Patient, Amount, Date, Status)
Load billing records
Commit: Implement BillingPanel extending CrudPanel with billing form
Task 2
Billing CRUD integration
Insert, update, delete billing records in DB
Refresh billing table
Commit: Connect BillingPanel with DB for CRUD operations





