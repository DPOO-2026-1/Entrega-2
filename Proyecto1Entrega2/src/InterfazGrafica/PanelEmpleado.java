package InterfazGrafica;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import Persistencia.GestorPersistencia;
import Usuario.Empleado;
import World.Cafeteria;

public class PanelEmpleado extends JPanel {

    private CardLayout layoutInterno;
    private JPanel contenedor;
    private JPanel panelHome;
    private PanelTorneosDisponibles panelTorneosDisponibles;
    private PanelTurnosEmpleado panelTurnosEmpleado;

    private Runnable accionCerrarSesion;

    public PanelEmpleado() {
        setLayout(new CardLayout());
        setBackground(EstiloUI.COLOR_FONDO_BEIGE);

        layoutInterno = new CardLayout();
        contenedor = new JPanel(layoutInterno);
        add(contenedor, "contenedor");

        panelHome = crearHome();
        panelTorneosDisponibles = new PanelTorneosDisponibles();
        panelTurnosEmpleado = new PanelTurnosEmpleado();

        panelTorneosDisponibles.setAccionVolver(() -> layoutInterno.show(contenedor, "home"));
        panelTurnosEmpleado.setAccionVolver(() -> layoutInterno.show(contenedor, "home"));

        contenedor.add(panelHome, "home");
        contenedor.add(panelTorneosDisponibles, "torneos");
        contenedor.add(panelTurnosEmpleado, "turnos");
    }

    private JPanel crearHome() {
        JPanel panel = new JPanel(null);
        panel.setBackground(EstiloUI.COLOR_FONDO_BEIGE);

        JLabel titulo = new JLabel("Board Game Cafe - Panel Empleado");
        titulo.setOpaque(true);
        titulo.setBackground(EstiloUI.COLOR_BANNER_CAFE);
        titulo.setForeground(Color.WHITE);
        titulo.setFont(new Font("Arial", Font.BOLD, 24));
        titulo.setBounds(0, 0, 1280, 60);
        panel.add(titulo);

        JButton btnTorneos = crearBoton("Torneos Disponibles");
        btnTorneos.setBounds(180, 130, 180, 55);
        panel.add(btnTorneos);

        JButton btnTurnos = crearBoton("Mis turnos");
        btnTurnos.setBounds(550, 130, 180, 55);
        panel.add(btnTurnos);

        JButton btnCerrar = crearBoton("Cerrar Sesión");
        btnCerrar.setBounds(920, 130, 180, 55);
        panel.add(btnCerrar);

        JLabel tarjeta = new JLabel(
                "<html><div style='text-align:center;'>Bienvenido<br><br>"
                        + "Desde aquí puedes consultar<br>"
                        + "torneos, inscribirte o revisar tus<br>"
                        + "turnos asignados</div></html>");
        tarjeta.setOpaque(true);
        tarjeta.setBackground(EstiloUI.COLOR_RECTANGULO_TEXTO);
        tarjeta.setForeground(Color.WHITE);
        tarjeta.setHorizontalAlignment(JLabel.CENTER);
        tarjeta.setFont(new Font("Arial", Font.PLAIN, 24));
        tarjeta.setBounds(420, 260, 440, 170);
        panel.add(tarjeta);

        btnTorneos.addActionListener(e -> {
            panelTorneosDisponibles.refrescarTabla();
            layoutInterno.show(contenedor, "torneos");
        });

        btnTurnos.addActionListener(e -> {
            panelTurnosEmpleado.refrescar();
            layoutInterno.show(contenedor, "turnos");
        });

        btnCerrar.addActionListener(e -> {
            if (accionCerrarSesion != null) {
                accionCerrarSesion.run();
            }
        });

        return panel;
    }

    private JButton crearBoton(String texto) {
        JButton boton = new JButton(texto);
        boton.setBackground(EstiloUI.COLOR_COMPONENTE_CAFE);
        boton.setForeground(Color.WHITE);
        boton.setFocusPainted(false);
        boton.setBorder(BorderFactory.createEmptyBorder());
        return boton;
    }

    public void configurarContexto(Cafeteria cafeteria, Empleado empleado, GestorPersistencia persistencia) {
        panelTorneosDisponibles.configurarContexto(cafeteria, empleado, persistencia);
        panelTurnosEmpleado.configurarContexto(cafeteria, empleado);
        layoutInterno.show(contenedor, "home");
    }

    public void setAccionCerrarSesion(Runnable accionCerrarSesion) {
        this.accionCerrarSesion = accionCerrarSesion;
    }
}