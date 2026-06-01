package World;

import ModuloVenta.CopiaVenta;
import java.util.ArrayList;
import java.util.List;

public class Juego {
	private String nombre;
	private int anioPublicacion;
	private String empresaMatriz;
	private int minJugadores;
	private int maxJugadores;
	private int edadMinima;
	private String categoria;
	private boolean esDificil;
	private List<CopiaPrestamo> copiasParaPrestamo; 
    private List<CopiaVenta> copiasParaVenta;
	
	// COMENTARIO: Se añade constructor protegido vacío para permitir que CopiaVenta instancie sin proveer todos los atributos del juego de base.
	protected Juego() {}
	
    public Juego(String nombre, int anioPublicacion, String empresaMatriz, int minJugadores, 
            int maxJugadores, int edadMinima, String categoria, boolean esDificil) {
    this.nombre = nombre;
    this.anioPublicacion = anioPublicacion;
    this.empresaMatriz = empresaMatriz;
    this.minJugadores = minJugadores;
    this.maxJugadores = maxJugadores;
    this.edadMinima = edadMinima;
    this.categoria = categoria;
    this.esDificil = esDificil;
   
    this.copiasParaPrestamo = new ArrayList<CopiaPrestamo>();
    this.copiasParaVenta = new ArrayList<CopiaVenta>();
    }
    
    public CopiaPrestamo getCopiaDisponible() {
        for (CopiaPrestamo copia : copiasParaPrestamo) {
            if (copia.estaDisponible()) {
                return copia;
            }
        }
        return null; // Retorna null si todas las copias están prestadas
    }
    
	public List<CopiaPrestamo> getCopiasPrestamo() {
		return copiasParaPrestamo;
	}
	
	public boolean esAptoParaEdad(int edadMinimaEnMesa) {
		return edadMinimaEnMesa >= this.edadMinima;
	}
	
	public boolean soportaNPersonas(int n) {
		return n >= this.minJugadores && n <= this.maxJugadores;
	}
	
	public String getCategoria() {
        return this.categoria;
    }
	
	public boolean estaDisponibleParaVenta() {
        return !this.copiasParaVenta.isEmpty();
    }
    
	public boolean estaDisponibleParaPrestamo() {
		if (copiasParaPrestamo != null) {
            for (CopiaPrestamo copia : copiasParaPrestamo) {
                if (copia != null && copia.estaDisponible()) {
                    return true;
                }
            }
        }
        return false;
	}
	
	public void agregarCopiaPrestamo(CopiaPrestamo copia) { 
        this.copiasParaPrestamo.add(copia); 
    }
    
    public void agregarCopiaVenta(CopiaVenta copia) { 
        this.copiasParaVenta.add(copia); 
    }

    public String getNombre() { return nombre; }
    public int getAnioPublicacion() { return anioPublicacion; }
    public String getEmpresaMatriz() { return empresaMatriz; }
    public int getMinJugadores() { return minJugadores; }
    public int getMaxJugadores() { return maxJugadores; }
    public int getEdadMinima() { return edadMinima; }
    public boolean isEsDificil() { return esDificil; }

	public List<CopiaPrestamo> getCopiasParaPrestamo() {
		return copiasParaPrestamo;
	}

	public void setCopiasParaPrestamo(List<CopiaPrestamo> copiasParaPrestamo) {
		this.copiasParaPrestamo = copiasParaPrestamo;
	}

	public List<CopiaVenta> getCopiasParaVenta() {
		return copiasParaVenta;
	}

	public void setCopiasParaVenta(List<CopiaVenta> copiasParaVenta) {
		this.copiasParaVenta = copiasParaVenta;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public void setAnioPublicacion(int anioPublicacion) {
		this.anioPublicacion = anioPublicacion;
	}

	public void setEmpresaMatriz(String empresaMatriz) {
		this.empresaMatriz = empresaMatriz;
	}

	public void setMinJugadores(int minJugadores) {
		this.minJugadores = minJugadores;
	}

	public void setMaxJugadores(int maxJugadores) {
		this.maxJugadores = maxJugadores;
	}

	public void setEdadMinima(int edadMinima) {
		this.edadMinima = edadMinima;
	}

	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}

	public void setEsDificil(boolean esDificil) {
		this.esDificil = esDificil;
	}
	
	@Override
	public String toString() {
	    return nombre;
	}
    
}
