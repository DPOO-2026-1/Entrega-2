package InterfazGrafica;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import Persistencia.GestorPersistencia;
import Usuario.Cliente;
import World.Cafeteria;

public class PanelCliente extends JPanel {

    private CardLayout layoutInterno;
    private JPanel contenedor;
    private JPanel panelHome;
    private PanelTorneosDisponibles panelTorneosDisponibles;
    private PanelMiPerfil panelMiPerfil;

    private Runnable accionCerrarSesion;

    public PanelCliente() {
        setLayout(new CardLayout());
        setBackground(EstiloUI.COLOR_FONDO_BEIGE);

        layoutInterno = new CardLayout();
        contenedor = new JPanel(layoutInterno);
        add(contenedor, "contenedor");

        panelHome = crearHome();
        panelTorneosDisponibles = new PanelTorneosDisponibles();
        panelMiPerfil = new PanelMiPerfil();

        panelTorneosDisponibles.setAccionVolver(() -> layoutInterno.show(contenedor, "home"));
        panelMiPerfil.setAccionVolver(() -> layoutInterno.show(contenedor, "home"));

        contenedor.add(panelHome, "home");
        contenedor.add(panelTorneosDisponibles, "torneos");
        contenedor.add(panelMiPerfil, "perfil");
    }

    private JPanel crearHome() {
        JPanel panel = new JPanel(null);
        panel.setBackground(EstiloUI.COLOR_FONDO_BEIGE);

        JLabel titulo = new JLabel("Board Game Cafe - Panel Cliente");
        titulo.setOpaque(true);
        titulo.setBackground(EstiloUI.COLOR_BANNER_CAFE);
        titulo.setForeground(Color.WHITE);
        titulo.setFont(new Font("Arial", Font.BOLD, 24));
        titulo.setBounds(0, 0, 1280, 60);
        panel.add(titulo);

        JButton btnTorneos = crearBoton("Torneos Disponibles");
        btnTorneos.setBounds(180, 130, 180, 55);
        panel.add(btnTorneos);

        JButton btnPerfil = crearBoton("Mi perfil");
        btnPerfil.setBounds(550, 130, 180, 55);
        panel.add(btnPerfil);

        JButton btnCerrar = crearBoton("Cerrar Sesión");
        btnCerrar.setBounds(920, 130, 180, 55);
        panel.add(btnCerrar);

        JLabel tarjeta = new JLabel(
                "<html><div style='text-align:center;'>Bienvenido<br><br>"
                        + "Desde aquí puedes consultar<br>"
                        + "torneos, inscribirte o revisar tu<br>"
                        + "perfil en la cafetería</div></html>");
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

        btnPerfil.addActionListener(e -> {
            panelMiPerfil.refrescar();
            layoutInterno.show(contenedor, "perfil");
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

    public void configurarContexto(Cafeteria cafeteria, Cliente cliente, GestorPersistencia persistencia) {
        panelTorneosDisponibles.configurarContexto(cafeteria, cliente, persistencia);
        panelMiPerfil.configurarContexto(cafeteria, cliente);
        layoutInterno.show(contenedor, "home");
    }

    public void setAccionCerrarSesion(Runnable accionCerrarSesion) {
        this.accionCerrarSesion = accionCerrarSesion;
    }
}