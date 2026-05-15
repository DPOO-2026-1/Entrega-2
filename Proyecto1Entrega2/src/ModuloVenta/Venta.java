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

    // ===== CAMBIO HECHO =====
    // Agregué constructor vacío para permitir crear ventas desde Mesero, Cliente, GestorVentas o pruebas.
    // ===== FIN CAMBIO =====
    public Venta() {
    }

    // ===== CAMBIO HECHO =====
    // Agregué constructor compatible con Date, porque el UML usa Date,
    // pero internamente esta clase usa LocalDateTime.
    // ===== FIN CAMBIO =====
    public Venta(Date fecha, ItemVenta[] itemsVenta, Usuario realizadaPor) {
        if (fecha != null) {
            this.fecha = LocalDateTime.ofInstant(fecha.toInstant(), ZoneId.systemDefault());
        } else {
            this.fecha = LocalDateTime.now();
        }

        this.itemsVenta = itemsVenta;
        this.realizadaPor = realizadaPor;

        calcularSubtotal();
        calcularImpuestosTotales();
        calcularTotal();
        calcularPuntosGenerados();
    }
	
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
	
	// ===== CAMBIO HECHO =====
    // Implementé descuento por código según el UML.
    // ===== FIN CAMBIO =====
    public void aplicarDescuento(String codigo) {
        if (codigo == null) {
            this.descuentoAplicado = 0;
        } else if (codigo.equalsIgnoreCase("EMPLEADO")) {
            this.descuentoAplicado = 20;
        } else if (codigo.equalsIgnoreCase("CLIENTE")) {
            this.descuentoAplicado = 10;
        } else {
            this.descuentoAplicado = 0;
        }
    }

    // ===== CAMBIO HECHO =====
    // Implementé aplicarBono(BonoTorneoAmistoso) porque el UML conecta Venta con BonoTorneoAmistoso.
    // Regla del UML: el bono no es acumulable con descuentos o puntos.
    // ===== FIN CAMBIO =====
    public void aplicarBono(BonoTorneoAmistoso bono) {
        if (bono == null || !bono.estaDisponible()) {
            throw new IllegalArgumentException("El bono no está disponible.");
        }

        if (descuentoAplicado > 0 || puntosGenerados < 0) {
            throw new IllegalStateException("El bono de torneo no es acumulable con otros descuentos o puntos.");
        }

        calcularSubtotal();
        calcularImpuestosTotales();

        double descuento = bono.getValor();
        this.total = Math.max(0, subtotal + impuestos + propina - descuento);

        bono.marcarUsado();
    }

	public double getSubtotal() {
        return subtotal;
    }

    public double getImpuestos() {
        return impuestos;
    }

    public double getPropina() {
        return propina;
    }

    public double getTotal() {
        return total;
    }

    public int getDescuentoAplicado() {
        return descuentoAplicado;
    }

    public int getPuntosGenerados() {
        return puntosGenerados;
    }

    public void setPuntosGenerados(int puntosGenerados) {
        this.puntosGenerados = puntosGenerados;
    }

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
