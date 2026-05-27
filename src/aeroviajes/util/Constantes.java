package aeroviajes.util;

/**
 * Constantes utilizadas en todo el sistema Aeroviajes.
 * Centralizar los valores literales en una sola clase facilita su mantenimiento
 * y evita "numeros magicos" o cadenas repetidas en el codigo.
 *
 * @author Equipo Aeroviajes
 */
public final class Constantes {

    private Constantes() {
        // No se instancia.
    }

    // ----- Rutas de archivos de persistencia -----
    public static final String ARCHIVO_USUARIOS  = "data/usuarios.dat";
    public static final String ARCHIVO_VUELOS    = "data/vuelos.dat";
    public static final String ARCHIVO_RESERVAS  = "data/reservas.dat";
    public static final String DIR_TICKETS       = "data/tickets/";

    // ----- Aplicacion -----
    public static final String NOMBRE_APP = "Aeroviajes";
    public static final String VERSION    = "1.0";
    public static final String TITULO_VENTANA =
            "Aeroviajes - Sistema de gestion y reservacion de vuelos";

    // ----- Ventana principal -----
    public static final int ANCHO_VENTANA = 900;
    public static final int ALTO_VENTANA  = 600;

    // ----- Generacion de contrasena segura -----
    public static final int LONGITUD_CONTRASENA_AUTO = 10;

    // ----- Administrador por defecto (1ra ejecucion) -----
    public static final String CORREO_ADMIN_DEFAULT     = "admin@aeroviajes.com";
    public static final String CONTRASENA_ADMIN_DEFAULT = "Admin123!";
}
