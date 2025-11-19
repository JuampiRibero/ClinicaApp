# Trabajo Práctico Integrador - Programación II

## Sistema de Gestión de Pacientes e Historias Clínicas

### Integrantes

- Re, Julian.
- Reinaudo, María Celeste.
- Ribero Mazzoni, Juan Pablo.
- Rivas, Alfredo Guillermo.

### Grupo: 60

### Comisión: 17

### Descripción del Proyecto

En el presente trabajo práctico integrador se documenta el desarrollo de una Aplicación Java que implementa los principios de la Programación Orientada a Objetos y patrones de diseño para la gestión de datos persistentes. El proyecto consiste en desarrollar un sistema de gestión de pacientes e historias clínicas que permita realizar operaciones CRUD, implementando una arquitectura robusta y profesional. Las dos entidades, Paciente e HistoriaClinica, están relacionadas mediante una asociación unidireccional uno a uno (1→1).

### Objetivos Académicos

El desarrollo de este sistema permite aplicar y consolidar los siguientes conceptos clave de la materia:

**1. Arquitectura en Capas**

- Implementación de separación de responsabilidades en 4 capas diferenciadas.
- Capa de Presentación (Main/UI): Interacción con el usuario mediante consola.
- Capa de Lógica de Negocio (Service): Validaciones y reglas de negocio.
- Capa de Acceso a Datos (DAO): Operaciones de persistencia.
- Capa de Modelo (Models): Representación de entidades del dominio.

**2. Programación Orientada a Objetos**

- Aplicación de principios SOLID (Single Responsibility, Dependency Injection).
- Uso de herencia mediante clase abstracta Base.
- Implementación de interfaces genéricas (GenericDAO, GenericService).
- Encapsulamiento con atributos privados y métodos de acceso.
- Sobrescritura de métodos (equals, hashCode, toString).

**3. Persistencia de Datos con JDBC**

- Conexión a base de datos MySQL mediante JDBC.
- Implementación del patrón DAO (Data Access Object).
- Uso de PreparedStatements para prevenir SQL Injection.
- Gestión de transacciones con commit y rollback.
- Manejo de claves autogeneradas (AUTO_INCREMENT).
- Consultas con LEFT JOIN para relaciones entre entidades.

**4. Manejo de Recursos y Excepciones**

- Uso del patrón try-with-resources para gestión automática de recursos JDBC.
- Implementación de AutoCloseable en TransactionManager.
- Manejo apropiado de excepciones con propagación controlada.
- Validación multi-nivel: base de datos y aplicación.

**5. Patrones de Diseño**

- Factory Pattern (DatabaseConnection).
- Service Layer Pattern (separación lógica de negocio).
- DAO Pattern (abstracción del acceso a datos).
- Soft Delete Pattern (eliminación lógica de registros).
- Dependency Injection manual.

**6. Validación de Integridad de Datos**

- Validación de unicidad (DNI único por paciente).
- Validación de campos obligatorios en múltiples niveles.
- Validación de integridad referencial (Foreign Keys).
- Implementación de eliminación segura para prevenir referencias huérfanas.

### Funcionalidades Implementadas

El sistema permite gestionar dos entidades principales con las siguientes operaciones:

## Características Principales

- **Gestión de Pacientes**: crear, listar, actualizar y eliminar pacientes con validación de DNI único.
- **Gestión de Historias Clínicas**: administrar historias clínicas de forma independiente o asociados a pacientes.
- **Búsqueda Inteligente**: filtrar pacientes por ID o DNI.
- **Soft Delete**: eliminación lógica que preserva la integridad de datos.
- **Seguridad**: protección contra SQL injection mediante PreparedStatements.
- **Validación Multi-capa**: validaciones en capa de servicio y base de datos.
- **Transacciones**: soporte para operaciones atómicas con rollback automático.

## Instalación

### 1. Configurar Base de Datos

Ejecutar el siguiente script SQL:

```sql
CREATE DATABASE IF NOT EXISTS historia_clinica_db;

USE historia_clinica_db;

CREATE TABLE IF NOT EXISTS paciente (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    eliminado BOOLEAN DEFAULT FALSE,
    nombre VARCHAR(80) NOT NULL,
    apellido VARCHAR(80) NOT NULL,
    dni VARCHAR(15) NOT NULL UNIQUE,
    fecha_nacimiento DATE
);

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
```

### 2. Carga de Datos para Test

```sql
INSERT INTO	paciente (eliminado, nombre, apellido, dni, fecha_nacimiento)
VALUES
(FALSE, 'María', 'Gómez', '35111222', '1990-05-14'),
(FALSE, 'Juan', 'Pérez', '34455987', '1989-09-20'),
(FALSE, 'Lucía', 'Martínez', '39100555', '1995-11-02');

INSERT INTO historia_clinica (eliminado, nro_historia, grupo_sanguineo, antecedentes, medicacion_actual, observaciones, paciente_id)
VALUES
(FALSE, 'HC-0001', 'A+', 'Hipertensión leve', 'Losartán 50mg', 'Control mensual', 1),
(FALSE, 'HC-0002', 'O-', 'Asma bronquial', 'Salbutamol', 'Evitar humedad', 2),
(FALSE, 'HC-0003', 'B+', 'Ninguno', 'Ninguno', 'Paciente en buen estado', 3);
```

### 3. Configurar Conexión

Por defecto conecta a:

- **Host**: localhost:3306.
- **Base de datos**: historia_clinica_db
- **Usuario**: root
- **Contraseña**: (vacía)

## Ejecución desde IDE

1. Abrir proyecto en Apache Netbeans.
2. Ejecutar clase `Main.java`.

### Verificar Conexión

1. Ejecutar clase `TestConnection.java`.

Salida esperada:

```
Conexión a la base de datos establecida exitosamente.

Listado de pacientes:

ID: 1, Nombre: María, Apellido: Gómez, DNI: 35111222, N° Historia: HC-0001, Grupo Sanguíneo: A+
------------------------------------------------------------------------------------------------------
ID: 2, Nombre: Juan, Apellido: Pérez, DNI: 34455987, N° Historia: HC-0002, Grupo Sanguíneo: O-
------------------------------------------------------------------------------------------------------
ID: 3, Nombre: Lucía, Apellido: Martínez, DNI: 39100555, N° Historia: HC-0003, Grupo Sanguíneo: B+
------------------------------------------------------------------------------------------------------
```

## Arquitectura

## Estructura de Directorios

```
ClinicaApp/
├── src
│   ├── config/
│   ├── dao/
│   ├── main/
│   ├── models/
│   └── service/
├── sql
└── HISTORIAS_DE_USUARIO.md
├── README.md
└── UML.pdf
```

### Componentes Principales

**Config/**

- `DatabaseConnection.java`: Gestión de conexiones JDBC con validación en inicialización estática.
- `TransactionManager.java`: Manejo de transacciones con AutoCloseable.

**Models/**

- `Base.java`: Clase abstracta con campos id y eliminado.
- `Paciente.java`: Entidad Paciente (nombre, apellido, dni, fechaNacimiento, historiaClinica).
- `HistoriaClinica.java`: Entidad HistoriaClinica (nroHistoria, grupoSanguineo, antecedentes, medicacionActual, observaciones).
- `GrupoSanguineo.java`: Entidad GrupoSanguineo (A+, A-, B+, B-, AB+, AB-, O+, O-).

**Dao/**

- `GenericDAO<T>`: Interface genérica con operaciones CRUD.
- `PacienteDAO`: Implementación para pacientes.
- `HistoriaClinicaDAO`: Implementación para historias clinicas.

**Service/**

- `GenericService<T>`: Interface genérica para servicios.
- `PacienteServiceImpl`: Validaciones de paciente.
- `HistoriaClinicaServiceImpl`: Validaciones de historia clínica.
- `PacienteHistoriaServiceImpl`: Coordinación entre pacientes e historias clínicas..

**Main/**

- `Main.java`: Punto de entrada.
- `AppMenu.java`: Orquestador del ciclo de menú.
- `MenuHandler.java`: Implementación de operaciones CRUD con captura de entrada.
- `MenuDisplay.java`: Lógica de visualización de menús.
- `TestConexion.java`: Utilidad para verificar conexión a la base de datos.

## Link al video
- https://youtu.be/cXPqpRnI2IM
