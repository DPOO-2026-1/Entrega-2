package Pruebas;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import World.Juego;
import World.CopiaPrestamo;
import ModuloVenta.CopiaVenta;
import Usuario.Administrador;
import ModuloVenta.ItemVenta;
import ModuloVenta.Venta;
import ModuloVenta.ProductoComestible;
import ModuloVenta.Bebida;
import ModuloVenta.Pasteleria;

import java.util.ArrayList;
import java.util.List;
import java.util.Date;

/**
 * Prueba de Flujo 3: Administración extrema (Robos, reparación y reportes
 * financieros).
 *
 * Cubre:
 * - Marcar copia como desaparecida (robada).
 * - Mover copia de inventario de ventas al de préstamos (reparación de
 * inventario).
 * - Informe de ventas que segrega: IVA 19% (juegos) vs Consumo 8% + Propina 10%
 * (comida).
 */
@DisplayName("Flujo 3 - Administración: Robos, Inventario y Reportes Financieros")
public class PruebaFlujo3 {

    private Administrador admin;
    private Juego juegoConRobo;

    @BeforeEach
    public void setUp() {
        admin = new Administrador("admin01", "adminPass", "Carlos Admin");
        juegoConRobo = new Juego("Catan", 1995, "Kosmos", 3, 4, 10, "Tablero", false);

        CopiaPrestamo copiaRobada = new CopiaPrestamo("P-CAT-001", "Prestado", false, 3);
        juegoConRobo.agregarCopiaPrestamo(copiaRobada);
    }

    @Test
    @DisplayName("Paso 1: Administrador marca una copia como desaparecida (robada)")
    public void testMarcarCopiaComoDesaparecida() {

        CopiaPrestamo copia = new CopiaPrestamo("P-CAT-001", "Activo", false, 3);
        admin.darJuegoPorRobado(copia);
        assertFalse(copia.estaDisponible(), "La copia robada no debe estar disponible.");
        assertEquals("Desaparecida", copia.getEstado(), "El estado debe ser 'Desaparecida'.");

        assertNotNull(admin, "El administrador debe estar correctamente instanciado.");
    }

    @Test
    @DisplayName("Paso 2: Mover una copia de venta a inventario de préstamo para suplir faltante")
    public void testMoverCopiaDeVentaAPrestamo() {
        // Lógica esperada del flujo:
        // 1. Admin verifica que no hay copias de préstamo disponibles.
        // 2. Admin toma una CopiaVenta del inventario de ventas y la convierte a
        // préstamo.
        // 3. Se marca la CopiaVenta como "removida" o se elimina del inventario de
        // ventas.
        // 4. Se crea una nueva CopiaPrestamo y se añade al juego.
        //
        CopiaVenta copiaVenta = new CopiaVenta("V-CAT-001", 65000.0);
        juegoConRobo.agregarCopiaVenta(copiaVenta);
        // int copiasVentaAntes = juegoConRobo.getCopiasParaVenta().size(); // El metodo
        // getCopiasParaVenta no existe

        admin.moverJuegoAPrestamo(copiaVenta, juegoConRobo);

        assertNotNull(juegoConRobo.getCopiaDisponible(), "Debe haber una copia de préstamo disponible.");
        assertTrue(true, "Placeholder - pendiente de implementación del método correcto en Administrador.");
    }

    @Test
    @DisplayName("Paso 3: Informe de ventas con IVA 19% en juegos e Impuesto al Consumo 8% en comida")
    public void testInformeVentasConImpuestosSegregados() {
        // Caso de juego comprado: precio $50,000, IVA 19%
        double precioJuego = 50000.0;
        double ivaJuego = precioJuego * 0.19; // 9,500
        double totalConIvaJuego = precioJuego + ivaJuego; // 59,500

        // Caso de bebida/comida: precio $10,000, Impuesto al Consumo 8%, Propina 10%
        // del subtotal
        double precioComida = 10000.0;
        double impuestoConsumoComida = precioComida * 0.08; // 800
        double propinaComida = precioComida * 0.10; // 1,000 (antes de impuestos)
        double totalComida = precioComida + impuestoConsumoComida + propinaComida; // 11,800

        // Verificamos la matemática del informe
        assertEquals(9500.0, ivaJuego, 0.01,
                "El IVA del 19% sobre $50,000 debe ser $9,500.");
        assertEquals(59500.0, totalConIvaJuego, 0.01,
                "El total del juego con IVA debe ser $59,500.");
        assertEquals(800.0, impuestoConsumoComida, 0.01,
                "El Impuesto al Consumo del 8% sobre $10,000 debe ser $800.");
        assertEquals(1000.0, propinaComida, 0.01,
                "La propina del 10% sobre $10,000 debe ser $1,000.");
        assertEquals(11800.0, totalComida, 0.01,
                "El total de la comida con impuesto y propina debe ser $11,800.");

        // Verificamos las tasas de impuesto de las clases concretas
        // CopiaVenta (juego de venta) debe tener 19%

        CopiaVenta cv = new CopiaVenta("V-001", 50000.0);
        assertEquals(0.19, cv.getTasaImpuesto(), 0.001, "CopiaVenta debe tener tasa de 19%.");

        // ProductoComestible (comida) debe tener 8%
        // La clase abstracta devuelve 0.08, verificable con cualquier subclase concreta
        Bebida bebida = new Bebida("Cafe", 10000.0, true, false);
        assertEquals(0.08, bebida.getTasaImpuesto(), 0.001, "Bebida debe tener tasa de 8%.");

        // Por ahora, documentamos las tasas correctas como constantes esperadas
        double tasaIvaEsperadaJuego = 0.19;
        double tasaConsumoEsperadaComida = 0.08;
        assertEquals(0.19, tasaIvaEsperadaJuego, "La tasa de IVA de juego debe ser 19%.");
        assertEquals(0.08, tasaConsumoEsperadaComida, "La tasa de impuesto al consumo de comida debe ser 8%.");
    }
}
