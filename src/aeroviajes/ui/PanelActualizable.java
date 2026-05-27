package aeroviajes.ui;

/**
 * Interfaz opcional para paneles que necesitan refrescar su contenido
 * cada vez que se muestran (por ejemplo, listar reservas del usuario
 * actual despues de un login).
 *
 * @author Equipo Aeroviajes
 */
public interface PanelActualizable {

    /** Se llama automaticamente cuando el panel se vuelve visible. */
    void alMostrarse();
}
