package Controladores;

import InterfazGrafica.VentanaPrincipal;

public class OpcionesController {

    private VentanaPrincipal vista;
    private ControllerPrincipal jefe; // The manager that handles the screen switching

    // The constructor receives the window and the main manager
    public OpcionesController(VentanaPrincipal vista, ControllerPrincipal jefe) {
        this.vista = vista;
        this.jefe = jefe;

        // Start listening to the buttons immediately
        configurarListeners();
    }

    private void configurarListeners() {
        // Si presionamos Administrador, se pasa a PantallaLogin
        vista.getPanelOpciones().getBtnAdministrador().addActionListener(e -> {
            jefe.viajarA("PantallaLogin");
        });

        // Si se clickea en Empleado, se pasa a pantalla Login
        vista.getPanelOpciones().getBtnEmpleado().addActionListener(e -> {
            jefe.viajarA("PantallaLogin");
        });

        // Si se clickea en cliente, se pasa a la pantalla de OpcionesCliente
        vista.getPanelOpciones().getBtnCliente().addActionListener(e -> {
            jefe.viajarA("PantallaOpcionesCliente");
        });
    }

}
