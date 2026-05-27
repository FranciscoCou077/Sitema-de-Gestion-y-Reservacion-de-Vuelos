package aeroviajes.service;

import aeroviajes.model.Reserva;
import aeroviajes.model.Ticket;
import aeroviajes.util.GeneradorCodigoTicket;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Genera el archivo .txt del ticket a partir de una reserva confirmada.
 * Este metodo lo invoca el hilo consumidor, nunca la GUI directamente,
 * para no bloquear la interfaz.
 */
public class GestorTickets {

    private static final String CARPETA_TICKETS = "data/tickets/";

    public Ticket generarTicket(Reserva reserva) {
        String codigo = GeneradorCodigoTicket.generar();
        Ticket ticket = new Ticket(codigo, reserva);
        guardarEnArchivo(ticket);
        return ticket;
    }

    private void guardarEnArchivo(Ticket ticket) {
        String nombreArchivo = CARPETA_TICKETS + ticket.getCodigoTicket() + ".txt";
        try (PrintWriter pw = new PrintWriter(new FileWriter(nombreArchivo))) {
            pw.print(ticket.generarContenido()); // Ticket ya tiene el formato listo
            System.out.println("[GestorTickets] Ticket guardado: " + nombreArchivo);
        } catch (IOException e) {
            System.err.println("[GestorTickets] Error al guardar ticket: " + e.getMessage());
        }
    }
}