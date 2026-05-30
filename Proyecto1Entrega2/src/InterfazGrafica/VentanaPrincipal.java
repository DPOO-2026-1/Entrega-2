package InterfazGrafica;

import javax.swing.*;
import java.awt.*;

public class VentanaPrincipal extends JFrame {
    private CardLayout controlLayout;

    // Atributos para poder acceder a los paneles desde fuera
    private PanelOpciones panelOpciones;
    private PanelLogin panelLogin;
    private PanelOpcionesCliente panelOpcionesCliente;
    private PanelRegistrarCliente panelRegistrarCliente;

    public VentanaPrincipal() {
        super("Ventana Principal");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1280, 720);
        setLocationRelativeTo(null);
        // Para configurar los componentes en card layout
        controlLayout = new CardLayout();
        setLayout(controlLayout);
        configurarComponentes();
    }

    private void configurarComponentes() {
        panelOpciones = new PanelOpciones();
        panelLogin = new PanelLogin();
        panelOpcionesCliente = new PanelOpcionesCliente();
        panelRegistrarCliente = new PanelRegistrarCliente();

        this.add(panelOpciones, "PanelOpciones");
        this.add(panelLogin, "PanelLogin");
        this.add(panelOpcionesCliente, "PanelOpcionesCliente");
        this.add(panelRegistrarCliente, "PanelRegistrarCliente");
    }

    // Método para cambiar la pantalla visible mediante su nombre clave
    public void cambiarPantalla(String nombrePantalla) {
        controlLayout.show(this.getContentPane(), nombrePantalla);
    }

    // Getters para que el ControllerPrincipal obtenga los paneles
    public PanelOpciones getPanelOpciones() {
        return panelOpciones;
    }

    public PanelLogin getPanelLogin() {
        return panelLogin;
    }

    public PanelOpcionesCliente getPanelOpcionesCliente() {
        return panelOpcionesCliente;
    }

    public PanelRegistrarCliente getPanelPanelRegistrarCliente() {
        return panelRegistrarCliente;
    }

    public static void main(String[] args) {
        VentanaPrincipal ventana = new VentanaPrincipal();
        ventana.setVisible(true);
    }
}