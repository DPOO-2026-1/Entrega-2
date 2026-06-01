package InterfazGrafica;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import Controladores.ControllerPrincipal;

public class VentanaPrincipal extends JFrame {
    private CardLayout controlLayout;
    private Runnable accionGuardarAlCerrar;	

    // Atributos para poder acceder a los paneles desde fuera
    private PanelOpciones panelOpciones;
    private PanelLogin panelLogin;
    private PanelOpcionesCliente panelOpcionesCliente;
    private PanelRegistrarCliente panelRegistrarCliente;
    private PanelCliente panelCliente;
    private PanelEmpleado panelEmpleado;
    private PanelAdmin panelAdmin;

    public VentanaPrincipal() {
        super("Ventana Principal");
        // CAMBIO IMPLEMENTADO: ahora el cierre pasa por WindowListener para guardar antes de salir
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                guardarYSalir();
            }
        });
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
        panelCliente = new PanelCliente();
        panelEmpleado = new PanelEmpleado();
        panelAdmin = new PanelAdmin();

        this.add(panelOpciones, "PanelOpciones");
        this.add(panelLogin, "PanelLogin");
        this.add(panelOpcionesCliente, "PanelOpcionesCliente");
        this.add(panelRegistrarCliente, "PanelRegistrarCliente");
        this.add(panelCliente, "PanelCliente");
        this.add(panelEmpleado, "PanelEmpleado");
        this.add(panelAdmin, "PanelAdmin");
    }

    // Método para cambiar la pantalla visible mediante su nombre clave
    public void cambiarPantalla(String nombrePantalla) {
        controlLayout.show(this.getContentPane(), nombrePantalla);
    }
    
    // CAMBIO IMPLEMENTADO: el controlador principal inyecta aquí la persistencia
    public void setAccionGuardarAlCerrar(Runnable accionGuardarAlCerrar) {
        this.accionGuardarAlCerrar = accionGuardarAlCerrar;
    }
    private void guardarYSalir() {
        try {
            if (accionGuardarAlCerrar != null) {
                accionGuardarAlCerrar.run();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "No se pudieron guardar los datos antes de cerrar: " + ex.getMessage(),
                    "Error de guardado",
                    JOptionPane.ERROR_MESSAGE
            );
        }

        dispose();
        System.exit(0);
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
    
    // Getters de PanelCliente y PanelEmpleado
    public PanelCliente getPanelCliente() {
        return panelCliente;
    }

    public PanelEmpleado getPanelEmpleado() {
        return panelEmpleado;
    }
    
    public PanelAdmin getPanelAdmin() {
        return panelAdmin;
    }
    
    public static void main(String[] args) {
        VentanaPrincipal ventana = new VentanaPrincipal();
        new ControllerPrincipal(ventana);
        ventana.setVisible(true);
    }
}