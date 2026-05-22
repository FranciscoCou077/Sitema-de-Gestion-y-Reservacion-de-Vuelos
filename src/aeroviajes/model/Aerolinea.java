package aeroviajes.model;

import java.io.Serializable;
import java.util.Objects;

/**
 * Objeto Flyweight inmutable. Representa una aerolinea cuyo estado intrinseco
 * se comparte entre multiples vuelos. Las instancias se gestionan mediante
 * AerolineaFlyweightFactory (paquete patterns.flyweight).
 *
 * @author Equipo Aeroviajes
 */
public final class Aerolinea implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String codigo;
    private final String nombre;

    public Aerolinea(String codigo, String nombre) {
        this.codigo = codigo;
        this.nombre = nombre;
    }

    public String getCodigo() { return codigo; }
    public String getNombre() { return nombre; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Aerolinea)) return false;
        Aerolinea that = (Aerolinea) o;
        return Objects.equals(codigo, that.codigo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codigo);
    }

    @Override
    public String toString() {
        return nombre + " (" + codigo + ")";
    }
}
