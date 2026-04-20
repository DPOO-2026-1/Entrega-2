package World;

public class CopiaPrestamo {
	private String idUnico;
	private String estado;
	private boolean estaDisponible;
	private int vecesPrestado;
	
	public void prestar() {
		if (estaDisponible) {
            this.estaDisponible = false;
        } else {
            throw new IllegalStateException("La copia ya está prestada o no disponible.");
        }
	}
	
	public void devolver() {
		this.estaDisponible = true;
	}
	
	public void marcarDesaparecida() {
		this.estaDisponible = false;
        this.estado = "Desaparecida";
	}
	
	public void reparar() {
		this.estaDisponible = true;
        this.estado = "Reparada";
	}
	
	public boolean estaDisponible() {
		return estaDisponible;
	}

	public void incrementarContadorPrestamos() {
		this.vecesPrestado++;
	}

	public String getIdUnico() {
        return idUnico;
    }

	public String getEstado() {
        return estado;
    }
}
