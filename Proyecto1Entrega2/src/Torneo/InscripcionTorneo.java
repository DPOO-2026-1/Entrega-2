package Torneo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import Usuario.Usuario;

public class InscripcionTorneo {
    private String idInscripcion;
    private Date fecha;
    private List<Usuario> usuarios;
    private int cantidadCupos;
    private int cuposReservados;
    private int cuposRegulares;
    private boolean esEmpleado;
    private double montoPagado;
    private boolean pagoConfirmado;
    private boolean elegiblePrecioMetalico;

    public InscripcionTorneo(String idInscripcion, Date fecha, List<Usuario> usuarios, int cantidadCupos,
            int cuposReservados, int cuposRegulares, boolean esEmpleado, double montoPagado,
            boolean pagoConfirmado, boolean elegiblePrecioMetalico) {

        this.idInscripcion = idInscripcion;
        this.fecha = fecha != null ? fecha : new Date();
        this.usuarios = usuarios != null ? usuarios : new ArrayList<Usuario>();
        this.cantidadCupos = cantidadCupos;
        this.cuposReservados = cuposReservados;
        this.cuposRegulares = cuposRegulares;
        this.esEmpleado = esEmpleado;
        this.montoPagado = montoPagado;
        this.pagoConfirmado = pagoConfirmado;
        this.elegiblePrecioMetalico = elegiblePrecioMetalico;

        validarConsistencia();
    }

    private void validarConsistencia() {
        if (cantidadCupos < 1 || cantidadCupos > 3) {
            throw new IllegalArgumentException("La inscripción debe tener entre 1 y 3 cupos.");
        }

        if (cuposReservados < 0 || cuposRegulares < 0) {
            throw new IllegalArgumentException("Los cupos usados no pueden ser negativos.");
        }

        if (cuposReservados + cuposRegulares != cantidadCupos) {
            throw new IllegalArgumentException("La suma de cupos reservados y regulares debe ser igual a cantidadCupos.");
        }
    }

    public String getIdInscripcion() {
        return idInscripcion;
    }

    public void setIdInscripcion(String idInscripcion) {
        this.idInscripcion = idInscripcion;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public List<Usuario> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(List<Usuario> usuarios) {
        this.usuarios = usuarios;
    }

    public int getCantidadCupos() {
        return cantidadCupos;
    }

    public void setCantidadCupos(int cantidadCupos) {
        this.cantidadCupos = cantidadCupos;
    }

    public int getCuposReservados() {
        return cuposReservados;
    }

    public void setCuposReservados(int cuposReservados) {
        this.cuposReservados = cuposReservados;
    }

    public int getCuposRegulares() {
        return cuposRegulares;
    }

    public void setCuposRegulares(int cuposRegulares) {
        this.cuposRegulares = cuposRegulares;
    }

    public boolean isEsEmpleado() {
        return esEmpleado;
    }

    public void setEsEmpleado(boolean esEmpleado) {
        this.esEmpleado = esEmpleado;
    }

    public double getMontoPagado() {
        return montoPagado;
    }

    public void setMontoPagado(double montoPagado) {
        this.montoPagado = montoPagado;
    }

    public boolean isPagoConfirmado() {
        return pagoConfirmado;
    }

    public void setPagoConfirmado(boolean pagoConfirmado) {
        this.pagoConfirmado = pagoConfirmado;
    }

    public boolean isElegiblePrecioMetalico() {
        return elegiblePrecioMetalico;
    }

    public boolean isElegiblePremioMetalico() {
        return elegiblePrecioMetalico;
    }

    public void setElegiblePrecioMetalico(boolean elegiblePrecioMetalico) {
        this.elegiblePrecioMetalico = elegiblePrecioMetalico;
    }
}