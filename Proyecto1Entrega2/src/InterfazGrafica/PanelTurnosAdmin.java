package InterfazGrafica;

import Usuario.DiaSemana;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class PanelTurnosAdmin extends JPanel {

    private JTable tablaEmpleados;
    private JTable tablaSolicitudes;

    private DefaultTableModel modeloEmpleados;
    private DefaultTableModel modeloSolicitudes;

    private JComboBox<String> cbEmpleado;
    private JComboBox<DiaSemana> cbDia;

    private JButton btnActualizar;
    private JButton btnAsignarTurno;
    private JButton btnQuitarTurno;
    private JButton btnAprobarSolicitud;
    private JButton btnRechazarSolicitud;

    public PanelTurnosAdmin() {
        setLayout(new BorderLayout(10, 10));
        setBackground(EstiloUI.COLOR_FONDO_BEIGE);
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel titulo = new JLabel("Gestión de Turnos del Personal");
        titulo.setFont(EstiloUI.FUENTE_TITULO);
        titulo.setForeground(EstiloUI.COLOR_TEXTO_OSCURO);
        add(titulo, BorderLayout.NORTH);

        JPanel panelCentro = new JPanel(new GridLayout(1, 2, 15, 15));
        panelCentro.setOpaque(false);

        modeloEmpleados = new DefaultTableModel(new Object[]{"Login", "Nombre", "Rol", "Turnos"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaEmpleados = new JTable(modeloEmpleados);
        tablaEmpleados.setRowHeight(24);

        JPanel panelIzquierdo = new JPanel(new BorderLayout(8, 8));
        panelIzquierdo.setOpaque(false);
        panelIzquierdo.add(new JLabel("Empleados y turnos asignados"), BorderLayout.NORTH);
        panelIzquierdo.add(new JScrollPane(tablaEmpleados), BorderLayout.CENTER);

        JPanel panelFormulario = new JPanel(new GridLayout(0, 2, 8, 8));
        panelFormulario.setOpaque(false);

        cbEmpleado = new JComboBox<>();
        cbDia = new JComboBox<>(DiaSemana.values());

        btnActualizar = crearBoton("Actualizar");
        btnAsignarTurno = crearBoton("Asignar turno");
        btnQuitarTurno = crearBoton("Quitar turno");

        panelFormulario.add(new JLabel("Empleado:"));
        panelFormulario.add(cbEmpleado);

        panelFormulario.add(new JLabel("Día:"));
        panelFormulario.add(cbDia);

        panelFormulario.add(btnActualizar);
        panelFormulario.add(btnAsignarTurno);

        panelFormulario.add(new JLabel(""));
        panelFormulario.add(btnQuitarTurno);

        panelIzquierdo.add(panelFormulario, BorderLayout.SOUTH);

        modeloSolicitudes = new DefaultTableModel(new Object[]{"#", "Empleado", "Día", "Tipo", "Estado"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaSolicitudes = new JTable(modeloSolicitudes);
        tablaSolicitudes.setRowHeight(24);

        JPanel panelDerecho = new JPanel(new BorderLayout(8, 8));
        panelDerecho.setOpaque(false);
        panelDerecho.add(new JLabel("Solicitudes de cambio/intercambio"), BorderLayout.NORTH);
        panelDerecho.add(new JScrollPane(tablaSolicitudes), BorderLayout.CENTER);

        JPanel panelBotonesSolicitudes = new JPanel(new GridLayout(1, 2, 8, 8));
        panelBotonesSolicitudes.setOpaque(false);

        btnAprobarSolicitud = crearBoton("Aprobar solicitud");
        btnRechazarSolicitud = crearBoton("Rechazar solicitud");

        panelBotonesSolicitudes.add(btnAprobarSolicitud);
        panelBotonesSolicitudes.add(btnRechazarSolicitud);

        panelDerecho.add(panelBotonesSolicitudes, BorderLayout.SOUTH);

        panelCentro.add(panelIzquierdo);
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

    public DefaultTableModel getModeloEmpleados() {
        return modeloEmpleados;
    }

    public DefaultTableModel getModeloSolicitudes() {
        return modeloSolicitudes;
    }

    public JComboBox<String> getCbEmpleado() {
        return cbEmpleado;
    }

    public String getLoginEmpleadoSeleccionado() {
        Object seleccionado = cbEmpleado.getSelectedItem();
        return seleccionado == null ? null : seleccionado.toString();
    }

    public DiaSemana getDiaSeleccionado() {
        return (DiaSemana) cbDia.getSelectedItem();
    }

    public JButton getBtnActualizar() {
        return btnActualizar;
    }

    public JButton getBtnAsignarTurno() {
        return btnAsignarTurno;
    }

    public JButton getBtnQuitarTurno() {
        return btnQuitarTurno;
    }

    public JButton getBtnAprobarSolicitud() {
        return btnAprobarSolicitud;
    }

    public JButton getBtnRechazarSolicitud() {
        return btnRechazarSolicitud;
    }

    public int getIndiceSolicitudSeleccionada() {
        int fila = tablaSolicitudes.getSelectedRow();

        if (fila < 0) {
            return -1;
        }

        int filaModelo = tablaSolicitudes.convertRowIndexToModel(fila);
        Object valor = modeloSolicitudes.getValueAt(filaModelo, 0);

        return Integer.parseInt(valor.toString());
    }
}