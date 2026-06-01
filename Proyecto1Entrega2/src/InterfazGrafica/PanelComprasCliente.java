package InterfazGrafica;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.table.DefaultTableModel;

import ModuloVenta.Bebida;
import ModuloVenta.CopiaVenta;
import ModuloVenta.ItemVenta;
import ModuloVenta.Pasteleria;
import ModuloVenta.ProductoComestible;
import ModuloVenta.ProductoVendible;
import ModuloVenta.Venta;
import Persistencia.GestorPersistencia;
import Usuario.Cliente;
import World.Cafeteria;
import World.Juego;

public class PanelComprasCliente extends JPanel {

    private Cafeteria cafeteria;
    private Cliente cliente;
    private GestorPersistencia persistencia;

    private JComboBox<ProductoOpcion> comboProducto;
    private JSpinner spinnerCantidad;
    private JTextField txtCodigoDescuento;
    private JTextField txtPuntosRedimir;

    private JTable tablaCarrito;
    private DefaultTableModel modeloCarrito;

    private JButton btnAgregar;
    private JButton btnFinalizar;
    private JButton btnVolver;

    private JLabel lblEstado;

    private List<ItemVenta> carrito;
    private List<ProductoOpcion> productosDisponibles;

    public PanelComprasCliente() {
        setLayout(null);
        setBackground(EstiloUI.COLOR_FONDO_BEIGE);

        carrito = new ArrayList<ItemVenta>();
        productosDisponibles = new ArrayList<ProductoOpcion>();

        JLabel titulo = new JLabel("  Board Game Cafe - Compras Cliente");
        titulo.setOpaque(true);
        titulo.setBackground(EstiloUI.COLOR_BANNER_CAFE);
        titulo.setForeground(Color.WHITE);
        titulo.setFont(new Font("Arial", Font.BOLD, 24));
        titulo.setBounds(0, 0, 1280, 60);
        add(titulo);

        JLabel lblCompra = seccion("Compra");
        lblCompra.setBounds(75, 90, 150, 35);
        add(lblCompra);

        JLabel lblProducto = etiqueta("Producto");
        lblProducto.setBounds(40, 145, 80, 30);
        add(lblProducto);

        comboProducto = new JComboBox<ProductoOpcion>();
        comboProducto.setBackground(EstiloUI.COLOR_COMPONENTE_CAFE);
        comboProducto.setForeground(Color.WHITE);
        comboProducto.setBounds(135, 145, 210, 30);
        add(comboProducto);

        JLabel lblCantidad = etiqueta("Cantidad");
        lblCantidad.setBounds(370, 145, 90, 30);
        add(lblCantidad);

        spinnerCantidad = new JSpinner(new SpinnerNumberModel(1, 1, 99, 1));
        spinnerCantidad.setBounds(475, 145, 90, 30);
        add(spinnerCantidad);

        JLabel lblCodigo = etiqueta("Código Descuento");
        lblCodigo.setBounds(590, 145, 140, 30);
        add(lblCodigo);

        txtCodigoDescuento = new JTextField();
        txtCodigoDescuento.setBounds(745, 145, 180, 30);
        add(txtCodigoDescuento);

        JLabel lblPuntos = etiqueta("Puntos a redimir");
        lblPuntos.setBounds(950, 145, 140, 30);
        add(lblPuntos);

        txtPuntosRedimir = new JTextField("0");
        txtPuntosRedimir.setBounds(1105, 145, 90, 30);
        add(txtPuntosRedimir);

        btnAgregar = boton("Agregar Item");
        btnAgregar.setBounds(520, 205, 180, 45);
        add(btnAgregar);

        JLabel lblCarrito = seccion("Carrito");
        lblCarrito.setBounds(180, 280, 170, 35);
        add(lblCarrito);

        modeloCarrito = new DefaultTableModel(
                new Object[] {
                        "Producto",
                        "Cantidad",
                        "Precio Unitario",
                        "Subtotal",
                        "Impuestos",
                        "Descuento",
                        "Total",
                        "Puntos Ganados"
                },
                0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaCarrito = tabla(modeloCarrito);

        JScrollPane scroll = new JScrollPane(tablaCarrito);
        scroll.setBounds(140, 335, 920, 205);
        add(scroll);

        btnVolver = boton("Volver");
        btnVolver.setBounds(20, 620, 110, 35);
        add(btnVolver);

        btnFinalizar = boton("Finalizar compra");
        btnFinalizar.setBounds(520, 595, 180, 45);
        add(btnFinalizar);

        lblEstado = new JLabel("Estado: ...");
        lblEstado.setOpaque(true);
        lblEstado.setForeground(Color.WHITE);
        lblEstado.setBackground(EstiloUI.COLOR_COMPONENTE_CAFE);
        lblEstado.setBounds(740, 595, 420, 45);
        add(lblEstado);

        btnAgregar.addActionListener(e -> agregarItem());
        btnFinalizar.addActionListener(e -> finalizarCompra());
    }

    private JLabel seccion(String texto) {
        JLabel label = new JLabel(texto, JLabel.CENTER);
        label.setOpaque(true);
        label.setBackground(EstiloUI.COLOR_RECTANGULO_TEXTO);
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Arial", Font.BOLD, 14));
        return label;
    }

    private JLabel etiqueta(String texto) {
        JLabel label = seccion(texto);
        label.setFont(new Font("Arial", Font.BOLD, 12));
        return label;
    }

    private JButton boton(String texto) {
        JButton boton = new JButton(texto);
        boton.setBackground(EstiloUI.COLOR_COMPONENTE_CAFE);
        boton.setForeground(Color.WHITE);
        boton.setFocusPainted(false);
        boton.setBorder(BorderFactory.createEmptyBorder());
        return boton;
    }

    private JTable tabla(DefaultTableModel modelo) {
        JTable tabla = new JTable(modelo);
        tabla.setBackground(EstiloUI.COLOR_COMPONENTE_CAFE);
        tabla.setForeground(Color.WHITE);
        tabla.setGridColor(EstiloUI.COLOR_TEXTO_OSCURO);
        tabla.setRowHeight(35);
        tabla.getTableHeader().setBackground(EstiloUI.COLOR_RECTANGULO_TEXTO);
        tabla.getTableHeader().setForeground(Color.WHITE);
        return tabla;
    }

    public void configurarContexto(Cafeteria cafeteria, Cliente cliente, GestorPersistencia persistencia) {
        this.cafeteria = cafeteria;
        this.cliente = cliente;
        this.persistencia = persistencia;
        refrescar();
    }

    public void setAccionVolver(Runnable accionVolver) {
        for (java.awt.event.ActionListener listener : btnVolver.getActionListeners()) {
            btnVolver.removeActionListener(listener);
        }

        btnVolver.addActionListener(e -> accionVolver.run());
    }

    public void refrescar() {
        cargarProductos();
        comboProducto.removeAllItems();

        for (ProductoOpcion opcion : productosDisponibles) {
            comboProducto.addItem(opcion);
        }

        lblEstado.setText("Estado: puntos disponibles: " + (cliente == null ? 0 : cliente.getPuntosFidelidad()));
    }

    private void cargarProductos() {
        productosDisponibles.clear();

        if (cafeteria != null) {
            for (Juego juego : cafeteria.getJuegos()) {
                for (CopiaVenta copia : juego.getCopiasParaVenta()) {
                    productosDisponibles.add(
                            new ProductoOpcion(
                                    juego.getNombre() + " - Juego",
                                    copia,
                                    copia.getPrecioVenta(),
                                    juego,
                                    copia));
                }
            }

            // CAMBIO NUEVO PROYECTO 3 - COMPRAS USAN EL MENÚ REAL
            // Los productos agregados por el administrador aparecen
            // aquí además de los productos quemados de respaldo.
            if (cafeteria.getMenuCafeteria() != null) {
                for (ProductoComestible producto : cafeteria.getMenuCafeteria()) {
                    if (producto != null) {
                        String tipo = producto instanceof Bebida ? "Bebida" : "Pastelería";

                        productosDisponibles.add(
                                new ProductoOpcion(
                                        producto.getNombre() + " - " + tipo + " (Menú)",
                                        producto,
                                        producto.getPrecioBase(),
                                        null,
                                        null));
                    }
                }
            }
        }

        // Productos quemados de respaldo. Se mantienen para que la app opere
        // incluso si el admin no ha agregado productos.
        productosDisponibles.add(new ProductoOpcion("Café - Bebida", new Bebida("Café", 6000, true, false), 6000, null, null));
        productosDisponibles.add(new ProductoOpcion("Gaseosa - Bebida", new Bebida("Gaseosa", 5000, false, false), 5000, null, null));
        productosDisponibles.add(new ProductoOpcion("Brownie - Pastelería", new Pasteleria("Brownie", 8000, new ArrayList<String>()), 8000, null, null));
    }

    private void agregarItem() {
        ProductoOpcion opcion = (ProductoOpcion) comboProducto.getSelectedItem();

        if (opcion == null) {
            JOptionPane.showMessageDialog(this, "No hay producto seleccionado.");
            return;
        }

        int cantidad = (Integer) spinnerCantidad.getValue();

        if (opcion.copiaVenta != null && cantidad > 1) {
            JOptionPane.showMessageDialog(this, "Cada copia de juego se agrega de a una unidad.");
            cantidad = 1;
            spinnerCantidad.setValue(1);
        }

        ItemVenta item = new ItemVenta(opcion.producto, cantidad, opcion.precio);
        carrito.add(item);

        double subtotal = item.getSubtotalItem();
        double impuestos = item.calcularImpuestoItem();
        double totalEstimado = subtotal + impuestos;
        int puntos = (int) (totalEstimado * 0.01);

        modeloCarrito.addRow(new Object[] {
                opcion.nombre,
                cantidad,
                opcion.precio,
                subtotal,
                impuestos,
                "-",
                totalEstimado,
                puntos
        });

        lblEstado.setText("Estado: item agregado al carrito.");
    }

    private void finalizarCompra() {
        try {
            if (carrito.isEmpty()) {
                throw new IllegalStateException("El carrito está vacío.");
            }

            String codigo = txtCodigoDescuento.getText().trim();
            int puntos = parseEntero(txtPuntosRedimir.getText().trim());

            Venta venta = cliente.realizarCompra(
                    carrito.toArray(new ItemVenta[0]),
                    codigo,
                    puntos);

            descontarCopiasVendidas();

            if (persistencia != null && cafeteria != null) {
                persistencia.guardarTodo(cafeteria);
            }

            JOptionPane.showMessageDialog(
                    this,
                    "Compra finalizada.\nTotal: $" + venta.getTotal()
                            + "\nPuntos ganados: " + venta.getPuntosGenerados(),
                    "Compra",
                    JOptionPane.INFORMATION_MESSAGE);

            carrito.clear();
            modeloCarrito.setRowCount(0);
            txtCodigoDescuento.setText("");
            txtPuntosRedimir.setText("0");
            refrescar();

        } catch (Exception ex) {
            lblEstado.setText("Estado: " + ex.getMessage());

            JOptionPane.showMessageDialog(
                    this,
                    ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private int parseEntero(String texto) {
        if (texto == null || texto.isEmpty()) {
            return 0;
        }

        try {
            return Integer.parseInt(texto);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Los puntos a redimir deben ser un número entero.");
        }
    }

    private void descontarCopiasVendidas() {
        for (ItemVenta item : carrito) {
            ProductoVendible producto = item.getProducto();

            if (producto instanceof CopiaVenta && cafeteria != null) {
                CopiaVenta copiaVendida = (CopiaVenta) producto;

                for (Juego juego : cafeteria.getJuegos()) {
                    juego.getCopiasParaVenta().remove(copiaVendida);
                }
            }
        }
    }

    private static class ProductoOpcion {
        private String nombre;
        private ProductoVendible producto;
        private double precio;
        private Juego juegoAsociado;
        private CopiaVenta copiaVenta;

        public ProductoOpcion(String nombre, ProductoVendible producto, double precio, Juego juegoAsociado, CopiaVenta copiaVenta) {
            this.nombre = nombre;
            this.producto = producto;
            this.precio = precio;
            this.juegoAsociado = juegoAsociado;
            this.copiaVenta = copiaVenta;
        }

        @Override
        public String toString() {
            return nombre;
        }
    }
}