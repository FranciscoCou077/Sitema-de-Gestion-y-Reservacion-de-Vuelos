package aeroviajes.model;

/**
 * Usuario con privilegios administrativos. Puede gestionar los vuelos
 * y los usuarios del sistema (las operaciones reales se controlan
 * mediante el patron Proxy en la capa de servicio).
 *
 * Demuestra: herencia y polimorfismo (sobreescribe getRol()).
 *
 * @author Equipo Aeroviajes
 */
public class Administrador extends Usuario {

    private static final long serialVersionUID = 1L;

    public Administrador(String id, String nombre, String apellido,
                         String correo, String contrasena) {
        super(id, nombre, apellido, correo, contrasena);
    }

    @Override
    public String getRol() {
        return "ADMINISTRADOR";
    }
}
