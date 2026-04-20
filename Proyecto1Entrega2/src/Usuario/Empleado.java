package Usuario;

import World.DiaSemana;
import World.SolicitudTurno;
import World.SugerenciaMenu;
import World.CopiaPrestamo;
import World.Prestamo;
import World.DiaTurno;
import ModuloVenta.ItemVenta;
import ModuloVenta.Venta;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;

public abstract class Empleado extends Usuario {
    protected String codigoDescuento;
    protected boolean estaDeTurno;
    protected List<DiaTurno> diasAsignados;

    public Empleado(String login, String password, String nombre, String codigoDescuento) {
        super(login, password, nombre);
        this.codigoDescuento = codigoDescuento;
        this.estaDeTurno = false;
        this.diasAsignados = new ArrayList<>();
    }

    public SolicitudTurno solicitarCambioTurno(DiaSemana dia) {
        return new SolicitudTurno(dia, "Pendiente", this, false);
    }

    public SolicitudTurno solicitarIntercambioTurno(Empleado otro, DiaSemana dia) {
        return new SolicitudTurno(dia, "Pendiente", this, true);
    }

    public SugerenciaMenu sugerirPlato(String desc) {
        return new SugerenciaMenu(desc, "Pendiente", this);
    }

    public abstract Venta realizarCompra(List<ItemVenta> items);

    public abstract Prestamo alquilarJuego(CopiaPrestamo copia);

    public boolean estaEnTurno() {
        return this.estaDeTurno;
    }

    public List<DiaTurno> consultarDiaAsignado() {
        return this.diasAsignados;
    }
}