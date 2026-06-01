package Usuario;

public class SugerenciaMenu {
	private String descripcion;
	private String estado;
	private Empleado creadoPor;
	
	// COMENTARIO: Añadido constructor y getters/setters para pruebas
	public SugerenciaMenu(String descripcion, String estado, Empleado creadoPor) {
        this.descripcion = descripcion;
        this.estado = estado;
        this.creadoPor = creadoPor;
    }
	
	// Getter necesario para mostrar las sugerencias en el PanelMenuAdmin.
    public String getDescripcion() {
        return descripcion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Empleado getCreadoPor() {
        return creadoPor;
    }
}
