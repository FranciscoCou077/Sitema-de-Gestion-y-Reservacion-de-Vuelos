package aeroviajes.exception;

/**
 * Se lanza cuando se intenta reservar un vuelo que ya no está disponible,
 * ya sea porque fue cancelado o porque no existe en el sistema.
 */
public class VueloNoDisponible extends Exception {

    private final String idVuelo;

    public VueloNoDisponible(String idVuelo) {
        super("El vuelo '" + idVuelo + "' no está disponible en el sistema.");
        this.idVuelo = idVuelo;
    }

    public String getIdVuelo() {
        return idVuelo;
    }
}
