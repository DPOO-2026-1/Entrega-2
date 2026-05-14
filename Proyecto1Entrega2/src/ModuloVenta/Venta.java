package ModuloVenta;
import java.time.*;

import Usuario.Usuario;

public class Venta {
	private int idVenta;
	private LocalDateTime fecha;
	private double subtotal;
	private double impuestos;
	private double propina;
	private double total;
	private int descuentoAplicado;
	private int puntosGenerados;
	private Usuario realizadaPor;
	private ItemVenta[] itemsVenta;
	
	public double calcularSubtotal() {
		double suma = 0;
        if (itemsVenta != null) {
            for (ItemVenta item : itemsVenta) {
                suma += item.getSubtotalItem();
            }
        }
        this.subtotal = suma;
        return subtotal;
    }
	
	public double calcularImpuestosTotales() {
		double suma = 0;
        if (itemsVenta != null) {
            for (ItemVenta item : itemsVenta) {
                suma += item.calcularImpuestoItem();
            }
        }	
        this.impuestos = suma;
        return impuestos;
    }
	
	public double calcularPropina() {
		return propina; //cómo sacábamos la propina? y si el cliente no quiere dar?
	}
	
	public void setPropina(double porcentaje) {
		this.propina = subtotal * porcentaje;
	}
	
	public double calcularTotal() {
		double descuento = subtotal * (descuentoAplicado / 100.0); //dónde definimos si es 10 o 20%? depende del cliente o mesero
        this.total = subtotal - descuento + impuestos + propina;
        return total;
	}
	
	public int calcularPuntosGenerados() {
		this.puntosGenerados = (int)(total * 0.01);
        return puntosGenerados;
	}
	
	public void aplicarDescuento(String codigo) {
		if (codigo.equalsIgnoreCase("EMPLEADO")) {
            this.descuentoAplicado = 20;
        } else if (codigo.equalsIgnoreCase("CLIENTE")) {
            this.descuentoAplicado = 10;
        } else {
            this.descuentoAplicado = 0;
        }
    } //revisar este método, está mal implementado

	public int getIdVenta() {
        return idVenta;
    }

    public void setIdVenta(int idVenta) {
        this.idVenta = idVenta;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

	public Usuario getRealizadaPor() {
        return realizadaPor;
    }

    public void setRealizadaPor(Usuario realizadaPor) {
        this.realizadaPor = realizadaPor;
    }

    public ItemVenta[] getItemsVenta() {
        return itemsVenta;
    }

    public void setItemsVenta(ItemVenta[] itemsVenta) {
        this.itemsVenta = itemsVenta;
    }

}
