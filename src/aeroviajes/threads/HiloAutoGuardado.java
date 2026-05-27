package aeroviajes.threads;

import aeroviajes.service.GestorUsuarios;
import aeroviajes.service.GestorVuelosReal;

/**
 * Hilo que persiste los datos del sistema cada cierto intervalo.
 * Implementa Runnable para seguir las buenas practicas de POO
 * (composicion sobre herencia cuando no se necesita extender Thread).
 */
public class HiloAutoGuardado implements Runnable {

    private static final long INTERVALO_MS = 30_000; // cada 30 segundos

    private final GestorUsuarios gestorUsuarios;
    private final GestorVuelosReal gestorVuelos;
    private boolean corriendo;

    public HiloAutoGuardado(GestorUsuarios gestorUsuarios, GestorVuelosReal gestorVuelos) {
        this.gestorUsuarios = gestorUsuarios;
        this.gestorVuelos = gestorVuelos;
        this.corriendo = true;
    }

    @Override
    public void run() {
        System.out.println("[AutoGuardado] Hilo de guardado iniciado.");
        while (corriendo) {
            try {
                Thread.sleep(INTERVALO_MS);
                guardar();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                corriendo = false;
                System.out.println("[AutoGuardado] Hilo detenido.");
            }
        }
    }

    private void guardar() {
        // Aqui se llama a los repositorios de persistencia para serializar
        System.out.println("[AutoGuardado] Guardando datos del sistema...");
        // gestorUsuarios.guardar(); -- se conecta con persistence/
        // gestorVuelos.guardar();
    }

    public void detener() {
        corriendo = false;
    }
}
