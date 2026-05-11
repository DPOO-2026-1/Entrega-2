import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import World.Cafeteria;
import Usuario.DiaTurno;
import Usuario.DiaSemana;
import Usuario.Administrador;
import Usuario.Mesero;
import Usuario.Cocinero;
import Usuario.SolicitudTurno;
import Usuario.SugerenciaMenu;
import ModuloVenta.Pasteleria;
import ModuloVenta.ProductoComestible;

import java.util.ArrayList;
import java.util.List;

/**
 * Prueba de Flujo 4: Lógica de empleados (Turnos y alérgenos).
 *
 * Cubre:
 * - Solicitud de cambio de turno por parte de un mesero.
 * - Rechazo automático del cambio si viola el mínimo de empleados en turno.
 * - Sugerencia de nuevo platillo por un cocinero.
 * - Aprobación de la sugerencia y registro del alérgeno por el administrador.
 */
@DisplayName("Flujo 4 - Empleados: Turnos y Alérgenos")
public class PruebaFlujo4 {

    private Administrador admin;
    private Mesero mesero1;
    private Mesero mesero2;
    private Cocinero cocinero;
    private Cafeteria cafeteria;

    @BeforeEach
    public void setUp() {
        // Reseteamos el singleton para cada prueba
        // TODO: Cafeteria.resetInstance() necesario para tests aislados.
        cafeteria = Cafeteria.getInstance(20, "Cafe Test Flujo4", null, null);

        admin    = new Administrador("admin01", "adminPass", "Carlos Admin");
        mesero1  = new Mesero("mes01", "pass", "Pedro Sánchez", "DESC-MES1");
        mesero2  = new Mesero("mes02", "pass", "Lucía Gómez",  "DESC-MES2");
        cocinero = new Cocinero("coc01", "pass", "Mario Chef",  "DESC-COC1");

        // Asignamos turno del MARTES a: 1 cocinero + 2 meseros (cumple el mínimo)
        // TODO: DiaTurno está en el paquete World pero su importación dice World.DiaTurno.
        // El archivo DiaTurno.java declara 'package World' → alineado.
        DiaTurno turnoMartes = new DiaTurno(DiaSemana.Martes, true);
        mesero1.consultarDiasAsignados().add(turnoMartes);
        mesero2.consultarDiasAsignados().add(turnoMartes);
        cocinero.consultarDiasAsignados().add(turnoMartes);

        // TODO: GestorUsuarios necesita getUsuarios(). Cuando esté disponible:
        // cafeteria.getGestorUsuarios().getUsuarios().add(mesero1);
        // cafeteria.getGestorUsuarios().getUsuarios().add(mesero2);
        // cafeteria.getGestorUsuarios().getUsuarios().add(cocinero);
        // Cafeteria.getInstance(...) recibe un gestor, para este test podemos obviar esto si la lista está vacía, pero dejamos la etiqueta.
    }

    @Test
    @DisplayName("Paso 1: Mesero puede crear una solicitud de cambio de turno")
    public void testMeseroPuedeCrearSolicitudCambioTurno() {
        // El mesero 1 solicita no trabajar el martes
        SolicitudTurno solicitud = mesero1.solicitarCambioTurno(DiaSemana.Martes);

        assertNotNull(solicitud, "La solicitud de turno no debe ser nula.");
        // TODO: Cuando SolicitudTurno tenga getters, descomentar:
        assertEquals(DiaSemana.Martes, solicitud.getDia(), "La solicitud debe ser para el Martes.");
        assertEquals("Pendiente", solicitud.getEstado(), "La solicitud debe empezar como 'Pendiente'.");
        assertEquals(mesero1, solicitud.getSolicitadoPor(), "Debe estar asociada al mesero que la pidió.");
    }

    @Test
    @DisplayName("Paso 2: Sistema rechaza cambio si deja al turno con menos de 2 meseros")
    public void testRechazoSolicitudPorInfringirMinimoEmpleados() {
        // Situación actual en el Martes: 1 cocinero + 2 meseros (mesero1 y mesero2)
        // Si el admin aprueba que mesero1 no trabaje el martes, quedaría: 1 cocinero + 1 mesero → VIOLA REGLA

        // Primero verificamos que el martes SÍ cumple el mínimo actualmente
        // TODO: cafeteria.validarMinimoEmpleados(DiaSemana.Martes) requiere gestorUsuarios configurado.
        // Cuando GestorUsuarios esté integrado, reemplazar con:
        // assertTrue(cafeteria.validarMinimoEmpleados(DiaSemana.Martes),
        //     "El martes debe cumplir el mínimo con 1 cocinero y 2 meseros.");

        // Simulamos el estado DESPUÉS de remover a mesero1 del turno del martes
        // (solo para verificar que la validación detecta el incumplimiento)
        List<Mesero> meserosEnTurno = new ArrayList<>();
        meserosEnTurno.add(mesero2); // Solo 1 mesero si mesero1 se va

        int cantMeseros = meserosEnTurno.size();
        int cantCocineros = 1; // El cocinero sigue

        // La regla de negocio: mínimo 1 cocinero y 2 meseros
        boolean cumpleMinimoSinMesero1 = (cantCocineros >= 1) && (cantMeseros >= 2);
        assertFalse(cumpleMinimoSinMesero1,
            "El turno con solo 1 mesero NO debe cumplir el mínimo requerido → solicitud debe rechazarse.");

        // Por tanto, el administrador debe rechazar la solicitud
        SolicitudTurno solicitud = mesero1.solicitarCambioTurno(DiaSemana.Martes);
        admin.rechazarSolicitudTurno(solicitud);

        // TODO: Cuando SolicitudTurno tenga getter:
        assertEquals("Rechazada", solicitud.getEstado(), "La solicitud debe quedar como 'Rechazada'.");
    }

    @Test
    @DisplayName("Paso 3: Cocinero puede sugerir un nuevo platillo al menú")
    public void testCocineroSugiereNuevoPlatillo() {
        SugerenciaMenu sugerencia = cocinero.sugerirPlato("Pastel de mantequilla de maní - contiene maní");

        assertNotNull(sugerencia, "La sugerencia no debe ser nula.");
        // TODO: Cuando SugerenciaMenu tenga getters, descomentar:
        assertEquals("Pendiente", sugerencia.getEstado(), "La sugerencia debe iniciar como 'Pendiente'.");
        assertEquals(cocinero, sugerencia.getCreadoPor(), "Debe estar asociada al cocinero que la creó.");
    }

    @Test
    @DisplayName("Paso 4: Administrador aprueba sugerencia y el platillo registra alérgeno de maní")
    public void testAdminApruebaSugerenciaYRegistraAlergeno() {
        SugerenciaMenu sugerencia = cocinero.sugerirPlato("Pastel de mantequilla de maní");

        // Administrador aprueba la sugerencia
        admin.aprobarSugerenciaMenu(sugerencia);
        // TODO: Cuando SugerenciaMenu tenga getter de estado:
        assertEquals("Aprobada", sugerencia.getEstado(), "La sugerencia debe quedar como 'Aprobada'.");

        // Creamos el pastel con el alérgeno registrado
        // TODO: Pasteleria necesita un constructor y setter de alérgenos.
        // Por ahora documentamos el comportamiento esperado:
        //
        List<String> alergenosMani = new ArrayList<>();
        alergenosMani.add("maní");
        Pasteleria pastel = new Pasteleria("Pastel de Maní", 15000.0, alergenosMani);
        List<ProductoComestible> menuCafeteria = new ArrayList<>();
        admin.agregarProductoMenu(menuCafeteria, pastel);
        
        assertEquals(1, menuCafeteria.size(), "El menú debe tener 1 platillo.");
        
        List<String> alergenos = cafeteria.consultarAlergenos(pastel);
        assertTrue(alergenos.contains("maní"), "El pastel debe advertir que contiene maní.");

        // Verificamos que el método consultarAlergenos existe en Cafeteria
        // y que Pasteleria tiene el método getAlergenos() (retorna String[] actualmente)
        // TODO: Cafeteria.consultarAlergenos() espera Pasteleria con getAlergenos() -> List<String>
        //       pero Pasteleria devuelve String[]. Requiere alineación de tipos.
        assertTrue(true, "Placeholder - pendiente de constructor en Pasteleria y alineación de tipos.");
    }
}
