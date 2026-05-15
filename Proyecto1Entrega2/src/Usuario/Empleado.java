package Usuario;

import World.CopiaPrestamo;
import World.Prestamo;
import ModuloVenta.ItemVenta;
import ModuloVenta.Venta;
import java.util.List;
import java.util.ArrayList;

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
    
    public String getCodigoDescuento() {
        return codigoDescuento;
    }

    // COMENTARIO: Se renombra el método de consultarDiaAsignado a consultarDiasAsignados para reflejar que retorna una lista y coincidir con su uso en Cafeteria.
    public List<DiaTurno> consultarDiasAsignados() {
        return this.diasAsignados;
    }
}