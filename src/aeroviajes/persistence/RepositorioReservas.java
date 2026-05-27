package aeroviajes.persistence;

import aeroviajes.model.Reserva;

/**
 * Repositorio de reservas. Persiste la coleccion en data/reservas.dat
 * usando el id de la reserva como identificador.
 *
 * @author Equipo Aeroviajes
 */
public class RepositorioReservas extends RepositorioBase<Reserva> {

    private static final String RUTA = "data/reservas.dat";

    public RepositorioReservas() {
        super(RUTA);
    }

    @Override
    protected String obtenerId(Reserva reserva) {
        return reserva.getId();
    }
}
