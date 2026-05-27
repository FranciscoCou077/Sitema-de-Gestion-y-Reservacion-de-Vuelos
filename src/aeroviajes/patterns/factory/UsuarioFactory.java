package aeroviajes.patterns.factory;

import aeroviajes.model.Administrador;
import aeroviajes.model.Cliente;
import aeroviajes.model.Usuario;

/**
 * Implementacion del patron Factory Method para la creacion de usuarios.
 *
 * Centraliza en un solo lugar la decision de QUE subclase de {@link Usuario}
 * instanciar (Administrador o Cliente). El resto del sistema no usa
 * "new Administrador(...)" ni "new Cliente(...)" directamente, sino que pide
 * el objeto a esta fabrica indicando el tipo. Asi, si en el futuro se agrega
 * un nuevo tipo de usuario, solo se modifica esta clase.
 *
 * La contrasena se recibe como parametro: su generacion automatica (para el
 * registro de clientes) es responsabilidad de la capa de utilidades
 * (GeneradorContrasena) y de la capa de servicio, no de la fabrica.
 *
 * Demuestra: patron Factory Method, polimorfismo (devuelve el tipo base
 * Usuario) y uso de enum.
 *
 * @author Equipo Aeroviajes
 */
public class UsuarioFactory {

    /** Tipos de usuario que la fabrica sabe construir. */
    public enum Tipo {
        ADMINISTRADOR, CLIENTE
    }

    private UsuarioFactory() {
        // Clase de utilidad: no se instancia.
    }

    /**
     * Crea un usuario del tipo solicitado.
     *
     * @param tipo       el tipo de usuario a crear
     * @param id         identificador del usuario
     * @param nombre     nombre
     * @param apellido   apellido
     * @param correo     correo electronico (clave unica)
     * @param contrasena contrasena (ya generada o asignada)
     * @return una instancia de Administrador o Cliente, segun el tipo
     * @throws IllegalArgumentException si el tipo no es reconocido
     */
    public static Usuario crear(Tipo tipo, String id, String nombre,
                                String apellido, String correo, String contrasena) {
        switch (tipo) {
            case ADMINISTRADOR:
                return new Administrador(id, nombre, apellido, correo, contrasena);
            case CLIENTE:
                return new Cliente(id, nombre, apellido, correo, contrasena);
            default:
                throw new IllegalArgumentException("Tipo de usuario no soportado: " + tipo);
        }
    }
}
