package InterfazGrafica;

import java.awt.Color;
import java.awt.Font;

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

import Persistencia.GestorPersistencia;
import Usuario.Cliente;
import World.Cafeteria;
import World.CopiaPrestamo;
import World.Juego;
import World.Mesa;
import World.Prestamo;

public class PanelPrestamosCliente extends JPanel {

    private Cafeteria cafeteria;
    private Cliente cliente;
    private GestorPersistencia persistencia;

    private JSpinner spinnerPersonas;
    private JComboBox<String> comboNinos;
    private JComboBox<String> comboJovenes;

    private JTable tablaJuegos;
    private JTable tablaPrestamos;
    private DefaultTableModel modeloJuegos;
    private DefaultTableModel modeloPrestamos;

    private JButton btnReservarMesa;
    private JButton btnSolicitarPrestamo;
    private JButton btnDevolver;
    private JButton btnVolver;

    private JLabel lblEstado;

    public PanelPrestamosCliente() {
        setLayout(null);
        setBackground(EstiloUI.COLOR_FONDO_BEIGE);

        add(titulo("Board Game Cafe - Prestamos Cliente"));

        JLabel lblMesa = seccion("Reservar Mesa");
        lblMesa.setBounds(85, 90, 300, 35);
        add(lblMesa);

        JLabel lblPersonas = etiqueta("Personas");
        lblPersonas.setBounds(155, 140, 80, 30);
        add(lblPersonas);

        spinnerPersonas = new JSpinner(new SpinnerNumberModel(1, 1, 12, 1));
        spinnerPersonas.setBounds(245, 140, 90, 30);
        add(spinnerPersonas);

        JLabel lblNinos = etiqueta("Hay Niños");
        lblNinos.setBounds(405, 140, 90, 30);
        add(lblNinos);

        comboNinos = comboSiNo();
        comboNinos.setBounds(505, 140, 65, 30);
        add(comboNinos);

        JLabel lblJovenes = etiqueta("Hay Jovenes");
        lblJovenes.setBounds(625, 140, 100, 30);
        add(lblJovenes);

        comboJovenes = comboSiNo();
        comboJovenes.setBounds(735, 140, 65, 30);
        add(comboJovenes);

        btnReservarMesa = boton("Reservar mesa");
        btnReservarMesa.setBounds(880, 130, 180, 50);
        add(btnReservarMesa);

        JLabel lblJuegos = seccion("Juegos Disponibles");
        lblJuegos.setBounds(60, 220, 300, 35);
        add(lblJuegos);

        modeloJuegos = new DefaultTableModel(
                new Object[] { "Nombre", "Categoría", "Edad Mínima", "Copias Disponibles", "Difícil" }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaJuegos = tabla(modeloJuegos);

        JScrollPane scrollJuegos = new JScrollPane(tablaJuegos);
        scrollJuegos.setBounds(60, 275, 450, 115);
        add(scrollJuegos);

        btnSolicitarPrestamo = boton("Solicitar Préstamo");
        btnSolicitarPrestamo.setBounds(60, 420, 180, 45);
        add(btnSolicitarPrestamo);

        JLabel lblActivos = seccion("Préstamos Activos");
        lblActivos.setBounds(590, 220, 300, 35);
        add(lblActivos);

        modeloPrestamos = new DefaultTableModel(new Object[] { "Juego", "Copia", "Estado" }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaPrestamos = tabla(modeloPrestamos);

        JScrollPane scrollPrestamos = new JScrollPane(tablaPrestamos);
        scrollPrestamos.setBounds(590, 275, 430, 115);
        add(scrollPrestamos);

        btnDevolver = boton("Devolver Préstamo");
        btnDevolver.setBounds(590, 420, 180, 45);
        add(btnDevolver);

        btnVolver = boton("Volver");
        btnVolver.setBounds(20, 620, 110, 35);
        add(btnVolver);

        lblEstado = new JLabel("Estado: ...");
        lblEstado.setOpaque(true);
        lblEstado.setForeground(Color.WHITE);
        lblEstado.setBackground(EstiloUI.COLOR_COMPONENTE_CAFE);
        lblEstado.setBounds(200, 620, 610, 35);
        add(lblEstado);

        btnReservarMesa.addActionListener(e -> reservarMesa());
        btnSolicitarPrestamo.addActionListener(e -> solicitarPrestamo());
        btnDevolver.addActionListener(e -> devolverPrestamo());
    }

    private JLabel titulo(String texto) {
        JLabel label = new JLabel("  " + texto);
        label.setOpaque(true);
        label.setBackground(EstiloUI.COLOR_BANNER_CAFE);
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Arial", Font.BOLD, 24));
        label.setBounds(0, 0, 1280, 60);
        return label;
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

    private JComboBox<String> comboSiNo() {
        JComboBox<String> combo = new JComboBox<>(new String[] { "Si", "No" });
        combo.setBackground(EstiloUI.COLOR_COMPONENTE_CAFE);
        combo.setForeground(Color.WHITE);
        return combo;
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
        modeloJuegos.setRowCount(0);
        modeloPrestamos.setRowCount(0);

        if (cafeteria == null || cliente == null) {
            return;
        }

        for (Juego juego : cafeteria.getJuegos()) {
            modeloJuegos.addRow(new Object[] {
                    juego.getNombre(),
                    juego.getCategoria(),
                    juego.getEdadMinima(),
                    contarCopiasDisponibles(juego),
                    juego.isEsDificil() ? "Sí" : "-"
            });
        }

        for (Prestamo prestamo : cafeteria.getHistorialPrestamos()) {
            if (prestamo.getSolicitadoPor() != null
                    && prestamo.getSolicitadoPor().getLogin().equals(cliente.getLogin())
                    && "Activo".equalsIgnoreCase(prestamo.getEstado())) {

                CopiaPrestamo copia = prestamo.getCopia();

                String juego = copia != null && copia.getJuegoAsociado() != null
                        ? copia.getJuegoAsociado().getNombre()
                        : "-";

                String idCopia = copia != null ? copia.getIdUnico() : "-";

                modeloPrestamos.addRow(new Object[] {
                        juego,
                        idCopia,
                        prestamo.getEstado()
                });
            }
        }

        lblEstado.setText("Estado: préstamos activos del cliente: " + modeloPrestamos.getRowCount());
    }

    private int contarCopiasDisponibles(Juego juego) {
        int disponibles = 0;

        for (CopiaPrestamo copia : juego.getCopiasParaPrestamo()) {
            if (copia.estaDisponible()) {
                disponibles++;
            }
        }

        return disponibles;
    }

    private void reservarMesa() {
        try {
            int personas = (Integer) spinnerPersonas.getValue();
            boolean hayNinos = "Si".equals(comboNinos.getSelectedItem());
            boolean hayJovenes = "Si".equals(comboJovenes.getSelectedItem());

            Mesa mesa = cliente.reservarMesa(personas, hayNinos, hayJovenes);

            guardar();

            lblEstado.setText("Estado: mesa reservada ID " + mesa.getIdMesa());

            JOptionPane.showMessageDialog(
                    this,
                    "Mesa reservada correctamente. ID: " + mesa.getIdMesa(),
                    "Éxito",
                    JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception ex) {
            mostrarError(ex);
        }
    }

    private void solicitarPrestamo() {
        int fila = tablaJuegos.getSelectedRow();

        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Selecciona un juego disponible.");
            return;
        }

        String nombreJuego = (String) modeloJuegos.getValueAt(
                tablaJuegos.convertRowIndexToModel(fila), 0);

        Juego juego = cafeteria.buscarJuego(nombreJuego);

        try {
            if (juego == null) {
                throw new IllegalArgumentException("Juego no encontrado.");
            }

            CopiaPrestamo copia = juego.getCopiaDisponible();

            if (copia == null) {
                throw new IllegalStateException("No hay copias disponibles de este juego.");
            }

            cliente.solicitarPrestamo(copia, cliente.getMesaActual());

            guardar();
            refrescar();

            JOptionPane.showMessageDialog(
                    this,
                    "Préstamo creado correctamente.",
                    "Éxito",
                    JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception ex) {
            mostrarError(ex);
        }
    }

    private void devolverPrestamo() {
        int fila = tablaPrestamos.getSelectedRow();

        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Selecciona un préstamo activo.");
            return;
        }

        int filaModelo = tablaPrestamos.convertRowIndexToModel(fila);
        String idCopia = (String) modeloPrestamos.getValueAt(filaModelo, 1);

        try {
            Prestamo prestamo = buscarPrestamoActivo(idCopia);

            if (prestamo == null) {
                throw new IllegalArgumentException("No se encontró el préstamo.");
            }

            cliente.devolverJuego(prestamo);

            guardar();
            refrescar();

            JOptionPane.showMessageDialog(
                    this,
                    "Préstamo devuelto correctamente.",
                    "Éxito",
                    JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception ex) {
            mostrarError(ex);
        }
    }

    private Prestamo buscarPrestamoActivo(String idCopia) {
        for (Prestamo prestamo : cafeteria.getHistorialPrestamos()) {
            CopiaPrestamo copia = prestamo.getCopia();

            if (prestamo.getSolicitadoPor() != null
                    && prestamo.getSolicitadoPor().getLogin().equals(cliente.getLogin())
                    && "Activo".equalsIgnoreCase(prestamo.getEstado())
                    && copia != null
                    && copia.getIdUnico().equals(idCopia)) {

                return prestamo;
            }
        }

        return null;
    }

    private void guardar() {
        if (persistencia != null && cafeteria != null) {
            persistencia.guardarTodo(cafeteria);
        }
    }

    private void mostrarError(Exception ex) {
        lblEstado.setText("Estado: " + ex.getMessage());

        JOptionPane.showMessageDialog(
                this,
                ex.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
    }
}