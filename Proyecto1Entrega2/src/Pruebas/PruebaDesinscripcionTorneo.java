import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import Torneo.TorneoAmistoso;
import Torneo.EstadoTorneo;
import Torneo.InscripcionTorneo;
import Usuario.DiaSemana;
import Usuario.Cliente;
import Usuario.Usuario;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;

@DisplayName("5. Desinscripción total")
public class PruebaDesinscripcionTorneo {

    private TorneoAmistoso torneo;
    private InscripcionTorneo inscripcion;

    @BeforeEach
    public void setUp() throws Exception {
        torneo = new TorneoAmistoso("T-001", DiaSemana.LUNES, new Date(), 120, null, 10, 2, 0, 0, new Date(), "Torneo Test", null, new ArrayList<>());
        
        Cliente cliente = new Cliente("cli1", "pass", "Cliente", false, false);
        List<Usuario> usuarios = new ArrayList<>();
        usuarios.add(cliente);
        
        // Cliente inscrito con 3 cupos regulares
        inscripcion = new InscripcionTorneo("I-001", new Date(), usuarios, 3, 0, 3, false, 0, true, false);
        torneo.inscribir(inscripcion);
    }

    @Test
    @DisplayName("Input: Cliente solicita desinscribirse. Outcome: Inscripción eliminada, 3 cupos regresan al total.")
    public void testDesinscripcionTotal() {
        assertEquals(3, torneo.getCupoOcupadoRegular(), "Deben haber 3 cupos ocupados antes de la desinscripción.");
        
        torneo.desinscribir(inscripcion);
        
        // Los 3 cupos regresan al total de cupos disponibles
        assertEquals(0, torneo.getCupoOcupadoRegular(), "Los cupos ocupados deben regresar a 0.");
        assertEquals(8, torneo.cuposDisponiblesRegulares(), "Los cupos regulares disponibles vuelven a ser 8.");
        
        // La inscripción se elimina por completo
        assertFalse(torneo.getInscripciones().contains(inscripcion), "La inscripción debe ser eliminada de la lista del torneo.");
    }
}
