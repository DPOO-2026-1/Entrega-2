package Usuario;

import java.time.LocalDateTime;
import java.time.chrono.ChronoLocalDateTime;
import java.util.Date;
import java.util.List;

import World.Cafeteria;
import World.CopiaPrestamo;
import World.Juego;
import World.Prestamo;
import ModuloVenta.CopiaVenta;
import ModuloVenta.GestorVentas;
import ModuloVenta.Venta;
import ModuloVenta.ProductoComestible;

public class Administrador extends Usuario {

    public Administrador(String login, String password, String nombre) {
        super(login, password, nombre);
    }

    public void aprobarSolicitudTurno(SolicitudTurno s) {
    	Cafeteria cafe = Cafeteria.getInstance();
        Empleado solicitante = s.getSolicitadoPor();
        DiaSemana dia = s.getDia();
        
        if (!s.isEsIntercambio()) {
        	if (!puedeAusentarse(solicitante, dia, cafe)) {
        		throw new IllegalStateException(
                        "No se puede aprobar: el café no cumpliría el mínimo de 1 cocinero y 2 meseros ese día.");
        	}
        	solicitante.consultarDiasAsignados().removeIf(dt -> dt.getDia().mismoDia(dia));
            
        } else {
        	solicitante.consultarDiasAsignados().add(new DiaTurno(dia, true));
        }
        s.setEstado("Aprobada");
    }
    
    public void rechazarSolicitudTurno(SolicitudTurno s) {
        s.setEstado("Rechazada");
    }
    
    public void asignarTurno(Empleado emp, DiaSemana dia) {
    	for (DiaTurno dt : emp.consultarDiasAsignados()) {
            if (dt.getDia().mismoDia(dia)) {
                throw new IllegalStateException("El empleado ya tiene asignado ese día.");
            }
        }
    	emp.consultarDiasAsignados().add(new DiaTurno(dia, true));
    }
    
    public void quitarTurno(Empleado emp, DiaSemana dia, Cafeteria cafe) {
    	if (!puedeAusentarse(emp, dia, cafe)) {
            throw new IllegalStateException(
                "No se puede quitar el turno: el café no cumpliría el mínimo de empleados ese día.");
        }
        emp.consultarDiasAsignados().removeIf(dt -> dt.getDia().mismoDia(dia));
    }
    
    public void comprarJuegos(Juego j, int cant, String tipo) {
        for (int i = 0; i < cant; i++) {
        	// ID único combinando tiempo + índice para evitar colisiones en bucles rápidos
        	String idBase = j.getNombre().replaceAll("\\s+", "") + "-" + System.currentTimeMillis() + "-" + i;
            
        	if (tipo.equalsIgnoreCase("prestamo")) {
                CopiaPrestamo nuevaCopiaP = new CopiaPrestamo("P-" + idBase, "Nuevo", true, 0);
                nuevaCopiaP.setJuegoAsociado(j);
                j.agregarCopiaPrestamo(nuevaCopiaP);
            } else if (tipo.equalsIgnoreCase("venta")) {
                CopiaVenta nuevaCopiaV = new CopiaVenta("V-" + idBase, 50000.0); 
                j.agregarCopiaVenta(nuevaCopiaV);
            } else {
            	throw new IllegalArgumentException("Tipo inválido. Use 'prestamo' o 'venta'.");
            }
        }
    }
    
    public void darJuegoPorRobado(CopiaPrestamo c) {
        c.marcarDesaparecida();
    }
    
    public void repararJuego(CopiaPrestamo c) {
        c.reparar();
    }
    
    public void moverJuegoAVenta(CopiaPrestamo c, Juego j) {
    	if (c.getJuegoAsociado() == null || !c.getJuegoAsociado().equals(j)) {
            throw new IllegalArgumentException("La copia no pertenece al juego indicado.");
    	}
    	if (!c.estaDisponible()) {
            throw new IllegalStateException("No se puede mover una copia que está actualmente prestada.");
        }
    	
    	j.getCopiasPrestamo().remove(c);
    	
    	String nuevoId = "V-" + c.getIdUnico();
    	CopiaVenta nuevaVenta = new CopiaVenta(nuevoId, 45000.0);
        j.agregarCopiaVenta(nuevaVenta);
    }
    
    // COMENTARIO: Añadido método para mover copia de venta a préstamo (reparación de inventario).
    public void moverJuegoAPrestamo(CopiaVenta c, Juego j) {
        // Se asume que la copia de venta se elimina o se marca como no disponible, pero como CopiaVenta no tiene estado, solo se crea la de préstamo.
    	j.getCopiasParaVenta().remove(c);
    	String nuevoId = "P-MOV-" + System.currentTimeMillis();
    	CopiaPrestamo nuevaPrestamo = new CopiaPrestamo(nuevoId, "Nuevo", true, 0);
        nuevaPrestamo.setJuegoAsociado(j);
        j.agregarCopiaPrestamo(nuevaPrestamo);
    }
    
    public String generarInformeVentas(LocalDateTime ini, LocalDateTime fin, String granularidad) {
    	Cafeteria cafe = Cafeteria.getInstance();
        GestorVentas gestorVentas = cafe.getGestorVentas();
        List<Usuario> usuarios = cafe.getUsuarios();
        return gestorVentas.generarInformeVentas(ini, fin, granularidad, usuarios);
    }
    
    public void aprobarSugerenciaMenu(SugerenciaMenu s) {
        s.setEstado("Aprobada");
    }
    
    public void rechazarSugerenciaMenu(SugerenciaMenu s) {
        s.setEstado("Rechazada");
    }
    
    public void agregarProductoMenu(List<ProductoComestible> menuCafeteria, ProductoComestible p) {
        if (p == null) {
            throw new IllegalArgumentException("El producto no puede ser nulo.");
        }
        if (menuCafeteria.contains(p)) {
            throw new IllegalStateException("El producto ya está en el menú.");
        }
        menuCafeteria.add(p);
    }
    
    public Empleado registrarEmpleado(String login, String password, String nombre, String tipo, String codigoDescuento) {
    	Cafeteria cafe = Cafeteria.getInstance();
    	return cafe.getGestorUsuarios().registrarEmpleado(login, password, nombre, tipo, codigoDescuento);
    	}
    
    public String verHistorialJuego(Juego j) {
        StringBuilder sb = new StringBuilder();
        sb.append("=== HISTORIAL DE: ").append(j.getNombre()).append(" ===\n");
        
        List<CopiaPrestamo> copias = j.getCopiasPrestamo();
        if (copias == null || copias.isEmpty()) {
            sb.append("No hay copias de préstamo registradas.\n");
            return sb.toString();
        }
        
        List<Prestamo> historial = Cafeteria.getInstance().getHistorialPrestamos();
        for (CopiaPrestamo cp : copias) {
            sb.append("- Copia [").append(cp.getIdUnico()).append("]\n")
              .append("  Estado actual:       ").append(cp.getEstado()).append("\n")
              .append("  Disponible:          ").append(cp.estaDisponible() ? "Sí" : "No").append("\n")
              .append("  Total veces prestado: ").append(cp.getVecesPrestado()).append("\n");
            
            sb.append("  Préstamos registrados:\n");
            
            boolean tienePrestamos = false;
            for (Prestamo p : historial) {
                for (CopiaPrestamo copiaEnPrestamo : p.getCopias()) {
                    if (copiaEnPrestamo.getIdUnico().equals(cp.getIdUnico())) {
                        sb.append("    · Solicitado por: ")
                          .append(p.getSolicitadoPor().getNombre()).append("\n")
                          .append("      Inicio: ").append(p.getFechaHoraInicio()).append("\n")
                          .append("      Fin:    ").append(
                              p.getFechaHoraFin() != null ? p.getFechaHoraFin() : "En curso").append("\n")
                          .append("      Estado: ").append(p.getEstado()).append("\n");
                        tienePrestamos = true;
                    }
                }
            }
            
            if (!tienePrestamos) {
                sb.append("    (ninguno registrado)\n");
            }
            sb.append("\n");
        }

        return sb.toString();
    }
    
    private boolean puedeAusentarse(Empleado solicitante, DiaSemana dia, Cafeteria cafe) {
        int cocineros = 0;
        int meseros = 0;

        for (Usuario u : cafe.getUsuarios()) {
            if (u instanceof Empleado && !u.getLogin().equals(solicitante.getLogin())) {
                Empleado emp = (Empleado) u;
                for (DiaTurno dt : emp.consultarDiasAsignados()) {
                    if (dt.getDia().mismoDia(dia) && dt.estaAsignado()) {
                        if (emp instanceof Cocinero) cocineros++;
                        else if (emp instanceof Mesero) meseros++;
                        break;
                    }
                }
            }
        }
        return cocineros >= 1 && meseros >= 2;
    }
}