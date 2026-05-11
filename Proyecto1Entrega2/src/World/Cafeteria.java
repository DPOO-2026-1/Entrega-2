package World;

import Usuario.Usuario;
import Usuario.GestorUsuarios;
import Usuario.DiaSemana;
import Usuario.Empleado;
import Usuario.Cocinero;
import Usuario.Mesero;
import Usuario.DiaTurno;
import ModuloVenta.GestorVentas;
import ModuloVenta.Pasteleria;

import java.util.ArrayList;
import java.util.List;

public class Cafeteria {

	private static Cafeteria instance;

    private int capacidadMax;
    private int cantPersonasActuales;
    private String nombreEstablecimiento;
    private List<Juego> juegos;
    private List<Mesa> mesas;
    private List<Prestamo> historialPrestamos;
    private GestorUsuarios gestorUsuarios;
    private GestorVentas gestorVentas;

 // Constructor Privado, evita que alguien haga "new Cafeteria()" desde fuera
    private Cafeteria(int capacidadMax, String nombre, GestorUsuarios gu, GestorVentas gv) {
        this.capacidadMax = capacidadMax;
        this.nombreEstablecimiento = nombre;
        this.gestorUsuarios = gu;
        this.gestorVentas = gv;
        this.juegos = new ArrayList<>();
        this.mesas = new ArrayList<>();
        this.historialPrestamos = new ArrayList<>();
    }

    // Metodos
    public Usuario login(String login, String pass) {
        return this.gestorUsuarios.autenticar(login, pass);
    }

    public boolean hayCapacidad(int nPersonas) {
        return (this.cantPersonasActuales + nPersonas) <= this.capacidadMax;
    }

    public boolean registrarIngreso(int nPersonas) {
        if (hayCapacidad(nPersonas)) {
            this.cantPersonasActuales += nPersonas;
            return true;
        }
        return false;
    }

    public void registrarSalida(int nPersonas) {
        this.cantPersonasActuales -= nPersonas;
        if (this.cantPersonasActuales < 0) {
            this.cantPersonasActuales = 0;
        }
    }

    public Juego buscarJuego(String nombre) {
        for (Juego juego : juegos) {
            if (juego.getNombre().equalsIgnoreCase(nombre)) {
                return juego;
            }
        }
        return null;
    }

    public Mesa getMesaDisponible(int personas) {
        for (Mesa mesa : mesas) {
            if (mesa.estaDisponible() && mesa.getCapacidadMax() >= personas) {
                return mesa;
            }
        }
        return null;
    }

    public boolean validarMinimoEmpleados(DiaSemana dia) {
        int contadorCocineros = 0;
        int contadorMeseros = 0;

        for (Usuario u : this.gestorUsuarios.getUsuarios()) {
            
            // Filtramos solo a los que son empleados
            if (u instanceof Empleado) {
                Empleado emp = (Empleado) u;
                
                // Revisamos la lista de turnos asignados de este empleado
                for (DiaTurno dt : emp.consultarDiasAsignados()) {
                    
                    // Si el turno corresponde al día que estamos validando y está asignado
                    if (dt.getDia().equals(dia) && dt.estaAsignado()) {
                        
                        // Sumamos al contador correspondiente según el rol
                        if (emp instanceof Cocinero) {
                            contadorCocineros++;
                        } else if (emp instanceof Mesero) {
                            contadorMeseros++;
                        }
                        
                        // Rompemos el ciclo interno porque ya confirmamos que trabaja este día
                        break; 
                    }
                }
            }
        }

        // Retornamos true solo si se cumple la condición de 1 cocinero y 2 meseros
        return contadorCocineros >= 1 && contadorMeseros >= 2;
    }

    public List<String> consultarAlergenos(Pasteleria p) {
        return p.getAlergenos();
    }


    // GETTERS Y SETTERS
    
    // getInstance: Si no existe la cafetería, la crea; si ya existe, devuelve la que hay
    public static Cafeteria getInstance(int cap, String nom, GestorUsuarios gu, GestorVentas gv) {
        if (instance == null) {
            instance = new Cafeteria(cap, nom, gu, gv);
        }
        return instance;
    }
    
    public static Cafeteria getInstance() {
        return instance;
    }
    
    // COMENTARIO: Este método permite limpiar la instancia del Singleton entre pruebas, para garantizar que los datos no se contaminen.
    public static void resetInstance() {
        instance = null;
    }
    
    public int getCapacidadMax() { 
    	return capacidadMax; 
    }
    public void setCapacidadMax(int capacidadMax) { 
    	this.capacidadMax = capacidadMax; 
    }

    public int getCantPersonasActuales() { 
    	return cantPersonasActuales; 
    }

    public String getNombreEstablecimiento() { 
    	return nombreEstablecimiento; 
    }
    public void setNombreEstablecimiento(String nombreEstablecimiento) { 
    	this.nombreEstablecimiento = nombreEstablecimiento; 
    }

    public List<Juego> getJuegos() { 
    	return juegos; 
    }
    public void setJuegos(List<Juego> juegos) { 
    	this.juegos = juegos; 
    }

    public List<Mesa> getMesas() { 
    	return mesas; 
    }
    public void setMesas(List<Mesa> mesas) { 
    	this.mesas = mesas; 
    }

    public List<Prestamo> getHistorialPrestamos() { 
    	return historialPrestamos; 
    }
    public void setHistorialPrestamos(List<Prestamo> historialPrestamos) { 
    	this.historialPrestamos = historialPrestamos; 
    }

    public GestorUsuarios getGestorUsuarios() { 
    	return gestorUsuarios; 
    }
    public void setGestorUsuarios(GestorUsuarios gestorUsuarios) { 
    	this.gestorUsuarios = gestorUsuarios; 
    }

    public GestorVentas getGestorVentas() { 
    	return gestorVentas; 
    }
    public void setGestorVentas(GestorVentas gestorVentas) { 
    	this.gestorVentas = gestorVentas; 
    }
}