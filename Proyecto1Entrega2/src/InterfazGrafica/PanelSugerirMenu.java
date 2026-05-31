package InterfazGrafica;

import java.awt.Color;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JOptionPane;

import Persistencia.GestorPersistencia;
import Usuario.Empleado;
import Usuario.SugerenciaMenu;
import World.Cafeteria;

public class PanelSugerirMenu extends JPanel {

    private Cafeteria cafeteria;
    private Empleado empleado;
    private GestorPersistencia persistencia;

    private JTextArea areaDescripcion;
    private JLabel lblEstado;
    private JButton btnEnviar;
    private JButton btnVolver;

    public PanelSugerirMenu() {
        setLayout(null);
        setBackground(EstiloUI.COLOR_FONDO_BEIGE);

        JLabel titulo = new JLabel("  Board Game Cafe - Sugerir Plato");
        titulo.setOpaque(true);
        titulo.setBackground(EstiloUI.COLOR_BANNER_CAFE);
        titulo.setForeground(Color.WHITE);
        titulo.setFont(new Font("Arial", Font.BOLD, 24));
        titulo.setBounds(0, 0, 1280, 60);
        add(titulo);

        JLabel lblDescripcion = new JLabel("Descripción de sugerencia", JLabel.CENTER);
        lblDescripcion.setOpaque(true);
        lblDescripcion.setBackground(EstiloUI.COLOR_RECTANGULO_TEXTO);
        lblDescripcion.setForeground(Color.WHITE);
        lblDescripcion.setFont(new Font("Arial", Font.BOLD, 14));
        lblDescripcion.setBounds(100, 100, 330, 35);
        add(lblDescripcion);

        areaDescripcion = new JTextArea();
        areaDescripcion.setLineWrap(true);
        areaDescripcion.setWrapStyleWord(true);
        areaDescripcion.setText("Detalle muy bien su sugerencia para que sea considerada de mejor forma");

        JScrollPane scroll = new JScrollPane(areaDescripcion);
        scroll.setBounds(315, 175, 580, 200);
        add(scroll);

        btnEnviar = boton("Enviar Sugerencia");
        btnEnviar.setBounds(500, 430, 180, 50);
        add(btnEnviar);

        btnVolver = boton("Volver");
        btnVolver.setBounds(20, 620, 110, 35);
        add(btnVolver);

        lblEstado = new JLabel("Estado: ...");
        lblEstado.setOpaque(true);
        lblEstado.setForeground(Color.WHITE);
        lblEstado.setBackground(EstiloUI.COLOR_COMPONENTE_CAFE);
        lblEstado.setBounds(200, 620, 720, 35);
        add(lblEstado);

        btnEnviar.addActionListener(e -> enviarSugerencia());
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
    }

    public void setAccionVolver(Runnable accionVolver) {
        for (java.awt.event.ActionListener listener : btnVolver.getActionListeners()) {
            btnVolver.removeActionListener(listener);
        }

        btnVolver.addActionListener(e -> accionVolver.run());
    }

    private void enviarSugerencia() {
        try {
            String descripcion = areaDescripcion.getText();

            SugerenciaMenu sugerencia = empleado.sugerirPlato(descripcion);

            if (persistencia != null && cafeteria != null) {
                persistencia.guardarTodo(cafeteria);
            }

            lblEstado.setText("Estado: sugerencia enviada como " + sugerencia.getEstado());
            areaDescripcion.setText("");

            JOptionPane.showMessageDialog(
                    this,
                    "Sugerencia enviada correctamente.",
                    "Éxito",
                    JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception ex) {
            lblEstado.setText("Estado: " + ex.getMessage());

            JOptionPane.showMessageDialog(
                    this,
                    ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}