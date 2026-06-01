package InterfazGrafica;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class PanelInventarioAdmin extends JPanel {

    private JTable tablaJuegos;
    private DefaultTableModel modeloTabla;

    private JComboBox<String> cbJuegos;
    private JComboBox<String> cbCopiasPrestamo;
    private JComboBox<String> cbCopiasVenta;

    private JTextField txtCantidad;
    private JComboBox<String> cbTipoCompra;

    private JButton btnActualizar;
    private JButton btnComprar;
    private JButton btnMoverVentaAPrestamo;
    private JButton btnReparar;
    private JButton btnMarcarRobado;
    private JButton btnVerHistorial;

    private JTextArea areaHistorial;

    public PanelInventarioAdmin() {
        setLayout(new BorderLayout(10, 10));
        setBackground(EstiloUI.COLOR_FONDO_BEIGE);
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel titulo = new JLabel("Gestión de Inventario de Juegos");
        titulo.setFont(EstiloUI.FUENTE_TITULO);
        titulo.setForeground(EstiloUI.COLOR_TEXTO_OSCURO);
        add(titulo, BorderLayout.NORTH);

        JPanel panelCentro = new JPanel(new GridLayout(1, 2, 15, 15));
        panelCentro.setOpaque(false);

        modeloTabla = new DefaultTableModel(
                new Object[]{"Juego", "Copias Préstamo", "Copias Venta", "Disponibles Préstamo", "Difícil"},
                0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaJuegos = new JTable(modeloTabla);
        tablaJuegos.setRowHeight(24);
        panelCentro.add(new JScrollPane(tablaJuegos));

        JPanel panelDerecho = new JPanel();
        panelDerecho.setLayout(new BoxLayout(panelDerecho, BoxLayout.Y_AXIS));
        panelDerecho.setOpaque(false);

        JPanel panelFormulario = new JPanel(new GridLayout(0, 2, 8, 8));
        panelFormulario.setOpaque(false);

        cbJuegos = new JComboBox<>();
        cbCopiasPrestamo = new JComboBox<>();
        cbCopiasVenta = new JComboBox<>();

        txtCantidad = new JTextField();
        cbTipoCompra = new JComboBox<>(new String[]{"prestamo", "venta"});

        panelFormulario.add(new JLabel("Juego:"));
        panelFormulario.add(cbJuegos);

        panelFormulario.add(new JLabel("Copia préstamo:"));
        panelFormulario.add(cbCopiasPrestamo);

        panelFormulario.add(new JLabel("Copia venta:"));
        panelFormulario.add(cbCopiasVenta);

        panelFormulario.add(new JLabel("Cantidad a comprar:"));
        panelFormulario.add(txtCantidad);

        panelFormulario.add(new JLabel("Tipo compra:"));
        panelFormulario.add(cbTipoCompra);

        panelDerecho.add(panelFormulario);
        panelDerecho.add(Box.createVerticalStrut(12));

        JPanel panelBotones = new JPanel(new GridLayout(0, 1, 8, 8));
        panelBotones.setOpaque(false);

        btnActualizar = crearBoton("Actualizar");
        btnComprar = crearBoton("Comprar/Reabastecer");
        btnMoverVentaAPrestamo = crearBoton("Mover venta a préstamo");
        btnReparar = crearBoton("Reparar copia préstamo");
        btnMarcarRobado = crearBoton("Marcar copia como robada");
        btnVerHistorial = crearBoton("Ver historial del juego");

        panelBotones.add(btnActualizar);
        panelBotones.add(btnComprar);
        panelBotones.add(btnMoverVentaAPrestamo);
        panelBotones.add(btnReparar);
        panelBotones.add(btnMarcarRobado);
        panelBotones.add(btnVerHistorial);

        panelDerecho.add(panelBotones);
        panelDerecho.add(Box.createVerticalStrut(12));

        areaHistorial = new JTextArea(10, 30);
        areaHistorial.setEditable(false);
        areaHistorial.setLineWrap(true);
        areaHistorial.setWrapStyleWord(true);
        panelDerecho.add(new JScrollPane(areaHistorial));

        panelCentro.add(panelDerecho);

        add(panelCentro, BorderLayout.CENTER);
    }

    private JButton crearBoton(String texto) {
        JButton boton = new JButton(texto);
        boton.setBackground(EstiloUI.COLOR_COMPONENTE_CAFE);
        boton.setForeground(Color.WHITE);
        boton.setFocusPainted(false);
        boton.setBorderPainted(false);
        boton.setOpaque(true);
        return boton;
    }

    public JTable getTablaJuegos() {
        return tablaJuegos;
    }

    public DefaultTableModel getModeloTabla() {
        return modeloTabla;
    }

    public JComboBox<String> getCbJuegos() {
        return cbJuegos;
    }

    public JComboBox<String> getCbCopiasPrestamo() {
        return cbCopiasPrestamo;
    }

    public JComboBox<String> getCbCopiasVenta() {
        return cbCopiasVenta;
    }

    public String getJuegoSeleccionado() {
        Object seleccionado = cbJuegos.getSelectedItem();
        return seleccionado == null ? null : seleccionado.toString();
    }

    public String getCopiaPrestamoSeleccionada() {
        Object seleccionado = cbCopiasPrestamo.getSelectedItem();
        return seleccionado == null ? null : seleccionado.toString();
    }

    public String getCopiaVentaSeleccionada() {
        Object seleccionado = cbCopiasVenta.getSelectedItem();
        return seleccionado == null ? null : seleccionado.toString();
    }

    public int getCantidad() {
        return Integer.parseInt(txtCantidad.getText().trim());
    }

    public String getTipoCompra() {
        Object seleccionado = cbTipoCompra.getSelectedItem();
        return seleccionado == null ? "prestamo" : seleccionado.toString();
    }

    public JButton getBtnActualizar() {
        return btnActualizar;
    }

    public JButton getBtnComprar() {
        return btnComprar;
    }

    public JButton getBtnMoverVentaAPrestamo() {
        return btnMoverVentaAPrestamo;
    }

    public JButton getBtnReparar() {
        return btnReparar;
    }

    public JButton getBtnMarcarRobado() {
        return btnMarcarRobado;
    }

    public JButton getBtnVerHistorial() {
        return btnVerHistorial;
    }

    public void setTextoHistorial(String texto) {
        areaHistorial.setText(texto);
    }

    public void limpiarCantidad() {
        txtCantidad.setText("");
    }
}