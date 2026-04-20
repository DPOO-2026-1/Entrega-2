package Proyecto1Entrega2.src.ModuloVenta;
import java.time.*;
import java.util.List;
import Proyecto1Entrega2.src.Persistencia.GestorPersistencia;

public class GestorVentas{
    private GestorPersistencia gestorPersistencia;

    public GestorVentas(GestorPersistencia gestorPersistencia) {
        this.gestorPersistencia = gestorPersistencia;
    }

    public void registrarVenta(Venta venta) {
        List<Venta> ventas = gestorPersistencia.cargarVentas(venta.getRealizadaPor().getCafeteria().getUsuarios());
        ventas.add(venta);
        gestorPersistencia.guardarVentas(ventas);
    }

    public Venta[] getVentas(LocalDateTime inicio, LocalDateTime fin) {
        public List<Venta> getVentas(LocalDateTime inicio, LocalDateTime fin) {
        List<Venta> todas = gestorPersistencia.cargarVentas(null);
        return todas.stream()
                .filter(v -> v.getFecha() != null &&
                        (v.getFecha().isEqual(inicio) || v.getFecha().isAfter(inicio)) &&
                        (v.getFecha().isEqual(fin) || v.getFecha().isBefore(fin)))
                .toList();
    }
    
    public String generarInformeVentas(LocalDateTime inicio, LocalDateTime fin, String granularidad){
        //TODO
    }
}