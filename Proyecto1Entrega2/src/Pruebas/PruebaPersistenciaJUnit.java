package Pruebas;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import Persistencia.GestorPersistencia;
import Usuario.*;
import World.*;
import ModuloVenta.*;
import Torneo.*;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.time.LocalDateTime;

@DisplayName("Pruebas Unitarias para GestorPersistencia")
public class PruebaPersistenciaJUnit {

    private static final String TEST_DIR = "data_test/";
    private GestorPersistencia gestorPersistencia;

    @BeforeEach
    public void setUp() {
        limpiarDirectorioPrueba();
        new File(TEST_DIR).mkdirs();
        gestorPersistencia = new GestorPersistencia(TEST_DIR);
    }

    @AfterEach
    public void tearDown() {
        limpiarDirectorioPrueba();
    }

    private void limpiarDirectorioPrueba() {
        File dir = new File(TEST_DIR);
        if (dir.exists()) {
            File[] archivos = dir.listFiles();
            if (archivos != null) {
                for (File f : archivos) {
                    f.delete();
                }
            }
            dir.delete();
        }
    }

    @Test
    @DisplayName("Persistencia de Usuarios: Guardar y Cargar Cliente, Administrador, Cocinero, Mesero")
    public void testPersistenciaUsuarios() {
        List<Usuario> listaOriginal = new ArrayList<>();
        listaOriginal.add(new Cliente("cli_test", "1234", "Juan Cliente", false, false));
        listaOriginal.add(new Administrador("admin_test", "admin1", "Sonia Admin"));
        listaOriginal.add(new Cocinero("coci_test", "cook", "Pedro Cocinero", "DESC10"));
        listaOriginal.add(new Mesero("mes_test", "waiter", "Maria Mesera", "DESC5"));

        gestorPersistencia.guardarUsuarios(listaOriginal);

        File archivo = new File(TEST_DIR + "usuarios.csv");
        assertTrue(archivo.exists(), "El archivo usuarios.csv debe haberse creado.");

        List<Usuario> listaCargada = gestorPersistencia.cargarUsuarios();
        assertEquals(4, listaCargada.size(), "Se deben cargar exactamente 4 usuarios.");

        Cliente cli = (Cliente) buscarPorLogin(listaCargada, "cli_test");
        assertNotNull(cli);
        assertEquals("Juan Cliente", cli.getNombre());
        assertFalse(cli.isEsNinio());

        Administrador adm = (Administrador) buscarPorLogin(listaCargada, "admin_test");
        assertNotNull(adm);
        assertEquals("Sonia Admin", adm.getNombre());

        Cocinero coc = (Cocinero) buscarPorLogin(listaCargada, "coci_test");
        assertNotNull(coc);
        assertEquals("DESC10", coc.getCodigoDescuento());

        Mesero mes = (Mesero) buscarPorLogin(listaCargada, "mes_test");
        assertNotNull(mes);
        assertEquals("DESC5", mes.getCodigoDescuento());
    }

    @Test
    @DisplayName("Persistencia de Juegos: Guardar y Cargar Catálogo de Juegos")
    public void testPersistenciaJuegos() {
        List<Juego> listaOriginal = new ArrayList<>();
        listaOriginal.add(new Juego("Chess", 1500, "Classic", 2, 2, 6, "Tablero", false));
        listaOriginal.add(new Juego("Call of Duty", 2003, "Activision", 1, 12, 18, "Accion", true));

        gestorPersistencia.guardarJuegos(listaOriginal);

        File archivo = new File(TEST_DIR + "juegos.csv");
        assertTrue(archivo.exists(), "El archivo juegos.csv debe haberse creado.");

        List<Juego> listaCargada = gestorPersistencia.cargarJuegos();
        assertEquals(2, listaCargada.size());

        Juego chess = buscarJuegoPorNombre(listaCargada, "Chess");
        assertNotNull(chess);
        assertEquals(1500, chess.getAnioPublicacion());
        assertEquals("Tablero", chess.getCategoria());
        assertFalse(chess.isEsDificil());

        Juego cod = buscarJuegoPorNombre(listaCargada, "Call of Duty");
        assertNotNull(cod);
        assertEquals(18, cod.getEdadMinima());
        assertEquals("Accion", cod.getCategoria());
        assertTrue(cod.isEsDificil());
    }

    @Test
    @DisplayName("Persistencia de Préstamos: Guardar y Cargar historial de Préstamos")
    public void testPersistenciaPrestamos() {
        List<Usuario> usuarios = new ArrayList<>();
        Cliente cli = new Cliente("cli_p", "1234", "Juan Cliente", false, false);
        usuarios.add(cli);

        List<Juego> juegos = new ArrayList<>();
        Juego catan = new Juego("Catan", 1995, "Kosmos", 3, 4, 10, "Tablero", false);
        CopiaPrestamo cp1 = new CopiaPrestamo("COP-01", "Buen estado", true, 0);
        catan.agregarCopiaPrestamo(cp1);
        juegos.add(catan);

        Mesa mesa = new Mesa(3, 4);

        List<CopiaPrestamo> copiasPrestadas = new ArrayList<>();
        copiasPrestadas.add(cp1);

        Prestamo prestamo = new Prestamo(cli, mesa, copiasPrestadas);
        List<Prestamo> listaOriginal = new ArrayList<>();
        listaOriginal.add(prestamo);

        gestorPersistencia.guardarPrestamos(listaOriginal);

        File archivo = new File(TEST_DIR + "prestamos.csv");
        assertTrue(archivo.exists(), "El archivo prestamos.csv debe haberse creado.");

        List<Prestamo> listaCargada = gestorPersistencia.cargarPrestamos(usuarios, juegos);
        assertEquals(1, listaCargada.size());

        Prestamo cargado = listaCargada.get(0);
        assertEquals("cli_p", cargado.getSolicitadoPor().getLogin());
        assertNotNull(cargado.getMesaAsociada());
        assertEquals(3, cargado.getMesaAsociada().getIdMesa());
        assertEquals(1, cargado.getCopias().size());
        assertEquals("COP-01", cargado.getCopias().get(0).getIdUnico());
    }

    @Test
    @DisplayName("Persistencia de Ventas: Guardar y Cargar registros de Ventas")
    public void testPersistenciaVentas() {
        List<Usuario> usuarios = new ArrayList<>();
        Cliente cli = new Cliente("cli_v", "1234", "Juan Cliente", false, false);
        usuarios.add(cli);

        Venta venta = new Venta();
        venta.setIdVenta(101);
        venta.setFecha(LocalDateTime.of(2026, 6, 1, 10, 30, 0));
        venta.setRealizadaPor(cli);
        venta.setTotal(45500.0);

        List<Venta> listaOriginal = new ArrayList<>();
        listaOriginal.add(venta);

        gestorPersistencia.guardarVentas(listaOriginal);

        File archivo = new File(TEST_DIR + "ventas.csv");
        assertTrue(archivo.exists(), "El archivo ventas.csv debe haberse creado.");

        List<Venta> listaCargada = gestorPersistencia.cargarVentas(usuarios);
        assertEquals(1, listaCargada.size());

        Venta cargada = listaCargada.get(0);
        assertEquals(101, cargada.getIdVenta());
        assertEquals(45500.0, cargada.getTotal());
        assertEquals("cli_v", cargada.getRealizadaPor().getLogin());
        assertEquals(LocalDateTime.of(2026, 6, 1, 10, 30, 0), cargada.getFecha());
    }

    @Test
    @DisplayName("Persistencia de Torneos: Guardar y Cargar Gestor de Torneos")
    public void testPersistenciaTorneoComplete() {
        List<Usuario> usuarios = new ArrayList<>();
        Administrador admin = new Administrador("admin_t", "admin", "Admin");
        Cliente fan = new Cliente("fan1", "pass", "Fan", false, false);
        usuarios.add(admin);
        usuarios.add(fan);

        List<Juego> juegos = new ArrayList<>();
        Juego catan = new Juego("Catan", 1995, "Kosmos", 3, 4, 10, "Tablero", false);
        catan.agregarCopiaPrestamo(new CopiaPrestamo("CP-001", "Disponible", true, 0));
        catan.agregarCopiaPrestamo(new CopiaPrestamo("CP-002", "Disponible", true, 0));
        catan.agregarCopiaPrestamo(new CopiaPrestamo("CP-003", "Disponible", true, 0));
        catan.agregarCopiaPrestamo(new CopiaPrestamo("CP-004", "Disponible", true, 0));
        catan.agregarCopiaPrestamo(new CopiaPrestamo("CP-005", "Disponible", true, 0));
        juegos.add(catan);

        GestorTorneo gestorOriginal = new GestorTorneo();
        
        TorneoAmistoso torneo = gestorOriginal.crearTorneoAmistoso(admin, catan, DiaSemana.LUNES, "10:00", 5, 2500.0);
        
        fan.agregarFavorito(catan);
        gestorOriginal.inscribir(fan, torneo.getIdTorneo(), 1);

        gestorPersistencia.guardarGestorTorneo(gestorOriginal);

        assertTrue(new File(TEST_DIR + "torneos.csv").exists());
        assertTrue(new File(TEST_DIR + "inscripciones_torneos.csv").exists());

        GestorTorneo gestorCargado = gestorPersistencia.cargarGestorTorneo(usuarios, juegos);
        assertEquals(1, gestorCargado.getCatalogoTorneos().size());

        Torneo torneoCargado = gestorCargado.getCatalogoTorneos().get(0);
        assertEquals(torneo.getIdTorneo(), torneoCargado.getIdTorneo());
        assertEquals("Catan", torneoCargado.getJuegoTorneo().getNombre());
        assertTrue(torneoCargado instanceof TorneoAmistoso);
        assertEquals(2500.0, ((TorneoAmistoso) torneoCargado).getValorBono());

        assertEquals(1, torneoCargado.getInscripciones().size());
        InscripcionTorneo insCargada = torneoCargado.getInscripciones().get(0);
        assertEquals(1, insCargada.getCantidadCupos());
        assertEquals("fan1", insCargada.getUsuarios().get(0).getLogin());
    }

    private Usuario buscarPorLogin(List<Usuario> usuarios, String login) {
        for (Usuario u : usuarios) {
            if (u.getLogin().equals(login)) {
                return u;
            }
        }
        return null;
    }

    private Juego buscarJuegoPorNombre(List<Juego> juegos, String nombre) {
        for (Juego j : juegos) {
            if (j.getNombre().equalsIgnoreCase(nombre)) {
                return j;
            }
        }
        return null;
    }
}
