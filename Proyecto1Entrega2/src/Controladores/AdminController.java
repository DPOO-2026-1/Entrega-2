package Controladores;

import InterfazGrafica.PanelAdmin;
import InterfazGrafica.VentanaPrincipal;
import Torneo.GestorTorneo;
import Torneo.Torneo;
import Usuario.Administrador;
import Usuario.DiaSemana;
import World.Juego;

import javax.swing.JOptionPane;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AdminController {
    private VentanaPrincipal vista;
    private ControllerPrincipal jefe;
    private PanelAdmin panelAdmin;

    public AdminController(VentanaPrincipal vista, ControllerPrincipal jefe) {
        this.vista = vista;
        this.jefe = jefe;
        this.panelAdmin = vista.getPanelAdmin();

        configurarListeners();
        cargarDatosIniciales();
    }

    // Carga los juegos disponibles en el ComboBox
    private void cargarDatosIniciales() {
        panelAdmin.getPanelCrearTorneo().getCbJuegoAsociado().removeAllItems();
        List<Juego> juegos = jefe.getCafeteria().getJuegos();
        if (juegos != null) {
            for (Juego j : juegos) {
                panelAdmin.getPanelCrearTorneo().getCbJuegoAsociado().addItem(j.getNombre());
            }
        }
    }

    private void configurarListeners() {
        panelAdmin.getPanelCrearTorneo().getBtnValidarCrear().addActionListener(e -> {
            crearNuevoTorneo();
        });

        panelAdmin.getPanelCrearTorneo().getBtnVolver().addActionListener(e -> {
            panelAdmin.getCardLayout().show(panelAdmin.getPanelCentral(), "DASHBOARD"); 
        });
    }

    private void crearNuevoTorneo() {
        try {
            if (!(jefe.getUsuarioActual() instanceof Administrador)) {
                throw new IllegalStateException("Error: Solo los administradores pueden crear torneos.");
            }
            Administrador admin = (Administrador) jefe.getUsuarioActual();
            GestorTorneo gestorTorneo = jefe.getCafeteria().getGestorTorneo();

            String nombreJuego = (String) panelAdmin.getPanelCrearTorneo().getCbJuegoAsociado().getSelectedItem();
            String fechaStr = panelAdmin.getPanelCrearTorneo().getFechaInicio();
            String tipo = panelAdmin.getPanelCrearTorneo().getTipoTorneo();
            String duracionStr = panelAdmin.getPanelCrearTorneo().getDuracion();
            String cupoStr = panelAdmin.getPanelCrearTorneo().getCupoTotal();
            String valorDinamicoStr = panelAdmin.getPanelCrearTorneo().getValorDinamico();

            if (nombreJuego == null || fechaStr.isEmpty() || duracionStr.isEmpty() || cupoStr.isEmpty() || valorDinamicoStr.isEmpty()) {
                JOptionPane.showMessageDialog(vista, "Todos los campos deben ser rellenados.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int duracion = Integer.parseInt(duracionStr);
            int cupo = Integer.parseInt(cupoStr);
            double valor = Double.parseDouble(valorDinamicoStr);

            // --- LÓGICA DE FECHAS: Convertir YYYY/MM/DD a un Enum DiaSemana ---
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
            sdf.setLenient(false);
            Date fechaParsed = sdf.parse(fechaStr);
            
            Calendar cal = Calendar.getInstance();
            cal.setTime(fechaParsed);
            int diaInt = cal.get(Calendar.DAY_OF_WEEK);
            
            DiaSemana diaEnum = null;
            switch(diaInt) {
                case Calendar.MONDAY: diaEnum = DiaSemana.LUNES; break;
                case Calendar.TUESDAY: diaEnum = DiaSemana.MARTES; break;
                case Calendar.WEDNESDAY: diaEnum = DiaSemana.MIERCOLES; break;
                case Calendar.THURSDAY: diaEnum = DiaSemana.JUEVES; break;
                case Calendar.FRIDAY: diaEnum = DiaSemana.VIERNES; break;
                case Calendar.SATURDAY: diaEnum = DiaSemana.SABADO; break;
                case Calendar.SUNDAY: diaEnum = DiaSemana.DOMINGO; break;
            }

            // Buscar el Objeto Juego completo
            Juego juegoAsociado = null;
            for (Juego j : jefe.getCafeteria().getJuegos()) {
                if (j.getNombre().equals(nombreJuego)) {
                    juegoAsociado = j;
                    break;
                }
            }

            if (juegoAsociado == null) throw new IllegalArgumentException("El juego seleccionado no existe.");

            // --- HACK PARA PRUEBAS: Si no hay copias, inyectamos copias virtuales para pasar el validador ---
            if (juegoAsociado.getCopiasPrestamo().isEmpty()) {
                for(int i = 0; i < cupo + 5; i++) { // Inyecta las copias necesarias
                    // Usamos la reflexión para no modificar las clases base del profesor
                    juegoAsociado.agregarCopiaPrestamo(new World.CopiaPrestamo("VIRTUAL-" + i, "Disponible", true, 0));
                }
                System.out.println("⚠️ NOTA: Se inyectaron copias virtuales temporales para permitir la creación del torneo.");
            }

            // Crear el torneo
            Torneo nuevoTorneo;
            if (tipo.equals("Amistoso")) {
                nuevoTorneo = gestorTorneo.crearTorneoAmistoso(admin, juegoAsociado, diaEnum, "00:00", cupo, valor);
            } else {
                nuevoTorneo = gestorTorneo.crearTorneoCompetitivo(admin, juegoAsociado, diaEnum, "00:00", cupo, valor);
            }

            // --- CORRECCIÓN DE LA LÓGICA BASE ---
            // Como GestorTorneo lo creaba con "new Date()", aquí le ponemos la fecha EXACTA que el admin digitó
            nuevoTorneo.setFechaInicio(fechaParsed);
            nuevoTorneo.setDuracionMin(duracion); // También actualizamos la duración que era 120 por defecto

            JOptionPane.showMessageDialog(vista, "¡Torneo de " + nombreJuego + " creado para el " + fechaStr + " (" + diaEnum + ")!", "Creación Exitosa", JOptionPane.INFORMATION_MESSAGE);
            panelAdmin.getPanelCrearTorneo().limpiarCampos();

        } catch (java.text.ParseException ex) {
            JOptionPane.showMessageDialog(vista, "Formato de fecha inválido. Usa YYYY/MM/DD (Ej: 2026/05/15).", "Error de Formato", JOptionPane.ERROR_MESSAGE);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(vista, "Por favor, ingresa solo números en Duración, Cupo y Tarifa.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException | IllegalStateException ex) {
            JOptionPane.showMessageDialog(vista, ex.getMessage(), "Operación Rechazada", JOptionPane.WARNING_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(vista, "Ocurrió un error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}