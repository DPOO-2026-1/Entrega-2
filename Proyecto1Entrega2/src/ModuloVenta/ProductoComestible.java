package ModuloVenta;

public abstract class ProductoComestible implements ProductoVendible {
	private String nombre;
	private double precioBase;
	
	public double getTasaImpuesto() {
		return 0.08;
	}
}
