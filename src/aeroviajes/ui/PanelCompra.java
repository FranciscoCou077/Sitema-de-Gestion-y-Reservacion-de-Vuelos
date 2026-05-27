package aeroviajes.ui;

import aeroviajes.exception.AsientoNoDisponible;
import aeroviajes.exception.PagoInvalido;
import aeroviajes.exception.VueloNoDisponible;
import aeroviajes.model.Reserva;
import aeroviajes.model.Usuario;
import aeroviajes.patterns.strategy.IValidadorPago;
import aeroviajes.patterns.strategy.ValidadorTarjetaCredito;
import aeroviajes.patterns.strategy.ValidadorTarjetaDebito;
import aeroviajes.service.GestorReservas;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

/**
 * Formulario de compra de boleto.
 * El cliente elige el tipo de tarjeta (credito o debito) y
 * el sistema selecciona el Strategy correcto para validar el pago.
 */
public class PanelCompra extends JPanel {

    private final VentanaPrincipal ventana;
    private final GestorReservas gestorReservas;
    private final Usuario clienteActual;

    private JRadioButton rbCredito;
    private JRadioButton rbDebito;
    private JTextField txtNumeroTarjeta;
    private JPasswordField txtCvvNip;
    private JTextField txtFechaExp;
    private JLabel lblFechaExp;
    private JButton btnConfirmar;
    private JButton btnCancelar;

    public PanelCompra(VentanaPrincipal ventana, GestorReservas gestorReservas, Usuario clienteActual) {
        this.ventana = ventana;
        this.gestorReservas = gestorReservas;
        this.clienteActual = clienteActual;
        initComponentes();
    }

    private void initComponentes() {
        setLayout(new BorderLayout(10, 10));
        add(new JLabel("Datos de pago", JLabel.CENTER), BorderLayout.NORTH);

        JPanel formulario = new JPanel(new GridLayout(6, 2, 8, 8));

        // Tipo de tarjeta
        formulario.add(new JLabel("Tipo de tarjeta:"));
        JPanel panelTipo = new JPanel();
        rbCredito = new JRadioButton("Credito", true);
        rbDebito = new JRadioButton("Debito");
        ButtonGroup grupo = new ButtonGroup();
        grupo.add(rbCredito);
        grupo.add(rbDebito);
        panelTipo.add(rbCredito);
        panelTipo.add(rbDebito);
        formulario.add(panelTipo);

        // Numero de tarjeta
        formulario.add(new JLabel("Numero de tarjeta:"));
        txtNumeroTarjeta = new JTextField();
        formulario.add(txtNumeroTarjeta);

        // CVV o NIP segun tipo
        formulario.add(new JLabel("CVV / NIP:"));
        txtCvvNip = new JPasswordField();
        formulario.add(txtCvvNip);

        // Fecha de expiracion (solo credito)
        lblFechaExp = new JLabel("Fecha de expiracion:");
        formulario.add(lblFechaExp);
        txtFechaExp = new JTextField();
        formulario.add(txtFechaExp);

        // Mostrar u ocultar campo de fecha segun tipo de tarjeta
        rbDebito.addActionListener(e -> {
            lblFechaExp.setVisible(false);
            txtFechaExp.setVisible(false);
        });
        rbCredito.addActionListener(e -> {
            lblFechaExp.setVisible(true);
            txtFechaExp.setVisible(true);
        });

        add(formulario, BorderLayout.CENTER);

        // Botones
        JPanel panelBotones = new JPanel();
        btnConfirmar = new JButton("Confirmar compra");
        btnCancelar = new JButton("Cancelar");
        btnConfirmar.addActionListener(e -> procesarCompra());
        btnCancelar.addActionListener(e -> ventana.mostrarPanel("listaVuelos"));
        panelBotones.add(btnConfirmar);
        panelBotones.add(btnCancelar);
        add(panelBotones, BorderLayout.SOUTH);
    }

    private void procesarCompra() {
        String numero = txtNumeroTarjeta.getText().trim();
        String cvvNip = new String(txtCvvNip.getPassword()).trim();
        String idVuelo = ventana.getVueloSeleccionado();

        // Strategy: el validador se elige segun lo que marco el cliente
        IValidadorPago validador;
        if (rbCredito.isSelected()) {
            validador = new ValidadorTarjetaCredito(numero, cvvNip, txtFechaExp.getText().trim());
        } else {
            validador = new ValidadorTarjetaDebito(numero, cvvNip);
        }

        try {
            Reserva reserva = gestorReservas.crearReserva(
                    clienteActual.getCorreo(), idVuelo, validador);
            JOptionPane.showMessageDialog(this,
                "Reserva confirmada. Tu ticket se esta generando...\nID reserva: "
                + reserva.getIdReserva());
            ventana.mostrarPanel("misReservas");
        } catch (PagoInvalido ex) {
            JOptionPane.showMessageDialog(this, "Error de pago: " + ex.getMessage(),
                "Pago invalido", JOptionPane.ERROR_MESSAGE);
        } catch (AsientoNoDisponible ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(),
                "Sin asientos", JOptionPane.WARNING_MESSAGE);
        } catch (VueloNoDisponible ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(),
                "Vuelo no disponible", JOptionPane.WARNING_MESSAGE);
        }
    }
}
