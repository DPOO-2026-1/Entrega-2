package InterfazGrafica;

import javax.swing.*;
import java.awt.*;

public class PanelGraficaBarras extends JPanel {
    // Datos de prueba (5 días, valores netos para Cafetería y Juegos)
    private double[] valoresCafeteria = {120, 150, 90, 200, 180};
    private double[] valoresJuegos = {80, 60, 110, 150, 130};

    public PanelGraficaBarras() {
        setPreferredSize(new Dimension(400, 300));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int ancho = getWidth();
        int alto = getHeight();
        int margen = 40;
        
        // Ejes
        g2d.drawLine(margen, alto - margen, ancho - margen, alto - margen); // X
        g2d.drawLine(margen, alto - margen, margen, margen); // Y

        double maxValor = 250.0; // En producción, calcular el máximo real iterando el array
        int anchoBarra = 20;
        int separacion = 15;
        
        for (int i = 0; i < 5; i++) {
            int x = margen + separacion + i * (anchoBarra * 2 + separacion);
            
            // Barra Cafetería
            int altoBarraCafe = (int) ((valoresCafeteria[i] / maxValor) * (alto - 2 * margen));
            g2d.setColor(new Color(239, 83, 80)); // Coral suave
            g2d.fillRect(x, alto - margen - altoBarraCafe, anchoBarra, altoBarraCafe);
            
            // Barra Juegos
            int altoBarraJuegos = (int) ((valoresJuegos[i] / maxValor) * (alto - 2 * margen));
            g2d.setColor(new Color(66, 165, 245)); // Azul Royal
            g2d.fillRect(x + anchoBarra, alto - margen - altoBarraJuegos, anchoBarra, altoBarraJuegos);
            
            // Etiqueta Eje X
            g2d.setColor(Color.BLACK);
            g2d.drawString("Día " + (i+1), x, alto - margen + 15);
        }
        g2d.drawString("Ventas de los últimos 5 días", margen, margen - 10);
    }
}