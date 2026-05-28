package aeroviajes.ui;

import aeroviajes.model.Reserva;
import aeroviajes.util.GeneradorCodigoTicket;
import java.awt.BorderLayout;
import java.awt.Font;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 * Muestra el contenido del ticket de una reserva confirmada.
 * El cliente puede verlo en pantalla y descargarlo como .txt
 * en la carpeta que prefiera.
 */
public class PanelTicket extends JPanel {

    private final VentanaPrincipal ventana;
    private JTextArea areaTicket;
    private Reserva reservaActual;

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
        JButton btnDescargar = new JButton("Descargar ticket (.txt)");
        JButton btnRegresar  = new JButton("Regresar a mis reservas");

        btnDescargar.addActionListener(e -> descargarTicket());
        btnRegresar.addActionListener(e -> ventana.mostrarPanel("misReservas"));

        panelBotones.add(btnDescargar);
        panelBotones.add(btnRegresar);
        add(panelBotones, BorderLayout.SOUTH);
    }

    /**
     * Recibe la reserva y muestra el comprobante en pantalla.
     */
    public void mostrarReserva(Reserva reserva) {
        this.reservaActual = reserva;
        if (reserva == null) {
            areaTicket.setText("No se encontro la reserva.");
            return;
        }
        areaTicket.setText(generarTexto(reserva));
    }

    /**
     * Abre un JFileChooser para que el usuario elija donde guardar el .txt
     * y escribe el contenido del ticket en ese archivo.
     */
    private void descargarTicket() {
        if (reservaActual == null) {
            JOptionPane.showMessageDialog(this, "No hay ticket para descargar.");
            return;
        }

        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Guardar ticket");
        chooser.setSelectedFile(new File("ticket-" + reservaActual.getId() + ".txt"));

        int resultado = chooser.showSaveDialog(this);
        if (resultado == JFileChooser.APPROVE_OPTION) {
            File archivo = chooser.getSelectedFile();
            // Asegurarse de que tenga extension .txt
            if (!archivo.getName().endsWith(".txt")) {
                archivo = new File(archivo.getAbsolutePath() + ".txt");
            }
            try (PrintWriter pw = new PrintWriter(new FileWriter(archivo))) {
                pw.print(generarTexto(reservaActual));
                JOptionPane.showMessageDialog(this,
                    "Ticket guardado en:\n" + archivo.getAbsolutePath(),
                    "Descarga exitosa", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this,
                    "Error al guardar el ticket: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private String generarTexto(Reserva reserva) {
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
        return sb.toString();
    }
}