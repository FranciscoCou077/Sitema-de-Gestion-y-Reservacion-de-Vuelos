package aeroviajes.service;

import aeroviajes.exception.AsientoNoDisponible;
import aeroviajes.exception.Autenticacion;
import aeroviajes.exception.VueloNoDisponible;
import aeroviajes.model.Vuelo;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Implementacion real de IGestorVuelos.
 * Almacena los vuelos en un HashMap donde la clave es el ID del vuelo,
 * lo que permite busquedas en O(1).
 */
public class GestorVuelosReal implements IGestorVuelos {

    private final Map<String, Vuelo> vuelos;

    public GestorVuelosReal() {
        this.vuelos = new HashMap<>();
    }

    @Override
    public void agregarVuelo(Vuelo vuelo) throws Autenticacion {
        vuelos.put(vuelo.getId(), vuelo);
        System.out.println("[GestorVuelos] Vuelo agregado: " + vuelo.getId());
    }

    @Override
    public void eliminarVuelo(String idVuelo) throws Autenticacion, VueloNoDisponible {
        if (!vuelos.containsKey(idVuelo)) {
            throw new VueloNoDisponible(idVuelo);
        }
        vuelos.remove(idVuelo);
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
        v.reservarAsiento(); // El metodo de Vuelo ya decrementa internamente
    }

    public Map<String, Vuelo> getVuelos() {
        return vuelos;
    }
}