package service;

import dao.HistoriaClinicaDAO;
import models.HistoriaClinica;
import config.DatabaseConnection;

import java.sql.Connection;
import java.util.List;

public class HistoriaClinicaServiceImpl implements GenericService<HistoriaClinica> {

    private final HistoriaClinicaDAO historiaDao;

    public HistoriaClinicaServiceImpl(HistoriaClinicaDAO historiaDao) {
        this.historiaDao = historiaDao;
    }

    @Override
    public long insertar(HistoriaClinica entidad) throws Exception {
        try (Connection conn = DatabaseConnection.getConnection()) {
            try {
                conn.setAutoCommit(false);

                long idGenerado = historiaDao.crear(entidad, conn);

                conn.commit();
                return idGenerado;

            } catch (Exception e) {
                conn.rollback();
                throw e;
            }
        }
    }

    @Override
    public void actualizar(HistoriaClinica entidad) throws Exception {
        try (Connection conn = DatabaseConnection.getConnection()) {
            try {
                conn.setAutoCommit(false);

                historiaDao.actualizar(entidad, conn);

                conn.commit();

            } catch (Exception e) {
                conn.rollback();
                throw e;
            }
        }
    }

    @Override
    public void eliminar(Long id) throws Exception {
        try (Connection conn = DatabaseConnection.getConnection()) {
            try {
                conn.setAutoCommit(false);

                historiaDao.eliminar(id, conn);

                conn.commit();

            } catch (Exception e) {
                conn.rollback();
                throw e;
            }
        }
    }

    @Override
    public HistoriaClinica getById(Long id) throws Exception {
            return historiaDao.leer(id);
    }

    @Override
    public List<HistoriaClinica> getAll() throws Exception {
            return historiaDao.leerTodos();
    }
    
    public HistoriaClinica getByPacienteId(Long pacienteId) throws Exception {
        return historiaDao.leerPorPacienteId(pacienteId);
    }
}
