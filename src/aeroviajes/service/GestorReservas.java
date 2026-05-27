package aeroviajes.service;

import aeroviajes.exception.AsientoNoDisponible;
import aeroviajes.exception.PagoInvalido;
import aeroviajes.exception.VueloNoDisponible;
import aeroviajes.model.Reserva;
import aeroviajes.model.Vuelo;
import aeroviajes.patterns.observer.NotificadorReservas;
import aeroviajes.patterns.strategy.IValidadorPago;
import java.util.ArrayList;
import java.util.List;

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
    public Reserva crearReserva(String idCliente, String idVuelo,
                                 IValidadorPago validadorPago)
            throws VueloNoDisponible, AsientoNoDisponible, PagoInvalido {

        // 1. Verificar que el vuelo existe y tiene asientos
        Vuelo vuelo = gestorVuelos.buscarVuelo(idVuelo);
        if (vuelo.getAsientosDisponibles() <= 0) {
            throw new AsientoNoDisponible(idVuelo);
        }

        // 2. Validar el pago (Strategy: no importa si es credito o debito)
        validadorPago.validar();

        // 3. Descontar el asiento del vuelo
        gestorVuelos.reservarAsiento(idVuelo);

        // 4. Crear la reserva y registrarla
        Reserva reserva = new Reserva(idCliente, vuelo);
        reservas.add(reserva);

        // 5. Notificar: esto dispara la generacion del ticket en segundo plano
        notificador.notificar(reserva);

        return reserva;
    }

    public List<Reserva> obtenerReservasPorCliente(String idCliente) {
        List<Reserva> resultado = new ArrayList<>();
        for (Reserva r : reservas) {
            if (r.getIdCliente().equals(idCliente)) {
                resultado.add(r);
            }
        }
        return resultado;
    }

    public List<Reserva> obtenerTodasLasReservas() {
        return new ArrayList<>(reservas);
    }
}
