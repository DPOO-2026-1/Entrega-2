package Usuario;

import java.time.LocalDateTime;

import ModuloVenta.Bebida;
import ModuloVenta.ItemVenta;
import ModuloVenta.Venta;
import World.Cafeteria;
import World.CopiaPrestamo;
import World.Juego;
import World.Mesa;
import World.Prestamo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Mesero extends Empleado{
	private List<Juego> juegosConocidos;

	public Mesero(String login, String password, String nombre, String codigoDescuento) {
        super(login, password, nombre, codigoDescuento);
        this.juegosConocidos = new ArrayList<>();
    }
	
	public Venta registrarVenta(ItemVenta[] items, Mesa mesa) {
		if (items == null || items.length == 0) {
            throw new IllegalArgumentException("Debe haber al menos un ítem en la venta.");
        }
		
		if (mesa != null) {
            for (ItemVenta item : items) {
                if (item.getProducto() instanceof Bebida) {
                    Bebida b = (Bebida) item.getProducto();
                    
                    if (b.isEsAlcoholica() && !mesa.puedeRecibirBebidaAlcoholica()) {
                        throw new IllegalStateException(
                            "No se puede servir bebida alcohólica a una mesa con menores de edad.");
                    }
                    
                    if (b.isEsCaliente()) {
                        mesa.setHayBebidaCaliente(true);
                        if (!mesa.puedeRecibirJuegoAccion()) {
                            throw new IllegalStateException(
                                "No se puede servir bebida caliente: la mesa tiene un juego de Acción activo.");
                        }
                    }
                }
            }
		}
		
		Venta v = new Venta(new Date(), items, this);
        v.setPropina(0.10);
        v.calcularTotal();
        v.calcularPuntosGenerados();
        
        Cafeteria.getInstance().getVentas().add(v);
        
        return v;
    }
	
	public Venta realizarCompra(List<ItemVenta> items) {
        if (items == null || items.isEmpty()) {
            throw new IllegalArgumentException("Debe haber al menos un ítem en la compra.");
        }
        Venta v = new Venta(new Date(), items.toArray(new ItemVenta[0]), this);
        v.aplicarDescuento("EMPLEADO");
        
        v.calcularSubtotal();
        v.calcularImpuestosTotales();
        v.calcularTotal();
        v.calcularPuntosGenerados();
        
        Cafeteria.getInstance().getVentas().add(v);
        
        return v;
	}
	
	@Override
    public Prestamo alquilarJuego(CopiaPrestamo copia) {
		if (estaEnTurno()) {
            throw new IllegalStateException("No puedes alquilar un juego mientras estás de turno.");
        }

        if (copia == null) {
            throw new IllegalArgumentException("La copia no puede ser nula.");
        }
        if (!copia.estaDisponible()) {
            throw new IllegalStateException("La copia del juego no está disponible.");
        }
        
        Prestamo p = new Prestamo(new Date(), copia, null, this);
        
        Cafeteria.getInstance().getHistorialPrestamos().add(p);
        
        return p;
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
