package Consola;

import Usuario.Administrador;
import Usuario.Empleado;
import Usuario.Usuario;

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
        System.out.println("2. Crear torneo");
        System.out.println("0. Salir");
    }

    @Override
    protected void ejecutarOpcion(int opcion) {
        switch (opcion) {
            case 1:
                menuRegistrarEmpleado();
                break;

            case 2:
                menuCrearTorneo();
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

    private void menuCrearTorneo() {
        System.out.println();
        System.out.println("=== CREAR TORNEO ===");

        System.out.println("Este menú todavía depende de GestorTorneos.");
        System.out.println("En el UML existe GestorTorneos, pero en el código actual del proyecto todavía no está implementado.");
        System.out.println("Cuando implementes GestorTorneos, aquí se debe llamar a crearTorneoAmistoso() o crearTorneoCompetitivo().");

        /*
         * Código cuando se cree GestorTorneos
         *
         * String tipo = pedirCadena("Tipo de torneo (AMISTOSO/COMPETITIVO): ");
         * String nombreJuego = pedirCadena("Nombre del juego: ");
         * DiaSemana dia = DiaSemana.valueOf(pedirCadena("Día: ").toUpperCase());
         * String hora = pedirCadena("Hora: ");
         * int cupoTotal = pedirEntero("Cupo total: ");
         *
         * Juego juego = cafeteria.buscarJuego(nombreJuego);
         *
         * if (tipo.equalsIgnoreCase("AMISTOSO")) {
         *     double valorBono = pedirDouble("Valor del bono: ");
         *     cafeteria.getGestorTorneos().crearTorneoAmistoso(
         *         (Administrador) usuarioActual, juego, dia, hora, cupoTotal, valorBono
         *     );
         * } else if (tipo.equalsIgnoreCase("COMPETITIVO")) {
         *     double tarifa = pedirDouble("Tarifa de entrada: ");
         *     cafeteria.getGestorTorneos().crearTorneoCompetitivo(
         *         (Administrador) usuarioActual, juego, dia, hora, cupoTotal, tarifa
         *     );
         * }
         */
    }
}