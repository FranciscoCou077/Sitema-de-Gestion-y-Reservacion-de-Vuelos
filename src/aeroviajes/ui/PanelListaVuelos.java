package aeroviajes.ui;

import aeroviajes.model.Vuelo;
import aeroviajes.service.IGestorVuelos;
import java.awt.BorderLayout;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JLabel;
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

    private JTable tablaVuelos;
    private DefaultTableModel modeloTabla;
    private JButton btnComprar;
    private JButton btnRegresar;

    public PanelListaVuelos(VentanaPrincipal ventana, IGestorVuelos gestorVuelos) {
        this.ventana = ventana;
        this.gestorVuelos = gestorVuelos;
        initComponentes();
    }

    private void initComponentes() {
        setLayout(new BorderLayout(10, 10));

        add(new JLabel("Vuelos disponibles", JLabel.CENTER), BorderLayout.NORTH);

        // Tabla de vuelos
        String[] columnas = {"ID", "Origen", "Destino", "Fecha", "Precio", "Asientos"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // La tabla es solo de lectura
            }
        };
        tablaVuelos = new JTable(modeloTabla);
        tablaVuelos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        add(new JScrollPane(tablaVuelos), BorderLayout.CENTER);

        // Botones
        JPanel panelBotones = new JPanel();
        btnComprar = new JButton("Comprar boleto");
        btnRegresar = new JButton("Regresar");

        btnComprar.addActionListener(e -> irACompra());
        btnRegresar.addActionListener(e -> ventana.mostrarPanel("cliente"));

        panelBotones.add(btnComprar);
        panelBotones.add(btnRegresar);
        add(panelBotones, BorderLayout.SOUTH);

        cargarVuelos();
    }

    private void cargarVuelos() {
        modeloTabla.setRowCount(0);
        List<Vuelo> vuelos = gestorVuelos.obtenerVuelosDisponibles();
        for (Vuelo v : vuelos) {
            modeloTabla.addRow(new Object[]{
                v.getId(),
                v.getOrigen().getCiudad(),
                v.getDestino().getCiudad(),
                v.getFechaSalida(),
                "$" + v.getPrecio(),
                v.getAsientosDisponibles()
            });
        }
    }

    private void irACompra() {
        int fila = tablaVuelos.getSelectedRow();
        if (fila == -1) {
            javax.swing.JOptionPane.showMessageDialog(this,
                "Por favor selecciona un vuelo primero.");
            return;
        }
        String idVuelo = (String) modeloTabla.getValueAt(fila, 0);
        ventana.setVueloSeleccionado(idVuelo);
        ventana.mostrarPanel("compra");
    }
}
