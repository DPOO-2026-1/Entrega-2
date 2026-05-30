package Usuario;

import World.Cafeteria;
import World.CopiaPrestamo;
import World.Prestamo;
import ModuloVenta.ItemVenta;
import ModuloVenta.Venta;
import java.util.List;
import java.util.ArrayList;

public abstract class Empleado extends Usuario {
    protected String codigoDescuento;
    protected List<DiaTurno> diasAsignados;

    public Empleado(String login, String password, String nombre, String codigoDescuento) {
        super(login, password, nombre);
        this.codigoDescuento = codigoDescuento;
        this.diasAsignados = new ArrayList<>();
    }
    
    public boolean estaEnTurno() {
        DiaSemana hoy = obtenerDiaHoy();
        for (DiaTurno dt : diasAsignados) {
            if (dt.getDia().mismoDia(hoy) && dt.estaAsignado()) {
                return true;
            }
        }
        return false;
    }
    //este método es para convertir el día sacado con java.time al enum que hicimos nosotros para los días
    private DiaSemana obtenerDiaHoy() {
        java.time.DayOfWeek dow = java.time.LocalDate.now().getDayOfWeek();
        switch (dow) {
            case MONDAY:    return DiaSemana.LUNES;
            case TUESDAY:   return DiaSemana.MARTES;
            case WEDNESDAY: return DiaSemana.MIERCOLES;
            case THURSDAY:  return DiaSemana.JUEVES;
            case FRIDAY:    return DiaSemana.VIERNES;
            case SATURDAY:  return DiaSemana.SABADO;
            default:        return DiaSemana.DOMINGO;
        }
    }

    public SolicitudTurno solicitarCambioTurno(DiaSemana dia) {
    	boolean trabajaEseDia = false;
    	for (DiaTurno dt : diasAsignados) {
            if (dt.getDia().mismoDia(dia) && dt.estaAsignado()) {
                trabajaEseDia = true;
                break;
            }
        }
    	if (!trabajaEseDia) {
            throw new IllegalStateException("No tienes turno asignado ese día.");
        }
        return new SolicitudTurno(dia, "Pendiente", this, false);
    }

    public SolicitudTurno solicitarIntercambioTurno(Empleado otro, DiaSemana dia) {
    	boolean otroTrabaja = false;
        for (DiaTurno dt : otro.consultarDiasAsignados()) {
            if (dt.getDia().mismoDia(dia) && dt.estaAsignado()) {
                otroTrabaja = true;
                break;
            }
        }
        if (!otroTrabaja) {
            throw new IllegalStateException("El otro empleado no tiene turno ese día para intercambiar.");
        }
        return new SolicitudTurno(dia, "Pendiente", this, true);
    }

    public SugerenciaMenu sugerirPlato(String desc) {
        if (desc == null || desc.trim().isEmpty()) {
            throw new IllegalArgumentException("La descripción del plato no puede estar vacía.");
        }
        SugerenciaMenu sugerencia = new SugerenciaMenu(desc, "Pendiente", this);
        Cafeteria.getInstance().getSugerencias().add(sugerencia);
        return sugerencia;
    }

    public abstract Venta realizarCompra(List<ItemVenta> items);

    public abstract Prestamo alquilarJuego(CopiaPrestamo copia);
    
    public String getCodigoDescuento() {
        return codigoDescuento;
    }
    
    public List<DiaTurno> consultarDiasAsignados() {
        return this.diasAsignados;
    }
    
    public void setDiasAsignados(List<DiaTurno> diasAsignados) {
        this.diasAsignados = diasAsignados;
    }
}