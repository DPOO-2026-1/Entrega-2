package World;

import Proyecto1Entrega2.src.World.Usuario.Cliente;

public class Mesa {
	private int idMesa;
	private int capacidadMax;
	private boolean ocupada;
	private int cantPersonasActuales;
	private boolean hayNinos;
	private boolean hayJovenes;
	private Cliente ocupadaPor;
	private boolean hayBebidaCaliente;
	
	public Mesa(int idMesa, int capacidadMax) {
        this.idMesa = idMesa;
        this.capacidadMax = capacidadMax;
        this.ocupada = false;
        this.cantPersonasActuales = 0;
        this.hayNinos = false;
        this.hayJovenes = false;
        this.hayBebidaCaliente = false;
        this.ocupadaPor = null;
    }
	
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
        this.hayBebidaCaliente = false;
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
	
	public void agregarPrestamo(Prestamo p) {
        this.prestamosActivos.add(p);
    }
    
    // Getters y setters de la nueva relación
    public Cliente getOcupadaPor() { 
    	return ocupadaPor; 
    }
    public void setOcupadaPor(Cliente cliente) { 
    	this.ocupadaPor = cliente; 
    }

    public int getIdMesa() { 
    	return idMesa; 
    }
    public int getCapacidadMax() { 
    	return capacidadMax; 
    }
    public void setHayBebidaCaliente(boolean hayBebidaCaliente) { 
    	this.hayBebidaCaliente = hayBebidaCaliente; 
    }
}
