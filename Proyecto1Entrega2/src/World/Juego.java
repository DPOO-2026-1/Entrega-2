package World;

public class Juego {
	private String nombre;
	private int anioPublicacion;
	private String empresaMatriz;
	private int minJugadores;
	private int maxJugadores;
	private int edadMinima;
	private String categoria;
	private boolean esDificil;
	private CopiaPrestamo[] copiasPrestamos;
	
	public CopiaPrestamo getCopiaDisponible() {
		
	}
	
	public CopiaPrestamo[] getCopiasPrestamo() {
		return copiasPrestamos;
	}
	
	public boolean esAptoParaEdad(int edadMinimaEnMesa) {
		//TODO
	}
	
	public boolean soportaNPersonas(int n) {
		//TODO
	}
	
	public boolean estaDisponibleParaVenta() {
		//TODO
	}
	
	public void incrementarContadorPrestamos() {
		//TODO
	}
	
	public String getCategoria() {
		return this.categoria;
	}
}
