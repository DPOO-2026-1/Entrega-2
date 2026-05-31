package InterfazGrafica;

import java.awt.Color;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import Persistencia.GestorPersistencia;
import Usuario.Empleado;
import World.Cafeteria;
import World.CopiaPrestamo;
import World.Juego;
import World.Prestamo;

public class PanelAlquilerEmpleado extends JPanel {

    private Cafeteria cafeteria;
    private Empleado empleado;
    private GestorPersistencia persistencia;

    private JTable tablaJuegos;
    private JTable tablaAlquileres;

    private DefaultTableModel modeloJuegos;
    private DefaultTableModel modeloAlquileres;

    private JButton btnAlquilar;
    private JButton btnDevolver;
    private JButton btnVolver;

    private JLabel lblEstado;

    public PanelAlquilerEmpleado() {
        setLayout(null);
        setBackground(EstiloUI.COLOR_FONDO_BEIGE);

        JLabel titulo = new JLabel("  Board Game Cafe - AlquilerEmpleado");
        titulo.setOpaque(true);
        titulo.setBackground(EstiloUI.COLOR_BANNER_CAFE);
        titulo.setForeground(Color.WHITE);
        titulo.setFont(new Font("Arial", Font.BOLD, 24));
        titulo.setBounds(0, 0, 1280, 60);
        add(titulo);

        JLabel lblDisponibles = seccion("Juegos Disponibles");
        lblDisponibles.setBounds(210, 80, 250, 35);
        add(lblDisponibles);

        modeloJuegos = new DefaultTableModel(
                new Object[] { "Nombre", "Categoría", "Copia", "Disponible" }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaJuegos = tabla(modeloJuegos);

        JScrollPane scrollJuegos = new JScrollPane(tablaJuegos);
        scrollJuegos.setBounds(160, 130, 850, 140);
        add(scrollJuegos);

        btnAlquilar = boton("Alquilar juego");
        btnAlquilar.setBounds(500, 300, 180, 50);
        add(btnAlquilar);

        JLabel lblActivos = seccion("Mis alquileres activos");
        lblActivos.setBounds(210, 380, 250, 35);
        add(lblActivos);

        modeloAlquileres = new DefaultTableModel(new Object[] { "Juego", "Copia", "Estado" }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaAlquileres = tabla(modeloAlquileres);

        JScrollPane scrollAlquileres = new JScrollPane(tablaAlquileres);
        scrollAlquileres.setBounds(210, 435, 700, 120);
        add(scrollAlquileres);

        btnDevolver = boton("Devolver juego");
        btnDevolver.setBounds(500, 570, 180, 45);
        add(btnDevolver);

        btnVolver = boton("Volver");
        btnVolver.setBounds(20, 620, 110, 35);
        add(btnVolver);

        lblEstado = new JLabel("Estado: ...");
        lblEstado.setOpaque(true);
        lblEstado.setForeground(Color.WHITE);
        lblEstado.setBackground(EstiloUI.COLOR_COMPONENTE_CAFE);
        lblEstado.setBounds(200, 620, 720, 35);
        add(lblEstado);

        btnAlquilar.addActionListener(e -> alquilarJuego());
        btnDevolver.addActionListener(e -> devolverJuego());
    }

    private JLabel seccion(String texto) {
        JLabel label = new JLabel(texto, JLabel.CENTER);
        label.setOpaque(true);
        label.setBackground(EstiloUI.COLOR_RECTANGULO_TEXTO);
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Arial", Font.BOLD, 14));
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
        modeloJuegos.setRowCount(0);
        modeloAlquileres.setRowCount(0);

        if (cafeteria == null || empleado == null) {
            return;
        }

        for (Juego juego : cafeteria.getJuegos()) {
            for (CopiaPrestamo copia : juego.getCopiasParaPrestamo()) {
                modeloJuegos.addRow(new Object[] {
                        juego.getNombre(),
                        juego.getCategoria(),
                        copia.getIdUnico(),
                        copia.estaDisponible() ? "Sí" : "-"
                });
            }
        }

        for (Prestamo prestamo : cafeteria.getHistorialPrestamos()) {
            if (prestamo.getSolicitadoPor() != null
                    && prestamo.getSolicitadoPor().getLogin().equals(empleado.getLogin())
                    && "Activo".equalsIgnoreCase(prestamo.getEstado())) {

                CopiaPrestamo copia = prestamo.getCopia();

                String juego = copia != null && copia.getJuegoAsociado() != null
                        ? copia.getJuegoAsociado().getNombre()
                        : "-";

                String idCopia = copia != null ? copia.getIdUnico() : "-";

                modeloAlquileres.addRow(new Object[] {
                        juego,
                        idCopia,
                        prestamo.getEstado()
                });
            }
        }

        lblEstado.setText("Estado: alquileres activos: " + modeloAlquileres.getRowCount());
    }

    private void alquilarJuego() {
        int fila = tablaJuegos.getSelectedRow();

        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Selecciona una copia disponible.");
            return;
        }

        int filaModelo = tablaJuegos.convertRowIndexToModel(fila);
        String idCopia = (String) modeloJuegos.getValueAt(filaModelo, 2);

        try {
            CopiaPrestamo copia = buscarCopia(idCopia);

            if (copia == null) {
                throw new IllegalArgumentException("No se encontró la copia.");
            }

            empleado.alquilarJuego(copia);

            guardar();
            refrescar();

            JOptionPane.showMessageDialog(
                    this,
                    "Juego alquilado correctamente.",
                    "Éxito",
                    JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception ex) {
            mostrarError(ex);
        }
    }

    private void devolverJuego() {
        int fila = tablaAlquileres.getSelectedRow();

        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Selecciona un alquiler activo.");
            return;
        }

        int filaModelo = tablaAlquileres.convertRowIndexToModel(fila);
        String idCopia = (String) modeloAlquileres.getValueAt(filaModelo, 1);

        try {
            Prestamo prestamo = buscarPrestamoActivo(idCopia);

            if (prestamo == null) {
                throw new IllegalArgumentException("No se encontró el alquiler.");
            }

            prestamo.finalizar();

            guardar();
            refrescar();

            JOptionPane.showMessageDialog(
                    this,
                    "Juego devuelto correctamente.",
                    "Éxito",
                    JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception ex) {
            mostrarError(ex);
        }
    }

    private CopiaPrestamo buscarCopia(String idCopia) {
        for (Juego juego : cafeteria.getJuegos()) {
            for (CopiaPrestamo copia : juego.getCopiasParaPrestamo()) {
                if (copia.getIdUnico().equals(idCopia)) {
                    return copia;
                }
            }
        }

        return null;
    }

    private Prestamo buscarPrestamoActivo(String idCopia) {
        for (Prestamo prestamo : cafeteria.getHistorialPrestamos()) {
            CopiaPrestamo copia = prestamo.getCopia();

            if (prestamo.getSolicitadoPor() != null
                    && prestamo.getSolicitadoPor().getLogin().equals(empleado.getLogin())
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