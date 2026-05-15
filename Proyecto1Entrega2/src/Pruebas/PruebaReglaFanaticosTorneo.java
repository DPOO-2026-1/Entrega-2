package Pruebas;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import Torneo.TorneoAmistoso;
import Torneo.EstadoTorneo;
import Torneo.GestorTorneo;
import Torneo.InscripcionTorneo;
import Usuario.DiaSemana;
import Usuario.Cliente;
import Usuario.Usuario;
import World.Juego;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;

@DisplayName("4. Regla del 20% para fanáticos")
public class PruebaReglaFanaticosTorneo {

    private TorneoAmistoso torneo;
    private Cliente clienteFanatico;
    private Juego juego;

    @BeforeEach
    public void setUp() {
        juego = new Juego("Catan", 1995, "Kosmos", 3, 4, 10, "Tablero", false);
        // Torneo con 10 cupos totales -> 2 reservados.
        torneo = new TorneoAmistoso("T-001", DiaSemana.LUNES, new Date(), 120, null, 10, 2, 0, 0, new Date(), "Torneo Catan", juego, new ArrayList<>());
        
        clienteFanatico = new Cliente("fan1", "pass", "Fanatico", false, false);
        clienteFanatico.agregarFavorito(juego);
    }

    @Test
    @DisplayName("Input: Cliente fanático toma 1 cupo. Outcome: Inscripción usa cupo reservado. Queda 1 reservado y 8 regulares.")
    public void testUsoCupoReservado() {
        // Crear gestor y registrar torneo
        GestorTorneo gestorTorneo = new GestorTorneo();
        gestorTorneo.getCatalogoTorneos().add(torneo); // registrar torneo en el catálogo

        // Verificar que el sistema lo reconoce como fanático
        assertTrue(torneo.esFanatico(clienteFanatico),
            "El cliente debe ser reconocido como fanático del juego del torneo.");

        // Fanático toma 1 cupo reservado
        assertDoesNotThrow(() -> gestorTorneo.inscribir(clienteFanatico, torneo.getIdTorneo(), 1),
            "La inscripción del fanático debe ser exitosa.");

        // Queda 1 reservado y 8 regulares
        assertEquals(1, torneo.cuposDisponiblesReservados(),
            "Debe quedar 1 cupo reservado disponible.");
        assertEquals(8, torneo.cuposDisponiblesRegulares(),
            "Deben quedar 8 cupos regulares disponibles.");
    }
}
