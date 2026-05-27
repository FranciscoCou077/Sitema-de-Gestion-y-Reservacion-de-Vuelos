package aeroviajes.ui;

import aeroviajes.model.Reserva;
import java.awt.BorderLayout;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 * Muestra el contenido del ticket de una reserva confirmada.
 */
public class PanelTicket extends JPanel {

    private final VentanaPrincipal ventana;
    private JTextArea areaTicket;

    public PanelTicket(VentanaPrincipal ventana) {
        this.ventana = ventana;
        initComponentes();
    }

    private void initComponentes() {
        setLayout(new BorderLayout(10, 10));
        add(new JLabel("Tu boleto", JLabel.CENTER), BorderLayout.NORTH);

        areaTicket = new JTextArea();
        areaTicket.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 13));
        areaTicket.setEditable(false);
        add(new JScrollPane(areaTicket), BorderLayout.CENTER);

        JPanel panelBotones = new JPanel();
        JButton btnRegresar = new JButton("Regresar a mis reservas");
        btnRegresar.addActionListener(e -> ventana.mostrarPanel("misReservas"));
        panelBotones.add(btnRegresar);
        add(panelBotones, BorderLayout.SOUTH);
    }

    /**
     * Recibe la reserva y usa el metodo generarContenido() del Ticket
     * para mostrar el comprobante formateado.
     */
    public void mostrarReserva(Reserva reserva) {
        if (reserva == null) {
            areaTicket.setText("No se encontro la reserva.");
            return;
        }
        // Construimos el texto directamente desde la reserva
        // (el Ticket con su codigo lo genera GestorTickets en segundo plano)
        StringBuilder sb = new StringBuilder();
        sb.append("====================================\n");
        sb.append("        AEROVIAJES - BOLETO\n");
        sb.append("====================================\n");
        sb.append("ID Reserva : ").append(reserva.getId()).append("\n");
        sb.append("Pasajero   : ").append(reserva.getCliente().getNombreCompleto()).append("\n");
        sb.append("Vuelo      : ").append(reserva.getVuelo().getId()).append("\n");
        sb.append("Aerolinea  : ").append(reserva.getVuelo().getAerolinea().getNombre()).append("\n");
        sb.append("Origen     : ").append(reserva.getVuelo().getOrigen().getCiudad()).append("\n");
        sb.append("Destino    : ").append(reserva.getVuelo().getDestino().getCiudad()).append("\n");
        sb.append("Fecha      : ").append(reserva.getVuelo().getFechaFormateada()).append("\n");
        sb.append("Asiento    : ").append(reserva.getAsientoAsignado()).append("\n");
        sb.append("Precio     : $").append(String.format("%.2f", reserva.getVuelo().getPrecio())).append("\n");
        sb.append("Estado     : ").append(reserva.getEstado()).append("\n");
        sb.append("====================================\n");
        sb.append("   Gracias por volar con Aeroviajes :)\n");
        areaTicket.setText(sb.toString());
    }
}