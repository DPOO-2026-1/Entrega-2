package InterfazGrafica;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Line2D;

public class PanelGraficaLineas extends JPanel {
    // Datos de prueba: Lunes a Domingo
    private int[] reservasSemanales = {5, 12, 8, 20, 35, 45, 30};

    public PanelGraficaLineas() {
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
        g2d.setStroke(new BasicStroke(2));
        g2d.drawLine(margen, alto - margen, ancho - margen, alto - margen);
        g2d.drawLine(margen, alto - margen, margen, margen);

        int maxReservas = 50; 
        int separacionX = (ancho - 2 * margen) / 6; 
        
        g2d.setColor(new Color(255, 152, 0)); // Naranja Vivo
        g2d.setStroke(new BasicStroke(3));

        int prevX = -1, prevY = -1;

        for (int i = 0; i < 7; i++) {
            int x = margen + i * separacionX;
            int y = alto - margen - (int) ((reservasSemanales[i] / (double) maxReservas) * (alto - 2 * margen));
            
            if (prevX != -1) {
                g2d.draw(new Line2D.Double(prevX, prevY, x, y));
            }
            prevX = x;
            prevY = y;
        }

        // Dibujar los nodos (puntos blancos con borde) encima de las líneas
        for (int i = 0; i < 7; i++) {
            int x = margen + i * separacionX;
            int y = alto - margen - (int) ((reservasSemanales[i] / (double) maxReservas) * (alto - 2 * margen));
            
            g2d.setColor(Color.WHITE);
            g2d.fillOval(x - 5, y - 5, 10, 10);
            g2d.setColor(new Color(255, 152, 0));
            g2d.drawOval(x - 5, y - 5, 10, 10);
        }
        
        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font("SansSerif", Font.PLAIN, 12));
        g2d.drawString("Evolución Semanal", margen, margen - 10);
    }
}
