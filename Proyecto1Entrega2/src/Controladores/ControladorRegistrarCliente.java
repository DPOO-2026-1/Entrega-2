package Controladores;

import InterfazGrafica.PanelRegistrarCliente;
import InterfazGrafica.VentanaPrincipal;
import javax.swing.JOptionPane; // Útil para mostrar errores visuales al usuario
import Usuario.Cliente;
import World.Cafeteria;

public class ControladorRegistrarCliente {
    private VentanaPrincipal vista;
    private ControllerPrincipal jefe;
    private PanelRegistrarCliente panelRegistro;
    private Cafeteria cafeteria; // Campo agregado

    public ControladorRegistrarCliente(VentanaPrincipal vista, ControllerPrincipal jefe, Cafeteria cafeteria) {
        this.vista = vista;
        this.jefe = jefe;
        this.cafeteria = cafeteria;
        // El panel de registro.
        this.panelRegistro = vista.getPanelRegistrarCliente();

        configurarListeners();
    }

    private void configurarListeners() {
        // 1. Action for the "Register" Button (logiButton en tu panel)
        panelRegistro.getBotonLogin().addActionListener(e -> {
            // Obtiene la info para registrar los clientes.
            String login = panelRegistro.getUsuario();
            String contrasenia = panelRegistro.getContrasena();
            
            // NOTA: Como tu panel actual solo pide Usuario y Contraseña, 
            // usaremos el Usuario como "Nombre" temporalmente para cumplir con tu modelo de datos.
            String nombre = login; 
            
            // Obtener el estado de los checkboxes
            boolean esNinio = panelRegistro.esNinio();
            boolean esJoven = panelRegistro.esJoven();

            // Nos dice si tenemos campos en empty.
            if (login.isEmpty() || contrasenia.isEmpty()) {
                System.out.println("Error: Todos los campos son obligatorios.");
                JOptionPane.showMessageDialog(vista, "Todos los campos son obligatorios.", "Error", JOptionPane.ERROR_MESSAGE);
                
            } else {
                System.out.println("Registrando cliente: " + login);
                
                try {
                    // CONEXIÓN CON TU LÓGICA ANTERIOR DE CONSOLA:
                    // Se asume que tu jefe (ControllerPrincipal) o la vista tienen acceso a la instancia 'cafeteria'
                    Cliente cliente = jefe.getCafeteria().getGestorUsuarios().registrarCliente(
                            login,
                            contrasenia,
                            nombre,
                            esNinio,
                            esJoven
                    );

                    // Guardamos el usuario actual en la sesión global
                    jefe.setUsuarioActual(cliente);
                    
                    System.out.println("Cliente registrado correctamente. Bienvenido, " + cliente.getNombre() + ".");
                    JOptionPane.showMessageDialog(vista, "Cliente registrado correctamente.\nBienvenido, " + cliente.getNombre() + ".", "Éxito", JOptionPane.INFORMATION_MESSAGE);

                    // Lleva a pantalla Login luego de haberse registrado.
                    jefe.moverseA(nombre);("PantallaLogin");

                } catch (Exception ex) {
                    System.out.println("No se pudo registrar el cliente.");
                    System.out.println("Detalle: " + ex.getMessage());
                    JOptionPane.showMessageDialog(vista, "No se pudo registrar: " + ex.getMessage(), "Error de Registro", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Todavía no tenemos botón para volver.
    }
}