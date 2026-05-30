package InterfazGrafica;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.*;

public class PanelOpcionesCliente extends JPanel {
    private JButton Loggearme;
    private JButton Registrarme;

    public PanelOpcionesCliente() {
        // Layout principal para separar el banner superior del contenido central
        setLayout(new BorderLayout());
        setBackground(EstiloUI.COLOR_FONDO_BEIGE);

        // 1. Banner Superior
        JPanel panelBanner = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelBanner.setBackground(EstiloUI.COLOR_BANNER_CAFE);

        JLabel textBanner = new JLabel("Gracias por elegirnos");
        textBanner.setFont(EstiloUI.FUENTE_TITULO);
        panelBanner.add(textBanner);

        add(panelBanner, BorderLayout.NORTH);

        // Usamos layouts anidados para ordenar toda la visual
        JPanel panelCentro = new JPanel(new GridBagLayout());
        panelCentro.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();

        // Usamos otro GridBackLayout para poder meter nuestras otras classes de panels
        // acá dentro
        JPanel panelIzquierdo = new JPanel(new GridBagLayout());
        // Queremos que esté transparente
        panelIzquierdo.setOpaque(false);
        GridBagConstraints gbcIzquierda = new GridBagConstraints();
        gbcIzquierda.gridx = 0; // Fixed: Explicitly initialize gridx
        gbcIzquierda.insets = new Insets(10, 5, 10, 5);
        gbcIzquierda.anchor = GridBagConstraints.WEST;

        // Etiqueta de Eres.
        gbcIzquierda.gridy = 0;
        JLabel lblEres = new JLabel("Eres...");
        lblEres.setFont(EstiloUI.FUENTE_TITULO);
        panelIzquierdo.add(lblEres, gbcIzquierda);

        // Metemos el panel de los botones en la siguiente fila
        gbcIzquierda.gridy = 1;
        JPanel panelBotones = new JPanel(new GridLayout(1, 2, 10, 0));
        panelBotones.setOpaque(false);

        Loggearme = new JButton("Loggearme");
        Registrarme = new JButton("Registrarme");

        Loggearme.setBackground(Color.WHITE);
        Registrarme.setBackground(Color.WHITE);

        panelBotones.add(Loggearme);
        panelBotones.add(Registrarme);

        panelIzquierdo.add(panelBotones, gbcIzquierda);

        // Ponemos nuestros botones en la parte izquierda del trabajo.
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 50, 0, 50);
        panelCentro.add(panelIzquierdo, gbc);

        // En el bloque derecho central metemos de nuevo la imágen que estamos usando de
        // logo.
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 0, 0); // Fixed: Reset insets so image doesn't mirror the 50px padding

        // Fixed: Load resource relative to the project structure so it works on any
        // machine
        ImageIcon iconoTaza = new ImageIcon(getClass().getResource("/InterfazGrafica/gameCafe.png"));
        JLabel labelImagen = new JLabel(iconoTaza);
        panelCentro.add(labelImagen, gbc);

        add(panelCentro, BorderLayout.CENTER);
    }

    // Usamos getters y setters para poder hacer el redigimiento con action
    // listeners en Ventana Principal
    public JButton getBtnLoggearse() {
        return Loggearme;
    }

    public JButton getRegistrarse() {
        return Registrarme;
    }

}