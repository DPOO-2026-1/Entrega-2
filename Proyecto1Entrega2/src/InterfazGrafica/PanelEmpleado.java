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

    private PanelAlquilerEmpleado panelAlquiler;
    private PanelComprasEmpleado panelCompras;
    private PanelTorneosDisponibles panelTorneos;
    private PanelTurnosEmpleado panelTurnos;
    private PanelSugerirMenu panelSugerir;
    private PanelFavoritos panelFavoritos;
    private PanelCajaMesero panelCajaMesero;
    private PanelEnsenarJuegosDificiles panelEnsenarJuegos;
    private Empleado empleadoActual;

    private Runnable accionCerrarSesion;

    public PanelEmpleado() {
        setLayout(new CardLayout());

        layoutInterno = new CardLayout();
        contenedor = new JPanel(layoutInterno);
        add(contenedor, "contenedor");

        panelAlquiler = new PanelAlquilerEmpleado();
        panelCompras = new PanelComprasEmpleado();
        panelTorneos = new PanelTorneosDisponibles();
        panelTurnos = new PanelTurnosEmpleado();
        panelSugerir = new PanelSugerirMenu();
        panelFavoritos = new PanelFavoritos();
        // =====================================================
        // CAMBIO NUEVO PROYECTO 3 - Instancia de paneles nuevos.
        // =====================================================
        panelCajaMesero = new PanelCajaMesero();
        panelEnsenarJuegos = new PanelEnsenarJuegosDificiles();

        panelAlquiler.setAccionVolver(() -> layoutInterno.show(contenedor, "home"));
        panelCompras.setAccionVolver(() -> layoutInterno.show(contenedor, "home"));
        panelTorneos.setAccionVolver(() -> layoutInterno.show(contenedor, "home"));
        panelTurnos.setAccionVolver(() -> layoutInterno.show(contenedor, "home"));
        panelSugerir.setAccionVolver(() -> layoutInterno.show(contenedor, "home"));
        panelFavoritos.setAccionVolver(() -> layoutInterno.show(contenedor, "home"));
        // =====================================================
        // CAMBIO NUEVO PROYECTO 3 - Volver desde caja y enseñanza.
        // =====================================================
        panelCajaMesero.setAccionVolver(() -> layoutInterno.show(contenedor, "home"));
        panelEnsenarJuegos.setAccionVolver(() -> layoutInterno.show(contenedor, "home"));

        contenedor.add(crearHome(), "home");
        contenedor.add(panelAlquiler, "alquiler");
        contenedor.add(panelCompras, "compras");
        contenedor.add(panelTorneos, "torneos");
        contenedor.add(panelTurnos, "turnos");
        contenedor.add(panelSugerir, "sugerir");
        contenedor.add(panelFavoritos, "favoritos");
        // =====================================================
        // CAMBIO NUEVO PROYECTO 3 - Cards nuevas para meseros.
        // =====================================================
        contenedor.add(panelCajaMesero, "caja");
     	contenedor.add(panelEnsenarJuegos, "ensenar");
    }

    private JPanel crearHome() {
        JPanel panel = new JPanel(null);
        panel.setBackground(EstiloUI.COLOR_FONDO_BEIGE);

        JLabel titulo = new JLabel("  Board Game Cafe - Panel Empleado");
        titulo.setOpaque(true);
        titulo.setBackground(EstiloUI.COLOR_BANNER_CAFE);
        titulo.setForeground(Color.WHITE);
        titulo.setFont(new Font("Arial", Font.BOLD, 24));
        titulo.setBounds(0, 0, 1280, 60);
        panel.add(titulo);

        JButton btnTorneos = boton("Torneos Disponibles");
        btnTorneos.setBounds(170, 125, 180, 50);
        panel.add(btnTorneos);

        JButton btnTurnos = boton("Mis turnos");
        btnTurnos.setBounds(500, 125, 180, 50);
        panel.add(btnTurnos);

        JButton btnCerrar = boton("Cerrar Sesión");
        btnCerrar.setBounds(830, 125, 180, 50);
        panel.add(btnCerrar);

        JLabel tarjeta = new JLabel(
                "<html><div style='text-align:center;'>"
                        + "Bienvenido<br><br>"
                        + "Desde aquí puedes consultar torneos,<br>"
                        + "inscribirte o revisar tus turnos asignados.<br>"
                        + "También puedes alquilar juegos, comprarlos,<br>"
                        + "sugerir un plato para el menú o marcar juegos<br>"
                        + "como favoritos"
                        + "</div></html>",
                JLabel.CENTER);

        tarjeta.setOpaque(true);
        tarjeta.setBackground(EstiloUI.COLOR_RECTANGULO_TEXTO);
        tarjeta.setForeground(Color.WHITE);
        tarjeta.setFont(new Font("Arial", Font.PLAIN, 21));
        tarjeta.setBounds(300, 230, 680, 235);
        panel.add(tarjeta);

        JButton btnAlquiler = boton("Alquilar Juegos");
        btnAlquiler.setBounds(120, 510, 180, 50);
        panel.add(btnAlquiler);

        JButton btnCompras = boton("Compras");
        btnCompras.setBounds(400, 510, 180, 50);
        panel.add(btnCompras);

        JButton btnSugerir = boton("Sugerir Plato");
        btnSugerir.setBounds(680, 510, 180, 50);
        panel.add(btnSugerir);

        JButton btnFavoritos = boton("Favoritos");
        btnFavoritos.setBounds(960, 510, 180, 50);
        panel.add(btnFavoritos);
        
		// =====================================================
		// CAMBIO NUEVO PROYECTO 3 - Botones de funciones específicas de mesero.
		// =====================================================
		JButton btnCaja = boton("Caja Mesero");
		btnCaja.setBounds(400, 590, 180, 45);
		panel.add(btnCaja);
		
		JButton btnEnsenar = boton("Enseñar Juegos");
		btnEnsenar.setBounds(680, 590, 180, 45);
		panel.add(btnEnsenar);

        btnTorneos.addActionListener(e -> {
            panelTorneos.refrescarTabla();
            layoutInterno.show(contenedor, "torneos");
        });

        btnTurnos.addActionListener(e -> {
            panelTurnos.refrescar();
            layoutInterno.show(contenedor, "turnos");
        });

        btnAlquiler.addActionListener(e -> {
            panelAlquiler.refrescar();
            layoutInterno.show(contenedor, "alquiler");
        });

        btnCompras.addActionListener(e -> {
            panelCompras.refrescar();
            layoutInterno.show(contenedor, "compras");
        });

        btnSugerir.addActionListener(e -> {
            layoutInterno.show(contenedor, "sugerir");
        });

        btnFavoritos.addActionListener(e -> {
            panelFavoritos.refrescar();
            layoutInterno.show(contenedor, "favoritos");
        });
        
	    // =====================================================
	    // CAMBIO NUEVO PROYECTO 3 - Acciones de caja y enseñanza.
	    // =====================================================
	    btnCaja.addActionListener(e -> {
	        panelCajaMesero.refrescar();
	        layoutInterno.show(contenedor, "caja");
	    });
	
	    btnEnsenar.addActionListener(e -> {
	        panelEnsenarJuegos.refrescar();
	        layoutInterno.show(contenedor, "ensenar");
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

    public void configurarContexto(Cafeteria cafeteria, Empleado empleado, GestorPersistencia persistencia) {
        panelAlquiler.configurarContexto(cafeteria, empleado, persistencia);
        panelCompras.configurarContexto(cafeteria, empleado, persistencia);
        panelTorneos.configurarContexto(cafeteria, empleado, persistencia);
        panelTurnos.configurarContexto(cafeteria, empleado, persistencia);
        panelSugerir.configurarContexto(cafeteria, empleado, persistencia);
        panelFavoritos.configurarContexto(cafeteria, empleado, persistencia);
        // =====================================================
        // CAMBIO NUEVO PROYECTO 3 - Se pasan datos a los paneles nuevos.
        // =====================================================
        panelCajaMesero.configurarContexto(cafeteria, empleado, persistencia);
        panelEnsenarJuegos.configurarContexto(cafeteria, empleado, persistencia);

        layoutInterno.show(contenedor, "home");
    }

    public void setAccionCerrarSesion(Runnable accionCerrarSesion) {
        this.accionCerrarSesion = accionCerrarSesion;
    }
}