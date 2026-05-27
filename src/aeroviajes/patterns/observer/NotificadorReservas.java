package aeroviajes.patterns.observer;

import aeroviajes.model.Reserva;
import java.util.ArrayList;
import java.util.List;

/**
 * Sujeto del patron Observer.
 * Mantiene una lista de observadores y los notifica
 * cada vez que se confirma una reserva.
 */
public class NotificadorReservas {

    private final List<IObservadorReserva> observadores;

    public NotificadorReservas() {
        this.observadores = new ArrayList<>();
    }

    public void agregarObservador(IObservadorReserva observador) {
        observadores.add(observador);
    }

    public void quitarObservador(IObservadorReserva observador) {
        observadores.remove(observador);
    }

    /**
     * Avisa a todos los observadores que se confirmo una reserva.
     * Cada uno decide que hacer con esa informacion.
     */
    public void notificar(Reserva reserva) {
        for (IObservadorReserva obs : observadores) {
            obs.onReservaConfirmada(reserva);
        }
    }
}
