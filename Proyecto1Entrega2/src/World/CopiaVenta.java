package World;

public class CopiaVenta extends Juego implements ProductoVendible{
	private String idUnico;
	private double precioVenta;
	
	public double getTasaImpuesto() {
		return 0.19;
	}
}
