package Controladores;

import InterfazGrafica.VentanaPrincipal;
import Usuario.Usuario;
import Usuario.Cliente;
import Usuario.Empleado;
import World.Cafeteria;
import World.CopiaPrestamo;
import World.Juego;
import World.Mesa;
import Persistencia.GestorPersistencia;
import Usuario.GestorUsuarios;
import ModuloVenta.CopiaVenta;
import ModuloVenta.GestorVentas;
import Torneo.GestorTorneo;

public class ControllerPrincipal {

    private VentanaPrincipal vista;
    private LoginController controladorLogin;
    private OpcionesClienteController controladorOpcionesCliente;
    private OpcionesController controladorOpciones;
    private ControladorRegistrarCliente controladorRegistrarCliente;

    private Cafeteria cafeteria;
    private Usuario usuarioActual;

    private GestorPersistencia persistencia;

    public ControllerPrincipal(VentanaPrincipal vista) {
        this.vista = vista;

        // 1. Inicializar la persistencia y gestores de igual forma que en la Consola
        this.persistencia = new GestorPersistencia("data/");
        GestorUsuarios gestorUsuarios = new GestorUsuarios(this.persistencia, null);
        GestorVentas gestorVentas = new GestorVentas(this.persistencia);
     
        // 2. Inicializar la instancia del Singleton Cafeteria
        this.cafeteria = Cafeteria.getInstance(80, "Board Nights", gestorUsuarios, gestorVentas);
        gestorUsuarios.setCafeteria(this.cafeteria);
        this.cafeteria.setGestorUsuarios(gestorUsuarios);
        this.cafeteria.setGestorVentas(gestorVentas);

        // 3. Cargar los datos desde los archivos de persistencia CSV al iniciar
        try {
            Cafeteria cargada = persistencia.cargarTodo();
            if (cargada != null) {
                this.cafeteria = cargada;
                Cafeteria.setInstance(cargada);
            }

            // Reconstruir y asignar el GestorTorneo unificado
            GestorTorneo loadedTorneos = persistencia.cargarGestorTorneo(
                    this.cafeteria.getUsuarios(),
                    this.cafeteria.getJuegos()
            );
            this.cafeteria.setGestorTorneo(loadedTorneos);
            
            asegurarDatosMinimosParaGUI();
            
            System.out.println("Datos cargados correctamente en la GUI.");
        } catch (Exception e) {
            System.out.println("No se pudieron cargar los datos en la GUI.");
            e.printStackTrace();
        }

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
        // Cambios agregados para PanelCliente y PanelEmpleado
        else if ("PanelCliente".equals(nombrePantalla)) {
            if (usuarioActual instanceof Cliente) {
                vista.getPanelCliente().configurarContexto(
                        cafeteria,
                        (Cliente) usuarioActual,
                        persistencia
                );

                vista.getPanelCliente().setAccionCerrarSesion(() -> {
                    usuarioActual.cerrarSesion();
                    usuarioActual = null;
                    vista.cambiarPantalla("PanelOpciones");
                });

                vista.cambiarPantalla("PanelCliente");
            }
        } else if ("PanelEmpleado".equals(nombrePantalla)) {
            if (usuarioActual instanceof Empleado) {
                vista.getPanelEmpleado().configurarContexto(
                        cafeteria,
                        (Empleado) usuarioActual,
                        persistencia
                );

                vista.getPanelEmpleado().setAccionCerrarSesion(() -> {
                    usuarioActual.cerrarSesion();
                    usuarioActual = null;
                    vista.cambiarPantalla("PanelOpciones");
                });

                vista.cambiarPantalla("PanelEmpleado");
            }
        }
    }
    
    // Esto evita que los paneles de préstamos/compras aparezcan totalmente vacíos
    // cuando los CSV no tienen copias o mesas cargadas.
    private void asegurarDatosMinimosParaGUI() {
        if (cafeteria == null) {
            return;
        }

        if (cafeteria.getMesas() == null || cafeteria.getMesas().isEmpty()) {
            cafeteria.getMesas().add(new Mesa(1, 4));
            cafeteria.getMesas().add(new Mesa(2, 6));
            cafeteria.getMesas().add(new Mesa(3, 8));
        }

        if (cafeteria.getJuegos() != null) {
            int contador = 1;

            for (Juego juego : cafeteria.getJuegos()) {
                if (juego.getCopiasParaPrestamo().isEmpty()) {
                    CopiaPrestamo copia = new CopiaPrestamo("P-" + contador, "Disponible", true, 0);
                    copia.setJuegoAsociado(juego);
                    juego.agregarCopiaPrestamo(copia);
                }

                if (juego.getCopiasParaVenta().isEmpty()) {
                    CopiaVenta copiaVenta = new CopiaVenta("V-" + contador, 50000);
                    juego.agregarCopiaVenta(copiaVenta);
                }

                contador++;
            }
        }
    }

    public Cafeteria getCafeteria() {
        return cafeteria;
    }
    
    public GestorPersistencia getPersistencia() {
        return persistencia;
    }

    public void setUsuarioActual(Usuario usuario) {
        this.usuarioActual = usuario;
    }

    public Usuario getUsuarioActual() {
        return usuarioActual;
    }
}