package ModuloVenta;

public class Bebida extends ProductoComestible{
	private boolean esCaliente;
	private boolean esAlcoholica;
	
	// COMENTARIO: Añadido constructor para Bebida
	public Bebida(String nombre, double precioBase, boolean esCaliente, boolean esAlcoholica) {
        super(nombre, precioBase);
        this.esCaliente = esCaliente;
        this.esAlcoholica = esAlcoholica;
    }

	public boolean isEsCaliente() {
		return esCaliente;
	}

	public void setEsCaliente(boolean esCaliente) {
		this.esCaliente = esCaliente;
	}

	public boolean isEsAlcoholica() {
		return esAlcoholica;
	}

	public void setEsAlcoholica(boolean esAlcoholica) {
		this.esAlcoholica = esAlcoholica;
	}
}
