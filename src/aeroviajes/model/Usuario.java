package aeroviajes.model;

import java.io.Serializable;
import java.util.Objects;

/**
 * Clase base abstracta para todos los usuarios del sistema Aeroviajes.
 * Define los atributos y comportamientos comunes a administradores y clientes.
 *
 * Demuestra: clase abstracta, encapsulamiento (atributos protected),
 * herencia (base de Administrador y Cliente) y serializacion.
 *
 * @author Equipo Aeroviajes
 */
public abstract class Usuario implements Serializable {

    private static final long serialVersionUID = 1L;

    protected String id;
    protected String nombre;
    protected String apellido;
    protected String correo;
    protected String contrasena;

    /**
     * Constructor protegido: solo las subclases pueden invocarlo.
     */
    protected Usuario(String id, String nombre, String apellido,
                      String correo, String contrasena) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.correo = correo;
        this.contrasena = contrasena;
    }

    /**
     * Cada subtipo de usuario define su propio rol.
     * Metodo abstracto que fuerza el polimorfismo.
     *
     * @return el rol del usuario ("ADMINISTRADOR" o "CLIENTE")
     */
    public abstract String getRol();

    /**
     * Verifica si la contrasena ingresada coincide con la del usuario.
     * No se expone la contrasena mediante getter por seguridad.
     */
    public boolean validarContrasena(String intento) {
        return this.contrasena != null && this.contrasena.equals(intento);
    }

    // ----- Getters -----
    public String getId() { return id; }
    public String getNombre() { return nombre; }
    public String getApellido() { return apellido; }
    public String getCorreo() { return correo; }
    public String getNombreCompleto() { return nombre + " " + apellido; }

    // Solo se permite cambiar la contrasena, no los datos de identidad.
    public void setContrasena(String contrasena) { this.contrasena = contrasena; }

    /**
     * Dos usuarios son iguales si comparten el mismo correo (clave natural).
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Usuario)) return false;
        Usuario usuario = (Usuario) o;
        return Objects.equals(correo, usuario.correo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(correo);
    }

    @Override
    public String toString() {
        return getRol() + " {correo=" + correo + ", nombre=" + getNombreCompleto() + "}";
    }
}
