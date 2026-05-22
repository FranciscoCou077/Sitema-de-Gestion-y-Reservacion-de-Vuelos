package aeroviajes.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Representa la reserva de un asiento en un vuelo por parte de un cliente.
 * Asocia un Vuelo, un Cliente y un asiento concreto, y lleva el control
 * del estado del ciclo de vida de la reserva.
 *
 * Demuestra: composicion/asociacion entre entidades y uso de enum.
 *
 * @author Equipo Aeroviajes
 */
public class Reserva implements Serializable {

    private static final long serialVersionUID = 1L;

    /** Estados posibles del ciclo de vida de una reserva. */
    public enum Estado {
        PENDIENTE, CONFIRMADA, CANCELADA
    }

    private final String id;
    private final Vuelo vuelo;
    private final Cliente cliente;
    private final String asientoAsignado;
    private final LocalDateTime fechaReserva;
    private Estado estado;

    public Reserva(String id, Vuelo vuelo, Cliente cliente, String asientoAsignado) {
        this.id = id;
        this.vuelo = vuelo;
        this.cliente = cliente;
        this.asientoAsignado = asientoAsignado;
        this.fechaReserva = LocalDateTime.now();
        this.estado = Estado.PENDIENTE;
    }

    public void confirmar() { this.estado = Estado.CONFIRMADA; }
    public void cancelar()  { this.estado = Estado.CANCELADA; }

    // ----- Getters -----
    public String getId() { return id; }
    public Vuelo getVuelo() { return vuelo; }
    public Cliente getCliente() { return cliente; }
    public String getAsientoAsignado() { return asientoAsignado; }
    public LocalDateTime getFechaReserva() { return fechaReserva; }
    public Estado getEstado() { return estado; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Reserva)) return false;
        return Objects.equals(id, ((Reserva) o).id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return String.format("Reserva %s | Vuelo %s | Asiento %s | %s",
            id, vuelo.getId(), asientoAsignado, estado);
    }
}
