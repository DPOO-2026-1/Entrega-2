package InterfazGrafica;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Line2D;

public class PanelGraficaLineas extends JPanel {
	private int[] reservasSemanales;
    private String[] dias;

    public PanelGraficaLineas() {
    	this.reservasSemanales = new int[7];
        this.dias = new String[]{"Lun", "Mar", "Mié", "Jue", "Vie", "Sáb", "Dom"};
        setPreferredSize(new Dimension(400, 300));
        setBackground(EstiloUI.COLOR_FONDO_BEIGE);
    }
    
    // CAMBIO IMPLEMENTADO: setter para reservas reales
    public void actualizarDatos(int[] reservasSemanales) {
        this.reservasSemanales = reservasSemanales != null ? reservasSemanales : new int[7];
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
    	super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int ancho = getWidth();
        int alto = getHeight();

        int margenIzq = 50;
        int margenInf = 45;
        int margenSup = 45;
        int margenDer = 30;
        int areaAlto = alto - margenSup - margenInf;

        g2d.setColor(EstiloUI.COLOR_TEXTO_OSCURO);
        g2d.setFont(new Font("SansSerif", Font.BOLD, 14));
        g2d.drawString("Reservas por día de torneo", margenIzq, 24);

        g2d.setStroke(new BasicStroke(2));
        g2d.drawLine(margenIzq, alto - margenInf, ancho - margenDer, alto - margenInf);
        g2d.drawLine(margenIzq, alto - margenInf, margenIzq, margenSup);

        int maxReservas = 1;

        for (int r : reservasSemanales) {
            maxReservas = Math.max(maxReservas, r);
        }

        int separacionX = (ancho - margenIzq - margenDer) / 6;

        int prevX = -1;
        int prevY = -1;

        g2d.setColor(EstiloUI.COLOR_COMPONENTE_CAFE);
        g2d.setStroke(new BasicStroke(3));

        for (int i = 0; i < 7; i++) {
            int x = margenIzq + i * separacionX;
            int y = alto - margenInf - (int) ((valorSeguro(i) / (double) maxReservas) * areaAlto);

            if (prevX != -1) {
                g2d.draw(new Line2D.Double(prevX, prevY, x, y));
            }

            prevX = x;
            prevY = y;
        }

        for (int i = 0; i < 7; i++) {
            int x = margenIzq + i * separacionX;
            int y = alto - margenInf - (int) ((valorSeguro(i) / (double) maxReservas) * areaAlto);

            g2d.setColor(Color.WHITE);
            g2d.fillOval(x - 5, y - 5, 10, 10);

            g2d.setColor(EstiloUI.COLOR_COMPONENTE_CAFE);
            g2d.drawOval(x - 5, y - 5, 10, 10);

            g2d.setColor(EstiloUI.COLOR_TEXTO_OSCURO);
            g2d.setFont(new Font("SansSerif", Font.PLAIN, 10));
            g2d.drawString(dias[i], x - 10, alto - margenInf + 15);
            g2d.drawString(String.valueOf(valorSeguro(i)), x - 4, y - 10);
        }
    }

    private int valorSeguro(int i) {
        return reservasSemanales != null && i >= 0 && i < reservasSemanales.length ? reservasSemanales[i] : 0;
    }
}
