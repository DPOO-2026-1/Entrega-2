package Pruebas;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import Torneo.TorneoAmistoso;
import Torneo.EstadoTorneo;
import Usuario.DiaSemana;
import java.util.Date;
import java.util.ArrayList;

@DisplayName("2. Límite de 3 cupos por inscripción (Regla estricta)")
public class PruebaLimiteCuposTorneo {

    private TorneoAmistoso torneo;

    @BeforeEach
    public void setUp() {
        torneo = new TorneoAmistoso("T-001", DiaSemana.LUNES, new Date(), 120, null, 10, 2, 0, 0, new Date(), "Torneo Test", null, new ArrayList<>());
    }

    @Test
    @DisplayName("Input: Un cliente intenta tomar 4 cupos. Outcome: El sistema bloquea la acción.")
    public void testBloqueoCuatroCupos() {
        // El sistema bloquea la acción, retorna un error/falso
        boolean esValido = torneo.validarCupoMaximoPorUsuario(4);
        assertFalse(esValido, "El sistema debe bloquear la inscripción de 4 cupos retornando falso.");
        
        // Los cupos del torneo no se modifican
        assertEquals(0, torneo.getCupoOcupadoRegular(), "Los cupos ocupados deben seguir en 0.");
        assertEquals(0, torneo.getCupoOcupadoReservado(), "Los cupos reservados ocupados deben seguir en 0.");
    }
}
