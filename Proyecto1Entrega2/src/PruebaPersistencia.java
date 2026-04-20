import Usuario.*;
import java.io.*;
import java.util.List;

public class PruebaPersistencia {
    private static final String FILE_PATH = "data/usuarios.txt";

    public static void main(String[] args) {
        new File("data").mkdirs(); 

        System.out.println("=== INICIO PRUEBA 1: PERSISTENCIA ===");
        
        GestorUsuarios gestor = new GestorUsuarios();
        System.out.println("Registrando usuario Santi123...");
        gestor.registrarCliente("Santi123", "clave1", "Santiago Escobar", false, false);

        // 2. GUARDAR (Ahora sí guarda lo que hay en el gestor)
        guardarEnArchivo(gestor);
        System.out.println("Datos guardados en " + FILE_PATH);

        System.out.println("Reiniciando sistema...");
        gestor = new GestorUsuarios(); 

        // 4. CARGAR
        cargarDesdeArchivo(gestor);
        System.out.println("Datos cargados correctamente.");

        // 5. VERIFICACIÓN
        Usuario user = gestor.autenticar("Santi123", "clave1");
        if (user != null) {
            System.out.println("SALIDA ESPERADA: EXITOSA. El usuario " + user.getNombre() + " pudo ingresar.");
        } else {
            System.out.println("SALIDA FALLIDA: El usuario no fue encontrado.");
        }
    }

    private static void guardarEnArchivo(GestorUsuarios gestor) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (Usuario u : gestor.getUsuarios()) {
                String tipo = (u instanceof Cliente) ? "Cliente" : "Empleado";
                bw.write(u.getLogin() + ";" + u.getPassword() + ";" + u.getNombre() + ";" + tipo);
                bw.newLine();
            }
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
                if (linea.trim().isEmpty()) continue;
                String[] d = linea.split(";");
                if (d.length >= 4) { 
                    if (d[3].equals("Cliente")) {
                        gestor.registrarCliente(d[0], d[1], d[2], false, false);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error al leer archivo: " + e.getMessage());
        }
    }
}