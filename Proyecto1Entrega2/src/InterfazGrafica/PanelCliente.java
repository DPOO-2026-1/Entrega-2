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

    private PanelPrestamosCliente panelPrestamos;
    private PanelComprasCliente panelCompras;
    private PanelTorneosDisponibles panelTorneos;
    private PanelFavoritos panelFavoritos;
    private PanelMiPerfil panelPerfil;

    private Runnable accionCerrarSesion;

    public PanelCliente() {
        setLayout(new CardLayout());

        layoutInterno = new CardLayout();
        contenedor = new JPanel(layoutInterno);
        add(contenedor, "contenedor");

        panelPrestamos = new PanelPrestamosCliente();
        panelCompras = new PanelComprasCliente();
        panelTorneos = new PanelTorneosDisponibles();
        panelFavoritos = new PanelFavoritos();
        panelPerfil = new PanelMiPerfil();

        panelPrestamos.setAccionVolver(() -> layoutInterno.show(contenedor, "home"));
        panelCompras.setAccionVolver(() -> layoutInterno.show(contenedor, "home"));
        panelTorneos.setAccionVolver(() -> layoutInterno.show(contenedor, "home"));
        panelFavoritos.setAccionVolver(() -> layoutInterno.show(contenedor, "home"));
        panelPerfil.setAccionVolver(() -> layoutInterno.show(contenedor, "home"));

        contenedor.add(crearHome(), "home");
        contenedor.add(panelPrestamos, "prestamos");
        contenedor.add(panelCompras, "compras");
        contenedor.add(panelTorneos, "torneos");
        contenedor.add(panelFavoritos, "favoritos");
        contenedor.add(panelPerfil, "perfil");
    }

    private JPanel crearHome() {
        JPanel panel = new JPanel(null);
        panel.setBackground(EstiloUI.COLOR_FONDO_BEIGE);

        JLabel titulo = new JLabel("  Board Game Cafe - Panel Cliente");
        titulo.setOpaque(true);
        titulo.setBackground(EstiloUI.COLOR_BANNER_CAFE);
        titulo.setForeground(Color.WHITE);
        titulo.setFont(new Font("Arial", Font.BOLD, 24));
        titulo.setBounds(0, 0, 1280, 60);
        panel.add(titulo);

        JButton btnTorneos = boton("Torneos Disponibles");
        btnTorneos.setBounds(210, 130, 180, 50);
        panel.add(btnTorneos);

        JButton btnPerfil = boton("Mi perfil");
        btnPerfil.setBounds(530, 130, 180, 50);
        panel.add(btnPerfil);

        JButton btnCerrar = boton("Cerrar Sesión");
        btnCerrar.setBounds(850, 130, 180, 50);
        panel.add(btnCerrar);

        JLabel tarjeta = new JLabel(
                "<html><div style='text-align:center;'>"
                        + "Bienvenido<br><br>"
                        + "Desde aquí puedes consultar<br>"
                        + "torneos, inscribirte o revisar tu<br>"
                        + "perfil en la cafetería. También<br>"
                        + "puedes hacer préstamos de<br>"
                        + "juegos, comprarlos o marcarlos<br>"
                        + "como favoritos"
                        + "</div></html>",
                JLabel.CENTER);

        tarjeta.setOpaque(true);
        tarjeta.setBackground(EstiloUI.COLOR_RECTANGULO_TEXTO);
        tarjeta.setForeground(Color.WHITE);
        tarjeta.setFont(new Font("Arial", Font.PLAIN, 22));
        tarjeta.setBounds(360, 250, 560, 230);
        panel.add(tarjeta);

        JButton btnPrestamos = boton("Préstamos");
        btnPrestamos.setBounds(210, 510, 180, 50);
        panel.add(btnPrestamos);

        JButton btnCompras = boton("Compras");
        btnCompras.setBounds(530, 510, 180, 50);
        panel.add(btnCompras);

        JButton btnFavoritos = boton("Favoritos");
        btnFavoritos.setBounds(850, 510, 180, 50);
        panel.add(btnFavoritos);

        btnTorneos.addActionListener(e -> {
            panelTorneos.refrescarTabla();
            layoutInterno.show(contenedor, "torneos");
        });

        btnPerfil.addActionListener(e -> {
            panelPerfil.refrescar();
            layoutInterno.show(contenedor, "perfil");
        });

        btnPrestamos.addActionListener(e -> {
            panelPrestamos.refrescar();
            layoutInterno.show(contenedor, "prestamos");
        });

        btnCompras.addActionListener(e -> {
            panelCompras.refrescar();
            layoutInterno.show(contenedor, "compras");
        });

        btnFavoritos.addActionListener(e -> {
            panelFavoritos.refrescar();
            layoutInterno.show(contenedor, "favoritos");
        });

        btnCerrar.addActionListener(e -> {
            if (accionCerrarSesion != null) {
                accionCerrarSesion.run();
            }
        });

        return panel;
    }

    private JButton boton(String texto) {
        JButton boton = new JButton(texto);
        boton.setBackground(EstiloUI.COLOR_COMPONENTE_CAFE);
        boton.setForeground(Color.WHITE);
        boton.setFocusPainted(false);
        boton.setBorder(BorderFactory.createEmptyBorder());
        return boton;
    }

    public void configurarContexto(Cafeteria cafeteria, Cliente cliente, GestorPersistencia persistencia) {
        panelPrestamos.configurarContexto(cafeteria, cliente, persistencia);
        panelCompras.configurarContexto(cafeteria, cliente, persistencia);
        panelTorneos.configurarContexto(cafeteria, cliente, persistencia);
        panelFavoritos.configurarContexto(cafeteria, cliente, persistencia);
        panelPerfil.configurarContexto(cafeteria, cliente);

        layoutInterno.show(contenedor, "home");
    }

    public void setAccionCerrarSesion(Runnable accionCerrarSesion) {
        this.accionCerrarSesion = accionCerrarSesion;
    }
}