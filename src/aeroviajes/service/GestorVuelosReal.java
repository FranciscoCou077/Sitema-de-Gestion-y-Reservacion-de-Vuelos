package aeroviajes.service;

import aeroviajes.exception.AsientoNoDisponible;
import aeroviajes.exception.Autenticacion;
import aeroviajes.exception.VueloNoDisponible;
import aeroviajes.model.Vuelo;
import aeroviajes.persistence.RepositorioVuelos;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Implementacion real de IGestorVuelos.
 * Mantiene un HashMap en memoria como cache (acceso O(1)) y lo respalda
 * con RepositorioVuelos para que los datos sobrevivan al cierre del programa.
 */
public class GestorVuelosReal implements IGestorVuelos {

    private final Map<String, Vuelo> vuelos;
    private final RepositorioVuelos repositorio;

    public GestorVuelosReal() {
        this.repositorio = new RepositorioVuelos();
        this.vuelos = new HashMap<>();
        // Cargar vuelos previamente persistidos al arrancar
        for (Vuelo v : repositorio.listarTodos()) {
            vuelos.put(v.getId(), v);
        }
        System.out.println("[GestorVuelos] " + vuelos.size() + " vuelos cargados de disco.");
    }

    @Override
    public void agregarVuelo(Vuelo vuelo) throws Autenticacion {
        vuelos.put(vuelo.getId(), vuelo);
        repositorio.guardar(vuelo);
        System.out.println("[GestorVuelos] Vuelo agregado: " + vuelo.getId());
    }

    @Override
    public void eliminarVuelo(String idVuelo) throws Autenticacion, VueloNoDisponible {
        if (!vuelos.containsKey(idVuelo)) {
            throw new VueloNoDisponible(idVuelo);
        }
        vuelos.remove(idVuelo);
        repositorio.eliminar(idVuelo);
        System.out.println("[GestorVuelos] Vuelo eliminado: " + idVuelo);
    }

    @Override
    public Vuelo buscarVuelo(String idVuelo) throws VueloNoDisponible {
        Vuelo v = vuelos.get(idVuelo);
        if (v == null) {
            throw new VueloNoDisponible(idVuelo);
        }
        return v;
    }

    @Override
    public List<Vuelo> obtenerVuelosDisponibles() {
        List<Vuelo> disponibles = new ArrayList<>();
        for (Vuelo v : vuelos.values()) {
            if (v.hayDisponibilidad()) {
                disponibles.add(v);
            }
        }
        return disponibles;
    }

    @Override
    public void reservarAsiento(String idVuelo) throws VueloNoDisponible, AsientoNoDisponible {
        Vuelo v = buscarVuelo(idVuelo);
        if (!v.hayDisponibilidad()) {
            throw new AsientoNoDisponible(idVuelo);
        }
        v.reservarAsiento();
        repositorio.actualizar(v); // persiste el nuevo conteo
    }

    public Map<String, Vuelo> getVuelos() {
        return vuelos;
    }
}