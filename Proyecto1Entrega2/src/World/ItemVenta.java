package World;

public class ItemVenta {
	private int cantidad;
	private double precioUnitario;
	private double descuentoAplicado;
	private ProductoVendible producto;
	
	public double calcularImpuestoItem() {
		//TODO
	}
	
	public double getSubtotalItem() {
		double bruto = precioUnitario * cantidad;
        double descuento = bruto * (descuentoAplicado / 100); //descuentoAplicado es un porcentaje? o ya es el valor?
        return bruto - descuento;
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

    public double getDescuentoAplicado() {
        return descuentoAplicado;
    }

	public void setDescuentoAplicado(double descuentoAplicado) {
        this.descuentoAplicado = descuentoAplicado;
    }

    public ProductoVendible getProducto() {
        return producto;
    }

    public void setProducto(ProductoVendible producto) {
        this.producto = producto;
    }
}
