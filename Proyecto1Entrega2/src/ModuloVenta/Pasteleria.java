package ModuloVenta;

import java.util.List;

public class Pasteleria extends ProductoComestible{
	private List<String> alergenos;
	
	// COMENTARIO: Constructor para pastelería y getter de alérgenos ajustado para devolver List<String> compatible con la cafetería.
	public Pasteleria(String nombre, double precioBase, List<String> alergenos) {
        super(nombre, precioBase);
        this.alergenos = alergenos;
    }
	
	public List<String> getAlergenos() {
		return this.alergenos;
	}
}
