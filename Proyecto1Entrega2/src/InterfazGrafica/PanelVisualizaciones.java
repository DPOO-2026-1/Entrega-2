package InterfazGrafica;

import javax.swing.*;
import java.awt.*;

public class PanelVisualizaciones extends JPanel {
    public PanelVisualizaciones() {
        setLayout(new GridLayout(2, 2, 15, 15));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // TODO: En tu código real, debes pasarle los datos (ej: listas de ventas, copias)
        add(new PanelGraficaPastel(15, 5)); // Datos falsos de prueba: 15 prestamo, 5 venta
        add(new PanelGraficaBarras()); 
        add(new PanelGraficaLineas());
    }
}