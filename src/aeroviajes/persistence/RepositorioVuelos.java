package aeroviajes.persistence;

import aeroviajes.model.Vuelo;

/**
 * Repositorio de vuelos. Persiste la coleccion en data/vuelos.dat
 * usando el id del vuelo como identificador.
 *
 * @author Equipo Aeroviajes
 */
public class RepositorioVuelos extends RepositorioBase<Vuelo> {

    private static final String RUTA = "data/vuelos.dat";

    public RepositorioVuelos() {
        super(RUTA);
    }

    @Override
    protected String obtenerId(Vuelo vuelo) {
        return vuelo.getId();
    }
}
