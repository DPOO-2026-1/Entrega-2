package ModuloVenta;

import java.time.*;
import java.time.temporal.ChronoField;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import Persistencia.GestorPersistencia;
import Usuario.Usuario;

public class GestorVentas{
    private GestorPersistencia gestorPersistencia;

    public GestorVentas(GestorPersistencia gestorPersistencia) {
        this.gestorPersistencia = gestorPersistencia;
    }
    
    //Código corregido, puede presentar problemas
    //Código anterior:public void registrarVenta(Venta venta) {
    // Obtenemos la lista actual, agregamos la nueva y sobreescribimos el archivo
    //List<Venta> ventas = gestorPersistencia.cargarVentas(venta.getRealizadaPor().getCafeteria().getUsuarios());
    //ventas.add(venta);
  //  gestorPersistencia.guardarVentas(ventas);
//}
    public void registrarVenta(Venta venta, List<Usuario> usuariosDelSistema) {
        // Obtenemos la lista actual, agregamos la nueva y sobreescribimos el archivo
        List<Venta> ventas = gestorPersistencia.cargarVentas(usuariosDelSistema);
        ventas.add(venta);
        gestorPersistencia.guardarVentas(ventas);
    }

    public List<Venta> getVentas(LocalDateTime inicio, LocalDateTime fin, List<Usuario> usuariosDelSistema) {
        List<Venta> todas = gestorPersistencia.cargarVentas(usuariosDelSistema);
        
        return todas.stream()
                .filter(v -> v.getFecha() != null &&
                        (v.getFecha().isEqual(inicio) || v.getFecha().isAfter(inicio)) &&
                        (v.getFecha().isEqual(fin) || v.getFecha().isBefore(fin)))
                .collect(Collectors.toList());
    }
    
    public String generarInformeVentas(LocalDateTime inicio, LocalDateTime fin, String granularidad, List<Usuario> usuariosDelSistema){
    List<Venta> ventasPeriodo = getVentas(inicio, fin, usuariosDelSistema);

    StringBuilder informe = new StringBuilder();
    informe.append("=== Informe de Ventas ===\n");
    informe.append("Periodo: ").append(inicio).append(" a ").append(fin).append("\n");
    informe.append("Granularidad: ").append(granularidad).append("\n\n");

    Map<String, List<Venta>> agrupadas;
    if ("DIARIO".equalsIgnoreCase(granularidad)) {
        agrupadas = ventasPeriodo.stream()
            .collect(Collectors.groupingBy(v -> v.getFecha().toLocalDate().toString()));
    } else if ("SEMANAL".equalsIgnoreCase(granularidad)) {
        agrupadas = ventasPeriodo.stream()
            .collect(Collectors.groupingBy(v -> v.getFecha().getYear() + "-W" + v.getFecha().get(ChronoField.ALIGNED_WEEK_OF_YEAR)));
    } else if ("MENSUAL".equalsIgnoreCase(granularidad)) {
        agrupadas = ventasPeriodo.stream()
            .collect(Collectors.groupingBy(v -> v.getFecha().getYear() + "-" + v.getFecha().getMonthValue()));
    } else {
        agrupadas = Map.of("TOTAL", ventasPeriodo);
    }

    for (String periodo : agrupadas.keySet()) {
        List<Venta> grupo = agrupadas.get(periodo);

        double totalJuegos = 0;
        double impuestosJuegos = 0;
        double propinasJuegos = 0;

        double totalComida = 0;
        double impuestosComida = 0;
        double propinasComida = 0;

        for (Venta v : grupo) {
            for (ItemVenta item : v.getItemsVenta()) {
                double subtotal = item.getSubtotalItem();
                double impuesto = item.calcularImpuestoItem();

                if (item.getProducto() instanceof CopiaVenta) {
                    totalJuegos += subtotal;
                    impuestosJuegos += impuesto;
                    propinasJuegos += v.calcularPropina();
                } else if (item.getProducto() instanceof ProductoComestible) {
                    totalComida += subtotal;
                    impuestosComida += impuesto;
                    propinasComida += v.calcularPropina();
                }
            }
        }

        informe.append("Periodo: ").append(periodo).append("\n");
        informe.append("  Juegos -> Costo: ").append(totalJuegos)
               .append(", Impuestos: ").append(impuestosJuegos)
               .append(", Propinas: ").append(propinasJuegos).append("\n");
        informe.append("  Comida -> Costo: ").append(totalComida)
               .append(", Impuestos: ").append(impuestosComida)
               .append(", Propinas: ").append(propinasComida).append("\n\n");
    }

    return informe.toString();
}

}