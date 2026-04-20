package Usuario;

public abstract class Empleado extends Usuario{
	private String codigoDesuento;
	private boolean estaDeTurno;
	private DiaTurno[] diasAsignados;
	
	public SoilicitudTurno solicitarCambioTurno(DiaSemana dia) {
		//TODO
	}
	
	public SolicitudTurno solicitarIntercambioTurno(Empleado otro, String dia) {
		//TODO
	}
	
	public SugerenciaMenu sugerirPlato(String desc) {
		
	}
	
	public Venta realizarCompra(ItemVenta[] items) {
		//TODO
	}
	
	public Prestamo alquilarJuego(CopiaPrestamo copia) {
		//TODO
	}
	
	public boolean estaEnTurno() {
		return estaDeTurno;
	}
	
	public DiaTurno[] consultarDiasAsignados() {
		return diasAsignados;
	}
}
