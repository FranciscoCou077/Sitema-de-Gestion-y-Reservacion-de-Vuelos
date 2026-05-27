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
        String nombreArchivo = CARPETA_TICKETS + ticket.getCodigo() + ".txt";
        try (PrintWriter pw = new PrintWriter(new FileWriter(nombreArchivo))) {
            pw.println("============================================");
            pw.println("        AEROVIAJES  -  BOLETO DE VUELO     ");
            pw.println("============================================");
            pw.println("Codigo  : " + ticket.getCodigo());
            pw.println("Cliente : " + ticket.getReserva().getIdCliente());
            pw.println("Vuelo   : " + ticket.getReserva().getVuelo().getId());
            pw.println("Origen  : " + ticket.getReserva().getVuelo().getOrigen().getCiudad());
            pw.println("Destino : " + ticket.getReserva().getVuelo().getDestino().getCiudad());
            pw.println("Fecha   : " + ticket.getReserva().getVuelo().getFechaSalida());
            pw.println("Precio  : $" + ticket.getReserva().getVuelo().getPrecio());
            pw.println("============================================");
            pw.println("   Gracias por volar con Aeroviajes :)     ");
            System.out.println("[GestorTickets] Ticket guardado: " + nombreArchivo);
        } catch (IOException e) {
            System.err.println("[GestorTickets] Error al guardar ticket: " + e.getMessage());
        }
    }
}
