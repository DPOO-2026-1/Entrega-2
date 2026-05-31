package Controladores;

import InterfazGrafica.PanelOpcionesCliente;
import InterfazGrafica.VentanaPrincipal;

public class OpcionesClienteController {
    private VentanaPrincipal vista;
    private ControllerPrincipal jefe;
    private PanelOpcionesCliente panelOpcionesCliente;

    public OpcionesClienteController(VentanaPrincipal vista, ControllerPrincipal jefe) {
        this.vista = vista;
        this.jefe = jefe;
        this.panelOpcionesCliente = vista.getPanelOpcionesCliente();

        configurarListeners();
    }

    private void configurarListeners() {
        // Redirige al login de clientes
        panelOpcionesCliente.getBtnLoggearse().addActionListener(e -> {
            jefe.moverseA("PanelLogin");
        });

        // Redirige al registro de nuevos clientes
        panelOpcionesCliente.getRegistrarse().addActionListener(e -> {
            jefe.moverseA("PanelRegistrarCliente");
        });
    }
}
