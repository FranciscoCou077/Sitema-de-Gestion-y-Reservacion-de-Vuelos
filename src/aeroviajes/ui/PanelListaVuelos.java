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
 * El cliente puede seleccionar uno y pasar a la pantalla de compra.
 */
public class PanelListaVuelos extends JPanel {

    private final VentanaPrincipal ventana;
    private final IGestorVuelos gestorVuelos;
    private final PanelCompra panelCompra;

    private JTable tablaVuelos;
    private DefaultTableModel modeloTabla;
    private JButton btnComprar;
    private JButton btnRegresar;

    public PanelListaVuelos(VentanaPrincipal ventana, IGestorVuelos gestorVuelos,
                             PanelCompra panelCompra) {
        this.ventana = ventana;
        this.gestorVuelos = gestorVuelos;
        this.panelCompra = panelCompra;
        initComponentes();
    }

    private void initComponentes() {
        setLayout(new BorderLayout(10, 10));
        add(new JLabel("Vuelos disponibles", JLabel.CENTER), BorderLayout.NORTH);

        String[] columnas = {"ID", "Origen", "Destino", "Fecha", "Precio", "Asientos"};
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
        btnComprar = new JButton("Comprar boleto");
        btnRegresar = new JButton("Regresar");
        btnComprar.addActionListener(e -> irACompra());
        btnRegresar.addActionListener(e -> ventana.mostrarPanel(VentanaPrincipal.P_CLIENTE));
        panelBotones.add(btnComprar);
        panelBotones.add(btnRegresar);
        add(panelBotones, BorderLayout.SOUTH);

        cargarVuelos();
    }

    public void cargarVuelos() {
        modeloTabla.setRowCount(0);
        List<Vuelo> vuelos = gestorVuelos.obtenerVuelosDisponibles();
        for (Vuelo v : vuelos) {
            modeloTabla.addRow(new Object[]{
                v.getId(),
                v.getOrigen().getCiudad(),
                v.getDestino().getCiudad(),
                v.getFechaFormateada(),
                String.format("$%.2f", v.getPrecio()),
                v.getAsientosDisponibles()
            });
        }
    }

    private void irACompra() {
        int fila = tablaVuelos.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Por favor selecciona un vuelo primero.");
            return;
        }
        String idVuelo = (String) modeloTabla.getValueAt(fila, 0);
        panelCompra.setIdVueloSeleccionado(idVuelo); // Le avisamos al panel de compra
        ventana.mostrarPanel("compra");
    }
}