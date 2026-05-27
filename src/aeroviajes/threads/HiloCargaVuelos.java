package aeroviajes.threads;

import aeroviajes.model.Vuelo;
import aeroviajes.service.GestorVuelosReal;
import java.util.List;
import javax.swing.SwingWorker;

/**
 * Carga el catalogo de vuelos en segundo plano usando SwingWorker.
 * Asi la GUI no se congela mientras se leen los datos del archivo.
 * Extiende SwingWorker<List<Vuelo>, Vuelo>:
 *   - List<Vuelo> es el resultado final
 *   - Vuelo es el tipo de dato publicado mientras carga (para actualizar la barra de progreso)
 */
public class HiloCargaVuelos extends SwingWorker<List<Vuelo>, Vuelo> {

    private final GestorVuelosReal gestorVuelos;

    public HiloCargaVuelos(GestorVuelosReal gestorVuelos) {
        this.gestorVuelos = gestorVuelos;
    }

    /**
     * Se ejecuta en el hilo de fondo (NO en el EDT de Swing).
     * Aqui va la lectura del archivo o base de datos.
     */
    @Override
    protected List<Vuelo> doInBackground() throws Exception {
        System.out.println("[HiloCargaVuelos] Cargando vuelos...");
        // Simulacion de carga: en la version final se lee desde el archivo .dat
        Thread.sleep(500);
        return gestorVuelos.obtenerVuelosDisponibles();
    }

    /**
     * Se ejecuta en el EDT de Swing cuando doInBackground() termina.
     * Aqui se actualiza la GUI con los resultados.
     */
    @Override
    protected void done() {
        try {
            List<Vuelo> vuelos = get();
            System.out.println("[HiloCargaVuelos] Carga completada. Vuelos: " + vuelos.size());
            // Aqui se notifica al panel para que actualice su tabla
        } catch (Exception e) {
            System.err.println("[HiloCargaVuelos] Error al cargar vuelos: " + e.getMessage());
        }
    }
}
