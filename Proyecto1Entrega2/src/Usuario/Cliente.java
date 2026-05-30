package Usuario;

import World.Mesa;
import World.Prestamo;
import World.Cafeteria;
import World.CopiaPrestamo;
import World.Juego;
import ModuloVenta.ItemVenta;
import ModuloVenta.Venta;
import java.util.Date;
import java.util.List;

public class Cliente extends Usuario {
    private boolean esNinio;
    private boolean esJoven;
    private int juegosReservados;
    private int puntosFidelidad;
    private Mesa mesaActual;

    public Cliente(String login, String password, String nombre, boolean esNinio, boolean esJoven) {
        super(login, password, nombre);
        this.esNinio = esNinio;
        this.esJoven = esJoven;
        this.juegosReservados = 0;
        this.puntosFidelidad = 0;
        this.mesaActual = null;
    }

    public Mesa reservarMesa(int personas, boolean hayNinos, boolean hayJovenes) {
        Cafeteria cafe = Cafeteria.getInstance();
        
        if (!cafe.registrarIngreso(personas)) {
            throw new IllegalStateException("El establecimiento no tiene capacidad para " + personas + " personas.");
        }
        
        Mesa mesaDisponible = cafe.buscarMesaDisponible(personas, hayNinos, hayJovenes);

        if (mesaDisponible == null) {
            // Revertir el ingreso si no hay mesa
            cafe.registrarSalida(personas);
            throw new IllegalStateException("No hay mesas disponibles para esas condiciones.");
        }
        mesaDisponible.ocupar(personas, hayNinos, hayJovenes, this);
        this.mesaActual = mesaDisponible;
        return mesaDisponible;
    }
    
    public void liberarMesa() {
        if (mesaActual != null) {
            Cafeteria cafe = Cafeteria.getInstance();
            cafe.registrarSalida(mesaActual.getCapacidadMax());
            mesaActual.liberar();
            this.mesaActual = null;
        }
    }

    public Prestamo solicitarPrestamo(CopiaPrestamo copia, Mesa mesa) {
        // Un cliente solo puede tener hasta dos juegos simultáneamente 
        if (this.juegosReservados >= 2) {
            throw new IllegalStateException("Ya tienes el máximo de 2 juegos prestados.");
        }
        
        if (mesa == null) {
            throw new IllegalStateException("Debes tener una mesa asignada para pedir un préstamo.");
        }

        // Validar que el juego sea apto para la edad de los comensales en la mesa
        Juego juego = copia.getJuegoAsociado();
        if (juego != null && !juego.esAptoParaEdad(mesa.getEdadMinimaEnMesa())) {
            throw new IllegalArgumentException("El juego no es apto para la edad de los comensales en la mesa.");
        }

        if (!copia.estaDisponible()) {
            throw new IllegalStateException("La copia del juego no está disponible.");
        }
        
        if (juego != null && !juego.soportaNPersonas(mesa.getCapacidadMax())) {
            throw new IllegalArgumentException("El juego no soporta el número de personas en la mesa.");
        }
        
        if (juego != null && "Accion".equalsIgnoreCase(juego.getCategoria()) && !mesa.puedeRecibirJuegoAccion()) {
            throw new IllegalStateException("No se puede prestar un juego de Acción a una mesa con bebida caliente.");
        }

        // Crear el préstamo y actualizar estado
        Prestamo nuevoPrestamo = new Prestamo(new Date(), copia, mesa, this);
        mesa.agregarPrestamo(nuevoPrestamo);
        Cafeteria.getInstance().getHistorialPrestamos().add(nuevoPrestamo);
        
        this.juegosReservados++;
        
        return nuevoPrestamo;
    }

    public void devolverJuego(Prestamo p) {
    	if (p == null) {
            throw new IllegalArgumentException("El préstamo no puede ser nulo.");
        }
        if (!p.getEstado().equals("Activo")) {
            throw new IllegalStateException("El préstamo ya fue finalizado.");
        }
        p.finalizar(); // Cambia el estado y registra fecha fin 
        if (this.juegosReservados > 0) {
            this.juegosReservados--;
        };
    }

    public Venta realizarCompra(ItemVenta[] items, String codigoDescuento, int puntosARedimir) {
    	if (items == null || items.length == 0) {
            throw new IllegalArgumentException("Debe haber al menos un ítem en la compra.");
        }
    	
    	Venta nuevaVenta = new Venta(new Date(), items, this);

        // Si el código es válido (10% de descuento compartido por empleado) 
    	if (codigoDescuento != null && !codigoDescuento.isEmpty()) {
            boolean codigoValido = validarCodigoEmpleado(codigoDescuento);
            if (codigoValido) {
                nuevaVenta.aplicarDescuento("CLIENTE"); // 10%
            }
        }
    	
    	double descuentoPuntos = 0;
        if (puntosARedimir > 0 && (codigoDescuento == null || codigoDescuento.isEmpty())) {
            descuentoPuntos = usarPuntosFidelidad(puntosARedimir);
        }
        
        nuevaVenta.calcularSubtotal();
        nuevaVenta.calcularImpuestosTotales();
        nuevaVenta.calcularTotal();
        
        if (descuentoPuntos > 0) {
            double totalFinal = Math.max(0, nuevaVenta.getTotal() - descuentoPuntos);
            nuevaVenta.setPuntosGenerados((int)(totalFinal * 0.01));
        }
        
        int puntosGanados = nuevaVenta.calcularPuntosGenerados();
        this.puntosFidelidad += puntosGanados;
        
        Cafeteria.getInstance().getVentas().add(nuevaVenta);
        return nuevaVenta;
    }
    
    public Venta realizarCompra(ItemVenta[] items, String codigoDescuento) {
        return realizarCompra(items, codigoDescuento, 0);
    }

    public double usarPuntosFidelidad(int puntosARedimir) {
        if (puntosARedimir <= 0) {
            throw new IllegalArgumentException("La cantidad de puntos debe ser positiva.");
        }
        if (puntosARedimir > this.puntosFidelidad) {
            throw new IllegalArgumentException("No tienes suficientes puntos. Tienes: " + this.puntosFidelidad);
        }

        // Cada punto equivale a un peso ($1) 
        this.puntosFidelidad -= puntosARedimir;
        return (double) puntosARedimir;
    }

    public boolean isEsNinio() { 
    	return esNinio; 
    }
    public boolean isEsJoven() { 
    	return esJoven; 
    }
    public int getPuntosFidelidad() { 
    	return puntosFidelidad; 
    }
    public void setPuntosFidelidad(int puntosFidelidad) {
        this.puntosFidelidad = puntosFidelidad;
    }
    public void marcarFavorito(Juego j) {
        agregarFavorito(j);
    }

    public void desmarcarFavorito(Juego j) {
        eliminarFavorito(j);
    }

    public boolean tieneFavorito(Juego j) {
        return getJuegosFavoritos().contains(j);
    }
    
    public int getJuegosReservados() {
        return juegosReservados;
    }

    public void setJuegosReservados(int juegosReservados) {
        this.juegosReservados = juegosReservados;
    }

    public Mesa getMesaActual() {
        return mesaActual;
    }

    public void setMesaActual(Mesa mesaActual) {
        this.mesaActual = mesaActual;
    }
    
    private boolean validarCodigoEmpleado(String codigo) {
        List<Usuario> usuarios = Cafeteria.getInstance().getUsuarios();
        for (Usuario u : usuarios) {
            if (u instanceof Empleado) {
                Empleado emp = (Empleado) u;
                if (codigo.equals(emp.getCodigoDescuento())) {
                    return true;
                }
            }
        }
        return false;
    }
}