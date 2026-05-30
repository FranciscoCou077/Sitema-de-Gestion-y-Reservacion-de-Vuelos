/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/File.java to edit this template
 */
package aeroviajes.ui;

/**
 *
 * @author Francisco equipo aeroviajes Francisco & Ernesto
 */
import aeroviajes.exception.UsuarioNoEncontrado;
import aeroviajes.model.Usuario;
import aeroviajes.service.GestorUsuarios;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

/**
 * Panel del administrador para ver y eliminar usuarios registrados.
 */
public class PanelGestionUsuarios extends JPanel implements PanelActualizable {

    private final VentanaPrincipal ventana;
    private final GestorUsuarios gestorUsuarios;
    private final DefaultTableModel modeloTabla;
    private final JTable tabla;

    public PanelGestionUsuarios(VentanaPrincipal ventana, GestorUsuarios gestorUsuarios) {
        this.ventana = ventana;
        this.gestorUsuarios = gestorUsuarios;
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(245, 247, 250));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel titulo = new JLabel("Gestion de usuarios (Admin)", SwingConstants.CENTER);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 20));
        titulo.setForeground(new Color(40, 60, 110));
        add(titulo, BorderLayout.NORTH);

        String[] cols = {"Correo", "Nombre", "Apellido", "Rol"};
        modeloTabla = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tabla = new JTable(modeloTabla);
        tabla.setRowHeight(24);
        add(new JScrollPane(tabla), BorderLayout.CENTER);

        JPanel botones = new JPanel();
        botones.setBackground(new Color(245, 247, 250));
        JButton btnEliminar = new JButton("Eliminar seleccionado");
        btnEliminar.addActionListener(e -> eliminarSeleccionado());
        JButton btnRegresar = new JButton("Regresar");
        btnRegresar.addActionListener(e -> ventana.mostrarPanel("admin"));
        botones.add(btnEliminar);
        botones.add(btnRegresar);
        add(botones, BorderLayout.SOUTH);
    }

    private void eliminarSeleccionado() {
        int fila = tabla.getSelectedRow();
        if (fila == -1) {
            ventana.info("Selecciona un usuario primero.");
            return;
        }
        String correo = (String) modeloTabla.getValueAt(fila, 0);
        if (ventana.getUsuarioActual() != null
                && correo.equals(ventana.getUsuarioActual().getCorreo())) {
            ventana.error("No puedes eliminar tu propia cuenta de administrador.");
            return;
        }
        int conf = JOptionPane.showConfirmDialog(this,
                "Eliminar al usuario " + correo + "?",
                "Confirmar", JOptionPane.YES_NO_OPTION);
        if (conf != JOptionPane.YES_OPTION) return;
        try {
            gestorUsuarios.eliminarUsuario(correo);
            ventana.info("Usuario eliminado.");
            cargarTabla();
        } catch (UsuarioNoEncontrado ex) {
            ventana.error("Error: " + ex.getMessage());
        }
    }

    private void cargarTabla() {
        modeloTabla.setRowCount(0);
        for (Usuario u : gestorUsuarios.obtenerTodosLosUsuarios()) {
            modeloTabla.addRow(new Object[]{
                u.getCorreo(), u.getNombre(), u.getApellido(), u.getRol()
            });
        }
    }

    @Override
    public void alMostrarse() {
        cargarTabla();
    }
}