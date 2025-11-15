package dao;

import config.DatabaseConnection;
import models.GrupoSanguineo;
import models.HistoriaClinica;

import java.sql.*;
import java.util.List;
import java.util.ArrayList;

public class HistoriaClinicaDAO implements GenericDAO<HistoriaClinica> {

    @Override
    public long crear(HistoriaClinica hc, Connection conn) throws Exception {
        String sql = "INSERT INTO historia_clinica (nro_historia, grupo_sanguineo, antecedentes, medicacion_actual, observaciones, eliminado, paciente_id) VALUES (?, ?, ?, ?, ?, ?, ?)";
        if (hc.getId() == null) {
            throw new IllegalArgumentException("HistoriaClinica debe tener asociado el ID del Paciente (paciente_id) antes de crear.");
        }

        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, hc.getNroHistoria());
            ps.setString(2, hc.getGrupoSanguineo().name());
            ps.setString(3, hc.getAntecedentes());
            ps.setString(4, hc.getMedicacionActual());
            ps.setString(5, hc.getObservaciones());
            ps.setBoolean(6, hc.isEliminado());
            ps.setLong(7, hc.getId());

            if (ps.executeUpdate() == 0) {
                throw new SQLException("Fallo al crear la Historia Clínica, no se afectaron filas.");
            }

            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getLong(1);
                } else {
                    throw new SQLException("Fallo al crear la Historia Clínica, no se obtuvo ID.");
                }
            }
        }
    }

    public HistoriaClinica leerPorPacienteId(long pacienteId) throws Exception {
        String sql = "SELECT id, nro_historia, grupo_sanguineo, antecedentes, medicacion_actual, observaciones, eliminado FROM historia_clinica WHERE paciente_id = ? AND eliminado = FALSE";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, pacienteId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToHistoriaClinica(rs);
                }
            }
        }
        return null;
    }

    @Override
    public void actualizar(HistoriaClinica hc, Connection conn) throws Exception {
        String sql = "UPDATE historia_clinica SET nro_historia = ?, grupo_sanguineo = ?, antecedentes = ?, medicacion_actual = ?, observaciones = ? WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, hc.getNroHistoria());
            // 🎯 Usar name() para guardar el nombre interno del Enum (ej: A_POS)
            ps.setString(2, hc.getGrupoSanguineo().name());
            ps.setString(3, hc.getAntecedentes());
            ps.setString(4, hc.getMedicacionActual());
            ps.setString(5, hc.getObservaciones());
            ps.setLong(6, hc.getId());

            if (ps.executeUpdate() == 0) {
                throw new SQLException("Actualización fallida para HC ID " + hc.getId() + ".");
            }
        }
    }

    private HistoriaClinica mapResultSetToHistoriaClinica(ResultSet rs) throws SQLException {
        String grupoSanguineoString = rs.getString("grupo_sanguineo");
        // 🎯 Usar valueOf() para convertir el String guardado (ej: A_POS) de vuelta al Enum
        GrupoSanguineo grupo = GrupoSanguineo.valueOf(grupoSanguineoString);

        return new HistoriaClinica(
                rs.getLong("id"),
                rs.getBoolean("eliminado"),
                rs.getString("nro_historia"),
                grupo,
                rs.getString("antecedentes"),
                rs.getString("medicacion_actual"),
                rs.getString("observaciones")
        );
    }

    @Override
    public HistoriaClinica leer(long id) throws Exception {
        String sql = "SELECT id, nro_historia, grupo_sanguineo, antecedentes, medicacion_actual, observaciones, eliminado FROM historia_clinica WHERE id = ? AND eliminado = FALSE";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToHistoriaClinica(rs);
                }
            }
        }
        return null;
    }

    @Override
    public List<HistoriaClinica> leerTodos() throws Exception {
        String sql = "SELECT id, nro_historia, grupo_sanguineo, antecedentes, medicacion_actual, observaciones, eliminado FROM historia_clinica WHERE eliminado = FALSE";
        List<HistoriaClinica> lista = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(mapResultSetToHistoriaClinica(rs));
            }
        }
        return lista;
    }

    @Override
    public void eliminar(long id, Connection conn) throws Exception {
        String sql =  "UPDATE historia_clinica SET eliminado = TRUE WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            if (ps.executeUpdate() == 0) {
                throw new SQLException("Eliminación (lógica) fallida para HC ID " + id + ".");
            }
        }
    }
}