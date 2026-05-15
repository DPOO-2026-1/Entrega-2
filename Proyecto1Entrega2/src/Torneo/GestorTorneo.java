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

        int reservados = calcularCuposReservados(cupoTotal);
        String id = "TA-" + System.currentTimeMillis();

        TorneoAmistoso torneo = new TorneoAmistoso(id, dia, new Date(), 120,
                EstadoTorneo.PROGRAMADO, cupoTotal, reservados, 0, 0, new Date(),
                "Amistoso " + juego.getNombre(), juego, new ArrayList<InscripcionTorneo>(), valorBono);

        catalogoTorneos.add(torneo);

        return torneo;
    }

    public TorneoCompetitivo crearTorneoCompetitivo(Administrador admin, Juego juego, DiaSemana dia,
            String hora, int cupoTotal, double tarifa) {

        validarAdmin(admin);
        validarCupoVsCopiasPrestamo(juego, cupoTotal);

        int reservados = calcularCuposReservados(cupoTotal);
        String id = "TC-" + System.currentTimeMillis();

        TorneoCompetitivo torneo = new TorneoCompetitivo(id, dia, new Date(), 120,
                EstadoTorneo.PROGRAMADO, cupoTotal, reservados, 0, 0, new Date(),
                "Competitivo " + juego.getNombre(), juego, new ArrayList<InscripcionTorneo>(), tarifa);

        catalogoTorneos.add(torneo);

        return torneo;
    }

    public List<Torneo> getTorneos(DiaSemana dia) {
        List<Torneo> resultado = new ArrayList<Torneo>();

        for (Torneo t : catalogoTorneos) {
            if (DiaSemana.normalizar(t.getDia()).equals(DiaSemana.normalizar(dia))) {
                resultado.add(t);
            }
        }

        return resultado;
    }

    public void inscribir(Usuario usuario, String torneoId, int cantidadCupos) throws Exception {
        Torneo torneo = buscarTorneo(torneoId);

        if (torneo == null) {
            throw new IllegalArgumentException("No existe el torneo " + torneoId);
        }

        if (!torneo.puedeInscribirse(usuario, cantidadCupos)) {
            throw new IllegalStateException("El usuario no puede inscribirse a este torneo.");
        }

        int reservados = 0;
        int regulares = cantidadCupos;

        if (torneo.esFanatico(usuario)) {
            reservados = Math.min(cantidadCupos, torneo.cuposDisponiblesReservados());
            regulares = cantidadCupos - reservados;
        }

        double monto = 0.0;
        boolean elegible = true;

        if (torneo instanceof TorneoCompetitivo) {
            TorneoCompetitivo competitivo = (TorneoCompetitivo) torneo;

            monto = competitivo.getTarifaEntrada() * cantidadCupos;
            competitivo.registrarPago(usuario, monto);
            elegible = competitivo.esElegiblePremioMetalico(usuario);
        }

        List<Usuario> usuarios = new ArrayList<Usuario>();
        usuarios.add(usuario);

        InscripcionTorneo inscripcion = new InscripcionTorneo(
                "I-" + System.currentTimeMillis(),
                new Date(),
                usuarios,
                cantidadCupos,
                reservados,
                regulares,
                usuario instanceof Empleado,
                monto,
                true,
                elegible
        );

        torneo.inscribir(inscripcion);
    }

    public void desinscribir(Usuario usuario, String torneoId) {
        Torneo torneo = buscarTorneo(torneoId);

        if (torneo != null) {
            InscripcionTorneo inscripcion = torneo.buscarInscripcion(usuario);
            torneo.desinscribir(inscripcion);
        }
    }

    public ResultadoTorneo finalizarTorneo(Administrador admin, String torneoId, Usuario ganador) {
        validarAdmin(admin);

        Torneo torneo = buscarTorneo(torneoId);

        if (torneo == null) {
            throw new IllegalArgumentException("No existe el torneo " + torneoId);
        }

        torneo.setEstado(EstadoTorneo.FINALIZADO);

        BonoTorneoAmistoso bono = null;
        double premio = 0.0;

        if (torneo instanceof TorneoAmistoso) {
            bono = ((TorneoAmistoso) torneo).otorgarBono(ganador);
            bonos.add(bono);
        } else if (torneo instanceof TorneoCompetitivo) {
            TorneoCompetitivo competitivo = (TorneoCompetitivo) torneo;
            premio = competitivo.esElegiblePremioMetalico(ganador) ? competitivo.calcularPozo() : 0.0;
        }

        return new ResultadoTorneo(torneo, ganador, premio, bono);
    }

    public int calcularCuposReservados(int cupoTotal) {
        return (int) Math.ceil(cupoTotal * 0.20);
    }

    public boolean validarCupoVsCopiasPrestamo(Juego juego, int cupoTotal) {
        if (juego == null || juego.getCopiasPrestamo() == null || juego.getCopiasPrestamo().size() < cupoTotal) {
            throw new IllegalArgumentException("No hay suficientes copias de préstamo para el cupo del torneo.");
        }

        return true;
    }

    public boolean validarEmpleadoSinTurno(Empleado empleado, DiaSemana dia, String hora) {
        if (empleado == null) {
            return false;
        }

        for (DiaTurno turno : empleado.consultarDiasAsignados()) {
            if (turno != null
                    && turno.estaAsignado()
                    && DiaSemana.normalizar(turno.getDia()).equals(DiaSemana.normalizar(dia))) {
                return false;
            }
        }

        return true;
    }

    public Torneo buscarTorneo(String torneoId) {
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
}