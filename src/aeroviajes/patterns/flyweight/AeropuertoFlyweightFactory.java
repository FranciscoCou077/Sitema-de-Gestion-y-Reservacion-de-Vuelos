package aeroviajes.patterns.flyweight;

import aeroviajes.model.Aeropuerto;
import java.util.HashMap;
import java.util.Map;

/**
 * Fabrica del patron Flyweight para objetos {@link Aeropuerto}.
 *
 * En un sistema de vuelos, muchos vuelos comparten los mismos aeropuertos
 * (origen y destino). En lugar de crear un objeto Aeropuerto nuevo por cada
 * vuelo, esta fabrica mantiene una cache: si el aeropuerto ya existe lo
 * reutiliza, y si no, lo crea una sola vez y lo guarda. Asi todos los vuelos
 * que usan, por ejemplo, "MEX" comparten la MISMA instancia en memoria.
 *
 * El estado intrinseco (codigo, nombre, ciudad, pais) vive en el flyweight
 * y es inmutable; el estado extrinseco (fecha, precio, asientos) vive en
 * cada Vuelo.
 *
 * Demuestra: patron Flyweight, coleccion Map, metodos estaticos.
 *
 * @author Equipo Aeroviajes
 */
public final class AeropuertoFlyweightFactory {

    // Cache de instancias compartidas, indexada por codigo IATA.
    private static final Map<String, Aeropuerto> cache = new HashMap<>();

    private AeropuertoFlyweightFactory() {
        // Clase de utilidad: no se instancia.
    }

    /**
     * Devuelve el aeropuerto con el codigo dado. Si ya existe en la cache,
     * reutiliza esa instancia; si no, la crea, la guarda y la devuelve.
     *
     * @return la instancia compartida (flyweight) del aeropuerto
     */
    public static Aeropuerto obtener(String codigoIATA, String nombre,
                                     String ciudad, String pais) {
        return cache.computeIfAbsent(codigoIATA,
                clave -> new Aeropuerto(clave, nombre, ciudad, pais));
    }

    /**
     * Devuelve un aeropuerto ya registrado por su codigo, o null si no existe.
     */
    public static Aeropuerto obtener(String codigoIATA) {
        return cache.get(codigoIATA);
    }

    /** Cantidad de aeropuertos distintos almacenados en la cache. */
    public static int cantidadEnCache() {
        return cache.size();
    }

    /** Limpia la cache (util para pruebas). */
    public static void limpiarCache() {
        cache.clear();
    }
}
