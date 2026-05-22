package aeroviajes.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * Representa un vuelo del catalogo. Mantiene referencias compartidas (Flyweight)
 * a su aeropuerto de origen, destino y aerolinea (estado intrinseco), y su propio
 * estado extrinseco unico (fecha de salida, precio y disponibilidad de asientos).
 *
 * Nota de diseno: la validacion de negocio (lanzar AsientoNoDisponibleException)
 * se realiza en la capa de servicio (GestorReservas). Aqui solo se expone
 * hayDisponibilidad() y una verificacion defensiva con IllegalStateException,
 * de modo que el paquete model no dependa del paquete exception.
 *
 * @author Equipo Aeroviajes
 */
public class Vuelo implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String id;
    private final Aeropuerto origen;
    private final Aeropuerto destino;
    private final Aerolinea aerolinea;
    private LocalDateTime fechaSalida;
    private double precio;
    private final int asientosTotales;
    private int asientosDisponibles;

    public Vuelo(String id, Aeropuerto origen, Aeropuerto destino, Aerolinea aerolinea,
                 LocalDateTime fechaSalida, double precio, int asientosTotales) {
        this.id = id;
        this.origen = origen;
        this.destino = destino;
        this.aerolinea = aerolinea;
        this.fechaSalida = fechaSalida;
        this.precio = precio;
        this.asientosTotales = asientosTotales;
        this.asientosDisponibles = asientosTotales;
    }

    public boolean hayDisponibilidad() {
        return asientosDisponibles > 0;
    }

    /**
     * Reserva un asiento decrementando la disponibilidad.
     * Verificacion defensiva: la capa de servicio debe comprobar
     * hayDisponibilidad() antes y lanzar la excepcion de negocio.
     */
    public void reservarAsiento() {
        if (!hayDisponibilidad()) {
            throw new IllegalStateException(
                "No hay asientos disponibles en el vuelo " + id);
        }
        asientosDisponibles--;
    }

    /** Libera un asiento (por ejemplo, al cancelar una reserva). */
    public void liberarAsiento() {
        if (asientosDisponibles < asientosTotales) {
            asientosDisponibles++;
        }
    }

    // ----- Getters -----
    public String getId() { return id; }
    public Aeropuerto getOrigen() { return origen; }
    public Aeropuerto getDestino() { return destino; }
    public Aerolinea getAerolinea() { return aerolinea; }
    public LocalDateTime getFechaSalida() { return fechaSalida; }
    public double getPrecio() { return precio; }
    public int getAsientosTotales() { return asientosTotales; }
    public int getAsientosDisponibles() { return asientosDisponibles; }

    // ----- Setters de los atributos modificables por el administrador -----
    public void setPrecio(double precio) { this.precio = precio; }
    public void setFechaSalida(LocalDateTime fechaSalida) { this.fechaSalida = fechaSalida; }

    public String getFechaFormateada() {
        return fechaSalida.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Vuelo)) return false;
        return Objects.equals(id, ((Vuelo) o).id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return String.format("Vuelo %s | %s -> %s | %s | $%.2f | %d/%d asientos",
            id, origen.getCodigoIATA(), destino.getCodigoIATA(),
            getFechaFormateada(), precio, asientosDisponibles, asientosTotales);
    }
}
