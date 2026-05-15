package Torneo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import Usuario.Administrador;
import Usuario.DiaSemana;
import Usuario.DiaTurno;
import Usuario.Empleado;
import Usuario.Usuario;
import World.Juego;

public class GestorTorneo {
    private List<Torneo> catalogoTorneos;
    private List<BonoTorneoAmistoso> bonos;

    public GestorTorneo() {
        this.catalogoTorneos = new ArrayList<Torneo>();
        this.bonos = new ArrayList<BonoTorneoAmistoso>();
    }

    public TorneoAmistoso crearTorneoAmistoso(Administrador admin, Juego juego, DiaSemana dia,
            String hora, int cupoTotal, double valorBono) {

        validarAdmin(admin);
        validarCupoVsCopiasPrestamo(juego, cupoTotal);

        int cuposReservados = calcularCuposReservados(cupoTotal);

        TorneoAmistoso torneo = new TorneoAmistoso(
                "TA-" + System.currentTimeMillis(),
                dia,
                new Date(),
                120,
                EstadoTorneo.PROGRAMADO,
                cupoTotal,
                cuposReservados,
                0,
                0,
                new Date(),
                "Torneo amistoso de " + juego.getNombre(),
                juego,
                new ArrayList<InscripcionTorneo>(),
                valorBono
        );

        catalogoTorneos.add(torneo);
        return torneo;
    }

    public TorneoCompetitivo crearTorneoCompetitivo(Administrador admin, Juego juego, DiaSemana dia,
            String hora, int cupoTotal, double tarifa) {

        validarAdmin(admin);
        validarCupoVsCopiasPrestamo(juego, cupoTotal);

        int cuposReservados = calcularCuposReservados(cupoTotal);

        TorneoCompetitivo torneo = new TorneoCompetitivo(
                "TC-" + System.currentTimeMillis(),
                dia,
                new Date(),
                120,
                EstadoTorneo.PROGRAMADO,
                cupoTotal,
                cuposReservados,
                0,
                0,
                new Date(),
                "Torneo competitivo de " + juego.getNombre(),
                juego,
                new ArrayList<InscripcionTorneo>(),
                tarifa
        );

        catalogoTorneos.add(torneo);
        return torneo;
    }

    public List<Torneo> getTorneos(DiaSemana dia) {
        List<Torneo> torneosDelDia = new ArrayList<Torneo>();

        for (Torneo torneo : catalogoTorneos) {
            if (torneo.getDia() != null && torneo.getDia().mismoDia(dia)) {
                torneosDelDia.add(torneo);
            }
        }

        return torneosDelDia;
    }

    // ===== CAMBIO HECHO =====
    // Esta es ahora la ÚNICA zona donde vive la lógica completa de inscripción.
    // Se valida:
    // 1. Que exista el torneo.
    // 2. Que la cantidad sea entre 1 y 3.
    // 3. Que el usuario no esté ya inscrito en ese torneo.
    // 4. Que el empleado no tenga turno ese día.
    // 5. Que existan cupos suficientes.
    // 6. Que los cupos reservados solo se usen si el usuario es fanático.
    // 7. Que en torneo competitivo se registre pago.
    // 8. Que se cree y agregue la InscripcionTorneo.
    // 9. Que se actualicen los cupos ocupados del torneo.
    // ===== FIN CAMBIO =====
    public void inscribir(Usuario usuario, String torneoId, int cantidadCupos) {
        Torneo torneo = buscarTorneo(torneoId);

        if (torneo == null) {
            throw new IllegalArgumentException("No existe un torneo con id: " + torneoId);
        }

        if (usuario == null) {
            throw new IllegalArgumentException("El usuario no puede ser null.");
        }

        if (!torneo.validarCupoMaximoPorUsuario(cantidadCupos)) {
            throw new IllegalArgumentException("La cantidad de cupos debe estar entre 1 y 3.");
        }

        if (buscarInscripcion(torneo, usuario) != null) {
            throw new IllegalStateException("El usuario ya está inscrito en este torneo.");
        }

        if (usuario instanceof Empleado) {
            Empleado empleado = (Empleado) usuario;

            if (!validarEmpleadoSinTurno(empleado, torneo.getDia(), "")) {
                throw new IllegalStateException("El empleado tiene turno asignado el día del torneo.");
            }
        }

        int cuposReservadosUsados = 0;
        int cuposRegularesUsados = 0;

        if (torneo.esFanatico(usuario)) {
            cuposReservadosUsados = Math.min(cantidadCupos, torneo.cuposDisponiblesReservados());
            cuposRegularesUsados = cantidadCupos - cuposReservadosUsados;
        } else {
            cuposRegularesUsados = cantidadCupos;
        }

        if (cuposRegularesUsados > torneo.cuposDisponiblesRegulares()) {
            throw new IllegalStateException("No hay suficientes cupos regulares disponibles.");
        }

        if (cuposReservadosUsados > torneo.cuposDisponiblesReservados()) {
            throw new IllegalStateException("No hay suficientes cupos reservados disponibles.");
        }

        double montoPagado = 0.0;
        boolean pagoConfirmado = true;
        boolean elegiblePremioMetalico = true;

        if (torneo instanceof TorneoCompetitivo) {
            TorneoCompetitivo competitivo = (TorneoCompetitivo) torneo;

            montoPagado = competitivo.getTarifaEntrada() * cantidadCupos;
            competitivo.registrarPago(usuario, montoPagado);

            elegiblePremioMetalico = competitivo.esElegiblePremioMetalico(usuario);
        }

        List<Usuario> usuariosInscritos = new ArrayList<Usuario>();
        usuariosInscritos.add(usuario);

        InscripcionTorneo inscripcion = new InscripcionTorneo(
                "INS-" + System.currentTimeMillis(),
                new Date(),
                usuariosInscritos,
                cantidadCupos,
                cuposReservadosUsados,
                cuposRegularesUsados,
                usuario instanceof Empleado,
                montoPagado,
                pagoConfirmado,
                elegiblePremioMetalico
        );

        torneo.getInscripciones().add(inscripcion);

        torneo.setCupoOcupadoReservado(
                torneo.getCupoOcupadoReservado() + cuposReservadosUsados
        );

        torneo.setCupoOcupadoRegular(
                torneo.getCupoOcupadoRegular() + cuposRegularesUsados
        );
    }

    // ===== CAMBIO HECHO =====
    // La lógica de desinscripción también queda solamente en GestorTorneo.
    // Busca la inscripción, la elimina y devuelve los cupos al torneo.
    // ===== FIN CAMBIO =====
    public void desinscribir(Usuario usuario, String torneoId) {
        Torneo torneo = buscarTorneo(torneoId);

        if (torneo == null) {
            throw new IllegalArgumentException("No existe un torneo con id: " + torneoId);
        }

        InscripcionTorneo inscripcion = buscarInscripcion(torneo, usuario);

        if (inscripcion == null) {
            throw new IllegalStateException("El usuario no está inscrito en este torneo.");
        }

        torneo.getInscripciones().remove(inscripcion);

        torneo.setCupoOcupadoReservado(
                Math.max(0, torneo.getCupoOcupadoReservado() - inscripcion.getCuposReservados())
        );

        torneo.setCupoOcupadoRegular(
                Math.max(0, torneo.getCupoOcupadoRegular() - inscripcion.getCuposRegulares())
        );
    }

    public ResultadoTorneo finalizarTorneo(Administrador admin, String torneoId, Usuario ganador) {
        validarAdmin(admin);

        Torneo torneo = buscarTorneo(torneoId);

        if (torneo == null) {
            throw new IllegalArgumentException("No existe un torneo con id: " + torneoId);
        }

        if (ganador == null) {
            throw new IllegalArgumentException("El ganador no puede ser null.");
        }

        torneo.setEstado(EstadoTorneo.FINALIZADO);

        BonoTorneoAmistoso bono = null;
        double premioMetalico = 0.0;

        if (torneo instanceof TorneoAmistoso) {
            bono = ((TorneoAmistoso) torneo).otorgarBono(ganador);
            bonos.add(bono);
        }

        if (torneo instanceof TorneoCompetitivo) {
            TorneoCompetitivo competitivo = (TorneoCompetitivo) torneo;

            if (competitivo.esElegiblePremioMetalico(ganador)) {
                premioMetalico = competitivo.calcularPozo();
            }
        }

        return new ResultadoTorneo(torneo, ganador, premioMetalico, bono);
    }

    public int calcularCuposReservados(int cupoTotal) {
        return (int) Math.ceil(cupoTotal * 0.20);
    }

    public boolean validarCupoVsCopiasPrestamo(Juego juego, int cupoTotal) {
        if (juego == null) {
            throw new IllegalArgumentException("El juego no puede ser null.");
        }

        if (juego.getCopiasPrestamo() == null) {
            throw new IllegalArgumentException("El juego no tiene lista de copias para préstamo.");
        }

        if (juego.getCopiasPrestamo().size() < cupoTotal) {
            throw new IllegalArgumentException("No hay suficientes copias de préstamo para el cupo del torneo.");
        }

        return true;
    }

    public boolean validarEmpleadoSinTurno(Empleado empleado, DiaSemana dia, String hora) {
        if (empleado == null) {
            return false;
        }

        if (empleado.consultarDiasAsignados() == null) {
            return true;
        }

        for (DiaTurno turno : empleado.consultarDiasAsignados()) {
            if (turno != null
                    && turno.getDia() != null
                    && turno.estaAsignado()
                    && turno.getDia().mismoDia(dia)) {
                return false;
            }
        }

        return true;
    }

    // ===== CAMBIO HECHO =====
    // Este método queda privado porque es una operación interna del GestorTorneo
    // Antes esta búsqueda estaba mal ubicada dentro de Torneo.
    // ===== FIN CAMBIO =====
    private InscripcionTorneo buscarInscripcion(Torneo torneo, Usuario usuario) {
        if (torneo == null || usuario == null) {
            return null;
        }

        for (InscripcionTorneo inscripcion : torneo.getInscripciones()) {
            for (Usuario inscrito : inscripcion.getUsuarios()) {
                if (inscrito.getLogin().equals(usuario.getLogin())) {
                    return inscripcion;
                }
            }
        }

        return null;
    }

    public Torneo buscarTorneo(String torneoId) {
        if (torneoId == null) {
            return null;
        }

        for (Torneo torneo : catalogoTorneos) {
            if (torneo.getIdTorneo().equals(torneoId)) {
                return torneo;
            }
        }

        return null;
    }

    public List<Torneo> getCatalogoTorneos() {
        return catalogoTorneos;
    }

    public List<BonoTorneoAmistoso> getBonos() {
        return bonos;
    }

    private void validarAdmin(Administrador admin) {
        if (admin == null) {
            throw new IllegalArgumentException("Solo un administrador puede realizar esta operación.");
        }
    }
    
    public void setCatalogoTorneos(List<Torneo> catalogoTorneos) {
        if (catalogoTorneos == null) {
            this.catalogoTorneos = new ArrayList<Torneo>();
        } else {
            this.catalogoTorneos = catalogoTorneos;
        }
    }
    
    // ===== CAMBIO HECHO =====
    // Permite restaurar los bonos desde persistencia.
    // ===== FIN CAMBIO =====
    public void setBonos(List<BonoTorneoAmistoso> bonos) {
     if (bonos == null) {
         this.bonos = new ArrayList<BonoTorneoAmistoso>();
     } else {
         this.bonos = bonos;
     }
 }
}