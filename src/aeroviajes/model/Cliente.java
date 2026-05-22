package aeroviajes.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Usuario estandar del sistema. Puede consultar vuelos, comprar boletos
 * y mantiene un historial de sus reservas.
 *
 * Demuestra: herencia, polimorfismo y uso de colecciones (List).
 *
 * @author Equipo Aeroviajes
 */
public class Cliente extends Usuario {

    private static final long serialVersionUID = 1L;

    private final List<Reserva> historialReservas;

    public Cliente(String id, String nombre, String apellido,
                   String correo, String contrasena) {
        super(id, nombre, apellido, correo, contrasena);
        this.historialReservas = new ArrayList<>();
    }

    @Override
    public String getRol() {
        return "CLIENTE";
    }

    public void agregarReserva(Reserva reserva) {
        historialReservas.add(reserva);
    }

    /**
     * Devuelve una copia defensiva de la lista para proteger
     * la encapsulacion (el llamador no puede modificar la lista interna).
     */
    public List<Reserva> getHistorialReservas() {
        return new ArrayList<>(historialReservas);
    }

    public int getCantidadReservas() {
        return historialReservas.size();
    }
}
