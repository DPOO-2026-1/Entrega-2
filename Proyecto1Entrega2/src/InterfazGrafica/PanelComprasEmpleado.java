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
import ModuloVenta.ProductoVendible;
import ModuloVenta.Venta;
import Persistencia.GestorPersistencia;
import Usuario.Empleado;
import World.Cafeteria;
import World.Juego;

public class PanelComprasEmpleado extends JPanel {

    private Cafeteria cafeteria;
    private Empleado empleado;
    private GestorPersistencia persistencia;

    private JComboBox<ProductoOpcion> comboProducto;
    private JSpinner spinnerCantidad;
    private JTextField txtCodigoDescuento;

    private JTable tablaCarrito;
    private DefaultTableModel modeloCarrito;

    private JButton btnAgregar;
    private JButton btnFinalizar;
    private JButton btnVolver;

    private JLabel lblEstado;

    private List<ItemVenta> carrito;
    private List<ProductoOpcion> productosDisponibles;

    public PanelComprasEmpleado() {
        setLayout(null);
        setBackground(EstiloUI.COLOR_FONDO_BEIGE);

        carrito = new ArrayList<ItemVenta>();
        productosDisponibles = new ArrayList<ProductoOpcion>();

        JLabel titulo = new JLabel("  Board Game Cafe - Compras Empleado");
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
        lblProducto.setBounds(120, 145, 80, 30);
        add(lblProducto);

        comboProducto = new JComboBox<ProductoOpcion>();
        comboProducto.setBackground(EstiloUI.COLOR_COMPONENTE_CAFE);
        comboProducto.setForeground(Color.WHITE);
        comboProducto.setBounds(215, 145, 210, 30);
        add(comboProducto);

        JLabel lblCantidad = etiqueta("Cantidad");
        lblCantidad.setBounds(455, 145, 90, 30);
        add(lblCantidad);

        spinnerCantidad = new JSpinner(new SpinnerNumberModel(1, 1, 99, 1));
        spinnerCantidad.setBounds(560, 145, 90, 30);
        add(spinnerCantidad);

        JLabel lblCodigo = etiqueta("Código Descuento");
        lblCodigo.setBounds(680, 145, 150, 30);
        add(lblCodigo);

        txtCodigoDescuento = new JTextField();
        txtCodigoDescuento.setEditable(false);
        txtCodigoDescuento.setBounds(845, 145, 220, 30);
        add(txtCodigoDescuento);

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

        lblEstado = new JLabel("Estado: descuento empleado aplicado automáticamente.");
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

    public void configurarContexto(Cafeteria cafeteria, Empleado empleado, GestorPersistencia persistencia) {
        this.cafeteria = cafeteria;
        this.empleado = empleado;
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

        if (empleado != null) {
            txtCodigoDescuento.setText(empleado.getCodigoDescuento());
        }
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
        }

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
        double descuentoEstimado = subtotal * 0.20;
        double totalEstimado = subtotal - descuentoEstimado + impuestos;
        int puntos = (int) (totalEstimado * 0.01);

        modeloCarrito.addRow(new Object[] {
                opcion.nombre,
                cantidad,
                opcion.precio,
                subtotal,
                impuestos,
                descuentoEstimado,
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

            Venta venta = empleado.realizarCompra(carrito);

            descontarCopiasVendidas();

            if (persistencia != null && cafeteria != null) {
                persistencia.guardarTodo(cafeteria);
            }

            JOptionPane.showMessageDialog(
                    this,
                    "Compra finalizada.\nTotal con descuento empleado: $" + venta.getTotal(),
                    "Compra",
                    JOptionPane.INFORMATION_MESSAGE);

            carrito.clear();
            modeloCarrito.setRowCount(0);
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