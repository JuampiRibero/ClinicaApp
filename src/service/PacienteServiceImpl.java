package service;

import dao.PacienteDAO;
import dao.HistoriaClinicaDAO;
import models.Paciente;
import config.DatabaseConnection;

import java.sql.Connection;
import java.util.List;

public class PacienteServiceImpl implements GenericService<Paciente> {

    private final HistoriaClinicaDAO historiaDao = new HistoriaClinicaDAO();
    private final PacienteDAO pacienteDao = new PacienteDAO(historiaDao);


    @Override
    public long insertar(Paciente entidad) throws Exception {
        try (Connection conn = DatabaseConnection.getConnection()) {
            return pacienteDao.crear(entidad, conn);
        }
    }

    @Override
    public void actualizar(Paciente entidad) throws Exception {
        try (Connection conn = DatabaseConnection.getConnection()) {
            pacienteDao.actualizar(entidad, conn);
        }
    }

    @Override
    public void eliminar(Long id) throws Exception {
        try (Connection conn = DatabaseConnection.getConnection()) {
            pacienteDao.eliminar(id, conn);
        }
    }

    @Override
    public Paciente getById(Long id) throws Exception {
       return pacienteDao.leer(id);
    }

    @Override
    public List<Paciente> getAll() throws Exception {
        return pacienteDao.leerTodos();
    }
    
    public Paciente getByDni(String dni) throws Exception {
        return pacienteDao.buscarPorDni(dni);
    }
}