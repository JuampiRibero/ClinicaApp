package service;

import config.DatabaseConnection; 
import dao.HistoriaClinicaDAO;
import dao.PacienteDAO;
import models.HistoriaClinica; 
import models.Paciente;

import java.sql.Connection;
import java.util.List;


public class PacienteServiceImpl implements GenericService<Paciente> {

    
    private final PacienteDAO pacienteDao;
    private final HistoriaClinicaDAO historiaDao; 

    
    public PacienteServiceImpl(PacienteDAO pacienteDao, HistoriaClinicaDAO historiaDao) {
        this.pacienteDao = pacienteDao;
        this.historiaDao = historiaDao;
    }

    
    @Override
    public long insertar(Paciente entidad) throws Exception {
        try (Connection conn = DatabaseConnection.getConnection()) {
            
            conn.setAutoCommit(true); 
            return pacienteDao.crear(entidad, conn);
        }
    }

    
    @Override
    public void actualizar(Paciente entidad) throws Exception {
        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(true); 
            pacienteDao.actualizar(entidad, conn);
        }
    }

    @Override
    public void eliminar(Long id) throws Exception {
        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false); 
            try {
                
                Paciente p = pacienteDao.leer(id); 
                if (p == null) {
                    throw new Exception("No se encontró el paciente con ID " + id);
                }
                
                
                HistoriaClinica hc = historiaDao.leerPorPacienteId(id); 
                
                
                if (hc != null) {
                    historiaDao.eliminar(hc.getId(), conn);
                }

                
                pacienteDao.eliminar(id, conn);

                
                conn.commit();

            } catch (Exception e) {
                conn.rollback(); 
                throw new Exception("Error en la transacción de eliminación: " + e.getMessage());
            } finally {
                conn.setAutoCommit(true); 
            }
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