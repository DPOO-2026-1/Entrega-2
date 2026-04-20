package Usuario;

import java.util.ArrayList;
import java.util.List;
import World.Juego;

public abstract class Usuario {
	private String login;
	private String password;
	private String nombre;
	private List<Juego> juegosFavoritos;

	public Usuario(String login, String password, String nombre) {
        this.login = login;
        this.password = password;
        this.nombre = nombre;
        this.juegosFavoritos = new ArrayList<>();
    }
	
	public boolean iniciarSesion(String login, String pass) {
		return this.login.equals(login) && this.password.equals(pass);
	}
	
	public void cerrarSesion() {
		System.out.println("Sesión cerrada para usuario: " + nombre);
	}
	
	public boolean cambiarPassword(String nuevoPass) {
		if (nuevoPass != null && !nuevoPass.isEmpty()) {
            this.password = nuevoPass;
            return true;
        }
        return false;
	}
	
	public void agregarFavorito(Juego j) {
		if (j != null && !juegosFavoritos.contains(j)) {
            juegosFavoritos.add(j);
        }
	}
	
	public void eliminarFavorito(Juego j) {
		juegosFavoritos.remove(j);
	}

	public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public String getNombre() {
        return nombre;
    }

    public List<Juego> getJuegosFavoritos() {
        return juegosFavoritos;
    }
}
