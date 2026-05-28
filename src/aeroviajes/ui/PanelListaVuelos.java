package aeroviajes.ui;

import aeroviajes.model.Vuelo;
import aeroviajes.service.IGestorVuelos;
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
 * Muestra la tabla de vuelos disponibles.
 * Al seleccionar uno, el cliente pasa al detalle del vuelo
 * antes de proceder con la compra.
 *
 * Implementa PanelActualizable para recargar la lista
 * cada vez que se navega aqui.
 */
public class PanelListaVuelos extends JPanel implements PanelActualizable {

    private final VentanaPrincipal ventana;
    private final IGestorVuelos gestorVuelos;
    private final PanelDetalleVuelo panelDetalle;

    private JTable tablaVuelos;
    private DefaultTableModel modeloTabla;

    public PanelListaVuelos(VentanaPrincipal ventana, IGestorVuelos gestorVuelos,
                             PanelDetalleVuelo panelDetalle) {
        this.ventana = ventana;
        this.gestorVuelos = gestorVuelos;
        this.panelDetalle = panelDetalle;
        initComponentes();
    }

    private void initComponentes() {
        setLayout(new BorderLayout(10, 10));
        add(new JLabel("Vuelos disponibles", JLabel.CENTER), BorderLayout.NORTH);

        String[] columnas = {"ID", "Aerolinea", "Origen", "Destino", "Fecha", "Precio", "Asientos"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablaVuelos = new JTable(modeloTabla);
        tablaVuelos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        add(new JScrollPane(tablaVuelos), BorderLayout.CENTER);

        JPanel panelBotones = new JPanel();
        JButton btnDetalle  = new JButton("Ver detalle y comprar");
        JButton btnRegresar = new JButton("Regresar");

        btnDetalle.addActionListener(e -> irADetalle());
        btnRegresar.addActionListener(e -> ventana.mostrarPanel(VentanaPrincipal.P_CLIENTE));

        panelBotones.add(btnDetalle);
        panelBotones.add(btnRegresar);
        add(panelBotones, BorderLayout.SOUTH);
    }

    @Override
    public void alMostrarse() {
        cargarVuelos();
    }

    public void cargarVuelos() {
        modeloTabla.setRowCount(0);
        List<Vuelo> vuelos = gestorVuelos.obtenerVuelosDisponibles();
        for (Vuelo v : vuelos) {
            modeloTabla.addRow(new Object[]{
                v.getId(),
                v.getAerolinea().getNombre(),
                v.getOrigen().getCiudad(),
                v.getDestino().getCiudad(),
                v.getFechaFormateada(),
                String.format("$%.2f", v.getPrecio()),
                v.getAsientosDisponibles()
            });
        }
    }

    private void irADetalle() {
        int fila = tablaVuelos.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Por favor selecciona un vuelo primero.");
            return;
        }
        String idVuelo = (String) modeloTabla.getValueAt(fila, 0);
        panelDetalle.setIdVuelo(idVuelo);
        ventana.mostrarPanel("detalleVuelo");
    }
}