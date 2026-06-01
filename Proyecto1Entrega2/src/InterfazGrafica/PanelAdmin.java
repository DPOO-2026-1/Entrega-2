package InterfazGrafica;

import javax.swing.*;
import java.awt.*;

public class PanelAdmin extends JPanel {
    private JPanel panelCentral;
    private CardLayout cardLayout;
    
    // Subpaneles
    private PanelRegistrarEmpleado panelRegistrar;
    private PanelCrearTorneo panelCrearTorneo;
    private PanelGestionarTorneos panelGestionar;
    private PanelVisualizaciones panelVisualizaciones;
    
    private PanelInventarioAdmin panelInventarioAdmin;
    private PanelMenuAdmin panelMenuAdmin;
    private PanelTurnosAdmin panelTurnosAdmin;
    
    // Botón para salir
    private JButton btnCerrarSesion;
    private JButton btnDashboard;

    public PanelAdmin() {
        setLayout(new BorderLayout());
        setBackground(EstiloUI.COLOR_FONDO_BEIGE);

        // Navegación (Izquierda)
        JPanel panelNav = new JPanel();
        panelNav.setLayout(new BoxLayout(panelNav, BoxLayout.Y_AXIS));
        panelNav.setBackground(EstiloUI.COLOR_RECTANGULO_TEXTO);
        panelNav.setPreferredSize(new Dimension(220, 0));

        JButton btnRegistrar = crearBotonNav("Registrar Empleado");
        JButton btnTorneo = crearBotonNav("Crear Torneo");
        JButton btnGestionar = crearBotonNav("Gestionar Torneos");
        JButton btnDashboard = crearBotonNav("Dashboard Gráficos");
        JButton btnInventario = crearBotonNav("Inventario");
        JButton btnMenu = crearBotonNav("Menú/Sugerencias");
        JButton btnTurnos = crearBotonNav("Turnos");
        btnCerrarSesion = crearBotonNav("Cerrar Sesión");	
        btnCerrarSesion.setBackground(new Color(150, 45, 35));

        panelNav.add(Box.createVerticalStrut(20));
        panelNav.add(btnRegistrar);
        panelNav.add(btnTorneo);
        panelNav.add(btnGestionar);
        panelNav.add(btnDashboard);
        panelNav.add(btnInventario);
        panelNav.add(btnMenu);
        panelNav.add(btnTurnos);
        panelNav.add(Box.createVerticalGlue()); 
        panelNav.add(btnCerrarSesion);

        // Contenedor Central
        cardLayout = new CardLayout();
        panelCentral = new JPanel(cardLayout);
        panelCentral.setBackground(EstiloUI.COLOR_FONDO_BEIGE);

        // Instanciar subpaneles
        panelRegistrar = new PanelRegistrarEmpleado();
        panelCrearTorneo = new PanelCrearTorneo();
        panelGestionar = new PanelGestionarTorneos();
        panelVisualizaciones = new PanelVisualizaciones();
        panelInventarioAdmin = new PanelInventarioAdmin();
        panelMenuAdmin = new PanelMenuAdmin();
        panelTurnosAdmin = new PanelTurnosAdmin();

        panelCentral.add(panelRegistrar, "REGISTRAR");
        panelCentral.add(panelCrearTorneo, "CREAR_TORNEO");
        panelCentral.add(panelGestionar, "GESTIONAR");
        panelCentral.add(panelVisualizaciones, "DASHBOARD");
        panelCentral.add(panelInventarioAdmin, "INVENTARIO");
        panelCentral.add(panelMenuAdmin, "MENU");
        panelCentral.add(panelTurnosAdmin, "TURNOS");
        
        // Navegación INTERNA (Esto se queda en la vista porque es puramente estético)
        btnRegistrar.addActionListener(e -> cardLayout.show(panelCentral, "REGISTRAR"));
        btnTorneo.addActionListener(e -> cardLayout.show(panelCentral, "CREAR_TORNEO"));
        btnGestionar.addActionListener(e -> cardLayout.show(panelCentral, "GESTIONAR"));
        btnDashboard.addActionListener(e -> cardLayout.show(panelCentral, "DASHBOARD"));
        btnInventario.addActionListener(e -> cardLayout.show(panelCentral, "INVENTARIO"));
        btnMenu.addActionListener(e -> cardLayout.show(panelCentral, "MENU"));
        btnTurnos.addActionListener(e -> cardLayout.show(panelCentral, "TURNOS"));

        add(panelNav, BorderLayout.WEST);
        add(panelCentral, BorderLayout.CENTER);
    }

    private JButton crearBotonNav(String texto) {
        JButton btn = new JButton(texto);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        btn.setForeground(Color.WHITE);
        
        // CAMBIO IMPLEMENTADO: botones admin con la misma paleta café/beige 
        btn.setBackground(EstiloUI.COLOR_COMPONENTE_CAFE);
        btn.setFont(EstiloUI.FUENTE_ETIQUETA);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setOpaque(true);
        return btn;
    }

    // --- GETTERS PARA QUE EL CONTROLADOR ACCEDA A LOS COMPONENTES ---
    public PanelRegistrarEmpleado getPanelRegistrar() { return panelRegistrar; }
    public PanelCrearTorneo getPanelCrearTorneo() { return panelCrearTorneo; }
    public PanelGestionarTorneos getPanelGestionar() { return panelGestionar; }
    public PanelVisualizaciones getPanelVisualizaciones() { return panelVisualizaciones; }
    
    public PanelInventarioAdmin getPanelInventarioAdmin() {
        return panelInventarioAdmin;
    }

    public PanelMenuAdmin getPanelMenuAdmin() {
        return panelMenuAdmin;
    }

    public PanelTurnosAdmin getPanelTurnosAdmin() {
        return panelTurnosAdmin;
    }
    
    public JButton getBtnCerrarSesion() { return btnCerrarSesion; }
    public JPanel getPanelCentral() { return panelCentral; }
    public CardLayout getCardLayout() { return cardLayout; }
}