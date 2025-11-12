-- Creamos la base de datos si no existe
CREATE DATABASE IF NOT EXISTS historia_clinica_db;

-- Usamos la base de datos para trabajar en ella
USE historia_clinica_db;

-- Creamos la tabla paciente si no existe
CREATE TABLE IF NOT EXISTS paciente (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    eliminado BOOLEAN DEFAULT FALSE,
    nombre VARCHAR(80) NOT NULL,
    apellido VARCHAR(80) NOT NULL,
    dni VARCHAR(15) NOT NULL UNIQUE,
    fecha_nacimiento DATE
);

-- Creamos la tabla historia_clinica si no existe
CREATE TABLE IF NOT EXISTS historia_clinica (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    eliminado BOOLEAN DEFAULT FALSE,
    nro_historia VARCHAR(20) UNIQUE,
    grupo_sanguineo ENUM('A+', 'A-', 'B+', 'B-', 'AB+', 'AB-', 'O+', 'O-'),
    antecedentes TEXT,
    medicacion_actual TEXT,
    observaciones TEXT,
    paciente_id BIGINT UNIQUE,
    CONSTRAINT fk_historia_clinica_paciente
		FOREIGN KEY (paciente_id) 
		REFERENCES paciente(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);