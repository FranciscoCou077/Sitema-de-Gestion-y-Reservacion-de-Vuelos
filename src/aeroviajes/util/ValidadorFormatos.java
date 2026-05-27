package aeroviajes.util;

import java.util.regex.Pattern;

/**
 * Validadores de formato para los campos de entrada de la GUI.
 *
 * Centraliza expresiones regulares y reglas de validacion (correo, nombre,
 * codigo IATA, etc.) para mantener consistencia en todo el sistema.
 *
 * @author Equipo Aeroviajes
 */
public final class ValidadorFormatos {

    // Patron de correo razonable: algo@algo.algo
    private static final Pattern PATRON_CORREO =
            Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    // Nombres y apellidos: solo letras (con acentos) y espacios, 2-50 chars
    private static final Pattern PATRON_NOMBRE =
            Pattern.compile("^[A-Za-zAEIOUNaeiounUuAEIOUNaeioun ]{2,50}$");

    // Codigo IATA: exactamente 3 letras mayusculas
    private static final Pattern PATRON_CODIGO_IATA =
            Pattern.compile("^[A-Z]{3}$");

    private ValidadorFormatos() {
        // No se instancia.
    }

    /** True si el correo tiene formato valido. */
    public static boolean esCorreoValido(String correo) {
        return correo != null && PATRON_CORREO.matcher(correo.trim()).matches();
    }

    /** True si el nombre o apellido tiene formato valido (solo letras y espacios). */
    public static boolean esNombreValido(String nombre) {
        return nombre != null && PATRON_NOMBRE.matcher(nombre.trim()).matches();
    }

    /** True si el codigo IATA tiene 3 letras mayusculas. */
    public static boolean esCodigoIATAValido(String codigo) {
        return codigo != null && PATRON_CODIGO_IATA.matcher(codigo.trim()).matches();
    }

    /** True si la cadena no es null y no esta vacia (despues de trim). */
    public static boolean noEstaVacio(String texto) {
        return texto != null && !texto.trim().isEmpty();
    }
}
