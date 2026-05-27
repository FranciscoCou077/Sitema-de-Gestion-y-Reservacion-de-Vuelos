package aeroviajes.patterns.strategy;

import aeroviajes.exception.PagoInvalido;

/**
 * Valida que los datos de una tarjeta de credito sean correctos.
 * Revisa que el numero tenga 16 digitos y que el CVV tenga 3.
 */
public class ValidadorTarjetaCredito implements IValidadorPago {

    private final String numeroTarjeta;
    private final String cvv;
    private final String fechaExpiracion;

    public ValidadorTarjetaCredito(String numeroTarjeta, String cvv, String fechaExpiracion) {
        this.numeroTarjeta = numeroTarjeta;
        this.cvv = cvv;
        this.fechaExpiracion = fechaExpiracion;
    }

    @Override
    public void validar() throws PagoInvalido {
        if (numeroTarjeta == null || !numeroTarjeta.matches("\\d{16}")) {
            throw new PagoInvalido("El numero de tarjeta de credito debe tener 16 digitos.");
        }
        if (cvv == null || !cvv.matches("\\d{3}")) {
            throw new PagoInvalido("El CVV debe tener exactamente 3 digitos.");
        }
        if (fechaExpiracion == null || fechaExpiracion.trim().isEmpty()) {
            throw new PagoInvalido("La fecha de expiracion no puede estar vacia.");
        }
        System.out.println("[Pago] Tarjeta de credito validada correctamente.");
    }
}
