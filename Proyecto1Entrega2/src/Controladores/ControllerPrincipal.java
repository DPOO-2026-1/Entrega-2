package Controladores;

import InterfazGrafica.VentanaPrincipal;
import Usuario.Usuario;
import World.Cafeteria;

public class ControllerPrincipal {

    private VentanaPrincipal vista;
    private LoginController controladorLogin;
    private OpcionesClienteController controladorOpcionesCliente;
    private OpcionesController controladorOpciones;
    private ControladorRegistrarCliente controladorRegistrarCliente;

    private Cafeteria cafeteria;
    private Usuario usuarioActual;

    public ControllerPrincipal(VentanaPrincipal vista) {
        this.vista = vista;

        this.cafeteria = new Cafeteria();

        // La app se inicializa con el primer controlador sólamente porque usamos Lazy
        // loading
        // ya que tenemos muchas páginas

        this.controladorOpciones = new OpcionesController(vista, this);

    }

    public void moverseA(String nombrePantalla) {
        // Si nombrePantalla es igual a PanelLogin

        // Si no, pasamos a PanelOpciones
        if ("PanelLogin".equals(nombrePantalla)) {
            if (controladorLogin == null) {
                controladorLogin = new LoginController(vista, this);
            }
            vista.cambiarPantalla("PanelLogin");
        }

        // Si no, pasamos a PanelOpciones
        else if ("PanelOpcionesCliente".equals(nombrePantalla)) {
            if (controladorOpcionesCliente == null) {
                controladorOpcionesCliente = new OpcionesClienteController(vista, this);
            }
            vista.cambiarPantalla("PanelOpcionesCliente");
        } else if ("PanelRegistrarCliente".equals(nombrePantalla)) {
            if (controladorRegistrarCliente == null) {
                controladorRegistrarCliente = new ControladorRegistrarCliente(vista, this);
            }
            vista.cambiarPantalla("PanelRegistrarCliente");
        }
    }

    public Cafeteria getCafeteria() {
        return cafeteria;
    }

    public void setUsuarioActual(Usuario usuario) {
        this.usuarioActual = usuario;
    }

    public Usuario getUsuarioActual() {
        return usuarioActual;
    }
}