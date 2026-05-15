package Torneo;

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
			int cuposReservados, int cuposRegulares, boolean esEmpleado, double montoPagado, boolean pagoConfirmado,
			boolean elegiblePrecioMetalico) {
		super();
		this.idInscripcion = idInscripcion;
		this.fecha = fecha;
		this.usuarios = usuarios;
		this.cantidadCupos = cantidadCupos;
		this.cuposReservados = cuposReservados;
		this.cuposRegulares = cuposRegulares;
		this.esEmpleado = esEmpleado;
		this.montoPagado = montoPagado;
		this.pagoConfirmado = pagoConfirmado;
		this.elegiblePrecioMetalico = elegiblePrecioMetalico;
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

	public int getCantidadCupos() {
		return cantidadCupos;
	}

	public void setCantidadCupos(int cantidadCupos) {
		this.cantidadCupos = cantidadCupos;
	}

	public int getCuposReservados() {
		return cuposReservados;
	}

	public void setCuposReservados(int cuposReservadosUsados) {
		this.cuposReservados = cuposReservadosUsados;
	}

	public int getCuposRegulares() {
		return cuposRegulares;
	}

	public void setCuposRegulares(int cuposRegularesUsados) {
		this.cuposRegulares = cuposRegularesUsados;
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

	public void setElegiblePrecioMetalico(boolean elegiblePrecioMetalico) {
		this.elegiblePrecioMetalico = elegiblePrecioMetalico;
	}

	public List<Usuario> getUsuarios() {
		return usuarios;
	}

	public void setUsuarios(List<Usuario> usuarios) {
		this.usuarios = usuarios;
	}
}
