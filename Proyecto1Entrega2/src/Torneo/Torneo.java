package Torneo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import Usuario.DiaSemana;
import Usuario.DiaTurno;
import Usuario.Empleado;
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
    private static final int REGLA_MAX_CUPOS_POR_USUARIO = 3;
    private String nombre;
    private Juego juegoTorneo;
    private List<InscripcionTorneo> inscripciones;

    public Torneo(String idTorneo, DiaSemana dia, Date fechaInicio, int duracionMin, EstadoTorneo estado,
            int cupoTotal, int cupoReservadoFanaticos, int cupoReservadoOcupado, int cupoOcupadoRegular,
            Date fechaCreacion, String nombre, Juego juegoTorneo, List<InscripcionTorneo> inscripciones) {

        this.idTorneo = idTorneo;
        this.dia = dia;
        this.fechaInicio = fechaInicio != null ? fechaInicio : new Date();
        this.duracionMin = duracionMin;
        this.estado = estado != null ? estado : EstadoTorneo.PROGRAMADO;
        this.cupoTotal = cupoTotal;
        this.cupoReservadoFanaticos = cupoReservadoFanaticos;
        this.cupoReservadoOcupado = cupoReservadoOcupado;
        this.cupoOcupadoRegular = cupoOcupadoRegular;
        this.fechaCreacion = fechaCreacion != null ? fechaCreacion : new Date();
        this.nombre = nombre;
        this.juegoTorneo = juegoTorneo;
        this.inscripciones = inscripciones != null ? inscripciones : new ArrayList<InscripcionTorneo>();
    }

    public int cuposDisponiblesReservados() {
        return Math.max(0, cupoReservadoFanaticos - cupoReservadoOcupado);
    }

    public int cuposDisponiblesRegulares() {
        return Math.max(0, cupoTotal - cupoReservadoFanaticos - cupoOcupadoRegular);
    }

    public int cuposDisponiblesTotales() {
        return cuposDisponiblesReservados() + cuposDisponiblesRegulares();
    }

    public boolean esFanatico(Usuario u) {
        return u != null && juegoTorneo != null && u.getJuegosFavoritos().contains(juegoTorneo);
    }

    public boolean validarCupoMaximoPorUsuario(int cantidad) {
        return cantidad > 0 && cantidad <= REGLA_MAX_CUPOS_POR_USUARIO;
    }

    public boolean puedeInscribirse(Usuario u, int cantidad) {
        if (u == null || !validarCupoMaximoPorUsuario(cantidad)) {
            return false;
        }

        if (buscarInscripcion(u) != null) {
            return false;
        }

        if (u instanceof Empleado && empleadoTieneTurnoEseDia((Empleado) u)) {
            return false;
        }

        return cuposDisponiblesTotales() >= cantidad;
    }

    public void puedeInscribirse(InscripcionTorneo inscripcion) throws Exception {
        if (inscripcion == null) {
            throw new IllegalArgumentException("La inscripción no puede ser null.");
        }

        if (!validarCupoMaximoPorUsuario(inscripcion.getCantidadCupos())) {
            throw new IllegalArgumentException("La inscripción supera el máximo de 3 cupos por usuario.");
        }

        if (inscripcion.getCuposReservados() > cuposDisponiblesReservados()) {
            throw new IllegalStateException("No hay suficientes cupos reservados disponibles.");
        }

        if (inscripcion.getCuposRegulares() > cuposDisponiblesRegulares()) {
            throw new IllegalStateException("No hay suficientes cupos regulares disponibles.");
        }

        for (Usuario u : inscripcion.getUsuarios()) {
            if (buscarInscripcion(u) != null) {
                throw new IllegalStateException("El usuario " + u.getNombre() + " ya tiene una inscripción activa.");
            }

            if (u instanceof Empleado && empleadoTieneTurnoEseDia((Empleado) u)) {
                throw new IllegalStateException("El empleado " + u.getNombre() + " tiene turno el día del torneo.");
            }
        }
    }

    public void inscribir(InscripcionTorneo inscripcion) throws Exception {
        puedeInscribirse(inscripcion);

        inscripciones.add(inscripcion);
        cupoReservadoOcupado += inscripcion.getCuposReservados();
        cupoOcupadoRegular += inscripcion.getCuposRegulares();
    }

    public void desinscribir(Usuario u, int cantidad) {
        InscripcionTorneo inscripcion = buscarInscripcion(u);

        if (inscripcion != null) {
            desinscribir(inscripcion);
        }
    }

    public void desinscribir(InscripcionTorneo inscripcion) {
        if (inscripcion != null && inscripciones.remove(inscripcion)) {
            cupoReservadoOcupado = Math.max(0, cupoReservadoOcupado - inscripcion.getCuposReservados());
            cupoOcupadoRegular = Math.max(0, cupoOcupadoRegular - inscripcion.getCuposRegulares());
        }
    }

    public InscripcionTorneo buscarInscripcion(Usuario usuario) {
        if (usuario == null) {
            return null;
        }

        for (InscripcionTorneo inscripcion : inscripciones) {
            if (inscripcion.getUsuarios().contains(usuario)) {
                return inscripcion;
            }

            for (Usuario u : inscripcion.getUsuarios()) {
                if (u.getLogin().equals(usuario.getLogin())) {
                    return inscripcion;
                }
            }
        }

        return null;
    }

    private boolean empleadoTieneTurnoEseDia(Empleado empleado) {
        if (empleado == null || empleado.consultarDiasAsignados() == null) {
            return false;
        }

        for (DiaTurno turno : empleado.consultarDiasAsignados()) {
            if (turno != null
                    && turno.getDia() != null
                    && DiaSemana.normalizar(turno.getDia()).equals(DiaSemana.normalizar(this.dia))
                    && turno.estaAsignado()) {
                return true;
            }
        }

        return false;
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
        return REGLA_MAX_CUPOS_POR_USUARIO;
    }
}