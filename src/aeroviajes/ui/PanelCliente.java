package aeroviajes.ui;

import aeroviajes.model.Usuario;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 * Menu del cliente despues de iniciar sesion. Muestra un saludo
 * personalizado y los accesos a las funcionalidades disponibles.
 *
 * Por ahora muestra avisos en las funciones que dependen de la capa de
 * servicio. Cuando esten listas, se conectaran los paneles correspondientes.
 *
 * @author Equipo Aeroviajes
 */
public class PanelCliente extends JPanel implements PanelActualizable {

    private final VentanaPrincipal ventana;
    private final JLabel lblSaludo = new JLabel();

    public PanelCliente(VentanaPrincipal ventana) {
        this.ventana = ventana;
        setLayout(new GridBagLayout());
        setBackground(new Color(245, 247, 250));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Saludo (se actualiza al mostrar el panel)
        lblSaludo.setFont(new Font("SansSerif", Font.BOLD, 22));
        lblSaludo.setForeground(new Color(40, 60, 110));
        lblSaludo.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridy = 0;
        gbc.insets = new Insets(30, 10, 30, 10);
        add(lblSaludo, gbc);

        // Botones de las opciones disponibles
        gbc.insets = new Insets(8, 10, 8, 10);

        JButton btnVuelos = crearBoton("Consultar vuelos disponibles",
                new Color(40, 110, 210));
        btnVuelos.addActionListener(e -> ventana.info(
                "El listado de vuelos estara disponible cuando se integre "
                + "el GestorVuelos de la capa de servicio."));
        gbc.gridy = 1;
        add(btnVuelos, gbc);

        JButton btnReservas = crearBoton("Mis reservas",
                new Color(40, 160, 100));
        btnReservas.addActionListener(e -> ventana.info(
                "Tus reservas se mostraran cuando se integre el "
                + "GestorReservas de la capa de servicio."));
        gbc.gridy = 2;
        add(btnReservas, gbc);

        JButton btnSalir = crearBoton("Cerrar sesion",
                new Color(180, 80, 70));
        btnSalir.addActionListener(e -> {
            ventana.setUsuarioActual(null);
            ventana.mostrarPanel(VentanaPrincipal.P_BIENVENIDA);
        });
        gbc.gridy = 3;
        gbc.insets = new Insets(30, 10, 10, 10);
        add(btnSalir, gbc);
    }

    private JButton crearBoton(String texto, Color color) {
        JButton b = new JButton(texto);
        b.setPreferredSize(new Dimension(320, 42));
        b.setFont(new Font("SansSerif", Font.BOLD, 14));
        b.setBackground(color);
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        return b;
    }

    @Override
    public void alMostrarse() {
        Usuario u = ventana.getUsuarioActual();
        if (u != null) {
            lblSaludo.setText("Bienvenido, " + u.getNombreCompleto());
        }
    }
}
