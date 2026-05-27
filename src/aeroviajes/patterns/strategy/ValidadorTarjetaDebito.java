package aeroviajes.patterns.strategy;

import aeroviajes.exception.PagoInvalido;

/**
 * Valida que los datos de una tarjeta de debito sean correctos.
 * El debito solo requiere numero de tarjeta y NIP (4 digitos).
 */
public class ValidadorTarjetaDebito implements IValidadorPago {

    private final String numeroTarjeta;
    private final String nip;

    public ValidadorTarjetaDebito(String numeroTarjeta, String nip) {
        this.numeroTarjeta = numeroTarjeta;
        this.nip = nip;
    }

    @Override
    public void validar() throws PagoInvalido {
        if (numeroTarjeta == null || !numeroTarjeta.matches("\\d{16}")) {
            throw new PagoInvalido("El numero de tarjeta de debito debe tener 16 digitos.");
        }
        if (nip == null || !nip.matches("\\d{4}")) {
            throw new PagoInvalido("El NIP debe tener exactamente 4 digitos.");
        }
        System.out.println("[Pago] Tarjeta de debito validada correctamente.");
    }
}
