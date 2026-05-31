package Controladores;

import InterfazGrafica.VentanaPrincipal;

public class OpcionesController {

    private VentanaPrincipal vista;
    private ControllerPrincipal jefe; // Se encarga de Screen Switching

    // Constructor recibe pantalla y manager
    public OpcionesController(VentanaPrincipal vista, ControllerPrincipal jefe) {
        this.vista = vista;
        this.jefe = jefe;

        // empezamos a hacer listening de los botones
        configurarListeners();
    }

    private void configurarListeners() {
        // Si presionamos Administrador, se pasa a PantallaLogin
        vista.getPanelOpciones().getBtnAdministrador().addActionListener(e -> {
            jefe.moverseA("PanelLogin");
        });

        // Si se clickea en Empleado, se pasa a pantalla Login
        vista.getPanelOpciones().getBtnEmpleado().addActionListener(e -> {
            jefe.moverseA("PanelLogin");
        });

        // Si se clickea en cliente, se pasa a la pantalla de OpcionesCliente
        vista.getPanelOpciones().getBtnCliente().addActionListener(e -> {
            jefe.moverseA("PanelOpcionesCliente");
        });
    }

}
