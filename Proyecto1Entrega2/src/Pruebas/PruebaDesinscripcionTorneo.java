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
import java.util.Date;
import java.util.ArrayList;
import java.util.List;

@DisplayName("5. Desinscripción total")
public class PruebaDesinscripcionTorneo {

	private GestorTorneo gestorTorneo;
	private TorneoAmistoso torneo;
	private Cliente cliente;
	private InscripcionTorneo inscripcion;

	@BeforeEach
	public void setUp() throws Exception {
	    gestorTorneo = new GestorTorneo();

	    torneo = new TorneoAmistoso(
	        "T-001",
	        DiaSemana.LUNES,
	        new Date(),
	        120,
	        null,
	        10,   // cupo total
	        2,    // cupo reservado fanáticos
	        0,    // ocupados reservados
	        0,    // ocupados regulares
	        new Date(),
	        "Torneo Test",
	        null,
	        new ArrayList<>(),
	        0.0   // valor bono
	    );

	    // Registrar torneo en el gestor
	    gestorTorneo.getCatalogoTorneos().add(torneo);

	    cliente = new Cliente("cli1", "pass", "Cliente", false, false);

	    // Inscribir cliente con 3 cupos regulares
	    gestorTorneo.inscribir(cliente, torneo.getIdTorneo(), 3);

	    // Guardar referencia a la inscripción creada
	    inscripcion = torneo.getInscripciones().get(0);
	}

	@Test
	@DisplayName("Input: Cliente solicita desinscribirse. Outcome: Inscripción eliminada, 3 cupos regresan al total.")
	public void testDesinscripcionTotal() {
	    assertEquals(3, torneo.getCupoOcupadoRegular(),
	        "Deben haber 3 cupos ocupados antes de la desinscripción.");

	    gestorTorneo.desinscribir(cliente, torneo.getIdTorneo());

	    // Los 3 cupos regresan al total de cupos disponibles
	    assertEquals(0, torneo.getCupoOcupadoRegular(),
	        "Los cupos ocupados deben regresar a 0.");
	    assertEquals(8, torneo.cuposDisponiblesRegulares(),
	        "Los cupos regulares disponibles vuelven a ser 8.");

	    // La inscripción se elimina por completo
	    assertFalse(torneo.getInscripciones().contains(inscripcion),
	        "La inscripción debe ser eliminada de la lista del torneo.");
	}
}
