package aeroviajes.ui;

import aeroviajes.model.Usuario;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

/**
 * Pantalla de inicio de sesion. Autentica al usuario contra el repositorio.
 * Tras un login correcto, el rol del usuario (Administrador o Cliente)
 * determina hacia que panel se navega.
 *
 * NOTA: Cuando este disponible el GestorUsuarios (capa de servicio), este
 * panel se refactorizara para usarlo en lugar del repositorio directamente.
 *
 * @author Equipo Aeroviajes
 */
public class PanelLogin extends JPanel {

    private final VentanaPrincipal ventana;
    private final JTextField     txtCorreo     = new JTextField(20);
    private final JPasswordField txtContrasena = new JPasswordField(20);

    public PanelLogin(VentanaPrincipal ventana) {
        this.ventana = ventana;
        setLayout(new GridBagLayout());
        setBackground(new Color(245, 247, 250));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;

        // Titulo
        JLabel titulo = new JLabel("Iniciar sesion", SwingConstants.CENTER);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 24));
        titulo.setForeground(new Color(40, 60, 110));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(30, 8, 30, 8);
        add(titulo, gbc);
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(8, 8, 8, 8);

        // Campo correo
        JLabel lblCorreo = new JLabel("Correo:");
        lblCorreo.setFont(new Font("SansSerif", Font.PLAIN, 14));
        gbc.gridx = 0; gbc.gridy = 1;
        add(lblCorreo, gbc);

        txtCorreo.setFont(new Font("SansSerif", Font.PLAIN, 14));
        txtCorreo.setPreferredSize(new Dimension(260, 30));
        gbc.gridx = 1;
        add(txtCorreo, gbc);

        // Campo contrasena
        JLabel lblPwd = new JLabel("Contrasena:");
        lblPwd.setFont(new Font("SansSerif", Font.PLAIN, 14));
        gbc.gridx = 0; gbc.gridy = 2;
        add(lblPwd, gbc);

        txtContrasena.setFont(new Font("SansSerif", Font.PLAIN, 14));
        txtContrasena.setPreferredSize(new Dimension(260, 30));
        gbc.gridx = 1;
        add(txtContrasena, gbc);

        // Botones
        JButton btnLogin = new JButton("Entrar");
        btnLogin.setBackground(new Color(40, 110, 210));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnLogin.setPreferredSize(new Dimension(140, 38));
        btnLogin.setFocusPainted(false);
        btnLogin.addActionListener(e -> autenticar());
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.insets = new Insets(20, 8, 8, 8);
        add(btnLogin, gbc);

        JButton btnVolver = new JButton("Volver");
        btnVolver.setFont(new Font("SansSerif", Font.PLAIN, 14));
        btnVolver.setPreferredSize(new Dimension(120, 38));
        btnVolver.setFocusPainted(false);
        btnVolver.addActionListener(e -> {
            limpiar();
            ventana.mostrarPanel(VentanaPrincipal.P_BIENVENIDA);
        });
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        add(btnVolver, gbc);

        // Enter dentro del campo de contrasena envia el formulario
        txtContrasena.addActionListener(e -> autenticar());
    }

    private void autenticar() {
        String correo     = txtCorreo.getText().trim().toLowerCase();
        String contrasena = new String(txtContrasena.getPassword());

        if (correo.isEmpty() || contrasena.isEmpty()) {
            ventana.error("Ingresa correo y contrasena.");
            return;
        }

        Usuario u = ventana.getRepoUsuarios().buscarPorId(correo);
        if (u == null) {
            ventana.error("No existe un usuario con ese correo.");
            return;
        }
        if (!u.validarContrasena(contrasena)) {
            ventana.error("Contrasena incorrecta.");
            return;
        }

        // Login exitoso: guardar sesion y navegar segun rol
        ventana.setUsuarioActual(u);
        limpiar();

        if ("ADMINISTRADOR".equals(u.getRol())) {
            // El panel de administracion completo depende de la capa de servicio
            // (GestorVuelosProxy de Ernesto). Por ahora mostramos un aviso y
            // regresamos a la bienvenida.
            ventana.info("Bienvenido administrador " + u.getNombreCompleto()
                    + ".\nEl panel de administracion estara disponible cuando "
                    + "se integre la capa de servicio.");
            ventana.setUsuarioActual(null);
            ventana.mostrarPanel(VentanaPrincipal.P_BIENVENIDA);
        } else {
            ventana.mostrarPanel(VentanaPrincipal.P_CLIENTE);
        }
    }

    private void limpiar() {
        txtCorreo.setText("");
        txtContrasena.setText("");
    }
}
