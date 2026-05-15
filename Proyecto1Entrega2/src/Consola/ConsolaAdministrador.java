package Consola;

import java.util.List;

import Torneo.Torneo;
import Torneo.TorneoAmistoso;
import Torneo.TorneoCompetitivo;
import Usuario.Administrador;
import Usuario.DiaSemana;
import Usuario.Empleado;
import Usuario.Usuario;
import World.Juego;

public class ConsolaAdministrador extends Consola {

    public static void main(String[] args) {
        ConsolaAdministrador consola = new ConsolaAdministrador();
        consola.iniciarAplicacion();
    }

    @Override
    protected boolean usuarioTienePermiso(Usuario usuario) {
        return usuario instanceof Administrador;
    }

    @Override
    protected void mostrarMenu() {
    System.out.println();
    System.out.println("=== MENÚ ADMINISTRADOR ===");
    System.out.println("1. Registrar empleado");
    System.out.println("2. Crear torneo amistoso");
    System.out.println("3. Crear torneo competitivo");
    System.out.println("4. Listar torneos por día");

    // ===== CAMBIO HECHO =====
    // Nueva opción para finalizar un torneo desde la consola del administrador.
    System.out.println("5. Finalizar torneo");
    // ===== FIN CAMBIO =====

    System.out.println("0. Salir");
    }

    @Override
    protected void ejecutarOpcion(int opcion) {
    switch (opcion) {
        case 1:
            menuRegistrarEmpleado();
            break;

        case 2:
            menuCrearTorneoAmistoso();
            break;

        case 3:
            menuCrearTorneoCompetitivo();
            break;

        case 4:
            menuListarTorneosPorDia();
            break;

        // ===== CAMBIO HECHO =====
        // Opción nueva del menú.
        case 5:
            menuFinalizarTorneo();
            break;
        // ===== FIN CAMBIO =====

        default:
            System.out.println("Opción inválida.");
            break;
    }
}

    private void menuRegistrarEmpleado() {
        System.out.println();
        System.out.println("=== REGISTRAR EMPLEADO ===");

        String login = pedirCadena("Login: ");
        String password = pedirCadena("Password: ");
        String nombre = pedirCadena("Nombre: ");
        String tipo = pedirCadena("Tipo de empleado (MESERO/COCINERO): ");
        String codigoDescuento = pedirCadena("Código de descuento: ");

        try {
            Empleado empleado = cafeteria.getGestorUsuarios().registrarEmpleado(
                    login,
                    password,
                    nombre,
                    tipo,
                    codigoDescuento
            );

            System.out.println("Empleado registrado correctamente: " + empleado.getNombre());
        } catch (Exception e) {
            System.out.println("No se pudo registrar el empleado.");
            System.out.println("Detalle: " + e.getMessage());
        }
    }

    // CAMBIO INTERFAZ: creación real de torneo amistoso usando GestorTorneo.
    private void menuCrearTorneoAmistoso() {
        System.out.println();
        System.out.println("=== CREAR TORNEO AMISTOSO ===");

        try {
            String nombreJuego = pedirCadena("Nombre del juego: ");
            Juego juego = cafeteria.buscarJuego(nombreJuego);

            if (juego == null) {
                System.out.println("No existe un juego con ese nombre.");
                return;
            }

            DiaSemana dia = pedirDiaSemana("Día del torneo: ");
            String hora = pedirCadena("Hora del torneo (ej. 15:30): ");
            int cupoTotal = pedirEntero("Cupo total: ");
            double valorBono = pedirDouble("Valor del bono: ");

            TorneoAmistoso torneo = gestorTorneo.crearTorneoAmistoso(
                    (Administrador) usuarioActual,
                    juego,
                    dia,
                    hora,
                    cupoTotal,
                    valorBono
            );

            System.out.println("Torneo amistoso creado correctamente.");
            imprimirTorneo(torneo);
        } catch (Exception e) {
            System.out.println("No se pudo crear el torneo amistoso.");
            System.out.println("Detalle: " + e.getMessage());
        }
    }

    // CAMBIO INTERFAZ: creación real de torneo competitivo usando GestorTorneo.
    private void menuCrearTorneoCompetitivo() {
        System.out.println();
        System.out.println("=== CREAR TORNEO COMPETITIVO ===");

        try {
            String nombreJuego = pedirCadena("Nombre del juego: ");
            Juego juego = cafeteria.buscarJuego(nombreJuego);

            if (juego == null) {
                System.out.println("No existe un juego con ese nombre.");
                return;
            }

            DiaSemana dia = pedirDiaSemana("Día del torneo: ");
            String hora = pedirCadena("Hora del torneo (ej. 15:30): ");
            int cupoTotal = pedirEntero("Cupo total: ");
            double tarifa = pedirDouble("Tarifa de entrada: ");

            TorneoCompetitivo torneo = gestorTorneo.crearTorneoCompetitivo(
                    (Administrador) usuarioActual,
                    juego,
                    dia,
                    hora,
                    cupoTotal,
                    tarifa
            );

            System.out.println("Torneo competitivo creado correctamente.");
            imprimirTorneo(torneo);
        } catch (Exception e) {
            System.out.println("No se pudo crear el torneo competitivo.");
            System.out.println("Detalle: " + e.getMessage());
        }
    }

    // CAMBIO INTERFAZ: listado de torneos por día.
    private void menuListarTorneosPorDia() {
        System.out.println();
        System.out.println("=== LISTAR TORNEOS POR DÍA ===");

        DiaSemana dia = pedirDiaSemana("Día a consultar: ");
        List<Torneo> torneos = gestorTorneo.getTorneos(dia);

        if (torneos.isEmpty()) {
            System.out.println("No hay torneos registrados para ese día.");
            return;
        }

        for (Torneo torneo : torneos) {
            imprimirTorneo(torneo);
        }
    }

    // ===== CAMBIO HECHO =====
    // Permite finalizar un torneo desde la consola de administrador.
    // Pide el ID del torneo y el login del ganador, delega la lógica al GestorTorneo
    // y muestra si el resultado fue bono o premio metálico.
    // ===== FIN CAMBIO =====
    private void menuFinalizarTorneo() {
    System.out.println();
    System.out.println("=== FINALIZAR TORNEO ===");

    try {
        String torneoId = pedirCadena("ID del torneo: ");
        String loginGanador = pedirCadena("Login del ganador: ");

        Usuario ganador = cafeteria.getGestorUsuarios().buscarUsuario(loginGanador);

        if (ganador == null) {
            System.out.println("No existe un usuario con ese login.");
            return;
        }

        ResultadoTorneo resultado = getGestorTorneo().finalizarTorneo(
                (Administrador) usuarioActual,
                torneoId,
                ganador
        );

        System.out.println("Torneo finalizado correctamente.");
        System.out.println("Ganador: " + resultado.getGanador().getNombre());

        BonoTorneoAmistoso bono = resultado.getBono();

        if (bono != null) {
            System.out.println("Premio: bono de torneo amistoso.");
            System.out.println("Código del bono: " + bono.getCodigo());
            System.out.println("Valor del bono: " + bono.getValor());
        } else if (resultado.getPremioMetalico() > 0) {
            System.out.println("Premio: premio metálico.");
            System.out.println("Valor del premio: " + resultado.getPremioMetalico());
        } else {
            System.out.println("Premio: sin premio metálico ni bono aplicable.");
        }
    } catch (Exception e) {
        System.out.println("No se pudo finalizar el torneo.");
        System.out.println("Detalle: " + e.getMessage());
    }
}

    // CAMBIO INTERFAZ: método auxiliar solo de impresión.
    private void imprimirTorneo(Torneo torneo) {
        System.out.println("--------------------------------");
        System.out.println("ID: " + torneo.getIdTorneo());
        System.out.println("Nombre: " + torneo.getNombre());
        System.out.println("Día: " + torneo.getDia());
        System.out.println("Juego: " + torneo.getJuegoTorneo().getNombre());
        System.out.println("Cupo total: " + torneo.getCupoTotal());
        System.out.println("Reservados disponibles: " + torneo.cuposDisponiblesReservados());
        System.out.println("Regulares disponibles: " + torneo.cuposDisponiblesRegulares());
        System.out.println("Estado: " + torneo.getEstado());
        System.out.println("--------------------------------");
    }
}