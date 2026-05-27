package aeroviajes.util;

import java.security.SecureRandom;

/**
 * Generador de contrasenas seguras aleatorias.
 *
 * El proyecto Aeroviajes especifica que al registrarse, el sistema debe
 * generar automaticamente una contrasena segura para el usuario. Esta clase
 * implementa esa generacion combinando mayusculas, minusculas, digitos y
 * simbolos, usando {@link SecureRandom} (mas robusto que Random comun).
 *
 * Garantiza que la contrasena tenga al menos un caracter de cada categoria.
 *
 * @author Equipo Aeroviajes
 */
public final class GeneradorContrasena {

    private static final String MAYUSCULAS = "ABCDEFGHJKLMNPQRSTUVWXYZ";   // sin I, O
    private static final String MINUSCULAS = "abcdefghijkmnpqrstuvwxyz";   // sin l, o
    private static final String DIGITOS    = "23456789";                   // sin 0, 1
    private static final String SIMBOLOS   = "!@#$%&*?";

    private static final SecureRandom random = new SecureRandom();

    private GeneradorContrasena() {
        // No se instancia.
    }

    /**
     * Genera una contrasena segura con la longitud por defecto definida
     * en {@link Constantes#LONGITUD_CONTRASENA_AUTO}.
     */
    public static String generar() {
        return generar(Constantes.LONGITUD_CONTRASENA_AUTO);
    }

    /**
     * Genera una contrasena segura de la longitud indicada (minimo 4).
     * Incluye al menos una mayuscula, una minuscula, un digito y un simbolo.
     */
    public static String generar(int longitud) {
        if (longitud < 4) {
            longitud = 4;
        }

        // Garantizamos al menos uno de cada categoria
        char[] resultado = new char[longitud];
        resultado[0] = MAYUSCULAS.charAt(random.nextInt(MAYUSCULAS.length()));
        resultado[1] = MINUSCULAS.charAt(random.nextInt(MINUSCULAS.length()));
        resultado[2] = DIGITOS.charAt(random.nextInt(DIGITOS.length()));
        resultado[3] = SIMBOLOS.charAt(random.nextInt(SIMBOLOS.length()));

        // El resto se completa con cualquier caracter del conjunto total
        String todos = MAYUSCULAS + MINUSCULAS + DIGITOS + SIMBOLOS;
        for (int i = 4; i < longitud; i++) {
            resultado[i] = todos.charAt(random.nextInt(todos.length()));
        }

        // Mezclamos para que las primeras 4 posiciones no sean siempre
        // del mismo tipo (algoritmo Fisher-Yates)
        for (int i = resultado.length - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            char tmp = resultado[i];
            resultado[i] = resultado[j];
            resultado[j] = tmp;
        }

        return new String(resultado);
    }
}
