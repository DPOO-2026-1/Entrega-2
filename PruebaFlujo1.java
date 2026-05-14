import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import World.Cafeteria;
import World.Mesa;
import World.Juego;
import World.CopiaPrestamo;
import World.Prestamo;
import Usuario.Cliente;

import java.util.ArrayList;
import java.util.List;

/**
 * Prueba de Flujo 1: Restricciones cruzadas de edad, menú y categorías de juego.
 *
 * Cubre:
 * - Verificación de aforo al asignar una mesa.
 * - Rechazo de bebidas alcohólicas en mesas con menores.
 * - Rechazo de juegos de Acción cuando hay bebidas calientes en la mesa.
 * - Aprobación del préstamo de un juego de Tablero apto para todo público.
 */
@DisplayName("Flujo 1 - Restricciones de Edad, Bebidas y Categoría de Juego")
public class PruebaFlujo1 {

    private Cafeteria cafeteria;
    private Mesa mesa;
    private Cliente cliente;

    @BeforeEach
    public void setUp() {
        // Reseteamos el singleton antes de cada prueba para aislarlas.
        // TODO: Cafeteria necesita un método resetInstance() para tests, ya que es Singleton.
        // Por ahora, intentamos obtenerla con capacidad para 20 personas.
        Cafeteria.resetInstance();
        cafeteria = Cafeteria.getInstance(20, "Cafe Test", null, null);

        // Creamos un cliente joven (no niño, pero no adulto completo)
        // El grupo tiene 4 personas, una de ellas menor de 18.
        // En el modelo, hayNinos=true representa a la persona menor de edad.
        cliente = new Cliente("cli01", "pass", "Grupo Mesa 1", false, false);

        // Creamos la mesa con capacidad para 4
        mesa = new Mesa(1, 4);
    }

    @Test
    @DisplayName("Paso 1: Asignar mesa si hay capacidad en el café")
    public void testAsignacionMesaConCapacidad() {
        int nPersonas = 4;

        // Verificar que el café tiene capacidad
        assertTrue(cafeteria.hayCapacidad(nPersonas),
            "El café debe tener capacidad para 4 personas con capacidad máx 20.");

        // Registrar el ingreso
        boolean ingresado = cafeteria.registrarIngreso(nPersonas);
        assertTrue(ingresado, "El ingreso debe haberse registrado exitosamente.");

        // Ocupar la mesa indicando que hay un menor (hayNinos = true)
        // Sin exception = éxito
        assertDoesNotThrow(() -> mesa.ocupar(nPersonas, true, false, cliente),
            "La mesa debe poder ocuparse con 4 personas y un menor.");

        // Verificar que la mesa ya NO está disponible
        assertFalse(mesa.estaDisponible(),
            "La mesa debe estar ocupada tras llamar a ocupar().");
    }

    @Test
    @DisplayName("Paso 2: Rechazar bebida alcohólica en mesa con menores")
    public void testRechazoBebidasAlcoholicasConMenores() {
        // Ocupamos la mesa con un menor (hayNinos = true)
        mesa.ocupar(4, true, false, cliente);

        // La mesa NO debe poder recibir bebidas alcohólicas
        assertFalse(mesa.puedeRecibirBebidaAlcoholica(),
            "No se deben servir bebidas alcohólicas en mesas con menores de edad.");
    }

    @Test
    @DisplayName("Paso 3: Bloquear juego de Acción en mesa con bebida caliente")
    public void testBloqueoPrestamoCategoriaAccionConBebidaCaliente() {
        // Ocupamos mesa sin menores para centrarnos en la restricción de bebida caliente
        mesa.ocupar(4, false, false, cliente);

        // Simulamos que la mesa ya tiene una bebida caliente
        mesa.setHayBebidaCaliente(true);

        // Creamos el juego Twister, categoría "Accion"
        Juego twister = new Juego("Twister", 1966, "Hasbro", 2, 6, 0, "Accion", false);

        // Un juego de Acción NO debe poder servirse con bebida caliente en la mesa
        assertFalse(mesa.puedeRecibirJuegoAccion(),
            "No se debe permitir un juego de Acción en una mesa con bebida caliente.");

        // Adicionalmente, la mesa tampoco puede recibir bebida caliente junto a un juego de Acción
        assertFalse(mesa.puedeRecibirBebidaCaliente(twister),
            "No se debe permitir bebida caliente en una mesa con un juego de Acción.");
    }

    @Test
    @DisplayName("Paso 4: Aprobar préstamo de juego de Tablero apto para todo público")
    public void testAprobacionPrestamoJuegoTableroAptoPúblico() {
        // Ocupamos la mesa con un menor
        mesa.ocupar(4, true, false, cliente);

        // Un juego de Tablero, apto desde 0 años, 2–6 jugadores → sin restricciones
        Juego caratula = new Juego("Caratula", 2015, "EmpresaY", 2, 6, 0, "Tablero", false);

        // Creamos una copia disponible del juego
        // TODO: CopiaPrestamo actualmente no tiene constructor público con parámetros expuestos.
        // Descomentar cuando el constructor esté disponible:
        CopiaPrestamo copia = new CopiaPrestamo("CP-001", "Nuevo", true, 0);
        caratula.agregarCopiaPrestamo(copia);

        // Verificar que el juego es apto para la edad mínima de la mesa (0 años para niños)
        int edadMesa = mesa.getEdadMinimaEnMesa();
        assertTrue(caratula.esAptoParaEdad(edadMesa),
            "El juego de Tablero debe ser apto para una mesa con niños (edad minima 0).");

        // Verificar que soporta el número de personas
        assertTrue(caratula.soportaNPersonas(4),
            "El juego de Tablero debe soportar 4 jugadores.");

        // Verificar que no hay bebida caliente para no tener conflicto con Acción
        // (el juego es de Tablero, así que siempre se puede)
        assertTrue(mesa.puedeRecibirBebidaCaliente(caratula),
            "Se debe poder servir bebida caliente en mesa con juego de Tablero.");
    }
}
