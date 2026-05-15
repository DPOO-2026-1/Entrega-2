package World;

public class CopiaPrestamo {
	private String idUnico;
	private String estado;
	private boolean estaDisponible;
	private int vecesPrestado;
	private Juego juegoAsociado;
	
	// COMENTARIO: Añadido constructor público para poder instanciar copias de préstamo en las pruebas y lógica de negocio.
	public CopiaPrestamo(String idUnico, String estado, boolean estaDisponible, int vecesPrestado) {
        this.idUnico = idUnico;
        this.estado = estado;
        this.estaDisponible = estaDisponible;
        this.vecesPrestado = vecesPrestado;
    }
    
    // COMENTARIO: Añadido setter para disponibilidad y getter de vecesPrestado.
    public void setEstaDisponible(boolean estaDisponible) {
        this.estaDisponible = estaDisponible;
    }
    
    public int getVecesPrestado() {
        return vecesPrestado;
    }
	
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
	public void setEstado(String estado) {
        this.estado = estado;
    }

    public Juego getJuegoAsociado() {
        return juegoAsociado;
    }

    public void setJuegoAsociado(Juego juegoAsociado) {
        this.juegoAsociado = juegoAsociado;
    }

    public void setEstaDisponible(boolean estaDisponible) {
        this.estaDisponible = estaDisponible;
    }

    public int getVecesPrestado() {
        return vecesPrestado;
    }
}
