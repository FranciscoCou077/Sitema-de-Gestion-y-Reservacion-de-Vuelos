package aeroviajes.patterns.observer;

import aeroviajes.model.Reserva;

/**
 * Interfaz del patron Observer.
 * Cualquier clase que quiera ser notificada cuando se confirme
 * una reserva debe implementar este metodo.
 */
public interface IObservadorReserva {

    void onReservaConfirmada(Reserva reserva);
}
