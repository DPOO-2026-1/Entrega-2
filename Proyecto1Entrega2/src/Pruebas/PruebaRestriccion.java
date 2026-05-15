package Pruebas;

import static org.junit.jupiter.api.Assertions.assertFalse;
import org.junit.jupiter.api.Test;
import World.Juego;
import World.Mesa;
import Usuario.Cliente;import World.Cafeteria;
public class PruebaRestriccion {

    @Test
    public void testBebidaCalienteConJuegoAccion() {
        Juego juegoAccion = new Juego("Ritmo y Bola", 2020, "EmpresaX", 2, 4, 12, "Accion", false);
        Cliente cliente = new Cliente("login123", "pass", "Carlos", false, true);
        Mesa mesa = new Mesa(5, 4);
        mesa.ocupar(2, false, true, cliente);
        boolean permitido = mesa.puedeRecibirBebidaCaliente(juegoAccion);
        assertFalse(permitido, "Protocolo de seguridad: No se permiten bebidas calientes...");
    }
}

