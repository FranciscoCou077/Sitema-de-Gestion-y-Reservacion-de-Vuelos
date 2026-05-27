package aeroviajes.ui;

import aeroviajes.model.Reserva;
import java.awt.BorderLayout;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;

/**
 * Muestra los detalles del ticket de una reserva.
 * El cliente puede ver la informacion de su vuelo confirmado.
 */
public class PanelTicket extends JPanel {

    private final VentanaPrincipal ventana;

    private JTextArea areaTicket;
    private JButton btnRegresar;

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
        btnRegresar = new JButton("Regresar a mis reservas");
        btnRegresar.addActionListener(e -> ventana.mostrarPanel("misReservas"));
        panelBotones.add(btnRegresar);
        add(panelBotones, BorderLayout.SOUTH);
    }

    /**
     * Carga y muestra los datos de la reserva seleccionada.
     */
    public void mostrarReserva(Reserva reserva) {
        if (reserva == null) {
            areaTicket.setText("No se encontro la reserva.");
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("============================================\n");
        sb.append("        AEROVIAJES  -  BOLETO DE VUELO     \n");
        sb.append("============================================\n");
        sb.append("ID Reserva : ").append(reserva.getIdReserva()).append("\n");
        sb.append("Cliente    : ").append(reserva.getIdCliente()).append("\n");
        sb.append("Vuelo      : ").append(reserva.getVuelo().getId()).append("\n");
        sb.append("Origen     : ").append(reserva.getVuelo().getOrigen().getCiudad()).append("\n");
        sb.append("Destino    : ").append(reserva.getVuelo().getDestino().getCiudad()).append("\n");
        sb.append("Fecha      : ").append(reserva.getVuelo().getFechaSalida()).append("\n");
        sb.append("Precio     : $").append(reserva.getVuelo().getPrecio()).append("\n");
        sb.append("============================================\n");
        sb.append("   Gracias por volar con Aeroviajes :)     \n");
        areaTicket.setText(sb.toString());
    }
}
