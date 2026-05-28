package aeroviajes;

import aeroviajes.model.Aeropuerto;
import aeroviajes.model.Aerolinea;
import aeroviajes.model.Usuario;
import aeroviajes.model.Vuelo;
import aeroviajes.patterns.factory.UsuarioFactory;
import aeroviajes.patterns.flyweight.AerolineaFlyweightFactory;
import aeroviajes.patterns.flyweight.AeropuertoFlyweightFactory;
import aeroviajes.persistence.RepositorioUsuarios;
import aeroviajes.persistence.RepositorioVuelos;
import aeroviajes.ui.VentanaPrincipal;
import aeroviajes.util.Constantes;
import java.time.LocalDateTime;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 * Punto de entrada del sistema Aeroviajes.
 */
public class Main {

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.err.println("Aviso: no se pudo aplicar el Look and Feel del sistema.");
        }

        poblarCacheFlyweights();
        crearAdminPorDefectoSiNoExiste();
        crearVuelosDeMuestraSiNoExisten();

        SwingUtilities.invokeLater(() -> {
            VentanaPrincipal ventana = new VentanaPrincipal();
            ventana.setVisible(true);
        });
    }

    /**
     * Registra los aeropuertos y aerolineas conocidos en el cache de flyweights.
     * Se ejecuta SIEMPRE (no solo en la primera ejecucion) para que el admin
     * pueda agregar vuelos usando esos codigos aunque ya existieran vuelos.
     */
    private static void poblarCacheFlyweights() {
        AeropuertoFlyweightFactory.obtener("MEX", "Benito Juarez",                "Ciudad de Mexico", "Mexico");
        AeropuertoFlyweightFactory.obtener("CUN", "Internacional de Cancun",      "Cancun",           "Mexico");
        AeropuertoFlyweightFactory.obtener("GDL", "Miguel Hidalgo",               "Guadalajara",      "Mexico");
        AeropuertoFlyweightFactory.obtener("MTY", "Mariano Escobedo",             "Monterrey",        "Mexico");
        AeropuertoFlyweightFactory.obtener("TIJ", "General Abelardo L. Rodriguez","Tijuana",          "Mexico");
        AeropuertoFlyweightFactory.obtener("MID", "Manuel Crescencio Rejon",      "Merida",           "Mexico");

        AerolineaFlyweightFactory.obtener("AM", "Aeromexico");
        AerolineaFlyweightFactory.obtener("Y4", "Volaris");
        AerolineaFlyweightFactory.obtener("VB", "Viva Aerobus");
    }

    private static void crearAdminPorDefectoSiNoExiste() {
        RepositorioUsuarios repo = new RepositorioUsuarios();
        if (repo.buscarPorId(Constantes.CORREO_ADMIN_DEFAULT) == null) {
            Usuario admin = UsuarioFactory.crear(
                    UsuarioFactory.Tipo.ADMINISTRADOR,
                    "A001", "Administrador", "Sistema",
                    Constantes.CORREO_ADMIN_DEFAULT,
                    Constantes.CONTRASENA_ADMIN_DEFAULT);
            repo.guardar(admin);
            System.out.println("Administrador por defecto creado: "
                    + Constantes.CORREO_ADMIN_DEFAULT + " / "
                    + Constantes.CONTRASENA_ADMIN_DEFAULT);
        }
    }

    private static void crearVuelosDeMuestraSiNoExisten() {
        RepositorioVuelos repoV = new RepositorioVuelos();
        if (!repoV.listarTodos().isEmpty()) {
            return; // ya hay vuelos persistidos, no resembrar
        }

        Aeropuerto mex = AeropuertoFlyweightFactory.obtener("MEX");
        Aeropuerto cun = AeropuertoFlyweightFactory.obtener("CUN");
        Aeropuerto gdl = AeropuertoFlyweightFactory.obtener("GDL");
        Aeropuerto mty = AeropuertoFlyweightFactory.obtener("MTY");
        Aeropuerto tij = AeropuertoFlyweightFactory.obtener("TIJ");
        Aeropuerto mid = AeropuertoFlyweightFactory.obtener("MID");

        Aerolinea amx = AerolineaFlyweightFactory.obtener("AM");
        Aerolinea vlo = AerolineaFlyweightFactory.obtener("Y4");
        Aerolinea vbz = AerolineaFlyweightFactory.obtener("VB");

        Vuelo[] vuelos = {
            new Vuelo("AM101", mex, cun, amx, LocalDateTime.of(2026, 6, 15,  8, 30), 2450.00, 150),
            new Vuelo("Y4202", mex, gdl, vlo, LocalDateTime.of(2026, 6, 16, 10, 15), 1280.00, 180),
            new Vuelo("VB303", gdl, mty, vbz, LocalDateTime.of(2026, 6, 17, 14, 45), 1650.00, 140),
            new Vuelo("AM404", mex, tij, amx, LocalDateTime.of(2026, 6, 18,  7,  0), 3200.00, 160),
            new Vuelo("Y4505", cun, mid, vlo, LocalDateTime.of(2026, 6, 19, 11, 30), 1100.00, 120),
            new Vuelo("VB606", mty, mex, vbz, LocalDateTime.of(2026, 6, 20, 16, 20), 1890.00, 150),
        };

        for (Vuelo v : vuelos) {
            repoV.guardar(v);
        }
        System.out.println("[Main] " + vuelos.length + " vuelos de muestra creados en data/vuelos.dat");
    }
}