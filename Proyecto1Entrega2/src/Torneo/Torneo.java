package Torneo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import Usuario.Usuario;
import World.Juego;
import Usuario.DiaSemana;

public abstract class Torneo {
    private String idTorneo;
    private DiaSemana dia;
    private Date fechaInicio;
    private int duracionMin;
    private EstadoTorneo estado;
    private int cupoTotal;
    private int cupoReservadoFanaticos;
    private int cupoOcupadoReservado;
    private int cupoOcupadoRegular;
    private Date fechaCreacion;
    private static final int REGLA_MAX_CUPOS_POR_USUARIO = 3;
    private String nombre;
    private Juego juegoTorneo;
    private List<InscripcionTorneo> inscripciones;

    public Torneo(String idTorneo, DiaSemana dia, Date fechaInicio, int duracionMin, EstadoTorneo estado,
            int cupoTotal, int cupoReservadoFanaticos, int cupoOcupadoReservado, int cupoOcupadoRegular,
            Date fechaCreacion, String nombre, Juego juegoTorneo, List<InscripcionTorneo> inscripciones) {

        this.idTorneo = idTorneo;
        this.dia = dia;
        this.fechaInicio = fechaInicio != null ? fechaInicio : new Date();
        this.duracionMin = duracionMin;
        this.estado = estado != null ? estado : EstadoTorneo.PROGRAMADO;
        this.cupoTotal = cupoTotal;
        this.cupoReservadoFanaticos = cupoReservadoFanaticos;
        this.cupoOcupadoReservado = cupoOcupadoReservado;
        this.cupoOcupadoRegular = cupoOcupadoRegular;
        this.fechaCreacion = fechaCreacion != null ? fechaCreacion : new Date();
        this.nombre = nombre;
        this.juegoTorneo = juegoTorneo;
        this.inscripciones = inscripciones != null ? inscripciones : new ArrayList<InscripcionTorneo>();
    }

    public int cuposDisponiblesReservados() {
        return Math.max(0, cupoReservadoFanaticos - cupoOcupadoReservado);
    }

    public int cuposDisponiblesRegulares() {
        return Math.max(0, cupoTotal - cupoOcupadoReservado - cupoOcupadoRegular);
    }

    public boolean esFanatico(Usuario u) {
        return u != null && juegoTorneo != null && u.getJuegosFavoritos().contains(juegoTorneo);
    }

    public boolean validarCupoMaximoPorUsuario(int cantidad) {
        return cantidad > 0 && cantidad <= REGLA_MAX_CUPOS_POR_USUARIO;
    }

    public boolean puedeInscribirse(Usuario u, int cantidad) {
        if (u == null) {
            return false;
        }

        if (!validarCupoMaximoPorUsuario(cantidad)) {
            return false;
        }

        return cuposDisponiblesReservados() + cuposDisponiblesRegulares() >= cantidad;
    }

    public String getIdTorneo() {
        return idTorneo;
    }

    public void setIdTorneo(String idTorneo) {
        this.idTorneo = idTorneo;
    }

    public DiaSemana getDia() {
        return dia;
    }

    public void setDia(DiaSemana dia) {
        this.dia = dia;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public int getDuracionMin() {
        return duracionMin;
    }

    public void setDuracionMin(int duracionMin) {
        this.duracionMin = duracionMin;
    }

    public EstadoTorneo getEstado() {
        return estado;
    }

    public void setEstado(EstadoTorneo estado) {
        this.estado = estado;
    }

    public int getCupoTotal() {
        return cupoTotal;
    }

    public void setCupoTotal(int cupoTotal) {
        this.cupoTotal = cupoTotal;
    }

    public int getCupoReservadoFanaticos() {
        return cupoReservadoFanaticos;
    }

    public void setCupoReservadoFanaticos(int cupoReservadoFanaticos) {
        this.cupoReservadoFanaticos = cupoReservadoFanaticos;
    }

    public int getCupoOcupadoReservado() {
        return cupoOcupadoReservado;
    }

    public void setCupoOcupadoReservado(int cupoOcupadoReservado) {
        this.cupoOcupadoReservado = cupoOcupadoReservado;
    }

    public int getCupoOcupadoRegular() {
        return cupoOcupadoRegular;
    }

    public void setCupoOcupadoRegular(int cupoOcupadoRegular) {
        this.cupoOcupadoRegular = cupoOcupadoRegular;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Juego getJuegoTorneo() {
        return juegoTorneo;
    }

    public void setJuegoTorneo(Juego juegoTorneo) {
        this.juegoTorneo = juegoTorneo;
    }

    public List<InscripcionTorneo> getInscripciones() {
        return inscripciones;
    }

    public void setInscripciones(List<InscripcionTorneo> inscripciones) {
        this.inscripciones = inscripciones;
    }

    public static int getReglaMaxCuposPorUsuario() {
        return REGLA_MAX_CUPOS_POR_USUARIO;
    }
}