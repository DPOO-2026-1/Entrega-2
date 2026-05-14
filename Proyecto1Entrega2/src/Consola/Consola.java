package Consola;

import java.util.Scanner;

import ModuloVenta.GestorVentas;
import Persistencia.GestorPersistencia;
import Usuario.GestorUsuarios;
import Usuario.Usuario;
import World.Cafeteria;

public abstract class Consola {

    protected Scanner scanner;
    protected Cafeteria cafeteria;
    protected GestorPersistencia persistencia;
    protected Usuario usuarioActual;
    protected boolean salir;

    public Consola() {
        this.scanner = new Scanner(System.in);
        this.persistencia = new GestorPersistencia("data/");

        GestorUsuarios gestorUsuarios = new GestorUsuarios(this.persistencia, null);
        GestorVentas gestorVentas = new GestorVentas(this.persistencia);

        this.cafeteria = Cafeteria.getInstance(80, "Board Nights", gestorUsuarios, gestorVentas);

        gestorUsuarios.setCafeteria(this.cafeteria);
        this.cafeteria.setGestorUsuarios(gestorUsuarios);
        this.cafeteria.setGestorVentas(gestorVentas);

        this.usuarioActual = null;
        this.salir = false;
    }

    public void iniciarAplicacion() {
        cargarDatos();

        boolean loginCorrecto = hacerLogin();

        if (!loginCorrecto) {
            System.out.println("No se pudo iniciar sesión.");
            guardarDatos();
            return;
        }

        while (!salir) {
            mostrarMenu();

            int opcion = pedirEntero("Seleccione una opción: ");

            if (opcion == 0) {
                salir = true;
            } else {
                ejecutarOpcion(opcion);
            }
        }

        guardarDatos();

        if (usuarioActual != null) {
            usuarioActual.cerrarSesion();
        }

        System.out.println("Aplicación cerrada correctamente.");
    }

    private void cargarDatos() {
        try {
            Cafeteria cargada = persistencia.cargarTodo();

            if (cargada != null) {
                this.cafeteria = cargada;
            }

            System.out.println("Datos cargados correctamente.");
        } catch (Exception e) {
            System.out.println("No se pudieron cargar los datos.");
            System.out.println("Detalle: " + e.getMessage());
        }
    }

    private void guardarDatos() {
        try {
            persistencia.guardarTodo(this.cafeteria);
            System.out.println("Datos guardados correctamente.");
        } catch (Exception e) {
            System.out.println("No se pudieron guardar los datos.");
            System.out.println("Detalle: " + e.getMessage());
        }
    }

    protected boolean hacerLogin() {
        System.out.println("=== INICIO DE SESIÓN ===");

        String login = pedirCadena("Login: ");
        String password = pedirCadena("Password: ");

        Usuario usuario = cafeteria.login(login, password);

        if (usuario == null) {
            System.out.println("Credenciales incorrectas.");
            return false;
        }

        if (!usuarioTienePermiso(usuario)) {
            System.out.println("El usuario existe, pero no tiene permiso para entrar a esta consola.");
            return false;
        }

        this.usuarioActual = usuario;
        System.out.println("Bienvenido, " + usuarioActual.getNombre() + ".");
        return true;
    }

    protected int pedirEntero(String mensaje) {
        while (true) {
            System.out.print(mensaje);

            String entrada = scanner.nextLine();

            try {
                return Integer.parseInt(entrada);
            } catch (NumberFormatException e) {
                System.out.println("Debe ingresar un número entero válido.");
            }
        }
    }

    protected double pedirDouble(String mensaje) {
        while (true) {
            System.out.print(mensaje);

            String entrada = scanner.nextLine();

            try {
                return Double.parseDouble(entrada);
            } catch (NumberFormatException e) {
                System.out.println("Debe ingresar un número válido.");
            }
        }
    }

    protected String pedirCadena(String mensaje) {
        System.out.print(mensaje);
        return scanner.nextLine();
    }

    protected boolean pedirBooleano(String mensaje) {
        while (true) {
            String entrada = pedirCadena(mensaje + " (s/n): ");

            if (entrada.equalsIgnoreCase("s")) {
                return true;
            } else if (entrada.equalsIgnoreCase("n")) {
                return false;
            } else {
                System.out.println("Ingrese solamente s o n.");
            }
        }
    }

    protected abstract boolean usuarioTienePermiso(Usuario usuario);

    protected abstract void mostrarMenu();

    protected abstract void ejecutarOpcion(int opcion);
}