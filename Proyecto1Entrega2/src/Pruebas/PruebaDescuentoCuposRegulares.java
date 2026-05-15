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

@DisplayName("3. Descuento correcto de cupos regulares")
public class PruebaDescuentoCuposRegulares {

    private TorneoAmistoso torneo;
    private Cliente clienteNormal;

    @BeforeEach
    public void setUp() {
        // Torneo con 10 cupos totales y 2 reservados para fanáticos. 8 regulares.
        torneo = new TorneoAmistoso("T-001", DiaSemana.LUNES, new Date(), 120, null, 10, 2, 0, 0, new Date(), "Torneo Test", null, new ArrayList<>());
        clienteNormal = new Cliente("cli1", "pass", "Cliente Normal", false, false);
    }

    @Test
    @DisplayName("Input: Torneo con 10 cupos. Cliente normal toma 2 cupos. Outcome: Inscripción exitosa y descuento correcto.")
    public void testDescuentoCuposRegulares() {
        List<Usuario> usuarios = new ArrayList<>();
        usuarios.add(clienteNormal);
        
        // Toma 2 cupos regulares
        InscripcionTorneo inscripcion = new InscripcionTorneo("I-001", new Date(), usuarios, 2, 0, 2, false, 0, true, false);
        
        assertDoesNotThrow(() -> torneo.inscribir(inscripcion), "La inscripción debe ser exitosa y no arrojar excepciones.");
        
        // Los cupos regulares del torneo bajan de 8 a 6
        assertEquals(2, torneo.getCupoOcupadoRegular(), "Se deben registrar 2 cupos ocupados regulares.");
        assertEquals(6, torneo.cuposDisponiblesRegulares(), "Deben quedar 6 cupos regulares disponibles.");
    }
}
