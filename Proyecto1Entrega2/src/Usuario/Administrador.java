package Usuario;

import java.time.LocalDateTime;
import java.time.chrono.ChronoLocalDateTime;
import java.util.Date;
import java.util.List;

import World.CopiaPrestamo;
import World.Juego;
import ModuloVenta.CopiaVenta;
import ModuloVenta.Venta;
import ModuloVenta.ProductoComestible;

public class Administrador extends Usuario {

    public Administrador(String login, String password, String nombre) {
        super(login, password, nombre);
    }

    public void aprobarSolicitudTurno(SolicitudTurno s) {
        s.setEstado("Aprobada");
        
        if (!s.isEsIntercambio()) {
            DiaTurno nuevoTurno = new DiaTurno(s.getDia(), true);
            s.getSolicitadoPor().consultarDiasAsignados().add(nuevoTurno);
        } else {
            s.getSolicitadoPor().consultarDiasAsignados().add(new DiaTurno(s.getDia(), true));
        }
    }
    
    public void rechazarSolicitudTurno(SolicitudTurno s) {
        s.setEstado("Rechazada");
    }
    
    public void comprarJuegos(Juego j, int cant, String tipo) {
        for (int i = 0; i < cant; i++) {
            if (tipo.equalsIgnoreCase("prestamo")) {
                // Se genera un ID único con System.currentTimeMillis para evitar duplicados
                CopiaPrestamo nuevaCopiaP = new CopiaPrestamo("P-" + System.currentTimeMillis() + "-" + i, "Nuevo", true, 0);
                j.agregarCopiaPrestamo(nuevaCopiaP);
            } else if (tipo.equalsIgnoreCase("venta")) {
                CopiaVenta nuevaCopiaV = new CopiaVenta("V-" + System.currentTimeMillis() + "-" + i, 50000.0); 
                j.agregarCopiaVenta(nuevaCopiaV);
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
        // Para moverlo, se saca de disponibilidad de préstamo y se crea la copia de venta
        c.setEstaDisponible(false); 
        c.setEstado("Movido a Venta");
        
        CopiaVenta nuevaVenta = new CopiaVenta("V-" + c.getIdUnico(), 45000.0); // Precio asumido
        j.agregarCopiaVenta(nuevaVenta);
    }
    
    // COMENTARIO: Añadido método para mover copia de venta a préstamo (reparación de inventario).
    public void moverJuegoAPrestamo(CopiaVenta c, Juego j) {
        // Se asume que la copia de venta se elimina o se marca como no disponible, pero como CopiaVenta no tiene estado, solo se crea la de préstamo.
        CopiaPrestamo nuevaPrestamo = new CopiaPrestamo("P-MOV-" + System.currentTimeMillis(), "Nuevo", true, 0);
        j.agregarCopiaPrestamo(nuevaPrestamo);
    }
    
    public String generarInformeVentas(List<Venta> inventarioVentas, ChronoLocalDateTime<?> ini, ChronoLocalDateTime<?> fin, String granularidad) {
        double totalSubtotal = 0.0;
        double totalImpuestos = 0.0;
        double totalPropinas = 0.0;
        double totalGeneral = 0.0;

        for (Venta v : inventarioVentas) {
            LocalDateTime fechaVenta = v.getFecha();
            if (!fechaVenta.isBefore(ini) && !fechaVenta.isAfter(fin)) {
                totalSubtotal += v.getSubtotal();
                totalImpuestos += v.getImpuestos();
                totalPropinas += v.getPropina();
                totalGeneral += v.getTotal();
            }
        }
        
        return "=== INFORME DE VENTAS (" + granularidad.toUpperCase() + ") ===\n" +
               "Periodo: " + ini.toString() + " a " + fin.toString() + "\n" +
               "Subtotal: $" + totalSubtotal + "\n" +
               "Impuestos: $" + totalImpuestos + "\n" +
               "Propinas: $" + totalPropinas + "\n" +
               "TOTAL GENERAL: $" + totalGeneral + "\n" +
               "===================================\n";
    }
    
    public void aprobarSugerenciaMenu(SugerenciaMenu s) {
        s.setEstado("Aprobada");
    }
    
    public void agregarProductoMenu(List<ProductoComestible> menuCafeteria, ProductoComestible p) {
        menuCafeteria.add(p);
    }
    
    public String verHistorialJuego(Juego j) {
        StringBuilder sb = new StringBuilder();
        sb.append("=== HISTORIAL DE: ").append(j.getNombre()).append(" ===\n");
        
        List<CopiaPrestamo> copias = j.getCopiasPrestamo();
        if (copias == null || copias.isEmpty()) {
            sb.append("No hay copias de préstamo registradas.\n");
            return sb.toString();
        }
        
        for (CopiaPrestamo cp : copias) {
            sb.append("- Copia [").append(cp.getIdUnico()).append("]\n")
              .append("  Estado actual: ").append(cp.getEstado()).append("\n")
              .append("  Disponible: ").append(cp.estaDisponible() ? "Sí" : "No").append("\n")
              .append("  Total de veces prestado: ").append(cp.getVecesPrestado()).append("\n\n");
        }
        
        return sb.toString();
    }
}