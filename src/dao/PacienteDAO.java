package dao;

import config.DatabaseConnection;
import models.HistoriaClinica;
import models.Paciente;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PacienteDAO implements GenericDAO<Paciente> {

    private HistoriaClinicaDAO historiaClinicaDao;

    public PacienteDAO(HistoriaClinicaDAO historiaClinicaDao) {
        this.historiaClinicaDao = historiaClinicaDao;
    }

    @Override
    public long crear(Paciente paciente, Connection conn) throws Exception {
        String sql = "INSERT INTO paciente (nombre, apellido, dni, fechaNacimiento, eliminado) VALUES (?,?,?,?,?)";
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, paciente.getNombre());
            ps.setString(2, paciente.getApellido());
            ps.setString(3, paciente.getDni());
            ps.setDate(4, Date.valueOf(paciente.getFechaNacimiento()));
            ps.setBoolean(5, paciente.isEliminado());

            int affectedRows = ps.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Fallo al crear el paciente, no se afectaron filas.");
            }

            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getLong(1);
                } else {
                    throw new SQLException("Fallo al crear el paciente, no se obtuvo ID.");
                }
            }
        }
    }

    @Override
    public Paciente leer(long id) throws Exception {
        String sql = "SELECT id, nombre, apellido, dni, fecha_nacimiento, eliminado FROM paciente WHERE id = ? AND eliminado = FALSE";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToPaciente(rs);
                }
            }
        }
        return null;
    }

    @Override
    public List<Paciente> leerTodos() throws Exception {
        String sql = "SELECT id, nombre, apellido, dni, fecha_nacimiento, eliminado FROM paciente WHERE eliminado = FALSE";
        List<Paciente> pacientes = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                pacientes.add(mapResultSetToPaciente(rs));
            }
        }
        return pacientes;
    }

    @Override
    public void actualizar(Paciente paciente, Connection conn) throws Exception {
        String sql = "UPDATE paciente SET nombre = ?, apellido = ?, dni = ?, fecha_nacimiento = ? WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, paciente.getNombre());
            ps.setString(2, paciente.getApellido());
            ps.setString(3, paciente.getDni());
            ps.setDate(4, Date.valueOf(paciente.getFechaNacimiento()));
            ps.setLong(5, paciente.getId());

            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Actualización fallida, ID " + paciente.getId() + " no encontrado o eliminado.");
            }
        }
    }

    @Override
    public void eliminar(long id, Connection conn) throws Exception {
        String sql = "UPDATE paciente SET eliminado = TRUE WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);

            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Eliminación fallida, ID " + id + " no encontrado.");
            }
        }
    }

    public Paciente buscarPorDni(String dni) throws Exception {
        String sql = "SELECT id, nombre, apellido, dni, fecha_nacimiento, eliminado FROM paciente WHERE dni = ? AND eliminado = FALSE";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, dni);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToPaciente(rs);
                }
            }
        }
        return null;
    }

    private Paciente mapResultSetToPaciente(ResultSet rs) throws SQLException, Exception {
        long pacienteId = rs.getLong("id");

        HistoriaClinica hc = historiaClinicaDao.leerPorPacienteId(pacienteId);

        return new Paciente(
                pacienteId,
                rs.getBoolean("eliminado"),
                rs.getString("nombre"),
                rs.getString("apellido"),
                rs.getString("dni"),
                rs.getDate("fecha_nacimiento").toLocalDate(),
                hc
        );
    }
}
