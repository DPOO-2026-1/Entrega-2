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
    
    // Botón para salir
    private JButton btnCerrarSesion;

    public PanelAdmin() {
        setLayout(new BorderLayout());

        // Navegación (Izquierda)
        JPanel panelNav = new JPanel();
        panelNav.setLayout(new BoxLayout(panelNav, BoxLayout.Y_AXIS));
        panelNav.setBackground(new Color(44, 62, 80)); 
        panelNav.setPreferredSize(new Dimension(220, 0));

        JButton btnRegistrar = crearBotonNav("Registrar Empleado");
        JButton btnTorneo = crearBotonNav("Crear Torneo");
        JButton btnGestionar = crearBotonNav("Gestionar Torneos");
        JButton btnDashboard = crearBotonNav("Dashboard Gráficos");
        btnCerrarSesion = crearBotonNav("Cerrar Sesión");
        btnCerrarSesion.setBackground(new Color(192, 57, 43)); // Rojo para salir

        panelNav.add(Box.createVerticalStrut(20));
        panelNav.add(btnRegistrar);
        panelNav.add(btnTorneo);
        panelNav.add(btnGestionar);
        panelNav.add(btnDashboard);
        panelNav.add(Box.createVerticalGlue()); 
        panelNav.add(btnCerrarSesion);

        // Contenedor Central
        cardLayout = new CardLayout();
        panelCentral = new JPanel(cardLayout);

        // Instanciar subpaneles
        panelRegistrar = new PanelRegistrarEmpleado();
        panelCrearTorneo = new PanelCrearTorneo();
        panelGestionar = new PanelGestionarTorneos();
        panelVisualizaciones = new PanelVisualizaciones();

        panelCentral.add(panelRegistrar, "REGISTRAR");
        panelCentral.add(panelCrearTorneo, "CREAR_TORNEO");
        panelCentral.add(panelGestionar, "GESTIONAR");
        panelCentral.add(panelVisualizaciones, "DASHBOARD");

        // Navegación INTERNA (Esto se queda en la vista porque es puramente estético)
        btnRegistrar.addActionListener(e -> cardLayout.show(panelCentral, "REGISTRAR"));
        btnTorneo.addActionListener(e -> cardLayout.show(panelCentral, "CREAR_TORNEO"));
        btnGestionar.addActionListener(e -> cardLayout.show(panelCentral, "GESTIONAR"));
        btnDashboard.addActionListener(e -> cardLayout.show(panelCentral, "DASHBOARD"));

        add(panelNav, BorderLayout.WEST);
        add(panelCentral, BorderLayout.CENTER);
    }

    private JButton crearBotonNav(String texto) {
        JButton btn = new JButton(texto);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        btn.setForeground(Color.WHITE);
        btn.setBackground(new Color(52, 73, 94));
        btn.setFocusPainted(false);
        return btn;
    }

    // --- GETTERS PARA QUE EL CONTROLADOR ACCEDA A LOS COMPONENTES ---
    public PanelRegistrarEmpleado getPanelRegistrar() { return panelRegistrar; }
    public PanelCrearTorneo getPanelCrearTorneo() { return panelCrearTorneo; }
    public PanelGestionarTorneos getPanelGestionar() { return panelGestionar; }
    public PanelVisualizaciones getPanelVisualizaciones() { return panelVisualizaciones; }
    public JButton getBtnCerrarSesion() { return btnCerrarSesion; }
 // --- AGREGA ESTOS DOS MÉTODOS AQUÍ ---
    public JPanel getPanelCentral() { return panelCentral; }
    public CardLayout getCardLayout() { return cardLayout; }
}