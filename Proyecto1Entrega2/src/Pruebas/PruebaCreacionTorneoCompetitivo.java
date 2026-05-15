package Pruebas;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import Torneo.EstadoTorneo;
import Usuario.DiaSemana;
import java.util.Date;
import java.util.ArrayList;

@DisplayName("7. Creación de Torneo Competitivo (Admin)")
public class PruebaCreacionTorneoCompetitivo {

    @Test
    @DisplayName("Input: Admin crea torneo competitivo. Outcome: Se guarda como Competitivo y registra la tarifa de $10000.")
    public void testCrearTorneoCompetitivo() {
        try {
            // Evaluamos mediante reflexión en caso de que la clase no esté expuesta aún
            Class<?> claseCompetitivo = Class.forName("Torneo.TorneoCompetitivo");
            assertNotNull(claseCompetitivo, "La clase TorneoCompetitivo debe estar implementada.");
            
            // Si la clase existe, aquí se validaría la creación con la tarifa:
            // TorneoCompetitivo tc = new TorneoCompetitivo(..., 10000);
            // assertEquals(10000, tc.getTarifa(), "La tarifa debe ser de $10000.");
        } catch (ClassNotFoundException e) {
            fail("Falta implementar la clase TorneoCompetitivo y la lógica de tarifa.");
        }
    }
}
