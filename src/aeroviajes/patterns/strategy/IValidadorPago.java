package aeroviajes.patterns.strategy;

import aeroviajes.exception.PagoInvalido;

/**
 * Interfaz del patron Strategy para validar pagos.
 * Cada implementacion define su propia logica de validacion
 * sin que GestorReservas tenga que saber cual esta usando.
 */
public interface IValidadorPago {

    /**
     * Valida los datos del metodo de pago.
     * Lanza PagoInvalido si algo esta mal.
     */
    void validar() throws PagoInvalido;
}
