package Consola;

import Usuario.Empleado;
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
        System.out.println("1. Inscribirse a torneo");
        System.out.println("0. Salir");
    }

    @Override
    protected void ejecutarOpcion(int opcion) {
        switch (opcion) {
            case 1:
                menuInscribirTorneo();
                break;

            default:
                System.out.println("Opción inválida.");
                break;
        }
    }

    private void menuInscribirTorneo() {
        System.out.println();
        System.out.println("=== INSCRIPCIÓN DE EMPLEADO A TORNEO ===");

        System.out.println("Este menú todavía depende de GestorTorneos.");
        System.out.println("Cuando GestorTorneos exista, primero se debe llamar a validarEmpleadoSinTurno().");

        /*
         * Código esperado cuando exista GestorTorneos:
         *
         * Empleado empleado = (Empleado) usuarioActual;
         *
         * DiaSemana dia = DiaSemana.valueOf(pedirCadena("Día del torneo: ").toUpperCase());
         * String hora = pedirCadena("Hora del torneo: ");
         *
         * boolean puede = cafeteria.getGestorTorneos().validarEmpleadoSinTurno(
         *     empleado,
         *     dia,
         *     hora
         * );
         *
         * if (!puede) {
         *     System.out.println("No puede inscribirse porque tiene turno en ese horario.");
         *     return;
         * }
         *
         * String torneoId = pedirCadena("ID del torneo: ");
         * int cantidadCupos = pedirEntero("Cantidad de cupos: ");
         *
         * cafeteria.getGestorTorneos().inscribir(empleado, torneoId, cantidadCupos);
         */
    }
}