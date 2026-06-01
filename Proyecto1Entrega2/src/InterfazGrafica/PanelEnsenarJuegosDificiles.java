package InterfazGrafica;

import java.awt.Color;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import Persistencia.GestorPersistencia;
import Usuario.Empleado;
import Usuario.Mesero;
import World.Cafeteria;
import World.Juego;

public class PanelEnsenarJuegosDificiles extends JPanel {

    private Cafeteria cafeteria;
    private Empleado empleado;
    private GestorPersistencia persistencia;

    private JComboBox<Juego> comboJuegos;
    private JButton btnRegistrarCapacitacion;
    private JButton btnEnsenar;
    private JButton btnVolver;
    private JLabel lblEstado;

    public PanelEnsenarJuegosDificiles() {
        setLayout(null);
        setBackground(EstiloUI.COLOR_FONDO_BEIGE);

        JLabel titulo = new JLabel("  Board Game Cafe - Enseñar Juegos Difíciles");
        titulo.setOpaque(true);
        titulo.setBackground(EstiloUI.COLOR_BANNER_CAFE);
        titulo.setForeground(Color.WHITE);
        titulo.setFont(new Font("Arial", Font.BOLD, 24));
        titulo.setBounds(0, 0, 1280, 60);
        add(titulo);

        JLabel lblJuego = new JLabel("Juego difícil", JLabel.CENTER);
        lblJuego.setOpaque(true);
        lblJuego.setBackground(EstiloUI.COLOR_RECTANGULO_TEXTO);
        lblJuego.setForeground(Color.WHITE);
        lblJuego.setFont(new Font("Arial", Font.BOLD, 14));
        lblJuego.setBounds(330, 155, 160, 35);
        add(lblJuego);

        comboJuegos = new JComboBox<Juego>();
        comboJuegos.setBackground(EstiloUI.COLOR_COMPONENTE_CAFE);
        comboJuegos.setForeground(Color.WHITE);
        comboJuegos.setBounds(520, 155, 330, 35);
        add(comboJuegos);

        btnRegistrarCapacitacion = boton("Registrar capacitación");
        btnRegistrarCapacitacion.setBounds(340, 250, 220, 50);
        add(btnRegistrarCapacitacion);

        btnEnsenar = boton("Enseñar reglas");
        btnEnsenar.setBounds(650, 250, 220, 50);
        add(btnEnsenar);

        lblEstado = new JLabel("Estado: selecciona un juego difícil");
        lblEstado.setOpaque(true);
        lblEstado.setForeground(Color.WHITE);
        lblEstado.setBackground(EstiloUI.COLOR_COMPONENTE_CAFE);
        lblEstado.setBounds(340, 360, 530, 45);
        add(lblEstado);

        btnVolver = boton("Volver");
        btnVolver.setBounds(20, 620, 110, 35);
        add(btnVolver);

        btnRegistrarCapacitacion.addActionListener(e -> registrarCapacitacion());
        btnEnsenar.addActionListener(e -> ensenarReglas());
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
        comboJuegos.removeAllItems();

        if (cafeteria != null && cafeteria.getJuegos() != null) {
            for (Juego juego : cafeteria.getJuegos()) {
                if (juego != null && juego.isEsDificil()) {
                    comboJuegos.addItem(juego);
                }
            }
        }

        lblEstado.setText(empleado instanceof Mesero
                ? "Estado: mesero cargado"
                : "Estado: solo meseros pueden enseñar juegos difíciles");
    }

    private void registrarCapacitacion() {
        try {
            if (!(empleado instanceof Mesero)) {
                throw new IllegalStateException("Solo un mesero puede registrarse como capacitado.");
            }

            Juego juego = (Juego) comboJuegos.getSelectedItem();

            if (juego == null) {
                throw new IllegalArgumentException("Selecciona un juego difícil.");
            }

            ((Mesero) empleado).agregarJuegoConocido(juego);

            if (persistencia != null && cafeteria != null) {
                persistencia.guardarTodo(cafeteria);
            }

            lblEstado.setText("Estado: capacitación registrada para " + juego.getNombre());
            JOptionPane.showMessageDialog(this, "Ahora el mesero puede enseñar: " + juego.getNombre());

        } catch (Exception ex) {
            lblEstado.setText("Estado: " + ex.getMessage());
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void ensenarReglas() {
        try {
            if (!(empleado instanceof Mesero)) {
                throw new IllegalStateException("Solo los meseros pueden enseñar juegos difíciles.");
            }

            Juego juego = (Juego) comboJuegos.getSelectedItem();

            if (juego == null) {
                throw new IllegalArgumentException("Selecciona un juego difícil.");
            }

            Mesero mesero = (Mesero) empleado;

            if (!mesero.puedeEnsenar(juego)) {
                throw new IllegalStateException("Este mesero no está capacitado para enseñar ese juego.");
            }

            JOptionPane.showMessageDialog(this,
                    "El mesero " + mesero.getNombre() + " puede enseñar las reglas de: " + juego.getNombre(),
                    "Reglas del juego",
                    JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception ex) {
            lblEstado.setText("Estado: " + ex.getMessage());
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}