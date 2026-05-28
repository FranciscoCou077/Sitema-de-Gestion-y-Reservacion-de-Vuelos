package aeroviajes.ui;

import aeroviajes.model.Usuario;
import aeroviajes.patterns.observer.NotificadorReservas;
import aeroviajes.patterns.observer.ProductorTickets;
import aeroviajes.patterns.proxy.GestorVuelosProxy;
import aeroviajes.persistence.RepositorioUsuarios;
import aeroviajes.persistence.RepositorioVuelos;
import aeroviajes.service.GestorReservas;
import aeroviajes.service.GestorTickets;
import aeroviajes.service.GestorUsuarios;
import aeroviajes.service.GestorVuelosReal;
import aeroviajes.threads.ColaTickets;
import aeroviajes.threads.HiloAutoGuardado;
import aeroviajes.threads.HiloConsumidorTickets;
import aeroviajes.util.Constantes;
import java.awt.CardLayout;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * Ventana principal de la aplicacion Aeroviajes.
 *
 * Aloja un CardLayout con todos los paneles del sistema.
 * Tambien inicializa y conecta todos los servicios, patrones y hilos.
 *
 * @author Equipo Aeroviajes
 */
public class VentanaPrincipal extends JFrame {

    // Nombres de los paneles registrados en el CardLayout
    public static final String P_BIENVENIDA   = "bienvenida";
    public static final String P_LOGIN        = "login";
    public static final String P_REGISTRO     = "registro";
    public static final String P_CLIENTE      = "cliente";
    public static final String P_LISTA_VUELOS = "listaVuelos";
    public static final String P_DETALLE      = "detalleVuelo";
    public static final String P_COMPRA       = "compra";
    public static final String P_MIS_RESERVAS = "misReservas";
    public static final String P_TICKET       = "ticket";

    private final CardLayout cardLayout;
    private final JPanel contenedor;

    // Repositorios
    private final RepositorioUsuarios repoUsuarios;
    private final RepositorioVuelos   repoVuelos;

    // Servicios
    private final GestorVuelosReal gestorVuelosReal;
    private final GestorUsuarios   gestorUsuarios;
    private final GestorReservas   gestorReservas;
    private final GestorTickets    gestorTickets;

    // Sesion activa
    private Usuario usuarioActual;

    public VentanaPrincipal() {
        super(Constantes.TITULO_VENTANA);
        setSize(Constantes.ANCHO_VENTANA, Constantes.ALTO_VENTANA);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // ----- Repositorios -----
        this.repoUsuarios    = new RepositorioUsuarios();
        this.repoVuelos      = new RepositorioVuelos();

        // ----- Servicios -----
        this.gestorVuelosReal = new GestorVuelosReal();
        this.gestorUsuarios   = new GestorUsuarios();
        this.gestorTickets    = new GestorTickets();

        // Cargar vuelos persistidos en el gestor real
        for (aeroviajes.model.Vuelo v : repoVuelos.listarTodos()) {
            try { gestorVuelosReal.agregarVuelo(v); }
            catch (Exception e) { /* primer arranque, sin vuelos */ }
        }

        // Cargar usuarios persistidos en el gestor de usuarios
        for (aeroviajes.model.Usuario u : repoUsuarios.listarTodos()) {
            try {
                if (u instanceof aeroviajes.model.Cliente) {
                    aeroviajes.model.Cliente c = (aeroviajes.model.Cliente) u;
                    gestorUsuarios.registrarCliente(
                        c.getNombre(), c.getApellido(),
                        c.getCorreo(), "dummy"); // la contrasena ya esta guardada
                }
            } catch (Exception e) { /* usuario ya existe */ }
        }

        // ----- Patrones Observer + Productor-Consumidor -----
        ColaTickets cola = new ColaTickets();
        NotificadorReservas notificador = new NotificadorReservas();
        notificador.agregarObservador(new ProductorTickets(cola));

        // ----- GestorReservas -----
        this.gestorReservas = new GestorReservas(gestorVuelosReal, notificador);

        // ----- Hilos en segundo plano -----
        new HiloConsumidorTickets(cola, gestorTickets).start();
        new Thread(new HiloAutoGuardado(gestorUsuarios, gestorVuelosReal)).start();

        // ----- Armar el CardLayout -----
        this.cardLayout  = new CardLayout();
        this.contenedor  = new JPanel(cardLayout);

        // Paneles que dependen de otros se crean primero
        PanelTicket       panelTicket  = new PanelTicket(this);
        PanelCompra       panelCompra  = new PanelCompra(this, gestorReservas);
        PanelDetalleVuelo panelDetalle = new PanelDetalleVuelo(this, gestorVuelosReal, panelCompra);
        PanelListaVuelos  panelLista   = new PanelListaVuelos(this, gestorVuelosReal, panelDetalle);
        PanelMisReservas  panelMisRes  = new PanelMisReservas(this, gestorReservas, panelTicket);

        // Registrar todos los paneles
        contenedor.add(new PanelBienvenida(this), P_BIENVENIDA);
        contenedor.add(new PanelLogin(this),      P_LOGIN);
        contenedor.add(new PanelRegistro(this), P_REGISTRO);
        contenedor.add(new PanelCliente(this),    P_CLIENTE);
        contenedor.add(panelLista,                P_LISTA_VUELOS);
        contenedor.add(panelDetalle,              P_DETALLE);
        contenedor.add(panelCompra,               P_COMPRA);
        contenedor.add(panelMisRes,               P_MIS_RESERVAS);
        contenedor.add(panelTicket,               P_TICKET);

        add(contenedor);
        mostrarPanel(P_BIENVENIDA);
    }

    /**
     * Cambia el panel visible y dispara alMostrarse() si el panel
     * implementa PanelActualizable.
     */
    public void mostrarPanel(String nombre) {
        cardLayout.show(contenedor, nombre);
        for (java.awt.Component c : contenedor.getComponents()) {
            if (c.isVisible() && c instanceof PanelActualizable) {
                ((PanelActualizable) c).alMostrarse();
            }
        }
    }

    // ----- Accesos compartidos para los paneles -----
    public RepositorioUsuarios getRepoUsuarios()  { return repoUsuarios; }
    public GestorVuelosReal    getGestorVuelos()  { return gestorVuelosReal; }
    public GestorUsuarios      getGestorUsuarios(){ return gestorUsuarios; }
    public GestorReservas      getGestorReservas(){ return gestorReservas; }
    public Usuario             getUsuarioActual() { return usuarioActual; }
    public void                setUsuarioActual(Usuario u) { this.usuarioActual = u; }

    public void info(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje,
                Constantes.NOMBRE_APP, JOptionPane.INFORMATION_MESSAGE);
    }

    public void error(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje,
                Constantes.NOMBRE_APP, JOptionPane.ERROR_MESSAGE);
    }
}