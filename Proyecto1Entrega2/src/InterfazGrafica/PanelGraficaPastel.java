package InterfazGrafica;

import javax.swing.*;
import java.awt.*;

public class PanelGraficaPastel extends JPanel {
    private int copiasPrestamo;
    private int copiasVenta;

    public PanelGraficaPastel(int prestamo, int venta) {
        this.copiasPrestamo = prestamo;
        this.copiasVenta = venta;
        setPreferredSize(new Dimension(300, 300));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        // Habilitar antialiasing para bordes suaves
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int total = copiasPrestamo + copiasVenta;
        int size = Math.min(getWidth(), getHeight()) - 40;
        int x = (getWidth() - size) / 2;
        int y = (getHeight() - size) / 2;

        if (total == 0) {
            g2d.setColor(Color.LIGHT_GRAY);
            g2d.fillOval(x, y, size, size);
            g2d.setColor(Color.BLACK);
            g2d.drawString("Sin Copias Registradas", x + size / 4, y + size / 2);
            return;
        }

        // Sombra sutil
        g2d.setColor(new Color(0, 0, 0, 50));
        g2d.fillOval(x + 5, y + 5, size, size);

        // Calcular ángulos
        int anguloPrestamo = (int) Math.round((copiasPrestamo * 360.0) / total);
        int anguloVenta = 360 - anguloPrestamo; 

        // Dibujar Préstamo
        g2d.setColor(new Color(92, 107, 192)); // Azul Slate
        g2d.fillArc(x, y, size, size, 0, anguloPrestamo);

        // Dibujar Venta
        g2d.setColor(new Color(255, 112, 67)); // Naranja Coral
        g2d.fillArc(x, y, size, size, anguloPrestamo, anguloVenta);
        
        // Título o Leyenda
        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font("SansSerif", Font.BOLD, 14));
        g2d.drawString("Disponibilidad de Juegos", 10, 20);
    }
}