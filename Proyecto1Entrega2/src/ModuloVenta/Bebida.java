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
}
