package aeroviajes.exception;

/**
 * Se lanza cuando se intenta comprar un boleto en un vuelo que ya no
 * tiene asientos libres.
 */
public class AsientoNoDisponible extends Exception {

    private final String idVuelo;

    public AsientoNoDisponible(String idVuelo) {
        super("No hay asientos disponibles en el vuelo '" + idVuelo + "'.");
        this.idVuelo = idVuelo;
    }

    public String getIdVuelo() {
        return idVuelo;
    }
}
