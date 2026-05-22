package aeroviajes.exception;

/**
 * Se lanza cuando se intenta registrar un nuevo usuario con un correo
 * que ya está en uso dentro del sistema.
 */
public class UsuarioYaExiste extends Exception {

    private final String correo;

    public UsuarioYaExiste(String correo) {
        super("Ya existe un usuario registrado con el correo: " + correo);
        this.correo = correo;
    }

    public String getCorreo() {
        return correo;
    }
}
