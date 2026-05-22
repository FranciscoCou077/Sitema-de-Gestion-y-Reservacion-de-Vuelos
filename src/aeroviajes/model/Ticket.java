package aeroviajes.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Comprobante generado a partir de una reserva. Su contenido textual
 * puede escribirse a un archivo .txt descargable o mostrarse en pantalla.
 *
 * @author Equipo Aeroviajes
 */
public class Ticket implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String codigoTicket;
    private final Reserva reserva;
    private final LocalDateTime fechaEmision;

    public Ticket(String codigoTicket, Reserva reserva) {
        this.codigoTicket = codigoTicket;
        this.reserva = reserva;
        this.fechaEmision = LocalDateTime.now();
    }

    public String getCodigoTicket() { return codigoTicket; }
    public Reserva getReserva() { return reserva; }
    public LocalDateTime getFechaEmision() { return fechaEmision; }

    /**
     * Construye el contenido textual del ticket, listo para escribirse
     * a un archivo o mostrarse al usuario.
     *
     * @return el comprobante formateado como texto
     */
    public String generarContenido() {
        Vuelo v = reserva.getVuelo();
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        StringBuilder sb = new StringBuilder();
        sb.append("====================================\n");
        sb.append("        AEROVIAJES - BOLETO\n");
        sb.append("====================================\n");
        sb.append("Codigo de ticket : ").append(codigoTicket).append("\n");
        sb.append("Pasajero         : ").append(reserva.getCliente().getNombreCompleto()).append("\n");
        sb.append("Vuelo            : ").append(v.getId()).append("\n");
        sb.append("Aerolinea        : ").append(v.getAerolinea().getNombre()).append("\n");
        sb.append("Origen           : ").append(v.getOrigen()).append("\n");
        sb.append("Destino          : ").append(v.getDestino()).append("\n");
        sb.append("Fecha de salida  : ").append(v.getFechaSalida().format(fmt)).append("\n");
        sb.append("Asiento          : ").append(reserva.getAsientoAsignado()).append("\n");
        sb.append("Precio           : $").append(String.format("%.2f", v.getPrecio())).append("\n");
        sb.append("Emision          : ").append(fechaEmision.format(fmt)).append("\n");
        sb.append("====================================\n");
        return sb.toString();
    }

    @Override
    public String toString() {
        return "Ticket " + codigoTicket + " (reserva " + reserva.getId() + ")";
    }
}
