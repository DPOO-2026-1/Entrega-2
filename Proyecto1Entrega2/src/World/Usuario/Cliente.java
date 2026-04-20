package Proyecto1Entrega2.src.World.Usuario;

public class Cliente extends Usuario{
	private boolean esNinio;
	private boolean esJoven;
	private int juegosReservados;
	private int puntosFidelidad;
	
	public Mesa reservarMesa(int personas, boolean hayNinos, boolean hayJovenes) {
		//TODO
	}
	
	public Prestamo solicitarPrestamo(CopiaPresta copia, Mesa mesa) {
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
