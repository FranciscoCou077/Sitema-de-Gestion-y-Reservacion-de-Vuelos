/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/File.java to edit this template
 */
package aeroviajes.ui;

/**
 *
 * @author Francisco
 */
import aeroviajes.exception.Autenticacion;
import aeroviajes.exception.VueloNoDisponible;
import aeroviajes.model.Aerolinea;
import aeroviajes.model.Aeropuerto;
import aeroviajes.model.Vuelo;
import aeroviajes.patterns.flyweight.AerolineaFlyweightFactory;
import aeroviajes.patterns.flyweight.AeropuertoFlyweightFactory;
import aeroviajes.service.GestorVuelosReal;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

/**
 * Panel de administrador para agregar / eliminar / listar vuelos.
 */
public class PanelGestionVuelos extends JPanel implements PanelActualizable {

    private final VentanaPrincipal ventana;
    private final GestorVuelosReal gestorVuelos;
    private final DefaultTableModel modeloTabla;
    private final JTable tabla;

    private final JTextField txtId        = new JTextField();
    private final JTextField txtOrigen    = new JTextField();
    private final JTextField txtDestino   = new JTextField();
    private final JTextField txtAerolinea = new JTextField();
    private final JTextField txtFecha     = new JTextField();
    private final JTextField txtPrecio    = new JTextField();
    private final JTextField txtAsientos  = new JTextField();

    public PanelGestionVuelos(VentanaPrincipal ventana, GestorVuelosReal gestorVuelos) {
        this.ventana = ventana;
        this.gestorVuelos = gestorVuelos;
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(245, 247, 250));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel titulo = new JLabel("Gestion de vuelos (Admin)", SwingConstants.CENTER);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 20));
        titulo.setForeground(new Color(40, 60, 110));
        add(titulo, BorderLayout.NORTH);

        String[] cols = {"ID", "Aerolinea", "Origen", "Destino", "Fecha", "Precio", "Asientos"};
        modeloTabla = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tabla = new JTable(modeloTabla);
        tabla.setRowHeight(24);
        add(new JScrollPane(tabla), BorderLayout.CENTER);

        JPanel inferior = new JPanel(new BorderLayout(5, 5));
        inferior.setBackground(new Color(245, 247, 250));

        JLabel info = new JLabel(
                "Codigos disponibles: MEX, CUN, GDL, MTY, TIJ, MID  |  Aerolineas: AM, Y4, VB",
                SwingConstants.CENTER);
        info.setFont(new Font("SansSerif", Font.ITALIC, 11));
        info.setForeground(new Color(120, 80, 30));
        inferior.add(info, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridLayout(2, 7, 5, 5));
        form.setBackground(new Color(245, 247, 250));
        form.add(new JLabel("ID:"));
        form.add(new JLabel("Origen:"));
        form.add(new JLabel("Destino:"));
        form.add(new JLabel("Aerolinea:"));
        form.add(new JLabel("Fecha (dd/MM/yyyy HH:mm):"));
        form.add(new JLabel("Precio:"));
        form.add(new JLabel("Asientos:"));
        form.add(txtId);
        form.add(txtOrigen);
        form.add(txtDestino);
        form.add(txtAerolinea);
        form.add(txtFecha);
        form.add(txtPrecio);
        form.add(txtAsientos);
        inferior.add(form, BorderLayout.CENTER);

        JPanel botones = new JPanel();
        botones.setBackground(new Color(245, 247, 250));
        JButton btnAgregar = new JButton("Agregar vuelo");
        btnAgregar.addActionListener(e -> agregarVuelo());
        JButton btnEliminar = new JButton("Eliminar seleccionado");
        btnEliminar.addActionListener(e -> eliminarVueloSeleccionado());
        JButton btnRegresar = new JButton("Regresar");
        btnRegresar.addActionListener(e -> ventana.mostrarPanel("admin"));
        botones.add(btnAgregar);
        botones.add(btnEliminar);
        botones.add(btnRegresar);
        inferior.add(botones, BorderLayout.SOUTH);

        add(inferior, BorderLayout.SOUTH);
    }

    private void agregarVuelo() {
        try {
            String id = txtId.getText().trim();
            if (id.isEmpty()) {
                id = "V" + UUID.randomUUID().toString().substring(0, 6).toUpperCase();
            }

            String codOrigen    = txtOrigen.getText().trim().toUpperCase();
            String codDestino   = txtDestino.getText().trim().toUpperCase();
            String codAerolinea = txtAerolinea.getText().trim().toUpperCase();

            Aeropuerto origen    = AeropuertoFlyweightFactory.obtener(codOrigen);
            Aeropuerto destino   = AeropuertoFlyweightFactory.obtener(codDestino);
            Aerolinea  aerolinea = AerolineaFlyweightFactory.obtener(codAerolinea);

            if (origen == null)    { ventana.error("Codigo de origen no existe: " + codOrigen);       return; }
            if (destino == null)   { ventana.error("Codigo de destino no existe: " + codDestino);     return; }
            if (aerolinea == null) { ventana.error("Codigo de aerolinea no existe: " + codAerolinea); return; }

            LocalDateTime fecha = LocalDateTime.parse(txtFecha.getText().trim(),
                    DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
            double precio  = Double.parseDouble(txtPrecio.getText().trim());
            int asientos   = Integer.parseInt(txtAsientos.getText().trim());

            Vuelo v = new Vuelo(id, origen, destino, aerolinea, fecha, precio, asientos);
            gestorVuelos.agregarVuelo(v);
            ventana.info("Vuelo " + id + " agregado correctamente.");
            limpiarForm();
            cargarTabla();
        } catch (Autenticacion ex) {
            ventana.error(ex.getMessage());
        } catch (Exception ex) {
            ventana.error("Error al agregar vuelo: " + ex.getMessage());
        }
    }

    private void eliminarVueloSeleccionado() {
        int fila = tabla.getSelectedRow();
        if (fila == -1) {
            ventana.info("Selecciona un vuelo de la tabla primero.");
            return;
        }
        String id = (String) modeloTabla.getValueAt(fila, 0);
        int conf = JOptionPane.showConfirmDialog(this,
                "Eliminar el vuelo " + id + "?",
                "Confirmar", JOptionPane.YES_NO_OPTION);
        if (conf != JOptionPane.YES_OPTION) return;
        try {
            gestorVuelos.eliminarVuelo(id);
            ventana.info("Vuelo eliminado.");
            cargarTabla();
        } catch (Autenticacion | VueloNoDisponible ex) {
            ventana.error("Error: " + ex.getMessage());
        }
    }

    private void limpiarForm() {
        txtId.setText("");
        txtOrigen.setText("");
        txtDestino.setText("");
        txtAerolinea.setText("");
        txtFecha.setText("");
        txtPrecio.setText("");
        txtAsientos.setText("");
    }

    private void cargarTabla() {
        modeloTabla.setRowCount(0);
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        for (Vuelo v : gestorVuelos.getVuelos().values()) {
            modeloTabla.addRow(new Object[]{
                v.getId(),
                v.getAerolinea().getNombre(),
                v.getOrigen().getCodigoIATA(),
                v.getDestino().getCodigoIATA(),
                v.getFechaSalida().format(fmt),
                "$" + v.getPrecio(),
                v.getAsientosDisponibles() + "/" + v.getAsientosTotales()
            });
        }
    }

    @Override
    public void alMostrarse() {
        cargarTabla();
    }
}