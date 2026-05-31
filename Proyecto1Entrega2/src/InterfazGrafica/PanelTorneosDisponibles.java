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
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.SpinnerNumberModel;
import javax.swing.table.DefaultTableModel;

import Persistencia.GestorPersistencia;
import Torneo.InscripcionTorneo;
import Torneo.Torneo;
import Usuario.DiaSemana;
import Usuario.Usuario;
import World.Cafeteria;

public class PanelTorneosDisponibles extends JPanel {

    private Cafeteria cafeteria;
    private Usuario usuarioActual;
    private GestorPersistencia persistencia;

    private JComboBox<DiaSemana> comboDia;
    private JTable tablaTorneos;
    private DefaultTableModel modeloTabla;
    private JSpinner spinnerCupos;
    private JButton btnActualizar;
    private JButton btnInscribirme;
    private JButton btnDesinscribirme;
    private JButton btnVolver;
    private JLabel lblEstado;

    public PanelTorneosDisponibles() {
        setLayout(null);
        setBackground(EstiloUI.COLOR_FONDO_BEIGE);

        JLabel titulo = new JLabel("Board Game Cafe - Torneos Disponibles");
        titulo.setOpaque(true);
        titulo.setBackground(EstiloUI.COLOR_BANNER_CAFE);
        titulo.setForeground(Color.WHITE);
        titulo.setFont(new Font("Arial", Font.BOLD, 24));
        titulo.setBounds(0, 0, 1280, 60);
        add(titulo);

        comboDia = new JComboBox<DiaSemana>(DiaSemana.values());
        comboDia.setBackground(EstiloUI.COLOR_COMPONENTE_CAFE);
        comboDia.setForeground(Color.WHITE);
        comboDia.setBounds(110, 110, 210, 40);
        add(comboDia);

        btnActualizar = crearBoton("Actualizar");
        btnActualizar.setBounds(885, 110, 185, 40);
        add(btnActualizar);

        modeloTabla = new DefaultTableModel(
                new Object[] { "ID", "Nombre", "Juego", "Tipo", "Estado", "Cupos libres", "Reservados libres", "Inscrito" },
                0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaTorneos = new JTable(modeloTabla);
        tablaTorneos.setBackground(EstiloUI.COLOR_COMPONENTE_CAFE);
        tablaTorneos.setForeground(Color.WHITE);
        tablaTorneos.setGridColor(EstiloUI.COLOR_TEXTO_OSCURO);
        tablaTorneos.getTableHeader().setBackground(EstiloUI.COLOR_RECTANGULO_TEXTO);
        tablaTorneos.getTableHeader().setForeground(Color.WHITE);
        tablaTorneos.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        tablaTorneos.setRowHeight(36);

        JScrollPane scroll = new JScrollPane(tablaTorneos);
        scroll.setBounds(300, 180, 665, 190);
        add(scroll);

        ocultarColumnaId();

        JLabel lblCupos = new JLabel("Cupos a reservar");
        lblCupos.setOpaque(true);
        lblCupos.setHorizontalAlignment(JLabel.CENTER);
        lblCupos.setForeground(Color.WHITE);
        lblCupos.setBackground(EstiloUI.COLOR_RECTANGULO_TEXTO);
        lblCupos.setBounds(255, 410, 150, 30);
        add(lblCupos);

        spinnerCupos = new JSpinner(new SpinnerNumberModel(1, 1, 3, 1));
        spinnerCupos.setBounds(440, 410, 95, 30);
        add(spinnerCupos);

        btnInscribirme = crearBoton("Inscribirme");
        btnInscribirme.setBounds(380, 490, 180, 50);
        add(btnInscribirme);

        btnDesinscribirme = crearBoton("Desinscribirme");
        btnDesinscribirme.setBounds(720, 490, 180, 50);
        add(btnDesinscribirme);

        btnVolver = crearBoton("Volver");
        btnVolver.setBounds(20, 610, 115, 35);
        add(btnVolver);

        lblEstado = new JLabel("Estado: Selecciona un torneo para continuar");
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

    private void configurarListeners() {
        comboDia.addActionListener(e -> refrescarTabla());
        btnActualizar.addActionListener(e -> refrescarTabla());
        btnInscribirme.addActionListener(e -> inscribirUsuario());
        btnDesinscribirme.addActionListener(e -> desinscribirUsuario());
    }

    public void configurarContexto(Cafeteria cafeteria, Usuario usuarioActual, GestorPersistencia persistencia) {
        this.cafeteria = cafeteria;
        this.usuarioActual = usuarioActual;
        this.persistencia = persistencia;
        refrescarTabla();
    }

    public void setAccionVolver(Runnable accionVolver) {
        for (java.awt.event.ActionListener listener : btnVolver.getActionListeners()) {
            btnVolver.removeActionListener(listener);
        }
        btnVolver.addActionListener(e -> accionVolver.run());
    }

    public void refrescarTabla() {
        modeloTabla.setRowCount(0);

        if (cafeteria == null || cafeteria.getGestorTorneo() == null) {
            lblEstado.setText("Estado: No hay torneos cargados");
            return;
        }

        DiaSemana dia = (DiaSemana) comboDia.getSelectedItem();
        List<Torneo> torneos = cafeteria.getGestorTorneo().getTorneos(dia);

        for (Torneo torneo : torneos) {
            String juego = torneo.getJuegoTorneo() == null ? "-" : torneo.getJuegoTorneo().getNombre();
            String tipo = torneo.getClass().getSimpleName().replace("Torneo", "");
            int cuposLibres = torneo.cuposDisponiblesRegulares() + torneo.cuposDisponiblesReservados();

            modeloTabla.addRow(new Object[] {
                    torneo.getIdTorneo(),
                    torneo.getNombre(),
                    juego,
                    tipo,
                    torneo.getEstado(),
                    cuposLibres,
                    torneo.cuposDisponiblesReservados(),
                    estaInscrito(torneo, usuarioActual) ? "Sí" : "-"
            });
        }

        lblEstado.setText("Estado: Torneos encontrados para " + dia + ": " + torneos.size());
    }

    private void inscribirUsuario() {
        String idTorneo = obtenerIdSeleccionado();

        if (idTorneo == null) {
            JOptionPane.showMessageDialog(this, "Selecciona un torneo primero.", "Torneo requerido", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int cupos = (Integer) spinnerCupos.getValue();

            // La lógica de negocio vive en GestorTorneo.
            cafeteria.getGestorTorneo().inscribir(usuarioActual, idTorneo, cupos);

            guardarCambios();
            refrescarTabla();

            JOptionPane.showMessageDialog(this, "Inscripción realizada correctamente.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "No se pudo inscribir", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void desinscribirUsuario() {
        String idTorneo = obtenerIdSeleccionado();

        if (idTorneo == null) {
            JOptionPane.showMessageDialog(this, "Selecciona un torneo primero.", "Torneo requerido", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            // La lógica de negocio vive en GestorTorneo.
            cafeteria.getGestorTorneo().desinscribir(usuarioActual, idTorneo);

            guardarCambios();
            refrescarTabla();

            JOptionPane.showMessageDialog(this, "Desinscripción realizada correctamente.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "No se pudo desinscribir", JOptionPane.ERROR_MESSAGE);
        }
    }

    private String obtenerIdSeleccionado() {
        int fila = tablaTorneos.getSelectedRow();

        if (fila < 0) {
            return null;
        }

        return (String) modeloTabla.getValueAt(tablaTorneos.convertRowIndexToModel(fila), 0);
    }

    private boolean estaInscrito(Torneo torneo, Usuario usuario) {
        if (torneo == null || usuario == null) {
            return false;
        }

        for (InscripcionTorneo inscripcion : torneo.getInscripciones()) {
            for (Usuario inscrito : inscripcion.getUsuarios()) {
                if (inscrito != null && usuario.getLogin().equals(inscrito.getLogin())) {
                    return true;
                }
            }
        }

        return false;
    }

    private void guardarCambios() {
        if (persistencia != null && cafeteria != null) {
            persistencia.guardarTodo(cafeteria);
        }
    }

    private void ocultarColumnaId() {
        tablaTorneos.getColumnModel().getColumn(0).setMinWidth(0);
        tablaTorneos.getColumnModel().getColumn(0).setMaxWidth(0);
        tablaTorneos.getColumnModel().getColumn(0).setPreferredWidth(0);
    }
}