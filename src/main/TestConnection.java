package main;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import config.DatabaseConnection;

public class TestConnection {
    public static void main(String[] args) {
        
        /**
         * Se usa un bloque try-with-resources para asegurar que la conexión se cierre automáticamente al salir del bloque.
         * No es necesario llamar explícitamente a conn.close().
         */
        try (Connection conn = DatabaseConnection.getConnection()) {
            if (conn != null) {
                System.out.println("Conexión a la base de datos establecida exitosamente.");
                 
                // Creamos y ejecutamos la consulta SQL con PreparedStatement.
                String sql = """
                    SELECT p.id, p.nombre, p.apellido, p.dni, 
                           h.nro_historia, h.grupo_sanguineo
                    FROM paciente p
                    LEFT JOIN historia_clinica h ON p.id = h.paciente_id
                """;
                try (PreparedStatement pstmt = conn.prepareStatement(sql);
                    ResultSet rs = pstmt.executeQuery()) {
                    System.out.println("\nListado de pacientes:\n");
                    while (rs.next()) {
                        int id = rs.getInt("id");
                        String nombre = rs.getString("nombre");
                        String apellido = rs.getString("apellido");
                        String dni = rs.getString("dni");
                        String nro_historia = rs.getString("nro_historia");
                        String grupo_sanguineo = rs.getString("grupo_sanguineo");
                        System.out.println("ID: " + id + ", Nombre: " + nombre + ", Apellido: " + apellido + ", DNI: " + dni + ", N° Historia: " + nro_historia + ", Grupo Sanguíneo: " + grupo_sanguineo);
                        System.out.println("------------------------------------------------------------------------------------------------------");
                    }
                }
            } else {
                System.out.println("No se pudo establecer la conexión a la base de datos.");
            }
        } catch (SQLException e) {
            // Manejo de errores en la conexión a la base de datos.
            System.err.println("Error al conectar a la base de datos: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
