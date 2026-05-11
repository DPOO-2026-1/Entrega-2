package Usuario;

public class SolicitudTurno {
	private String descripcion;
	private String estado;
	private Empleado solicitadoPor;
	private boolean intercambio;
	
	private DiaSemana dia;

	// COMENTARIO: Constructor y getters/setters añadidos para poder interactuar con las solicitudes de turno en las pruebas.
	public SolicitudTurno(DiaSemana dia, String estado, Empleado solicitadoPor, boolean intercambio) {
        this.dia = dia;
        this.estado = estado;
        this.solicitadoPor = solicitadoPor;
        this.intercambio = intercambio;
    }

    public DiaSemana getDia() {
        return dia;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Empleado getSolicitadoPor() {
        return solicitadoPor;
    }

    public boolean isEsIntercambio() {
        return intercambio;
    }
}
