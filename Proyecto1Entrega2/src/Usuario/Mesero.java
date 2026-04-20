package Proyecto1Entrega2.src.World.Usuario;

import java.time.LocalDateTime;

import ModuloVenta.ItemVenta;
import ModuloVenta.Venta;
import World.Juego;
import World.Mesa;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Mesero extends Empleado{
	private List<Juego> juegosConocidos;

	public Mesero(String login, String password, String nombre, String codigoDescuento) {
        super(login, password, nombre, codigoDescuento);
        this.juegosConocidos = new ArrayList<>();
    }
	
	public Venta registrarVenta(ItemVenta[] items, Mesa mesa) {
        Venta v = new Venta();
        v.setItemsVenta(items);
        v.setFecha(LocalDateTime.now());
        v.setRealizadaPor(this);
        v.calcularSubtotal();
        v.calcularImpuestosTotales();
        v.setPropina(0.10);
        v.calcularTotal();
        v.calcularPuntosGenerados();
        return v;
    }
	
	public boolean puedeEnsenar(Juego j) {
        return juegosConocidos.contains(j);
    }
	
	public void agregarJuegoConocido(Juego j) {
        if (j != null && !juegosConocidos.contains(j)) {
            juegosConocidos.add(j);
        }
    }

	public List<Juego> getJuegosConocidos() {
        return juegosConocidos;
    }
}
