package Consola;

import java.util.List;

import Torneo.Torneo;
import Usuario.DiaSemana;
import Usuario.Empleado;
import Usuario.SolicitudTurno;
import Usuario.Usuario;

public class ConsolaEmpleado extends Consola {

    public static void main(String[] args) {
        ConsolaEmpleado consola = new ConsolaEmpleado();
        consola.iniciarAplicacion();
    }

    @Override
    protected boolean usuarioTienePermiso(Usuario usuario) {
        return usuario instanceof Empleado;
    }

    @Override
    protected void mostrarMenu() {
        System.out.println();
        System.out.println("=== MENÚ EMPLEADO ===");
        System.out.println("1. Listar torneos por día");
        System.out.println("2. Inscribirse a torneo");
        System.out.println("3. Solicitar cambio de turno");
        System.out.println("0. Salir");
    }

    @Override
    protected void ejecutarOpcion(int opcion) {
        switch (opcion) {
            case 1:
                menuListarTorneosPorDia();
                break;

            case 2:
                menuInscribirTorneo();
                break;

            case 3:
                menuSolicitarCambioTurno();
                break;

            default:
                System.out.println("Opción inválida.");
                break;
        }
    }

    // CAMBIO INTERFAZ: listar torneos.
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

    // CAMBIO INTERFAZ: inscripción delegada a GestorTorneo.
    // La validación de turno NO se hace aquí; ya está en GestorTorneo.inscribir().
    private void menuInscribirTorneo() {
        System.out.println();
        System.out.println("=== INSCRIPCIÓN DE EMPLEADO A TORNEO ===");

        try {
            DiaSemana dia = pedirDiaSemana("Día del torneo: ");
            List<Torneo> torneos = gestorTorneo.getTorneos(dia);

            if (torneos.isEmpty()) {
                System.out.println("No hay torneos registrados para ese día.");
                return;
            }

            for (Torneo torneo : torneos) {
                imprimirTorneo(torneo);
            }

            String torneoId = pedirCadena("ID del torneo: ");
            int cantidadCupos = pedirEntero("Cantidad de cupos a tomar (1 a 3): ");

            gestorTorneo.inscribir(usuarioActual, torneoId, cantidadCupos);

            System.out.println("Inscripción realizada correctamente.");
        } catch (Exception e) {
            System.out.println("No se pudo realizar la inscripción.");
            System.out.println("Detalle: " + e.getMessage());
        }
    }

    // CAMBIO INTERFAZ: cambio de turno desde consola de empleado.
    private void menuSolicitarCambioTurno() {
        System.out.println();
        System.out.println("=== SOLICITAR CAMBIO DE TURNO ===");

        try {
            Empleado empleado = (Empleado) usuarioActual;

            DiaSemana dia = pedirDiaSemana("Día del turno que desea cambiar: ");

            SolicitudTurno solicitud = empleado.solicitarCambioTurno(dia);

            System.out.println("Solicitud de cambio de turno creada correctamente.");

            if (solicitud != null) {
                System.out.println("Descripción: " + solicitud.getDescripcion());
                System.out.println("Estado: " + solicitud.getEstado());
            }
        } catch (Exception e) {
            System.out.println("No se pudo crear la solicitud de cambio de turno.");
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