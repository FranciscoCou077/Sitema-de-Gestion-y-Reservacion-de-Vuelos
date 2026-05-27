package aeroviajes.threads;

import aeroviajes.model.Reserva;
import aeroviajes.service.GestorTickets;

/**
 * Hilo consumidor del patron Productor-Consumidor.
 * Corre en segundo plano esperando reservas en la cola.
 * Cuando llega una, genera el ticket sin bloquear la GUI.
 */
public class HiloConsumidorTickets extends Thread {

    private final ColaTickets cola;
    private final GestorTickets gestorTickets;
    private boolean corriendo;

    public HiloConsumidorTickets(ColaTickets cola, GestorTickets gestorTickets) {
        this.cola = cola;
        this.gestorTickets = gestorTickets;
        this.corriendo = true;
        setDaemon(true);
        setName("HiloConsumidorTickets");
    }

    @Override
    public void run() {
        System.out.println("[HiloConsumidor] Iniciado, esperando reservas...");
        while (corriendo) {
            try {
                Reserva reserva = cola.tomar();
                System.out.println("[HiloConsumidor] Procesando reserva: " + reserva.getId());
                gestorTickets.generarTicket(reserva);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                corriendo = false;
                System.out.println("[HiloConsumidor] Hilo detenido.");
            }
        }
    }

    public void detener() {
        corriendo = false;
        interrupt();
    }
}