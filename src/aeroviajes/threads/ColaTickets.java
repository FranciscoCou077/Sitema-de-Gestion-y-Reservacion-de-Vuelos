package aeroviajes.threads;

import aeroviajes.model.Reserva;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Cola thread-safe que conecta al productor (ProductorTickets)
 * con el consumidor (HiloConsumidorTickets).
 * Usa LinkedBlockingQueue que es segura para multiples hilos sin
 * necesidad de sincronizacion manual.
 */
public class ColaTickets {

    private final BlockingQueue<Reserva> cola;

    public ColaTickets() {
        this.cola = new LinkedBlockingQueue<>();
    }

    /**
     * El productor llama este metodo para meter una reserva.
     * Si la cola esta llena, el hilo espera automaticamente.
     */
    public void agregar(Reserva reserva) throws InterruptedException {
        cola.put(reserva);
    }

    /**
     * El consumidor llama este metodo para sacar una reserva.
     * Si la cola esta vacia, el hilo espera hasta que llegue algo.
     */
    public Reserva tomar() throws InterruptedException {
        return cola.take();
    }

    public boolean estaVacia() {
        return cola.isEmpty();
    }

    public int tamano() {
        return cola.size();
    }
}
