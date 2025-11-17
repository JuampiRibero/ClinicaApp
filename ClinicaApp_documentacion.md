# Documentación del Proyecto ClinicaApp

## 1. Introducción

Este documento describe la estructura, componentes y funcionamiento del proyecto **ClinicaApp**, basado únicamente en los paquetes y clases provistos. El sistema se centra en la gestión de **pacientes**, **historias clínicas**, **grupos sanguíneos** y una configuración mínima de conexión a la ase de datos. No incluye módulos de turnos, médicos u otros componentes externos.

---

## 2. Estructura del Proyecto

    ClinicaApp
    │── Source Packages
    │   │── config
    │   │   └── DatabaseConnection.java
    │   │   └── TransactionManager.java
    │   │── dao
    │   │   └── GenericDAO<T>
    │   │   └── PacienteDAO.java
    │   │   └── HistoriaClinicaDAO.java
    │   │── main
    │   │   └── Main.java
    │   │   └── AppMenu.java
    │   │   └── MenuHandler.java
    │   │   └── MenuDisplay.java
    │   │   └── TestConnection.java
    │   │── models
    │   │   ├── Base.java
    │   │   ├── GrupoSanguineo.java
    │   │   ├── HistoriaClinica.java
    │   │   └── Paciente.java
    │   └── service
    │   │   └── GenericService<T>
    │   │   └── PacienteServiceImpl.java
    │   │   └── HistoriaClinicaServiceImpl.java
    │   │   └── PacienteHistoriaService.java
    │── Test Packages
    │── Libraries
    └── Test Libraries

---

## 3. Paquetes y Responsabilidades

### 3.1 config

Contiene las clases responsables de **configuración general del sistema**.

#### ✔ DatabaseConnection.java

- Maneja la conexión con la base de datos.
- Centraliza URL, usuario, contraseña.
- Expone métodos para obtener conexiones vivas (Connection).
- Controla excepciones SQL relacionadas con conexión.

#### ✔ TransactionManager.java

- Maneja las transacciones con AutoCloseable.

---

### 3.2 dao

Capa donde deberían ubicarse las clases de acceso a datos (**Data Access Objects**).
Aunque no se listan clases específicas en este módulo, este paquete es responsable de:

- Consultas SQL.
- Inserciones, actualizaciones y eliminaciones.
- Conversión entre resultados JDBC y modelos.

En esta versión del proyecto, el paquete está preparado pero sin implementación incluida en el listado.

---

### 3.3 main

Contiene clases auxiliares o utilitarias vinculadas al flujo principal
del proyecto.

#### ✔ TestConnection.java

Clase diseñada para probar la conexión a la base de datos.

Responsabilidades:

- Ejecutar una conexión simple utilizando `DatabaseConnection`.
- Verificar que la base está accesible.
- Confirmar parámetros correctos de configuración.

---

### 3.4 models

Define el **modelo de dominio** del sistema.

#### ✔ Base.java

Clase base para todas las entidades del sistema, con responsabilidades
como:

- Manejar el ID de cada entidad.
- Registrar fechas de creación/modificación.
- Proveer estructura común para herencia.

---

#### ✔ GrupoSanguineo.java

Representa los tipos de grupo sanguíneo asociados a un paciente.

Debe manejar:

- Enumeración de tipos (A+, A-, B+, O-, etc.).
- Validación de valores.
- Asociación con el paciente (si corresponde).

---

#### ✔ HistoriaClinica.java

Entidad que representa la historia clínica del paciente.

Responsabilidades:

- Asociarse **1 a 1** con el paciente.
- Registrar antecedentes, observaciones y datos médicos esenciales.
- Fecha de creación obligatoria.
- Base para futuras ampliaciones (consultas, diagnósticos, etc.).

---

#### ✔ Paciente.java

Entidad principal del sistema.

Contiene:

- Datos personales (nombre, apellido, DNI, email, etc.).
- Grupo sanguíneo (objeto GrupoSanguineo).
- Historia clínica (objeto HistoriaClinica).
- Métodos de acceso y validación.

Relaciones principales:

    Paciente 1 — 1 HistoriaClinica
    Paciente 1 — 1 GrupoSanguineo

---

### 3.5 service

Capa destinada a la lógica de negocio.

Responsabilidades esperadas:

- Implementar operaciones CRUD para las entidades.
- Manejar validaciones previas a persistencia.
- Integrarse con la capa DAO.
- Preparar lógica para transacciones si se agregan más operaciones en el futuro.

---

## 4. Flujo Básico del Sistema

### 4.1 Creación de Paciente

1.  Se crea objeto `Paciente`.
2.  Se valida DNI y datos obligatorios.
3.  Se asigna un `GrupoSanguineo`.
4.  Se genera automáticamente `HistoriaClinica`.
5.  DAO persiste la información en BD.

---

### 4.2 Consulta de Datos

1.  Usuario solicita datos al servicio.
2.  Service pide datos al DAO.
3.  DAO usa `DatabaseConnection` para ejecutar la consulta.
4.  DAO transforma resultados JDBC → objetos de dominio.
5.  Service devuelve los modelos al usuario.

---

## 5. Reglas de Negocio Principales

### 5.1 Paciente

- DNI obligatorio y único.
- Historia clínica debe existir siempre.
- Grupo sanguíneo válido según enumeración.

### 5.2 Historia Clínica

- No puede existir sin paciente.
- Idealmente no debe eliminarse físicamente.
- Debe registrar fecha de creación.

### 5.3 Conexión a Base de Datos

- Toda operación usa `DatabaseConnection`.
- Errores SQL deben manejarse adecuadamente.

---

## 6. Conclusión

El proyecto **ClinicaApp** presenta una arquitectura modular y sólida. La estructura actual es funcional para un sistema básico de gestión de pacientes e historias clínicas, con capacidad de ampliación.
Este documento describe claramente cada capa y su propósito dentro del sistema.
