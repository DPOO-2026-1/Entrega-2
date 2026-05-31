package ModuloVenta;
import World.Juego;

public class CopiaVenta extends Juego implements ProductoVendible{
	private String idUnico;
	private double precioVenta;
	
	// COMENTARIO: Añadido constructor público para CopiaVenta.
	public CopiaVenta(String idUnico, double precioVenta) {
        super();
        this.idUnico = idUnico;
        this.precioVenta = precioVenta;
    }
	
	public double getTasaImpuesto() {
		return 0.19;
	}

	public String getIdUnico() {
        return idUnico;
    }

    public double getPrecioVenta() {
        return precioVenta;
    }
}
