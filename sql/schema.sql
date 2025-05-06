CREATE DATABASE IF NOT EXISTS smart_clinic;
USE smart_clinic;

-- Patients (matches PatientDAO’s INSERT/SELECT on `patients`)
CREATE TABLE IF NOT EXISTS patients (
  id      VARCHAR(10)    PRIMARY KEY,
  name    VARCHAR(100)   NOT NULL,
  age     INT            NOT NULL,
  contact VARCHAR(15)    NOT NULL,
  gender  ENUM('Male','Female','Other') NOT NULL,
  issue   VARCHAR(255)   NOT NULL
);

-- Doctors (matches DoctorDAO’s INSERT/SELECT on `doctors`)
CREATE TABLE IF NOT EXISTS doctors (
  id             VARCHAR(10)   PRIMARY KEY,
  name           VARCHAR(100)  NOT NULL,
  specialization VARCHAR(100)  NOT NULL,
  time_slots     TEXT
);

-- Appointments (matches AppointmentDAO’s INSERT/SELECT on `appointments`)
CREATE TABLE IF NOT EXISTS appointments (
  id            VARCHAR(10)   PRIMARY KEY,
  patient_id    VARCHAR(10)   NOT NULL,
  doctor_id     VARCHAR(10)   NOT NULL,
  date          DATE          NOT NULL,
  time_slot     VARCHAR(20),
  status        VARCHAR(20),
  FOREIGN KEY (patient_id) REFERENCES patients(id) ON DELETE CASCADE,
  FOREIGN KEY (doctor_id)  REFERENCES doctors(id)  ON DELETE CASCADE
);