package aeroviajes.ui;

import aeroviajes.model.Usuario;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Panel principal del cliente despues de iniciar sesion.
 * Muestra un saludo y los botones para navegar a las distintas secciones.
 */
public class PanelCliente extends JPanel {

    private final VentanaPrincipal ventana;

    public PanelCliente(VentanaPrincipal ventana) {
        this.ventana = ventana;
        initComponentes();
    }

    private void initComponentes() {
        setLayout(new BorderLayout(10, 10));

        // El saludo se actualiza cuando se muestra el panel
        JLabel lblBienvenida = new JLabel("Bienvenido", JLabel.CENTER);
        add(lblBienvenida, BorderLayout.NORTH);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));

        JButton btnVerVuelos   = new JButton("Ver vuelos disponibles");
        JButton btnMisReservas = new JButton("Mis reservas");
        JButton btnCerrarSesion = new JButton("Cerrar sesion");

        btnVerVuelos.addActionListener(e -> ventana.mostrarPanel("listaVuelos"));
        btnMisReservas.addActionListener(e -> ventana.mostrarPanel("misReservas"));
        btnCerrarSesion.addActionListener(e -> {
            ventana.setUsuarioActual(null);
            ventana.mostrarPanel(VentanaPrincipal.P_BIENVENIDA);
        });

        panelBotones.add(btnVerVuelos);
        panelBotones.add(btnMisReservas);
        panelBotones.add(btnCerrarSesion);
        add(panelBotones, BorderLayout.CENTER);

        // Actualizar saludo cuando el panel se vuelve visible
        addHierarchyListener(e -> {
            if (isShowing()) {
                Usuario u = ventana.getUsuarioActual();
                if (u != null) {
                    lblBienvenida.setText("Bienvenido, " + u.getNombreCompleto());
                }
            }
        });
    }
}