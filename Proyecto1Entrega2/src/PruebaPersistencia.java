import Usuario.GestorUsuarios;
import Usuario.Usuario;
import World.Cafeteria;
import ModuloVenta.GestorVentas;
import Persistencia.GestorPersistencia;
import java.io.File;
	
public class PruebaPersistencia {
    private static final String DATA_DIR = "data/";

    public static void main(String[] args) {
        new File(DATA_DIR).mkdirs(); 
        
        System.out.println("=== INICIO PRUEBA 1: PERSISTENCIA ===");
        
        GestorPersistencia gp = new GestorPersistencia(DATA_DIR);
        GestorVentas gv = new GestorVentas(gp);
        Cafeteria cafe = Cafeteria.getInstance(50, "Cafe Central", null, gv);
        
        // Cargar todo (si existiera algo previo)
        gp.cargarTodo();
        
        GestorUsuarios gestor = new GestorUsuarios(gp, cafe);
        cafe.setGestorUsuarios(gestor);

        System.out.println("Registrando usuario Santi123...");
        try {
            gestor.registrarCliente("Santi123", "clave1", "Santiago Escobar", false, false);
            System.out.println("Usuario registrado correctamente.");
        } catch (IllegalArgumentException e) {
            System.out.println("El usuario ya estaba registrado: " + e.getMessage());
        }

        System.out.println("Guardando el estado completo a traves de GestorPersistencia...");
        gp.guardarTodo(cafe);
        System.out.println("Datos guardados en " + DATA_DIR);

        // Simulamos un reinicio instanciando un nuevo GestorPersistencia y limpiando la lista actual
        System.out.println("Reiniciando sistema...");
        cafe.getGestorUsuarios().getUsuarios().clear();

        // 4. CARGAR
        gp.cargarTodo();
        System.out.println("Datos cargados correctamente desde CSVs.");

        // 5. VERIFICACIÓN
        Usuario user = gestor.autenticar("Santi123", "clave1");
        if (user != null) {
            System.out.println("SALIDA ESPERADA: EXITOSA. El usuario " + user.getNombre() + " pudo ingresar.");
        } else {
            System.out.println("SALIDA FALLIDA: El usuario no fue encontrado.");
        }
    }
}