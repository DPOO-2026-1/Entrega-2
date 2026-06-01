package Controladores;

import InterfazGrafica.PanelAdmin;
import InterfazGrafica.PanelInventarioAdmin;
import InterfazGrafica.PanelMenuAdmin;
import InterfazGrafica.PanelTurnosAdmin;
import InterfazGrafica.VentanaPrincipal;

import ModuloVenta.Bebida;
import ModuloVenta.CopiaVenta;
import ModuloVenta.Pasteleria;
import ModuloVenta.ProductoComestible;

import Torneo.GestorTorneo;
import Torneo.ResultadoTorneo;
import Torneo.Torneo;

import Usuario.Administrador;
import Usuario.Cocinero;
import Usuario.DiaSemana;
import Usuario.DiaTurno;
import Usuario.Empleado;
import Usuario.Mesero;
import Usuario.SolicitudTurno;
import Usuario.SugerenciaMenu;
import Usuario.Usuario;

import World.Cafeteria;
import World.CopiaPrestamo;
import World.Juego;

import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AdminController {

    private VentanaPrincipal vista;
    private ControllerPrincipal jefe;
    private PanelAdmin panelAdmin;
    private SimpleDateFormat sdfTabla;

    public AdminController(VentanaPrincipal vista, ControllerPrincipal jefe) {
        this.vista = vista;
        this.jefe = jefe;
        this.panelAdmin = vista.getPanelAdmin();
        this.sdfTabla = new SimpleDateFormat("yyyy/MM/dd");

        configurarListeners();

        // CAMBIO Se conectan los paneles nuevos del administrador: inventario, menú/sugerencias y turnos.
        configurarListenersPanelesFaltantes();

        cargarDatosIniciales();
    }

    private void cargarDatosIniciales() {
        cargarJuegosEnCrearTorneo();

        // CAMBIO Carga inicial real de tabla de torneos, dashboard, inventario, menú y turnos.
        cargarTorneosEnTabla(null);

        if (panelAdmin.getPanelVisualizaciones() != null) {
            panelAdmin.getPanelVisualizaciones().configurarDatos(jefe.getCafeteria());
        }

        refrescarInventario();
        refrescarMenuYSugerencias();
        refrescarTurnosAdmin();
    }

    private void cargarJuegosEnCrearTorneo() {
        panelAdmin.getPanelCrearTorneo().getCbJuegoAsociado().removeAllItems();

        if (jefe.getCafeteria() == null || jefe.getCafeteria().getJuegos() == null) {
            return;
        }

        List<Juego> juegos = jefe.getCafeteria().getJuegos();

        for (Juego juego : juegos) {
            if (juego != null && juego.getNombre() != null) {
                panelAdmin.getPanelCrearTorneo().getCbJuegoAsociado().addItem(juego.getNombre());
            }
        }
    }

    private void configurarListeners() {
        panelAdmin.getPanelCrearTorneo().getBtnValidarCrear().addActionListener(e -> crearNuevoTorneo());

        panelAdmin.getPanelCrearTorneo().getBtnVolver().addActionListener(e -> {
            panelAdmin.getCardLayout().show(panelAdmin.getPanelCentral(), "DASHBOARD");

            if (panelAdmin.getPanelVisualizaciones() != null) {
                panelAdmin.getPanelVisualizaciones().configurarDatos(jefe.getCafeteria());
            }
        });

        // CAMBIO Registro real de empleados desde el panel de administrador.
        panelAdmin.getPanelRegistrar().getBtnGuardar().addActionListener(e -> registrarEmpleadoDesdeGUI());
        
        // CAMBIO Gestión real de torneos: búsqueda y finalización con ganador.
        panelAdmin.getPanelGestionar().getBtnBuscar().addActionListener(e -> {
            cargarTorneosEnTabla(panelAdmin.getPanelGestionar().getBusqueda());
        });

        panelAdmin.getPanelGestionar().getBtnFinalizar().addActionListener(e -> finalizarTorneoSeleccionado());
        panelAdmin.getBtnCerrarSesion().addActionListener(e -> {
            guardarDatos();
            jefe.setUsuarioActual(null);
            vista.cambiarPantalla("PanelOpciones");
        });
    }

    // CAMBIO Conexión de paneles faltantes: Inventario, Menú/Sugerencias y Turnos.
    private void configurarListenersPanelesFaltantes() {
        configurarListenersInventario();
        configurarListenersMenu();
        configurarListenersTurnos();
    }

    // CAMBIO REGISTRAR EMPLEADO
    private void registrarEmpleadoDesdeGUI() {
        try {
            validarAdminActual();

            String nombre = panelAdmin.getPanelRegistrar().getNombre();
            String documento = panelAdmin.getPanelRegistrar().getDocumento();
            String login = panelAdmin.getPanelRegistrar().getLoginEmpleado();
            String password = panelAdmin.getPanelRegistrar().getPasswordEmpleado();
            String tipo = panelAdmin.getPanelRegistrar().getTipoRol();
            String codigo = panelAdmin.getPanelRegistrar().getCodigoDescuento();

            if (nombre == null || nombre.trim().isEmpty()
                    || login == null || login.trim().isEmpty()
                    || password == null || password.trim().isEmpty()
                    || codigo == null || codigo.trim().isEmpty()) {

                JOptionPane.showMessageDialog(
                        vista,
                        "Completa nombre, login, password y código de descuento.",
                        "Campos incompletos",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            String tipoDominio;

            if ("Mesero".equalsIgnoreCase(tipo)) {
                tipoDominio = "MESERO";
            } else {
                tipoDominio = "COCINERO";
            }

            Administrador admin = (Administrador) jefe.getUsuarioActual();

            Empleado empleado = admin.registrarEmpleado(
                    login.trim(),
                    password.trim(),
                    nombre.trim(),
                    tipoDominio,
                    codigo.trim()
            );

            guardarDatos();
            refrescarTurnosAdmin();

            panelAdmin.getPanelRegistrar().limpiarCampos();

            JOptionPane.showMessageDialog(
                    vista,
                    "Empleado registrado correctamente:\n"
                            + "Nombre: " + empleado.getNombre() + "\n"
                            + "Login: " + empleado.getLogin() + "\n"
                            + "Documento: " + documento + "\n"
                            + "Tipo: " + tipo,
                    "Registro exitoso",
                    JOptionPane.INFORMATION_MESSAGE
            );

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                    vista,
                    ex.getMessage(),
                    "Error registrando empleado",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // CAMBIO CREAR TORNEO
    private void crearNuevoTorneo() {
        try {
            validarAdminActual();

            Administrador admin = (Administrador) jefe.getUsuarioActual();
            GestorTorneo gestorTorneo = jefe.getCafeteria().getGestorTorneo();

            String nombreJuego = (String) panelAdmin.getPanelCrearTorneo().getCbJuegoAsociado().getSelectedItem();
            String fechaStr = panelAdmin.getPanelCrearTorneo().getFechaInicio();
            String tipo = panelAdmin.getPanelCrearTorneo().getTipoTorneo();
            String duracionStr = panelAdmin.getPanelCrearTorneo().getDuracion();
            String cupoStr = panelAdmin.getPanelCrearTorneo().getCupoTotal();
            String valorDinamicoStr = panelAdmin.getPanelCrearTorneo().getValorDinamico();

            if (nombreJuego == null
                    || fechaStr == null || fechaStr.trim().isEmpty()
                    || duracionStr == null || duracionStr.trim().isEmpty()
                    || cupoStr == null || cupoStr.trim().isEmpty()
                    || valorDinamicoStr == null || valorDinamicoStr.trim().isEmpty()) {

                JOptionPane.showMessageDialog(
                        vista,
                        "Todos los campos deben ser rellenados.",
                        "Advertencia",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            int duracion = Integer.parseInt(duracionStr.trim());
            int cupo = Integer.parseInt(cupoStr.trim());
            double valor = Double.parseDouble(valorDinamicoStr.trim());

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
            sdf.setLenient(false);
            Date fechaParsed = sdf.parse(fechaStr.trim());

            Calendar cal = Calendar.getInstance();
            cal.setTime(fechaParsed);

            DiaSemana diaEnum = convertirDia(cal.get(Calendar.DAY_OF_WEEK));

            Juego juegoAsociado = buscarJuegoPorNombre(nombreJuego);

            if (juegoAsociado == null) {
                throw new IllegalArgumentException("El juego seleccionado no existe.");
            }

            if (juegoAsociado.getCopiasPrestamo() == null || juegoAsociado.getCopiasPrestamo().isEmpty()) {
                for (int i = 0; i < cupo + 5; i++) {
                    CopiaPrestamo copia = new CopiaPrestamo("VIRTUAL-" + System.currentTimeMillis() + "-" + i, "Disponible", true, 0);
                    copia.setJuegoAsociado(juegoAsociado);
                    juegoAsociado.agregarCopiaPrestamo(copia);
                }
            }

            Torneo nuevoTorneo;

            if ("Amistoso".equalsIgnoreCase(tipo)) {
                nuevoTorneo = gestorTorneo.crearTorneoAmistoso(
                        admin,
                        juegoAsociado,
                        diaEnum,
                        "00:00",
                        cupo,
                        valor
                );
            } else {
                nuevoTorneo = gestorTorneo.crearTorneoCompetitivo(
                        admin,
                        juegoAsociado,
                        diaEnum,
                        "00:00",
                        cupo,
                        valor
                );
            }

            nuevoTorneo.setFechaInicio(fechaParsed);
            nuevoTorneo.setDuracionMin(duracion);

            guardarDatos();
            cargarTorneosEnTabla(null);

            if (panelAdmin.getPanelVisualizaciones() != null) {
                panelAdmin.getPanelVisualizaciones().configurarDatos(jefe.getCafeteria());
            }

            JOptionPane.showMessageDialog(
                    vista,
                    "Torneo creado correctamente:\n"
                            + nombreJuego + "\n"
                            + fechaStr + " (" + diaEnum + ")",
                    "Creación exitosa",
                    JOptionPane.INFORMATION_MESSAGE
            );

            panelAdmin.getPanelCrearTorneo().limpiarCampos();

        } catch (java.text.ParseException ex) {
            JOptionPane.showMessageDialog(
                    vista,
                    "Formato de fecha inválido. Usa YYYY/MM/DD. Ejemplo: 2026/05/15",
                    "Error de formato",
                    JOptionPane.ERROR_MESSAGE
            );
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(
                    vista,
                    "Ingresa solo números en duración, cupo y tarifa/bono.",
                    "Error de formato",
                    JOptionPane.ERROR_MESSAGE
            );
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                    vista,
                    ex.getMessage(),
                    "Error creando torneo",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // CAMBIO GESTIONAR / FINALIZAR TORNEOS
    private void cargarTorneosEnTabla(String filtroDia) {
        DefaultTableModel modelo = panelAdmin.getPanelGestionar().getModeloTabla();
        modelo.setRowCount(0);

        if (jefe.getCafeteria() == null || jefe.getCafeteria().getGestorTorneo() == null) {
            return;
        }

        List<Torneo> torneos = jefe.getCafeteria().getGestorTorneo().getCatalogoTorneos();

        if (torneos == null) {
            return;
        }

        String filtro = filtroDia == null ? "" : filtroDia.trim().toUpperCase();

        for (Torneo torneo : torneos) {
            if (torneo == null) {
                continue;
            }

            String diaTexto = torneo.getDia() == null ? "" : torneo.getDia().name();

            if (!filtro.isEmpty() && !diaTexto.toUpperCase().contains(filtro)) {
                continue;
            }

            modelo.addRow(new Object[]{
                    torneo.getIdTorneo(),
                    torneo.getNombre(),
                    torneo.getFechaInicio() != null ? sdfTabla.format(torneo.getFechaInicio()) : "Sin fecha",
                    torneo.getEstado()
            });
        }
    }

    private void finalizarTorneoSeleccionado() {
        try {
            validarAdminActual();

            String idTorneo = obtenerIdTorneoSeleccionado();

            if (idTorneo == null) {
                JOptionPane.showMessageDialog(
                        vista,
                        "Selecciona un torneo de la tabla.",
                        "Sin selección",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            String loginGanador = JOptionPane.showInputDialog(vista, "Login del ganador:");

            if (loginGanador == null || loginGanador.trim().isEmpty()) {
                return;
            }

            Usuario ganador = buscarUsuarioPorLogin(loginGanador.trim());

            if (ganador == null) {
                throw new IllegalArgumentException("No existe un usuario con login: " + loginGanador);
            }

            ResultadoTorneo resultado = jefe.getCafeteria().getGestorTorneo().finalizarTorneo(
                    (Administrador) jefe.getUsuarioActual(),
                    idTorneo,
                    ganador
            );

            guardarDatos();
            cargarTorneosEnTabla(panelAdmin.getPanelGestionar().getBusqueda());

            if (panelAdmin.getPanelVisualizaciones() != null) {
                panelAdmin.getPanelVisualizaciones().configurarDatos(jefe.getCafeteria());
            }

            StringBuilder mensaje = new StringBuilder();
            mensaje.append("Torneo finalizado correctamente.\n");
            mensaje.append("Ganador: ").append(resultado.getGanador().getNombre()).append("\n");

            if (resultado.getBono() != null) {
                mensaje.append("Bono otorgado: ").append(resultado.getBono().getCodigo()).append("\n");
            }

            if (resultado.getPremioMetalico() > 0) {
                mensaje.append("Premio metálico: $").append(resultado.getPremioMetalico()).append("\n");
            }

            if (resultado.getBono() == null && resultado.getPremioMetalico() == 0) {
                mensaje.append("No hubo bono ni premio metálico para este ganador.");
            }

            JOptionPane.showMessageDialog(
                    vista,
                    mensaje.toString(),
                    "Torneo finalizado",
                    JOptionPane.INFORMATION_MESSAGE
            );

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                    vista,
                    ex.getMessage(),
                    "Error finalizando torneo",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private String obtenerIdTorneoSeleccionado() {
        JTable tabla = panelAdmin.getPanelGestionar().getTablaTorneos();
        DefaultTableModel modelo = panelAdmin.getPanelGestionar().getModeloTabla();

        int fila = tabla.getSelectedRow();

        if (fila < 0) {
            return null;
        }

        int filaModelo = tabla.convertRowIndexToModel(fila);
        Object valor = modelo.getValueAt(filaModelo, 0);

        return valor == null ? null : valor.toString();
    }

    // CAMBIO INVENTARIO ADMIN
    private void configurarListenersInventario() {
        PanelInventarioAdmin panel = panelAdmin.getPanelInventarioAdmin();

        panel.getBtnActualizar().addActionListener(e -> refrescarInventario());

        panel.getCbJuegos().addActionListener(e -> cargarCopiasDelJuegoSeleccionado());

        panel.getBtnComprar().addActionListener(e -> comprarInventarioDesdeGUI());

        panel.getBtnMoverVentaAPrestamo().addActionListener(e -> moverVentaAPrestamoDesdeGUI());

        panel.getBtnReparar().addActionListener(e -> repararCopiaDesdeGUI());

        panel.getBtnMarcarRobado().addActionListener(e -> marcarRobadoDesdeGUI());

        panel.getBtnVerHistorial().addActionListener(e -> verHistorialJuegoDesdeGUI());
    }

    private void refrescarInventario() {
        PanelInventarioAdmin panel = panelAdmin.getPanelInventarioAdmin();

        panel.getModeloTabla().setRowCount(0);
        panel.getCbJuegos().removeAllItems();

        if (jefe.getCafeteria() == null || jefe.getCafeteria().getJuegos() == null) {
            return;
        }

        for (Juego juego : jefe.getCafeteria().getJuegos()) {
            if (juego == null) {
                continue;
            }

            int copiasPrestamo = juego.getCopiasParaPrestamo() == null ? 0 : juego.getCopiasParaPrestamo().size();
            int copiasVenta = juego.getCopiasParaVenta() == null ? 0 : juego.getCopiasParaVenta().size();
            int disponiblesPrestamo = 0;

            if (juego.getCopiasParaPrestamo() != null) {
                for (CopiaPrestamo copia : juego.getCopiasParaPrestamo()) {
                    if (copia != null && copia.estaDisponible()) {
                        disponiblesPrestamo++;
                    }
                }
            }

            panel.getModeloTabla().addRow(new Object[]{
                    juego.getNombre(),
                    copiasPrestamo,
                    copiasVenta,
                    disponiblesPrestamo,
                    juego.isEsDificil() ? "Sí" : "No"
            });

            panel.getCbJuegos().addItem(juego.getNombre());
        }

        cargarCopiasDelJuegoSeleccionado();
    }

    private void cargarCopiasDelJuegoSeleccionado() {
        PanelInventarioAdmin panel = panelAdmin.getPanelInventarioAdmin();

        panel.getCbCopiasPrestamo().removeAllItems();
        panel.getCbCopiasVenta().removeAllItems();

        Juego juego = buscarJuegoPorNombre(panel.getJuegoSeleccionado());

        if (juego == null) {
            return;
        }

        if (juego.getCopiasParaPrestamo() != null) {
            for (CopiaPrestamo copia : juego.getCopiasParaPrestamo()) {
                if (copia != null) {
                    panel.getCbCopiasPrestamo().addItem(copia.getIdUnico());
                }
            }
        }

        if (juego.getCopiasParaVenta() != null) {
            for (CopiaVenta copia : juego.getCopiasParaVenta()) {
                if (copia != null) {
                    panel.getCbCopiasVenta().addItem(copia.getIdUnico());
                }
            }
        }
    }

    private void comprarInventarioDesdeGUI() {
        try {
            validarAdminActual();

            PanelInventarioAdmin panel = panelAdmin.getPanelInventarioAdmin();
            Juego juego = buscarJuegoPorNombre(panel.getJuegoSeleccionado());

            if (juego == null) {
                throw new IllegalArgumentException("Selecciona un juego válido.");
            }

            int cantidad = panel.getCantidad();

            if (cantidad <= 0) {
                throw new IllegalArgumentException("La cantidad debe ser mayor a 0.");
            }

            Administrador admin = (Administrador) jefe.getUsuarioActual();
            admin.comprarJuegos(juego, cantidad, panel.getTipoCompra());

            guardarDatos();
            refrescarInventario();
            panel.limpiarCantidad();

            JOptionPane.showMessageDialog(
                    vista,
                    "Inventario actualizado correctamente.",
                    "Éxito",
                    JOptionPane.INFORMATION_MESSAGE
            );

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                    vista,
                    ex.getMessage(),
                    "Error inventario",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void moverVentaAPrestamoDesdeGUI() {
        try {
            validarAdminActual();

            PanelInventarioAdmin panel = panelAdmin.getPanelInventarioAdmin();
            Juego juego = buscarJuegoPorNombre(panel.getJuegoSeleccionado());

            if (juego == null) {
                throw new IllegalArgumentException("Selecciona un juego válido.");
            }

            CopiaVenta copiaVenta = buscarCopiaVenta(juego, panel.getCopiaVentaSeleccionada());

            if (copiaVenta == null) {
                throw new IllegalArgumentException("Selecciona una copia de venta válida.");
            }

            Administrador admin = (Administrador) jefe.getUsuarioActual();
            admin.moverJuegoAPrestamo(copiaVenta, juego);

            guardarDatos();
            refrescarInventario();

            JOptionPane.showMessageDialog(
                    vista,
                    "Copia movida de venta a préstamo correctamente.",
                    "Éxito",
                    JOptionPane.INFORMATION_MESSAGE
            );

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                    vista,
                    ex.getMessage(),
                    "Error inventario",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void repararCopiaDesdeGUI() {
        try {
            validarAdminActual();

            PanelInventarioAdmin panel = panelAdmin.getPanelInventarioAdmin();
            Juego juego = buscarJuegoPorNombre(panel.getJuegoSeleccionado());

            if (juego == null) {
                throw new IllegalArgumentException("Selecciona un juego válido.");
            }

            CopiaPrestamo copiaPrestamo = buscarCopiaPrestamo(juego, panel.getCopiaPrestamoSeleccionada());

            if (copiaPrestamo == null) {
                throw new IllegalArgumentException("Selecciona una copia de préstamo válida.");
            }

            Administrador admin = (Administrador) jefe.getUsuarioActual();
            admin.repararJuego(copiaPrestamo);

            guardarDatos();
            refrescarInventario();

            JOptionPane.showMessageDialog(
                    vista,
                    "Copia reparada correctamente.",
                    "Éxito",
                    JOptionPane.INFORMATION_MESSAGE
            );

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                    vista,
                    ex.getMessage(),
                    "Error inventario",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void marcarRobadoDesdeGUI() {
        try {
            validarAdminActual();

            PanelInventarioAdmin panel = panelAdmin.getPanelInventarioAdmin();
            Juego juego = buscarJuegoPorNombre(panel.getJuegoSeleccionado());

            if (juego == null) {
                throw new IllegalArgumentException("Selecciona un juego válido.");
            }

            CopiaPrestamo copiaPrestamo = buscarCopiaPrestamo(juego, panel.getCopiaPrestamoSeleccionada());

            if (copiaPrestamo == null) {
                throw new IllegalArgumentException("Selecciona una copia de préstamo válida.");
            }

            Administrador admin = (Administrador) jefe.getUsuarioActual();
            admin.darJuegoPorRobado(copiaPrestamo);

            guardarDatos();
            refrescarInventario();

            JOptionPane.showMessageDialog(
                    vista,
                    "Copia marcada como robada/desaparecida.",
                    "Éxito",
                    JOptionPane.INFORMATION_MESSAGE
            );

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                    vista,
                    ex.getMessage(),
                    "Error inventario",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void verHistorialJuegoDesdeGUI() {
        try {
            validarAdminActual();

            PanelInventarioAdmin panel = panelAdmin.getPanelInventarioAdmin();
            Juego juego = buscarJuegoPorNombre(panel.getJuegoSeleccionado());

            if (juego == null) {
                throw new IllegalArgumentException("Selecciona un juego válido.");
            }

            Administrador admin = (Administrador) jefe.getUsuarioActual();
            String historial = admin.verHistorialJuego(juego);

            panel.setTextoHistorial(historial);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                    vista,
                    ex.getMessage(),
                    "Error historial",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // CAMBIO MENÚ Y SUGERENCIAS ADMIN
    private void configurarListenersMenu() {
        PanelMenuAdmin panel = panelAdmin.getPanelMenuAdmin();

        panel.getBtnActualizar().addActionListener(e -> refrescarMenuYSugerencias());

        panel.getBtnAgregarProducto().addActionListener(e -> agregarProductoMenuDesdeGUI());

        panel.getBtnAprobarSugerencia().addActionListener(e -> aprobarSugerenciaDesdeGUI());

        panel.getBtnRechazarSugerencia().addActionListener(e -> rechazarSugerenciaDesdeGUI());
    }

    private void refrescarMenuYSugerencias() {
        PanelMenuAdmin panel = panelAdmin.getPanelMenuAdmin();

        panel.getModeloMenu().setRowCount(0);
        panel.getModeloSugerencias().setRowCount(0);

        if (jefe.getCafeteria() == null) {
            return;
        }

        if (jefe.getCafeteria().getMenuCafeteria() != null) {
            for (ProductoComestible producto : jefe.getCafeteria().getMenuCafeteria()) {
                if (producto == null) {
                    continue;
                }

                panel.getModeloMenu().addRow(new Object[]{
                        producto.getNombre(),
                        producto.getPrecioBase(),
                        producto instanceof Bebida ? "Bebida" : "Pastelería"
                });
            }
        }

        if (jefe.getCafeteria().getSugerencias() != null) {
            for (int i = 0; i < jefe.getCafeteria().getSugerencias().size(); i++) {
                SugerenciaMenu sugerencia = jefe.getCafeteria().getSugerencias().get(i);

                if (sugerencia == null) {
                    continue;
                }

                panel.getModeloSugerencias().addRow(new Object[]{
                        i,
                        sugerencia.getDescripcion(),
                        sugerencia.getCreadoPor() != null ? sugerencia.getCreadoPor().getLogin() : "N/A",
                        sugerencia.getEstado()
                });
            }
        }
    }

    private void agregarProductoMenuDesdeGUI() {
        try {
            validarAdminActual();

            PanelMenuAdmin panel = panelAdmin.getPanelMenuAdmin();

            String nombre = panel.getNombreProducto();
            double precio = panel.getPrecioProducto();
            String tipo = panel.getTipoProducto();

            if (nombre == null || nombre.trim().isEmpty()) {
                throw new IllegalArgumentException("El nombre del producto no puede estar vacío.");
            }

            if (precio <= 0) {
                throw new IllegalArgumentException("El precio debe ser mayor a 0.");
            }

            ProductoComestible producto;

            if ("Bebida".equalsIgnoreCase(tipo)) {
                producto = new Bebida(nombre.trim(), precio, false, false);
            } else {
                ArrayList<String> alergenos = new ArrayList<String>();

                if (panel.getAlergenos() != null && !panel.getAlergenos().trim().isEmpty()) {
                    alergenos.addAll(Arrays.asList(panel.getAlergenos().split(",")));
                }

                producto = new Pasteleria(nombre.trim(), precio, alergenos);
            }

            Administrador admin = (Administrador) jefe.getUsuarioActual();
            admin.agregarProductoMenu(jefe.getCafeteria().getMenuCafeteria(), producto);

            guardarDatos();
            refrescarMenuYSugerencias();
            panel.limpiarFormularioProducto();

            JOptionPane.showMessageDialog(
                    vista,
                    "Producto agregado al menú correctamente.",
                    "Éxito",
                    JOptionPane.INFORMATION_MESSAGE
            );

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                    vista,
                    ex.getMessage(),
                    "Error menú",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void aprobarSugerenciaDesdeGUI() {
        try {
            validarAdminActual();

            PanelMenuAdmin panel = panelAdmin.getPanelMenuAdmin();
            int indice = panel.getIndiceSugerenciaSeleccionada();

            if (indice < 0 || jefe.getCafeteria().getSugerencias() == null || indice >= jefe.getCafeteria().getSugerencias().size()) {
                throw new IllegalArgumentException("Selecciona una sugerencia válida.");
            }

            SugerenciaMenu sugerencia = jefe.getCafeteria().getSugerencias().get(indice);

            Administrador admin = (Administrador) jefe.getUsuarioActual();
            admin.aprobarSugerenciaMenu(sugerencia);

            // CAMBIO NUEVO PROYECTO 3 - Al aprobar una sugerencia,
            // el administrador también puede convertirla en producto real del menú.
            int opcion = JOptionPane.showConfirmDialog(
                    vista,
                    "¿Quieres agregar esta sugerencia como producto real del menú?\n\n" + sugerencia.getDescripcion(),
                    "Convertir sugerencia en producto",
                    JOptionPane.YES_NO_OPTION
            );

            if (opcion == JOptionPane.YES_OPTION) {
                String nombre = JOptionPane.showInputDialog(vista, "Nombre del producto:", sugerencia.getDescripcion());

                if (nombre != null && !nombre.trim().isEmpty()) {
                    String precioTexto = JOptionPane.showInputDialog(vista, "Precio del producto:", "0");
                    double precio = Double.parseDouble(precioTexto);

                    Object[] tipos = new Object[]{"Bebida", "Pastelería"};
                    Object tipo = JOptionPane.showInputDialog(
                            vista,
                            "Tipo de producto:",
                            "Tipo",
                            JOptionPane.QUESTION_MESSAGE,
                            null,
                            tipos,
                            tipos[0]
                    );

                    ProductoComestible producto;

                    if ("Bebida".equals(tipo)) {
                        producto = new Bebida(nombre.trim(), precio, false, false);
                    } else {
                        producto = new Pasteleria(nombre.trim(), precio, new ArrayList<String>());
                    }

                    admin.agregarProductoMenu(jefe.getCafeteria().getMenuCafeteria(), producto);
                }
            }
            guardarDatos();
            refrescarMenuYSugerencias();

            JOptionPane.showMessageDialog(
                    vista,
                    "Sugerencia aprobada correctamente.",
                    "Éxito",
                    JOptionPane.INFORMATION_MESSAGE
            );

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                    vista,
                    ex.getMessage(),
                    "Error sugerencia",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void rechazarSugerenciaDesdeGUI() {
        try {
            validarAdminActual();

            PanelMenuAdmin panel = panelAdmin.getPanelMenuAdmin();
            int indice = panel.getIndiceSugerenciaSeleccionada();

            if (indice < 0 || jefe.getCafeteria().getSugerencias() == null || indice >= jefe.getCafeteria().getSugerencias().size()) {
                throw new IllegalArgumentException("Selecciona una sugerencia válida.");
            }

            SugerenciaMenu sugerencia = jefe.getCafeteria().getSugerencias().get(indice);

            Administrador admin = (Administrador) jefe.getUsuarioActual();
            admin.rechazarSugerenciaMenu(sugerencia);

            guardarDatos();
            refrescarMenuYSugerencias();

            JOptionPane.showMessageDialog(
                    vista,
                    "Sugerencia rechazada correctamente.",
                    "Éxito",
                    JOptionPane.INFORMATION_MESSAGE
            );

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                    vista,
                    ex.getMessage(),
                    "Error sugerencia",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // CAMBIO TURNOS ADMIN
    private void configurarListenersTurnos() {
        PanelTurnosAdmin panel = panelAdmin.getPanelTurnosAdmin();

        panel.getBtnActualizar().addActionListener(e -> refrescarTurnosAdmin());

        panel.getBtnAsignarTurno().addActionListener(e -> asignarTurnoDesdeGUI());

        panel.getBtnQuitarTurno().addActionListener(e -> quitarTurnoDesdeGUI());

        panel.getBtnAprobarSolicitud().addActionListener(e -> aprobarSolicitudTurnoDesdeGUI());

        panel.getBtnRechazarSolicitud().addActionListener(e -> rechazarSolicitudTurnoDesdeGUI());
    }

    private void refrescarTurnosAdmin() {
        PanelTurnosAdmin panel = panelAdmin.getPanelTurnosAdmin();

        panel.getModeloEmpleados().setRowCount(0);
        panel.getModeloSolicitudes().setRowCount(0);
        panel.getCbEmpleado().removeAllItems();

        if (jefe.getCafeteria() == null || jefe.getCafeteria().getUsuarios() == null) {
            return;
        }

        for (Usuario usuario : jefe.getCafeteria().getUsuarios()) {
            if (!(usuario instanceof Empleado)) {
                continue;
            }

            Empleado empleado = (Empleado) usuario;

            panel.getCbEmpleado().addItem(empleado.getLogin());

            panel.getModeloEmpleados().addRow(new Object[]{
                    empleado.getLogin(),
                    empleado.getNombre(),
                    empleado instanceof Mesero ? "Mesero" : empleado instanceof Cocinero ? "Cocinero" : "Empleado",
                    construirTextoTurnos(empleado)
            });
        }

        if (jefe.getCafeteria().getSolicitudesTurno() != null) {
            for (int i = 0; i < jefe.getCafeteria().getSolicitudesTurno().size(); i++) {
                SolicitudTurno solicitud = jefe.getCafeteria().getSolicitudesTurno().get(i);

                if (solicitud == null) {
                    continue;
                }

                panel.getModeloSolicitudes().addRow(new Object[]{
                        i,
                        solicitud.getSolicitadoPor() != null ? solicitud.getSolicitadoPor().getLogin() : "N/A",
                        solicitud.getDia(),
                        solicitud.isEsIntercambio() ? "Intercambio" : "Cambio",
                        solicitud.getEstado()
                });
            }
        }
    }

    private String construirTextoTurnos(Empleado empleado) {
        StringBuilder sb = new StringBuilder();

        if (empleado == null || empleado.consultarDiasAsignados() == null || empleado.consultarDiasAsignados().isEmpty()) {
            return "Sin turnos";
        }

        for (DiaTurno turno : empleado.consultarDiasAsignados()) {
            if (turno != null) {
                if (sb.length() > 0) {
                    sb.append(", ");
                }

                sb.append(turno.getDia());

                if (!turno.estaAsignado()) {
                    sb.append(" (no asignado)");
                }
            }
        }

        return sb.toString();
    }

    private void asignarTurnoDesdeGUI() {
        try {
            validarAdminActual();

            PanelTurnosAdmin panel = panelAdmin.getPanelTurnosAdmin();

            Empleado empleado = buscarEmpleadoPorLogin(panel.getLoginEmpleadoSeleccionado());

            if (empleado == null) {
                throw new IllegalArgumentException("Selecciona un empleado válido.");
            }

            Administrador admin = (Administrador) jefe.getUsuarioActual();
            admin.asignarTurno(empleado, panel.getDiaSeleccionado());

            guardarDatos();
            refrescarTurnosAdmin();

            JOptionPane.showMessageDialog(
                    vista,
                    "Turno asignado correctamente.",
                    "Éxito",
                    JOptionPane.INFORMATION_MESSAGE
            );

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                    vista,
                    ex.getMessage(),
                    "Error turnos",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void quitarTurnoDesdeGUI() {
        try {
            validarAdminActual();

            PanelTurnosAdmin panel = panelAdmin.getPanelTurnosAdmin();

            Empleado empleado = buscarEmpleadoPorLogin(panel.getLoginEmpleadoSeleccionado());

            if (empleado == null) {
                throw new IllegalArgumentException("Selecciona un empleado válido.");
            }

            Administrador admin = (Administrador) jefe.getUsuarioActual();
            admin.quitarTurno(empleado, panel.getDiaSeleccionado(), jefe.getCafeteria());

            guardarDatos();
            refrescarTurnosAdmin();

            JOptionPane.showMessageDialog(
                    vista,
                    "Turno quitado correctamente.",
                    "Éxito",
                    JOptionPane.INFORMATION_MESSAGE
            );

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                    vista,
                    ex.getMessage(),
                    "Error turnos",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void aprobarSolicitudTurnoDesdeGUI() {
        try {
            validarAdminActual();

            PanelTurnosAdmin panel = panelAdmin.getPanelTurnosAdmin();
            int indice = panel.getIndiceSolicitudSeleccionada();

            if (indice < 0 || jefe.getCafeteria().getSolicitudesTurno() == null || indice >= jefe.getCafeteria().getSolicitudesTurno().size()) {
                throw new IllegalArgumentException("Selecciona una solicitud válida.");
            }

            SolicitudTurno solicitud = jefe.getCafeteria().getSolicitudesTurno().get(indice);

            Administrador admin = (Administrador) jefe.getUsuarioActual();
            admin.aprobarSolicitudTurno(solicitud);

            guardarDatos();
            refrescarTurnosAdmin();

            JOptionPane.showMessageDialog(
                    vista,
                    "Solicitud aprobada correctamente.",
                    "Éxito",
                    JOptionPane.INFORMATION_MESSAGE
            );

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                    vista,
                    ex.getMessage(),
                    "Error solicitud",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void rechazarSolicitudTurnoDesdeGUI() {
        try {
            validarAdminActual();

            PanelTurnosAdmin panel = panelAdmin.getPanelTurnosAdmin();
            int indice = panel.getIndiceSolicitudSeleccionada();

            if (indice < 0 || jefe.getCafeteria().getSolicitudesTurno() == null || indice >= jefe.getCafeteria().getSolicitudesTurno().size()) {
                throw new IllegalArgumentException("Selecciona una solicitud válida.");
            }

            SolicitudTurno solicitud = jefe.getCafeteria().getSolicitudesTurno().get(indice);

            Administrador admin = (Administrador) jefe.getUsuarioActual();
            admin.rechazarSolicitudTurno(solicitud);

            guardarDatos();
            refrescarTurnosAdmin();

            JOptionPane.showMessageDialog(
                    vista,
                    "Solicitud rechazada correctamente.",
                    "Éxito",
                    JOptionPane.INFORMATION_MESSAGE
            );

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                    vista,
                    ex.getMessage(),
                    "Error solicitud",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // CAMBIO HELPERS GENERALES
    private void validarAdminActual() {
        if (!(jefe.getUsuarioActual() instanceof Administrador)) {
            throw new IllegalStateException("Solo el administrador puede hacer esta operación.");
        }
    }

    private void guardarDatos() {
        if (jefe.getPersistencia() != null && jefe.getCafeteria() != null) {
            jefe.getPersistencia().guardarTodo(jefe.getCafeteria());
        }
    }

    private DiaSemana convertirDia(int diaCalendar) {
        switch (diaCalendar) {
            case Calendar.MONDAY:
                return DiaSemana.LUNES;
            case Calendar.TUESDAY:
                return DiaSemana.MARTES;
            case Calendar.WEDNESDAY:
                return DiaSemana.MIERCOLES;
            case Calendar.THURSDAY:
                return DiaSemana.JUEVES;
            case Calendar.FRIDAY:
                return DiaSemana.VIERNES;
            case Calendar.SATURDAY:
                return DiaSemana.SABADO;
            case Calendar.SUNDAY:
                return DiaSemana.DOMINGO;
            default:
                return DiaSemana.LUNES;
        }
    }

    private Juego buscarJuegoPorNombre(String nombreJuego) {
        if (nombreJuego == null || jefe.getCafeteria() == null || jefe.getCafeteria().getJuegos() == null) {
            return null;
        }

        for (Juego juego : jefe.getCafeteria().getJuegos()) {
            if (juego != null && nombreJuego.equals(juego.getNombre())) {
                return juego;
            }
        }

        return null;
    }

    private Usuario buscarUsuarioPorLogin(String login) {
        if (login == null || jefe.getCafeteria() == null || jefe.getCafeteria().getUsuarios() == null) {
            return null;
        }

        for (Usuario usuario : jefe.getCafeteria().getUsuarios()) {
            if (usuario != null && login.equals(usuario.getLogin())) {
                return usuario;
            }
        }

        return null;
    }

    private Empleado buscarEmpleadoPorLogin(String login) {
        Usuario usuario = buscarUsuarioPorLogin(login);

        if (usuario instanceof Empleado) {
            return (Empleado) usuario;
        }

        return null;
    }

    private CopiaPrestamo buscarCopiaPrestamo(Juego juego, String id) {
        if (juego == null || id == null || juego.getCopiasParaPrestamo() == null) {
            return null;
        }

        for (CopiaPrestamo copia : juego.getCopiasParaPrestamo()) {
            if (copia != null && id.equals(copia.getIdUnico())) {
                return copia;
            }
        }

        return null;
    }

    private CopiaVenta buscarCopiaVenta(Juego juego, String id) {
        if (juego == null || id == null || juego.getCopiasParaVenta() == null) {
            return null;
        }

        for (CopiaVenta copia : juego.getCopiasParaVenta()) {
            if (copia != null && id.equals(copia.getIdUnico())) {
                return copia;
            }
        }

        return null;
    }
}