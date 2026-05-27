package aeroviajes.patterns.observer;

import aeroviajes.model.Reserva;
import aeroviajes.threads.ColaTickets;

/**
 * Observador que actua como puente entre el patron Observer y el Productor-Consumidor.
 * Cuando se confirma una reserva, la mete a la cola para que
 * el hilo consumidor la procese y genere el ticket sin bloquear la GUI.
 */
public class ProductorTickets implements IObservadorReserva {

    private final ColaTickets cola;

    public ProductorTickets(ColaTickets cola) {
        this.cola = cola;
    }

    @Override
    public void onReservaConfirmada(Reserva reserva) {
        try {
            cola.agregar(reserva);
            System.out.println("[ProductorTickets] Reserva en cola: " + reserva.getId());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("[ProductorTickets] Hilo interrumpido al agregar a la cola.");
        }
    }
}