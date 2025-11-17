package config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Clase utilitaria para gestionar conexiones a la base de datos MySQL.
 */
public final class DatabaseConnection {
    // URL de conexión JDBC. Configurable via -Ddb.url.
    private static final String URL = System.getProperty("db.url", "jdbc:mysql://localhost:3306/historia_clinica_db");

    // Usuario de la base de datos. Configurable via -Ddb.user.
    private static final String USER = System.getProperty("db.user", "root");

    // Contraseña del usuario. Configurable via -Ddb.password.
    private static final String PASSWORD = System.getProperty("db.password", "");

    /**
     * Bloque de inicialización estática.
     * Se ejecuta una sola vez cuando la clase se carga en memoria.
     */
    static {
        try {
            // Carga explícita del driver.
            Class.forName("com.mysql.cj.jdbc.Driver");
            // Valida configuración tempranamente.
            validateConfiguration();
        } catch (ClassNotFoundException e) {
            throw new ExceptionInInitializerError("No se encontró el driver JDBC de MySQL: " + e.getMessage());
        } catch (IllegalStateException e) {
            throw new ExceptionInInitializerError("Error en la configuración de la base de datos: " + e.getMessage());
        }
    }

    /**
     * Constructor privado para prevenir instanciación.
     * Esta es una clase utilitaria con solo métodos estáticos.
     */
    private DatabaseConnection() {
        throw new UnsupportedOperationException("Esta es una clase utilitaria y no debe ser instanciada.");
    }

    /**
     * Obtiene una nueva conexión a la base de datos.
     *
     * Importante:
     * - Cada llamada crea una nueva conexión.
     * - El caller es responsable de cerrar la conexión.
     * - La configuración ya fue validada en el bloque static.
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    /**
     * Comprueba que los parámetros de configuración sean válidos.
     * Llamado una sola vez desde el bloque static.
     *
     * Reglas:
     * - URL y USER no pueden ser null ni estar vacíos.
     * - PASSWORD puede ser vacío (común en MySQL local root sin password).
     * - PASSWORD no puede ser null.
     */
    private static void validateConfiguration() {
        if (URL == null || URL.trim().isEmpty()) {
            throw new IllegalStateException("La URL de la base de datos no está configurada.");
        }
        if (USER == null || USER.trim().isEmpty()) {
            throw new IllegalStateException("El usuario de la base de datos no está configurado.");
        }
        if (PASSWORD == null) {
            throw new IllegalStateException("La contraseña de la base de datos no está configurada.");
        }
    }
}
