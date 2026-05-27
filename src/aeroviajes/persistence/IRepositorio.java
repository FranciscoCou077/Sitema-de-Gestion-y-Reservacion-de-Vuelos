package aeroviajes.persistence;

import java.util.List;

/**
 * Contrato generico para los repositorios del sistema Aeroviajes.
 * Define las operaciones CRUD (crear, leer, actualizar, eliminar) y de
 * persistencia para cualquier tipo de entidad T.
 *
 * Demuestra: uso de interfaces y de genericos (parametro de tipo T).
 *
 * @param <T> el tipo de entidad que gestiona el repositorio
 * @author Equipo Aeroviajes
 */
public interface IRepositorio<T> {

    /** Agrega una nueva entidad y la persiste. */
    void guardar(T entidad);

    /** Reemplaza una entidad existente (identificada por su id) y persiste. */
    void actualizar(T entidad);

    /** Elimina la entidad cuyo identificador coincide con el dado y persiste. */
    void eliminar(String id);

    /** Busca una entidad por su identificador. Devuelve null si no existe. */
    T buscarPorId(String id);

    /** Devuelve una copia de todas las entidades almacenadas. */
    List<T> listarTodos();

    /** Escribe la coleccion completa al archivo en disco. */
    void persistir();

    /** Lee la coleccion desde el archivo en disco. */
    void cargar();
}
