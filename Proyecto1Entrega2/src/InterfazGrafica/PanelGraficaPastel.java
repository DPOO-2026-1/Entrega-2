package InterfazGrafica;

import javax.swing.*;
import java.awt.*;

public class PanelGraficaPastel extends JPanel {
    private int copiasPrestamo;
    private int copiasVenta;
    private String nombreJuego;
    
    public PanelGraficaPastel(int prestamo, int venta) {
        this(prestamo, venta, "Juego seleccionado");
    }

    // CAMBIO IMPLEMENTADO: constructor con nombre del juego y soporte para datos reales
    public PanelGraficaPastel(int prestamo, int venta, String nombreJuego) {
        this.copiasPrestamo = prestamo;
        this.copiasVenta = venta;
        this.nombreJuego = nombreJuego;
        setPreferredSize(new Dimension(360, 300));
        setBackground(EstiloUI.COLOR_FONDO_BEIGE);
    }
    
    public void actualizarDatos(int prestamo, int venta, String nombreJuego) {
        this.copiasPrestamo = prestamo;
        this.copiasVenta = venta;
        this.nombreJuego = nombreJuego;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
    	super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int total = copiasPrestamo + copiasVenta;
        int size = Math.min(getWidth(), getHeight()) - 90;
        int x = (getWidth() - size) / 2;
        int y = 55;

        g2d.setColor(EstiloUI.COLOR_TEXTO_OSCURO);
        g2d.setFont(new Font("SansSerif", Font.BOLD, 14));
        g2d.drawString("Copias préstamo vs venta", 15, 22);

        g2d.setFont(new Font("SansSerif", Font.PLAIN, 12));
        g2d.drawString(nombreJuego != null ? nombreJuego : "Sin juego", 15, 40);

        if (total == 0) {
            g2d.setColor(Color.LIGHT_GRAY);
            g2d.fillOval(x, y, size, size);
            g2d.setColor(Color.BLACK);
            g2d.drawString("Sin copias registradas", x + 35, y + size / 2);
            return;
        }

        int anguloPrestamo = (int) Math.round((copiasPrestamo * 360.0) / total);
        int anguloVenta = 360 - anguloPrestamo;

        g2d.setColor(EstiloUI.COLOR_COMPONENTE_CAFE);
        g2d.fillArc(x, y, size, size, 0, anguloPrestamo);

        g2d.setColor(EstiloUI.COLOR_BANNER_CAFE);
        g2d.fillArc(x, y, size, size, anguloPrestamo, anguloVenta);

        g2d.setColor(Color.DARK_GRAY);
        g2d.drawOval(x, y, size, size);

        int leyendaY = y + size + 22;

        g2d.setColor(EstiloUI.COLOR_COMPONENTE_CAFE);
        g2d.fillRect(20, leyendaY - 10, 12, 12);
        g2d.setColor(EstiloUI.COLOR_TEXTO_OSCURO);
        g2d.drawString("Préstamo: " + copiasPrestamo, 38, leyendaY);

        g2d.setColor(EstiloUI.COLOR_BANNER_CAFE);
        g2d.fillRect(150, leyendaY - 10, 12, 12);
        g2d.setColor(EstiloUI.COLOR_TEXTO_OSCURO);
        g2d.drawString("Venta: " + copiasVenta, 168, leyendaY);
    }
}