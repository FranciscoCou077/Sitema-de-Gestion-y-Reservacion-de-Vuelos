/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/File.java to edit this template
 */
package aeroviajes.ui;

/**
 *
 * @author Aeroviajes Francisco & Ernesto
 */

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
 * Menu del administrador despues de iniciar sesion. Da acceso a la
 * gestion de vuelos y de usuarios (operaciones protegidas por rol).
 */
public class PanelAdmin extends JPanel implements PanelActualizable {

    private final VentanaPrincipal ventana;
    private final JLabel lblSaludo = new JLabel();

    public PanelAdmin(VentanaPrincipal ventana) {
        this.ventana = ventana;
        setLayout(new GridBagLayout());
        setBackground(new Color(245, 247, 250));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        lblSaludo.setFont(new Font("SansSerif", Font.BOLD, 22));
        lblSaludo.setForeground(new Color(40, 60, 110));
        lblSaludo.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridy = 0;
        gbc.insets = new Insets(30, 10, 30, 10);
        add(lblSaludo, gbc);

        gbc.insets = new Insets(8, 10, 8, 10);

        JButton btnVuelos = crearBoton("Gestion de vuelos", new Color(40, 110, 210));
        btnVuelos.addActionListener(e -> ventana.mostrarPanel("gestionVuelos"));
        gbc.gridy = 1;
        add(btnVuelos, gbc);

        JButton btnUsuarios = crearBoton("Gestion de usuarios", new Color(40, 160, 100));
        btnUsuarios.addActionListener(e -> ventana.mostrarPanel("gestionUsuarios"));
        gbc.gridy = 2;
        add(btnUsuarios, gbc);

        JButton btnSalir = crearBoton("Cerrar sesion", new Color(180, 80, 70));
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
            lblSaludo.setText("Panel de administrador - " + u.getNombreCompleto());
        }
    }
}