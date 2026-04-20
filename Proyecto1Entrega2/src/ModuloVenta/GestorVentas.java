package Proyecto1Entrega2.src.ModuloVenta;

import java.time.*;
import java.util.List;
import java.util.stream.Collectors;
import Proyecto1Entrega2.src.Persistencia.GestorPersistencia;

public class GestorVentas{
    private GestorPersistencia gestorPersistencia;

    public GestorVentas(GestorPersistencia gestorPersistencia) {
        this.gestorPersistencia = gestorPersistencia;
    }

    public void registrarVenta(Venta venta) {
        // Obtenemos la lista actual, agregamos la nueva y sobreescribimos el archivo
        List<Venta> ventas = gestorPersistencia.cargarVentas(venta.getRealizadaPor().getCafeteria().getUsuarios());
        ventas.add(venta);
        gestorPersistencia.guardarVentas(ventas);
    }

    public List<Venta> getVentas(LocalDateTime inicio, LocalDateTime fin, List<Usuario> usuariosDelSistema) {
        // Se necesita pasar la lista de usuarios para que el gestor pueda armar las referencias
        List<Venta> todas = gestorPersistencia.cargarVentas(usuariosDelSistema);
        
        return todas.stream()
                .filter(v -> v.getFecha() != null &&
                        (v.getFecha().isEqual(inicio) || v.getFecha().isAfter(inicio)) &&
                        (v.getFecha().isEqual(fin) || v.getFecha().isBefore(fin)))
                .collect(Collectors.toList());
    }
    
    public String generarInformeVentas(LocalDateTime inicio, LocalDateTime fin, String granularidad){
        //TODO
        return "";
    }
}