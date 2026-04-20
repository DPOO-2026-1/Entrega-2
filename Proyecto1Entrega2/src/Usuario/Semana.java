package Proyecto1Entrega2.src.World.Usuario;
import java.util.Map;

public class Semana {
	private Map<DiaSemana, DiaTurno> dias;
	
	public Semana(Map<DiaSemana, DiaTurno> dias) {
        this.dias = dias;
    }
	
	public DiaTurno getTurnoDelDia(DiaSemana dia) {
		return dias.get(dia);
	}
	
	public void actualizarTurno(DiaSemana dia, DiaTurno nuevoTurno) {
        dias.put(dia, nuevoTurno);
    }

	public Map<DiaSemana, DiaTurno> getDias() {
        return dias;
    }
}
