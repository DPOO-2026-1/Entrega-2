package World;
import java.time.*;

public class Venta {
	private int idVenta;
//	private Date fecha;
	private double subtotal;
	private double impuestos;
	private double propina;
	private double total;
	private int descuentoAplicado;
	private int puntosGenerados;
	private Usuario realizadaPor;
	private ItemVenta[] itemsVenta;
	
	public double calcularSubtotal() {
		//TODO
	}
	
	public double calcularImpuestosTotales() {
		//TODO
	}
	
	public double calcularPropina() {
		//TODO
	}
	
	public void setPropina(double porcentaje) {
		//TODO
	}
	
	public double calcularTotal() {
		//TODO
	}
	
	public int calcularPuntosGenerados() {
		//TODO
	}
	
	public void aplicarDescuento(String codigo) {
		//TODO
	}
}
