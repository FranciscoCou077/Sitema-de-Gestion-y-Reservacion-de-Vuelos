package aeroviajes.ui;

import aeroviajes.exception.VueloNoDisponible;
import aeroviajes.model.Vuelo;
import aeroviajes.service.IGestorVuelos;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * Muestra el detalle completo de un vuelo seleccionado antes de comprar.
 * El cliente puede revisar toda la informacion y decidir si procede
 * con la compra o regresa a la lista.
 *
 * Implementa PanelActualizable para refrescarse con el vuelo correcto
 * cada vez que se navega a esta pantalla.
 */
public class PanelDetalleVuelo extends JPanel implements PanelActualizable {

    private final VentanaPrincipal ventana;
    private final IGestorVuelos gestorVuelos;
    private final PanelCompra panelCompra;

    // ID del vuelo a mostrar, lo asigna PanelListaVuelos antes de navegar
    private String idVueloActual;

    // Etiquetas que se actualizan con los datos del vuelo
    private JLabel lblId;
    private JLabel lblAerolinea;
    private JLabel lblOrigen;
    private JLabel lblDestino;
    private JLabel lblFecha;
    private JLabel lblPrecio;
    private JLabel lblAsientos;
    private JLabel lblEstado;

    private JButton btnComprar;

    public PanelDetalleVuelo(VentanaPrincipal ventana, IGestorVuelos gestorVuelos,
                              PanelCompra panelCompra) {
        this.ventana = ventana;
        this.gestorVuelos = gestorVuelos;
        this.panelCompra = panelCompra;
        initComponentes();
    }

    private void initComponentes() {
        setLayout(new BorderLayout(10, 10));

        JLabel titulo = new JLabel("Detalle del vuelo", JLabel.CENTER);
        titulo.setFont(titulo.getFont().deriveFont(Font.BOLD, 16f));
        add(titulo, BorderLayout.NORTH);

        // Cuadricula con los datos del vuelo
        JPanel datos = new JPanel(new GridLayout(8, 2, 8, 8));
        datos.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));

        datos.add(new JLabel("ID del vuelo:"));
        lblId = new JLabel("-");
        datos.add(lblId);

        datos.add(new JLabel("Aerolinea:"));
        lblAerolinea = new JLabel("-");
        datos.add(lblAerolinea);

        datos.add(new JLabel("Origen:"));
        lblOrigen = new JLabel("-");
        datos.add(lblOrigen);

        datos.add(new JLabel("Destino:"));
        lblDestino = new JLabel("-");
        datos.add(lblDestino);

        datos.add(new JLabel("Fecha de salida:"));
        lblFecha = new JLabel("-");
        datos.add(lblFecha);

        datos.add(new JLabel("Precio:"));
        lblPrecio = new JLabel("-");
        datos.add(lblPrecio);

        datos.add(new JLabel("Asientos disponibles:"));
        lblAsientos = new JLabel("-");
        datos.add(lblAsientos);

        datos.add(new JLabel("Estado:"));
        lblEstado = new JLabel("-");
        datos.add(lblEstado);

        add(datos, BorderLayout.CENTER);

        // Botones
        JPanel panelBotones = new JPanel();
        btnComprar = new JButton("Comprar boleto");
        JButton btnRegresar = new JButton("Regresar a la lista");

        btnComprar.addActionListener(e -> irACompra());
        btnRegresar.addActionListener(e -> ventana.mostrarPanel("listaVuelos"));

        panelBotones.add(btnComprar);
        panelBotones.add(btnRegresar);
        add(panelBotones, BorderLayout.SOUTH);
    }

    /** PanelListaVuelos llama este metodo antes de navegar aqui. */
    public void setIdVuelo(String idVuelo) {
        this.idVueloActual = idVuelo;
    }

    /** Se llama automaticamente al mostrarse el panel. Carga los datos frescos. */
    @Override
    public void alMostrarse() {
        if (idVueloActual == null) return;

        try {
            Vuelo v = gestorVuelos.buscarVuelo(idVueloActual);

            lblId.setText(v.getId());
            lblAerolinea.setText(v.getAerolinea().getNombre());
            lblOrigen.setText(v.getOrigen().getCiudad()
                + " (" + v.getOrigen().getCodigoIATA() + ")");
            lblDestino.setText(v.getDestino().getCiudad()
                + " (" + v.getDestino().getCodigoIATA() + ")");
            lblFecha.setText(v.getFechaFormateada());
            lblPrecio.setText(String.format("$%.2f", v.getPrecio()));
            lblAsientos.setText(v.getAsientosDisponibles()
                + " de " + v.getAsientosTotales());

            if (v.hayDisponibilidad()) {
                lblEstado.setText("Disponible");
                btnComprar.setEnabled(true);
            } else {
                lblEstado.setText("Sin asientos");
                btnComprar.setEnabled(false);
            }

        } catch (VueloNoDisponible ex) {
            JOptionPane.showMessageDialog(this,
                "No se pudo cargar el vuelo: " + ex.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
            ventana.mostrarPanel("listaVuelos");
        }
    }

    private void irACompra() {
        panelCompra.setIdVueloSeleccionado(idVueloActual);
        ventana.mostrarPanel("compra");
    }
}
