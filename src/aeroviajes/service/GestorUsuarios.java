package aeroviajes.service;

import aeroviajes.exception.Autenticacion;
import aeroviajes.exception.UsuarioNoEncontrado;
import aeroviajes.exception.UsuarioYaExiste;
import aeroviajes.model.Cliente;
import aeroviajes.model.Usuario;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Maneja el registro, login y eliminacion de usuarios.
 * Usa un HashMap con el correo como clave para busquedas rapidas.
 */
public class GestorUsuarios {

    private final Map<String, Usuario> usuarios;

    public GestorUsuarios() {
        this.usuarios = new HashMap<>();
    }

    /**
     * Registra un nuevo cliente en el sistema.
     * Lanza UsuarioYaExiste si el correo ya esta en uso.
     */
    public void registrarCliente(String nombre, String apellido,
                                  String correo, String contrasena)
            throws UsuarioYaExiste {
        if (usuarios.containsKey(correo)) {
            throw new UsuarioYaExiste(correo);
        }
        Cliente cliente = new Cliente(nombre, apellido, correo, contrasena);
        usuarios.put(correo, cliente);
        System.out.println("[GestorUsuarios] Cliente registrado: " + correo);
    }

    /**
     * Valida las credenciales de un usuario.
     * Devuelve el objeto Usuario si todo es correcto.
     */
    public Usuario iniciarSesion(String correo, String contrasena)
            throws UsuarioNoEncontrado, Autenticacion {
        Usuario u = usuarios.get(correo);
        if (u == null) {
            throw new UsuarioNoEncontrado(correo);
        }
        if (!u.getContrasena().equals(contrasena)) {
            throw new Autenticacion("Contrasena incorrecta para: " + correo);
        }
        return u;
    }

    /**
     * Elimina un usuario del sistema (solo lo usa el admin).
     */
    public void eliminarUsuario(String correo) throws UsuarioNoEncontrado {
        if (!usuarios.containsKey(correo)) {
            throw new UsuarioNoEncontrado(correo);
        }
        usuarios.remove(correo);
        System.out.println("[GestorUsuarios] Usuario eliminado: " + correo);
    }

    public List<Usuario> obtenerTodosLosUsuarios() {
        return new ArrayList<>(usuarios.values());
    }

    public boolean existeUsuario(String correo) {
        return usuarios.containsKey(correo);
    }
}
