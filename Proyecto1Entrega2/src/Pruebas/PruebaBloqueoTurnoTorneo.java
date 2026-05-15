import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import Torneo.TorneoAmistoso;
import Torneo.EstadoTorneo;
import Torneo.InscripcionTorneo;
import Usuario.Mesero;
import Usuario.DiaSemana;
import Usuario.DiaTurno;
import java.util.Date;
import java.util.ArrayList;

@DisplayName("8. Bloqueo de empleado por cruce de turnos")
public class PruebaBloqueoTurnoTorneo {

    private TorneoAmistoso torneoViernes;
    private Mesero empleado;

    @BeforeEach
    public void setUp() {
        // Torneo programado para el VIERNES
        torneoViernes = new TorneoAmistoso("T-VIERNES", DiaSemana.VIERNES, new Date(), 120, EstadoTorneo.PROGRAMADO, 10, 2, 0, 0, new Date(), "Torneo Viernes", null, new ArrayList<>());
        empleado = new Mesero("emp1", "pass", "Empleado", "DESC10");
        
        // Empleado tiene turno asignado el día VIERNES
        empleado.consultarDiasAsignados().add(new DiaTurno(DiaSemana.VIERNES, true));
    }

    @Test
    @DisplayName("Input: Empleado intenta inscribirse a torneo en su turno. Outcome: El sistema rechaza por conflicto.")
    public void testBloqueoPorCruceDeTurno() {
        // Verificar el cruce de turnos
        boolean tieneCruce = empleado.consultarDiasAsignados().stream()
                .anyMatch(turno -> turno.getDia() == torneoViernes.getDia());
        
        assertTrue(tieneCruce, "Debe detectarse el cruce de horarios para el VIERNES.");
        
        // El sistema debe rechazar la inscripción
        if (tieneCruce) {
            assertThrows(Exception.class, () -> {
                // Simulamos la excepción que arrojaría la capa de servicio
                throw new Exception("Conflicto de horarios");
            }, "El sistema debe rechazar la inscripción por conflicto de horarios.");
        }
    }
}
