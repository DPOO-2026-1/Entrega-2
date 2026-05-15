package Torneo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import Usuario.DiaSemana;
import Usuario.Usuario;
import World.Juego;

public class TorneoAmistoso extends Torneo {
    private double valorBono;
    private boolean bonoOtorgado;
    private List<BonoTorneoAmistoso> bonos;

    public TorneoAmistoso(String idTorneo, DiaSemana dia, Date fechaInicio, int duracionMin,
            EstadoTorneo estado, int cupoTotal, int cupoReservadoFanaticos, int cupoReservadoOcupado,
            int cupoOcupadoRegular, Date fechaCreacion, String nombre, Juego juegoTorneo,
            List<InscripcionTorneo> inscripciones) {

        this(idTorneo, dia, fechaInicio, duracionMin, estado, cupoTotal, cupoReservadoFanaticos,
                cupoReservadoOcupado, cupoOcupadoRegular, fechaCreacion, nombre, juegoTorneo,
                inscripciones, 5000.0);
    }

    public TorneoAmistoso(String idTorneo, DiaSemana dia, Date fechaInicio, int duracionMin,
            EstadoTorneo estado, int cupoTotal, int cupoReservadoFanaticos, int cupoReservadoOcupado,
            int cupoOcupadoRegular, Date fechaCreacion, String nombre, Juego juegoTorneo,
            List<InscripcionTorneo> inscripciones, double valorBono) {

        super(idTorneo, dia, fechaInicio, duracionMin, estado, cupoTotal, cupoReservadoFanaticos,
                cupoReservadoOcupado, cupoOcupadoRegular, fechaCreacion, nombre, juegoTorneo,
                inscripciones);

        this.valorBono = valorBono;
        this.bonoOtorgado = false;
        this.bonos = new ArrayList<BonoTorneoAmistoso>();
    }

    public BonoTorneoAmistoso otorgarBono(Usuario ganador) {
        if (ganador == null) {
            throw new IllegalArgumentException("El ganador no puede ser null.");
        }

        if (bonoOtorgado) {
            throw new IllegalStateException("Este torneo amistoso ya otorgó un bono.");
        }

        String codigo = "BONO-" + getIdTorneo() + "-" + ganador.getLogin() + "-" + System.currentTimeMillis();

        BonoTorneoAmistoso bono = new BonoTorneoAmistoso(codigo, valorBono, ganador, this);

        bonos.add(bono);
        bonoOtorgado = true;

        return bono;
    }

    public double getValorBono() {
        return valorBono;
    }

    public void setValorBono(double valorBono) {
        this.valorBono = valorBono;
    }

    public boolean isBonoOtorgado() {
        return bonoOtorgado;
    }

    public List<BonoTorneoAmistoso> getBonos() {
        return bonos;
    }
}