package aeroviajes.service;

import aeroviajes.exception.AsientoNoDisponible;
import aeroviajes.exception.Autenticacion;
import aeroviajes.exception.VueloNoDisponible;
import aeroviajes.model.Vuelo;
import java.util.List;

/**
 * Contrato que deben cumplir tanto el gestor real como el proxy.
 * Asi el proxy puede sustituir al real sin que el resto del codigo
 * note la diferencia (principio de sustitucion de Liskov).
 */
public interface IGestorVuelos {

    void agregarVuelo(Vuelo vuelo) throws Autenticacion;

    void eliminarVuelo(String idVuelo) throws Autenticacion, VueloNoDisponible;

    Vuelo buscarVuelo(String idVuelo) throws VueloNoDisponible;

    List<Vuelo> obtenerVuelosDisponibles();

    void reservarAsiento(String idVuelo) throws VueloNoDisponible, AsientoNoDisponible;
}
