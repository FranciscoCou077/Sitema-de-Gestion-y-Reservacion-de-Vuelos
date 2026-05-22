package aeroviajes.model;

import java.io.Serializable;
import java.util.Objects;

/**
 * Objeto Flyweight inmutable. Representa un aeropuerto cuyo estado intrinseco
 * (codigo, nombre, ciudad, pais) se comparte entre multiples vuelos para
 * optimizar memoria. Las instancias se gestionan mediante
 * AeropuertoFlyweightFactory (paquete patterns.flyweight).
 *
 * La clase es 'final' y todos sus atributos son 'final' para garantizar
 * inmutabilidad, requisito del patron Flyweight.
 *
 * @author Equipo Aeroviajes
 */
public final class Aeropuerto implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String codigoIATA;
    private final String nombre;
    private final String ciudad;
    private final String pais;

    public Aeropuerto(String codigoIATA, String nombre, String ciudad, String pais) {
        this.codigoIATA = codigoIATA;
        this.nombre = nombre;
        this.ciudad = ciudad;
        this.pais = pais;
    }

    // Solo getters: el objeto es inmutable.
    public String getCodigoIATA() { return codigoIATA; }
    public String getNombre() { return nombre; }
    public String getCiudad() { return ciudad; }
    public String getPais() { return pais; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Aeropuerto)) return false;
        Aeropuerto that = (Aeropuerto) o;
        return Objects.equals(codigoIATA, that.codigoIATA);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codigoIATA);
    }

    @Override
    public String toString() {
        return codigoIATA + " (" + ciudad + ", " + pais + ")";
    }
}
