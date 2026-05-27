package aeroviajes.ui;

import aeroviajes.model.Reserva;
import aeroviajes.model.Usuario;
import aeroviajes.service.GestorReservas;
import java.util.List;
import java.awt.BorderLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

/**
 * Muestra el historial de reservas del cliente.
 * Permite seleccionar una para ver su ticket.
 */
public class PanelMisReservas extends JPanel {

    private final VentanaPrincipal ventana;
    private final GestorReservas gestorReservas;
    private final Usuario clienteActual;

    private JTable tablaReservas;
    private DefaultTableModel modeloTabla;
    private JButton btnVerTicket;
    private JButton btnRegresar;

    public PanelMisReservas(VentanaPrincipal ventana, GestorReservas gestorReservas,
                             Usuario clienteActual) {
        this.ventana = ventana;
        this.gestorReservas = gestorReservas;
        this.clienteActual = clienteActual;
        initComponentes();
    }

    private void initComponentes() {
        setLayout(new BorderLayout(10, 10));
        add(new JLabel("Mis reservas", JLabel.CENTER), BorderLayout.NORTH);

        String[] columnas = {"ID Reserva", "Vuelo", "Origen", "Destino", "Fecha", "Precio"};
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
        btnVerTicket = new JButton("Ver ticket");
        btnRegresar = new JButton("Regresar");

        btnVerTicket.addActionListener(e -> verTicketSeleccionado());
        btnRegresar.addActionListener(e -> ventana.mostrarPanel("cliente"));

        panelBotones.add(btnVerTicket);
        panelBotones.add(btnRegresar);
        add(panelBotones, BorderLayout.SOUTH);

        cargarReservas();
    }

    public void cargarReservas() {
        modeloTabla.setRowCount(0);
        List<Reserva> reservas = gestorReservas.obtenerReservasPorCliente(
                clienteActual.getCorreo());
        for (Reserva r : reservas) {
            modeloTabla.addRow(new Object[]{
                r.getIdReserva(),
                r.getVuelo().getId(),
                r.getVuelo().getOrigen().getCiudad(),
                r.getVuelo().getDestino().getCiudad(),
                r.getVuelo().getFechaSalida(),
                "$" + r.getVuelo().getPrecio()
            });
        }
    }

    private void verTicketSeleccionado() {
        int fila = tablaReservas.getSelectedRow();
        if (fila == -1) {
            javax.swing.JOptionPane.showMessageDialog(this,
                "Selecciona una reserva primero.");
            return;
        }
        String idReserva = (String) modeloTabla.getValueAt(fila, 0);
        ventana.setReservaSeleccionada(idReserva);
        ventana.mostrarPanel("ticket");
    }
}
