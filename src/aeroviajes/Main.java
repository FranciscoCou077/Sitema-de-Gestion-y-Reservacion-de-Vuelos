package aeroviajes;

import aeroviajes.model.Administrador;
import aeroviajes.model.Usuario;
import aeroviajes.patterns.factory.UsuarioFactory;
import aeroviajes.persistence.RepositorioUsuarios;
import aeroviajes.ui.VentanaPrincipal;
import aeroviajes.util.Constantes;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 * Punto de entrada del sistema Aeroviajes.
 *
 * Realiza una inicializacion minima antes de lanzar la interfaz grafica:
 *  1. Configura el Look and Feel del sistema operativo (apariencia nativa).
 *  2. Si no existe ningun administrador en el repositorio, crea uno por
 *     defecto con las credenciales definidas en Constantes (para que el
 *     sistema sea utilizable en su primera ejecucion).
 *  3. Lanza la VentanaPrincipal en el hilo de eventos de Swing (EDT).
 *
 * @author Equipo Aeroviajes
 */
public class Main {

    public static void main(String[] args) {
        // 1) Look and Feel del sistema (apariencia nativa de Windows / macOS / Linux)
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            // Si falla, se usa el L&F por defecto. No es critico.
            System.err.println("Aviso: no se pudo aplicar el Look and Feel del sistema.");
        }

        // 2) Asegurar que existe un administrador por defecto
        crearAdminPorDefectoSiNoExiste();

        // 3) Lanzar la GUI en el hilo de eventos de Swing
        SwingUtilities.invokeLater(() -> {
            VentanaPrincipal ventana = new VentanaPrincipal();
            ventana.setVisible(true);
        });
    }

    /**
     * Si el repositorio no tiene ningun usuario, crea un Administrador por
     * defecto. Esto garantiza que el sistema sea utilizable la primera vez
     * que se ejecuta (de otro modo, no habria forma de acceder como admin).
     */
    private static void crearAdminPorDefectoSiNoExiste() {
        RepositorioUsuarios repo = new RepositorioUsuarios();
        if (repo.buscarPorId(Constantes.CORREO_ADMIN_DEFAULT) == null) {
            Usuario admin = UsuarioFactory.crear(
                    UsuarioFactory.Tipo.ADMINISTRADOR,
                    "A001",
                    "Administrador",
                    "Sistema",
                    Constantes.CORREO_ADMIN_DEFAULT,
                    Constantes.CONTRASENA_ADMIN_DEFAULT);
            repo.guardar(admin);
            System.out.println("Administrador por defecto creado: "
                    + Constantes.CORREO_ADMIN_DEFAULT
                    + " / " + Constantes.CONTRASENA_ADMIN_DEFAULT);
        }
    }
}
