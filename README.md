# Hospital Management System (Java + MySQL)

A desktop (Swing) application for managing core hospital entities (Doctors, Patients, Appointments, Billing, etc.) backed by a MySQL database. The UI is tab‑based and built for quick CRUD operations with consistent styling.

## Features
* CRUD management for doctors, patients, appointments, billing
* Tabbed, modular Swing interface (`JTabbedPane`)
* Centralized JDBC connector with basic recovery / guidance prompts
* Structured packages for UI, data access, models, and bootstrap code
* Easily extensible panels (shared abstract `CrudPanel` base)

## Tech Stack
* Java 8+ (Swing)
* MySQL (JDBC)

## Project Structure
```
PackagesTest/
├─ DATABASE_MIGRATION.sql        # Schema + seed
├─ DOCUMENTATION.md              # Additional functional / design notes (if any)
├─ README.md
├─ PackagesTest.iml              # IDE module file (IntelliJ)
└─ src/
   ├─ jdbc.jar                   # Local JDBC driver (consider using Maven/Gradle instead)
   ├─ META-INF/
   │  └─ MANIFEST.MF            # Manifest (add Main-Class if packaging a jar)
   └─ packages/
      ├─ main/
      │  └─ HospitalManagementSystem.java   # Application entry point (has main method)
      ├─ db/
      │  ├─ DatabaseConnector.java          # Handles MySQL connection + basic error loops
      │  ├─ DatabaseException.java          # Helper / user prompts for DB issues
      │  ├─ DatabaseMigration.java          # (If present) programmatic migration logic
      │  └─ ResetQueries.java               # Utility queries / resets
      ├─ model/
      │  ├─ DoctorItem.java                 # Domain model (doctor)
      │  └─ PatientItem.java                # Domain model (patient)
      └─ ui/
         ├─ CrudPanel.java                  # Abstract base with table + actions
         ├─ DoctorPanel.java                # Doctor CRUD implementation
         ├─ PatientPanel.java               # Patient CRUD implementation
         ├─ AppointmentPanel.java           # Appointment CRUD implementation
         ├─ BillingPanel.java               # Billing functionality
         └─ (other panels as added)
```

## Database Setup
1. Start MySQL locally (default port 3306 unless changed).
2. Open `DATABASE_MIGRATION.sql` in your MySQL client (CLI, Workbench, DBeaver, etc.).
3. Execute the script to create the schema + tables.
4. Adjust credentials (user, password, port) in `com.hospital.db.DatabaseConnector` if your local setup differs.
   * Defaults used in code:
     * DB name: `project_hospital_db`
     * User: `root`
     * Password: (empty string)
5. (Optional) Externalize these settings via environment variables or a properties file for production.

## Run the Application
In your IDE (IntelliJ / Eclipse / VS Code Java):
1. Ensure the project SDK is set (Java 8+).
2. Make sure the MySQL server is running.
3. Run the `main` method in `com.hospital.main.HospitalManagementSystem`.
4. Tabs for Doctors, Patients, Appointments, Billing will be available.

### Packaging (Optional Manual Jar)
If you want a runnable jar:
1. Add `Main-Class: com.hospital.main.HospitalManagementSystem` to `META-INF/MANIFEST.MF` (or regenerate).
2. Include the JDBC driver on the classpath when running if not shaded.

## Extending
To add a new entity panel:
1. Create a model class under `com.hospital.model`.
2. Subclass `CrudPanel` under `com.hospital.ui` and implement:
   * `setupForm()` for layout
   * `loadData()` for SELECT queries
   * `addRecord()`, `updateRecord()`, `deleteRecord()` for mutation logic
3. Register the panel by adding a new `tabbedPane.addTab(...)` in `HospitalManagementSystem` constructor.

## Contribution Guidelines
We welcome improvements—UI polish, additional entities, refactoring, tests, and migration tooling.

### Ground Rules
* Keep code modular, prefer small focused methods.
* Follow existing package layout; do not introduce deeply nested structures unless necessary.
* Avoid committing credentials or machine‑specific configs.
* Maintain Swing UI consistency (fonts, spacing) via `UIManager` adjustments rather than per‑component hacks.

### Workflow
1. Fork the repository.
2. Create a feature branch:
   * `feat/<short-description>` for new features
   * `fix/<issue-id-or-bug>` for bug fixes
   * `chore/`, `refactor/`, `docs/` prefixes as appropriate
3. Commit with conventional style:
   * `feat: add patient search filter`
   * `fix: correct doctor email validation`
4. Rebase (or merge) main before opening a PR to avoid conflicts.
5. Open a Pull Request describing:
   * What changed & why
   * Screenshots (if UI changes)
   * DB changes (if any) + snippet of added SQL

### Database Changes
* For schema updates also append changes to `DATABASE_MIGRATION.sql` OR introduce a new incremental file (e.g. `migration_2025_08_add_invoice_table.sql`) and reference it in `DOCUMENTATION.md`.
* Reflect new columns/entities in all affected panels.

### Testing / Validation Checklist (include in PR description)
* [ ] Application starts without exceptions
* [ ] CRUD works for modified entities
* [ ] No hardcoded credentials introduced
* [ ] UI layout not broken on 1366x768 baseline

### Reporting Issues
Please include:
* Steps to reproduce
* Expected vs actual behavior
* Relevant stack trace (if any)
* Screenshot (for UI issues)

### Style & Formatting
* Use standard Java formatting (IntelliJ default okay). Avoid large unrelated reformatting in feature PRs.
* Keep SQL readable (uppercase keywords, snake_case table/column names if adding new ones).

### Security / Data
* No production PHI / real patient data in test fixtures.
* Use mock / anonymized data when sharing screenshots.

## Roadmap (Suggestions)
* Add user authentication / roles
* Introduce Maven/Gradle build instead of manual jar management
* Add logging (slf4j + logback) & application properties
* Implement search / filtering per panel
* Add export (CSV / PDF) for reports

## License
MIT

---
Encounter an issue setting up? Open an issue with logs or stack trace and environment details (OS, JDK, MySQL version).
