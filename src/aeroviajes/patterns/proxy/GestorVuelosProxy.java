package aeroviajes.patterns.proxy;

import aeroviajes.exception.AsientoNoDisponible;
import aeroviajes.exception.Autenticacion;
import aeroviajes.exception.VueloNoDisponible;
import aeroviajes.model.Administrador;
import aeroviajes.model.Usuario;
import aeroviajes.model.Vuelo;
import aeroviajes.service.GestorVuelosReal;
import aeroviajes.service.IGestorVuelos;
import java.util.List;

/**
 * Protection Proxy de IGestorVuelos.
 * Intercepta las operaciones sensibles (agregar y eliminar vuelos)
 * y verifica que el usuario sea administrador antes de delegarlas
 * al GestorVuelosReal. Las operaciones de lectura las deja pasar libremente.
 */
public class GestorVuelosProxy implements IGestorVuelos {

    private final GestorVuelosReal gestorReal;
    private final Usuario usuarioActual;

    public GestorVuelosProxy(GestorVuelosReal gestorReal, Usuario usuarioActual) {
        this.gestorReal = gestorReal;
        this.usuarioActual = usuarioActual;
    }

    private void verificarAdmin() throws Autenticacion {
        if (!(usuarioActual instanceof Administrador)) {
            throw new Autenticacion("Solo un administrador puede realizar esta operacion.");
        }
    }

    @Override
    public void agregarVuelo(Vuelo vuelo) throws Autenticacion {
        verificarAdmin();
        gestorReal.agregarVuelo(vuelo);
    }

    @Override
    public void eliminarVuelo(String idVuelo) throws Autenticacion, VueloNoDisponible {
        verificarAdmin();
        gestorReal.eliminarVuelo(idVuelo);
    }

    // Las siguientes operaciones no requieren ser admin

    @Override
    public Vuelo buscarVuelo(String idVuelo) throws VueloNoDisponible {
        return gestorReal.buscarVuelo(idVuelo);
    }

    @Override
    public List<Vuelo> obtenerVuelosDisponibles() {
        return gestorReal.obtenerVuelosDisponibles();
    }

    @Override
    public void reservarAsiento(String idVuelo) throws VueloNoDisponible, AsientoNoDisponible {
        gestorReal.reservarAsiento(idVuelo);
    }
}
