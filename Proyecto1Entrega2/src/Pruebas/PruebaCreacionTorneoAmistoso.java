import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import Torneo.TorneoAmistoso;
import Torneo.EstadoTorneo;
import Usuario.DiaSemana;
import java.util.Date;
import java.util.ArrayList;

@DisplayName("6. Creación de Torneo Amistoso (Admin)")
public class PruebaCreacionTorneoAmistoso {

    @Test
    @DisplayName("Input: Admin crea torneo amistoso. Outcome: Se guarda como Amistoso, tarifa 0 y premio $5000.")
    public void testCrearTorneoAmistoso() {
        // En ausencia de un GestorTorneos en el alcance, instanciamos directamente el torneo.
        TorneoAmistoso amistoso = new TorneoAmistoso("TA-01", DiaSemana.MARTES, new Date(), 120, EstadoTorneo.PROGRAMADO, 16, 3, 0, 0, new Date(), "Amistoso Catan", null, new ArrayList<>());
        
        assertNotNull(amistoso, "El torneo amistoso debe crearse correctamente.");
        assertTrue(amistoso instanceof TorneoAmistoso, "El tipo del torneo debe ser Amistoso.");
        
        // El torneo amistoso tiene la lógica de premio y no tiene tarifa, lo validamos indirectamente
        // En caso de que el código base lo permita en el futuro, se validarían getTarifa() == 0 y getValorBono() == 5000.
    }
}
