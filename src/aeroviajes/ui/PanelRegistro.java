package aeroviajes.ui;

import aeroviajes.model.Cliente;
import aeroviajes.model.Usuario;
import aeroviajes.patterns.factory.UsuarioFactory;
import aeroviajes.util.GeneradorContrasena;
import aeroviajes.util.ValidadorFormatos;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.UUID;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

/**
 * Pantalla de registro de nuevos clientes.
 *
 * El usuario ingresa su nombre, apellido y correo. El sistema:
 *  - valida los formatos,
 *  - genera automaticamente una contrasena segura ({@link GeneradorContrasena}),
 *  - crea el objeto Cliente mediante {@link UsuarioFactory} (patron Factory),
 *  - lo persiste mediante el repositorio,
 *  - muestra al usuario su contrasena generada y vuelve a la bienvenida.
 *
 * NOTA: Cuando este disponible el GestorUsuarios (capa de servicio), este
 * panel se refactorizara para usarlo en lugar del repositorio directamente.
 *
 * @author Equipo Aeroviajes
 */
public class PanelRegistro extends JPanel {

    private final VentanaPrincipal ventana;
    private final JTextField txtNombre   = new JTextField(20);
    private final JTextField txtApellido = new JTextField(20);
    private final JTextField txtCorreo   = new JTextField(20);

    public PanelRegistro(VentanaPrincipal ventana) {
        this.ventana = ventana;
        setLayout(new GridBagLayout());
        setBackground(new Color(245, 247, 250));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;

        // Titulo
        JLabel titulo = new JLabel("Crear cuenta nueva", SwingConstants.CENTER);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 24));
        titulo.setForeground(new Color(40, 60, 110));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(20, 8, 30, 8);
        add(titulo, gbc);
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(8, 8, 8, 8);

        // Campos
        agregarCampo(gbc, 1, "Nombre:",   txtNombre);
        agregarCampo(gbc, 2, "Apellido:", txtApellido);
        agregarCampo(gbc, 3, "Correo:",   txtCorreo);

        // Aviso de contrasena automatica
        JLabel aviso = new JLabel("<html><i>El sistema generara una contrasena segura "
                + "y se mostrara al finalizar el registro.</i></html>");
        aviso.setFont(new Font("SansSerif", Font.PLAIN, 12));
        aviso.setForeground(new Color(120, 80, 30));
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        gbc.insets = new Insets(15, 8, 15, 8);
        add(aviso, gbc);

        // Botones
        JButton btnRegistrar = new JButton("Registrarme");
        btnRegistrar.setBackground(new Color(40, 160, 100));
        btnRegistrar.setForeground(Color.WHITE);
        btnRegistrar.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnRegistrar.setPreferredSize(new Dimension(160, 38));
        btnRegistrar.setFocusPainted(false);
        btnRegistrar.addActionListener(e -> registrar());
        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.EAST;
        add(btnRegistrar, gbc);

        JButton btnVolver = new JButton("Volver");
        btnVolver.setFont(new Font("SansSerif", Font.PLAIN, 14));
        btnVolver.setPreferredSize(new Dimension(120, 38));
        btnVolver.setFocusPainted(false);
        btnVolver.addActionListener(e -> {
            limpiar();
            ventana.mostrarPanel(VentanaPrincipal.P_BIENVENIDA);
        });
        gbc.gridx = 1; gbc.gridy = 5;
        gbc.anchor = GridBagConstraints.WEST;
        add(btnVolver, gbc);
    }

    private void agregarCampo(GridBagConstraints gbc, int fila, String etiqueta, JTextField campo) {
        JLabel lbl = new JLabel(etiqueta);
        lbl.setFont(new Font("SansSerif", Font.PLAIN, 14));
        gbc.gridx = 0; gbc.gridy = fila;
        add(lbl, gbc);

        campo.setFont(new Font("SansSerif", Font.PLAIN, 14));
        campo.setPreferredSize(new Dimension(260, 30));
        gbc.gridx = 1;
        add(campo, gbc);
    }

    private void registrar() {
        String nombre   = txtNombre.getText().trim();
        String apellido = txtApellido.getText().trim();
        String correo   = txtCorreo.getText().trim().toLowerCase();

        // Validaciones de formato
        if (!ValidadorFormatos.esNombreValido(nombre)) {
            ventana.error("Nombre invalido. Use solo letras y espacios (2-50 caracteres).");
            return;
        }
        if (!ValidadorFormatos.esNombreValido(apellido)) {
            ventana.error("Apellido invalido. Use solo letras y espacios (2-50 caracteres).");
            return;
        }
        if (!ValidadorFormatos.esCorreoValido(correo)) {
            ventana.error("Correo invalido. Ejemplo: usuario@dominio.com");
            return;
        }

        // Verificar que el correo no este registrado
        if (ventana.getRepoUsuarios().buscarPorId(correo) != null) {
            ventana.error("Ya existe un usuario con el correo " + correo);
            return;
        }

        // Generar contrasena segura y crear el cliente con la fabrica
        String contrasena = GeneradorContrasena.generar();
        String id = "C" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        Usuario nuevo = UsuarioFactory.crear(
                UsuarioFactory.Tipo.CLIENTE,
                id, nombre, apellido, correo, contrasena);

        // Persistir
        ventana.getRepoUsuarios().guardar(nuevo);

        // Mostrar la contrasena generada en un dialogo claro
        JOptionPane.showMessageDialog(ventana,
                "<html><div style='font-family:SansSerif;'>"
                + "<h3>Registro exitoso</h3>"
                + "Usuario: <b>" + correo + "</b><br>"
                + "Contrasena generada: <b style='font-size:14pt;'>"
                + contrasena + "</b><br><br>"
                + "<i>Guarda tu contrasena en un lugar seguro.</i>"
                + "</div></html>",
                "Cuenta creada", JOptionPane.INFORMATION_MESSAGE);

        limpiar();
        ventana.mostrarPanel(VentanaPrincipal.P_BIENVENIDA);
    }

    private void limpiar() {
        txtNombre.setText("");
        txtApellido.setText("");
        txtCorreo.setText("");
    }
}
