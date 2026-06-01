package InterfazGrafica;

import java.awt.Color;
import java.awt.Font;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import Persistencia.GestorPersistencia;
import Usuario.DiaSemana;
import Usuario.DiaTurno;
import Usuario.Empleado;
import Usuario.SolicitudTurno;
import Usuario.Usuario;
import World.Cafeteria;

public class PanelTurnosEmpleado extends JPanel {

    private Cafeteria cafeteria;
    private Empleado empleado;
    private GestorPersistencia persistencia;

    private JTable tablaTurnos;
    private DefaultTableModel modeloTabla;
    private JComboBox<DiaSemana> comboDiaCambio;
    private JComboBox<DiaSemana> comboDiaIntercambio;
    private JComboBox<String> comboEmpleado;
    private JButton btnActualizar;
    private JButton btnSolicitarCambio;
    private JButton btnSolicitarIntercambio;
    private JButton btnVolver;
    private JLabel lblEstado;

    public PanelTurnosEmpleado() {
        setLayout(null);
        setBackground(EstiloUI.COLOR_FONDO_BEIGE);

        JLabel titulo = new JLabel("Board Game Cafe - Panel Empleado");
        titulo.setOpaque(true);
        titulo.setBackground(EstiloUI.COLOR_BANNER_CAFE);
        titulo.setForeground(Color.WHITE);
        titulo.setFont(new Font("Arial", Font.BOLD, 24));
        titulo.setBounds(0, 0, 1280, 60);
        add(titulo);

        btnActualizar = crearBoton("Actualizar");
        btnActualizar.setBounds(560, 105, 185, 50);
        add(btnActualizar);

        modeloTabla = new DefaultTableModel(new Object[] { "Día", "Asignado", "Aprobado" }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaTurnos = new JTable(modeloTabla);
        tablaTurnos.setBackground(EstiloUI.COLOR_COMPONENTE_CAFE);
        tablaTurnos.setForeground(Color.WHITE);
        tablaTurnos.setGridColor(EstiloUI.COLOR_TEXTO_OSCURO);
        tablaTurnos.setRowHeight(42);
        tablaTurnos.getTableHeader().setBackground(EstiloUI.COLOR_RECTANGULO_TEXTO);
        tablaTurnos.getTableHeader().setForeground(Color.WHITE);
        tablaTurnos.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));

        JScrollPane scroll = new JScrollPane(tablaTurnos);
        scroll.setBounds(420, 185, 460, 170);
        add(scroll);

        JLabel tituloCambio = crearBarraSeccion("Solicitar cambio de turno");
        tituloCambio.setBounds(120, 380, 1040, 30);
        add(tituloCambio);

        JLabel lblDiaCambio = crearEtiquetaPequena("Día");
        lblDiaCambio.setBounds(220, 425, 55, 30);
        add(lblDiaCambio);

        comboDiaCambio = crearComboDias();
        comboDiaCambio.setBounds(290, 425, 205, 30);
        add(comboDiaCambio);

        btnSolicitarCambio = crearBoton("Solicitar cambio");
        btnSolicitarCambio.setBounds(560, 425, 150, 30);
        add(btnSolicitarCambio);

        JLabel tituloIntercambio = crearBarraSeccion("Solicitar intercambio");
        tituloIntercambio.setBounds(120, 485, 1040, 30);
        add(tituloIntercambio);

        JLabel lblEmpleado = crearEtiquetaPequena("Empleado");
        lblEmpleado.setBounds(220, 530, 80, 30);
        add(lblEmpleado);

        comboEmpleado = new JComboBox<String>();
        comboEmpleado.setBackground(EstiloUI.COLOR_COMPONENTE_CAFE);
        comboEmpleado.setForeground(Color.WHITE);
        comboEmpleado.setBounds(310, 530, 190, 30);
        add(comboEmpleado);

        JLabel lblDiaIntercambio = crearEtiquetaPequena("Día");
        lblDiaIntercambio.setBounds(535, 530, 55, 30);
        add(lblDiaIntercambio);

        comboDiaIntercambio = crearComboDias();
        comboDiaIntercambio.setBounds(605, 530, 205, 30);
        add(comboDiaIntercambio);

        btnSolicitarIntercambio = crearBoton("Solicitar intercambio");
        btnSolicitarIntercambio.setBounds(875, 530, 160, 30);
        add(btnSolicitarIntercambio);

        btnVolver = crearBoton("Volver");
        btnVolver.setBounds(20, 610, 115, 35);
        add(btnVolver);

        lblEstado = new JLabel("Estado: No hay solicitudes pendientes");
        lblEstado.setOpaque(true);
        lblEstado.setForeground(Color.WHITE);
        lblEstado.setBackground(EstiloUI.COLOR_COMPONENTE_CAFE);
        lblEstado.setBounds(220, 610, 610, 35);
        add(lblEstado);

        configurarListeners();
    }

    private JButton crearBoton(String texto) {
        JButton boton = new JButton(texto);
        boton.setBackground(EstiloUI.COLOR_COMPONENTE_CAFE);
        boton.setForeground(Color.WHITE);
        boton.setFocusPainted(false);
        boton.setBorder(BorderFactory.createEmptyBorder());
        return boton;
    }

    private JLabel crearEtiquetaPequena(String texto) {
        JLabel label = new JLabel(texto);
        label.setOpaque(true);
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setBackground(EstiloUI.COLOR_RECTANGULO_TEXTO);
        label.setForeground(Color.WHITE);
        return label;
    }

    private JLabel crearBarraSeccion(String texto) {
        JLabel label = new JLabel("   " + texto);
        label.setOpaque(true);
        label.setBackground(EstiloUI.COLOR_RECTANGULO_TEXTO);
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Arial", Font.PLAIN, 20));
        return label;
    }

    private JComboBox<DiaSemana> crearComboDias() {
        JComboBox<DiaSemana> combo = new JComboBox<DiaSemana>(DiaSemana.values());
        combo.setBackground(EstiloUI.COLOR_COMPONENTE_CAFE);
        combo.setForeground(Color.WHITE);
        return combo;
    }
    
    // CAMBIO - Sobrecarga para recibir persistencia y guardar solicitudes de turno.
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

    private void configurarListeners() {
        btnActualizar.addActionListener(e -> refrescar());
        btnSolicitarCambio.addActionListener(e -> solicitarCambio());
        btnSolicitarIntercambio.addActionListener(e -> solicitarIntercambio());
    }

    public void refrescar() {
        modeloTabla.setRowCount(0);
        comboEmpleado.removeAllItems();

        if (empleado == null) {
            lblEstado.setText("Estado: No hay empleado cargado");
            return;
        }

        List<DiaTurno> turnos = empleado.consultarDiasAsignados();

        for (DiaTurno turno : turnos) {
            modeloTabla.addRow(new Object[] {
                    turno.getDia(),
                    turno.estaAsignado() ? "Sí" : "No",
                    turno.isAprobado() ? "Sí" : "No"
            });
        }

        if (cafeteria != null) {
            for (Usuario usuario : cafeteria.getUsuarios()) {
                if (usuario instanceof Empleado && !usuario.getLogin().equals(empleado.getLogin())) {
                    comboEmpleado.addItem(usuario.getLogin());
                }
            }
        }

        lblEstado.setText("Estado: Turnos cargados: " + turnos.size());
    }

    private void solicitarCambio() {
        try {
            DiaSemana dia = (DiaSemana) comboDiaCambio.getSelectedItem();
            SolicitudTurno solicitud = empleado.solicitarCambioTurno(dia);

            // =====================================================
            // CAMBIO La solicitud ahora se guarda en Cafeteria para que el admin pueda aprobarla/rechazarla.
            if (cafeteria != null) {
                cafeteria.getSolicitudesTurno().add(solicitud);
            }

            if (persistencia != null && cafeteria != null) {
                persistencia.guardarTodo(cafeteria);
            }

            lblEstado.setText("Estado: Solicitud de cambio " + solicitud.getEstado() + " para " + solicitud.getDia());
            JOptionPane.showMessageDialog(this, "Solicitud de cambio creada correctamente.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "No se pudo solicitar cambio", JOptionPane.ERROR_MESSAGE);
        }
    }
    private void solicitarIntercambio() {
        try {
            String loginOtro = (String) comboEmpleado.getSelectedItem();
            Empleado otro = buscarEmpleadoPorLogin(loginOtro);
            DiaSemana dia = (DiaSemana) comboDiaIntercambio.getSelectedItem();

            if (otro == null) {
                throw new IllegalArgumentException("Selecciona un empleado válido.");
            }

            SolicitudTurno solicitud = empleado.solicitarIntercambioTurno(otro, dia);

            // CAMBIO La solicitud ahora se guarda en Cafeteria para que el admin pueda aprobarla/rechazarla.
            if (cafeteria != null) {
                cafeteria.getSolicitudesTurno().add(solicitud);
            }

            if (persistencia != null && cafeteria != null) {
                persistencia.guardarTodo(cafeteria);
            }

            lblEstado.setText("Estado: Solicitud de intercambio " + solicitud.getEstado() + " para " + solicitud.getDia());
            JOptionPane.showMessageDialog(this, "Solicitud de intercambio creada correctamente.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "No se pudo solicitar intercambio", JOptionPane.ERROR_MESSAGE);
        }
    }
    private Empleado buscarEmpleadoPorLogin(String login) {
        if (login == null || cafeteria == null) {
            return null;
        }

        for (Usuario usuario : cafeteria.getUsuarios()) {
            if (usuario instanceof Empleado && login.equals(usuario.getLogin())) {
                return (Empleado) usuario;
            }
        }

        return null;
    }
}