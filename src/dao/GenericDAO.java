package dao;

import java.sql.Connection;
import java.util.List;

/**
 * Interfaz generica para el patron DAO.
 * Define las operaciones CRUD basicas para cualquier entidad de dominio.
 * Se utiliza la generica <T> para tipar la entidad.
 */
public interface GenericDAO<T> {

    /**
     * Inserta una nueva entidad en la base de datos.
     * @param entidad La entidad a crear.
     * @param conn La conexión a usar (para transacciones en Service).
     * @return El ID generado de la nueva entidad.
     * @throws Exception Si ocurre un error de base de datos o de negocio.
     */
    long crear(T entidad, Connection conn) throws Exception;

    /**
     * Busca y retorna una entidad por su clave primaria.
     * @param id El ID de la entidad a buscar.
     * @return La entidad encontrada o null si no existe.
     * @throws Exception Si ocurre un error de base de datos.
     */
    T leer(long id) throws Exception;

    /**
     * Retorna una lista de todas las entidades activas (eliminado = false).
     * @return Una lista de entidades.
     * @throws Exception Si ocurre un error de base de datos.
     */
    List<T> leerTodos() throws Exception;

    /**
     * Actualiza una entidad existente en la base de datos.
     * @param entidad La entidad con los datos actualizados.
     * @param conn La conexión a usar (para transacciones en Service).
     * @throws Exception Si ocurre un error de base de datos o de negocio.
     */
    void actualizar(T entidad, Connection conn) throws Exception;

    /**
     * Realiza una baja logica (soft delete) sobre la entidad.
     * Esto significa actualizar el campo 'eliminado' a true.
     * @param id El ID de la entidad a eliminar logicamente.
     * @param conn La conexion a usar (para transacciones en Service).
     * @throws Exception Si ocurre un error de base de datos.
     */
    void eliminar(long id, Connection conn) throws Exception;
}