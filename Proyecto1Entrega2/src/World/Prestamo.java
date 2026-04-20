package World;
import java.time.*;

public class Prestamo {
	private String estado;
	private Mesa mesaAsociada;
	private LocalDateTime fechaHoraInicio;
	private LocalDateTime fechaHoraFin;
	
	public void finalizar() {
		this.fechaHoraFin = LocalDateTime.now();
        this.estado = "Finalizado";
	}
	
	public boolean estaVencido() {
		Duration duracion = Duration.between(fechaHoraInicio, LocalDateTime.now());
        return duracion.toHours() >= 4;
	}
	
	public double getDuracionHoras() {
		if (fechaHoraInicio != null && fechaHoraFin != null) {
            Duration duracion = Duration.between(fechaHoraInicio, fechaHoraFin);
            return duracion.toMinutes() / 60.0;
		}
		return 0.0;
	}

	public String getEstado() {
        return estado;
    }

    public LocalDateTime getFechaHoraInicio() {
        return fechaHoraInicio;
    }

    public LocalDateTime getFechaHoraFin() {
        return fechaHoraFin;
    }
}
