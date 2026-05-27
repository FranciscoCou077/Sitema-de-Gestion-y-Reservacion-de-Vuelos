package aeroviajes.service;

import aeroviajes.exception.AsientoNoDisponible;
import aeroviajes.exception.PagoInvalido;
import aeroviajes.exception.VueloNoDisponible;
import aeroviajes.model.Cliente;
import aeroviajes.model.Reserva;
import aeroviajes.model.Vuelo;
import aeroviajes.patterns.observer.NotificadorReservas;
import aeroviajes.patterns.strategy.IValidadorPago;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Coordina todo el proceso de compra de un boleto:
 * verifica el vuelo, valida el pago (Strategy) y
 * notifica a los observadores cuando se confirma la reserva (Observer).
 */
public class GestorReservas {

    private final IGestorVuelos gestorVuelos;
    private final NotificadorReservas notificador;
    private final List<Reserva> reservas;

    public GestorReservas(IGestorVuelos gestorVuelos, NotificadorReservas notificador) {
        this.gestorVuelos = gestorVuelos;
        this.notificador = notificador;
        this.reservas = new ArrayList<>();
    }

    /**
     * Crea una reserva. El validador de pago se inyecta desde afuera
     * (tarjeta de credito o debito) sin cambiar este metodo.
     */
    public Reserva crearReserva(Cliente cliente, String idVuelo,
                                 IValidadorPago validadorPago)
            throws VueloNoDisponible, AsientoNoDisponible, PagoInvalido {

        // 1. Verificar que el vuelo existe y tiene asientos
        Vuelo vuelo = gestorVuelos.buscarVuelo(idVuelo);
        if (!vuelo.hayDisponibilidad()) {
            throw new AsientoNoDisponible(idVuelo);
        }

        // 2. Validar el pago (Strategy: no importa si es credito o debito)
        validadorPago.validar();

        // 3. Descontar el asiento del vuelo
        gestorVuelos.reservarAsiento(idVuelo);

        // 4. Generar ID de reserva y asiento asignado
        String idReserva = "R-" + UUID.randomUUID().toString().substring(0, 6).toUpperCase();
        String asiento = "A" + (vuelo.getAsientosTotales() - vuelo.getAsientosDisponibles());

        // 5. Crear la reserva y confirmarla
        Reserva reserva = new Reserva(idReserva, vuelo, cliente, asiento);
        reserva.confirmar();
        reservas.add(reserva);

        // 6. Notificar: esto dispara la generacion del ticket en segundo plano
        notificador.notificar(reserva);

        return reserva;
    }

    public List<Reserva> obtenerReservasPorCliente(String correoCliente) {
        List<Reserva> resultado = new ArrayList<>();
        for (Reserva r : reservas) {
            if (r.getCliente().getCorreo().equals(correoCliente)) {
                resultado.add(r);
            }
        }
        return resultado;
    }

    public List<Reserva> obtenerTodasLasReservas() {
        return new ArrayList<>(reservas);
    }
}
