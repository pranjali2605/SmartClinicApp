# SmartClinicApp

SmartClinicApp is a Java-based desktop application developed to simulate a smart clinic management system. It supports comprehensive management of patients, doctors, and appointments with persistent data storage using MySQL and data processing logic implemented using standard data structures and algorithms (DSA). The application features a graphical user interface built with Java Swing and interacts with a backend MySQL database through JDBC.

## Features

### Doctor Management
- Add, update, delete, and view doctor records
- Assign specializations and time slots
- Search by ID, name, or specialization
- Sort doctors by name

### Patient Management
- Add, update, delete, and view patient records
- Input and store patient issues, age, and contact details
- Search patients by name, contact number, or issue

### Appointment Management
- Book appointments based on patient issues and doctor specialization
- Automatically map issues to medical specializations
- Check availability of doctors based on date and time slot
- Add patients to a waitlist if a slot is unavailable
- Update or cancel existing appointments
- Sort appointments by date and time
- Search appointments by patient name, doctor ID, or date

## Technologies Used

- Java SE 8+
- Java Swing (GUI)
- MySQL (Relational Database)
- JDBC (Java Database Connectivity)
- IntelliJ IDEA (Recommended IDE)

## Project Structure
````
SmartClinicApp/
├── .idea/                     # IntelliJ IDEA project configuration
│   └── libraries/
├── lib/                       # External libraries (e.g., MySQL JDBC driver)
├── out/                       # Compiled output (auto-generated)
│   └── production/
│       └── SmartClinicApp/
│           └── com/
│               └── smartclinic/
│                   ├── app/   # GUI menus and application launcher
│                   ├── dao/   # Database access layer
│                   ├── model/ # Data models (Patient, Doctor, Appointment)
│                   ├── service/ # Business logic and DSA implementations
│                   └── util/  # Utility helpers (e.g., SpecializationMapper)
├── sql/                       # SQL scripts for DB setup
│   └── schema.sql
├── src/                       # Source code root
│   └── com/
│       └── smartclinic/
│           ├── app/           # GUI menus and application launcher
│           ├── dao/           # Database access layer
│           ├── model/         # Data models (Patient, Doctor, Appointment)
│           ├── service/       # Business logic and DSA implementations
│           └── util/          # Utility helpers (e.g., SpecializationMapper)
└── README.md                  # Project documentation

````
## Data Structures and Algorithms (DSA) Implementation

- Queue: Used to manage patient waitlists when time slots are unavailable.
- LinkedList: Used for storing and manipulating collections of appointments and doctors.
- Comparator: Used for custom sorting of lists (e.g., appointments by date).
- Binary Search: Applied for efficient search of appointments by patient name.

## Database Schema

The application requires a MySQL database. Execute the `sql/schema.sql` script to create the following tables:

- `patients` (id, name, age, contact, gender, issue)
- `doctors` (id, name, specialization, time_slots)
- `appointments` (id, patient_id, doctor_id, issue, date, time_slot, status)

Database credentials must be configured in `DBConnection.java`.

## Setup Instructions

### 1. Clone the Repository

git clone https://github.com/pranjali2605/SmartClinicApp.git
cd SmartClinicApp


### 2. Configure the Database

- Ensure MySQL is installed and running
- Create a database named `smartclinic` or use the provided `schema.sql` script:

mysql -u your_username -p < sql/schema.sql


- Open `DBConnection.java` and update your credentials:

java
String url = "jdbc:mysql://localhost:3306/smartclinic";
String user = "your_mysql_user";
String password = "your_mysql_password";

### 3. Run the Application
Open the project in IntelliJ IDEA (or any Java IDE)

Run SmartClinicApp.java located in com.smartclinic.app

### License

This project is provided for academic and educational purposes. It is free to use and modify with appropriate credit to the original author.

### Author
Developed by Pranjali K.

GitHub: pranjali2605