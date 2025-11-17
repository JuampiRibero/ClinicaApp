package service;

import dao.HistoriaClinicaDAO;
import dao.PacienteDAO;
import models.HistoriaClinica;
import models.Paciente;
import config.DatabaseConnection;

import java.sql.Connection;

public class PacienteHistoriaService {

    private final PacienteDAO pacienteDao;
    private final HistoriaClinicaDAO historiaDao;

    public PacienteHistoriaService(PacienteDAO pacienteDao, HistoriaClinicaDAO historiaDao) {
        this.pacienteDao = pacienteDao;
        this.historiaDao = historiaDao;
    }

    // Insertar Paciente + HistoriaClinica en una única transacción
    public long crearPacienteConHistoria(Paciente paciente, HistoriaClinica historia) throws Exception {

        try (Connection conn = DatabaseConnection.getConnection()) {
            try {

                conn.setAutoCommit(false);

                // Validaciones obligatorias
                if (paciente.getDni() == null || paciente.getDni().isBlank()) {
                    throw new Exception("El DNI es obligatorio.");
                }

                // Regla 1→1: verificar que ese paciente NO tenga historia
                HistoriaClinica existente = historiaDao.leerPorPacienteId(paciente.getId());
                if (existente != null) {
                    throw new Exception("El paciente ya tiene una historia clínica asignada.");
                }

                // Insertar paciente
                long pacienteId = pacienteDao.crear(paciente, conn);

                // Asociar la historia al paciente
                paciente.setHistoriaClinica(historia); 

                // Insertar historia clínica
                historiaDao.crear(historia, conn);

                conn.commit();
                return pacienteId;

            } catch (Exception e) {
                conn.rollback(); // rollback SIEMPRE ante error
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }
        }
    }

}
