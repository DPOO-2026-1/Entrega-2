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
		if (copiasPrestamos != null) {
			for (CopiaPrestamo copia : copiasPrestamos) {
                if (copia != null && copia.estaDisponible()) {
                    return copia;
                }
            }
		}
		return null;
	}
	
	public CopiaPrestamo[] getCopiasPrestamo() {
		return copiasPrestamos;
	}
	
	public boolean esAptoParaEdad(int edadMinimaEnMesa) {
		return edadMinimaEnMesa >= this.edadMinima;
	}
	
	public boolean soportaNPersonas(int n) {
		return n >= this.minJugadores && n <= this.maxJugadores;
	}
	
	public boolean estaDisponibleParaVenta() {
		//REVISAR CÓMO LO HAYAMOS IMPLEMENTADO
		if (copiasPrestamos != null) {
            for (CopiaPrestamo copia : copiasPrestamos) {
                if (copia != null && copia.estaDisponibleParaVenta()) {
                    return true;
                }
            }
        }
        return false;
	}
	
	public String getCategoria() {
		return this.categoria;
	}
}
