package Torneo;

import java.util.Date;
import java.util.List;

import Usuario.DiaSemana;
import Usuario.Usuario;
import World.Juego;

public abstract class Torneo {
	private String idTorneo;
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
	private List<InscripcionTorneo> inscripciones;
	
	//Builder
	public Torneo(String idTorneo, DiaSemana dia, Date fechaInicio, int duracionMin, EstadoTorneo estado, int cupoTotal,
			int cupoReservadoFanaticos, int cupoReservadoOcupado, int cupoOcupadoRegular, Date fechaCreacion,
			String nombre, Juego juegoTorneo, List<InscripcionTorneo> inscripciones) {
		super();
		this.idTorneo = idTorneo;
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

	public List<InscripcionTorneo> getInscripciones() {
		return inscripciones;
	}

	public void setInscripciones(List<InscripcionTorneo> inscripciones) {
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
		return cantidad > 0 && cantidad <= REGLA_MAX_CUPOS__POR_USUARIO;
	}
	
	//Excepciones para inscribir
	
	public class CupoInsuficienteException extends Exception {
	    public CupoInsuficienteException(String mensaje) {
	        super(mensaje);
	    }
	}

	public class UsuarioYaInscritoException extends Exception {
	    public UsuarioYaInscritoException(String mensaje) {
	        super(mensaje);
	    }
	}
	
	public void puedeInscribirse(InscripcionTorneo inscripcion) throws Exception {
		if (cuposDisponiblesReservados() < inscripcion.getCuposReservados() && (cuposDisponiblesRegulares() < inscripcion.getCuposReservados() || cuposDisponiblesRegulares() < inscripcion.getCuposRegulares())) {
			throw new CupoInsuficienteException("No hay suficientes cupos disponibles");
		}
		
		for (Usuario u : inscripcion.getUsuarios()) {
	        for (InscripcionTorneo existente : inscripciones) {
	            if (existente.getUsuarios().contains(u)) {
	                throw new UsuarioYaInscritoException("El usuario " + u.getNombre() + " tiene una inscripción activa");
	            }
	        }
	    }
	}
	
	public void inscribir(InscripcionTorneo inscripcion) throws Exception {
		try {
			puedeInscribirse(inscripcion);
			inscripciones.add(inscripcion);
			cupoReservadoOcupado += inscripcion.getCuposReservados();
	        cupoOcupadoRegular += inscripcion.getCuposRegulares();
			System.out.println("Éxito");
		}
		catch (CupoInsuficienteException | UsuarioYaInscritoException e){
			System.out.println("Error: " + e.getMessage());
		}
	}
	
	public void desinscribir(Usuario u, int cantidad) {	
	}
	
	public void desinscribir(InscripcionTorneo inscripcion) {
		if (inscripciones.remove(inscripcion)) {
			cupoReservadoOcupado -= inscripcion.getCuposReservados();
	        cupoOcupadoRegular -= inscripcion.getCuposRegulares();
	        System.out.println("Inscripción eliminada y cupos liberados");
		}
		else {
			System.out.println("La inscripción no existe en la lista");
		}
	}
}
