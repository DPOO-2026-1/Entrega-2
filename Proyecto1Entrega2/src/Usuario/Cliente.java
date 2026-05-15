package Usuario;

import World.Mesa;
import World.Prestamo;
import World.Cafeteria;
import World.CopiaPrestamo;
import ModuloVenta.ItemVenta;
import ModuloVenta.Venta;
import java.util.Date;

public class Cliente extends Usuario {
    private boolean esNinio;
    private boolean esJoven;
    private int juegosReservados;
    private int puntosFidelidad;

    public Cliente(String login, String password, String nombre, boolean esNinio, boolean esJoven) {
        super(login, password, nombre);
        this.esNinio = esNinio;
        this.esJoven = esJoven;
        this.juegosReservados = 0;
        this.puntosFidelidad = 0;
    }

    public Mesa reservarMesa(int personas, boolean hayNinos, boolean hayJovenes) {
        Cafeteria cafe = Cafeteria.getInstance();
        Mesa mesaDisponible = cafe.buscarMesaDisponible(personas, hayNinos, hayJovenes);

        if (mesaDisponible != null) {
            mesaDisponible.ocupar(personas, hayNinos, hayJovenes, this);
            return mesaDisponible;
        } else {
            throw new IllegalStateException("No hay mesas disponibles para esas condiciones.");
        }
    }

    public Prestamo solicitarPrestamo(CopiaPrestamo copia, Mesa mesa) {
        // Un cliente solo puede tener hasta dos juegos simultáneamente 
        if (this.juegosReservados >= 2) {
            throw new IllegalStateException("Ya tienes el máximo de 2 juegos prestados.");
        }

        // Validar que el juego sea apto para la edad de los comensales en la mesa
        if (!copia.getJuegoAsociado().esAptoParaEdad(mesa.getEdadMinimaEnMesa())) {
            throw new IllegalArgumentException("El juego no es apto para la edad de los comensales.");
        }

        if (!copia.estaDisponible()) {
            throw new IllegalStateException("La copia del juego no está disponible.");
        }

        // Crear el préstamo y actualizar estado
        Prestamo nuevoPrestamo = new Prestamo(new Date(), copia, mesa, this);
        copia.prestar(); 
        this.juegosReservados++;
        
        return nuevoPrestamo;
    }

    public void devolverJuego(Prestamo p) {
        if (p != null && p.getEstado().equals("Activo")) {
            p.finalizar(); // Cambia el estado y registra fecha fin 
            p.getCopia().devolver(); // Marca la copia como disponible nuevamente
            this.juegosReservados--;
        }
    }

    public Venta realizarCompra(ItemVenta[] items, String codigoDesc) {
        Venta nuevaVenta = new Venta(new Date(), items, this);

        // Si el código es válido (10% de descuento compartido por empleado) 
        if (codigoDesc != null && !codigoDesc.isEmpty()) {
            nuevaVenta.aplicarDescuento("CLIENTE");
        }

        // Calcular puntos generados (1% del total de la venta) 
        int puntosGanados = (int) (nuevaVenta.getTotal() * 0.01);
        this.puntosFidelidad += puntosGanados;
        nuevaVenta.setPuntosGenerados(puntosGanados);

        return nuevaVenta;
    }

    public double usarPuntosFidelidad(int puntosARedimir) {
        if (puntosARedimir > this.puntosFidelidad) {
            throw new IllegalArgumentException("No tienes suficientes puntos.");
        }

        // Cada punto equivale a un peso ($1) 
        double descuentoPesos = (double) puntosARedimir;
        this.puntosFidelidad -= puntosARedimir;

        return descuentoPesos;
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
}