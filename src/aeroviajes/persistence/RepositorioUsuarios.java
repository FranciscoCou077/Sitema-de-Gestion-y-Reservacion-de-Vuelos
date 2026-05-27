package aeroviajes.persistence;

import aeroviajes.model.Usuario;

/**
 * Repositorio de usuarios. Persiste la coleccion en data/usuarios.dat
 * usando el correo como identificador natural (clave unica de login).
 *
 * Demuestra: herencia (extiende RepositorioBase) y polimorfismo
 * (implementa obtenerId para el tipo Usuario).
 *
 * @author Equipo Aeroviajes
 */
public class RepositorioUsuarios extends RepositorioBase<Usuario> {

    private static final String RUTA = "data/usuarios.dat";

    public RepositorioUsuarios() {
        super(RUTA);
    }

    @Override
    protected String obtenerId(Usuario usuario) {
        return usuario.getCorreo();
    }
}
