package InterfazGrafica;

import java.awt.Color;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import Torneo.InscripcionTorneo;
import Torneo.Torneo;
import Usuario.Cliente;
import Usuario.Usuario;
import World.Cafeteria;

public class PanelMiPerfil extends JPanel {

    private Cafeteria cafeteria;
    private Cliente cliente;

    private JTable tablaPerfil;
    private JTable tablaTorneo;
    private DefaultTableModel modeloPerfil;
    private DefaultTableModel modeloTorneo;
    private JComboBox<String> comboTorneos;
    private JButton btnVolver;

    public PanelMiPerfil() {
        setLayout(null);
        setBackground(EstiloUI.COLOR_FONDO_BEIGE);

        JLabel titulo = new JLabel("Board Game Cafe - Mi Perfil");
        titulo.setOpaque(true);
        titulo.setBackground(EstiloUI.COLOR_BANNER_CAFE);
        titulo.setForeground(Color.WHITE);
        titulo.setFont(new Font("Arial", Font.BOLD, 24));
        titulo.setBounds(0, 0, 1280, 60);
        add(titulo);

        JLabel bloqueTitulo = new JLabel("Info de Mi perfil");
        bloqueTitulo.setOpaque(true);
        bloqueTitulo.setHorizontalAlignment(JLabel.CENTER);
        bloqueTitulo.setBackground(EstiloUI.COLOR_RECTANGULO_TEXTO);
        bloqueTitulo.setForeground(Color.WHITE);
        bloqueTitulo.setFont(new Font("Arial", Font.PLAIN, 24));
        bloqueTitulo.setBounds(470, 120, 300, 70);
        add(bloqueTitulo);

        modeloPerfil = new DefaultTableModel(new Object[] { "Info", "Datos" }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaPerfil = crearTabla(modeloPerfil);
        JScrollPane scrollPerfil = new JScrollPane(tablaPerfil);
        scrollPerfil.setBounds(130, 250, 460, 190);
        add(scrollPerfil);

        comboTorneos = new JComboBox<String>();
        comboTorneos.setBackground(EstiloUI.COLOR_COMPONENTE_CAFE);
        comboTorneos.setForeground(Color.WHITE);
        comboTorneos.setBounds(660, 250, 200, 30);
        add(comboTorneos);

        modeloTorneo = new DefaultTableModel(new Object[] { "Info", "Datos Torneo" }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaTorneo = crearTabla(modeloTorneo);
        JScrollPane scrollTorneo = new JScrollPane(tablaTorneo);
        scrollTorneo.setBounds(660, 300, 460, 150);
        add(scrollTorneo);

        btnVolver = crearBoton("Volver");
        btnVolver.setBounds(20, 610, 115, 35);
        add(btnVolver);

        comboTorneos.addActionListener(e -> refrescarTorneoSeleccionado());
    }

    private JTable crearTabla(DefaultTableModel modelo) {
        JTable tabla = new JTable(modelo);
        tabla.setBackground(EstiloUI.COLOR_COMPONENTE_CAFE);
        tabla.setForeground(Color.WHITE);
        tabla.setGridColor(EstiloUI.COLOR_TEXTO_OSCURO);
        tabla.setRowHeight(38);
        tabla.getTableHeader().setBackground(EstiloUI.COLOR_RECTANGULO_TEXTO);
        tabla.getTableHeader().setForeground(Color.WHITE);
        return tabla;
    }

    private JButton crearBoton(String texto) {
        JButton boton = new JButton(texto);
        boton.setBackground(EstiloUI.COLOR_COMPONENTE_CAFE);
        boton.setForeground(Color.WHITE);
        boton.setFocusPainted(false);
        boton.setBorder(BorderFactory.createEmptyBorder());
        return boton;
    }

    public void configurarContexto(Cafeteria cafeteria, Cliente cliente) {
        this.cafeteria = cafeteria;
        this.cliente = cliente;
        refrescar();
    }

    public void setAccionVolver(Runnable accionVolver) {
        for (java.awt.event.ActionListener listener : btnVolver.getActionListeners()) {
            btnVolver.removeActionListener(listener);
        }

        btnVolver.addActionListener(e -> accionVolver.run());
    }

    public void refrescar() {
        modeloPerfil.setRowCount(0);
        modeloTorneo.setRowCount(0);
        comboTorneos.removeAllItems();

        if (cliente == null) {
            return;
        }

        modeloPerfil.addRow(new Object[] { "Nombre", cliente.getNombre() });
        modeloPerfil.addRow(new Object[] { "login", cliente.getLogin() });
        modeloPerfil.addRow(new Object[] { "Tipo", obtenerTipoCliente() });
        modeloPerfil.addRow(new Object[] { "Puntos Fidelidad", cliente.getPuntosFidelidad() });
        modeloPerfil.addRow(new Object[] { "Juegos Reservados", cliente.getJuegosReservados() });

        cargarTorneosDelCliente();
        refrescarTorneoSeleccionado();
    }

    private String obtenerTipoCliente() {
        if (cliente.isEsNinio()) {
            return "Niño";
        }

        if (cliente.isEsJoven()) {
            return "Joven";
        }

        return "Adulto";
    }

    private void cargarTorneosDelCliente() {
        if (cafeteria == null || cafeteria.getGestorTorneo() == null) {
            return;
        }

        for (Torneo torneo : cafeteria.getGestorTorneo().getCatalogoTorneos()) {
            if (buscarInscripcion(torneo, cliente) != null) {
                comboTorneos.addItem(torneo.getIdTorneo() + " - " + torneo.getNombre());
            }
        }

        if (comboTorneos.getItemCount() == 0) {
            comboTorneos.addItem("Sin torneos inscritos");
        }
    }

    private void refrescarTorneoSeleccionado() {
        modeloTorneo.setRowCount(0);

        if (comboTorneos.getSelectedItem() == null || cafeteria == null || cafeteria.getGestorTorneo() == null) {
            return;
        }

        String seleccionado = comboTorneos.getSelectedItem().toString();

        if (seleccionado.equals("Sin torneos inscritos")) {
            modeloTorneo.addRow(new Object[] { "Estado", "No hay torneos" });
            return;
        }

        String idTorneo = seleccionado.split(" - ")[0];
        Torneo torneo = cafeteria.getGestorTorneo().buscarTorneo(idTorneo);

        if (torneo != null) {
            InscripcionTorneo inscripcion = buscarInscripcion(torneo, cliente);

            modeloTorneo.addRow(new Object[] { "Día", torneo.getDia() });
            modeloTorneo.addRow(new Object[] { "Estado", torneo.getEstado() });
            modeloTorneo.addRow(new Object[] { "Juego", torneo.getJuegoTorneo() == null ? "-" : torneo.getJuegoTorneo().getNombre() });
            modeloTorneo.addRow(new Object[] { "Cupos", inscripcion == null ? "-" : inscripcion.getCantidadCupos() });
        }
    }

    private InscripcionTorneo buscarInscripcion(Torneo torneo, Usuario usuario) {
        if (torneo == null || usuario == null) {
            return null;
        }

        for (InscripcionTorneo inscripcion : torneo.getInscripciones()) {
            for (Usuario inscrito : inscripcion.getUsuarios()) {
                if (inscrito != null && usuario.getLogin().equals(inscrito.getLogin())) {
                    return inscripcion;
                }
            }
        }

        return null;
    }
}