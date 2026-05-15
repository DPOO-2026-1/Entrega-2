package Torneo;

import Usuario.Usuario;

public class ResultadoTorneo {
    private Torneo torneo;
    private Usuario ganador;
    private double premioMetalico;
    private BonoTorneoAmistoso bono;

    public ResultadoTorneo(Torneo torneo, Usuario ganador, double premioMetalico, BonoTorneoAmistoso bono) {
        this.torneo = torneo;
        this.ganador = ganador;
        this.premioMetalico = premioMetalico;
        this.bono = bono;
    }

    public Torneo getTorneo() {
        return torneo;
    }

    public Usuario getGanador() {
        return ganador;
    }

    public double getPremioMetalico() {
        return premioMetalico;
    }

    public BonoTorneoAmistoso getBono() {
        return bono;
    }
}