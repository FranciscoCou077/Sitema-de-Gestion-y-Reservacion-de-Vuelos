package aeroviajes.util;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Genera codigos unicos para cada ticket en formato AV-YYYY-NNNNN.
 * Usa AtomicInteger para que sea seguro en entornos con multiples hilos.
 */
public class GeneradorCodigoTicket {

    private static final AtomicInteger contador = new AtomicInteger(1);

    private GeneradorCodigoTicket() {
        // Clase de utilidad, no se instancia
    }

    public static String generar() {
        int numero = contador.getAndIncrement();
        int anio = java.time.Year.now().getValue();
        return String.format("AV-%d-%05d", anio, numero);
    }
}
