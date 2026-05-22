package aeroviajes;

import aeroviajes.model.Aerolinea;
import aeroviajes.model.Aeropuerto;
import aeroviajes.model.Cliente;
import aeroviajes.model.Reserva;
import aeroviajes.model.Ticket;
import aeroviajes.model.Vuelo;
import java.time.LocalDateTime;

/**
 * Punto de entrada del sistema Aeroviajes.
 *
 * Por ahora ejecuta una prueba rapida (smoke test) del paquete model
 * para verificar que las entidades se construyen y relacionan correctamente.
 * Se sustituira por el lanzamiento de la interfaz grafica (VentanaPrincipal)
 * cuando este lista.
 *
 * @author Equipo Aeroviajes
 */
public class Main {

    public static void main(String[] args) {
        System.out.println("=== Smoke test del paquete model ===\n");

        // Flyweights: aeropuertos y aerolinea compartidos
        Aeropuerto mex = new Aeropuerto("MEX", "Benito Juarez", "Ciudad de Mexico", "Mexico");
        Aeropuerto cun = new Aeropuerto("CUN", "Internacional de Cancun", "Cancun", "Mexico");
        Aerolinea amx = new Aerolinea("AM", "Aeromexico");

        // Vuelo con estado extrinseco propio
        Vuelo vuelo = new Vuelo("AM123", mex, cun, amx,
                LocalDateTime.of(2026, 6, 15, 8, 30), 2450.00, 150);

        // Cliente
        Cliente cliente = new Cliente("C001", "Francisco", "Cou",
                "francisco@correo.com", "Segura123");

        System.out.println(vuelo);
        System.out.println(cliente);
        System.out.println();

        try {
            // Flujo basico de reserva
            vuelo.reservarAsiento();
            Reserva reserva = new Reserva("R001", vuelo, cliente, "12A");
            reserva.confirmar();
            cliente.agregarReserva(reserva);

            // Generacion del ticket
            Ticket ticket = new Ticket("TICKET-0001", reserva);
            System.out.println(ticket.generarContenido());

            System.out.println("Asientos disponibles tras la reserva: "
                    + vuelo.getAsientosDisponibles());
            System.out.println("Reservas del cliente: " + cliente.getCantidadReservas());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

        System.out.println("\n=== Smoke test finalizado correctamente ===");
    }
}
