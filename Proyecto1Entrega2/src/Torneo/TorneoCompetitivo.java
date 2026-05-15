package Torneo;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Usuario.DiaSemana;
import Usuario.Empleado;
import Usuario.Usuario;
import World.Juego;

public class TorneoCompetitivo extends Torneo {
    private double tarifaEntrada;
    private double pozoPremio;
    private double porcentajePozoPremio;
    private Map<String, Double> pagosPorUsuario;

    public TorneoCompetitivo(String idTorneo, DiaSemana dia, Date fechaInicio, int duracionMin,
            EstadoTorneo estado, int cupoTotal, int cupoReservadoFanaticos, int cupoReservadoOcupado,
            int cupoOcupadoRegular, Date fechaCreacion, String nombre, Juego juegoTorneo,
            List<InscripcionTorneo> inscripciones, double tarifaEntrada) {

        super(idTorneo, dia, fechaInicio, duracionMin, estado, cupoTotal, cupoReservadoFanaticos,
                cupoReservadoOcupado, cupoOcupadoRegular, fechaCreacion, nombre, juegoTorneo,
                inscripciones);

        this.tarifaEntrada = tarifaEntrada;
        this.porcentajePozoPremio = 0.70;
        this.pozoPremio = 0.0;
        this.pagosPorUsuario = new HashMap<String, Double>();
    }

    public double calcularPozo() {
        double totalPagado = 0.0;

        for (Double pago : pagosPorUsuario.values()) {
            totalPagado += pago;
        }

        this.pozoPremio = totalPagado * porcentajePozoPremio;

        return pozoPremio;
    }

    public boolean esElegiblePremioMetalico(Usuario u) {
        return u != null && !(u instanceof Empleado);
    }

    public void registrarPago(Usuario u, double monto) {
        if (u == null) {
            throw new IllegalArgumentException("El usuario no puede ser null.");
        }

        if (monto < 0) {
            throw new IllegalArgumentException("El monto no puede ser negativo.");
        }

        pagosPorUsuario.put(u.getLogin(), pagosPorUsuario.getOrDefault(u.getLogin(), 0.0) + monto);

        calcularPozo();
    }

    public double getTarifaEntrada() {
        return tarifaEntrada;
    }

    public double getTarifa() {
        return tarifaEntrada;
    }

    public void setTarifaEntrada(double tarifaEntrada) {
        this.tarifaEntrada = tarifaEntrada;
    }

    public double getPozoPremio() {
        return pozoPremio;
    }

    public double getPorcentajePozoPremio() {
        return porcentajePozoPremio;
    }

    public void setPorcentajePozoPremio(double porcentajePozoPremio) {
        this.porcentajePozoPremio = porcentajePozoPremio;
    }

    public Map<String, Double> getPagosPorUsuario() {
        return pagosPorUsuario;
    }
}