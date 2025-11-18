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

    public long crearPacienteConHistoria(Paciente paciente, HistoriaClinica historia) throws Exception {

        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);
            try {

                if (paciente.getDni() == null || paciente.getDni().isBlank()) {
                    throw new Exception("El DNI es obligatorio.");
                }

                if (pacienteDao.buscarPorDni(paciente.getDni()) != null) {
                    throw new Exception("ERROR: Ya existe un paciente con el DNI " + paciente.getDni());
                }

                long pacienteId = pacienteDao.crear(paciente, conn);

                historia.setId(pacienteId); 

                historiaDao.crear(historia, conn);
                
                paciente.setHistoriaClinica(historia);
                paciente.setId(pacienteId);

                conn.commit();
                return pacienteId;

            } catch (Exception e) {
                conn.rollback();
                throw new Exception("Error en la transacción: " + e.getMessage()); 
            } finally {
                conn.setAutoCommit(true);
            }
        }
    }
}