package Usuario;

import World.Cafeteria;
import World.CopiaPrestamo;
import World.Prestamo;
import ModuloVenta.ItemVenta;
import ModuloVenta.Venta;
import java.util.List;
import java.util.Date;

public class Cocinero extends Empleado {

    public Cocinero(String login, String password, String nombre, String codigoDescuento) {
        super(login, password, nombre, codigoDescuento);
    }

    @Override
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
}	