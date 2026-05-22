package aeroviajes.exception;

/**
 * Se lanza cuando se busca un usuario por correo o ID y no se encuentra
 * en el sistema (por ejemplo, al iniciar sesión con credenciales incorrectas).
 */
public class UsuarioNoEncontrado extends Exception {

    private final String correo;

    public UsuarioNoEncontrado(String correo) {
        super("No se encontró ningún usuario con el correo: " + correo);
        this.correo = correo;
    }

    public String getCorreo() {
        return correo;
    }
}
