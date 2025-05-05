CREATE DATABASE IF NOT EXISTS smart_clinic;
USE smart_clinic;

-- Patient Table
CREATE TABLE IF NOT EXISTS Patient (
    id VARCHAR(10) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    age INT NOT NULL,
    contact VARCHAR(15) NOT NULL,
    gender ENUM('Male', 'Female', 'Other') NOT NULL,
    issue VARCHAR(255) NOT NULL
);

-- Doctor Table
CREATE TABLE IF NOT EXISTS Doctor (
    id VARCHAR(10) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    specialization VARCHAR(100) NOT NULL,
    time_slots TEXT
);

-- Appointment Table
CREATE TABLE IF NOT EXISTS Appointment (
    id VARCHAR(10) PRIMARY KEY,
    patient_id VARCHAR(10) NOT NULL,
    doctor_id VARCHAR(10) NOT NULL,
    appointment_date DATE NOT NULL,
    time_slot VARCHAR(20),
    status VARCHAR(20),
    FOREIGN KEY (patient_id) REFERENCES Patient(id) ON DELETE CASCADE,
    FOREIGN KEY (doctor_id) REFERENCES Doctor(id) ON DELETE CASCADE
);