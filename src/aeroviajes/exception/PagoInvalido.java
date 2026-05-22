package aeroviajes.exception;

/**
 * Se lanza cuando el método de pago proporcionado no pasa la validación
 * del Strategy correspondiente (tarjeta de crédito o débito).
 */
public class PagoInvalido extends Exception {

    private final String motivo;

    public PagoInvalido(String motivo) {
        super("El pago no pudo procesarse: " + motivo);
        this.motivo = motivo;
    }

    public String getMotivo() {
        return motivo;
    }
}
