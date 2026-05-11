package ModuloVenta;

public abstract class ProductoComestible implements ProductoVendible {
	private String nombre;
	private double precioBase;
	
	// COMENTARIO: Constructor para instanciar productos comestibles
	public ProductoComestible(String nombre, double precioBase) {
        this.nombre = nombre;
        this.precioBase = precioBase;
    }
	
	public double getTasaImpuesto() {
		return 0.08;
	}
}
