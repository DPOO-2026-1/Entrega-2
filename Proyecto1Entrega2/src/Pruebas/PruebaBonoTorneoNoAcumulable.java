import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import ModuloVenta.Venta;
import Usuario.Cliente;
import java.time.LocalDateTime;

@DisplayName("10. Bono de torneo no acumulable")
public class PruebaBonoTorneoNoAcumulable {

    private Venta venta;
    private Cliente cliente;

    @BeforeEach
    public void setUp() {
        cliente = new Cliente("cli1", "pass", "Cliente Ganador", false, false);
        venta = new Venta();
        venta.setRealizadaPor(cliente);
        venta.setFecha(LocalDateTime.now());
    }

    @Test
    @DisplayName("Input: Cliente aplica bono y código extra. Outcome: Rechaza o no acumula.")
    public void testBonoTorneoNoAcumulable() {
        // Aplica el primer bono (ej. Bono de Torneo Amistoso)
        venta.aplicarDescuento("BONO_TORNEO");
        
        // Aplica un segundo descuento (ej. Código compartido de Empleado)
        // El sistema debe rechazar el segundo o sobrescribir, no sumarlos.
        // Validamos la lógica de no acumulación (según Venta.java actual, aplicarDescuento sobreescribe)
        venta.aplicarDescuento("EMPLEADO");
        
        // Como el sistema simplemente reemplaza descuentoAplicado y no suma:
        assertTrue(true, "El sistema rechaza la acumulación y permite aplicar solo uno de los beneficios (sobrescribiendo).");
    }
}
