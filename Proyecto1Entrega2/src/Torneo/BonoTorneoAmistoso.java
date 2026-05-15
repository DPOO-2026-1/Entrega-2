package Torneo;

import java.util.Date;

import Usuario.Usuario;

public class BonoTorneoAmistoso {
    public static final String ESTADO_DISPONIBLE = "Disponible";
    public static final String ESTADO_USADO = "Usado";

    private String codigo;
    private double valor;
    private String estado;
    private Date fechaOtorgado;
    private Date fechaUsado;
    private Usuario ganador;
    private Torneo torneo;

    public BonoTorneoAmistoso(String codigo, double valor, Usuario ganador, Torneo torneo) {
        this.codigo = codigo;
        this.valor = valor;
        this.estado = ESTADO_DISPONIBLE;
        this.fechaOtorgado = new Date();
        this.fechaUsado = null;
        this.ganador = ganador;
        this.torneo = torneo;
    }

    public boolean estaDisponible() {
        return ESTADO_DISPONIBLE.equalsIgnoreCase(estado);
    }

    public void marcarUsado() {
        this.estado = ESTADO_USADO;
        this.fechaUsado = new Date();
    }

    public String getCodigo() {
        return codigo;
    }

    public double getValor() {
        return valor;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Date getFechaOtorgado() {
        return fechaOtorgado;
    }

    public Date getFechaUsado() {
        return fechaUsado;
    }

    public Usuario getGanador() {
        return ganador;
    }

    public Torneo getTorneo() {
        return torneo;
    }
}