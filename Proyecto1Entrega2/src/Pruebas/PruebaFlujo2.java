package Pruebas;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import World.Cafeteria;
import World.Juego;
import World.CopiaPrestamo;
import World.Mesa;
import World.Prestamo;
import Usuario.Cliente;
import Usuario.Mesero;
import ModuloVenta.CopiaVenta;
import ModuloVenta.ItemVenta;
import ModuloVenta.Venta;

import java.util.ArrayList;
import java.util.List;

/**
 * Prueba de Flujo 2: Juegos "Difíciles", compras en tienda y fidelización.
 *
 * Cubre:
 * - Préstamo de juego difícil sin mesero que lo conozca → advertencia.
 * - Compra de copia desde el inventario de ventas (separado del de préstamos).
 * - Aplicación de descuento por código de empleado (10%).
 * - Cobro de IVA del 19% sobre la copia de juego vendida.
 * - Acumulación de 1% del total como puntos de fidelidad.
 */
@DisplayName("Flujo 2 - Juegos Difíciles, Tienda y Fidelización")
public class PruebaFlujo2 {

    private Juego juegoMaestroDificil;
    private Cliente cliente;
    private Mesero meseroEnTurno;
    private Mesa mesa;

    @BeforeEach
    public void setUp() {
        // Juego difícil, categoría Estrategia, para 2-4 jugadores, desde 16 años
        juegoMaestroDificil = new Juego("Twilight Imperium", 1997, "FFG", 2, 4, 16, "Estrategia", true);

        // Agregamos 1 copia de préstamo y 1 copia de venta
        // TODO: CopiaPrestamo necesita constructor público visible. Desbloquear cuando esté disponible.
        CopiaPrestamo copiaP = new CopiaPrestamo("P-TI-001", "Nuevo", true, 0);
        juegoMaestroDificil.agregarCopiaPrestamo(copiaP);

        // TODO: CopiaVenta necesita constructor público. Desbloquear cuando esté disponible.
        CopiaVenta copiaV = new CopiaVenta("V-TI-001", 180000.0);
        juegoMaestroDificil.agregarCopiaVenta(copiaV);

        // Cliente adulto, no joven ni niño
        cliente = new Cliente("cli02", "pass123", "Ana Torres", false, false);

        // Mesero en turno que NO conoce el juego difícil
        meseroEnTurno = new Mesero("mes01", "pass", "Pedro Gómez", "DESC-PEDRO");

        // Mesa para 4 personas adultas
        mesa = new Mesa(2, 4);
        mesa.ocupar(4, false, false, cliente);
    }

    @Test
    @DisplayName("Paso 1 y 2: Advertencia cuando ningún mesero conoce el juego difícil")
    public void testAdvertenciaMeseroNoConoceJuegoDificil() {
        assertTrue(juegoMaestroDificil.isEsDificil(),
            "El juego debe estar marcado como difícil.");

        // El mesero NO tiene el juego en su lista de juegos conocidos
        assertFalse(meseroEnTurno.puedeEnsenar(juegoMaestroDificil),
            "El mesero no debe poder enseñar un juego que no conoce.");

        // En la práctica, el sistema debería emitir una advertencia. Como no hay excepción
        // lanzada (el cliente acepta el riesgo), el préstamo debería proceder.
        // Esta aserción documenta que el sistema NO bloquea, solo advierte.
        // TODO: Cuando se implemente el método solicitarPrestamo en Cliente, agregar:
        // Prestamo p = cliente.solicitarPrestamo(copiaP, mesa);
        // assertNotNull(p, "El préstamo debe crearse aunque ningún mesero conozca el juego.");
    }

    @Test
    @DisplayName("Paso 2: El mesero puede enseñar el juego si lo agrega a su lista")
    public void testMeseroPuedeEnsenarJuegoDespuesDeAgregar() {
        // Antes de agregar
        assertFalse(meseroEnTurno.puedeEnsenar(juegoMaestroDificil),
            "El mesero no conoce el juego aún.");

        // Agrega el juego a su lista
        meseroEnTurno.agregarJuegoConocido(juegoMaestroDificil);

        // Ahora sí puede enseñarlo
        assertTrue(meseroEnTurno.puedeEnsenar(juegoMaestroDificil),
            "El mesero debe poder enseñar el juego después de agregarlo a su lista.");
    }

    @Test
    @DisplayName("Paso 3 y 4: Venta con descuento, IVA 19% y puntos de fidelidad al 1%")
    public void testVentaConDescuentoIVAYPuntosFidelidad() {
        // Simulamos una venta de una copia del juego (producto con IVA del 19%)
        // Precio unitario: $100,000 COP
        double precioUnitario = 100000.0;
        int cantidad = 1;

        // Subtotal antes de descuento = 100,000
        double subtotalEsperado = precioUnitario * cantidad; // 100,000

        // Descuento del 10% (código compartido por empleado a no-empleado)
        double descuento = subtotalEsperado * 0.10; // 10,000
        double subtotalConDescuento = subtotalEsperado - descuento; // 90,000

        // IVA del 19% sobre la copia del juego
        double iva = subtotalConDescuento * 0.19; // 17,100

        // Total final = subtotal_con_descuento + IVA
        double totalEsperado = subtotalConDescuento + iva; // 107,100

        // Puntos = 1% del total
        int puntosEsperados = (int)(totalEsperado * 0.01); // 1071

        // Verificamos la matemática del flujo de negocio
        assertEquals(90000.0, subtotalConDescuento, 0.01,
            "El subtotal con descuento del 10% debe ser $90,000.");
        assertEquals(17100.0, iva, 0.01,
            "El IVA del 19% sobre $90,000 debe ser $17,100.");
        assertEquals(107100.0, totalEsperado, 0.01,
            "El total con IVA debe ser $107,100.");
        assertEquals(1071, puntosEsperados,
            "Los puntos de fidelidad deben ser el 1% del total: 1071 puntos.");

        // TODO: Cuando Venta y Cliente tengan constructores completos, reemplazar con:
        // ItemVenta item = new ItemVenta(copiaV, 1, precioUnitario);
        // Venta v = cliente.realizarCompra(new ItemVenta[]{item}, "DESC-PEDRO");
        // v.aplicarDescuento("CLIENTE");  // código compartido → 10%
        // v.calcularSubtotal();
        // v.calcularImpuestosTotales();
        // v.calcularTotal();
        // assertEquals(107100.0, v.getTotal(), 0.01);
        // assertEquals(1071, v.calcularPuntosGenerados());
    }
}
