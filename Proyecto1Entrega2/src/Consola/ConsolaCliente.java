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