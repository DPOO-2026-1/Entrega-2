package InterfazGrafica;

import javax.swing.*;
import java.awt.*;

public class PanelGraficaBarras extends JPanel {
    // Datos de prueba (5 días, valores netos para Cafetería y Juegos)
	private double[] valoresCafeteria;
    private double[] valoresJuegos;
    private String[] etiquetasDias;

    public PanelGraficaBarras() {
    	// CAMBIO IMPLEMENTADO: valores iniciales vacíos; se actualizan desde ventas reales
        this.valoresCafeteria = new double[5];
        this.valoresJuegos = new double[5];
        this.etiquetasDias = new String[]{"D1", "D2", "D3", "D4", "D5"};

        setPreferredSize(new Dimension(430, 300));
        setBackground(EstiloUI.COLOR_FONDO_BEIGE);
    }
    
    // CAMBIO IMPLEMENTADO: setter para que PanelVisualizaciones pase datos reales
    public void actualizarDatos(double[] cafeteria, double[] juegos, String[] etiquetas) {
        this.valoresCafeteria = cafeteria != null ? cafeteria : new double[5];
        this.valoresJuegos = juegos != null ? juegos : new double[5];
        this.etiquetasDias = etiquetas != null ? etiquetas : new String[]{"D1", "D2", "D3", "D4", "D5"};
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
        int margenDer = 25;
        int areaAlto = alto - margenSup - margenInf;

        g2d.setColor(EstiloUI.COLOR_TEXTO_OSCURO);
        g2d.setFont(new Font("SansSerif", Font.BOLD, 14));
        g2d.drawString("Ventas netas últimos 5 días", margenIzq, 22);

        g2d.setFont(new Font("SansSerif", Font.PLAIN, 11));
        g2d.drawString("Sin impuestos", margenIzq, 38);

        g2d.setColor(Color.DARK_GRAY);
        
        // Ejes
        g2d.drawLine(margenIzq, alto - margenInf, ancho - margenDer, alto - margenInf); // X
        g2d.drawLine(margenIzq, alto - margenInf, margenIzq, margenSup); // Y

        double maxValor = 1.0;

        for (double v : valoresCafeteria) {
            maxValor = Math.max(maxValor, v);
        }

        for (double v : valoresJuegos) {
            maxValor = Math.max(maxValor, v);
        }

        int grupos = 5;
        int anchoGrupo = Math.max(55, (ancho - margenIzq - margenDer) / grupos);
        int anchoBarra = Math.max(12, anchoGrupo / 4);

        for (int i = 0; i < grupos; i++) {
            int xBase = margenIzq + i * anchoGrupo + anchoGrupo / 4;

            int hCafe = (int) ((valorSeguro(valoresCafeteria, i) / maxValor) * areaAlto);
            g2d.setColor(EstiloUI.COLOR_BANNER_CAFE);
            g2d.fillRect(xBase, alto - margenInf - hCafe, anchoBarra, hCafe);

            int hJuegos = (int) ((valorSeguro(valoresJuegos, i) / maxValor) * areaAlto);
            g2d.setColor(EstiloUI.COLOR_COMPONENTE_CAFE);
            g2d.fillRect(xBase + anchoBarra + 4, alto - margenInf - hJuegos, anchoBarra, hJuegos);

            g2d.setColor(EstiloUI.COLOR_TEXTO_OSCURO);
            g2d.setFont(new Font("SansSerif", Font.PLAIN, 10));
            g2d.drawString(etiquetaSegura(i), xBase - 4, alto - margenInf + 15);
        }

        int leyendaY = alto - 12;

        g2d.setColor(EstiloUI.COLOR_BANNER_CAFE);
        g2d.fillRect(margenIzq, leyendaY - 9, 10, 10);
        g2d.setColor(EstiloUI.COLOR_TEXTO_OSCURO);
        g2d.drawString("Cafetería", margenIzq + 14, leyendaY);

        g2d.setColor(EstiloUI.COLOR_COMPONENTE_CAFE);
        g2d.fillRect(margenIzq + 105, leyendaY - 9, 10, 10);
        g2d.setColor(EstiloUI.COLOR_TEXTO_OSCURO);
        g2d.drawString("Juegos", margenIzq + 119, leyendaY);
    }
    private double valorSeguro(double[] arr, int i) {
        return arr != null && i >= 0 && i < arr.length ? arr[i] : 0.0;
    }

    private String etiquetaSegura(int i) {
        return etiquetasDias != null && i >= 0 && i < etiquetasDias.length ? etiquetasDias[i] : "D" + (i + 1);
    }
}