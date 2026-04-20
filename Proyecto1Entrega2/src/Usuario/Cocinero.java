package Usuario;

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
        Venta v = new Venta(new Date(), items.toArray(new ItemVenta[0]), this);
        v.aplicarDescuento(0.20); // 20% descuento empleado 
        return v;
    }

    @Override
    public Prestamo alquilarJuego(CopiaPrestamo copia) {
        if (this.estaDeTurno) {
            throw new IllegalStateException("No se puede alquilar en turno laboral."); 
        }
        if (!copia.estaDisponible()) {
            throw new IllegalStateException("Copia no disponible.");
        }
        
        Prestamo p = new Prestamo(new Date(), copia, null, this); 
        copia.prestar();
        return p;
    }
}	