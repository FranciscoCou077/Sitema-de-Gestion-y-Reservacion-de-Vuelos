package aeroviajes.ui;

import aeroviajes.model.Usuario;
import aeroviajes.persistence.RepositorioUsuarios;
import aeroviajes.util.Constantes;
import java.awt.CardLayout;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * Ventana principal de la aplicacion Aeroviajes.
 *
 * Es un {@link JFrame} que aloja un panel contenedor con {@link CardLayout}
 * para conmutar entre los diferentes paneles (bienvenida, login, registro,
 * cliente, etc.) sin abrir nuevas ventanas. Tambien guarda referencias
 * compartidas a los repositorios y al usuario que tiene la sesion activa.
 *
 * Demuestra: GUI con Swing (JFrame, CardLayout) y gestion de sesion.
 *
 * @author Equipo Aeroviajes
 */
public class VentanaPrincipal extends JFrame {

    // Nombres de los paneles registrados en el CardLayout
    public static final String P_BIENVENIDA = "bienvenida";
    public static final String P_LOGIN      = "login";
    public static final String P_REGISTRO   = "registro";
    public static final String P_CLIENTE    = "cliente";

    private final CardLayout cardLayout;
    private final JPanel contenedor;

    // Repositorio compartido (los paneles lo usan a traves de la ventana)
    private final RepositorioUsuarios repoUsuarios;

    // Sesion activa: usuario que esta logueado, o null si nadie
    private Usuario usuarioActual;

    public VentanaPrincipal() {
        // Inicializacion de la ventana
        super(Constantes.TITULO_VENTANA);
        setSize(Constantes.ANCHO_VENTANA, Constantes.ALTO_VENTANA);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Repositorios compartidos
        this.repoUsuarios = new RepositorioUsuarios();

        // Contenedor con CardLayout: permite tener varios paneles superpuestos
        // y mostrar uno a la vez.
        this.cardLayout = new CardLayout();
        this.contenedor = new JPanel(cardLayout);

        // Registramos los paneles
        contenedor.add(new PanelBienvenida(this), P_BIENVENIDA);
        contenedor.add(new PanelLogin(this),      P_LOGIN);
        contenedor.add(new PanelRegistro(this),   P_REGISTRO);
        contenedor.add(new PanelCliente(this),    P_CLIENTE);

        add(contenedor);

        // Empezamos en la bienvenida
        mostrarPanel(P_BIENVENIDA);
    }

    /**
     * Cambia el panel visible. Los paneles llaman a este metodo para
     * navegar entre pantallas.
     */
    public void mostrarPanel(String nombre) {
        cardLayout.show(contenedor, nombre);
        // Si el panel destino tiene una accion al mostrarse, la disparamos
        for (java.awt.Component c : contenedor.getComponents()) {
            if (c.isVisible() && c instanceof PanelActualizable) {
                ((PanelActualizable) c).alMostrarse();
            }
        }
    }

    // ----- Accesos compartidos para los paneles -----
    public RepositorioUsuarios getRepoUsuarios() { return repoUsuarios; }
    public Usuario getUsuarioActual()            { return usuarioActual; }
    public void setUsuarioActual(Usuario u)      { this.usuarioActual = u; }

    /**
     * Muestra un dialogo informativo simple.
     */
    public void info(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje,
                Constantes.NOMBRE_APP, JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Muestra un dialogo de error.
     */
    public void error(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje,
                Constantes.NOMBRE_APP, JOptionPane.ERROR_MESSAGE);
    }
}
