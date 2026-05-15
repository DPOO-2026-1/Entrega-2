package Consola;

import java.util.List;

import Torneo.Torneo;
import Usuario.Cliente;
import Usuario.DiaSemana;
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
        System.out.println("1. Listar torneos por día");
        System.out.println("2. Inscribirse a torneo");
        System.out.println("3. Desinscribirse de torneo");
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
                menuDesinscribirTorneo();
                break;

            default:
                System.out.println("Opción inválida.");
                break;
        }
    }

    // CAMBIO INTERFAZ: listado de torneos disponibles.
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

    // CAMBIO INTERFAZ: inscripción real delegada a GestorTorneo.
    private void menuInscribirTorneo() {
        System.out.println();
        System.out.println("=== INSCRIBIRSE A TORNEO ===");

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

    // CAMBIO INTERFAZ: desinscripción real delegada a GestorTorneo.
    private void menuDesinscribirTorneo() {
        System.out.println();
        System.out.println("=== DESINSCRIBIRSE DE TORNEO ===");

        try {
            String torneoId = pedirCadena("ID del torneo: ");

            gestorTorneo.desinscribir(usuarioActual, torneoId);

            System.out.println("Desinscripción realizada correctamente.");
        } catch (Exception e) {
            System.out.println("No se pudo realizar la desinscripción.");
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