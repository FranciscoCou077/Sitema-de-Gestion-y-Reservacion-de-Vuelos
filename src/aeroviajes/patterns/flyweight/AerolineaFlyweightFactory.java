package aeroviajes.patterns.flyweight;

import aeroviajes.model.Aerolinea;
import java.util.HashMap;
import java.util.Map;

/**
 * Fabrica del patron Flyweight para objetos {@link Aerolinea}.
 *
 * Pocas aerolineas operan muchos vuelos, por lo que conviene compartir una
 * sola instancia de cada aerolinea entre todos los vuelos que opera, en lugar
 * de duplicar sus datos. Esta fabrica mantiene una cache indexada por el
 * codigo de la aerolinea.
 *
 * Demuestra: patron Flyweight, coleccion Map, metodos estaticos.
 *
 * @author Equipo Aeroviajes
 */
public final class AerolineaFlyweightFactory {

    private static final Map<String, Aerolinea> cache = new HashMap<>();

    private AerolineaFlyweightFactory() {
        // Clase de utilidad: no se instancia.
    }

    /**
     * Devuelve la aerolinea con el codigo dado, reutilizando la instancia
     * de la cache o creandola una sola vez si aun no existe.
     */
    public static Aerolinea obtener(String codigo, String nombre) {
        return cache.computeIfAbsent(codigo,
                clave -> new Aerolinea(clave, nombre));
    }

    /** Devuelve una aerolinea ya registrada por su codigo, o null. */
    public static Aerolinea obtener(String codigo) {
        return cache.get(codigo);
    }

    public static int cantidadEnCache() {
        return cache.size();
    }

    public static void limpiarCache() {
        cache.clear();
    }
}
