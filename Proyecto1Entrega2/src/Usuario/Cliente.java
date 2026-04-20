package Usuario;

import World.Mesa;
import World.Prestamo;
import World.Cafeteria;
import World.CopiaPrestamo;
import ModuloVenta.ItemVenta;
import ModuloVenta.Venta;

public class Cliente extends Usuario{
	private boolean esNinio;
	private boolean esJoven;
	private int juegosReservados;
	private int puntosFidelidad;
	
	public Cliente(String login, String password, String nombre, boolean esNinio, boolean esJoven) {
        super(login, password, nombre);
        this.esNinio = esNinio;
        this.esJoven = esJoven;
        this.juegosReservados = 0;
        this.puntosFidelidad = 0;
    }

	public Mesa reservarMesa(int personas, boolean hayNinos, boolean hayJovenes) {
		Cafeteria cafe = Cafeteria.getInstance();
    	Mesa mesaDisponible = cafe.buscarMesaDisponible(personas, hayNinos, hayJovenes);

    	if (mesaDisponible != null) {
        	mesaDisponible.ocupar(personas, hayNinos, hayJovenes, this);
        	return mesaDisponible;
    	} else {
        	throw new IllegalStateException("No hay mesas disponibles para esas condiciones.");
    	}
	}
	
	public Prestamo solicitarPrestamo(CopiaPrestamo copia, Mesa mesa) {
		//TODO
	}
	
	public void devolverJuego(Prestamo p) {
		//TODO
	}
	
	public Venta realizarCompra(ItemVenta[] items, String codigoDesc) {
		//TODO
	}
	
	public double usarPuntosFidelidad(int puntos) {
		
	}
}
