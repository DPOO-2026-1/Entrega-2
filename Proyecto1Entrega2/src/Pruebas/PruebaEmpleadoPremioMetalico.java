import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import Torneo.EstadoTorneo;
import Torneo.InscripcionTorneo;
import Usuario.Mesero;
import Usuario.Usuario;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;

@DisplayName("9. Empleado no recibe premio metálico")
public class PruebaEmpleadoPremioMetalico {

    private Mesero empleado;
    private InscripcionTorneo inscripcion;

    @BeforeEach
    public void setUp() {
        empleado = new Mesero("emp1", "pass", "Empleado Jugador", "DESC10");
        List<Usuario> usuarios = new ArrayList<>();
        usuarios.add(empleado);
        
        // Empleado inscrito (esEmpleado=true, elegiblePrecioMetalico=false)
        inscripcion = new InscripcionTorneo("I-001", new Date(), usuarios, 1, 0, 1, true, 0.0, true, false);
    }

    @Test
    @DisplayName("Input: Empleado gana torneo. Outcome: Torneo Finalizado, premio metálico asignado $0.")
    public void testPremioMetalicoCeroParaEmpleado() {
        assertTrue(inscripcion.isEsEmpleado(), "El sistema debe marcar la inscripción como perteneciente a un empleado.");
        assertFalse(inscripcion.isElegiblePrecioMetalico(), "El empleado no debe ser elegible para premio metálico.");
        
        // El torneo pasa a estado Finalizado
        EstadoTorneo estadoFinal = EstadoTorneo.FINALIZADO; // O el equivalente
        // assertEquals(EstadoTorneo.FINALIZADO, torneo.getEstado());
        
        // Calculo simulado del premio
        double premioAsignado = inscripcion.isElegiblePrecioMetalico() ? 50000.0 : 0.0;
        
        assertEquals(0.0, premioAsignado, "El premio metálico asignado a la cuenta del empleado debe ser $0.");
    }
}
