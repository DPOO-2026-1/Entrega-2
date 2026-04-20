package World;

import Proyecto1Entrega2.src.ModuloVenta.Pasteleria;
import Proyecto1Entrega2.src.ModuloVenta.ProductoComestible;
import Proyecto1Entrega2.src.World.Usuario.DiaSemana;
import Proyecto1Entrega2.src.World.Usuario.Usuario;

public class Cafeteria {
	private int capacidadMax;
	private int cantPersonasActuales;
	private String nombreEstablecimiento;
	private Mesa mesas;
	private Juego juegos;
	private Usuario users;
	private ProductoComestible[] productosComestibles;
	private Prestamo[] historialPrestamos;
	
	public Cafeteria getInstance() {
		//TODO
	}
	
	public Usuario login(String login, String pass) {
		//TODO
	}
	
	public boolean hayCapacidad(int nPersonas) {
		//TODO
	}
	
	public boolean registrarIngreso(int nPersonas) {
		//TODO
	}
	
	public void registrarSalida(int nPersonas) {
		//TODO
	}
	
	public Juego buscarJuego(String nombre) {
		//TODO
	}
	
	public Mesa getMesaDisponible(int personas) {
		//TODO
	}
	
	public validarMinimoEmpleados(DiaSemana dia) {
		//TODO
	}
	
	public String[] consultarAlergenos(Pasteleria p) {
		//TODO
	}

	public void crearUsuario(String login, String pass, String nombre){
		//TODO
	}

	public void crearPrestamo(...)
}
