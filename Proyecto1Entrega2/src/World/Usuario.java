package World;

public abstract class Usuario {
	private String login;
	private String password;
	private String nombre;
	private Juego juegosFavoritos;
	
	public boolean iniciarSesion(String login, String pass) {
		//TODO
	}
	
	public void cerrarSesion() {
		//TODO
	}
	
	public boolean cambiarPassword(String nuevoPass) {
		//TODO
	}
	
	public void agregarFavorito(Juego j) {
		//TODO
	}
	
	public void eliminarFavorito(Juego j) {
		//TODO
	}
}
