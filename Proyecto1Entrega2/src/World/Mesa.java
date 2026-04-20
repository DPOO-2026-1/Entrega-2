package World;

import Usuario.Cliente;

public class Mesa {
	private int idMesa;
	private int capacidadMax;
	private boolean ocupada;
	private int cantPersonasActuales;
	private boolean hayNinos;
	private boolean hayJovenes;
	private Cliente ocupadaPor;
	private boolean hayBebidaCaliente;
	
	public void ocupar(int personas, boolean hayNinos, boolean hayJovenes, Cliente cliente) {
		if (!ocupada && personas <= capacidadMax) {
			this.ocupada = true;
			this.cantPersonasActuales = personas;
			this.hayNinos = hayNinos;
			this.hayJovenes = hayJovenes;
			this.ocupadaPor = cliente;
		}
		else {
			throw new IllegalStateException("La mesa no está disponible o excede la capacidad");
		}
	}
	
	public void liberar() {
		this.ocupada = false;
		this.cantPersonasActuales = 0;
		this.hayNinos = false;
        this.hayJovenes = false;
        this.ocupadaPor = null;
	}
	
	public boolean estaDisponible() {
		return this.ocupada;
	}
	
	public boolean puedeRecibirBebidaCaliente(Juego juego) {
		if (juego.getCategoria() == "Accion"){
				return false;
		}
		return true;
	}
	
	public boolean puedeRecibirBebidaAlcoholica() {
		return !hayJovenes && !hayNinos;
	}
	
	public boolean puedeRecibirJuegoAccion() {
		return !hayBebidaCaliente;
	}
}
