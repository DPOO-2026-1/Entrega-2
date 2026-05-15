package Pruebas;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import Usuario.GestorUsuarios;
import Usuario.Cliente;
import Usuario.Usuario;
import World.Cafeteria;

@DisplayName("1. Registro autónomo de cliente")
public class PruebaRegistroCliente {

    private GestorUsuarios gestorUsuarios;
    private Cafeteria cafeteria;

    @BeforeEach
    public void setUp() {
        Cafeteria.resetInstance();
        cafeteria = Cafeteria.getInstance(20, "Cafe Test", null, null);
        gestorUsuarios = new GestorUsuarios(null, cafeteria);
        // Aseguramos que la lista general referencie a la misma
        gestorUsuarios.setUsuarios(cafeteria.getUsuarios());
    }

    @Test
    @DisplayName("Input: Datos de cliente nuevo. Outcome: Se crea, guarda y autentica exitosamente")
    public void testRegistroYAutenticacionCliente() {
        // El sistema crea el cliente
        Cliente nuevoCliente = gestorUsuarios.registrarCliente("juan123", "1234", "Juan", false, false);
        
        assertNotNull(nuevoCliente, "El cliente no debe ser nulo.");
        assertEquals("juan123", nuevoCliente.getLogin());
        
        // Lo guarda en la lista de usuarios
        assertTrue(gestorUsuarios.getUsuarios().contains(nuevoCliente), "El cliente debe guardarse en la lista de usuarios.");
        
        // Permite que inicie sesión exitosamente
        Usuario autenticado = gestorUsuarios.autenticar("juan123", "1234");
        assertNotNull(autenticado, "El usuario 'juan123' debe poder iniciar sesión exitosamente.");
        assertEquals("juan123", autenticado.getLogin(), "El login del usuario autenticado debe coincidir.");
    }
}
