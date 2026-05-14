package Usuario;

import java.util.ArrayList;
import java.util.List;

import Persistencia.GestorPersistencia;
import World.Cafeteria;

public class GestorUsuarios{
    private GestorPersistencia gestorPersistencia;
    private Cafeteria cafeteria;
    private List<Usuario> usuarios;

    public GestorUsuarios(GestorPersistencia gp, Cafeteria cafe) {
        this.gestorPersistencia = gp;
        this.cafeteria = cafe;
        this.usuarios = new ArrayList<>();
    }
    
    public List<Usuario> getUsuarios() {
        return this.usuarios;
    }

    public Cliente registrarCliente(String login, String password, String nombre, boolean esNino, boolean esJoven) {
        if (existeUsuario(login)) {
            throw new IllegalArgumentException("Ya existe un usuario con ese login.");
        }
        Cliente c = new Cliente(login, password, nombre, esNino, esJoven);
        this.usuarios.add(c);
        if (gestorPersistencia != null) {
            gestorPersistencia.guardarUsuarios(this.usuarios);
        }
        return c;
    }

    public Empleado registrarEmpleado(String login, String password, String nombre, String tipo, String codigoDescuento) {
        if (existeUsuario(login)) {
            throw new IllegalArgumentException("Ya existe un usuario con ese login.");
        }
        Empleado e;
        if ("MESERO".equalsIgnoreCase(tipo)) {
            e = new Mesero(login, password, nombre, codigoDescuento);
        } else if ("COCINERO".equalsIgnoreCase(tipo)) {
            e = new Cocinero(login, password, nombre, codigoDescuento);
        } else {
            throw new IllegalArgumentException("Tipo de empleado no válido.");
        }
        cafeteria.getUsuarios().add(e);
        gestorPersistencia.guardarUsuarios(cafeteria.getUsuarios());
        return e;
    }

    public Administrador registrarAdministrador(String login, String password, String nombre) {
        if (existeUsuario(login)) {
            throw new IllegalArgumentException("Ya existe un usuario con ese login.");
        }
        Administrador a = new Administrador(login, password, nombre);
        cafeteria.getUsuarios().add(a);
        gestorPersistencia.guardarUsuarios(cafeteria.getUsuarios());
        return a;
    }

    public Usuario autenticar(String login, String password) {
        for (Usuario u : cafeteria.getUsuarios()) {
            if (u.iniciarSesion(login, password)) {
                return u;
            }
        }
        return null;
    }

    public Usuario buscarUsuario(String login) {
        for (Usuario u : this.usuarios) {
            if (u.getLogin().equals(login)) {
                return u;
            }
        }
        return null;
    }

    public boolean existeUsuario(String login) {
        return buscarUsuario(login) != null;
    }

    public boolean eliminarUsuario(String login) {
        Usuario u = buscarUsuario(login);
        if (u != null) {
            this.usuarios.remove(u);
            if(gestorPersistencia != null) {
                gestorPersistencia.guardarUsuarios(this.usuarios);
            }
            return true;
        }
        return false;
    }
    
    // Cambios para consola
    public void setCafeteria(Cafeteria cafeteria) {
        this.cafeteria = cafeteria;
    }

    public void setUsuarios(List<Usuario> usuarios) {
        this.usuarios = usuarios;
    }
}