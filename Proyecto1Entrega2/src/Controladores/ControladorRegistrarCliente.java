package Controladores;

import InterfazGrafica.PanelRegistrarCliente;
import InterfazGrafica.VentanaPrincipal;
import javax.swing.JOptionPane;
import Usuario.Cliente;
import World.Cafeteria;

public class ControladorRegistrarCliente {
    private VentanaPrincipal vista;
    private ControllerPrincipal jefe;
    private PanelRegistrarCliente panelRegistro;
    private Cafeteria cafeteria;

    public ControladorRegistrarCliente(VentanaPrincipal vista, ControllerPrincipal jefe) {
        this.vista = vista;
        this.jefe = jefe;
        this.cafeteria = jefe.getCafeteria();
        // El panel de registro.
        this.panelRegistro = vista.getPanelPanelRegistrarCliente();

        configurarListeners();
    }

    private void configurarListeners() {
        // Acción para boton de registro.
        panelRegistro.getBotonLogin().addActionListener(e -> {
            // Obtiene la info para registrar los clientes
            String login = panelRegistro.getUsuario();
            String contrasenia = panelRegistro.getContrasena();
            String nombre = panelRegistro.getNombre();

            // Obtener el estado de los checkboxes
            boolean esNinio = panelRegistro.esNinio();
            boolean esJoven = panelRegistro.esJoven();

            // Nos dice si tenemos campos en empty.
            if (login.isEmpty() || contrasenia.isEmpty() || nombre.isEmpty()) {
                System.out.println("Error: Todos los campos son obligatorios.");
                JOptionPane.showMessageDialog(vista, "Todos los campos son obligatorios.", "Error",
                        JOptionPane.ERROR_MESSAGE);

            } else {
                System.out.println("Registrando cliente: " + login);

                try {
                    // Replicamos el comportamiento de consola.
                    Cliente cliente = jefe.getCafeteria().getGestorUsuarios().registrarCliente(
                            login,
                            contrasenia,
                            nombre,
                            esNinio,
                            esJoven);

                    // Inicializamos una sesión global para que el usuario pueda hacer lo necesario.
                    jefe.setUsuarioActual(cliente);

                    System.out.println("Cliente registrado correctamente. Bienvenido, " + cliente.getNombre() + ".");
                    JOptionPane.showMessageDialog(vista,
                            "Cliente registrado correctamente.\nBienvenido, " + cliente.getNombre() + ".", "Éxito",
                            JOptionPane.INFORMATION_MESSAGE);

                    // Lleva a login luego del registro.
                    jefe.moverseA("PanelLogin");

                } catch (Exception ex) {
                    System.out.println("No se pudo registrar el cliente.");
                    System.out.println("Detalle: " + ex.getMessage());
                    // Imprimimos diálogos ya que el usuario no ve la consola.
                    JOptionPane.showMessageDialog(vista, "No se pudo registrar: " + ex.getMessage(),
                            "Error de Registro", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Todavía no tenemos botón para volver.
    }
}