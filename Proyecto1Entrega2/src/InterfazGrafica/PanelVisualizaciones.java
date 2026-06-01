package InterfazGrafica;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import ModuloVenta.CopiaVenta;
import ModuloVenta.ItemVenta;
import ModuloVenta.ProductoComestible;
import ModuloVenta.Venta;
import Torneo.InscripcionTorneo;
import Torneo.Torneo;
import Usuario.DiaSemana;
import World.Cafeteria;
import World.Juego;

public class PanelVisualizaciones extends JPanel {
    private JComboBox<String> cbJuegos;
    private JButton btnActualizar;
    private PanelGraficaPastel graficaPastel;
    private PanelGraficaBarras graficaBarras;
    private PanelGraficaLineas graficaLineas;
    private Cafeteria cafeteria;

    public PanelVisualizaciones() {
        setLayout(new BorderLayout(10, 10));
        setBackground(EstiloUI.COLOR_FONDO_BEIGE);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel panelSuperior = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelSuperior.setOpaque(false);
        panelSuperior.add(new JLabel("Juego para gráfica de pastel:"));

        cbJuegos = new JComboBox<>();
        panelSuperior.add(cbJuegos);

        btnActualizar = new JButton("Actualizar gráficas");
        btnActualizar.setBackground(EstiloUI.COLOR_BANNER_CAFE);
        btnActualizar.setForeground(Color.WHITE);
        btnActualizar.setFocusPainted(false);
        btnActualizar.setBorderPainted(false);
        btnActualizar.setOpaque(true);
        panelSuperior.add(btnActualizar);

        add(panelSuperior, BorderLayout.NORTH);

        JPanel panelGraficas = new JPanel(new GridLayout(2, 2, 15, 15));
        panelGraficas.setOpaque(false);

        graficaPastel = new PanelGraficaPastel(0, 0, "Sin juego seleccionado");
        graficaBarras = new PanelGraficaBarras();
        graficaLineas = new PanelGraficaLineas();

        panelGraficas.add(graficaPastel);
        panelGraficas.add(graficaBarras);
        panelGraficas.add(graficaLineas);

        add(panelGraficas, BorderLayout.CENTER);

        // CAMBIO IMPLEMENTADO: botón que recalcula con datos reales del sistema 
        btnActualizar.addActionListener(e -> recalcularGraficas());
        cbJuegos.addActionListener(e -> recalcularGraficaPastel());
    }

    // CAMBIO IMPLEMENTADO: entrada pública para que AdminController conecte la Cafetería real
    public void configurarDatos(Cafeteria cafeteria) {
        this.cafeteria = cafeteria;
        cargarComboJuegos();
        recalcularGraficas();
    }

    private void cargarComboJuegos() {
        cbJuegos.removeAllItems();

        if (cafeteria == null || cafeteria.getJuegos() == null) {
            return;
        }

        for (Juego juego : cafeteria.getJuegos()) {
            if (juego != null && juego.getNombre() != null) {
                cbJuegos.addItem(juego.getNombre());
            }
        }
    }

    private void recalcularGraficas() {
        recalcularGraficaPastel();
        recalcularGraficaBarras();
        recalcularGraficaLineas();
    }

    private void recalcularGraficaPastel() {
        if (cafeteria == null) {
            graficaPastel.actualizarDatos(0, 0, "Sin datos");
            return;
        }

        String nombreJuego = (String) cbJuegos.getSelectedItem();
        Juego juego = buscarJuego(nombreJuego);

        if (juego == null) {
            graficaPastel.actualizarDatos(0, 0, "Sin juego seleccionado");
            return;
        }

        int prestamo = juego.getCopiasParaPrestamo() != null ? juego.getCopiasParaPrestamo().size() : 0;
        int venta = juego.getCopiasParaVenta() != null ? juego.getCopiasParaVenta().size() : 0;

        graficaPastel.actualizarDatos(prestamo, venta, juego.getNombre());
    }

    private Juego buscarJuego(String nombreJuego) {
        if (nombreJuego == null || cafeteria == null || cafeteria.getJuegos() == null) {
            return null;
        }

        for (Juego juego : cafeteria.getJuegos()) {
            if (juego != null && nombreJuego.equals(juego.getNombre())) {
                return juego;
            }
        }

        return null;
    }

    private void recalcularGraficaBarras() {
        double[] cafe = new double[5];
        double[] juegos = new double[5];
        String[] etiquetas = new String[5];

        LocalDate hoy = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM");

        for (int i = 0; i < 5; i++) {
            LocalDate dia = hoy.minusDays(4 - i);
            etiquetas[i] = dia.format(formatter);
        }

        if (cafeteria != null && cafeteria.getVentas() != null) {
            for (Venta venta : cafeteria.getVentas()) {
                if (venta == null || venta.getFecha() == null || venta.getItemsVenta() == null) {
                    continue;
                }

                LocalDate fechaVenta = venta.getFecha().toLocalDate();
                int indice = (int) java.time.temporal.ChronoUnit.DAYS.between(hoy.minusDays(4), fechaVenta);

                if (indice < 0 || indice >= 5) {
                    continue;
                }

                for (ItemVenta item : venta.getItemsVenta()) {
                    if (item == null || item.getProducto() == null) {
                        continue;
                    }

                    // CAMBIO IMPLEMENTADO: valores netos sin impuestos, usando subtotal del item
                    if (item.getProducto() instanceof ProductoComestible) {
                        cafe[indice] += item.getSubtotalItem();
                    } else if (item.getProducto() instanceof CopiaVenta) {
                        juegos[indice] += item.getSubtotalItem();
                    }
                }
            }
        }

        graficaBarras.actualizarDatos(cafe, juegos, etiquetas);
    }

    private void recalcularGraficaLineas() {
        int[] reservas = new int[7];

        if (cafeteria != null
                && cafeteria.getGestorTorneo() != null
                && cafeteria.getGestorTorneo().getCatalogoTorneos() != null) {

            List<Torneo> torneos = cafeteria.getGestorTorneo().getCatalogoTorneos();

            for (Torneo torneo : torneos) {
                if (torneo == null || torneo.getDia() == null || torneo.getInscripciones() == null) {
                    continue;
                }

                int indiceDia = indiceDia(torneo.getDia());

                if (indiceDia < 0) {
                    continue;
                }

                int cuposReservados = 0;

                for (InscripcionTorneo inscripcion : torneo.getInscripciones()) {
                    if (inscripcion != null) {
                        cuposReservados += inscripcion.getCantidadCupos();
                    }
                }

                reservas[indiceDia] += cuposReservados;
            }
        }

        graficaLineas.actualizarDatos(reservas);
    }

    private int indiceDia(DiaSemana dia) {
        String normalizado = DiaSemana.normalizar(dia);

        if (normalizado.startsWith("LUNES")) return 0;
        if (normalizado.startsWith("MARTES")) return 1;
        if (normalizado.startsWith("MIERCOLES")) return 2;
        if (normalizado.startsWith("JUEVES")) return 3;
        if (normalizado.startsWith("VIERNES")) return 4;
        if (normalizado.startsWith("SABADO")) return 5;
        if (normalizado.startsWith("DOMINGO")) return 6;

        return -1;
    }
}