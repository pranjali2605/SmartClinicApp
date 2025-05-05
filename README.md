# SmartClinicApp

SmartClinicApp is a Java-based console application that provides CRUD operations for managing patients, doctors, and appointments using MySQL and JDBC.

## 📁 Folder Structure

- `app`: Menu classes for user interaction
- `dao`: Data access layer for database operations
- `model`: JavaBeans for Patient, Doctor, Appointment
- `service`: Business logic layer
- `util`: Utility/helper classes (Date formatting, input validation)
- `sql`: SQL schema file
- `lib`: External libraries (MySQL connector)

## 💻 Technologies

- Java
- MySQL
- JDBC
- IntelliJ IDEA

## 🧱 Setup Instructions

1. Clone the repo and open in IntelliJ
2. Import the MySQL JDBC driver (already in `/lib`)
3. Create the database using `sql/schema.sql`
4. Run `SmartClinicApp.java`

## 🔧 Features

- Add/View/Update/Delete Patients
- Add/View/Update/Delete Doctors
- Book/Cancel Appointments
- Appointment Queue Management

## 🔐 Author

Pranjali Garg
