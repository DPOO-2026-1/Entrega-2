package World;

import Usuario.Usuario;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;

public class Prestamo {
    private String estado;
    private Date fechaHoraInicio;
    private Date fechaHoraFin;

    private Usuario solicitadoPor;         
    private Mesa mesaAsociada;             
    private List<CopiaPrestamo> copias;  

    // ===== CAMBIO HECHO =====
    // Agregué constructor compatible con usos donde se presta una sola copia.
    // Este constructor lo usa Mesero.alquilarJuego y puede servir para Cliente.solicitarPrestamo.
    // ===== FIN CAMBIO =====
    public Prestamo(Date fechaInicio, CopiaPrestamo copia, Mesa mesaAsociada, Usuario solicitadoPor) {
        this(solicitadoPor, mesaAsociada, new ArrayList<CopiaPrestamo>());

        if (copia != null) {
            this.copias.add(copia);

            if (copia.estaDisponible()) {
                copia.prestar();
            }
        }

        if (fechaInicio != null) {
            this.fechaHoraInicio = fechaInicio;
        } else {
            this.fechaHoraInicio = new Date();
        }
    }
  

    public Prestamo(Usuario solicitadoPor, Mesa mesaAsociada, List<CopiaPrestamo> copias) {
        this.estado = "Activo";
        this.fechaHoraInicio = new Date();
        this.fechaHoraFin = null;
        this.solicitadoPor = solicitadoPor;
        this.mesaAsociada = mesaAsociada;
        this.copias = (copias != null) ? copias : new ArrayList<>();
        
        // Al crear el préstamo, prestamos físicamente las copias
        for(CopiaPrestamo copia : this.copias) {
            copia.prestar();
        }
    }

    public void finalizar() {
        this.estado = "Finalizado";
        this.fechaHoraFin = new Date();
        
        // Devolvemos las copias
        for(CopiaPrestamo copia : copias) {
            copia.devolver();
        }
    }

    public boolean estaVencido() {
        if (this.estado.equals("Activo")) {
            long diferenciaMs = new Date().getTime() - fechaHoraInicio.getTime();
            return diferenciaMs >= (24 * 60 * 60 * 1000); 
        }
        return false;
    }

    public double getDuracionHoras() {
        Date fin = (this.fechaHoraFin != null) ? this.fechaHoraFin : new Date();
        long diferenciaMs = fin.getTime() - fechaHoraInicio.getTime();
        return diferenciaMs / (1000.0 * 60 * 60);
    }

     // ===== CAMBIO HECHO =====
    // Getters/setters necesarios para pruebas, persistencia e historial.
    // ===== FIN CAMBIO =====
    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Date getFechaHoraInicio() {
        return fechaHoraInicio;
    }

    public Date getFechaHoraFin() {
        return fechaHoraFin;
    }

    public Usuario getSolicitadoPor() {
        return solicitadoPor;
    }

    public Mesa getMesaAsociada() {
        return mesaAsociada;
    }

    public List<CopiaPrestamo> getCopias() {
        return copias;
    }

    // ===== CAMBIO HECHO =====
    // Getter de compatibilidad para código que espera un solo juego/copia.
    // ===== FIN CAMBIO =====
    public CopiaPrestamo getCopia() {
        if (copias != null && !copias.isEmpty()) {
            return copias.get(0);
        }

        return null;
    }
}
