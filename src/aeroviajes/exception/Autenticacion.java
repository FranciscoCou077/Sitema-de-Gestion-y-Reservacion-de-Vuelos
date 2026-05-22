package aeroviajes.exception;

/**
 * Se lanza cuando las credenciales de inicio de sesión son incorrectas
 * (contraseña no coincide) o cuando un usuario sin permisos intenta
 * acceder a una operación restringida.
 */
public class Autenticacion extends Exception {

    public Autenticacion(String mensaje) {
        super(mensaje);
    }
}
