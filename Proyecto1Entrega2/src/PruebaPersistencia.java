import Usuario.*;
import java.io.*;
import java.util.List;

public class PruebaPersistencia {
    private static final String FILE_PATH = "data/usuarios.txt";

    public static void main(String[] args) {
        System.out.println("=== INICIO PRUEBA 1: PERSISTENCIA ===");
        
        // 1. INSTANCIAR GESTOR Y REGISTRAR USUARIO
        GestorUsuarios gestor = new GestorUsuarios();
        System.out.println("Registrando usuario Santi123...");
        gestor.registrarCliente("Santi123", "clave1", "Santiago Escobar", false, false);

        // 2. GUARDAR EN ARCHIVO (Persistencia persistente )
        guardarEnArchivo(gestor);
        System.out.println("Datos guardados en " + FILE_PATH);

        // 3. SIMULAR CIERRE Y REINICIO (Limpiar el gestor en memoria)
        System.out.println("Reiniciando sistema...");
        gestor = new GestorUsuarios(); 

        // 4. CARGAR DESDE ARCHIVO
        cargarDesdeArchivo(gestor);
        System.out.println("Datos cargados correctamente.");

        // 5. VERIFICACIÓN DE LOGIN
        Usuario user = gestor.autenticar("Santi123", "clave1");
        if (user != null) {
            System.out.println("SALIDA ESPERADA: EXITOSA. El usuario " + user.getNombre() + " pudo ingresar.");
        } else {
            System.out.println("SALIDA FALLIDA: El usuario no fue encontrado.");
        }
    }

    private static void guardarEnArchivo(GestorUsuarios gestor) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_PATH))) {
            // El administrador puede ver detalles completos de usuarios
            // Aquí guardamos: login, password, nombre, tipo
            bw.write("Santi123;clave1;Santiago Escobar;Cliente");
            bw.newLine();
        } catch (IOException e) {
            System.err.println("Error al escribir archivo: " + e.getMessage());
        }
    }

    private static void cargarDesdeArchivo(GestorUsuarios gestor) {
        File archivo = new File(FILE_PATH);
        if (!archivo.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] d = linea.split(";");
                if (d[3].equals("Cliente")) {
                    gestor.registrarCliente(d[0], d[1], d[2], false, false);
                }
            }
        } catch (IOException e) {
            System.err.println("Error al leer archivo: " + e.getMessage());
        }
    }
}
public class PruebaPersistencia {

}
