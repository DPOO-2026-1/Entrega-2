package Usuario;

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
        this.juegosConocidos = new ArrayList<Juego>();
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

	// ===== CAMBIO HECHO =====
    // Implementé este método porque Empleado lo declara abstracto.
    // Si no se implementa, Mesero no compila.
    // Además aplica descuento de empleado.
    // ===== FIN CAMBIO =====
    @Override
    public Venta realizarCompra(List<ItemVenta> items) {
        Venta v = new Venta();

        if (items != null) {
            v.setItemsVenta(items.toArray(new ItemVenta[0]));
        } else {
            v.setItemsVenta(new ItemVenta[0]);
        }

        v.setFecha(LocalDateTime.now());
        v.setRealizadaPor(this);

        v.calcularSubtotal();
        v.calcularImpuestosTotales();

        // ===== CAMBIO HECHO =====
        // Descuento de empleado. Se usa 0.20 porque Venta.aplicarDescuento(double)
        // recibe porcentaje en decimal.
        // ===== FIN CAMBIO =====
        v.aplicarDescuento(0.20);

        v.calcularTotal();
        v.calcularPuntosGenerados();

        return v;
    }

    // ===== CAMBIO HECHO =====
    // Implementé este método porque Empleado lo declara abstracto.
    // Un empleado no puede alquilar juegos si está de turno.
    // ===== FIN CAMBIO =====
    @Override
    public Prestamo alquilarJuego(CopiaPrestamo copia) {
        if (this.estaDeTurno) {
            throw new IllegalStateException("No se puede alquilar en turno laboral.");
        }

        if (copia == null || !copia.estaDisponible()) {
            throw new IllegalStateException("Copia no disponible.");
        }

        return new Prestamo(new java.util.Date(), copia, null, this);
    }

    // ===== CAMBIO HECHO =====
    // Getter necesario para consultar los juegos que el mesero sabe enseñar.
    // ===== FIN CAMBIO =====
    public List<Juego> getJuegosConocidos() {
        return juegosConocidos;
    }
}
