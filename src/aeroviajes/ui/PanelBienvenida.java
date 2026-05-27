package aeroviajes.ui;

import aeroviajes.util.Constantes;
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
 * Pantalla de bienvenida con el menu principal. Permite registrarse,
 * iniciar sesion como cliente o acceder como administrador.
 *
 * @author Equipo Aeroviajes
 */
public class PanelBienvenida extends JPanel {

    public PanelBienvenida(VentanaPrincipal ventana) {
        setLayout(new GridBagLayout());
        setBackground(new Color(245, 247, 250));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Titulo
        JLabel titulo = new JLabel(Constantes.NOMBRE_APP.toUpperCase(),
                SwingConstants.CENTER);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 42));
        titulo.setForeground(new Color(40, 60, 110));
        gbc.gridy = 0;
        gbc.insets = new Insets(20, 8, 0, 8);
        add(titulo, gbc);

        // Subtitulo
        JLabel subtitulo = new JLabel(
                "Sistema de gestion y reservacion de vuelos", SwingConstants.CENTER);
        subtitulo.setFont(new Font("SansSerif", Font.PLAIN, 14));
        subtitulo.setForeground(new Color(90, 90, 90));
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 8, 40, 8);
        add(subtitulo, gbc);

        // Boton: iniciar sesion
        JButton btnLogin = crearBoton("Iniciar sesion (Cliente)", new Color(40, 110, 210));
        btnLogin.addActionListener(e -> ventana.mostrarPanel(VentanaPrincipal.P_LOGIN));
        gbc.gridy = 2;
        add(btnLogin, gbc);

        // Boton: registrarse
        JButton btnRegistro = crearBoton("Crear cuenta nueva", new Color(40, 160, 100));
        btnRegistro.addActionListener(e -> ventana.mostrarPanel(VentanaPrincipal.P_REGISTRO));
        gbc.gridy = 3;
        add(btnRegistro, gbc);

        // Boton: acceder como admin (usa el mismo panel de login, internamente
        // detectamos el rol del usuario una vez autenticado)
        JButton btnAdmin = crearBoton("Acceder como administrador", new Color(120, 90, 160));
        btnAdmin.addActionListener(e -> ventana.mostrarPanel(VentanaPrincipal.P_LOGIN));
        gbc.gridy = 4;
        add(btnAdmin, gbc);

        // Version (esquina inferior)
        JLabel version = new JLabel("v " + Constantes.VERSION, SwingConstants.CENTER);
        version.setFont(new Font("SansSerif", Font.ITALIC, 11));
        version.setForeground(Color.GRAY);
        gbc.gridy = 5;
        gbc.insets = new Insets(40, 8, 8, 8);
        add(version, gbc);
    }

    private JButton crearBoton(String texto, Color colorFondo) {
        JButton boton = new JButton(texto);
        boton.setPreferredSize(new Dimension(280, 44));
        boton.setFont(new Font("SansSerif", Font.BOLD, 14));
        boton.setBackground(colorFondo);
        boton.setForeground(Color.WHITE);
        boton.setFocusPainted(false);
        boton.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        return boton;
    }
}
