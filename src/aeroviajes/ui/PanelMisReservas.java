package aeroviajes.ui;

import aeroviajes.model.Cliente;
import aeroviajes.model.Reserva;
import aeroviajes.service.GestorReservas;
import java.awt.BorderLayout;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

/**
 * Muestra el historial de reservas del cliente actual.
 */
public class PanelMisReservas extends JPanel {

    private final VentanaPrincipal ventana;
    private final GestorReservas gestorReservas;
    private final PanelTicket panelTicket;

    private JTable tablaReservas;
    private DefaultTableModel modeloTabla;
    private List<Reserva> reservasActuales;

    public PanelMisReservas(VentanaPrincipal ventana, GestorReservas gestorReservas,
                             PanelTicket panelTicket) {
        this.ventana = ventana;
        this.gestorReservas = gestorReservas;
        this.panelTicket = panelTicket;
        initComponentes();
    }

    private void initComponentes() {
        setLayout(new BorderLayout(10, 10));
        add(new JLabel("Mis reservas", JLabel.CENTER), BorderLayout.NORTH);

        String[] columnas = {"ID Reserva", "Vuelo", "Origen", "Destino", "Fecha", "Asiento"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablaReservas = new JTable(modeloTabla);
        tablaReservas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        add(new JScrollPane(tablaReservas), BorderLayout.CENTER);

        JPanel panelBotones = new JPanel();
        JButton btnVerTicket = new JButton("Ver ticket");
        JButton btnRegresar = new JButton("Regresar");
        btnVerTicket.addActionListener(e -> verTicketSeleccionado());
        btnRegresar.addActionListener(e -> ventana.mostrarPanel(VentanaPrincipal.P_CLIENTE));
        panelBotones.add(btnVerTicket);
        panelBotones.add(btnRegresar);
        add(panelBotones, BorderLayout.SOUTH);
    }

    public void cargarReservas() {
        modeloTabla.setRowCount(0);
        if (!(ventana.getUsuarioActual() instanceof Cliente)) return;

        String correo = ventana.getUsuarioActual().getCorreo();
        reservasActuales = gestorReservas.obtenerReservasPorCliente(correo);

        for (Reserva r : reservasActuales) {
            modeloTabla.addRow(new Object[]{
                r.getId(),
                r.getVuelo().getId(),
                r.getVuelo().getOrigen().getCiudad(),
                r.getVuelo().getDestino().getCiudad(),
                r.getVuelo().getFechaFormateada(),
                r.getAsientoAsignado()
            });
        }
    }

    private void verTicketSeleccionado() {
        int fila = tablaReservas.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona una reserva primero.");
            return;
        }
        Reserva reserva = reservasActuales.get(fila);
        panelTicket.mostrarReserva(reserva); // Le pasamos la reserva directamente
        ventana.mostrarPanel("ticket");
    }
}