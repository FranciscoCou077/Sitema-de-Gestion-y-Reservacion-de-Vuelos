package aeroviajes.persistence;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementacion base abstracta de {@link IRepositorio}. Mantiene la coleccion
 * de entidades en memoria, delega la lectura/escritura de archivos en
 * {@link GestorArchivos} y deja que cada subclase defina como obtener el
 * identificador unico de una entidad y en que archivo persistir.
 *
 * Al centralizar aqui la logica CRUD comun, las subclases concretas quedan
 * minimas (principio DRY: no repetir codigo).
 *
 * Demuestra: clase abstracta, genericos, herencia, colecciones, manejo de
 * archivos y excepciones.
 *
 * @param <T> el tipo de entidad gestionada
 * @author Equipo Aeroviajes
 */
public abstract class RepositorioBase<T> implements IRepositorio<T> {

    protected final String rutaArchivo;
    protected List<T> elementos;

    /**
     * @param rutaArchivo ruta del archivo .dat donde se persiste la coleccion
     */
    protected RepositorioBase(String rutaArchivo) {
        this.rutaArchivo = rutaArchivo;
        this.elementos = new ArrayList<>();
        cargar();
    }

    /**
     * Metodo plantilla: cada subclase indica como extraer el identificador
     * unico de una entidad concreta (por ejemplo, el correo de un usuario
     * o el id de un vuelo).
     *
     * @param entidad la entidad de la que se extrae el id
     * @return el identificador unico como cadena
     */
    protected abstract String obtenerId(T entidad);

    @Override
    public void guardar(T entidad) {
        elementos.add(entidad);
        persistir();
    }

    @Override
    public void actualizar(T entidad) {
        for (int i = 0; i < elementos.size(); i++) {
            if (obtenerId(elementos.get(i)).equals(obtenerId(entidad))) {
                elementos.set(i, entidad);
                persistir();
                return;
            }
        }
    }

    @Override
    public void eliminar(String id) {
        elementos.removeIf(e -> obtenerId(e).equals(id));
        persistir();
    }

    @Override
    public T buscarPorId(String id) {
        for (T e : elementos) {
            if (obtenerId(e).equals(id)) {
                return e;
            }
        }
        return null;
    }

    @Override
    public List<T> listarTodos() {
        return new ArrayList<>(elementos);   // copia defensiva
    }

    @Override
    public final void persistir() {
        try {
            GestorArchivos.guardarLista(elementos, rutaArchivo);
        } catch (IOException e) {
            throw new RuntimeException(
                    "Error al guardar en " + rutaArchivo + ": " + e.getMessage(), e);
        }
    }

    @Override
    public final void cargar() {
        try {
            this.elementos = GestorArchivos.cargarLista(rutaArchivo);
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Aviso: no se pudo cargar " + rutaArchivo
                    + " (se inicia con coleccion vacia). Detalle: " + e.getMessage());
            this.elementos = new ArrayList<>();
        }
    }

    /** Cantidad de elementos actualmente almacenados. */
    public int contar() {
        return elementos.size();
    }
}
