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
import Usuario.Empleado;
import Usuario.Mesero;
import World.Cafeteria;
import World.Juego;

public class PanelCajaMesero extends JPanel {

    private Cafeteria cafeteria;
    private Empleado empleado;
    private GestorPersistencia persistencia;

    private JComboBox<ProductoOpcion> comboProducto;
    private JSpinner spinnerCantidad;
    private JTable tablaCarrito;
    private DefaultTableModel modeloCarrito;
    private JButton btnAgregar;
    private JButton btnRegistrarVenta;
    private JButton btnVolver;
    private JLabel lblEstado;

    private List<ItemVenta> carrito;
    private List<ProductoOpcion> productosDisponibles;

    public PanelCajaMesero() {
        setLayout(null);
        setBackground(EstiloUI.COLOR_FONDO_BEIGE);

        carrito = new ArrayList<ItemVenta>();
        productosDisponibles = new ArrayList<ProductoOpcion>();

        JLabel titulo = new JLabel("  Board Game Cafe - Caja Mesero");
        titulo.setOpaque(true);
        titulo.setBackground(EstiloUI.COLOR_BANNER_CAFE);
        titulo.setForeground(Color.WHITE);
        titulo.setFont(new Font("Arial", Font.BOLD, 24));
        titulo.setBounds(0, 0, 1280, 60);
        add(titulo);

        JLabel lblProducto = etiqueta("Producto");
        lblProducto.setBounds(170, 130, 100, 30);
        add(lblProducto);

        comboProducto = new JComboBox<ProductoOpcion>();
        comboProducto.setBackground(EstiloUI.COLOR_COMPONENTE_CAFE);
        comboProducto.setForeground(Color.WHITE);
        comboProducto.setBounds(285, 130, 330, 30);
        add(comboProducto);

        JLabel lblCantidad = etiqueta("Cantidad");
        lblCantidad.setBounds(650, 130, 100, 30);
        add(lblCantidad);

        spinnerCantidad = new JSpinner(new SpinnerNumberModel(1, 1, 99, 1));
        spinnerCantidad.setBounds(765, 130, 90, 30);
        add(spinnerCantidad);

        btnAgregar = boton("Agregar a caja");
        btnAgregar.setBounds(900, 120, 170, 45);
        add(btnAgregar);

        modeloCarrito = new DefaultTableModel(new Object[] {
                "Producto", "Cantidad", "Precio Unitario", "Subtotal", "Impuestos", "Total"
        }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaCarrito = new JTable(modeloCarrito);
        tablaCarrito.setBackground(EstiloUI.COLOR_COMPONENTE_CAFE);
        tablaCarrito.setForeground(Color.WHITE);
        tablaCarrito.setGridColor(EstiloUI.COLOR_TEXTO_OSCURO);
        tablaCarrito.setRowHeight(35);
        tablaCarrito.getTableHeader().setBackground(EstiloUI.COLOR_RECTANGULO_TEXTO);
        tablaCarrito.getTableHeader().setForeground(Color.WHITE);

        JScrollPane scroll = new JScrollPane(tablaCarrito);
        scroll.setBounds(160, 210, 940, 280);
        add(scroll);

        btnVolver = boton("Volver");
        btnVolver.setBounds(20, 620, 110, 35);
        add(btnVolver);

        btnRegistrarVenta = boton("Registrar venta en caja");
        btnRegistrarVenta.setBounds(520, 535, 220, 50);
        add(btnRegistrarVenta);

        lblEstado = new JLabel("Estado: caja lista");
        lblEstado.setOpaque(true);
        lblEstado.setForeground(Color.WHITE);
        lblEstado.setBackground(EstiloUI.COLOR_COMPONENTE_CAFE);
        lblEstado.setBounds(780, 535, 360, 50);
        add(lblEstado);

        btnAgregar.addActionListener(e -> agregarItem());
        btnRegistrarVenta.addActionListener(e -> registrarVentaCaja());
    }

    private JLabel etiqueta(String texto) {
        JLabel label = new JLabel(texto, JLabel.CENTER);
        label.setOpaque(true);
        label.setBackground(EstiloUI.COLOR_RECTANGULO_TEXTO);
        label.setForeground(Color.WHITE);
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

        lblEstado.setText(empleado instanceof Mesero ? "Estado: caja lista" : "Estado: solo meseros pueden usar caja");
    }

    private void cargarProductos() {
        productosDisponibles.clear();

        if (cafeteria != null) {
            for (Juego juego : cafeteria.getJuegos()) {
                for (CopiaVenta copia : juego.getCopiasParaVenta()) {
                    productosDisponibles.add(new ProductoOpcion(juego.getNombre() + " - Juego", copia, copia.getPrecioVenta(), copia));
                }
            }

            // =====================================================
            // CAMBIO NUEVO PROYECTO 3 - CAJA USA EL MENÚ REAL DEL ADMIN.
            // =====================================================
            if (cafeteria.getMenuCafeteria() != null) {
                for (ProductoComestible producto : cafeteria.getMenuCafeteria()) {
                    String tipo = producto instanceof Bebida ? "Bebida" : "Pastelería";
                    productosDisponibles.add(new ProductoOpcion(producto.getNombre() + " - " + tipo + " (Menú)", producto, producto.getPrecioBase(), null));
                }
            }
            // =====================================================
            // FIN CAMBIO NUEVO PROYECTO 3
            // =====================================================
        }

        productosDisponibles.add(new ProductoOpcion("Café - Bebida", new Bebida("Café", 6000, true, false), 6000, null));
        productosDisponibles.add(new ProductoOpcion("Gaseosa - Bebida", new Bebida("Gaseosa", 5000, false, false), 5000, null));
        productosDisponibles.add(new ProductoOpcion("Brownie - Pastelería", new Pasteleria("Brownie", 8000, new ArrayList<String>()), 8000, null));
    }

    private void agregarItem() {
        ProductoOpcion opcion = (ProductoOpcion) comboProducto.getSelectedItem();

        if (opcion == null) {
            JOptionPane.showMessageDialog(this, "No hay producto seleccionado.");
            return;
        }

        int cantidad = (Integer) spinnerCantidad.getValue();

        if (opcion.copiaVenta != null && cantidad > 1) {
            cantidad = 1;
            spinnerCantidad.setValue(1);
            JOptionPane.showMessageDialog(this, "Cada copia de juego se vende de a una unidad.");
        }

        ItemVenta item = new ItemVenta(opcion.producto, cantidad, opcion.precio);
        carrito.add(item);

        modeloCarrito.addRow(new Object[] {
                opcion.nombre,
                cantidad,
                opcion.precio,
                item.getSubtotalItem(),
                item.calcularImpuestoItem(),
                item.getSubtotalItem() + item.calcularImpuestoItem()
        });
    }

    private void registrarVentaCaja() {
        try {
            if (!(empleado instanceof Mesero)) {
                throw new IllegalStateException("Solo los meseros pueden registrar operaciones de caja.");
            }

            if (carrito.isEmpty()) {
                throw new IllegalStateException("El carrito de caja está vacío.");
            }

            Mesero mesero = (Mesero) empleado;
            Venta venta = mesero.registrarVenta(carrito.toArray(new ItemVenta[0]), null);

            descontarCopiasVendidas();

            if (persistencia != null && cafeteria != null) {
                persistencia.guardarTodo(cafeteria);
            }

            JOptionPane.showMessageDialog(this, "Venta registrada en caja.\nTotal: $" + venta.getTotal());

            carrito.clear();
            modeloCarrito.setRowCount(0);
            refrescar();

        } catch (Exception ex) {
            lblEstado.setText("Estado: " + ex.getMessage());
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error caja", JOptionPane.ERROR_MESSAGE);
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
        private CopiaVenta copiaVenta;

        public ProductoOpcion(String nombre, ProductoVendible producto, double precio, CopiaVenta copiaVenta) {
            this.nombre = nombre;
            this.producto = producto;
            this.precio = precio;
            this.copiaVenta = copiaVenta;
        }

        @Override
        public String toString() {
            return nombre;
        }
    }
}