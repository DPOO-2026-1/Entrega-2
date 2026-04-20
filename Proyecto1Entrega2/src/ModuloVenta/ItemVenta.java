package ModuloVenta;

public class ItemVenta {
	private int cantidad;
	private double precioUnitario;
	private ProductoVendible producto;
	
	public double calcularImpuestoItem() {
		double subtotal = getSubtotalItem();
        return subtotal * producto.getTasaImpuesto();
	}
	
	public double getSubtotalItem() {
		return precioUnitario * cantidad;
	}

	public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public double getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(double precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public ProductoVendible getProducto() {
        return producto;
    }

    public void setProducto(ProductoVendible producto) {
        this.producto = producto;
    }
}
