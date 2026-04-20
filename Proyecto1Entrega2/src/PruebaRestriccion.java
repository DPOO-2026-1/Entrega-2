import static org.junit.Assert.*;
import org.junit.Test;

import Proyecto1Entrega2.src.World.Mesa;
import Proyecto1Entrega2.src.World.Juego;
import Proyecto1Entrega2.src.Usuario.Cliente;

public class PruebaRestriccion {

    @Test
    public void testBebidaCalienteConJuegoAccion() {
        Juego juegoAccion = new Juego("Ritmo y Bola", 2020, "EmpresaX", 2, 4, 12, "Accion", false);
        Cliente cliente = new Cliente("login123", "pass", "Carlos", false, true);
        Mesa mesa = new Mesa(5, 4);
        mesa.ocupar(2, false, true, cliente);
        boolean permitido = mesa.puedeRecibirBebidaCaliente(juegoAccion);
        assertFalse("Protocolo de seguridad: No se permiten bebidas calientes cerca de juegos de acción para evitar daños al material", permitido);
    }
}

