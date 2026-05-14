package Consola;

import Usuario.Cliente;
import Usuario.Usuario;

public class ConsolaCliente extends Consola {

    public static void main(String[] args) {
        ConsolaCliente consola = new ConsolaCliente();
        consola.iniciarAplicacion();
    }

    @Override
    protected boolean usuarioTienePermiso(Usuario usuario) {
        return usuario instanceof Cliente;
    }

    @Override
    protected boolean hacerLogin() {
        System.out.println("=== CONSOLA CLIENTE ===");
        System.out.println("1. Iniciar sesión");
        System.out.println("2. Registrarse");

        int opcion = pedirEntero("Seleccione una opción: ");

        if (opcion == 1) {
            return super.hacerLogin();
        } else if (opcion == 2) {
            return registrarCliente();
        } else {
            System.out.println("Opción inválida.");
            return false;
        }
    }

    private boolean registrarCliente() {
        System.out.println();
        System.out.println("=== REGISTRO DE CLIENTE ===");

        String login = pedirCadena("Login: ");
        String password = pedirCadena("Password: ");
        String nombre = pedirCadena("Nombre: ");
        boolean esNino = pedirBooleano("¿Es niño?");
        boolean esJoven = pedirBooleano("¿Es joven?");

        try {
            Cliente cliente = cafeteria.getGestorUsuarios().registrarCliente(
                    login,
                    password,
                    nombre,
                    esNino,
                    esJoven
            );

            this.usuarioActual = cliente;

            System.out.println("Cliente registrado correctamente.");
            System.out.println("Bienvenido, " + cliente.getNombre() + ".");

            return true;
        } catch (Exception e) {
            System.out.println("No se pudo registrar el cliente.");
            System.out.println("Detalle: " + e.getMessage());
            return false;
        }
    }

    @Override
    protected void mostrarMenu() {
        System.out.println();
        System.out.println("=== MENÚ CLIENTE ===");
        System.out.println("1. Inscribirse a torneo");
        System.out.println("2. Desinscribirse de torneo");
        System.out.println("0. Salir");
    }

    @Override
    protected void ejecutarOpcion(int opcion) {
        switch (opcion) {
            case 1:
                menuInscribirTorneo();
                break;

            case 2:
                menuDesinscribirTorneo();
                break;

            default:
                System.out.println("Opción inválida.");
                break;
        }
    }

    private void menuInscribirTorneo() {
        System.out.println();
        System.out.println("=== INSCRIBIRSE A TORNEO ===");

        System.out.println("Este menú todavía depende de GestorTorneos.");
        System.out.println("En el UML existe, pero en el código actual todavía no está implementado.");

        /*
         * Código esperado cuando exista GestorTorneos:
         *
         * DiaSemana dia = DiaSemana.valueOf(pedirCadena("Día del torneo: ").toUpperCase());
         * List<Torneo> torneos = cafeteria.getGestorTorneos().getTorneos(dia);
         *
         * for (Torneo t : torneos) {
         *     System.out.println(t.getId_torneo() + " - " + t.getNombre());
         * }
         *
         * String torneoId = pedirCadena("ID del torneo: ");
         * int cantidadCupos = pedirEntero("Cantidad de cupos: ");
         *
         * cafeteria.getGestorTorneos().inscribir(usuarioActual, torneoId, cantidadCupos);
         */
    }

    private void menuDesinscribirTorneo() {
        System.out.println();
        System.out.println("=== DESINSCRIBIRSE DE TORNEO ===");

        System.out.println("Este menú todavía depende de GestorTorneos.");

        /*
         * Código esperado cuando exista GestorTorneos:
         *
         * String torneoId = pedirCadena("ID del torneo: ");
         * cafeteria.getGestorTorneos().desinscribir(usuarioActual, torneoId);
         */
    }
}