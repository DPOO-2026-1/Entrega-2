package Usuario;

import java.util.Date;
import java.util.List;

public class Administrador extends Usuario {

    public Administrador(String login, String password, String nombre) {
        super(login, password, nombre);
    }

    public void aprobarSolicitudTurno(SolicitudTurno s) {
        s.setEstado("Aprobada");
        
        if (!s.isEsIntercambio()) {
            DiaTurno nuevoTurno = new DiaTurno(s.getDia(), true);
            s.getSolicitadoPor().getDiasAsignados().add(nuevoTurno);
        } else {
            s.getSolicitadoPor().getDiasAsignados().add(new DiaTurno(s.getDia(), true));
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
    
    public String generarInformeVentas(List<Venta> inventarioVentas, Date ini, Date fin, String granularidad) {
        double totalSubtotal = 0.0;
        double totalImpuestos = 0.0;
        double totalPropinas = 0.0;
        double totalGeneral = 0.0;

        for (Venta v : inventarioVentas) {
            Date fechaVenta = v.getFecha();
            if (!fechaVenta.before(ini) && !fechaVenta.after(fin)) {
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