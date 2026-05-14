package Torneo;

import java.util.Date;

import Usuario.DiaSemana;
import Usuario.Usuario;
import World.Juego;

public abstract class Torneo {
	private String id_torneo;
	private DiaSemana dia;
	private Date fechaInicio;
	private int duracionMin;
	private EstadoTorneo estado;
	private int cupoTotal;
	private int cupoReservadoFanaticos;
	private int cupoReservadoOcupado;
	private int cupoOcupadoRegular;
	private Date fechaCreacion;
	private static final int REGLA_MAX_CUPOS__POR_USUARIO = 3;
	private String nombre;
	private Juego juegoTorneo;
	private InscripcionTorneo inscripciones;
	
	//Builder
	public Torneo(String id_torneo, DiaSemana dia, Date fechaInicio, int duracionMin, EstadoTorneo estado,
			int cupoTotal, int cupoReservadoFanaticos, int cupoReservadoOcupado, int cupoOcupadoRegular,
			Date fechaCreacion, String nombre, Juego juegoTorneo, InscripcionTorneo inscripciones) {
		super();
		this.id_torneo = id_torneo;
		this.dia = dia;
		this.fechaInicio = fechaInicio;
		this.duracionMin = duracionMin;
		this.estado = estado;
		this.cupoTotal = cupoTotal;
		this.cupoReservadoFanaticos = cupoReservadoFanaticos;
		this.cupoReservadoOcupado = cupoReservadoOcupado;
		this.cupoOcupadoRegular = cupoOcupadoRegular;
		this.fechaCreacion = fechaCreacion;
		this.nombre = nombre;
		this.juegoTorneo = juegoTorneo;
		this.inscripciones = inscripciones;
	}
	
	//Getters y Setters

	public String getId_torneo() {
		return id_torneo;
	}

	public void setId_torneo(String id_torneo) {
		this.id_torneo = id_torneo;
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

	public int getCupoReservadoOcupado() {
		return cupoReservadoOcupado;
	}

	public void setCupoReservadoOcupado(int cupoReservadoOcupado) {
		this.cupoReservadoOcupado = cupoReservadoOcupado;
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

	public InscripcionTorneo getInscripciones() {
		return inscripciones;
	}

	public void setInscripciones(InscripcionTorneo inscripciones) {
		this.inscripciones = inscripciones;
	}

	public static int getReglaMaxCuposPorUsuario() {
		return REGLA_MAX_CUPOS__POR_USUARIO;
	}
	
	//RF
	
	public int cuposDisponiblesReservados() {
		return cupoReservadoFanaticos - cupoReservadoOcupado;
	}
	
	public int cuposDisponiblesRegulares() {
		return cupoTotal - cupoReservadoFanaticos - cupoOcupadoRegular;
	}
	
	public boolean esFanatico(Usuario u) {
		return u.getJuegosFavoritos().contains(juegoTorneo);
	}
	
	public boolean validarCupoMaximoPorUsuario(int cantidad) {
		
	}
	
	public boolean puedeInscribirse(Usuario u, int cantidad) {
		
	}
	
	public void inscribir(Usuario u, int cantidad) {
		if (esFanatico(u) && cuposDisponiblesReservados() >= cantidad) {
			cupoReservadoOcupado += cantidad;
			inscripciones.add()
		}
	}
	
}
