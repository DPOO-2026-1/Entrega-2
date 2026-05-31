package Controladores;

import InterfazGrafica.PanelLogin;
import InterfazGrafica.VentanaPrincipal;
import Usuario.Usuario;
import Usuario.Cliente;
import Usuario.Empleado;
import javax.swing.JOptionPane;

public class LoginController {
    private VentanaPrincipal vista;
    private ControllerPrincipal jefe;
    private PanelLogin panelLogin;

    public LoginController(VentanaPrincipal vista, ControllerPrincipal jefe) {
        this.vista = vista;
        this.jefe = jefe;
        this.panelLogin = vista.getPanelLogin();

        configurarListeners();
    }

    private void configurarListeners() {
        panelLogin.getBotonLogin().addActionListener(e -> {
            String usuarioStr = panelLogin.getUsuario();
            String contrasenaStr = panelLogin.getContrasena();

            if (usuarioStr.isEmpty() || contrasenaStr.isEmpty()) {
                JOptionPane.showMessageDialog(vista, "Todos los campos son obligatorios.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                return;
            }

            try {
                // Reutilizamos la lógica del modelo de Cafetería cargada en ControllerPrincipal
                Usuario usuarioAutenticado = jefe.getCafeteria().login(usuarioStr, contrasenaStr);

                if (usuarioAutenticado != null) {
                    jefe.setUsuarioActual(usuarioAutenticado);
                    // Modificaciones para PanelCliente y PanelEmpleado
                    if (usuarioAutenticado instanceof Cliente) {
                        jefe.moverseA("PanelCliente");
                    } else if (usuarioAutenticado instanceof Empleado) {
                        jefe.moverseA("PanelEmpleado");
                    } 
                    String tipoUsuario = usuarioAutenticado.getClass().getSimpleName();
                    JOptionPane.showMessageDialog(vista, "Bienvenido de nuevo, " + usuarioAutenticado.getNombre() + ".", "Inicio de Sesión", JOptionPane.INFORMATION_MESSAGE);
                    
                    if (tipoUsuario.equals("Administrador")) {
                        jefe.moverseA("PanelAdmin");
                    } else if (tipoUsuario.equals("Cliente")) {
                        jefe.moverseA("PanelOpcionesCliente");
                    } 
                    // En consolas futuras se redirigirá a los páneles correspondientes según el rol
                    System.out.println("Usuario autenticado correctamente: " + usuarioAutenticado.getNombre() + " (" + usuarioAutenticado.getClass().getSimpleName() + ")");
                } else {
                    JOptionPane.showMessageDialog(vista, "Credenciales incorrectas.", "Error de Inicio de Sesión", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(vista, "Error al iniciar sesión: " + ex.getMessage(), "Error Crítico", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}
